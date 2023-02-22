package cn.sowjz.souwen.v1.req_parser;

import java.util.ArrayList;
import java.util.List;

import cn.sowjz.souwen.v1.util.VConvert;

public class ArrayUtil {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static long[] toLongArray(String tx) {
		if(tx==null)
			return null;
		String ss[]=tx.split(",");
		long[] l=new long[ss.length];
		for(int i=0;i<ss.length;i++)
			l[i]=VConvert.str2Long(ss[i]);
		return l;
	}
	public static int[] toIntArray(String tx) {
		if(tx==null)
			return null;
		String ss[]=tx.split(",");
		int[] l=new int[ss.length];
		for(int i=0;i<ss.length;i++)
			l[i]=VConvert.str2Int(ss[i]);
		return l;
	}
	public static byte[] toByteArray(String tx) {
		if(tx==null)
			return null;
		String ss[]=tx.split(",");
		byte[] l=new byte[ss.length];
		for(int i=0;i<ss.length;i++)
			l[i]=VConvert.str2Byte(ss[i]);
		return l;
	}

	
	
	public static String [] split(String str,char s)
	   {
		   
		   if(str==null)return null;
		   List<String> l=new ArrayList<String>();
		   String t="";
//		   boolean in=false;
		  
		   for(int i=0;i<str.length();i++)
		   {
			   char c=str.charAt(i);
			   if(c==s)
			   {
				   l.add(t);
				   t="";
				  
				   if(i<str.length()-1)
				   {
					   char c1=str.charAt(i+1);
					   if(c1=='"')
					   {
						   int c2=str.indexOf("\",",i+2);
						   if(c2==-1)
						   {
							   char c3=str.charAt(str.length()-1);
							   if(c3=='"')
							   {
								   t=str.substring(i+1);
								   break;
							   }   
						   }else
						   {
							   t=str.substring(i+1,c2+1);
							   i=c2;
						   }   
						   
					   }	   
				   }	   
				   
			   }else
				   t+=c;
		   }	   
		   l.add(t);
		   
		   String e[]=new String[l.size()];
		   for(int i=0;i<l.size();i++)
		   {   String a=((String) l.get(i)).trim();
		       if(a.startsWith("\"") && a.endsWith("\"")&&a.length()>1)
		          a=a.substring(1, a.length()-1);
		       e[i]=a;
		   }
		   return e;
	   }

	public static int[] toIntArray(List<String> value) {
		if(value==null)
		    return null;
		int []a=new int[value.size()];
		for(int i=0;i<value.size();i++){
			a[i]=VConvert.str2Int(value.get(i));
		}
		return a;
	}
	public static long[] toLongArray(List<String> value) {
		if(value==null)
		    return null;
		long []a=new long[value.size()];
		for(int i=0;i<value.size();i++){
			a[i]=VConvert.str2Long(value.get(i));
		}
		return a;
	}
	public static byte[] toByteArray(List<String> value) {
		if(value==null)
		    return null;
		byte []a=new byte[value.size()];
		for(int i=0;i<value.size();i++){
			a[i]=VConvert.str2Byte(value.get(i));
		}
		return a;
	}

	public static byte[][] toBytesArray(List<String> value) {
		if(value==null)
		    return null;
		byte [][]a=new byte[value.size()][];
		for(int i=0;i<value.size();i++){
			String v=value.get(i);
			if(v.startsWith("0x"))
				v=v.substring(2);
			a[i]=VConvert.hexToByteArray(v);
		}
		return a;
	}
	public static byte[] hexToByteArray(String value) {
		if(value==null)
		    return null;
			
			if(value.startsWith("0x"))
				value=value.substring(2);
			return VConvert.hexToByteArray(value);
		
	}

	public static byte[] hexToByteArray(String value, int len) {
		if(value==null)
		    return null;
			
			if(value.startsWith("0x"))
				value=value.substring(2);
			return VConvert.hexToByteArray(value,len);
	}
}
