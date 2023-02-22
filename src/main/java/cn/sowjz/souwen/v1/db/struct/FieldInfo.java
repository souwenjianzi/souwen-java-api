package cn.sowjz.souwen.v1.db.struct;

import java.io.IOException;

import cn.sowjz.souwen.v1.util.ByteBuff;

/**
 * 描述一个全文库字段的类
 * @author Ryan
 * @since 1.0
 * @version 1.1
 */
public class FieldInfo
{
	
	public static final int TYPE_TEXT=1;
	public static final int TYPE_VARCHAR=2;
	public static final int TYPE_INT32=3;
	public static final int TYPE_INT64=4;
	public static final int TYPE_SEQUENCE=6;
	public static final int TYPE_BYTE16=8;
	public static final int TYPE_CLOB=9;
	public static final int TYPE_BINARY=10;
	public static final int TYPE_BYTE=11;
	public static final int TYPE_SHORT=12;
	public static final int TYPE_BIT=13;
	public static final int TYPE_BIT2=14;
	public static final int TYPE_BIT4=15;
	public static final int TYPE_VARCHAR_ARRAY=16;
	public static final int TYPE_CATEGORY=17;
	public static final int TYPE_INT24=18;
	
	public static final String types[]=new String[]{"TYPE_TEXT","TYPE_VARCHAR","TYPE_INT32","TYPE_INT64",null
		,"TYPE_SEQUENCE",null,"TYPE_BYTE16","TYPE_CLOB","TYPE_BINARY"
		,"TYPE_BYTE","TYPE_SHORT","TYPE_BIT","TYPE_BIT2","TYPE_BIT4"
		,"TYPE_VARCHAR_ARRAY","TYPE_CATEGORY","TYPE_INT24"};
	
	protected String name;

	protected int sn; // serial number

	protected int type;


	protected boolean cached;


	protected boolean unsign;

	protected boolean time;

	protected boolean store;
	protected  byte flag;

	/**
	 * 构造
	 */
	public FieldInfo()
	{
		store=true;
	}

	/**
	 * 构造
	 * @param name -- 字段名
	 * @param type -- 类型
	 * @param weight -- 权重
	 * @param cached -- 是否被缓存
	 * @param index -- 是否分词
	 * @param compare -- 是否比较
	 * @param unsign -- 是否带符号
	 * @param time -- 是否是时间
	 * @param store -- 是否存储
	 */
	public FieldInfo(int sn, String name, int type,int flag,  boolean cached,boolean unsign, boolean time, boolean store)
	{
		this.name = name;
		this.sn = sn; // serial number
		this.type = type;
		this.cached = cached;
		this.unsign = unsign;
		this.time = time;
		this.store = store;
		this.flag=(byte)flag;
	}
	public FieldInfo(int sn, String name, int type,  boolean cached,  boolean unsign, boolean time, boolean store)
	{
		this(sn,name,type,0,cached,  unsign, time, store);
	}
	public FieldInfo(byte[] bb)
	{
		sn=bb[0]&0xff;
		name=new String(bb,1,2);
		
		 type=bb[8]&0xff;
		 cached=bb[10]==1;
		 unsign=bb[13]==1;
         time=bb[14]==1;
         store=bb[15]==1;
         flag=bb[7];
	}
	public FieldInfo(byte[] bb,int begin)
	{
		sn=bb[begin]&0xff;
		name=new String(bb,begin+1,2);
		
		 type=bb[begin+8]&0xff;
		 cached=bb[begin+10]==1;
		 unsign=bb[begin+13]==1;
         time=bb[begin+14]==1;
         store=bb[begin+15]==1;
         flag=bb[begin+7];
	}
	/**
	 * 是否压缩
	 * @return true表示是。false表示不是
	 */
	public boolean isCached()
	{
		return cached;
	}




	/**
	 * 得到字段名
	 * @return 字段名
	 */
	public String getName()
	{
		return name;
	}



	/**
	 * 返回序号
	 * @return 字段序号
	 */
	public int getSn()
	{
		return sn;
	}

	public void setSn(int sn) {
		this.sn=sn;
	}

	/**
	 * 是否存储
	 * @return true表示是。false表示不是
	 */
	public boolean isStore()
	{
		return store;
	}



	/**
	 * 是否是时间
	 * @return true表示是。false表示不是
	 */
	public boolean isTime()
	{
		return time;
	}



	/**
	 * 是否是Sequenece字段
	 * @return true表示是。false表示不是
	 */
	public boolean isSequenceField()
	{
		return type == TYPE_SEQUENCE;
	}

	/**
	 * 是否是文本字段
	 * @return true表示是。false表示不是
	 */
	public boolean isTextField()
	{
		return type == TYPE_TEXT ;
	}

	public boolean isVarcharField()
	{
		return type==TYPE_VARCHAR ;
	}
	public boolean isBinaryField()
	{
		return type==TYPE_BINARY;
	}
	public boolean isClobField()
	{
		return type==TYPE_CLOB;
	}
	
