package cn.sowjz.souwen.v1.net.control;

import java.util.ArrayList;
import java.util.List;

import cn.sowjz.souwen.v1.Constants;
import cn.sowjz.souwen.v1.query.response.XWord;
import cn.sowjz.souwen.v1.util.ByteBuff;
import cn.sowjz.souwen.v1.util.VConvert;

public class AskMining {
	
	BaseConn conn;
	
	public AskMining(BaseConn conn)
	{
		this.conn=conn;
	}
	
	 public List<XWord> tokenText(String text,String charset)throws Exception
	 {
		 byte []tb=text.getBytes(charset);
		 ByteBuff bb=new ByteBuff();
		 bb.append(tb);
		 bb.append((byte)0);
			
		 byte [] buf=conn.runCommand(Constants.MINING_TOKEN,bb.array(),bb.getUsed());
		if(buf==null)return null;	
		
			
			int start=0;
			int size = VConvert.bytes2Int(buf, start);
			start += 4;
			List<XWord> words=new ArrayList<XWord>(size);
			
			for(int i=0;i<size;i++)
			{
				XWord w = new XWord();
				start += w.byte2Me(buf, start, charset);
				words.add(w);
			}	
			
		
			return words;
	 }

	
}
