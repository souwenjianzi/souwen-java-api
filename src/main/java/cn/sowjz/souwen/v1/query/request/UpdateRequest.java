package cn.sowjz.souwen.v1.query.request;

import cn.sowjz.souwen.v1.BaseStructure;

public class UpdateRequest extends QueryRequest{

	public UpdateRequest(BaseStructure ss) {
		super(ss);
	}
	/*
	 * returnUpdatedSeq 如果值发生了变化，则返回seq字段的值，
	 * */
	public void setUpdateConfig(boolean returnUpdatedSeq){
		header.updateSetting=0;
		if(returnUpdatedSeq)
			header.updateSetting|=1;
	}
	
}
