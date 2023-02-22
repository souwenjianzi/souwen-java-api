package cn.sowjz.souwen.v1;


import java.util.List;

import cn.sowjz.souwen.v1.conf.SouwenConfig;
import cn.sowjz.souwen.v1.conf.SpecialIdxParser;
import cn.sowjz.souwen.v1.db.struct.FieldInfo;
import cn.sowjz.souwen.v1.db.struct.FieldInfos;
import cn.sowjz.souwen.v1.doc.Doc;
import cn.sowjz.souwen.v1.net.control.AskIndex;
import cn.sowjz.souwen.v1.net.control.AskMining;
import cn.sowjz.souwen.v1.net.control.AskQuery;
import cn.sowjz.souwen.v1.net.control.BaseConn;
import cn.sowjz.souwen.v1.net.control.IndexConn;
import cn.sowjz.souwen.v1.net.control.QueryConn;
import cn.sowjz.souwen.v1.net.pool.ConnPool;
import cn.sowjz.souwen.v1.net.pool.ISearchConnPool;
import cn.sowjz.souwen.v1.net.pool.IndexConnBuilder;
import cn.sowjz.souwen.v1.net.pool.NoPool;
import cn.sowjz.souwen.v1.net.pool.QueryConnBuilder;
import cn.sowjz.souwen.v1.query.request.QueryRequest;
import cn.sowjz.souwen.v1.query.request.UpdateRequest;
import cn.sowjz.souwen.v1.query.response.ExamineResponse;
import cn.sowjz.souwen.v1.query.response.QueryResponse;
import cn.sowjz.souwen.v1.query.response.UpdateResponse;
import cn.sowjz.souwen.v1.query.response.XWord;
import cn.sowjz.souwen.v1.server.state.ServerIdxThreadStates;
import cn.sowjz.souwen.v1.util.ByteBuff;


public class SouwenClient extends BaseStructure
{
	public static boolean open_on_create=true;
	public final static String Api_Version="142";
	public final static int Isearch_Version_Support=109;
	public final static int Once_Feed_Max_Table=100;
	
	


	
	
	private ConnPool<QueryConn>  qconn_pool= null;
	private ConnPool<IndexConn>  iconn_pool= null;
	
	
	

	/**
	 * 构造方法
	 * @param cfg -- 配置信息
	 * @throws SysConnectionException
	 */
	public SouwenClient(SouwenConfig cfg) throws Exception
	{   
		
		this.cfg=cfg;
		
		init_ConnPool(cfg);
		
		if(open_on_create) open();
		
		sparser =  new SpecialIdxParser(cfg.getProps());
	}
	
	protected SouwenClient()
	{
			
	}
	
