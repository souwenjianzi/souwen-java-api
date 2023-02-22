package cn.sowjz.souwen.v1.query.request;


import cn.sowjz.souwen.v1.BaseStructure;
import cn.sowjz.souwen.v1.db.struct.FieldInfo;
import cn.sowjz.souwen.v1.util.ByteBuff;
import cn.sowjz.souwen.v1.util.DocValueHelper;
import cn.sowjz.souwen.v1.util.String2Bytes;

public class QueryRequest {
	BaseStructure baseStru;
	
	protected CritHeader header;
	
	
	String charset;
	String2Bytes str2bytes;
	

	

	
	
	

	
	
	public final static int AND_OP	=-1;
	public final static int OR_OP	=-2;
	public final static int AND_NOT_OP	=-3;
	public final static int OR_NOT_OP	=-4;
	
	
	protected DocValueHelper docValueHelper;
	protected QueryRequest(){
		docValueHelper=new DocValueHelper();
	}
	
	public QueryRequest(BaseStructure ss)
	{
		this(ss, OrderBy.time, SumType.none);
	}
	public QueryRequest(BaseStructure ss,  SumType sumtype)
	{
		this(ss, OrderBy.time, sumtype);
	}
	public QueryRequest(BaseStructure ss, OrderBy orderby, SumType sumtype) {
		if (null == ss.getInfos())
			throw new RuntimeException(
					"the System FieldInofs is null. please check the is full db exist?");
		this.baseStru = ss;
		
		header =new CritHeader(this,0,0);
		
		header.orderby=(byte) orderby.ordinal();
		header.sumtype=(byte) sumtype.ordinal();
		header.schlen=20;
		
		
		
		charset=ss.getCharset();
		str2bytes=String2Bytes.getInstance(charset);
		
		docValueHelper=new DocValueHelper();
	}

	public BaseStructure getBaseStructure(){
		return baseStru;
	}
	public CritHeader getHeader(){return header;}
	public void setQueryType(int type)
	{
		header.type=type;
	}


	
	public void setAskNum(int askNum) {
		header.schlen=askNum;
	}

	public int getAskNum()
	{
		return header.schlen;
	}


	public void setSumType(SumType sumtype) {
		header.sumtype=(byte) sumtype.ordinal();
	}




	public void setStart(int start) {
		header.schbegin=start;
	}
	


	public boolean addReturnField(FieldInfo info) throws Exception
	{


		boolean rf=header.addRetField((byte) info.getSn(),(byte) info.getType());
		if(!rf)
			throw new Exception("can not set any more return field "+info.getName());
		return rf;
	}

	






	// -----------------------------------------------------------------
	public ByteBuff toByteBuff() throws Exception {
		
		
		
		ByteBuff buf = new ByteBuff(1024);
		
		byte []hf=null;
		
		int subCritNum= root.critCount();
		if(subCritNum>1000)
			throw new Exception("too many subCrit");
		header.subCritNum=(short)subCritNum;
		
		if(header.orderby==7)
		{	
			if(header.formula==null || header.formula.length()==0)
				throw new Exception("heat function undefined");
			hf=header.formula.getBytes();
			header.heatFuncLen=(short) (hf.length+1);
		}
		header.toByteBuffer(buf);
		
		root.toByteBuffer(buf);

		if(hf!=null)
		{	buf.append(hf);buf.append((byte)0);
		}
		return buf;
	}

	// -----------------------------------------------------------------
	

	
	

	
	

	public static enum OrderBy{
		time,rela,random,reserved,time_asc,field_desc,field_asc,formula;
		
	}
	public static enum SumType{
		none,count,estimate;
	}
	


	

	public String toString()
	{
		
		StringBuffer strb=new StringBuffer();
		
		strb.append(header.toString());
	
		
		strb.append(" where ");

		root.toStringBuffer(strb);

	
		return strb.toString();
	}
	
	

	public CritHeader getCritHeader() 
	{
		
		return header;
	}

	public void setOrderByTime() {
		header.orderby=(byte) OrderBy.time.ordinal();
	}
	public void setOrderByRela() {
		header.orderby=(byte) OrderBy.rela.ordinal();
	}
	public void setOrderByRamdom() {
		header.orderby=(byte) OrderBy.random.ordinal();
	}
	
	public void setOrderByTimeAse() {
		header.orderby=(byte) OrderBy.time_asc.ordinal();
	}
	public void setOrderByFieldDesc(String fieldname) {
		FieldInfo info = baseStru.getInfos().find(fieldname);
		if (null == info)
			throw new IllegalArgumentException("there is no field named: "
					+ fieldname);
		if (!(info.isIntField()||info.isInt64Field()))
			throw new IllegalArgumentException(
					"orderByField operate must be with a int field."+fieldname);
		
		insertStringTo(header.orderbyfn,0,fieldname,2);
		header.orderby=(byte) OrderBy.field_desc.ordinal();
	}
	public void setOrderByFieldAsc(String fieldname) {
		FieldInfo info = baseStru.getInfos().find(fieldname);
		if (null == info)
			throw new IllegalArgumentException("there is no field named: "
					+ fieldname);
		if (!(info.isIntField()||info.isInt64Field()))
			throw new IllegalArgumentException(
					"orderByField operate must be with a int field."+fieldname);
		
		insertStringTo(header.orderbyfn,0,fieldname,2);
		header.orderby=(byte) OrderBy.field_asc.ordinal();
	}
	public void setOrderByFormula(String formula) {
		header.orderby=(byte) OrderBy.formula.ordinal();
		header.formula=formula;
	}
	

	
	public OrderBy getOrderBy(){
		 return OrderBy.values()[header.orderby];
	}
	
	
	
	public String getFormula() {
		return header.formula;
	}
	

	
	
	public Criteria createCriteria(){
		if(root==null)
			root= new  SubCrit(this);
		return root;
	}
	
	public SubCrit createSubCrit(){
		return new SubCrit(this);
	}
	


	
	
	SubCrit root=null;
	public void mergeAnd(SubCrit bt){
		if(root==null ||root.isEmpty())
			root=bt;
		else{
		
			root= root.mergeAnd(bt);
		}
	}
	
	public void and(SubCrit bt){
		if(root==null)
			root=bt;
		else
			root=root.mergeAnd(bt);
		
	}

	public void or(SubCrit bt){
		if(root==null)
			root=bt;
		else 
			root=root.or(bt);
	
	}
	public void andNot(SubCrit bt){
		if(root==null)
			root=bt;
		else 
			root=root.andNot(bt);
		
	}
	public void orNot(SubCrit bt){
		if(root==null)
			root=bt;
		else 
			root=root.orNot(bt);
	}
	
	protected void insertStringTo(byte[] ftv,int begin, String v,int strlen) 
	{
		byte[] bb=v.getBytes();
		
		for(int i=0;i<strlen;i++)
		{
			ftv[begin+i]=bb[i];
		}	
	}
	


	
	


	public void setOnlyUseCharIndex(boolean onlyUseCharIndex){
		header.onlyUseCharIndex=(byte) (onlyUseCharIndex?1:0);
	}
	
	public byte[]getFieldCritValue(String fieldname,byte operator){
		if(root==null)return null;
		return root.getFieldCritValue(fieldname, operator);
	}	
	
}
