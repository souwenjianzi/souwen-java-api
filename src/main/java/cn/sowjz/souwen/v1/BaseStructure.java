package cn.sowjz.souwen.v1;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.sowjz.souwen.v1.conf.SouwenConfig;
import cn.sowjz.souwen.v1.conf.SpecialIdxParser;
import cn.sowjz.souwen.v1.db.struct.FieldInfos;
import cn.sowjz.souwen.v1.net.control.BaseConn;
import cn.sowjz.souwen.v1.server.state.ServerFeedInfo;

public class BaseStructure {
	protected final static Logger log = LoggerFactory.getLogger(BaseStructure.class);

	protected FieldInfos infos;
	public ServerFeedInfo feedinfo;
	/**
	 * 得到全文库的结构
	 * @return -- 全文库结构
	 */
	public FieldInfos getInfos()
	{
		return infos;
		
	}

	protected SpecialIdxParser sparser; 
	public SpecialIdxParser getSpecialIdxParser() 
	{
		return sparser;
	}
	public void setSpecialIdxParser(SpecialIdxParser sparser) {
		this.sparser = sparser;
	}
	protected  SouwenConfig cfg = null;
	/**
	 * 得到配置信息
	 * @return Cofiguration
	 */
	public SouwenConfig getCfg()
	{
		return cfg;
	}
	public String getCharset()
	{
	    	if(feedinfo==null)
	    	{   log.error("feed info has not been  inited");
	    		return "ISO-8859-1";
	    	}
	    	return feedinfo.getCharset();
	}
	public String getFieldEnumName(String fieldName,int v)
	{
		if(sparser==null) return String.valueOf(v);
		return sparser.getEnumName(fieldName, v);
	}
	public Integer getFieldEnumId(String fieldName,String v)
	{
		if(sparser==null) return null;
		return sparser.getEnumId(fieldName, v);
	}
	public void testServerConnection(BaseConn conn) throws Exception {
	
	}
}
