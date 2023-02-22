package cn.sowjz.souwen.v1.query.request;

import cn.sowjz.souwen.v1.db.struct.FieldInfo;
import cn.sowjz.souwen.v1.util.ByteBuff;

public class CritHeader 
{
	int version=0;
    int type=0;
	int schbegin=0;            //数据返回开始位置
	int schlen;              //数据返回长度
	public byte orderby=0;           // 0 时间 1 相关
	public byte sumtype=0;          // 0:不统计 1：精确统计，2：估计
	byte matchtype=0;
	
	byte retfnum=0;
	public byte retFields[]=new byte[40];       //具体字段，最多不超过10个 
	
	
	byte targetfn[]=new byte[5];
	long groupBegin=0;
	long groupStep=0;
	
	
	byte dCfnum=0;
	byte dCFields[]=new byte[40];       //具体字段，最多不超过10个 
		

	int  dCMax=0;
	
	public short maxKeyWords;  //wordcloud, wam,cluster 的最大关键词数
	
	double clusterDistance;  
	int clustermaxloop;
	int cluster_minwordnum_group;
	
	byte distinctKeyDoc;    //0 以最新时间文章为distinct组主文章， 1 以最旧时间文章distinct组主文章。
	short clusterMaxDocInGroup;  //聚类结果中每组最多返回文章数，0表示无限制
	short clusterMaxGroup;       //聚类结果返回组最大数量，0 表示无限制
	short clusterWordNumLimitPerDoc; //聚类算法中，每篇文章最大有效关键词数，0表示无限制
	//128版本以后停用，下面的变量替换short clusterDocNumLimitPerWord; //聚类算法中，有效关键词最
	public short clusterWordTotalMaxLimit;//参加计算总词量上限

	byte cube_type;  // 0 按权重，1按字段
	short cube_weight_max;  //按权重去计算时，最大权重。若实际权重大于改值，则数量归改值。
	byte cube2fn[]=new byte[5];  //第二个分类字段。可以是FIELD_VARCHAR/ FIELD_INT32/ FIELD_CATEGORY
	int cube2f_max;   //第二个字段最大返回数量，0：无限制
	byte group_type;  // 0 按权重，1按字段
	short group_weight_max;  //按权重去计算时，最大权重。若实际权重大于改值，则数量归改值。
	byte orderbyfn[]=new byte[5];
	
	short heatFuncLen;
	byte simhash_threshold;  
	byte summaryQuery_txFields[]=new byte[10]; //结果摘要文本字段集合
	short summaryQuery_maxSentence;
	byte summaryQuery_endWithSign;  
	short focusOn;
	byte updateSetting;
	short subCritNum; //0=1;
	short execorNum; //0=1;
	byte onlyUseCharIndex;
	
	String tableName;
	public String formula;
	
	QueryRequest req;
	public CritHeader(QueryRequest req,int version,int type)
	{
		this.version=version;
		this.type=type;
		this.req=req;
		maxKeyWords=100;
		
		clusterDistance=0.5;
		clustermaxloop=5;
		cluster_minwordnum_group=4;
		
		distinctKeyDoc=0;
		clusterMaxDocInGroup=5;
		clusterMaxGroup=20;
		clusterWordNumLimitPerDoc=20;
		clusterWordTotalMaxLimit=4000;
		
		cube_type=1;
		group_type=1;
		
		heatFuncLen=0;
	}
	
