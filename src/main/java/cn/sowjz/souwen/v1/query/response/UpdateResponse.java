package cn.sowjz.souwen.v1.query.response;

import java.util.Arrays;

import cn.sowjz.souwen.v1.util.VConvert;

public class UpdateResponse {

	long findNum;
	long updateNum; //如果修改值和实际值没有变化，也会累计到updateNum里面
	long docSeq[];   //如果修改值和实际值没有变化，不会出现在docSeq里面
	public void byte2Me(byte[] buf) {
		int p = 0;
		findNum=VConvert.bytes2Long(buf, p);
		p+=8;
		updateNum=VConvert.bytes2Long(buf, p);
		p+=8;
		if(p==buf.length)
			return;
		if(p+8*updateNum<=buf.length){
			docSeq=new long[(int) updateNum];
			for(int i=0;i<updateNum;i++){
				docSeq[i]=VConvert.bytes2Long(buf, p);
				p+=8;
			}
		}
		
	}
	@Override
	public String toString() {
		return "UpdateResponse [findNum=" + findNum + ", updateNum=" + updateNum
				+ ", docSeq=" + Arrays.toString(docSeq) + "]";
	}
	public long getFindNum() {
		return findNum;
	}
	public long getUpdateNum() {
		return updateNum;
	}
	
	public long[] getUpdatedDocSeqs(){
		return docSeq;
	}
}
