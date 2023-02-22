package cn.sowjz.souwen.v1.util;

import java.io.UnsupportedEncodingException;

import cn.sowjz.souwen.v1.Constants;

public class Str2Bytes_GBK extends String2Bytes 
{
	
	public Str2Bytes_GBK()
	{
		super(Constants.GBK);
	}

	@Override
	public byte[] toBytes(String txt) throws UnsupportedEncodingException
	{
		if(txt==null)
			return null;
		
		ByteBuff bb=new ByteBuff();
		
		for(int i=0;i<txt.length();i++)
		{
			String ss0=txt.substring(i, i+1);
			byte[]bs=ss0.getBytes(charset);
			String s1=new String(bs,charset);
			if(ss0.equals(s1))
				bb.append(bs);
			else
				bb.append((byte)' ');
		}	
		return bb.getBytes();
	}
	
	
	public static void main(String [] argv)throws Exception
	{
		Str2Bytes_GBK sb=new Str2Bytes_GBK();
		String b=new String(sb.toBytes(";    中新网兴国6月19日电18日?"),"GBK");
		System.out.println(b);
	}
}
