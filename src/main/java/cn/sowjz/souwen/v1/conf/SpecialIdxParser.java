package cn.sowjz.souwen.v1.conf;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.sowjz.souwen.v1.util.FileUtil;
import cn.sowjz.souwen.v1.util.StringUtil4Common;

public class SpecialIdxParser
{
	protected final static Logger log = LoggerFactory.getLogger(SpecialIdxParser.class);

	Map<String, String[]> smap;
	Map<String,Map<Integer,String[]>> enumMap=new HashMap<String,Map<Integer,String[]>>();
	Set<String> fill0Fields;
	/*
 	String fname;
	public SpecialIdxParser() throws Exception
	{
		this("SpecialIndex.conf");
	}

	public SpecialIdxParser(String fname) throws Exception
	{
		this.fname = fname;
		pros = new SysConf(fname).read();
		pros.remove("system.path");
		smap=parse();
	}
*/
	public SpecialIdxParser(Properties pros) throws Exception
	{

		smap=parse(pros);
		
		fill0Fields=fill0whenNull(pros.getProperty("special.fillZeroWhenNull.fieldnames"));
		
	}
	private Set<String> fill0whenNull(String fns) {
		if(fns==null || fns.length()==0) 
			return null;
		
		Set<String> set=new HashSet<String>();
		String ss[]=fns.split(",");
		for(String s:ss)
			set.add(s);
		return set;
	}
	
	public boolean shouldFillZeroWhenNull(String fieldName){
		if(fill0Fields==null) return false;
		return fill0Fields.contains(fieldName);
	}
	
	public Map<String, String[]> parse(Properties pros)
	{
		Map<String, String[]> map = new Hashtable<String, String[]>();
		
		
		String sifn = pros.getProperty("special.index.fieldnames");
		if(sifn==null || sifn.trim().length()==0)
			return map;
		String[] fieldnames = StringUtil4Common.split(sifn, ", ");

		for (int i = 0; i < fieldnames.length; i++)
		{
			String value = pros.getProperty("special.index." + fieldnames[i]);
			if (value == null)
				continue;
			value=replace(value,"\\t","\t");
			value=replace(value,"\\r","\r");
			value=replace(value,"\\n","\n");
			String[] values = StringUtil4Common.split(value, "[] ");
			
			map.put(fieldnames[i], values);
		}
		
		parseEnumValues(pros);
		return map;
	}
	private void parseEnumValues(Properties pros) {
		Iterator<Object> it = pros.keySet().iterator();
		while(it.hasNext())
		{
			String k=it.next().toString();
			String kn=k.toUpperCase();
			if(kn.startsWith("ENUM."))
			{
				String f=kn.substring(5);
				String v=pros.getProperty(k);
				log.info("f:"+f+" v:"+v);
				Map<Integer,String[]> vm=parseValueString(v);
				if(vm!=null)
				{ 
					System.out.println(f);
					for(Entry<Integer,String[]>et: vm.entrySet())
					{
						System.out.print(et.getKey()+":");
						for(String s:et.getValue())
							System.out.print(s+",");
						System.out.println("");
					}
					enumMap.put(f, vm);
				}
			}	
		}	
	}
	private Map<Integer, String[]> parseValueString(String v) {
		if(v==null || v.length()==0) return null;
		
		Map<Integer, String[]> map=new HashMap<Integer, String[]>();
		String vs[]=v.split(",");
		for(int i=0;i<vs.length;i++)
		{
			if(vs[i].length()>0)
			{
				String v1s[]=vs[i].split(":");
				if(v1s.length!=2)
					log.warn("unparsable:"+vs[i]);
				else{
					Integer iv=Integer.parseInt(v1s[0]);
					String []sv=v1s[1].split("\\|");
					map.put(iv, sv);
				}
			}	
		}
		
		return map;
	}
	public static String replace(String str, String r, String t)
	  { if(r==null)return str;
	    if(str==null)return str;


	    int p=str.indexOf(r);
	    int last=0;
	    if(p==-1)return str;
	    StringBuffer strb=new StringBuffer(str.length()<<1);

	    while(p>=0)
	    { strb.append(str.substring(last,p));
	      if(t!=null)strb.append(t);
	      last=p+r.length();
	      p=str.indexOf(r,last);
	    }
	    strb.append(str.substring(last));
	    return strb.toString();
	  }
	public Map<String, String[]> getSFMap()
	{
		return smap;
	}
	
	public Integer getEnumId(String fieldName,String v)
	{
		if(fieldName==null)return null;
		Map<Integer,String[]> vm=enumMap.get(fieldName.toUpperCase());
		if(vm==null)
			return null;
		for(Entry<Integer,String[]>et: vm.entrySet())
		{
			
			for(String s:et.getValue())
			{
				if(s.equals(v))
					return et.getKey();
			}
		}
		return null;
	}
	public String getEnumName(String fieldName,int v)
	{
		if(fieldName!=null)
		{	
			Map<Integer,String[]> vm=enumMap.get(fieldName.toUpperCase());
			if(vm!=null)
			{	String ss[]=vm.get(v);
				if(ss!=null && ss.length>0)
					return ss[0];
			}
		}	
		
		return String.valueOf(v);
	}
	

	public static void main(String[] args) throws Exception
	{
		Properties props = FileUtil.loadPropertiesFromFile("C:\\Users\\x230\\Desktop\\智山2\\log\\conf\\SearchSystem.conf", "UTF8");
		SpecialIdxParser parser = new SpecialIdxParser(props);
		System.out.println(parser.getEnumId("CD", "正文"));
	}
	
}
