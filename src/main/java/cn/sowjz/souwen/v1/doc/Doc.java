package cn.sowjz.souwen.v1.doc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.sowjz.souwen.v1.BaseStructure;
import cn.sowjz.souwen.v1.conf.SpecialIdxParser;
import cn.sowjz.souwen.v1.db.struct.FieldInfo;
import cn.sowjz.souwen.v1.db.struct.FieldInfos;
import cn.sowjz.souwen.v1.util.ByteBuff;
import cn.sowjz.souwen.v1.util.DocUpdateChecker;
import cn.sowjz.souwen.v1.util.DocValueHelper;
import cn.sowjz.souwen.v1.util.String2Bytes;
import cn.sowjz.souwen.v1.util.TxUtil;
import cn.sowjz.souwen.v1.util.VConvert;



public class Doc
{
	
	protected FieldInfos finfos;
	String charset;
	
	String2Bytes str2bytes;
	
	protected Map<String, String> strMap; // fieldInfo.text

	protected Map<String, Long> longMap; // fieldInfo.int64

	protected Map<String, Integer> intMap; // fieldInfo.int32

	protected Map<String, byte[]> byteArrayMap; // fieldInfo.byte16

	
	private final static int STR_MAP		=1;
	private final static int LONG_MAP		=2;
	private final static int INT_MAP		=3;
	private final static int BYTEARRAY_MAP	=4;
	
	private int inWhitchMap(FieldInfo info)
	{
		switch(info.getType())
		{
		case FieldInfo.TYPE_VARCHAR_ARRAY:
		case FieldInfo.TYPE_TEXT:
		case FieldInfo.TYPE_VARCHAR:
		case FieldInfo.TYPE_CATEGORY:
		case FieldInfo.TYPE_CLOB:
			return STR_MAP;
		case FieldInfo.TYPE_BYTE16:
		case FieldInfo.TYPE_BINARY:
			return BYTEARRAY_MAP;
		case FieldInfo.TYPE_INT64:
		case FieldInfo.TYPE_SEQUENCE:
			return LONG_MAP;
		case FieldInfo.TYPE_INT32:
		case FieldInfo.TYPE_BYTE:
		case FieldInfo.TYPE_SHORT:		
		case FieldInfo.TYPE_INT24:
		case FieldInfo.TYPE_BIT:
		case FieldInfo.TYPE_BIT2:
		case FieldInfo.TYPE_BIT4:
		   return INT_MAP;
		}
		return 0;
	}
	protected final static Logger log = LoggerFactory.getLogger(Doc.class);
	

	
	int maxTxtlen=204800;
	/**
	 * 构造方法
	 * @param ss -- 检索系统实例
	 */
	public Doc(BaseStructure ss)
	{
		if (null == ss.getInfos())
			throw new java.lang.IllegalArgumentException("the System FieldInofs is null. please check the full db is exist ?");
		finfos=ss.getInfos();
		charset=ss.feedinfo.getCharset();
		str2bytes=String2Bytes.getInstance(charset);
		
		strMap = new Hashtable<String, String>();
		longMap = new Hashtable<String, Long>();
		intMap = new Hashtable<String, Integer>();
		byteArrayMap=new  Hashtable<String, byte[]>();
		sparser=ss.getSpecialIdxParser();
		maxTxtlen=ss.getCfg().getMaxTxtLen();
	}
	SpecialIdxParser sparser;
	
	public Doc(FieldInfos fis,String charset,SpecialIdxParser sp)
	{
		finfos=fis;
		this.charset=charset;
		str2bytes=String2Bytes.getInstance(charset);
		sparser=sp;
		strMap = new Hashtable<String, String>();
		longMap = new Hashtable<String, Long>();
		intMap = new Hashtable<String, Integer>();
		byteArrayMap=new  Hashtable<String, byte[]>();
	}