	protected void init_ConnPool(SouwenConfig cfg) throws Exception
	{
		if(cfg.getPropertyOfboolean("system.net.sock.query.pool.enable"))
			qconn_pool=new ISearchConnPool<QueryConn>(new QueryConnBuilder(),
					cfg.getPropertyOfint("system.net.sock.query.pool.maxActive"),
					cfg.getPropertyOfint("system.net.sock.query.pool.maxIdle"),
					cfg.getPropertyOflong("system.net.sock.query.pool.maxWait.ms"),
					cfg.getPropertyOfint("system.net.sock.query.pool.idleRemoveDelay.second"),
					cfg.getPropertyOflong("system.net.sock.query.pool.pulse.second")*1000
					);
		else	
			qconn_pool=new NoPool<QueryConn>(new QueryConnBuilder());
		
		qconn_pool.initialize(cfg);
		
		if(cfg.getPropertyOfboolean("system.net.sock.index.pool.enable"))
			iconn_pool=new ISearchConnPool<IndexConn>(new IndexConnBuilder(),
					cfg.getPropertyOfint("system.net.sock.index.pool.maxActive"),
					cfg.getPropertyOfint("system.net.sock.index.pool.maxIdle"),
					cfg.getPropertyOflong("system.net.sock.index.pool.maxWait.ms"),
					cfg.getPropertyOfint("system.net.sock.index.pool.idleRemoveDelay.second"),
					cfg.getPropertyOflong("system.net.sock.index.pool.pulse.second")*1000
					);
		else
			iconn_pool=new NoPool<IndexConn>(new IndexConnBuilder());
		iconn_pool.initialize(cfg);
		
	}
	/**
	 * 打开全文检索系统，如果存在会得到全文库的配置结构
	 * @throws SysConnectionException -- 当网络不通时会抛出该异常
	 */
	 protected void open() throws Exception
	{
		log.info("Opening system .");
		QueryConn conn=qconn_pool.getConn();
		try
		{
			testServerConnection(conn);
		} catch (Exception ex)
		{
			qconn_pool.releaseConn(conn,ex);
			log.error("open system error : " + ex.getMessage());
			throw ex;
		}
		readFeedInfo(conn);
		
		try
		{
			infos = descDb(conn);
		} catch (Exception ex)
		{
			qconn_pool.releaseConn(conn,ex);
			log.warn("open system warn(descDb()): " + ex.getMessage());
			// Ingore;
			return;
		}
		
		
		qconn_pool.releaseConn(conn);
		log.info("System has been opened.");
		
	}


	protected void readFeedInfo(BaseConn conn)throws Exception {
		
		feedinfo=new AskQuery(conn).feedInfo();
		
		if(feedinfo==null)
			throw new Exception("read feed info from "+cfg.getSockIp()+":"+cfg.getQuerySockPort()+" failed");
		
		FEED_BUf_MAX_LEN=feedinfo.feedBuffSize;
		if(FEED_BUf_MAX_LEN<=0)
			throw new Exception("max feed size is error");
		log.info("max feed buffer size = "+(FEED_BUf_MAX_LEN>>10)+"KB");
		
		if(feedinfo.version % 1000<Isearch_Version_Support)
			throw new Exception("The isearch version["+feedinfo.version+"] is low, this API doesnt support. the version of isearch should be or above "+Isearch_Version_Support+".");
	}


	

	
	
	
	/**
	 * 测试服务器的连接状况
	 * @throws Exception
	 */
	public void testServerConnection() throws Exception
	{
		//log.info("Test Server Query Connection.");
		QueryConn conn=qconn_pool.getConn();
		try
		{
			testServerConnection(conn);
		}catch(Exception e)
		{
			qconn_pool.releaseConn(conn,e);
			throw e;
		}
		qconn_pool.releaseConn(conn);
		
	}
	@Override
	public void testServerConnection(BaseConn conn) throws Exception
	{
		//log.info("Test Server Query Connection.");
		if(new AskQuery(conn).testConnection())
			log.info("Test Server Query Connection Successful .");
		else 
			throw new Exception("Test Server Query Connection failed! "+conn.getErrMsg());
			
	}
	/**
	 * 测试服务器加载线程的状态。
	 * @return -- 服务器的加载情况
	 * @throws Exception
	 */
	public ServerIdxThreadStates queryServerAddState() throws Exception
	{
		ServerIdxThreadStates idxStates =null;
		QueryConn conn=qconn_pool.getConn();
		try
		{
			idxStates= queryServerAddState(conn);
		}catch(Exception e)
		{
			qconn_pool.releaseConn(conn,e);
			throw e;
		}
		qconn_pool.releaseConn(conn);
	
		return idxStates;
	}
	public ServerIdxThreadStates queryServerAddState(QueryConn conn) throws Exception
	{
		//log.info("Test Server Index Thread State.");
		if (descDb() == null)
			throw new Exception("No Created DB");
		
		ServerIdxThreadStates idxStates =new AskQuery(conn).testAddIndex(infos,feedinfo.getCharset());
		if(idxStates==null)throw new Exception(conn.getErrMsg());
		log.info("Test Server Index Thread State Successful .");
		return idxStates;
	}
	/**
	 * 查询库结构
	 * @return -- 返回全文库结构
	 * @throws Exception
	 */
	public FieldInfos descDb() throws Exception
	{
		if (infos == null)
		{
			QueryConn conn=qconn_pool.getConn();
			try
			{
			infos = descDb(conn);
			}catch(Exception e)
			{
				qconn_pool.releaseConn(conn,e);
				throw e;
			}
			qconn_pool.releaseConn(conn);
			
			log.info("Query DB Struction Success.");
		}
		return infos;
	}
	public FieldInfos descDb(BaseConn conn) throws Exception
	{
		if (infos == null)
		{
			infos = new AskQuery(conn).descDb(feedinfo.getCharset());
			if(infos==null)throw new Exception(conn.getErrMsg());
			log.info("Query DB Struction Success.");
		}
		
		return infos;
	}
	/**
	 * 创建一个全文库
	 * @param infos -- 要创建的全文库的结构
	 * @throws Exception
	 */
	public void createDb(FieldInfos infos) throws Exception
	{
		IndexConn conn=iconn_pool.getConn();	
		try
		{
			createDb(conn,infos);
		}catch(Exception e)
		{
			iconn_pool.releaseConn(conn,e);
			throw e;
		}
		iconn_pool.releaseConn(conn);
		
		
	}
	
