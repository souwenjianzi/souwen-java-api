package cn.sowjz.souwen.v1.query.response;

import java.util.ArrayList;
import java.util.List;

import cn.sowjz.souwen.v1.SouwenClient;
import cn.sowjz.souwen.v1.db.struct.FieldInfo;
import cn.sowjz.souwen.v1.query.request.QueryRequest;
import cn.sowjz.souwen.v1.util.VConvert;

public class ExamineResponse {

	
	public final static String[] Types=new String[]{"NONE","DISK","CACHE"};
	
	SouwenClient searchSystem;
	QueryRequest req;
	public ExamineResponse(SouwenClient searchSystem, QueryRequest req) {
		this.searchSystem=searchSystem;
		this.req=req;
	}
	
	public class FieldExamHit{
		FieldInfo finfo;
		long maxValue;
		long minValue;
		int type;
		public int bytes2Me(byte[] buf, int start) {
			int start_bak=start;
			
			int t_len = VConvert.bytes2Int(buf, start);
			start += 4;
			
			String fn;
			if (t_len == 0)
				fn = "";
			else
				fn = new String(buf, start , t_len-1 );
			start += t_len;
				
			finfo=searchSystem.getInfos().find(fn);
			
			
			maxValue=VConvert.bytes2Long(buf, start);
			start += 8;
			
			minValue=VConvert.bytes2Long(buf, start);
			start += 8;
			
			type=VConvert.bytes2Int(buf, start);
			start += 4;
			
			return start - start_bak;
		}
		@Override
		public String toString() {
		
			return "FieldExamHit [finfo=" + finfo.getName() + ", maxValue=" + maxValue
					+ ", minValue=" + minValue + ", type=" + Types[type] + "]";
		}
		public FieldInfo getFinfo() {
			return finfo;
		}
		public long getMaxValue() {
			return maxValue;
		}
		public long getMinValue() {
			return minValue;
		}
		public int getType() {
			return type;
		}
		
		public String getTypeName() {
			return Types[type];
		}
	}
	public class SegExamHit{
		String segName;
		int hostSn;
		List<FieldExamHit> fieldHit;
		public int bytes2Me(byte[] buf, int start) {
			int start_bak=start;
			
			int t_len = VConvert.bytes2Int(buf, start);
			start += 4;
			
			if (t_len == 0)
				segName = "";
			else
				segName = new String(buf, start , t_len-1 );
			start += t_len;
			
			hostSn=VConvert.bytes2Int(buf, start);
			start += 4;
			
			int size=VConvert.bytes2Int(buf, start);
			start += 4;
			
			fieldHit=new ArrayList<FieldExamHit>(size);
			for(int i=0;i<size;i++){
				FieldExamHit fh=new FieldExamHit();
				
				start+=fh.bytes2Me(buf,start);
				
				fieldHit.add(fh);
			}
			
			return start - start_bak;
		}
		@Override
		public String toString() {
			StringBuffer strb=new StringBuffer();
			strb.append("SegExamHit [segName=" + segName + ", hostSn=" + hostSn + "]");
			if(fieldHit!=null)
			for(FieldExamHit h:fieldHit){
				strb.append("\r\n\t").append(h.toString());
			}
			strb.append("\r\n");
			return strb.toString();	
		}
		public String getSegName() {
			return segName;
		}
		public int getHostSn() {
			return hostSn;
		}
		public List<FieldExamHit> getFieldHit() {
			return fieldHit;
		}
	}
	
	List<SegExamHit> segHitList;
	
	

	public static String[] getTypes() {
		return Types;
	}



	public SouwenClient getSearchSystem() {
		return searchSystem;
	}



	public QueryRequest getQueryRequest() {
		return req;
	}



	public List<SegExamHit> getSegHitList() {
		return segHitList;
	}



	@Override
	public String toString() {
		StringBuffer strb=new StringBuffer();
		if(segHitList!=null){
			for(SegExamHit s:segHitList){
				strb.append(s.toString()).append("\r\n");
			}
			
		}
		return strb.toString();
	}



	public void bytes2Me(byte[] buf) {
		int start=0;
		
		int size = VConvert.bytes2Int(buf, start);
		start += 4;
		
		segHitList =new ArrayList<SegExamHit>(size);
		
		for(int i=0;i<size;i++){
			SegExamHit sh=new SegExamHit();
			start+=sh.bytes2Me(buf,start);
			
			segHitList.add(sh);
			
		}
		
		
	}

}