	public boolean hasValue(FieldInfo info)
	{
		switch(info.getType())
		{
		case FieldInfo.TYPE_VARCHAR_ARRAY:
		case FieldInfo.TYPE_TEXT:
		case FieldInfo.TYPE_VARCHAR:
		case FieldInfo.TYPE_CATEGORY:
		case FieldInfo.TYPE_CLOB:
			return strMap.containsKey(info.getName());
		case FieldInfo.TYPE_BYTE16:
		case FieldInfo.TYPE_BINARY:
			return byteArrayMap.containsKey(info.getName());
		case FieldInfo.TYPE_INT64:
		case FieldInfo.TYPE_SEQUENCE:
			return longMap.containsKey(info.getName());
		case FieldInfo.TYPE_INT32:
		case FieldInfo.TYPE_BYTE:
		case FieldInfo.TYPE_SHORT:		
		case FieldInfo.TYPE_INT24:		
		case FieldInfo.TYPE_BIT:
		case FieldInfo.TYPE_BIT2:
		case FieldInfo.TYPE_BIT4:
			 return intMap.containsKey(info.getName());
		}
		
		return false;
	}
	
	public int  fillIn(byte[] buf,int start,int len) throws Exception
	{
		int begin=start;
		
		while (len > 0)
		{
			String fieldname = new String(buf, start, 2, charset);
		
		
				
				start += 2;
				len -= 2;
				FieldInfo info = finfos.find(fieldname);
				if(info==null)
					throw new Exception("Unkown field:"+fieldname+" for isearch("+finfos.getAllNames()+")");
				switch(info.getType())
				{
				case FieldInfo.TYPE_VARCHAR_ARRAY:
				case FieldInfo.TYPE_TEXT:
				case FieldInfo.TYPE_VARCHAR:
				case FieldInfo.TYPE_CATEGORY:
				case FieldInfo.TYPE_CLOB:
				{ // 字符串内容 or 分类内容
					String v = null;

					int t_len = VConvert.bytes2Int(buf, start);
					if (t_len == 0)
						v = "";
					else
						v = new String(buf, start + 4, t_len - 1, charset);

					setValue(fieldname, v);
					start += (t_len + 4);
					len -= (t_len + 4);
					break;
				}
				case FieldInfo.TYPE_BYTE16:
				{
					byte [] v=new byte[16];
					for(int i=0;i<16;i++)
						v[i]=buf[i+start];
					setValue(fieldname, v);
					start += 16;
					len -= 16;
				}	break;
				case FieldInfo.TYPE_BINARY:
				{
					int t_len = VConvert.bytes2Int(buf, start);
					if (t_len != 0)
					{	byte v[]=new byte[t_len];
					    for(int i=0;i<t_len;i++)
						v[i] = buf[ start + 4+i];
					    setValue(fieldname, v);
					}	
					start += (t_len + 4);
					len -= (t_len + 4);
				}
				break;
				case FieldInfo.TYPE_INT64:
				case FieldInfo.TYPE_SEQUENCE:
				{
					long id = VConvert.bytes2Long(buf, start);
					start += 8;
					len -= 8;
					setValue(fieldname,id);
					break;
				}
				case FieldInfo.TYPE_INT32:
				case FieldInfo.TYPE_INT24:
				case FieldInfo.TYPE_BYTE:
				case FieldInfo.TYPE_SHORT:		
				{ // 4个字节内容
					int v = (VConvert.bytes2Int(buf, start));
					setValue(fieldname, v);
					start += 4;
					len -= 4;
					break;
				}
				case FieldInfo.TYPE_BIT:
				case FieldInfo.TYPE_BIT2:
				case FieldInfo.TYPE_BIT4:
				{ // 4个字节内容
					byte v = buf[ start];
					setValue(fieldname, v);
					start += 1;
					len -= 1;
				}break;
				default:
					throw new Exception("the byte format error! field: " + fieldname + " not exist !");
				}		
				
				
				
			
		}
		removeSpecialstr();
		return start-begin;
	}
	