	public void createDb(IndexConn conn,FieldInfos infos) throws Exception
	{
		log.info("Create DB .");
		if (this.infos != null)
			throw new Exception("DB Exists");

		checkCreatDbFieldInfos(infos);
		new AskIndex(conn).createDb(infos,feedinfo.getCharset());
		log.info("Create DB Successful !");
		this.infos = infos;
	}
	

	/**
	 * 检测建库的字段
	 * @param infos
	 * @throws Exception
	 */
	private void checkCreatDbFieldInfos(FieldInfos infos) throws Exception
	{
		int seqnum=infos.howManySequenceField();
		if (seqnum==0)
		{
			Exception ex = new Exception("a requence field is required !");
			log.error(ex.getMessage());
			throw ex;
		}
		if (seqnum> 1)
		{
			Exception ex = new Exception("There is to many requence field !");
			log.error(ex.getMessage());
			throw ex;
		}
		int sortno = infos.getSortno();
		if (sortno < 0 || sortno >= infos.size())
			throw new Exception("the sortno is error value. ");
		FieldInfo info = infos.get(sortno);
		if (info == null)
			throw new Exception("the sortno is error value. ");
		if (!info.isInt32Field() && !info.isInt64Field())
			throw new Exception("sort field must int32 field or int64 field. ");
	}


	/**
	 * 加载文章
	 * @param doc -- 要加载的文章
	 * @throws Exception
	 */
	public void addDoc(Doc doc) throws Exception
	{
		
			IndexConn conn=iconn_pool.getConn();
			try
			{
				addDoc(conn,doc);
			}catch(Exception e)
			{
				iconn_pool.releaseConn(conn,e);
				throw e;
			}
			iconn_pool.releaseConn(conn);
			
			
		
	}
	public void addDoc(IndexConn conn,Doc doc) throws Exception
	{
	
			log.debug("Remote Add Doc .");
			ByteBuff buf = new ByteBuff();
			ByteBuff tmp = doc.toByteBuff();
			if(tmp==null) return;
			
			if(tmp.getUsed() +4 >FEED_BUf_MAX_LEN)
			{
				log.warn("size of Doc is too big (size="+(tmp.getUsed()+4)+"). please increase index.feed_buffer.size.KB of isearch");
				throw new Exception("Remote Add Doc failed !");
			}
			
			buf.append(tmp.getUsed()).append(tmp);

			boolean r=new AskIndex(conn).addDoc(buf.array(), buf.getUsed());
			if(r) log.debug("Remote Add Doc Successful !");
			else throw new Exception("Remote Add Doc failed !");
		
	}
	

