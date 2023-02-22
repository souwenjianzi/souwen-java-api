package cn.sowjz.souwen.v1.tools;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import cn.sowjz.souwen.v1.SouwenClient;
import cn.sowjz.souwen.v1.conf.SouwenConfig;
import cn.sowjz.souwen.v1.conf.SpecialIdxParser;
import cn.sowjz.souwen.v1.db.struct.FieldInfos;
import cn.sowjz.souwen.v1.db.struct.FieldInfosUtil;
import cn.sowjz.souwen.v1.doc.Doc;
import cn.sowjz.souwen.v1.util.FileUtil;
import cn.sowjz.souwen.v1.util.PropsLoader;
import cn.sowjz.souwen.v1.util.VConvert;




public class SSDataReader {

	FieldInfos finfos;
	String charset;
	SpecialIdxParser sparser;
	public SSDataReader(SouwenClient ss) throws Exception
	{
		finfos=ss.getInfos();
		charset=ss.feedinfo.getCharset();
		sparser=ss.getSpecialIdxParser();
	}
	
	
	public SSDataReader(FieldInfos finfos,	String charset,	SpecialIdxParser sparser )
	{
		this.finfos=finfos;
		this.charset=charset;
		this.sparser=sparser;
	}
	
	/**
	 * searchlib_config: C:\\ISearch\\db-ok\\searchlib.config
	 * charset: GBK
	 * SearchSystem_conf: ../search_api7/conf/SearchSystem.conf
	 * */
	public SSDataReader(String searchlib_config,String charset,String SearchSystem_conf ) throws Exception{
		finfos=FieldInfosUtil.loadFromFile(searchlib_config);
		this.charset=charset;
		Properties pros =PropsLoader.loadFromFile(SearchSystem_conf);
		sparser=new SpecialIdxParser(pros);
	}
	
	public FieldInfos getFieldInfos(){
		return finfos;
	}
	
	FileInputStream in;
	
	public void open(String datafn)throws Exception
	{
		in=new FileInputStream(new File(datafn));
	
	}
	public void open(File file)throws Exception
	{
		in=new FileInputStream(file);
	
	}
	
	byte []lenbb=new byte[4];
	
	public Doc next()throws Exception
	{
		if(in==null)
			return null;
		
		    int len=in.read(lenbb,0,4);
		    if(len==-1)
		    {
		    	in.close();
		    	in=null;
		    	return null;
		    }
		    if(4!=len)
		    	throw new Exception("file format error");
		    
		    int alen=VConvert.bytes2Int(lenbb);
		    
		    byte[] buff=new byte[alen];
		    int rlen=in.read(buff,0,alen);
		    if(rlen!=alen)
		    	throw new Exception("file format error");
		    
		    Doc a=new Doc(finfos,charset,sparser);
		    a.fillIn(buff,0, rlen);
		    return a;
		    
	}
	public void close()throws Exception
	{
		if(in!=null)
			in.close();
		in=null;
	}
	
	public static void main(String [] argv)throws Exception
	{
		SouwenClient ss =new  SouwenClient( new SouwenConfig(FileUtil.loadPropertiesFromFile("conf/SearchSystem.conf", "UTF8")));
		
		SSDataReader dr=new SSDataReader(ss);
		
		dr.open("C:\\isearch\\bin\\idx_2289SP\\idx_2285GR.dat");
		while(true)
		{  Doc a=dr.next();
		   if(a==null)
			   break;
	    	System.out.println(a);
		}
		dr.close();
		
		ss.destroy();
	}
}
