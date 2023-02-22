package cn.sowjz.souwen.v1.util;

import java.io.UnsupportedEncodingException;

import cn.sowjz.souwen.v1.Constants;

public class String2Bytes 
{

	String charset;
	protected  String2Bytes(String cs)
	{
		charset=cs;
	}
	public static String2Bytes getInstance(String charset)
	{
		if(Constants.GBK.equalsIgnoreCase(charset))
			return new Str2Bytes_GBK();
		return new String2Bytes(charset);
	}
	public byte[] toBytes(String txt) throws UnsupportedEncodingException
	{
		if(txt==null)return null;
		return txt.getBytes(charset);
	}
	public String toStr(byte[] value,int begin,int len) throws UnsupportedEncodingException
	{
		if(value==null)return null;
		return new String(value,begin,len,charset);
	}
}
