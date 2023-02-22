package cn.sowjz.souwen.v1.conf;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import cn.sowjz.souwen.v1.util.PropsLoader;


public class SouwenConfig
{

	/* 核心配置文件内容 */
	Properties pros;

	/* 服务端IP地址 */
	String sockIp;

	/* 服务端查询监听端口 */
	int querySockPort;

	/* 服务端检索监听端口 */
	int indexSockPort;

	

	
	

	
	
	
	int socket_timeout;
	
	int maxtxtlen;

	

	public SouwenConfig(Properties props) throws Exception
	{
		pros = props;
		init();
	}
	public SouwenConfig(InputStream in) throws Exception
	{
		pros =  PropsLoader.loadFromFile(in);
		init();
	}
	
	protected SouwenConfig() {
		
	}
	
	/**
	 * 读取配置文件信息。
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	protected void init() throws FileNotFoundException, IOException
	{
		sockIp = pros.getProperty("ip").trim().trim().toLowerCase();
		querySockPort = Integer.valueOf(pros.getProperty("socket.query.port").trim().toLowerCase()).intValue();
		if(pros.getProperty("socket.index.port")!=null)
			indexSockPort = Integer.valueOf(pros.getProperty("socket.index.port").trim().toLowerCase()).intValue();
		if(pros.getProperty("socket.timeout.second")!=null)
		   socket_timeout=Integer.valueOf(pros.getProperty("socket.timeout.second")).intValue();
		
		if(pros.getProperty("doc.textfield.maxlength.K")!=null)
		maxtxtlen=Integer.valueOf(pros.getProperty("doc.textfield.maxlength.K")).intValue()<<10;
		if(maxtxtlen==0)
			maxtxtlen=200<<10;
	}


	


	




	/**
	 * 得到加载的配置文件
	 * @return 配置文件绝对路径
	 */
	//public String getCfgfile()
	//{
	//	return cfgfile;
	//}

	/**
	 * 得到全文库加载的通信端口
	 * @return 加载通信端口
	 */
	public int getIndexSockPort()
	{
		return indexSockPort;
	}

	/**
	 * 设置全文库加载的通信端口
	 * @param indexSockPort -- 加载的通信端口
	 */
	protected void setIndexSockPort(int indexSockPort)
	{
		this.indexSockPort = indexSockPort;
	}

	/**
	 * 得到全文库检索的通信端口
	 * @return 检索通信端口
	 */
	public int getQuerySockPort()
	{
		return querySockPort;
	}

	/**
	 * 设置全文库检索的通信端口
	 * @param querySockPort -- 检索的通信端口
	 */
	protected void setQuerySockPort(int querySockPort)
	{
		this.querySockPort = querySockPort;
	}

	/**
	 * 得到服务器端的ip
	 * @return ip地址
	 */
	public String getSockIp()
	{
		return sockIp;
	}

	/**
	 * 设置服务器端的ip
	 * @param sockIp -- ip
	 */
	protected void setSockIp(String sockIp)
	{
		this.sockIp = sockIp;
	}

	

	

	public int getSocketTimeOut()
	{
		return socket_timeout;
	}
	public int getMaxTxtLen()
	{
		return maxtxtlen;
	}

	public Properties getProps() {
	
		return pros;
	}
	public int getPropertyOfint(String key)
	{
		String v=pros.getProperty(key);
		if(v==null)
			return 0;
		v=v.trim();
		return Integer.parseInt(v);
	}
	public long getPropertyOflong(String key)
	{
		String v=pros.getProperty(key);
		if(v==null)
			return 0;
		v=v.trim();
		return Long.parseLong(v);
	}
	public boolean getPropertyOfboolean(String key)
	{
		String v=pros.getProperty(key);
		if(v==null)
			return false;
		v=v.trim();
		return Boolean.parseBoolean(v);
	}
}
