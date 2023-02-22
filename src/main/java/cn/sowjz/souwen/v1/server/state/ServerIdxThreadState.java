package cn.sowjz.souwen.v1.server.state;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cn.sowjz.souwen.v1.db.struct.FieldInfos;
import cn.sowjz.souwen.v1.util.VConvert;

public class ServerIdxThreadState
{
	FieldInfos infos;
	public  ServerIdxThreadState(FieldInfos infos)
	{
		this.infos=infos;
	}
	
	public static final int FREE = 0;

	public static final int BUSY = 1;

	public static final int BAD = 15;

	private int state;

	private long totalBytes;

	private long freeBytes;

	private long freeBytesToCaller;

	private String host_sn;

	private List<ServerSegState> segs;

	public List<ServerSegState> getSegs()
	{
		return segs;
	}

	public void setSegs(List<ServerSegState> segs)
	{
		this.segs = segs;
	}

	public long getFreeKB()
	{
		return freeBytes;
	}


	public long getFreeKBToCaller()
	{
		return freeBytesToCaller;
	}



	public long getTotalKB()
	{
		return totalBytes;
	}



	/**
	 * @return Returns the state.
	 */
	public int getState()
	{
		return state;
	}

	/**
	 * @param state The state to set.
	 */
	public void setState(int state)
	{
		this.state = state;
	}

	/**
	 * byte 2 me
	 * @param buf
	 * @param start
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public int bytes2Me(byte[] buf, int start, String charset) throws UnsupportedEncodingException
	{
		int start_bak = start;

		int ipLen = VConvert.bytes2Int(buf, start);
		start += 4;

		this.host_sn = new String(buf, start, ipLen, charset);
		start += ipLen;

		this.state = VConvert.bytes2Int(buf, start);
		start += 4;

		this.totalBytes = VConvert.bytes2Long(buf, start);
		start += 8;

		this.freeBytes = VConvert.bytes2Long(buf, start);
		start += 8;

		this.freeBytesToCaller = VConvert.bytes2Long(buf, start);
		start += 8;

		int size = VConvert.bytes2Int(buf, start);
		start += 4;
		segs = new ArrayList<ServerSegState>(size);
		for (int i = 0; i < size; i++)
		{
			ServerSegState seg = new ServerSegState(infos);
			start += seg.byte2Me(buf, start, charset);
			segs.add(seg);
		}

		return start - start_bak;
	}

	@Override
	public String toString()
	{
		StringBuffer strb = new StringBuffer();
		strb.append("服务器SN：").append(host_sn).append("\n");
		strb.append("加载线程状况：");
		if (state == ServerIdxThreadState.BAD)
			strb.append("不能加载。");
		if (state == ServerIdxThreadState.BUSY)
			strb.append("正在加载。");
		if (state == ServerIdxThreadState.FREE)
			strb.append("没有加载进行。");
		strb.append("\n");
		strb.append("磁盘总共空间：" + ((float) totalBytes) / ( 1024 )).append("G\n");
		strb.append("剩余磁盘空间：" + ((float) freeBytes) / ( 1024 )).append("G\n");
		strb.append("可用磁盘空间：" + ((float) freeBytes) / ( 1024 )).append("G\n");
		for (int i = 0; i < segs.size(); i++)
			strb.append("seg_").append(i).append(segs.get(i).toString());

		return strb.toString();
	}

	public String getHostSn()
	{
		return host_sn;
	}

}