	public void cluster(double distance,int maxloop,int minwordnum,short maxgroup,short mxWordPerDoc,short wordTotalMaxLimit)
	{
		clusterDistance=distance;
		clustermaxloop=maxloop;
		cluster_minwordnum_group=minwordnum;
		clusterWordNumLimitPerDoc=mxWordPerDoc;
		clusterMaxGroup=maxgroup;
		clusterWordTotalMaxLimit=wordTotalMaxLimit;
		if(clusterWordTotalMaxLimit<1)clusterWordTotalMaxLimit=4000;
		
	}
	public ByteBuff toByteBuffer(ByteBuff bb)
	{
		bb.append(version);
		bb.append(type);
		bb.append(schbegin);
		bb.append(schlen);
		bb.append(orderby);
		bb.append(sumtype);
		bb.append(matchtype);
		
		bb.append(retfnum);
		bb.append(retFields,0,40);		
		bb.append(targetfn,0,5);
		bb.append(groupBegin);
		bb.append(groupStep);
		
		bb.append(dCfnum);
		bb.append(dCFields,0,40);
		
		bb.append(dCMax);
		
		bb.append(maxKeyWords);
		
		bb.append((int)(clusterDistance*10000));
		bb.append(clustermaxloop);
		bb.append(cluster_minwordnum_group);
		bb.append(distinctKeyDoc);
		bb.append(clusterMaxDocInGroup);
		bb.append(clusterMaxGroup);
		bb.append(clusterWordNumLimitPerDoc);
		bb.append(clusterWordTotalMaxLimit);
		
		bb.append(cube_type);
		bb.append(cube_weight_max);
		bb.append(cube2fn,0,5);
		bb.append(group_type);
		bb.append(group_weight_max);
		bb.append(orderbyfn,0,5);
		bb.append(cube2f_max);   //第二个字段最大返回数量，0：无限制
		
		bb.append(heatFuncLen);
		bb.append(simhash_threshold);
		
		bb.append(summaryQuery_txFields,0,10);
		bb.append(summaryQuery_maxSentence);
		bb.append(summaryQuery_endWithSign);
		bb.append(focusOn);
		bb.append(updateSetting);
		bb.append(subCritNum);
		bb.append(execorNum);
		bb.append(onlyUseCharIndex);
		for(int i=0;i<31;i++)
		{
			bb.append((byte)0);
		}	
		
		if(tableName!=null){
			byte tbs[]=tableName.getBytes();
			if(tbs.length>30)
				bb.append(tbs,0,30);
			else
				bb.append(tbs);
			for(int i=tbs.length;i<32;i++)
			{
				bb.append((byte)0);
			}
		}else
			for(int i=0;i<32;i++)
			{
				bb.append((byte)0);
			}
		return bb;
	}
	
	public boolean addRetField(byte fno,byte type)
	{
		if(retfnum==20)return false;
		
		
		
		retFields[retfnum*2]=(byte) fno;
		retFields[retfnum*2+1]=(byte) type;
		
		retfnum++;
		return true;		
	}
	public boolean addDCField(byte fno)
	{
		return addDCField(fno,(byte)0); 
	}
	public boolean addDCField(byte fno,byte type)
	{
		
		if(dCfnum==20)return false;	
			
			
			dCFields[dCfnum*2]=(byte) fno;
			dCFields[dCfnum*2+1]=(byte) type;	
	
		dCfnum++;
		return true;		
	}
	
	public void setTargetFN(String fn)
	{
		byte bs[]=fn.getBytes();
		for(int i=0;i<2;i++)
			targetfn[i]=bs[i];
	}
	public boolean summaryQueryUndefined(){
		if(summaryQuery_txFields[0]==0)
			return true;
		return false;
	}
	
	public static void main(String[] argv)throws Exception
	{
		CritHeader th=new CritHeader(null,1,10);
		
		
		th.addRetField((byte) 1,(byte) 0);
		th.addRetField((byte)3,(byte) 0);
		th.addRetField((byte)2,(byte) 0);
		
		th.addDCField((byte)1);
		
		ByteBuff bb=new ByteBuff();
		th.toByteBuffer(bb);
		
		System.out.println(bb.getUsed());
		System.out.println(bb.toHexString());
		
	}

	public int length() 
	{
		
		return 256;
	}

