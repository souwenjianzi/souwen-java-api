package cn.sowjz.souwen.v1.net.control;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.sowjz.souwen.v1.Constants;
import cn.sowjz.souwen.v1.SouwenClient;
import cn.sowjz.souwen.v1.net.sock.MSocket;
import cn.sowjz.souwen.v1.util.ByteBuff;
import cn.sowjz.souwen.v1.util.VConvert;

public class BaseConn {
	protected final static Logger log = LoggerFactory.getLogger(BaseConn.class);

	protected final static  int Header_Len=32;
	/*
	 * #define ERR_MEM_ALLOC			1
#define ERR_MEM_ALLOC_LOCKED	2
#define ERR_SOCKET_READ			3
#define ERR_SOCKET_WRITE		4
#define ERR_NULL				5
#define ERR_UNKNOW				6
#define ERR_FILE_NO_FIND		7
#define ERR_DB_NOT_EXIST		8			// 库不存在
#define ERR_COUNT				9		// 统计错误
#define ERR_NO_REG				10		// 没有seg
#define ERR_FILE_IO				11			// io错误
#define ERR_SQL_FORMAT			12		// SQL错误
#define ERR_DEL					13		// 删除错误
#define ERR_OPERATION_NO		14
#define ERR_API_VERSION_LOW		15
#define ERR_API_WRONG			16
#define ERR_UNSUPPORTED			17      //不支持的操作
#define ERR_ARRAY_INDEX_OUTOF_BOUNDS		18      //数组越界
#define ERR_HTTP				19		//http错误
#define	ERR_FEED_SIZE_TOO_BIG	20 // the size of  added doc is too big!";
#define ERR_FEED_CANNOT_INDEX	21  //cannot index data any more!";
	 * */
	final static String [] err_msgs=new String[]{
		"内存错误","内存加锁","socket读错误","socket写错误","Null值"
		,"未知错误","文件没有找到","库不存在","统计错误","没有注册"
		,"文件读写错误","检索格式错误","删除信息错误","未知操作指令","API版本过低"
		,"API版本不匹配","操作不被支持","数组越界","HTTP错误","加载数据太大",
		"无法继续加载数据","线程错误","计算超时","数据错误","逻辑错误",
		"加锁失败","IP禁止","表不存在"};
	
	
	String ip;
	int port;
	int timeout;
    public BaseConn(String ip,int port,int timeout)
    {
    	this.ip=ip;
    	this.port=port;
    	this.timeout=timeout;
    }

    int errcode=0;
    String errmsg=null;
    protected String cmd_protocol="souwen";
    
	protected ByteBuff buildCommand(int opt)
	{
		ByteBuff bb =new ByteBuff();
		bb.append(("BINARY /ver"+SouwenClient.Api_Version+" "+cmd_protocol+"\n").getBytes());
		for(int i=bb.getUsed();i<Header_Len;i++)
		   bb.append((byte)0);	
		
		bb.append(opt);
		
		return bb;
	}
	
	
	
	public byte[] runCommand(int opt)throws Exception 
	{
		byte [] buf = null;
		int v;
		MSocket s=mysock;
		
			
			ByteBuff bb=buildCommand(opt);
			bb.append(0);
			s.send(bb.array(),0,bb.getUsed());
			
			v = s.recvInt();
			if (v == Constants.SUCCESS)
			{
				int len = s.recvInt();
				buf = new byte[len];
				if(len>0)
				{	
				    s.recvBytes(buf, len);
				}    
				
			}
			
			
		
		if(v==Constants.SUCCESS)
		    return buf;
		
		errcode=v;
		errmsg=getErrMsg(v);
		return null;
		
	}
	
	public byte[] runCommand(int opt,byte[] cmd,int cmdlen)throws Exception 
	{
		byte [] buf = null;
		int v;
		MSocket s=mysock;
		
			
			ByteBuff bb=buildCommand(opt);
			bb.append(cmdlen);
			bb.append(cmd,0,cmdlen);
			s.send(bb.array(),0,bb.getUsed());
			
			v = s.recvInt();
			if (v == Constants.SUCCESS)
			{
				int len = s.recvInt();
				if(len>0)
				{	
			    	buf = new byte[len];
				    s.recvBytes(buf, len);
				}    
			}
			
			
		
		if(v==Constants.SUCCESS)
		    return buf;
		errcode=v;
		errmsg=getErrMsg(v);
		return null;
		
	}
	
	public byte[] runCommand(int opt,byte[] cmd,int cmdlen,
			byte[] data, int datalen)throws Exception  {
		byte [] buf = null;
		int v;
		MSocket s=mysock;
		
			
			ByteBuff bb=buildCommand(opt);
			bb.append(cmdlen+datalen+8);
			bb.append(cmdlen);
			bb.append(cmd,0,cmdlen);
			bb.append(datalen);
			bb.append(data,0,datalen);
			
			s.send(bb.array(),0,bb.getUsed());
			
			v = s.recvInt();
			if (v == Constants.SUCCESS)
			{
				int len = s.recvInt();
				if(len>0)
				{	
			    	buf = new byte[len];
				    s.recvBytes(buf, len);
				}    
			}
			
			
		
		if(v==Constants.SUCCESS)
		    return buf;
		errcode=v;
		errmsg=getErrMsg(v);
		return null;
	}
	
	private String getErrMsg(int errcode) 
	{
		
		if(errcode>0 && errcode<=err_msgs.length)
			return err_msgs[errcode-1];
		return "Unknow error";
	}
	
	
	public  int addDocRemote_once(byte[] docsBytes,int begin, int len) throws Exception{
		
		byte lenarray[]=VConvert.int2Bytes(len-begin);
		
		int v;
		MSocket s=mysock;
		
			
			ByteBuff bb=buildCommand(Constants.DOC_ADD);
			bb.append(lenarray.length);
			bb.append(lenarray);
			s.send(bb.array(),0,bb.getUsed());
			
			v = s.recvInt();
			if (v == Constants.SUCCESS)
			{
				s.send(docsBytes, begin, len);
				s.recvInt();
			}
		
			
			
			
		
		
		if(v==Constants.SUCCESS)
		    return 0;
		
		if(v==1)return -1;
		errcode=v;
		errmsg=getErrMsg(v);
		
		return v;
	}
	
	public String getErrMsg() {
		return errmsg;
	}
	protected MSocket mysock;
	public void open() throws Exception 
	{
		
		mysock=new MSocket(ip,port,timeout);
		mysock.connect();
		log.info("BaseConn open finish");
	}
	public void close()
	{
		try
		{
			mysock.send(Constants.ACK);
			
		}catch(Exception e)
		{
			log.error("sock send data failed",e);
		}
		try
		{
			mysock.close();
			mysock=null;
		}catch(Exception e)
		{
			log.error("close sock failed",e);
		}
		log.info("BaseConn close finish");
	}

    public boolean isActive()
    {
    	return mysock!=null;
    }

	public int getErrCode() {
		
		return errcode;
	}
	public boolean isError()
	{
		return errcode>0;
	}
	int getPort(){return port;}
	String getIp(){return ip;}
	
	public String toString()
	{
		return ip+":"+port;
	}
	public boolean isAlive()
	{
		try
		{
			return runCommand(Constants.CONNECTION_TEST)!=null;	
		}catch(Exception e)
		{
			System.out.println("Conn test alive meet err:"+e);
		}
		return false;
	}



	


	
	
}
