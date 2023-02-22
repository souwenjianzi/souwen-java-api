package cn.sowjz.souwen.v1.server.state;

import cn.sowjz.souwen.v1.Constants;
import cn.sowjz.souwen.v1.util.VConvert;

public class ServerFeedInfo 
{

	public int feedBuffSize;       	//byte
	
	public int hostSn;
	public int charset;  			//0 GBK 1 UTF8
	public int tokenMode; 			//5:letter 1:single 2: double 3 :word 4:external
	public int segMngMode; 			// 1: none 2:bymonth 3:byday
	public int segCacheSize; 		//M
	public int autoCommitMinute;   		//minute
	public int queryThreadNum;
	public int queryMemory;  		//M
	public int queryTimeOut;
	public int indexTimeOut;
	public int version;
	public int indexStatus;  //1: INDEX_BUSY		2:INDEX_CANNOT	3:INDEX_AVAILABLE
	
	public void byte2Me(byte[] buf, int start, int length) throws Exception
	{
		if(length<60)
			throw new Exception("ServerFeedInfo.byte2Me() size of byte buff is wrong ");
		
		feedBuffSize= VConvert.bytes2Int(buf, start);
		start += 4;
		 VConvert.bytes2Int(buf, start);
		start += 4;
		hostSn = VConvert.bytes2Int(buf, start);
		start += 4;
		charset = VConvert.bytes2Int(buf, start);
		start += 4;
		tokenMode = VConvert.bytes2Int(buf, start);
		start += 4;
		segMngMode = VConvert.bytes2Int(buf, start);
		start += 4;
		segCacheSize = VConvert.bytes2Int(buf, start);
		start += 4;
		autoCommitMinute = VConvert.bytes2Int(buf, start);
		start += 4;
		queryThreadNum = VConvert.bytes2Int(buf, start);
		start += 4;
		queryMemory = VConvert.bytes2Int(buf, start);
		start += 4;
		VConvert.bytes2Int(buf, start);
		start += 4;
		queryTimeOut = VConvert.bytes2Int(buf, start);
		start += 4;
		indexTimeOut = VConvert.bytes2Int(buf, start);
		start += 4;		
		version = VConvert.bytes2Int(buf, start);
		start += 4;		
		indexStatus = VConvert.bytes2Int(buf, start);
		start += 4;	
	}

	public boolean isDistributeMode() 
	{
		
		return false;
	}
	public String getCharset()
	{
	   if(charset==0)return Constants.GBK;
	   return "UTF8";
	}
	public String toString()
	{
		StringBuffer strb=new StringBuffer();
		strb.append("feedBuffSize=").append(feedBuffSize).append("byte\n");
		strb.append("distributedMode=");
		
		strb.append("hostSn=").append(hostSn).append("\n");
		strb.append("charset=");
		if(charset==1)	strb.append("UTF8\n");
		else if(charset==0)	strb.append("GBK\n");
		else	strb.append("unkown\n");
		strb.append("tokenMode=");
		if(tokenMode==1)	strb.append("single\n");
		else if(tokenMode==2)	strb.append("double\n");
		else if(tokenMode==3)	strb.append("word\n");
		else if(tokenMode==4)	strb.append("external\n");
		else if(tokenMode==5)	strb.append("letter\n");
		else	strb.append("unkown\n");
		
		strb.append("segMngMode=");
		if(segMngMode==1)	strb.append("none\n");
		else if(segMngMode==2)	strb.append("bymonth\n");
		else if(segMngMode==3)	strb.append("byday\n");
		else	strb.append("unkown\n");
		
		strb.append("segCacheSize=").append(segCacheSize).append("M\n");
		strb.append("autoCommitMinute=").append(autoCommitMinute).append("minute\n");
		strb.append("queryThreadNum=").append(queryThreadNum).append("\n");
		
		strb.append("queryMemory=").append(queryMemory).append("M\n");
		
		strb.append("queryTimeOut=").append(queryTimeOut).append("\n");
		strb.append("indexTimeOut=").append(indexTimeOut).append("\n");
		strb.append("version=").append(version).append("\n");
	
		
		strb.append("indexStatus=");
		if(indexStatus==1)	strb.append("INDEX_BUSY\n");
		else if(indexStatus==2)	strb.append("INDEX_CANNOT\n");
		else if(indexStatus==3)	strb.append("INDEX_AVAILABLE\n");
		else	strb.append("unkown\n");

	
		return strb.toString();
	}
}
