package cn.sowjz.souwen.v1.net.control;

import java.security.MessageDigest;
import java.util.Map;
import java.util.Map.Entry;

import cn.sowjz.souwen.v1.Constants;
import cn.sowjz.souwen.v1.SouwenClient;
import cn.sowjz.souwen.v1.conf.SouwenConfig;
import cn.sowjz.souwen.v1.util.ByteBuff;

public class QueryConn extends BaseConn{

	boolean first_connect=true;
	
	Map<String,QueryResultBody>cache=null;
	
	public QueryConn(SouwenConfig cfg,Map<String,QueryResultBody>cache) 
	{
		super(cfg.getSockIp(),cfg.getQuerySockPort(),cfg.getSocketTimeOut());
		this.cache=cache;
	}
	
	@Override

	protected ByteBuff buildCommand(int opt)
	{
		ByteBuff bb =new ByteBuff();
		
		if(first_connect)
		{	
			bb.append(("BINARY /ver"+SouwenClient.Api_Version+" isearch7\n").getBytes());
			for(int i=bb.getUsed();i<Header_Len;i++)
				bb.append((byte)0);	
			first_connect=false;
		}
		else
			bb.append(Constants.LOOPACK);
		bb.append(opt);
		
		return bb;
	}

	@Override
	public byte[] runCommand(final int opt, final byte[] cmd, final int cmdlen) throws Exception {
		if(cache==null)
			return super.runCommand(opt, cmd, cmdlen);
		
		final String key;
		QueryResultBody qrb;
		boolean first=false;
		synchronized (cache) {
			key=getByteArrayMd5(cmd,cmdlen)+opt;
		//	System.out.println(key.hashCode());
			
			qrb=cache.get(key);
			if(qrb==null){
				qrb=new QueryResultBody(){

					@Override
					byte[] run_inner() throws Exception{
						
						return QueryConn.super.runCommand(opt, cmd, cmdlen);
					}

					
					@Override
					public void wait_command() {
						long end_t=System.currentTimeMillis()+timeout*1000;
						while(System.currentTimeMillis()<end_t){
							if(over)
								return;
							try {Thread.sleep(100);} catch (InterruptedException e) {}
							clean_cache();
							//System.out.println("wait::  over="+over+" end="+end_t+" end-cur="+(end_t-System.currentTimeMillis())+" "+this);
						}
						ex=new Exception(err_msgs[22]);
					}
					
				};
				cache.put(key, qrb);
				first=true;
			}
		}
		if(first){
			qrb.run_command();
			cache.remove(key);
		}	
		else
			qrb.wait_command();
		
	//	System.out.println("cache size="+cache.size());
		return qrb.getResult();
		
	}


	protected  void  clean_cache() {
		if(cache==null) return;
		synchronized (cache) {
			for(Entry<String,QueryResultBody> en : cache.entrySet()){
				if(System.currentTimeMillis()-en.getValue().time>3600000l)
				   cache.remove(en.getKey());
			}
		}
	}

	public String getByteArrayMd5(byte []bb,int len) throws Exception
	{
		byte bbr[]=new byte[len];
		for(int i=0;i<len;i++)
		{
			bbr[i]=bb[i];
		}	
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte aa[]=md.digest(bbr);
		 StringBuffer sb = new StringBuffer(32);
         String s = null;
         for (int i = 0; i < aa.length; i++)
         {
             s = Integer.toHexString((int) aa[i] & 0xff);
             if (s.length() < 2)   sb.append('0');
             sb.append(s);
         }
         String m= sb.toString();
         //System.out.println(m);
         return m;
        
	}

	public static class QueryResultBody{
		volatile boolean over=false;
		byte[] result;
		Exception ex;
		long time=System.currentTimeMillis();
		
		public void run_command() {
			try{
				result=run_inner();
			}catch(Exception e){
				ex=e;
			}
			over=true;
	//		System.out.println("over=true "+this);
		}
		public void wait_command() {
			
		}
		public byte[] getResult()throws Exception {
			if(ex!=null)throw ex;
			return result;
		}
		byte[] run_inner() throws Exception{
			return null;
		}
	}
	
}