	public long getGroupBeginValue() 
	{
		
		return groupBegin;
	}
	public long getGroupStepValue()
	{
		return groupStep;
	}
	
	
	final static String orderbystr[]=new String[]{"time","rela","random","copies","time_asc","field_desc ","field_asc ","formula"};
	final static String sumtypestr[]=new String[]{"none","count","estimate"};
	public String toString()
	{
		StringBuffer strb=new StringBuffer();
		switch(type)
		{
		    case 0: strb.append("SEARCH [").append(schbegin).append(",").append(schlen).append("]")
		         .append(" orderby=").append(orderbystr[orderby]);
		         if(orderby==5||orderby==6)
		        	 strb.append("('").append(new String(orderbyfn,0,2)).append("')");
		         if(orderby==7)
		 			strb.append("('").append(formula).append("')");
		 	     strb.append(" sum=").append(sumtypestr[sumtype]);
		         break;
		    case 1:{ 
		    	String tfn=new String(targetfn,0,2);
		    	 FieldInfo fi=req.baseStru.getInfos().find(tfn);
		    	strb.append("GROUP  asknum=").append(schlen>0?String.valueOf(schlen):"ALL");
		        strb.append(" params=[").append(tfn);
		      if(fi.isNumberField())
	            strb.append("(").append(groupBegin).append(",").append(groupStep).append(")");
		      if(retfnum>0){
		    	  strb.append(", sum(");
					for(int i=0;i<retfnum;i++){
						if(i>0)
							strb.append(",");
						strb.append(req.baseStru.getInfos().find(retFields[i+i]).getName());
					}
					strb.append(")");
				}
		      
		      strb.append("]");
	         break;
		    }
		    case 2: strb.append("DISTINCT [").append(schbegin).append(",").append(schlen).append("]")
	         .append(" orderby=").append(orderbystr[orderby]);
		     if(orderby==5||orderby==6)
	        	 strb.append("('").append(new String(orderbyfn,0,2)).append("')");
	         if(orderby==7)
		 			strb.append("('").append(formula).append("')");
		 	 strb.append(" sum=").append(sumtypestr[sumtype])
	         .append(" params=[").append(new String(targetfn,0,2))
	         .append("]");
	         break;		  
		    case 3: 
		    {   	String tfn=new String(targetfn,0,2);
		    	 FieldInfo fi=req.baseStru.getInfos().find(tfn);
		    	strb.append("CUBE  asknum=").append(schlen>0?String.valueOf(schlen):"ALL").append("|").append(cube2f_max>0?String.valueOf(cube2f_max):"ALL")
	         .append(" params=[").append(new String(targetfn,0,2));
		    	if(fi.isNumberField())
	         strb.append("(").append(groupBegin).append(",").append(groupStep).append(")")
	         .append(",").append(new String(cube2fn,0,2));
			      if(retfnum>0){
			    	  strb.append(", sum(");
						for(int i=0;i<retfnum;i++){
							if(i>0)
								strb.append(",");
							strb.append(req.baseStru.getInfos().find(retFields[i+i]).getName());
						}
						strb.append(")");
					}
			      
			      strb.append("]");
		    	}break;
		    case 4: strb.append("KEYWORDS [").append(schbegin).append(",").append(schlen).append("]")
	         .append(" orderby=").append(orderbystr[orderby]);
	           if(orderby==5||orderby==6)
	        	 strb.append("('").append(new String(orderbyfn,0,2)).append("')");
	         if(orderby==7)
		 			strb.append("('").append(formula).append("')");
	         strb.append(" sum=").append(sumtypestr[sumtype])
	         .append(" params=[").append(new String(targetfn,0,2))  .append("]");
	         break;	         
		    case 5: strb.append("WORDCLOUD [").append(schbegin).append(",").append(schlen).append("]")
		    .append(" orderby=").append(orderbystr[orderby]);
	         if(orderby==5||orderby==6)
	        	 strb.append("('").append(new String(orderbyfn,0,2)).append("')");
	         if(orderby==7)
	 			strb.append("('").append(formula).append("')");
	         strb.append(" sum=").append(sumtypestr[sumtype]);
		    strb.append(" params=[").append(new String(targetfn,0,2))
		        .append(",").append(maxKeyWords)
		        .append(",").append(clusterWordNumLimitPerDoc).append("]");
	         break;	         
		    case 6: strb.append("WAM [").append(schbegin).append(",").append(schlen).append("]")
		    .append(" orderby=").append(orderbystr[orderby]);
	         if(orderby==5||orderby==6)
	        	 strb.append("('").append(new String(orderbyfn,0,2)).append("')");
	         if(orderby==7)
	 			strb.append("('").append(formula).append("')");
	         strb.append(" sum=").append(sumtypestr[sumtype]);
		    strb.append("  params=[").append(new String(targetfn,0,2))
		        .append(",").append(maxKeyWords)
		        .append(",").append(clusterWordNumLimitPerDoc).append("]");
	         break;	         
		    case 7: strb.append("CLUSTER [").append(schbegin).append(",").append(schlen).append("]")
		    .append(" orderby=").append(orderbystr[orderby]);
	         if(orderby==5||orderby==6)
	        	 strb.append("('").append(new String(orderbyfn,0,2)).append("')");
	         if(orderby==7)
	 			strb.append("('").append(formula).append("')");
	         strb.append(" sum=").append(sumtypestr[sumtype]);
		    strb.append("  params=[").append(new String(targetfn,0,2))
		    .append(",").append(clusterDistance)
		    .append(",").append(clustermaxloop)
		    .append(",").append(cluster_minwordnum_group)
		    .append(",").append(clusterMaxDocInGroup)
		    .append(",").append(clusterMaxGroup)
		    .append(",").append(clusterWordNumLimitPerDoc)
		    .append(",").append(clusterWordTotalMaxLimit).append("]");
	         break;
		}
		if(tableName!=null && tableName.length()>0){
			 strb.append(" from ").append(tableName.length()<=30?tableName:tableName.substring(0, 30));
		}
		return strb.toString();
	}

