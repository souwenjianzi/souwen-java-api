package cn.sowjz.souwen.v1.net.control;

import cn.sowjz.souwen.v1.Constants;
import cn.sowjz.souwen.v1.SouwenClient;
import cn.sowjz.souwen.v1.conf.SouwenConfig;
import cn.sowjz.souwen.v1.util.ByteBuff;

public class IndexConn extends BaseConn{
	
	public IndexConn(SouwenConfig cfg) 
	{
		super(cfg.getSockIp(),cfg.getIndexSockPort(),cfg.getSocketTimeOut());
	}

	boolean first_connect=true;
	@Override
	protected ByteBuff buildCommand(int opt)
	{
		ByteBuff bb =new ByteBuff();
		if(first_connect)
		{	
			bb.append(("BINARY /ver"+SouwenClient.Api_Version+" isearch7\n").getBytes());
			for(int i=bb.getUsed();i<Header_Len;i++)
				bb.append((byte)0);	
			first_connect=false;
		}
		else
			bb.append(Constants.LOOPACK);
		bb.append(opt);
		
		return bb;
	}
	

}