	 public static  int FEED_BUf_MAX_LEN =	1<<20;
	/**
	 * 加载文章
	 * @param docs -- 要加载的文章的集合
	 * @throws Exception
	 */
	public void addDoc(List<? extends Doc> docs) throws Exception
	{
		if(docs==null ||docs.size()==0 )
			return;
		
		
			IndexConn conn=iconn_pool.getConn();
			try
			{
				addDoc(conn,docs);
			}catch(Exception e)
			{
				iconn_pool.releaseConn(conn,e);
				throw e;
			}
			iconn_pool.releaseConn(conn);
		
	}

	public void addDoc(IndexConn conn,List<? extends Doc> docs) throws Exception
	{
		int success=0;
		try
		{
			AskIndex ai=new AskIndex(conn);
			
			log.debug("Remote Add Doc List .");
			ByteBuff buf = new ByteBuff();
			
			
			for (int i = 0; i < docs.size(); i++)
			{
				ByteBuff tmp = docs.get(i).toByteBuff();
				if(tmp==null)
					continue;
				if(tmp.getUsed() +4 >FEED_BUf_MAX_LEN)
				{
					log.warn("size of Doc is too big (size="+(tmp.getUsed()+4)+"). please increase index.feed_buffer.size.KB of isearch");
					continue;
				}
				
				if(buf.getUsed()+tmp.getUsed()+4>FEED_BUf_MAX_LEN)
				{
					boolean r=ai.addDoc(buf.array(), buf.getUsed());
					if(r){ log.debug("Remote Add Doc Successful !");
					  success=i;
					}
					else throw new Exception("Remote Add Doc failed !");
					
					buf = new ByteBuff();
				}	
				buf.append(tmp.getUsed()).append(tmp);
			}
			boolean r=ai.addDoc(buf.array(), buf.getUsed());
			if(r) log.debug("Remote Add Doc Successful !");
			else throw new Exception("Remote Add Doc failed !");
		}catch(Exception e)
		{
			log.info("success="+success+" Doc list size="+docs.size());
			for(int i=0;i<success;i++)
			{
				docs.remove(0);
			}	
			throw e;
		}
	}

	
	
	
	
	






	
	

	/**
	 * 通过检索条件进行检索
	 * <p>
	 * 如果只是要统计能够检索到多少条记录。请使用<code>SearchSystem.count(request)</code>
	 * </p>
	 * @param request -- 检索的条件
	 * @return -- 检索结果集
	 * @throws Exception
	 */
	public QueryResponse query(QueryRequest request) throws Exception
	{
		QueryResponse hits =null;
		QueryConn conn=qconn_pool.getConn();
		try
		{
			hits =query(conn,request);
		}catch(Exception e)
		{
			qconn_pool.releaseConn(conn,e);
			throw e;
		}
		qconn_pool.releaseConn(conn);
		
		
		return hits;
	}
	
	public QueryResponse query(QueryConn conn,QueryRequest request) throws Exception
	{
		log.info("Query  .");
		
		if (null == this.infos)
		{
			Exception ex = new Exception("No Created DB");
			log.error(ex.getMessage());
			throw ex;
		}

		
		QueryResponse hits =new AskQuery(conn).query(request,this);
		
		if(hits==null)throw new Exception(conn.getErrMsg());
		log.info("Query  Successful !");
		return hits;
	}
	

	
	/**
	 * 通过条件统计总数
	 * <p>
	 * 只返回统计结果的条数。内容不返回。如果需要检索内容，使用<code>SearchSystem.query(request)</code>
	 * </p>
	 * @param request -- 条件
	 * @return -- 统计的总数
	 * @throws Exception
	 */
	public long count(QueryRequest request) throws Exception
	{
		long relt=0;
		QueryConn conn=qconn_pool.getConn();
		try
		{
			relt =count(conn,request);
		}catch(Exception e)
		{
			qconn_pool.releaseConn(conn,e);
			throw e;
		}
		qconn_pool.releaseConn(conn);
		
		
		return relt;
	}
	public long count(QueryConn conn,QueryRequest request) throws Exception
	{
		
		if (null == this.infos)
		{
			Exception ex = new Exception("No Created DB");
			log.error(ex.getMessage());
			throw ex;
		}
		long relt = new AskQuery(conn).count(request);
		if(relt<0)throw new Exception(conn.getErrMsg());
		log.info("Count  Successful !");
		return relt;
	}
	