	public byte getRetfnum() {
		return retfnum;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setSchbegin(int schbegin) {
		this.schbegin = schbegin;
	}

	public void setSchlen(int schlen) {
		this.schlen = schlen;
	}

	public void setOrderby(byte orderby) {
		this.orderby = orderby;
	}

	public void setSumtype(byte sumtype) {
		this.sumtype = sumtype;
	}

	public void setMatchtype(byte matchtype) {
		this.matchtype = matchtype;
	}

	public void setRetfnum(byte retfnum) {
		this.retfnum = retfnum;
	}

	public void setRetFields(byte[] retFields) {
		this.retFields = retFields;
	}

	public void setTargetfn(byte[] targetfn) {
		this.targetfn = targetfn;
	}

	public void setGroupBegin(long groubBegin) {
		this.groupBegin = groubBegin;
	}

	public void setGroupStep(long groupStep) {
		this.groupStep = groupStep;
	}

	public void setdCfnum(byte dCfnum) {
		this.dCfnum = dCfnum;
	}

	public void setdCFields(byte[] dCFields) {
		this.dCFields = dCFields;
	}

	public void setdCMax(int dCMax) {
		this.dCMax = dCMax;
	}

	public void setMaxKeyWords(short maxKeyWords) {
		this.maxKeyWords = maxKeyWords;
	}

	public void setClusterDistance(double clusterDistance) {
		this.clusterDistance = clusterDistance;
	}

	public void setClustermaxloop(int clustermaxloop) {
		this.clustermaxloop = clustermaxloop;
	}

	public void setCluster_minwordnum_group(int cluster_minwordnum_group) {
		this.cluster_minwordnum_group = cluster_minwordnum_group;
	}

	public void setDistinctKeyDoc(byte distinctKeyDoc) {
		this.distinctKeyDoc = distinctKeyDoc;
	}

	public void setClusterMaxDocInGroup(short clusterMaxDocInGroup) {
		this.clusterMaxDocInGroup = clusterMaxDocInGroup;
	}

	public void setClusterMaxGroup(short clusterMaxGroup) {
		this.clusterMaxGroup = clusterMaxGroup;
	}

	public void setClusterWordNumLimitPerDoc(short clusterWordNumLimitPerDoc) {
		this.clusterWordNumLimitPerDoc = clusterWordNumLimitPerDoc;
	}

	public void setClusterWordTotalMaxLimit(short clusterWordTotalMaxLimit) {
		this.clusterWordTotalMaxLimit = clusterWordTotalMaxLimit;
	}

	public void setCube_type(byte cube_type) {
		this.cube_type = cube_type;
	}

	public void setCube_weight_max(short cube_weight_max) {
		this.cube_weight_max = cube_weight_max;
	}

	public void setCube2fn(byte[] cube2fn) {
		this.cube2fn = cube2fn;
	}

	public void setCube2f_max(int cube2f_max) {
		this.cube2f_max = cube2f_max;
	}

	public void setGroup_type(byte group_type) {
		this.group_type = group_type;
	}

	public void setGroup_weight_max(short group_weight_max) {
		this.group_weight_max = group_weight_max;
	}

	public void setOrderbyfn(byte[] orderbyfn) {
		this.orderbyfn = orderbyfn;
	}

	public void setHeatFuncLen(short heatFuncLen) {
		this.heatFuncLen = heatFuncLen;
	}

	public void setSimhash_threshold(byte simhash_threshold) {
		this.simhash_threshold = simhash_threshold;
	}

	public void setSummaryQuery_txFields(byte[] summaryQuery_txFields) {
		this.summaryQuery_txFields = summaryQuery_txFields;
	}

	public void setSummaryQuery_maxSentence(short summaryQuery_maxSentence) {
		this.summaryQuery_maxSentence = summaryQuery_maxSentence;
	}

	public void setSummaryQuery_endWithSign(byte summaryQuery_endWithSign) {
		this.summaryQuery_endWithSign = summaryQuery_endWithSign;
	}

	public void setFocusOn(short focusOn) {
		this.focusOn = focusOn;
	}

	public void setUpdateSetting(byte updateSetting) {
		this.updateSetting = updateSetting;
	}

	public void setSubCritNum(short subCritNum) {
		this.subCritNum = subCritNum;
	}

	public void setExecorNum(short execorNum) {
		this.execorNum = execorNum;
	}

	public void setHeatFunc(String heatFunc) {
		this.formula = heatFunc;
	}

	public void setQueryRequest(QueryRequest req) {
		this.req = req;
	}

	public int getType() {
		return type;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableName() {
		return tableName;
	}
	
	
}
