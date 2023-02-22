package cn.sowjz.souwen.v1.db.struct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.sowjz.souwen.v1.util.ByteBuff;


/**
 * 全文库结构的描述
 * @author Ryan
 */
public class FieldInfos
{
	private List<FieldInfo> infos;

	private int sortno;
	
	private int seqno;

	/**
	 * 构造
	 */
	public FieldInfos()
	{
		this(6);
	}

	/**
	 * 构造
	 * @param size -- 大小
	 */
	public FieldInfos(int size)
	{
		sortno = -1;
		seqno=-1;
		infos = new ArrayList<FieldInfo>(size);
	}

	/**
	 * 字段个数
	 * @return 字段个数
	 */
	public int size()
	{
		return infos.size();
	}

	/**
	 * 添加一个字段
	 * @param info
	 */
	public void add(FieldInfo info)
	{
		infos.add(info);
	}

	
	/**
	 * 通过字段名，查找字段
	 * @param name -- 字段名
	 * @return 字段
	 */
	public FieldInfo find(String name)
	{
		
		
		
		for (FieldInfo info : infos)
		{
			if (info.getName().equals(name))
				return info;
		}
		return null;
	}

	/**
	 * 通过序号，查找字段
	 * @param sn
	 * @return 字段
	 */
	public FieldInfo find(int sn)
	{
		for (FieldInfo info : infos)
		{
			if (info.getSn() == sn)
				return info;
		}
		return null;
	}

	/**
	 * 得到所有的字段信息
	 * @return 所有字段的集合
	 */
	public List<FieldInfo> getInfos()
	{
		return infos;
	}

	/**
	 * 得到第i个字段
	 * @param i
	 * @return 字段
	 */
	public FieldInfo get(int i)
	{
		if (i < 0 || size() == 0 || i >= size())
			return null;
		else
			return infos.get(i);
	}

	/**
	 * 得到排序字段的序号
	 * @return 排序字段序号
	 */
	public int getSortno()
	{
		return sortno;
	}

	/**
	 * 设置排序字段的序号
	 * @param sortno
	 */
	public void setSortno(int sortno)
	{
		this.sortno = sortno;
	}
	
	public void setSortnoByName(String fieldname) throws Exception
	{
		int n=0;
		for (FieldInfo info : getInfos())
			info.setSn(n++);
		
		FieldInfo fi = find(fieldname);
		if(fi==null)throw new Exception("no such a field:"+fieldname);
		this.sortno = fi.getSn();
	}
	
	public FieldInfo getSeqFI()
	{
		if(seqno==-1)return null;
		if(infos==null)return null;
		if(seqno>=infos.size())return null;
		return infos.get(seqno);
	}

	@Override
	public String toString()
	{
		StringBuffer buf = new StringBuffer(256);
		buf.append("size:").append(size()).append(", sortno:").append(sortno).append("\n");
		for (int i = 0; i < size(); i++)
			buf.append(i).append("====> ").append(get(i).toString()).append("\n");
		return buf.toString();
	}

	/**
	 * 把自己转化成字节流，便于网络传输，网络传输的时候会自动调用。
	 * @param charset -- 字符集
	 * @return 字节流对象
	 * @throws IOException
	 */
	public ByteBuff toByteBuffer(String charset) throws IOException
	{
		ByteBuff buf = new ByteBuff();
		// num (c:char)
		buf.append((byte) size());
		// info
		for (FieldInfo info : infos)
			buf.append(info.toByteBuffer(charset));
		// sortno(c:char)
		return buf.append((byte) sortno);
	}

	/**
	 * 把字节流转化成自己
	 * @param buf -- 字节数组
	 * @param charset -- 字符集
	 * @return FieldInfos
	 * @throws IOException
	 */
	public static FieldInfos bytes2Me(byte[] buf, String charset) throws IOException
	{
		int i = 0;

		FieldInfos reltInfos = new FieldInfos(buf[i++]);
		for (int j = 0; j < (int) buf[0]; j++)
		{
			FieldInfo info = new FieldInfo(buf,i);

			i+=16;
			reltInfos.add(info);
		}
		reltInfos.setSortno((int) buf[i]);
		return reltInfos;
	}

	public int howManySequenceField()
	{
		int num = 0;
		for (int i = 0; i < size(); i++)
		{
			if (infos.get(i).getType() == FieldInfo.TYPE_SEQUENCE)
				num++;
		}
		return num;
	}

	public String getAllNames() {
		StringBuffer buf = new StringBuffer(256);
		
		for (int i = 0; i < size(); i++)
			buf.append(get(i).getName()).append(",");
		return buf.toString();
	}
}
