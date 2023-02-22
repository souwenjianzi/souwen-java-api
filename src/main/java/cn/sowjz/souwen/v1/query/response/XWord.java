package cn.sowjz.souwen.v1.query.response;


import cn.sowjz.souwen.v1.util.VConvert;





public class XWord {

	public String word;
	public int kind;
	public double tf;
	public double idf;
	public String attr;
	
	public String toString()
	{
		return word+" "+tf+" "+idf+" "+attr;
	}

	public int byte2Me(byte[] buf, int start, String charset) throws Exception
	{
		int start_bak = start;
		
		int len = VConvert.bytes2Int(buf, start);
		start += 4;
		word= new String(buf, start, len - 1, charset);
		start += len;
		
		kind= VConvert.bytes2Int(buf, start);
		start += 4;
		
		tf= VConvert.bytes2Int(buf, start)/100.;
		start += 4;
		
		idf= VConvert.bytes2Int(buf, start)/100.;
		start += 4;
		
		len = VConvert.bytes2Int(buf, start);
		start += 4;
		attr= new String(buf, start, len - 1, charset);
		start += len;
		
		return start - start_bak;
	}
	public boolean isSymbol()
	{

		return (kind ==7|| kind==8||kind ==10);
	}
}