	private void removeSpecialstr() throws Exception
	{
		
		Map<String, String[]> map = sparser.getSFMap();
		for (Iterator<String> iter = map.keySet().iterator(); iter.hasNext();)
		{
			String fieldname = iter.next();
			String[] specialValues = map.get(fieldname);

			StringBuffer strb = new StringBuffer();
			for (String v : specialValues)
			{
				FieldInfo info = finfos.find(v);
				if (null == info)
				{
					strb.append(v);
				} else if (inWhitchMap(info)==STR_MAP)
				{
					if (!v.equals(fieldname))
					{
						strb.append(getAsString(v));
					} else 
					{
						String str = getAsString(fieldname);
						if(str!=null && str.length()>0)
						{	
							String specialstr = strb.toString();

							int p = str.indexOf(specialstr);
							if (p >= 0)
								str = str.substring(p + specialstr.length());

							setValue(fieldname, str);
						}
					}
				}
			}
		}
	}
	


	/**
	 * 得到字段的内容
	 * @param fieldName -- 字段名
	 * @return 字段内容
	 */
	public String getAsString(String fieldName)
	{
		FieldInfo fi=finfos.find(fieldName);
		
		if(fi==null){ 
			
			return null;
		}
		if(fi.isInt64Field()){
			Long v = longMap.get(fieldName);
			if(v==null) return null;
			return v.toString();
		}
		
		if( fi.isIntField()||fi.isBitField())
		{
			Integer v = intMap.get(fieldName);
			if (v != null)
				return sparser.getEnumName(fieldName, v);
			return null;
		}	
		if(fi.isByte16Field()){
			return getAsByteArrayOfHexStr(fieldName);
		}
		String v = strMap.get(fieldName);
		
		return v;
	}