	/**
	 * 通过条件删除数据
	 * @param request -- 要删除的条件
	 * @return -- 删除的条数
	 * @throws Exception
	 */
	public int delDoc(QueryRequest request) throws Exception
	{
		int v =0;
		QueryConn conn=qconn_pool.getConn();
		try
		{
			v =delDoc(conn,request);
		}catch(Exception e)
		{
			qconn_pool.releaseConn(conn,e);
			throw e;
		}
		qconn_pool.releaseConn(conn);
		
		
		return v;
	}
	public int delDoc(QueryConn conn,QueryRequest request) throws Exception
	{
		log.info("Delete Doc By QueryRequest.");
		
		if (null == this.infos)
		{
			Exception ex =new Exception("No Created DB");
			log.error(ex.getMessage());
			throw ex;
		}
		int v =new AskQuery(conn).removeDoc(request);
		if(v<0)throw new Exception(conn.getErrMsg());
		log.info("Delete Doc By QueryRequest Successful !");
		return v;
	}

	/**
	 * 提交服务器端的内存数据到全文库
	 * <p>
	 * 服务器端有缓存机制，加载的数据会先保存在服务器的缓存的中，直到缓存满了，服务器会自动地提交内存的数据到全文库。调用这个方法，如果服务器缓存中有数据，它会强制提交内存的数据到全文库，而不管缓存是否满了。
	 * </p>
	 * @throws Exception
	 */
	public synchronized void commit() throws Exception
	{
		
		IndexConn conn=iconn_pool.getConn();
		try
		{
			commit(conn);
		}catch(Exception e)
		{
			iconn_pool.releaseConn(conn,e);
			throw e;
		}
		iconn_pool.releaseConn(conn);
	
		
	}
	public synchronized void commit(IndexConn conn) throws Exception
	{
		log.info("DB Commit .");
		
		if (null == this.infos)
		{
			Exception ex =new Exception("No Created DB");
			log.error(ex.getMessage());
			throw ex;
		}
		boolean r=new AskIndex(conn).commit();
		if(!r)
			log.info("DB Commit failed !");
		else
		    log.info("DB Commit Successful !");
		
	}
	
	/**
	 * 停止服务器的运行。
	 * @throws Exception
	 */
	public void stopServer() throws Exception
	{
		log.info("Stop server .");
		QueryConn conn=qconn_pool.getConn();
		try
		{
			new AskQuery(conn).stopServer();
		}catch(Exception e)
		{
			qconn_pool.releaseConn(conn,e);
			throw e;
		}
		qconn_pool.releaseConn(conn);
		
	
	

		log.info("Stop Server Successful !");
	}


	
	
	
	
	public void emptyDB()  throws Exception{
		

		IndexConn conn=iconn_pool.getConn();
		try
		{
			emptyDB(conn);
		}
		catch(Exception e)
		{
			iconn_pool.releaseConn(conn,e);
			throw e;
		}
		iconn_pool.releaseConn(conn);
		
		
	}
	public void emptyDB(IndexConn conn)  throws Exception{
		log.info("empty server .");
		new AskIndex(conn).empty();
		log.info("empty Server Successful !");
	}
   
    
   
   
  
   
    
