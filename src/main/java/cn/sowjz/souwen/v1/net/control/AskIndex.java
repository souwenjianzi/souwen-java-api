package cn.sowjz.souwen.v1.net.control;

import cn.sowjz.souwen.v1.Constants;
import cn.sowjz.souwen.v1.db.struct.FieldInfos;
import cn.sowjz.souwen.v1.util.ByteBuff;
import cn.sowjz.souwen.v1.util.VConvert;

public class AskIndex {

	BaseConn conn;
	
	public AskIndex(BaseConn conn)
	{
		this.conn=conn;
	}

	public void createDb(FieldInfos infos, String charset) throws Exception {
		ByteBuff bb=infos.toByteBuffer(charset);
				
		conn.runCommand(Constants.DB_CREATE,bb.array(),bb.length());
		
	}

	public  boolean commit()  throws Exception
	{
	//	int n=0;
		
		  while(true)
		  {  
			  int r=commit_once();
			  if(r==0)return true;
			  else if(r!=-1)return false;
		//	  n++;
		  	  Thread.sleep(1000);
			  //if(n>200)	return false;
		  }
		
	}
	private int commit_once()  throws Exception 
	{
		byte [] buf=conn.runCommand(Constants.COMMIT);
		if(buf==null)
			return conn.getErrCode();
		int r=VConvert.bytes2Int(buf);
		if(r==0)
		   return 0;
		return -1;
	}
	public void empty()throws Exception
	{
		for(int i=0;i<10;i++)
		{  int r=empty_once();
		
	       if(r==0)
		    	return;
	       else if(r!=-1)
	    	   break;
	       Thread.sleep(10000);
	    
		}  
		throw new Exception("can not empty db");
	}
	private int empty_once() throws Exception
	{
		byte [] buf=conn.runCommand(Constants.DB_EMPTY);
		if(buf==null)return conn.getErrCode();
		int r=VConvert.bytes2Int(buf);
		if(r==0) return 0;
		return -1;
	}

	public boolean addDoc(byte[] docBytes, int len)throws Exception 
	{
		if (len==0) return true;
		int interval=100;
		while (true)
		{
			int r=conn.addDocRemote_once(docBytes,0,len);
			
			if(r==0)return true;
			if(r!=-1)
				return false;
			
			interval=interval<<1;
			if(interval>1000)
			{	interval=1000;
				BaseConn.log.debug("Remote Add Doc blocked ! "+conn.getIp()+":"+conn.getPort());
			}
			Thread.sleep(interval);
		}
	}
	


	public void testConnection()throws Exception  {
		conn.runCommand(Constants.CONNECTION_TEST);		
	}
	



}
