package cn.sowjz.souwen.v1.query.response;

import java.util.ArrayList;
import java.util.List;

import cn.sowjz.souwen.v1.SouwenClient;
import cn.sowjz.souwen.v1.db.struct.FieldInfo;
import cn.sowjz.souwen.v1.db.struct.FieldInfos;
import cn.sowjz.souwen.v1.doc.Doc;
import cn.sowjz.souwen.v1.query.highlight.HighLight;
import cn.sowjz.souwen.v1.query.request.Criteria;
import cn.sowjz.souwen.v1.query.request.QueryRequest;
import cn.sowjz.souwen.v1.tools.htmlparse.IHtml2Txt;
import cn.sowjz.souwen.v1.util.VConvert;

public class Hit
{
	SouwenClient ss = null;

	protected long time;

	protected int segid;

	protected int docid;

	protected int weight;

	protected long id;

	protected double mark;
	
	private Doc doc;
	
	int doclen;
	
	

	QueryResponse hits;
	
	public Hit(SouwenClient ss,QueryResponse hits)
	{
		doc = new Doc(ss);
		this.ss = ss;
		this.hits=hits;
	}
	public Hit(SouwenClient ss)
	{
		doc = new Doc(ss);
		this.ss = ss;
	}

	/**
	 * @return Returns the docid.
	 */
	public int getDocid()
	{
		return docid;
	}

	/**
	 * @return Returns the segid.
	 */
	public int getSegid()
	{
		return segid;
	}

	/**
	 * @return Returns the time.
	 */
	public long getTime()
	{
		return time;
	}
    public Doc getDoc()
    {
    	return doc;
    }
	/**
	 * @return Returns the weight.
	 */
	public int getWeight()
	{
		return weight;
	}

	public String[] getFieldnames()
	{
		return doc.getFieldnames();
	}

	public String stringValue(String fieldName)
	{
		return doc.getAsString(fieldName);
	}

	public int intValue(String fieldName)
	{
		return doc.getAsint(fieldName);
	}

	public long longValue(String fieldName)
	{
		return doc.getAslong(fieldName);
	}
	public byte[]getByteArray(String fieldName)
	{
	
		return doc.getAsbyteArray(fieldName);
	}
	public String getByteArrayOfHexStr(String fieldName)
	{
		
		
		return doc.getAsByteArrayOfHexStr(fieldName);
	}

	public String getSummary(String fieldname, String keyStr, String startMark, String endMark, int reltLen)
	{
		FieldInfo info = ss.getInfos().find(fieldname);
		if (null == info)
			throw new IllegalArgumentException("no field named : " + fieldname);
		if (!info.isCateField() && !info.isTextField()&&!info.isVarcharField())
			throw new IllegalArgumentException("should be category field or text field, but field named : " + fieldname + " is not.");

		String v = this.stringValue(fieldname);

		if((info.getType()==FieldInfo.TYPE_TEXT)&& (info.getFlag() &2)!=0)
		{
			v=IHtml2Txt.parse(v);
		}
		if(keyStr!=null && keyStr.length()>0 && isSearchOp(fieldname))
		{
			try {
				List<XWord> ws=hits.tokenSearchOpKey(keyStr);
				if(ws!=null && ws.size()>0)
				  return new HighLight().highLight4searchOp(v, ws, startMark, endMark, reltLen);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}	
		
		return new HighLight().highLight(v, keyStr, startMark, endMark, reltLen);
	}

	private boolean isSearchOp(String fieldname) {
		if(hits==null)
			return false;
		QueryRequest req=hits.getQueryRequest();
		if(req==null)
			return false;
		if(req.getFieldCritValue(fieldname,Criteria.FUZZYMATCH)!=null)
			return true;
		return false;
	}

	/**
	 * 设置字段的内容。如果该字段已经有内容，新的内容会覆盖原始内容
	 * @param fieldname -- 字段名
	 * @param value -- 字段内容
	 */
	public void setValue(String fieldname, String value)
	{
		doc.setValue(fieldname, value);
	}

	/**
	 * 设置字段的内容。如果该字段已经有内容，新的内容会覆盖原始内容
	 * @param fieldname -- 字段名
	 * @param value -- 字段内容
	 */
	public void setValue(String fieldname, long value)
	{
		doc.setValue(fieldname, value);
	}

	/**
	 * 设置字段的内容。如果该字段已经有内容，新的内容会覆盖原始内容
	 * @param fieldname -- 字段名
	 * @param value -- 字段内容
	 */
	public void setValue(String fieldname, int value)
	{
		doc.setValue(fieldname, value);
	}

	public int bytes2Me(byte[] buf, int start, FieldInfos infos, String charset) throws Exception
	{
		int start_bak = start;
		// --------------------------------------------
		this.time = VConvert.bytes2Long(buf, start);
		start += 8;
		// this.segid = Convert.bytes2Int(buf, start);
		// start += 4;
		// this.docid = Convert.bytes2Int(buf, start);
		// start += 4;
		this.weight = VConvert.bytes2Int(buf, start);
		start += 4;
		this.mark = VConvert.bytes2double(buf, start);
		start += 8;
		
		
		start += 4;
		start += 8;
		
		int len = VConvert.bytes2Int(buf, start);
		start += 4;
		doclen=len;
		int al=doc.fillIn(buf,start,len);
       
		FieldInfo fi=infos.getSeqFI();
		if(fi!=null)
		
	    id=doc.getAslong(fi.getName());
		return start - start_bak+al;
	}



	/**
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuffer buf = new StringBuffer(64);
		buf.append("[time=").append(time).append(", id=").append(id).append(", segid=").append(segid).append(", docid=").append(docid).append(", weight=")
				.append(weight).append(", heat=")
				.append(mark).append('\n');
		buf.append(doc);
		buf.append("]");

		return buf.toString();
	}

	public long getId()
	{
		return id;
	}



	public int getDoclen()
	{
		return doclen;
	}

	
	
	public double getFormulaResult(){
		return mark;
	}
	
}