	public byte[]getAsbyteArray(String fieldName)
	{
		byte[] v = byteArrayMap.get(fieldName);
		if (v == null){
			if(fieldName==null || fieldName.length()!=4)
				return null;
			if(fieldName.charAt(2)=='0')
				return byteArrayMap.get(fieldName);
		}	
		return v;
	}
	public String getAsByteArrayOfHexStr(String fieldName)
	{
		byte[] v = getAsbyteArray(fieldName);
		
		return VConvert.byteArrayToHex(v);
	}
	/**
	 * 得到字段名对应的值
	 * @param fieldname -- 字段名
	 * @return 字段名对应的值
	 */
	public int getAsint(String fieldname)
	{
		Integer v = intMap.get(fieldname);
		if (v != null)
			return v;
		return 0;
	}
	public Integer getAsInteger(String fieldname){
		return intMap.get(fieldname);
	}
	public byte getAsbyte(String fieldname)
	{
		Integer v = intMap.get(fieldname);
		if (v != null)
			return(byte) v.byteValue();
		return (byte)0;
	}
	/**
	 * 得到字段名对应的值
	 * @param fieldname -- 字段名
	 * @return 字段名对应的值
	 */
	public long getAslong(String fieldname)
	{
		FieldInfo fi=finfos.find(fieldname);
		if(fi!=null && fi.isInt32TimeField())
		{
			return getAsint(fieldname);
		}	
		Long v = longMap.get(fieldname);
		if (v != null)
			return v;
		return 0;
	}
	public Long getAsLong(String fieldname)
	{
		FieldInfo fi=finfos.find(fieldname);
		if(fi!=null && fi.isInt32TimeField())
		{
			return (long)getAsint(fieldname);
		}	
		return longMap.get(fieldname);
	}
	public Date getAsDate(String fieldname)
	{
		FieldInfo info = finfos.find(fieldname);
		if(info.isInt32Field())
		{
			Integer v = intMap.get(fieldname);
			if (v != null)
				return new Date(v.longValue()*1000);
		}	
		else if(info.isInt64Field())
		{
			Long v = longMap.get(fieldname);
			if (v != null)
				return new Date(v.longValue());
		}	
		return null;
	}
	/**
	 * 设置字段的内容。如果该字段已经有内容，新的内容会覆盖原始内容
	 * @param fieldname -- 字段名
	 * @param value -- 字段内容
	 */
	public void setValue(String fieldname, String value)
	{
		if(value==null) return;
		
		
		FieldInfo info = finfos.find(fieldname);
		if (null == info)
			throw new IllegalArgumentException("no field named : " + fieldname);
		int m=inWhitchMap(info);
		if(m==INT_MAP)
		{
			Integer id=sparser.getEnumId(fieldname, value);
			if(id!=null)
			{	
				setValue(fieldname,id.intValue());
				return ;
			}else{
				setValue(fieldname,Integer.parseInt(value));
				return ;
			}
			//throw new IllegalArgumentException("unknown value["+value+"] for field  " + fieldname );
		}	
		if (m!=STR_MAP )
			throw new IllegalArgumentException("should be a String field, but field named : " + fieldname + " is not.");
		
			// value = Convert.replace(value, "", "");
			strMap.put(fieldname, value);
		
	}
	public void setValue(String fieldname, String[] values)
	{
		if(values==null) return;
		
		
		FieldInfo info = finfos.find(fieldname);
		if (null == info)
			throw new IllegalArgumentException("no field named : " + fieldname);
		
		StringBuffer strb=new StringBuffer();
		switch(info.getType())
		{
			case FieldInfo.TYPE_VARCHAR_ARRAY:
				for(int i=0;i<values.length;i++) {
					if(i>0)
						strb.append("\t");
					strb.append(values[i]);
				}
					break;
			case FieldInfo.TYPE_CATEGORY:
				for(int i=0;i<values.length;i++) {
					if(i>0)
						strb.append(",");
					strb.append(values[i]);
				}
					break;
			default:	
				throw new IllegalArgumentException("should be a CATEGORY field or a VARCHAR_ARRAY field, but field named : " + fieldname + " is not.");
		}	
		
	
			strMap.put(fieldname, strb.toString());
		
	}

	public void setValue(String fieldname,Date value)
	{
		FieldInfo info = finfos.find(fieldname);
		if (null == info)
			throw new IllegalArgumentException("no field named : " + fieldname);
			
		if (info.isTime() && info.isInt64Field())
			longMap.put(fieldname, value.getTime());
		else if(info.isTime() && info.isInt32Field())
		   intMap.put(fieldname, DocValueHelper.timeToInt32(value, info.isUnsign()));
		else
			throw new IllegalArgumentException("should be int64 or int32 field with datetime=true, but field named : " + fieldname + " is not.");
		
	}
	/**
	 * 设置字段的内容。如果该字段已经有内容，新的内容会覆盖原始内容
	 * @param fieldname -- 字段名
	 * @param value -- 字段内容
	 */
	public void setValue(String fieldname, long value)
	{
		FieldInfo info = finfos.find(fieldname);
		if (null == info)
			throw new IllegalArgumentException("no field named : " + fieldname);
		if (inWhitchMap(info)==LONG_MAP)
     		longMap.put(fieldname, value);
		else
		  throw new IllegalArgumentException("should be a long field, but field named : " + fieldname + " is not.");
	}