	public boolean isByte16Field()
	{
		return type==TYPE_BYTE16;
	}
	public boolean isNativeID()
	{
		return type==TYPE_SEQUENCE && flag==0;
	}
	/**
	 * 是否是分类字段
	 * @return true表示是。false表示不是
	 */
	public boolean isCateField()
	{
		return type == TYPE_CATEGORY;
	}

	/**
	 * 是否是int32字段
	 * @return true表示是。false表示不是
	 */
	public boolean isInt32Field()
	{
		return type == TYPE_INT32;
	}
	public boolean isInt32TimeField()
	{
		return type == TYPE_INT32 && time;
	}

	public boolean isNumberField(){
		return isIntField()|| isInt64Field()||isBitField();
	}
	public boolean isIntField()
	{
    return type==TYPE_INT32 || type==TYPE_BYTE || type==TYPE_SHORT|| type==TYPE_INT24;
 	}
 	public boolean isBitField()
 	{
 		return type==TYPE_BIT || type==TYPE_BIT2 || type==TYPE_BIT4;
 		}
	/**
	 * 是否是int64字段
	 * @return true表示是。false表示不是
	 */
	public boolean isInt64Field()
	{
		return type == TYPE_INT64 ||type==TYPE_SEQUENCE;
	}

	public boolean isStringField()
	{
		return type==TYPE_TEXT|| type==TYPE_VARCHAR || type==TYPE_CATEGORY
		         || type==TYPE_CLOB||type==TYPE_VARCHAR_ARRAY;

	}
	

	/**
	 * 是否有符号
	 * @return true表示是。false表示不是
	 */
	public boolean isUnsign()
	{
		return unsign;
	}

	
	
	/**
	 * 得到字段类型
	 * @return 字段类型
	 */
	public int getType()
	{
		return type;
	}

	public byte getFlag()
	{
		return flag;
	}
	
	@Override
	public String toString()
	{
		StringBuffer buf = new StringBuffer(20);
		buf.append("name:").append(name).append(", sn:").append(sn).append(", type:").append(type).append("|").append(types[type-1])
		.append(", flag:").append(flag);	
		buf.append(", cached: ").append(cached).append(", unsign:").append(unsign);
		buf.append(", time:").append(time).append(", store:").append(store);
		return buf.toString();
	}

	/**
	 * 转化成为字节流，便于网络传输。在进行网络传输的时候，会自动地通过此方法进行传输
	 * @param charset -- 字符集
	 * @return 字节流对象
	 * @throws IOException
	 */
	public ByteBuff toByteBuffer(String charset) throws IOException
	{
		ByteBuff buf = new ByteBuff();
		buf.append((byte) sn); // sn (c:char)
		byte[] namebytes = null;
		namebytes = name.getBytes(charset);
		buf.append(namebytes, 0, namebytes.length); // name (char[7])
		for (int j = 0; j < 6 - namebytes.length; j++)
			buf.append((byte) 0);
		// type(c:char), weight(c:char), cached(c:char), index(c:char),compare(c:char),unsigned(c:char),time(c:char),store(c:char)
		buf.append((byte) flag);
		buf.append((byte) type).append((byte) 1).append(cached).append((byte)1).append((byte)1).append(unsign).append(time).append(store);
		return buf;
	}
	
	public String getTypeName()
	{
		
		switch(type)
		{
		     case TYPE_TEXT: return "正文";
		     case TYPE_VARCHAR : return "短语";
		     case TYPE_BINARY:  return "存储";
		     case TYPE_INT32: return "32位整数";
		     case TYPE_INT64 :return "64位整数";
		     case TYPE_CATEGORY : return "分类";
		     case TYPE_SEQUENCE : return "序列";
		 
		     case TYPE_BYTE16 : return "byte16";
		     case TYPE_CLOB : return "clob";
		     case TYPE_BYTE : return "字节";
		     case TYPE_SHORT :return "16位整数";
		     case TYPE_BIT : return "1位布尔";
		     case TYPE_BIT2 : return "2位整数";
		     case TYPE_BIT4 : return "4位整数";
		     case TYPE_VARCHAR_ARRAY : return "短语数组";
		     case TYPE_INT24 :return "24位整数";
		}
		 return "未知";
	}
	
	

	
	

	

	
	public boolean isSupportHtml(){
		return (type == TYPE_TEXT ) && (flag &2)>0;
	}
	
	/**--------------------------------------------------------------*/
	protected FieldInfo sn(int sn){
		this.sn=sn;
		return this;
	}
	protected FieldInfo name( String name){
		this.name=name;
		return this;
	}
	protected FieldInfo type( int type){
		this.type=type;
		return this;
	}
	
	
	
