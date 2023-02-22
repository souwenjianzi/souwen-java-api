package cn.sowjz.souwen.v1.util;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DocValueHelper {

	public static  int timeToInt32(Date date,boolean unsign)
	{
		long t=date.getTime()/1000;
		if(unsign)
		{
			if(t<0) return 0;
		}
		
		return (int) t;
	}
	public   int timeToInt32(long  value,boolean unsign)
	{
		if(unsign)
		{
			if(value<0) return 0;
		}
		
		return (int) value;
	}
	public  int timeToInt32(String value,boolean unsign)
	{
		 long l=0;
	     if(value==null) return 0;
	     try
	     { l=Long.parseLong(value);
	     }catch(Exception e){return 0;}
	    
	     return timeToInt32(l,unsign);
	}
	public  long timeTolong(String value)
	{
		 long l=0;
	     if(value==null) return 0;
	     
	     l=VConvert.str2Long(value);
	     
	     return l;
	}
	
	public static void main(String [] argv)throws Exception{
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(sdf.parse("21-01-01").getTime());
		
		int a=DocValueHelper.timeToInt32(sdf.parse("21-08-01"),true);
		System.out.println(a);
		System.out.println(sdf.format(new Date(a*1000l)));
		
	}
}
