package cn.sowjz.souwen.v1.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.sowjz.souwen.v1.query.highlight.HighLightUtil;




public class StringUtil4Common
{
	public static String data2(String s)
	{
		if (s == null)
			return null;

		MessageDigest md = null;
		try
		{
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e)
		{
			return null;
		}

		return toHex(md.digest(s.getBytes()));
	}

	public static String toHex(byte buffer[])
	{
		StringBuffer sb = new StringBuffer(32);
		String s = null;
		for (int i = 0; i < buffer.length; i++)
		{
			s = Integer.toHexString((int) buffer[i] & 0xff);
			if (s.length() < 2)
				sb.append('0');
			sb.append(s);
		}
		return sb.toString();
	}
	public static byte[]fromHex(String tx)
	{
		int len=tx.length()/2;
		byte b[]=new byte[len];
		for(int i=0;i<len;i++)
			b[i]=(byte) Integer.parseInt(tx.substring(i*2,i*2+2), 16);
		return b;
	}
	
	public static String[] split(String str, String s)
	{
		if (str == null)
			return null;

		if (s == null)
			return new String[] { str };

		StringTokenizer st = new StringTokenizer(str, s);
		String[] r = new String[st.countTokens()];
		int i = 0;
		while (st.hasMoreTokens())
			r[i++] = st.nextToken();
		return r;
	}

	public static int indexOfIgnoreCase(String str, char ch)
	{
		return indexOfIgnoreCase(str, 0, ch);
	}

	public static int indexOfIgnoreCase(String str, int fromIndex, char ch)
	{
		if (str == null || str.length() == 0)
			return -1;

		if (fromIndex >= str.length())
			return -1; // Note: fromIndex might be near -1>>>1.

		if (fromIndex < 0)
			fromIndex = 0;

		for (int i = fromIndex; i < str.length(); i++)
		{
			if (StringUtil4Common.equalsIngoreCase(ch, str.charAt(i)))
				return i;
		}
		return -1;
	}

	public static int indexOfIgnoreCase(String str, String target)
	{
		return indexOfIgnoreCase(str, 0, target);
	}

	public static int indexOfIgnoreCase(String str, int fromIndex, String target)
	{
		if (str == null || str.length() == 0 || target == null || target.length() == 0)
			return -1;

		if (fromIndex >= str.length())
			return -1;

		if (fromIndex < 0)
			fromIndex = 0;

		char first = target.charAt(0);
		int max = str.length() - target.length();

		for (int i = fromIndex; i <= max; i++)
		{
			if (!equalsIngoreCase(str.charAt(i), first))
			{
				while (++i <= max && !equalsIngoreCase(str.charAt(i), first))
				{
				}
			}
			if (i <= max)
			{
				int j = i + 1;
				int end = j + target.length() - 1;
				for (int k = 1; j < end && equalsIngoreCase(str.charAt(j), target.charAt(k)); j++, k++)
				{
				}
				if (j == end) // Found whole string.
					return i;
			}
		}

		return -1;
	}

	/**
	 * ??????????????????????????????????????????
	 * @param str
	 * @param regexStr
	 * @return
	 */
	public static String firstMatchStr(String str, String regexStr)
	{
		Pattern p = Pattern.compile(regexStr);
		Matcher m = p.matcher(str);

		if (m.find())
			return m.group();
		return null;
	}

	// public static String firstIgnoreMorphology(String str, String target)
	// {
	//		
	// }

	public static boolean equalsIngoreCase(char c1, char c2)
	{
		if (c1 == c2)
			return true;

		// If characters don't match but case may be ignored,
		// try converting both characters to uppercase.
		// If the results match, then return true
		char u1 = Character.toUpperCase(c1);
		char u2 = Character.toUpperCase(c2);
		if (u1 == u2)
			return true;
		// Unfortunately, conversion to uppercase does not work properly
		// for the Georgian alphabet, which has strange rules about case
		// conversion. So we need to make one last check before
		// exiting.
		return (Character.toLowerCase(u1) == Character.toLowerCase(u2));
	}