	/**
	 * 设置字段的内容。如果该字段已经有内容，新的内容会覆盖原始内容
	 * @param fieldname -- 字段名
	 * @param value -- 字段内容
	 */
	public void setValue(String fieldname, int value)
	{
		FieldInfo info =finfos.find(fieldname);
		if (null == info)
			throw new IllegalArgumentException("no field named : " + fieldname);
		if (inWhitchMap(info)==INT_MAP)
		  intMap.put(fieldname, value);
		else  if (inWhitchMap(info)==LONG_MAP)
     		longMap.put(fieldname,(long) value);
		else
		  throw new IllegalArgumentException("should be int field, but field named : " + fieldname + " is not.");
	}
	public void setValue(String fieldname, byte value)
	{
		FieldInfo info =finfos.find(fieldname);
		if (null == info)
			throw new IllegalArgumentException("no field named : " + fieldname);
		if (inWhitchMap(info)==INT_MAP)
		   intMap.put(fieldname,new Integer( value));
		else if (inWhitchMap(info)==LONG_MAP)
	     		longMap.put(fieldname,(long) value);
		else
		  throw new IllegalArgumentException("should be int field, but field named : " + fieldname + " is not.");
		
	}

	public void setValue(String fieldname,byte[]value)
	{
		
		if(value==null)return ;
		
		if(fieldname.endsWith("02"))
		{
			byteArrayMap.put(fieldname, value);
			return;
		}
		FieldInfo info = finfos.find(fieldname);
		if (null == info)
			throw new IllegalArgumentException("no field named : " + fieldname);
		if (info.isByte16Field() )
		{ if(value.length!=16 )
			throw new IllegalArgumentException("the value of field : " + fieldname + " is not a byte16 value.");
	      byteArrayMap.put(fieldname, value);
	      return;
		}
		if(info.isBinaryField())
		{
			byteArrayMap.put(fieldname, value);
			return;
			
		}	
		throw new IllegalArgumentException("should be byte16 field, but field named : " + fieldname + " is not.");
		
	}
	/**
	 * 得到所有的字段名
	 * @return 所有的字段名
	 */
	public String[] getFieldnames()
	{
		int size = strMap.size() + intMap.size() + longMap.size()+ byteArrayMap.size();
		String[] names = new String[size];

		int i = 0;
		for (Iterator<String> iter = strMap.keySet().iterator(); iter.hasNext(); i++)
			names[i] = iter.next();

		for (Iterator<String> iter = intMap.keySet().iterator(); iter.hasNext(); i++)
			names[i] = iter.next();

		for (Iterator<String> iter = longMap.keySet().iterator(); iter.hasNext(); i++)
			names[i] = iter.next();

		for (Iterator<String> iter = byteArrayMap.keySet().iterator(); iter.hasNext(); i++)
			names[i] = iter.next();

		return names;
	}

