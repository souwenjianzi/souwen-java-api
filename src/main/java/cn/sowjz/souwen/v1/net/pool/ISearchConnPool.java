package cn.sowjz.souwen.v1.net.pool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.sowjz.souwen.v1.conf.SouwenConfig;
import cn.sowjz.souwen.v1.net.control.BaseConn;

public class ISearchConnPool <T extends BaseConn> implements ConnPool<T>{

	
	protected final static Logger log = LoggerFactory.getLogger(ISearchConnPool.class);
	
	int maxActive;
	int maxIdle;
	long maxWait;
	long idleRemoveDelay;
	long pulseInteval;
	ConnBuilder<T> builder;
	
	
	
	
	public ISearchConnPool(ConnBuilder<T> builder,int ma,int mi,long mw,int ird,long pi)
	{
		maxActive=ma;
		maxIdle=mi;
		maxWait=mw;
		idleRemoveDelay=ird*1000;
		if(maxActive<1) maxActive=1;
		if(maxIdle<0) maxIdle=0;
		if(maxIdle>maxActive)	maxIdle=maxActive;
		pulseInteval=pi;
		if(pulseInteval<=0)
			pulseInteval=30000;
		this.builder=builder;
		if(idleRemoveDelay<0) idleRemoveDelay=0;
	}
	
	static PulseThread pulseT=null;
	static List<ISearchConnPool<?>> all_pools=new ArrayList<ISearchConnPool<?>>();
	
	
	List<AConn> connlist=new ArrayList<AConn>();
	SouwenConfig conf;
	
	@Override
	public void initialize(SouwenConfig conf) throws Exception {
			
		synchronized (all_pools) {
			if(pulseT==null)
			{
				pulseT=new PulseThread();
				pulseT.start();
			}
			all_pools.add(this);
		}
		
		
		this.conf=conf;
		for(int i=0;i<maxIdle;i++)
			connlist.add(new AConn(builder.createConn(conf)));
		
		
	}

	@Override
	public T getConn() throws Exception {
		
		long t0=System.currentTimeMillis();
		while(System.currentTimeMillis()-t0<maxWait)
		{	
			synchronized(this)
			{
				for(AConn ac:connlist)
				{
					if(ac.actived==false)
					{
						ac.actived=true;
						ac.lastUsed=System.currentTimeMillis();
//	System.out.println("connlist.size()="+connlist.size());
						if(ac.conn.isAlive())
							return ac.conn;
						else{
							connlist.remove(ac);
							break;
						}
					}	
				}
				if(connlist.size()<maxActive)
				{	AConn ac=new AConn(builder.createConn(conf));
					connlist.add(ac);
					ac.actived=true;
		//	System.out.println("connlist.size()="+connlist.size());
					return ac.conn;
				}	
			}
			try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
		}
		return builder.createConn(conf);
	}


	@Override
	public void releaseConn(T conn) throws Exception {
		releaseConn(conn,false);
	}

	@Override
	public void releaseConn(T conn, Exception e) throws Exception {
		
		if(e!=null && ( e instanceof IOException))
		   releaseConn(conn,true);
		else
			releaseConn(conn,false);
	}

	private void releaseConn(T conn, boolean ioerr)
	{
		synchronized(this)
		{
			for(AConn ac:connlist)
			{
				if(ac.conn==conn)
				{
					if(ioerr)
					{
						conn.close();
						connlist.remove(ac);
						return;
					}	
					ac.actived=false;
					ac.lastPulse=System.currentTimeMillis();
					return;
				}	
			}
		}
		conn.close();
	}
	
	@Override
	public void destroy() {
		pulseT.working=false;
		while(!pulseT.isStop())
		{
			try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
			//System.out.println("waiting pulse stop");
		}	
		synchronized(this)
		{
			for(AConn ac:connlist)
			{
				ac.conn.close();
			}
			connlist.clear();
		}
	}
	
	@Override
	public void pulse() {
	
		long now=System.currentTimeMillis();
		synchronized(this)
		{
			int idlenum=0;
			for(int i=connlist.size()-1;i>=0;i--)
			{
				AConn ac=connlist.get(i);
				if(ac.actived==false)
				{
					idlenum++;
//					System.out.println("begin test alive for "+i+" pulseInteval="+pulseInteval+" now-ac.lastPulse="+(now-ac.lastPulse));
					
					if(now-ac.lastPulse > pulseInteval)
					{	
						//System.out.println("begin test alive for "+i);
						if(ac.conn.isAlive())
							ac.lastPulse=now;
						else
						{	ac.conn.close();
						    connlist.remove(i);
						    idlenum--;
						}
					}
				}	
			}	
			if(idlenum>maxIdle)
				for(int i=connlist.size()-1;i>=0;i--)
				{
					AConn ac=connlist.get(i);
					if(ac.actived==false && now-ac.lastUsed>idleRemoveDelay)
					{
						ac.conn.close();
					    connlist.remove(i);
					    idlenum--;
					    if(idlenum<=maxIdle)
					    	break;
					}	
				}	
		}
	}

	public String toString()
	{
		StringBuffer strb=new StringBuffer();
		strb.append("maxActive=").append(maxActive).append("\n");
		strb.append("maxIdle=").append(maxIdle).append("\n");
		strb.append("maxWait.ms=").append(maxWait).append("\n");
		strb.append("idleRemoveDelay.s=").append(idleRemoveDelay/1000).append("\n");
		strb.append("pulseInteval.s=").append(pulseInteval/1000).append("\n");
		
		for(int i=0;i<connlist.size();i++)
		{
			AConn ac=connlist.get(i);
			strb.append("conn").append(i+1).append(":").append(ac).append("\n");
		}	
		return strb.toString();
	}
	
	
	static class PulseThread extends Thread
	{
		public PulseThread()
		{
			setDaemon(true);
		}
		boolean stop=false;
		public boolean isStop(){
			return stop;
		}
		boolean working=true;
		
		@Override
		public void run() {
			 stop=false;
			while(working)
			{
//				System.out.println("PulseThread working");
				//log.debug("PulseThread working ");
				try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
				
				if(!working)break;
				
				
				for(ISearchConnPool<?> p:all_pools){
					p.pulse();
				}
				
				/*
				Iterator<SearchSystem>it= SearchSystem.getAllSystem();
				int n=0;
				while(it.hasNext())
				{
					if(!working)break;
					SearchSystem ss=it.next();
					ss.getQueryConnPool().pulse();
					//log.debug("ss"+n+".getQueryConnPool():"+ss.getQueryConnPool());
					ss.getIndexConnPool().pulse();
					//log.debug("ss"+n+".getIndexConnPool():"+ss.getIndexConnPool());
					n++;
				}	
				
				*/
			}
			
			log.debug("PulseThread quit");
			 stop=true;
		}
		
		
	}
	class AConn 
	{
		T conn;
		long lastUsed;
		long lastPulse;
		boolean actived=false;
		public AConn(T c)
		{
			this.conn=c;
			lastUsed=System.currentTimeMillis();
			lastPulse=System.currentTimeMillis();
		}
		public String toString()
		{
			return "lastUsed:"+lastUsed+",actived:"+actived;
		}
	}
	@Override
	public void setBuilder(ConnBuilder<T> builder) {
		this.builder = builder;
	}
	
}
