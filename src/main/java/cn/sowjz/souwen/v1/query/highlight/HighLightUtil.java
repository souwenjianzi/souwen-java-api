package cn.sowjz.souwen.v1.query.highlight;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import cn.sowjz.souwen.v1.query.response.XWord;
import cn.sowjz.souwen.v1.util.PorterStemmer;
import cn.sowjz.souwen.v1.util.SearchToken;
import cn.sowjz.souwen.v1.util.StringUtil4Common;

public class HighLightUtil
{
	public static final String splitStr = "+-()[]* ,&|~^\"\r\n";

	private Comparator< SearchKey> comp;

	PorterStemmer ps;

	public HighLightUtil()
	{
		comp = new SearchKeyComparator();
		ps = new PorterStemmer();
	}

	public String simpleRemoveHtmlTag(String str)
	{
		if (str == null)
			return str;
		StringBuffer strb = new StringBuffer(str.length());
		for (int i = 0; i < str.length(); i++)
		{
			char c = str.charAt(i);
			if (c == '<')
				strb.append('[');
			else if (c == '>')
				strb.append(']');
			else
				strb.append(c);
		}
		return strb.toString();
	}

	public int indexOf(String str, SearchKey target)
	{
		return indexOf(str,target.getKey());
	}
	public int indexOf(String str, String target)
	{
		return indexOf(str, 0, target);
	}

	public int indexOf(String str, int fromIndex, SearchKey target)
	{
		return indexOf(str,fromIndex,target.getKey());
	}
	public int indexOf(String str, int fromIndex, String target)
	{
		if (StringUtil4Common.isRegex(target))
			return indexOfRegexIgnoreCase(str, fromIndex, target);

		if (!StringUtil4Common.isEn(target))
			return StringUtil4Common.indexOfIgnoreCase(str, fromIndex, target);
		else
			return this.indexOfIgnoreMorphology(str, fromIndex, target);
	}
	
	private int indexOfRegexIgnoreCase(String str, int fromIndex, String target)
	{
		String baseKey = chg2RegexStr(target);
		String reltStr = StringUtil4Common.firstMatchStr(str.substring(fromIndex), baseKey);
		if (reltStr != null)
		{
			
			return StringUtil4Common.indexOfIgnoreCase(str, simpleRemoveHtmlTag(reltStr));
		}

		return -1;
	}
	/*
	private int indexOfRegexIgnoreCase(String str, int fromIndex, SearchKey target)
	{
		String baseKey = chg2RegexStr(target.getKey());
		String reltStr = StringUtil4Common.firstMatchStr(str.substring(fromIndex), baseKey);
		if (reltStr != null)
		{
			target.setKey(simpleRemoveHtmlTag(reltStr));
			return StringUtil4Common.indexOfIgnoreCase(str, reltStr);
		}

		return -1;
	}
*/
	public int indexOfIgnoreMorphology(String str, SearchKey target)
	{
		return indexOfIgnoreMorphology(str, 0, target);
	}
	public int indexOfIgnoreMorphology(String str, String target)
	{
		return indexOfIgnoreMorphology(str, 0, target);
	}

	public int indexOfIgnoreMorphology(String str, int fromIndex, SearchKey target)
	{
		return indexOfIgnoreMorphology(str,fromIndex,target.getKey());
	}
	public int indexOfIgnoreMorphology(String str, int fromIndex, String target)
	{
		ps.reset();
		String t = ps.stem(target).trim();
		ps.reset();
		for (int i = 0; i < str.length(); i++)
		{
			char c = str.charAt(i);
			if (StringUtil4Common.isLetter(c))
			{
				ps.add(c);
			} else
			{
				String tmp = ps.toString().trim();
				if (t.equalsIgnoreCase(ps.stem(tmp)))
				{
					//target.setKey(tmp);
					return i - t.length();
				}
				ps.reset();
			}

		}
		return -1;
	}

	public String chg2RegexStr(String str)
	{
		StringBuffer strb = new StringBuffer(str.length() << 1);
		char[] array = str.toCharArray();
		for (char c : array)
		{
			if (c == '?')
				strb.append(".{1}");
			else if (c == '*')
				strb.append(".+");
			else if (c >= 'A' && c <= 'Z')
				strb.append('[').append(c).append(Character.toLowerCase(c)).append(']');
			else if (c >= 'a' && c <= 'z')
				strb.append('[').append(Character.toUpperCase(c)).append(c).append(']');
			else
				strb.append(c);
		}
		return strb.toString();
	}

	public SearchKey[] getKeys(String text, String keyStr)
	{
		if (text == null || keyStr == null)
			return null;
		SearchKey[] relt = getKeys(keyStr);
		return sortKeys(text, relt);
	}

	private SearchKey[] getKeys(String keyStr)
	{
/*		String[] baseKeys = StringUtil4Common.split(keyStr, splitStr);
		SearchKey[] keys = new SearchKey[baseKeys.length];
		for (int i = 0; i < baseKeys.length; i++)
		{
			keys[i] = new SearchKey();
			keys[i].setKey(baseKeys[i]);
			keys[i].setFirstPos(0);
		}
		return keys;
*/		
		List<String> baseKeys = SearchToken.split(keyStr);
		if(baseKeys==null)return null;
		//System.out.println(keyStr+"------>"+baseKeys);
		
		SearchKey[] keys = new SearchKey[baseKeys.size()];
		for (int i = 0; i < baseKeys.size(); i++)
		{
			keys[i] = new SearchKey();
			keys[i].setKey((String)baseKeys.get(i));
			keys[i].setFirstPos(0);
		}
		return keys;
		
	}
	public SearchKey[] getKeys(String text, List<XWord> ws) {
		
		int len=0;
		for (int i = 0; i < ws.size(); i++)
		{
			if(!ws.get(i).isSymbol()) len++;
		}
		if(len==0)
			return null;
		SearchKey[] keys = new SearchKey[len];
		int j=0;
		for (int i = 0; i < ws.size(); i++)
		{
			XWord w=ws.get(i);
			if(w.isSymbol())
				continue;
			keys[j] = new SearchKey();
			keys[j].setKey(w.word);
			keys[j].setFirstPos(0);
			j++;
		}
		return keys;
	}
	private SearchKey[] sortKeys(String text, SearchKey[] keys)
	{
		for (int i = 0; i < keys.length; i++)
		{
			int firstPos = this.indexOf(text, keys[i]);
			keys[i].setFirstPos(firstPos);
		}

		Arrays.sort(keys, comp);

		int first = -1;
		for (int i = 0; i < keys.length; i++)
		{
			if (keys[i].getFirstPos() >= 0)
			{
				first = i;
				break;
			}
		}
		if (first == -1)
			return null;
		if (first == 0)
			return keys;

		SearchKey[] newKeys = new SearchKey[keys.length - first];
		for (int i = first; i < keys.length; i++)
			newKeys[i - first] = keys[i];

		return newKeys;
	}


}
