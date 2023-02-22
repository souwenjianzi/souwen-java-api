package cn.sowjz.souwen.v1.util;

import java.io.IOException;


public class VConvert
{
	/**
	 * convert a string to a byte
	 * @param s
	 * @return byte
	 */
	public static byte str2Byte(String s)
	{
		if (s == null || s.trim().length() == 0)
			return 0;
		if(s.startsWith("0x"))
			return Byte.parseByte(s.substring(2), 16);
		return Byte.parseByte(s);
	}

	/**
	 * convert a string to a short
	 * @param s
	 * @return short
	 */
	public static short str2Short(String s)
	{
		if (s == null || s.trim().length() == 0)
			return 0;
		if(s.startsWith("0x"))
			return Short.parseShort(s.substring(2), 16);
		return Short.parseShort(s);
	}

	/**
	 * convert a string to a int
	 * @param s
	 * @return
	 */
	public static int str2Int(String s)
	{
		if (s == null || s.trim().length() == 0)
			return 0;
		if(s.startsWith("0x"))
			return Integer.parseUnsignedInt(s.substring(2), 16);
		return Integer.parseInt(s);
	}

	/**
	 * convert a string to a long
	 * @param s
	 * @return
	 */
	public static long str2Long(String s)
	{
		if (s == null || s.trim().length() == 0)
			return 0;
		if(s.startsWith("0x"))
			return Long.parseUnsignedLong(s.substring(2), 16);
		return Long.parseLong(s);
	}

	/**
	 * convert a string a float
	 * @param s
	 * @return
	 */
	public static float str2Float(String s)
	{
		if (s == null || s.trim().length() == 0)
			return 0.0F;
		return Float.parseFloat(s);
	}

	/**
	 * convert a string to a double
	 * @param s
	 * @return
	 */
	public static double str2Double(String s)
	{
		if (s == null || s.trim().length() == 0)
			return 0;
		return Double.parseDouble(s);
	}

	/**
	 * convert a string to a boolean
	 * @param s
	 * @return
	 */
	public static boolean str2Bool(String s)
	{
		if (s == null || s.trim().length() == 0)
			return false;
		else if (s.trim().equalsIgnoreCase("true"))
			return true;
		else if (s.trim().equals("1"))
			return true;
		else
			return false;
	}

	/**
	 * convert a string to a byte array
	 * @param str
	 * @param charset
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static byte[] str2Bytes(String str, String charset) throws IOException
	{
		if (null == charset)
			return str.getBytes();
		return str.getBytes(charset);
	}

	/**
	 * join two byte arrays to one byte array
	 * @param fir
	 * @param sec
	 * @return
	 */
	public static byte[] joinBytes(byte[] fir, byte[] sec)
	{
		int firLen = fir.length;
		int secLen = sec.length;
		byte[] third = new byte[firLen + secLen];

		for (int i = 0; i < firLen; i++)
			third[i] = fir[i];
		for (int i = 0; i < secLen; i++)
			third[firLen + i] = sec[i];

		return third;
	}

	/**
	 * convert a long value to bytes array
	 * @param num
	 * @return
	 */
	public static byte[] long2Bytes(long num)
	{
		byte[] b = new byte[8];
		for (int i = 0; i < b.length; i++)
			b[i] = (byte) ((num >> (i * 8)) & 0xff);
		return b;
	}

	/**
	 * convert a long value to a boolean value
	 * @param num
	 * @return
	 */
	public static boolean long2Bool(long num)
	{
		if (num == 0)
			return false;
		return true;
	}

	/**
	 * convert a byte array to a long
	 * @param b
	 * @return
	 */
	public static long bytes2Long(byte[] b)
	{
		return bytes2Long(b, 0);
	}

	/**
	 * convert a byte array to a long
	 * @param b
	 * @param start
	 * @return
	 */
	public static long bytes2Long(byte[] b, int start)
	{
		long result = 0;
		for (int i = 0; i < 8; i++)
			result += (long) (((long) (b[start + i]) & 0xff) << (i * 8));
		return result;
	}

	/**
	 * convert a int to a byte array
	 * @param num
	 * @return
	 */
	public static byte[] int2Bytes(int num)
	{
		byte[] b = new byte[4];
		for (int i = 0; i < b.length; i++)
			b[i] = (byte) ((num >> (i * 8)) & 0xff);
		return b;
	}