	/**
	 * 把文章转化成为字节形式。格式是加载时的格式。
	 * @return -- 文章的字节表示形式
	 * @throws Exception -- 转化出错抛出该异常
	 */
	public ByteBuff toByteBuff() throws Exception
	{

		Map<String, String[]> specialIdxs =sparser.getSFMap();
		ByteBuff bbuf = new ByteBuff();

		List<FieldInfo>infos= finfos.getInfos();
		for(FieldInfo fi:infos)
		{
			String fn=fi.getName();
			switch(fi.getType())
			{
			case  FieldInfo.TYPE_INT32:
			case  FieldInfo.TYPE_INT24:
			case  FieldInfo.TYPE_BYTE:
			case  FieldInfo.TYPE_SHORT:
			{
				Integer v = intMap.get(fn);
				if (v != null)
				{
					bbuf.append(fn, charset);
					bbuf.append(v);
				}else if(sparser.shouldFillZeroWhenNull(fn)){
					bbuf.append(fn, charset);
					bbuf.append((int)0);
				}	
				break;
			}
			case  FieldInfo.TYPE_INT64:
			{
				Long v = longMap.get(fn);
				if (v != null)
				{
					bbuf.append(fn, charset);
					bbuf.append(v);
				}	else if(sparser.shouldFillZeroWhenNull(fn)){
					bbuf.append(fn, charset);
					bbuf.append((long)0);
				}	
				break;
			}
			case  FieldInfo.TYPE_SEQUENCE:
			{
				if(fi.getFlag()==0)
					break;
				Long v = longMap.get(fn);
				if (v != null)
				{
					bbuf.append(fn, charset);
					bbuf.append(v);
				}else{
					log.warn(" the external value of sequence field is undefined, this doc will be skiped!");
					return null;
				}	
				break;
			}	
			case  FieldInfo.TYPE_BYTE16:
			{
				byte[] v = byteArrayMap.get(fn);
				if (v != null)
				{
					bbuf.append(fn, charset);
					bbuf.append(v);
				}	else if(sparser.shouldFillZeroWhenNull(fn)){
					bbuf.append(fn, charset);
					v=new byte[16];
					bbuf.append(v);
				}	
				break;
			}	
			case  FieldInfo.TYPE_BINARY:
			{
				byte []bb=getAsbyteArray(fn);
				if(bb!=null)
				{
				  bbuf.append(fn, charset);
				  bbuf.append(bb.length);
				  bbuf.append(bb);
				}  
				break;
			}
			case  FieldInfo.TYPE_BIT:
			case  FieldInfo.TYPE_BIT2:
			case  FieldInfo.TYPE_BIT4:
			{
				Integer v = intMap.get(fn);
				if (v != null)
				{	
					 bbuf.append(fn, charset);
					 bbuf.append((byte) v.byteValue());
				}else if(sparser.shouldFillZeroWhenNull(fn)){
					bbuf.append(fn, charset);
					bbuf.append((byte)0);
				}	
				break;
			}
			case  FieldInfo.TYPE_TEXT:
			case  FieldInfo.TYPE_VARCHAR:
			case  FieldInfo.TYPE_CATEGORY:
			case  FieldInfo.TYPE_VARCHAR_ARRAY:	
			case  FieldInfo.TYPE_CLOB:
			{
				String v = null;
				if (!specialIdxs.containsKey(fn))
				{ // 如果该字段不是特殊的
					v = getAsString(fn);
				} else
				{ // 是特殊的
					String[] specialValues = specialIdxs.get(fn); // 得到特殊的字符规则
					if((fi.getType()==FieldInfo.TYPE_TEXT) && (fi.getFlag() & 2)!=0)
						 v = getSpecialIdxValue4html(specialValues);
					else	
						v = getSpecialIdxValue(specialValues);
				}

				if (null != v && 0 != v.trim().length())
				{
					if(v.length()>maxTxtlen)
						v=v.substring(0, maxTxtlen);
					
					bbuf.append(fn, charset);
					 
					if(fi.getType()==FieldInfo.TYPE_TEXT )
						v=TxUtil.visibleChars(v);
					
					byte[] buf =str2bytes.toBytes(v);
					bbuf.append(buf.length + 1).append(buf, 0, buf.length).append((byte) 0);
				}
				break;
			}
			
	
				default: throw new Exception("unkown type for field "+fi.getName());
			}
		}	
		
	
		return bbuf;
	}