	public static String addMarkIgnoreCase(String str, char r, String beginMark, String endMark)
	{
		if (str == null || str.length() == 0)
			return str;
		StringBuffer strb = new StringBuffer(str.length());
		for (int i = 0; i < str.length(); i++)
		{
			char c = str.charAt(i);

			if (equalsIngoreCase(c, r))
				strb.append(beginMark).append(c).append(endMark);
			else
				strb.append(c);
		}
		return strb.toString();
	}

	public static String addMarkIgnoreCase(HighLightUtil hlu,String str, String r, String beginMark, String endMark)
	{
		if (str == null || str.length() == 0 || r == null || r.length() == 0)
			return str;

		//int p = indexOfIgnoreCase(str, r); // ???????????????????????????
		int p=hlu.indexOf(str,r);
		if (p == -1)
			return str;

		int last = 0;
		StringBuffer strb = new StringBuffer(str.length() << 1); // ????????????StringBuffer,
		// ????????? ??????1
		// ??????????????????

		while (p >= 0)
		{
			strb.append(str.substring(last, p));
			strb.append(beginMark);
			strb.append(str.substring(p, p + r.length()));
			strb.append(endMark);

			last = p + r.length();
			p = indexOfIgnoreCase(str, last, r);
		}
		return strb.append(str.substring(last)).toString();
	}

	/**
	 * replace r to t from str
	 * @param str
	 * @param r
	 * @param t
	 * @return
	 */
	public static String replace(String str, char r, char t)
	{
		if (str == null)
			return str;
		StringBuffer strb = new StringBuffer(str.length());
		for (int i = 0; i < str.length(); i++)
		{
			char c = str.charAt(i);
			if (c == r)
				c = t;
			strb.append(c);
		}
		return strb.toString();
	}

	/**
	 * replace r to t from str
	 * @param str
	 * @param r
	 * @param t
	 * @return
	 */
	public static String replace(String str, String r, String t)
	{
		if (str == null || r == null || t == null)
			return str;
		if (str.trim().length() == 0 || r.length() == 0)
			return str;
		int p = str.indexOf(r); // ???????????????????????????
		if (p == -1)
			return str;

		int last = 0;
		StringBuffer strb = new StringBuffer(str.length() << 1); // ????????????StringBuffer,
		// ????????? ??????1
		// ??????????????????

		while (p >= 0)
		{
			strb.append(str.substring(last, p));
			if (t != null)
				strb.append(t);
			last = p + r.length();
			p = str.indexOf(r, last);
		}
		return strb.append(str.substring(last)).toString();
	}

	public static boolean isEn(String target)
	{
		for (int i = 0; i < target.length(); i++)
		{
			if (!isLetter(target.charAt(i)))
				return false;
		}
		return true;
	}

	public static boolean isRegex(String str)
	{
		if ((str.indexOf('*')) != -1 || (str.indexOf('?')) != -1)
			return true;
		return false;
	}

	public static boolean isLetter(char ch)
	{
		if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'))
			return true;
		return false;
	}
	public static void main(String[] argv)throws Exception
	{
		String a="1234567890";
		System.out.println(toHex(fromHex(a)));
	}

	// public static void main(String[] args)
	// {
	// String s1 = "abc abc";
	// String s2 = "??????";
	// System.out.println(StringUtil.isEn(s1));
	// System.out.println(StringUtil.isEn(s2));
	// s1 = StringUtil.replace(s1, " ", "");
	// System.out.println(s1);
	// // String str = "123women????????????3";
	// // char ch = 'W';
	// // String t = "mEN";
	// // System.out.println(StringUtil.indexOfIgnoreCase(str, 3, ch));
	// // System.out.println(StringUtil.indexOfIgnoreCase(str, 0, t));
	// // System.out.println(StringUtil.addMarkIgnoreCase(str, ch, "<div>",
	// // "</div>"));
	// // System.out.println(StringUtil.addMarkIgnoreCase(str, t, "<div>",
	// // "</div>"));
	// }
}