	/**
	 * convert a int to a boolean value
	 * @param num
	 * @return
	 */
	public static boolean int2Bool(int num)
	{
		if (num == 0)
			return false;
		return true;
	}

	/**
	 * convert a byte array to a int
	 * @param b
	 * @return
	 */
	public static int bytes2Int(byte[] b)
	{
		return bytes2Int(b, 0);
	}

	/**
	 * convert a byte array to a int
	 * @param b
	 * @param start
	 * @return
	 */
	public static int bytes2Int(byte[] b, int start)
	{
		int result = 0;
		for (int i = 0; i < 4; i++)
			result += (long) (((int) (b[start + i]) & 0xff) << (i * 8));
		return result;
	}
	public static double bytes2double(byte[] buf, int start) {
		long value = 0;
		for (int i = 0; i < 8; i++) {
			value |= ((long) (buf[i+start] & 0xff)) << (8 * i);
		}
		return Double.longBitsToDouble(value);
	}
	

	public static boolean byte2Bool(byte v)
	{
		if (v == (byte) 0)
			return false;
		else
			return true;
	}

	public static byte bool2Byte(boolean b)
	{
		if (b == false)
			return (byte) 0;
		else
			return (byte) 1;
	}

	public static String str2Filepath(String str)
	{
		if (str == null || str.trim().length() == 0)
			return str;
		String separator = System.getProperty("file.separator");
		if (!str.endsWith("\\") && !str.endsWith("/"))
			str += separator;
		return str;
	}

	public static String str2Webpath(String str)
	{
		if (str == null || str.trim().length() == 0)
			return str;
		if (!str.endsWith("/"))
			str += "/";
		return str;
	}
	public static  String byteArrayToHex(byte[] bb)
	{
		if(bb==null)return null;
		StringBuffer strb=new StringBuffer();
		for(int i=0;i<bb.length;i++)
		{
			
	        int b=bb[i] & 0xff;    
	        if(b<16)
	        	strb.append("0");
			strb.append(Integer.toHexString(b));
		}	
		return strb.toString();
	}
	public static  String byteArrayToHex(byte[] bb,int begin,int len)
	{
		if(bb==null)return null;
		StringBuffer strb=new StringBuffer();
		for(int i=begin;i<bb.length&& i<begin+len;i++)
		{
			
	        int b=bb[i] & 0xff;    
	        if(b<16)
	        	strb.append("0");
			strb.append(Integer.toHexString(b));
		}	
		return strb.toString();
	}
	public static  byte[] hexToByteArray(String hexstr)
	{
		if(hexstr==null)return null;
		
		int len=hexstr.length();
		hexstr=hexstr.toUpperCase();
		
		byte[] bb=new byte[len/2];
		
		for(int i=0;i<len/2;i++)
		{
			char c=hexstr.charAt(i*2);
			if(c<='9')
		       bb[i]=(byte) ((c-'0')<<4);	
			else
				bb[i]=(byte) ((c-'A'+10)<<4);	
			
			 c=hexstr.charAt(i*2+1);
			 if(c<='9')
			       bb[i]+=(byte) ((c-'0'));	
				else
					bb[i]+=(byte) ((c-'A'+10));	
		}	
		return bb;
	}
	public static byte[] hexToByteArray(String hexstr, int len) {
		if(hexstr==null)return null;
		
		
		hexstr=hexstr.toUpperCase();
		while(hexstr.length()<len+len)
			hexstr="0"+hexstr;
		
		byte[] bb=new byte[len];
		
		for(int i=0;i<len;i++)
		{
			char c=hexstr.charAt(i*2);
			if(c<='9')
		       bb[i]=(byte) ((c-'0')<<4);	
			else
				bb[i]=(byte) ((c-'A'+10)<<4);	
			
			 c=hexstr.charAt(i*2+1);
			 if(c<='9')
			       bb[i]+=(byte) ((c-'0'));	
				else
					bb[i]+=(byte) ((c-'A'+10));	
		}	
		return bb;
	}

	
	 public static void main(String[] args)
	 {
	 String str = "aaaaaaa";
	 str = VConvert.byteArrayToHex(str.getBytes());
	 System.out.println(str);
     
	 byte cc[]=hexToByteArray(str);
	 System.out.println(new String(cc));
	 
	 byte bb[]=new byte[256];
	 for(int i=0;i<256;i++)
		 bb[i]=(byte) i;
	    System.out.println(byteArrayToHex(bb));
	 }


	

	
}