	public static class Sequence extends FieldInfo{
		public Sequence(String name){
			super();
			name(name);
			type( TYPE_SEQUENCE);
		}
		public Sequence valueFromExternalInput(){
			this.flag=(byte)1;
			return this;
		}
		public Sequence valueFromNativeGeneration(){
			this.flag=(byte) 0;
			return this;
		}
		public Sequence cached( boolean cached){
			this.cached=cached;
			return this;
		} 
		public Sequence store(boolean store){
			this.store=store;
			return this;
		}
	}
	
	
	public static class Text extends FieldInfo{
		public Text(String name){
			super();
			name(name);
			type(TYPE_TEXT);
		}
		public Text supportHtml(){
			flag |=2;
			return this;
		}
		public Text cached( boolean cached){
			this.cached=cached;
			return this;
		} 
		public Text store(boolean store){
			this.store=store;
			return this;
		}
	}
	public static class Varchar extends FieldInfo{
		public Varchar(String name){
			super();
			name(name);
			type( TYPE_VARCHAR);
		}
		public Varchar cached( boolean cached){
			this.cached=cached;
			return this;
		} 
		public Varchar store(boolean store){
			this.store=store;
			return this;
		}
	}

	
	public static class Int32 extends FieldInfo{
		public Int32(String name){
			super();
			name(name);
			type( TYPE_INT32);
		}
		public Int32 unsign(boolean isUnsign){
			this.unsign=isUnsign;
			return this;
		}
		public Int32 time(boolean isTime){
			this.time=isTime;
			return this;
		} 
		public Int32 cached( boolean cached){
			this.cached=cached;
			return this;
		} 
		public Int32 store(boolean store){
			this.store=store;
			return this;
		}
	}
	public static class Int64 extends FieldInfo{
		public Int64(String name){
			super();
			name(name);
			type( TYPE_INT64);
		}
		public Int64 time(boolean isTime){
			this.time=isTime;
			return this;
		} 
		public Int64 cached( boolean cached){
			this.cached=cached;
			return this;
		} 
		public Int64 store(boolean store){
			this.store=store;
			return this;
		}
	}
	public static class Category extends FieldInfo{
		public Category(String name){
			super();
			name(name);
			type( TYPE_CATEGORY);
		}
		public Category cached( boolean cached){
			this.cached=cached;
			return this;
		} 
		public Category store(boolean store){
			this.store=store;
			return this;
		}
	}
	
	
	public static class Byte16 extends FieldInfo{
		public Byte16(String name){
			super();
			name(name);
			type( TYPE_BYTE16);
		}
		public Byte16 cached( boolean cached){
			this.cached=cached;
			return this;
		} 
		public Byte16 store(boolean store){
			this.store=store;
			return this;
		}
	}
	public static class Clob extends FieldInfo{
		public Clob(String name){
			super();
			name(name);
			type( TYPE_CLOB);
		}
		
		public Clob store(boolean store){
			this.store=store;
			return this;
		}
	}
	public static class Binary extends FieldInfo{
		public Binary(String name){
			super();
			name(name);
			type(TYPE_BINARY);
		}
		public Binary store(boolean store){
			this.store=store;
			return this;
		}
	}
	public static class Byte extends FieldInfo{
		public Byte(String name){
			super();
			name(name);
			type( TYPE_BYTE);
		}
		public Byte unsign(boolean isUnsign){
			this.unsign=isUnsign;
			return this;
		}
		public Byte cached( boolean cached){
			this.cached=cached;
			return this;
		} 
		public Byte store(boolean store){
			this.store=store;
			return this;
		}
	}
	public static class Short extends FieldInfo{
		public Short(String name){
			super();
			name(name);
			type( TYPE_SHORT);
		}
		public Short unsign(boolean isUnsign){
			this.unsign=isUnsign;
			return this;
		}
		public Short cached( boolean cached){
			this.cached=cached;
			return this;
		} 
		public Short store(boolean store){
			this.store=store;
			return this;
		}
	}
	public static class Bit extends FieldInfo{
		public Bit(String name){
			super();
			name(name);
			type( TYPE_BIT);
		}
		public Bit cached( boolean cached){
			this.cached=cached;
			return this;
		} 
		public Bit store(boolean store){
			this.store=store;
			return this;
		}
	}
	public static class Bit2 extends FieldInfo{
		public Bit2(String name){
			super();
			name(name);
			type( TYPE_BIT2);
		}
		public Bit2 cached( boolean cached){
			this.cached=cached;
			return this;
		} 
		public Bit2 store(boolean store){
			this.store=store;
			return this;
		}
	}
	public static class Bit4 extends FieldInfo{
		public Bit4(String name){
			super();
			name(name);
			type( TYPE_BIT4);
		}
		public Bit4 cached( boolean cached){
			this.cached=cached;
			return this;
		} 
		public Bit4 store(boolean store){
			this.store=store;
			return this;
		}
	}
	public static class VarcharArray extends FieldInfo{
		public VarcharArray(String name){
			super();
			name(name);
			type(TYPE_VARCHAR_ARRAY);
		}
		public VarcharArray cached( boolean cached){
			this.cached=cached;
			return this;
		} 
		public VarcharArray store(boolean store){
			this.store=store;
			return this;
		}
	}
	
	public static class Int24 extends FieldInfo{
		public Int24(String name){
			super();
			name(name);
			type( TYPE_INT24);
		}
		public Int24 unsign(boolean isUnsign){
			this.unsign=isUnsign;
			return this;
		}
		public Int24 cached( boolean cached){
			this.cached=cached;
			return this;
		} 
		public Int24 store(boolean store){
			this.store=store;
			return this;
		}
	}


	


}
