package cn.sowjz.souwen.v1.tools;

import cn.sowjz.souwen.v1.SouwenClient;
import cn.sowjz.souwen.v1.db.struct.FieldInfo;
import cn.sowjz.souwen.v1.db.struct.FieldInfos;
import cn.sowjz.souwen.v1.doc.Doc;
import cn.sowjz.souwen.v1.query.request.QueryRequest;
import cn.sowjz.souwen.v1.query.request.UpdateRequest;
import cn.sowjz.souwen.v1.query.response.QueryResponse;
import cn.sowjz.souwen.v1.query.response.UpdateResponse;
import cn.sowjz.souwen.v1.util.DocUpdateChecker;

public class UpdateTool {

	SouwenClient ss;
	public UpdateTool(SouwenClient ss){
		this.ss=ss;
	}
	/**
	 * 构造一个Doc对象，添加目标数据SEQ字段，然后增加需要改变的字段值。
	 * 
	 * */
	
	public int update(Doc one) throws Exception{
		FieldInfos infos = ss.getInfos();
		FieldInfo idFI = infos.getSeqFI();
		if(one.getAsLong(idFI.getName())==null)
			throw new Exception("value of "+idFI.getName()+" is null");
		if(new DocUpdateChecker().canQuickUpdate(one)){
			
			UpdateRequest req = new UpdateRequest(ss);
			req.createCriteria().andEqual(idFI.getName(), one.getAslong(idFI.getName()));
			UpdateResponse n=ss.update(req, one);
			return (int) n.getUpdateNum();
		}
		
		QueryRequest req = new QueryRequest(ss, QueryRequest.OrderBy.time, QueryRequest.SumType.count);
		req.createCriteria().andEqual(idFI.getName(), one.getAslong(idFI.getName()));
		QueryResponse hits = ss.query(req);
		if(hits.getDocNum()>0){
			Doc a=hits.get(0).getDoc();
			a.copy(one);
			one=a;
			ss.delDoc(req);
		}
		ss.addDoc(one);
		return 1;
	}
}
