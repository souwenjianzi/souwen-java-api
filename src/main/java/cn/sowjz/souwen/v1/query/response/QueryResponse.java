package cn.sowjz.souwen.v1.query.response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.sowjz.souwen.v1.SouwenClient;
import cn.sowjz.souwen.v1.db.struct.FieldInfos;
import cn.sowjz.souwen.v1.query.request.QueryRequest;
import cn.sowjz.souwen.v1.util.VConvert;

public class QueryResponse
{

	private int doc_num;
	
	private int doc_total;

	List<Hit> hits;
    QueryRequest queryReq;
	private SouwenClient client;
	private byte[]databuf;

	public SouwenClient getSS()
	{
		return client;
	}

	public QueryResponse(SouwenClient souwen,QueryRequest req)
	{
		this.client = souwen;
		hits = new ArrayList<Hit>(6);
		this.queryReq=req;
	}
	
	public QueryResponse(SouwenClient souwen)
	{
		this.client = souwen;
		hits = new ArrayList<Hit>(6);
	}
	public void addHit(Hit hit)
	{
		hits.add(hit);
	}


	/**
	 * @return the num, it's not a real size
	 */
	public int getDocNum()
	{
		return doc_num;
	}

	/**
	 * @return Returns the total.
	 */
	public int getDocTotal()
	{
		return doc_total;
	}

	

	public Hit get(int i)
	{
		if (hits.size() <= 0 || i < 0 || i >= hits.size())
			return null;
		return hits.get(i);
	}

	/**
	 * @param hits The hits to set.
	 */
	public void setHits(List<Hit> hits)
	{
		this.hits = hits;
	}

	/**
	 * convert a byte array to Hits class
	 * @param buf
	 * @return
	 * @throws IOException
	 * @throws
	 * @throws Exception
	 */
	public QueryResponse bytes2Me(byte[] buf) throws Exception{
		return bytes2Me(buf,0);
	}
	public QueryResponse bytes2Me(byte[] buf,int i) throws Exception
	{
		databuf=buf;
		
		FieldInfos infos = client.getInfos();
		String charset = client.feedinfo.getCharset();

		
		doc_num=(VConvert.bytes2Int(buf, i));
		i += 4;
		i += 4;
		i += 4;
		doc_total=(VConvert.bytes2Int(buf, i));
		i += 4;
		hits = new ArrayList<Hit>(this.doc_num);
		for (int j = 0; j < this.doc_num; j++)
		{
			Hit hit = new Hit(client,this);

			i += hit.bytes2Me(buf, i, infos, charset);

				hits.add(hit);
		}
		return this;
	}



	@Override
	public String toString()
	{
		StringBuffer strb = new StringBuffer();
		strb.append("一共查到记录: ").append(doc_total).append("条,");
		strb.append("此次返回记录：").append(doc_num).append("条\n");
		for (int i = 0; i < hits.size(); i++)
		{
			strb.append("Hit-").append(i).append(": ").append(get(i).toString()).append("\n");
		}
		return strb.toString();
	}

	public Iterator<Hit> iterator()
	{
		return hits.iterator();
	}


	public byte[] getDataBuf()
	{
		return databuf;
	}

	public QueryRequest getQueryRequest() {
		
		return queryReq;
	}

	List<XWord> schOpKeyWs;
	
	public List<XWord> tokenSearchOpKey(String keyStr)throws Exception {
		if(schOpKeyWs==null)
		{ 
			
		      schOpKeyWs= client.tokenText(keyStr);
		}
		return schOpKeyWs;
	}

	
}
