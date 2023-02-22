
package cn.sowjz.souwen.v1.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Properties;

public class PropsLoader 
{
	 public static Properties loadFromFile(String filename)throws Exception
	 {
		 return loadFromFile(new FileInputStream(filename),null);
	 }
	 
	 public static Properties loadFromFile(InputStream ins)throws Exception
	 {
		 return loadFromFile(ins,null);
	 }
	 
	 public static Properties loadFromFile(String filename,Properties parent)throws Exception{
		 return loadFromFile(new FileInputStream(filename),parent);
	 }
     
	 
     public static Properties loadFromFile(InputStream ins ,Properties parent)throws Exception
     {
    	 ByteBuff bb=FileUtil.readToByteBuffer(ins);
 		String encoding="GBK";
 		if(CharsetUtil.isUtf8(bb))
 		{
 			encoding="UTF8";
 		}	
 		
 		
 		Properties props=new Properties();
	 	 BufferedReader in=new BufferedReader( new StringReader(bb.toString(encoding)));

		
		String line=in.readLine();
		while(line!=null)
		{
			line=line.trim();
			if(line.length()>0 && line.charAt(0)!='#')
			{
				int p=line.indexOf("=");
				if(p>0 )
				{
					if(p!=line.length()-1)
					{	
					  String k=line.substring(0,p).trim();
					  String v=line.substring(p+1).trim();
					  v=valueReplace(v,parent,props);
					  props.put(k,v);
					}else
						 props.put(line.substring(0,p),"");
				}	
			}	
			line=in.readLine();
		}	
		in.close();
		return props;
     }

	private static String valueReplace(String v, Properties props1,Properties props2) throws Exception
	{
		
		if(v.indexOf("%")<0)
			return v;
		StringBuffer strb=new StringBuffer();
		
		for(int i=0;i<v.length();i++)
		{
			char c=v.charAt(i);
			if(c=='%')
			{
				int len=0;
				for(int j=i+1;j<v.length();j++)
				{
					char cj=v.charAt(j);
					if(cj=='%')
					{
						len=j-i;break;
					}	
				}	
				if(len==0) throw new  Exception("only one %, if it should be, please double it");
				else if(len==1)
				{
					strb.append('%');
					i++;
				}else
				{
					String k=v.substring(i+1,i+len);
					String vk=null;
					if(props1!=null)
					   vk=props1.getProperty(k);
					if(vk==null && props2!=null)
						vk=props2.getProperty(k);
					if(vk==null)
						throw new Exception("no such a key defined:"+k);
					strb.append(vk);
					i+=len;
				}	
			}else
				strb.append(c);
		}	
		
		return strb.toString();
	}
	
	public static void main(String argv[])throws Exception
	{
		Properties p=loadFromFile("p1.conf");
		System.out.println(p);
		
		Properties p1=loadFromFile("p2.conf",p);
		System.out.println(p1);
	}
}
