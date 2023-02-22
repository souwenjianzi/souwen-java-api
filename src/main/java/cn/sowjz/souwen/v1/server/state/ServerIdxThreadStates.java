package cn.sowjz.souwen.v1.server.state;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.sowjz.souwen.v1.db.struct.FieldInfos;

public class ServerIdxThreadStates
{
	List<ServerIdxThreadState> states;

	FieldInfos infos;
	public ServerIdxThreadStates(FieldInfos infos)
	{
		this.infos=infos;
		states = new ArrayList<ServerIdxThreadState>();
	}

	public ServerIdxThreadState get(int idx)
	{
		return states.get(idx);
	}

	public int size()
	{
		return states.size();
	}

	public void bytes2Me(byte[] buf, String charset) throws IOException
	{
		int start = 0;

		// int size = Convert.bytes2Int(buf, start);
		// start += 4;
		int size = 1;
		if (states == null)
			states = new ArrayList<ServerIdxThreadState>(size);
		for (int i = 0; i < size; i++)
		{
			ServerIdxThreadState state = new ServerIdxThreadState(infos);
			start += state.bytes2Me(buf, start, charset);
			states.add(state);
		}

		if (start != buf.length)
			throw new IOException("the buf size != start");
	}
}
