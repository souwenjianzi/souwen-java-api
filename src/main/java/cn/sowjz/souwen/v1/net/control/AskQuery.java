package cn.sowjz.souwen.v1.net.control;

import cn.sowjz.souwen.v1.Constants;
import cn.sowjz.souwen.v1.SouwenClient;
import cn.sowjz.souwen.v1.db.struct.FieldInfos;
import cn.sowjz.souwen.v1.doc.Doc;
import cn.sowjz.souwen.v1.query.request.QueryRequest;
import cn.sowjz.souwen.v1.query.request.UpdateRequest;
import cn.sowjz.souwen.v1.query.response.ExamineResponse;
import cn.sowjz.souwen.v1.query.response.QueryResponse;
import cn.sowjz.souwen.v1.query.response.UpdateResponse;
import cn.sowjz.souwen.v1.server.state.ServerFeedInfo;
import cn.sowjz.souwen.v1.server.state.ServerIdxThreadStates;
import cn.sowjz.souwen.v1.util.ByteBuff;
import cn.sowjz.souwen.v1.util.VConvert;

public class AskQuery {
	
	BaseConn conn;
	
	public AskQuery(BaseConn conn)
	{
		this.conn=conn;
	}
	/*
	public AskQuery(Configuration cfg)
	{
		super(cfg.getSockIp(),cfg.getQuerySockPort(),cfg.getSocketTimeOut());
	}
*/
	public ServerFeedInfo feedInfo()throws Exception 
	{
		
		byte [] buf=conn.runCommand(Constants.MAX_FEED_SIZE);
		if(buf==null)return null;
		
		ServerFeedInfo sfi=new ServerFeedInfo();
		sfi.byte2Me(buf,0,buf.length);
		
		return sfi;		
	}

	public boolean testConnection()throws Exception  {
		return conn.runCommand(Constants.CONNECTION_TEST)!=null;		
	}

	public FieldInfos descDb(String charset)throws Exception 
	{
		byte [] buf=conn.runCommand(Constants.DB_DESC);
		if(buf==null)return null;
		return FieldInfos.bytes2Me(buf, charset);
		
	}

	public ServerIdxThreadStates testAddIndex(FieldInfos infos,String charset) throws Exception 
	{
		byte [] buf=conn.runCommand(Constants.ADD_INDEX_TEST);
		if(buf==null)return null;
		ServerIdxThreadStates states = new ServerIdxThreadStates(infos);
		states.bytes2Me(buf, charset);
		return states;
	}



	public QueryResponse query(QueryRequest req,  SouwenClient searchSystem) throws Exception 
	{
		ByteBuff bb=req.toByteBuff();
		byte [] buf=conn.runCommand(Constants.DOC_SEARCH,bb.array(),bb.getUsed());
		if(buf==null)return null;
		QueryResponse hits = new QueryResponse(searchSystem,req);
		hits.bytes2Me(buf);
		return hits;
	}

	

	public long count(QueryRequest req) throws Exception 
	{
		ByteBuff bb=req.toByteBuff();
		byte [] buf=conn.runCommand(Constants.DOC_COUNT,bb.array(),bb.getUsed());
		if(buf==null)return -1;
		return  VConvert.bytes2Long(buf);
	}



	public int removeDoc(QueryRequest req) throws Exception 
	{
		ByteBuff bb=req.toByteBuff();
		byte [] buf=conn.runCommand(Constants.DOC_DEL,bb.array(),bb.getUsed());
		if(buf==null)return -1;
		return VConvert.bytes2Int(buf);
	}

	public void stopServer() throws Exception 
	{
		conn.runCommand(Constants.STOP_SERVER);
		
	}

	public UpdateResponse update(UpdateRequest req, Doc doc, SouwenClient searchSystem) throws Exception {
		ByteBuff bb=req.toByteBuff();
		ByteBuff ab=doc.toByteBuff4update();
		byte [] buf=conn.runCommand(Constants.UPDATE_DOC,bb.array(),bb.getUsed(),ab.array(),ab.getUsed());
		if(buf==null)return null;
		UpdateResponse r=new UpdateResponse();
		r.byte2Me(buf);
		return  r;
	}
	
	public ExamineResponse examine(QueryRequest req, SouwenClient searchSystem) throws Exception {
		ByteBuff bb=req.toByteBuff();
		byte [] buf=conn.runCommand(Constants.EXAMINE,bb.array(),bb.getUsed());
		if(buf==null)return null;
		ExamineResponse hits = new ExamineResponse(searchSystem,req);
		hits.bytes2Me(buf);
		return hits;
	}

}
