package cn.sowjz.souwen.v1.server.state;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.sowjz.souwen.v1.db.struct.FieldInfo;
import cn.sowjz.souwen.v1.db.struct.FieldInfos;
import cn.sowjz.souwen.v1.util.VConvert;

public class ServerSegState
{
	private String name;

	private int docNum;

	private long size;
	private long mindatetime;
	private long maxdatetime;

	FieldInfos infos;
	FieldInfo sort_fi;
	public ServerSegState(FieldInfos infos)
	{
		this.infos=infos;
		sort_fi=infos.get(infos.getSortno());
	}

	public int getDocNum()
	{
		return docNum;
	}

	public void setDocNum(int docNum)
	{
		this.docNum = docNum;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public long getSize()
	{
		return size;
	}

	public void setSize(long size)
	{
		this.size = size;
	}

	public int byte2Me(byte[] buf, int start, String charset) throws UnsupportedEncodingException
	{
		int start_bak = start;
		int len = VConvert.bytes2Int(buf, start);
		start += 4;

		name = new String(buf, start, len, charset);
		start += len;

		docNum = VConvert.bytes2Int(buf, start);
		start += 4;

		size = VConvert.bytes2Long(buf, start);
		start += 8;
		mindatetime = VConvert.bytes2Long(buf, start);
		start += 8;
		maxdatetime = VConvert.bytes2Long(buf, start);
		start += 8;
		return start - start_bak;
	}

	@Override
	public String toString()
	{
		StringBuffer strb = new StringBuffer();
		strb.append("[name = ").append(name).append(", ");
		strb.append("docNum= ").append(docNum).append(", ");
		strb.append("size  = ").append(size);
		strb.append(" value  = ").append(getMinData());
		strb.append(" ~ ").append(getMaxData()).append("]\n");
		return strb.toString();
	}
	public String getMinData()
	{
		if(sort_fi.isTime())
		{	
			if(sort_fi.isInt32Field())
				return new SimpleDateFormat("yyyy-MM-dd").format(new Date(mindatetime*1000));
			if(sort_fi.isInt64Field())
				return new SimpleDateFormat("yyyy-MM-dd").format(new Date(mindatetime));
		}
		  return String.valueOf(mindatetime);
		  
	}
	public String getMaxData()
	{
		if(sort_fi.isTime())
		{	
			if(sort_fi.isInt32Field())
				return new SimpleDateFormat("yyyy-MM-dd").format(new Date(maxdatetime*1000));
			if(sort_fi.isInt64Field())
				return new SimpleDateFormat("yyyy-MM-dd").format(new Date(maxdatetime));
		}
		 return String.valueOf(maxdatetime);
	}
	
}