	public ByteBuff toByteBuff4update() throws Exception
	{

		Map<String, String[]> specialIdxs =sparser.getSFMap();
		ByteBuff bbuf = new ByteBuff();

		List<FieldInfo>infos= finfos.getInfos();
		for(FieldInfo fi:infos)
		{
			String fn=fi.getName();
			switch(fi.getType())
			{
			case  FieldInfo.TYPE_INT32:
			case  FieldInfo.TYPE_INT24:
			case  FieldInfo.TYPE_BYTE:
			case  FieldInfo.TYPE_SHORT:
			{
				Integer v = intMap.get(fn);
				if (v != null)
				{
					bbuf.append(fn, charset);
					bbuf.append(v);
				}	
				break;
			}
			case  FieldInfo.TYPE_INT64:
			{
				Long v = longMap.get(fn);
				if (v != null)
				{
					bbuf.append(fn, charset);
					bbuf.append(v);
				}	
				break;
			}
			case  FieldInfo.TYPE_SEQUENCE:
			{
				if(fi.getFlag()==0)
					break;
				Long v = longMap.get(fn);
				if (v != null)
				{
					bbuf.append(fn, charset);
					bbuf.append(v);
				}
//				else
//					throw new Exception(" the external value of sequence field is undefined");
				break;
			}	
			case  FieldInfo.TYPE_BYTE16:
			{
				byte[] v = byteArrayMap.get(fn);
				if (v != null)
				{
					bbuf.append(fn, charset);
					bbuf.append(v);
				}	
				break;
			}	
			case  FieldInfo.TYPE_BINARY:
			{
				byte []bb=getAsbyteArray(fn);
				if(bb!=null)
				{
				  bbuf.append(fn, charset);
				  bbuf.append(bb.length);
				  bbuf.append(bb);
				}  
				break;
			}
			case  FieldInfo.TYPE_BIT:
			case  FieldInfo.TYPE_BIT2:
			case  FieldInfo.TYPE_BIT4:
			{
				Integer v = intMap.get(fn);
				if (v != null)
				{	
					 bbuf.append(fn, charset);
					 bbuf.append((byte) v.byteValue());
				}
				break;
			}
			case  FieldInfo.TYPE_TEXT:
			case  FieldInfo.TYPE_VARCHAR:
			case  FieldInfo.TYPE_CATEGORY:
			case  FieldInfo.TYPE_VARCHAR_ARRAY:	
			case  FieldInfo.TYPE_CLOB:
			{
				String v = null;
				if (!specialIdxs.containsKey(fn))
				{ // 如果该字段不是特殊的
					v = getAsString(fn);
				} else
				{ // 是特殊的
					String[] specialValues = specialIdxs.get(fn); // 得到特殊的字符规则
					if((fi.getType()==FieldInfo.TYPE_TEXT) && (fi.getFlag() & 2)!=0)
						 v = getSpecialIdxValue4html(specialValues);
					else	
					  v = getSpecialIdxValue(specialValues);
				}

				if (null != v && 0 != v.trim().length())
				{
					if(v.length()>maxTxtlen)
						v=v.substring(0, maxTxtlen);
					
					bbuf.append(fn, charset);
					 
					byte[] buf =str2bytes.toBytes(v);
					bbuf.append(buf.length + 1).append(buf, 0, buf.length).append((byte) 0);
				}
				break;
			}
			
	
				default: throw new Exception("unkown type for field "+fi.getName());
			}
		}	
		
	
		return bbuf;
	}

	private String getSpecialIdxValue4html(String[] specialValues) {
		StringBuffer sb = new StringBuffer();
		for (String v : specialValues)
		{
			FieldInfo info = finfos.find(v);
			if (info == null)
				sb.append(v);
			else if (inWhitchMap(info)==STR_MAP)
			{	
				String fv=getAsString(v);
				if(( info.getType()==FieldInfo.TYPE_TEXT) && (info.getFlag() & 2)==0)
				   fv=text2Html(fv);	
				if(fv!=null)
				   sb.append(fv);		
			}
		}
		return sb.toString();
	}

	private String text2Html(String v) {
		if(v==null) return v;
		
		StringBuffer sb=new StringBuffer();			
		if(v!=null)
		for(int i=0;i<v.length();i++)
		{
			char c=v.charAt(i);
			switch (c)
			{
			   case '&' : sb.append("&amp;");break;
			   case '<' : sb.append("&lt;");break;
			   case '>' : sb.append("&gt;");break;
			   default : sb.append(c);
			}
		}	
		return sb.toString();
	}

	private String getSpecialIdxValue(String[] specialValues)
	{
		StringBuffer sb = new StringBuffer();
		for (String v : specialValues)
		{
			FieldInfo info = finfos.find(v);
			if (info == null)
				sb.append(v);
			else if (inWhitchMap(info)==STR_MAP)
			{	
				String fv=getAsString(v);
				if(fv!=null)
				   sb.append(fv);		
			}
		}
		return sb.toString();
	}