    public List<XWord> tokenText(String text)throws Exception
    {
    	List<XWord> lw =null;
		QueryConn conn=qconn_pool.getConn();
		try
		{
			lw =tokenText(conn,text);
		}catch(Exception e)
		{
			qconn_pool.releaseConn(conn,e);
			throw e;
		}
		qconn_pool.releaseConn(conn);
		return lw;
    }
    public List<XWord> tokenText(QueryConn conn,String text)throws Exception
    {
		List<XWord> lw = new AskMining(conn).tokenText(text,getCharset()); 
		return lw;
    }
    
    
    public void destroy()
    {
    	qconn_pool.destroy();
    	iconn_pool.destroy();
    	log.info("search api destory");
    }
    public IndexConn createIndexConn() throws Exception {
		
		return iconn_pool.getConn();
	}
	public void releaseIndexConn(IndexConn conn) throws Exception {
		iconn_pool.releaseConn(conn);
		
	}
	public QueryConn createQueryConn() throws Exception {
		 return qconn_pool.getConn();
	}
	public void releaseQueryConn(QueryConn conn) throws Exception {
		qconn_pool.releaseConn(conn);
		
	}

	public ConnPool<QueryConn> getQueryConnPool() {
		return qconn_pool;
	}
	public ConnPool<IndexConn> getIndexConnPool() {
		return iconn_pool;
	}
	
	public UpdateResponse update(UpdateRequest req,Doc doc) throws Exception
	{
		UpdateResponse hits ;
		QueryConn conn=qconn_pool.getConn();
		try
		{
			hits =update(conn,req,doc);
		}catch(Exception e)
		{
			qconn_pool.releaseConn(conn,e);
			throw e;
		}
		qconn_pool.releaseConn(conn);
		
		
		return hits;
	}
	public UpdateResponse update(QueryConn conn,UpdateRequest request,Doc doc) throws Exception
	{
		log.info("Doc Update .");
		
		if (null == this.infos)
		{
			Exception ex =new Exception("No Created DB");
			log.error(ex.getMessage());
			throw ex;
		}
		doc.verify4update();
		
		
	
		while(true)
		{	
			UpdateResponse r =new AskQuery(conn).update(request,doc,this);
		
			if(r==null)
			{	if(conn.getErrCode()!=26)
					throw new Exception(conn.getErrMsg());
				Thread.sleep(1000);
				log.info("Update Doc meet lock error, try again.");
			}
			else
			{	log.info("Update Doc Successful! Changed number is "+r);
				return r;
			}	
		}	
	}
	
	
	public UpdateResponse update(long seq_value_of_doc,Doc doc) throws Exception
	{
		UpdateRequest req = new UpdateRequest(this);
		req.createCriteria().andIn(infos.getSeqFI().getName(), new long[]{seq_value_of_doc});
		return update(req,doc);
	}
	public UpdateResponse update(QueryConn conn,long seq_value_of_doc,Doc doc) throws Exception
	{
		UpdateRequest req = new UpdateRequest(this);
		req.createCriteria().andIn(infos.getSeqFI().getName(), new long[]{seq_value_of_doc});
		return update(conn,req,doc);
	}
	
	

	


	
	public ExamineResponse examine(QueryRequest request) throws Exception
	{
		ExamineResponse hits =null;
		QueryConn conn=qconn_pool.getConn();
		try
		{
			hits =examine(conn,request);
		}catch(Exception e)
		{
			qconn_pool.releaseConn(conn,e);
			throw e;
		}
		qconn_pool.releaseConn(conn);
		
		
		return hits;
	}
	
	public ExamineResponse examine(QueryConn conn,QueryRequest request) throws Exception
	{
		log.info("Examine .");
		
		
		
		if (null == this.infos)
		{
			Exception ex =new Exception("No Created DB");
			log.error(ex.getMessage());
			throw ex;
		}
		
		ExamineResponse hits =new AskQuery(conn).examine(request,this);
		
		if(hits==null)throw new Exception(conn.getErrMsg());
		log.info("Examine Successful !");
		return hits;
	}

	
	
	
	
}