	@Override
	public String toString()
	{
		SimpleDateFormat sdf=new SimpleDateFormat("yy-MM-dd hh:mm:ss.SSS");
		StringBuffer strb = new StringBuffer();

		String[] fieldnames = this.getFieldnames();
		for (int i = 0; i < fieldnames.length; i++)
		{
			String fn = fieldnames[i];
			strb.append(fn).append(" : ");
			FieldInfo info = finfos.find(fn);
			if(info.isTime()){
				strb.append(sdf.format(getAsDate(fn)));	
			}	
			else if (inWhitchMap(info)==INT_MAP)
				strb.append(this.getAsint(fn));
			else if (inWhitchMap(info)==LONG_MAP)
				strb.append(this.getAslong(fn));
			else if (inWhitchMap(info)==STR_MAP)
			{	String tx=this.getAsString(fn);
				if(tx.length()>1024)
				 strb.append(tx.substring(0, 1024)).append("...");
				else 
					strb.append(tx);
			}
			else if (inWhitchMap(info)==BYTEARRAY_MAP)
			  strb.append(this.getAsByteArrayOfHexStr(fn));
			else	
				strb.append("字段不显示。");

			strb.append("\n");
		}

		return strb.toString();
	}


	public void verify4update() throws Exception {

		DocUpdateChecker checker = new DocUpdateChecker();
		if(!checker.canQuickUpdate(this))
			throw new Exception(checker.getMessage());
		
	}
	
	public Long getSequenceFieldValue(){
		return longMap.get(finfos.getSeqFI().getName());
	}
	
	
	public FieldInfos getFieldsInfos() {
		return finfos;
	}

	public void copy(Doc one){

		List<FieldInfo>infos= finfos.getInfos();
		for(FieldInfo fi:infos)
		{
			String fn=fi.getName();
			switch(fi.getType())
			{
			case  FieldInfo.TYPE_INT32:
			case  FieldInfo.TYPE_INT24:
			case  FieldInfo.TYPE_BYTE:
			case  FieldInfo.TYPE_SHORT:
			{
				Integer v = one.intMap.get(fn);
				if (v != null)
				{
					intMap.put(fn, v);
				}	
				break;
			}
			case  FieldInfo.TYPE_INT64:
			{
				Long v = one.longMap.get(fn);
				if (v != null)
				{
					longMap.put(fn, v);
				}	
				break;
			}
			case  FieldInfo.TYPE_SEQUENCE:
			{
				if(fi.getFlag()==0)
					break;
				Long v = one.longMap.get(fn);
				if (v != null)
				{
					longMap.put(fn, v);
				}	
				break;
			}	
			case  FieldInfo.TYPE_BYTE16:
			{
				byte[] v = one.byteArrayMap.get(fn);
				if (v != null)
				{
					byteArrayMap.put(fn, v);
				}	
				break;
			}	
			case  FieldInfo.TYPE_BINARY:
			{
				byte []bb=one.getAsbyteArray(fn);
				if(bb!=null)
				{
					byteArrayMap.put(fn, bb);
				}  
				break;
			}
			case  FieldInfo.TYPE_BIT:
			case  FieldInfo.TYPE_BIT2:
			case  FieldInfo.TYPE_BIT4:
			{
				Integer v = one.intMap.get(fn);
				if (v != null)
				{	
					intMap.put(fn, v);
				}	
				break;
			}
			case  FieldInfo.TYPE_TEXT:
			case  FieldInfo.TYPE_VARCHAR:
			case  FieldInfo.TYPE_CATEGORY:
			case  FieldInfo.TYPE_VARCHAR_ARRAY:	
			case  FieldInfo.TYPE_CLOB:
			{
				String v = one.strMap.get(fn);
			
				if (null != v)
				{
					strMap.put(fn, v);
				}
				break;
			}
			
	
			
			}
		}	
		
	}
	
	

}
