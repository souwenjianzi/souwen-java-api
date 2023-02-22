package cn.sowjz.souwen.v1.query.request;

import cn.sowjz.souwen.v1.util.ByteBuff;


public class SubCrit extends Criteria{
//	protected SubCrit(){
//		
//	}
	
//	public abstract int count() ;
//
//	public abstract void toByteArrayForCache(ByteBuffer buf) throws Exception;
//	public abstract void toByteBuffer(ByteBuffer buf) throws Exception;
//	public abstract void toStringBuffer(StringBuffer strb) ;

	
	protected SubCrit(QueryRequest request) {
		super(request);
	}
	
	
	public SubCrit mergeAnd(SubCrit bt){
		if(bt.isSingle() && this.isSingle())
		{
			if(((SubCrit)this).merge((SubCrit)bt))
				return this;
		}	
		AndSubCrit and=new AndSubCrit(request);
		and.one=this;
		and.two=bt;
		return and;
	}
	public SubCrit and(Criteria bt){
		AndSubCrit and=new AndSubCrit(request);
		and.one=this;
		and.two=bt;
		return and;
	}
	public SubCrit or(Criteria bt){
		OrSubCrit or=new OrSubCrit(request);
		or.one=this;
		or.two=bt;
		return or;
	}
	public SubCrit andNot(Criteria bt){
		AndNotSubCrit and=new AndNotSubCrit(request);
		and.one=this;
		and.two=bt;
		return and;
	}
	public SubCrit orNot(Criteria bt){
		OrNotSubCrit or=new OrNotSubCrit(request);
		or.one=this;
		or.two=bt;
		return or;
	}
	public String toString()
	{
		
		StringBuffer strb=new StringBuffer();
		toStringBuffer(strb);
		return strb.toString();
	}

	public boolean isEmpty() {
		return false;
	}
	
	public boolean isSingle(){return true;}
	
	
	private class AndSubCrit extends SubCrit{
		private AndSubCrit(QueryRequest request) {
			super(request);
		}
		Criteria one;
		Criteria two;
	
		@Override
		public void toByteBuffer(ByteBuff buf) throws Exception {
			one.toByteBuffer(buf);
			two.toByteBuffer(buf);
			buf.append(QueryRequest.AND_OP);
		}
		@Override
		public void toStringBuffer(StringBuffer strb) {
			strb.append("(");
			one.toStringBuffer(strb);
			strb.append(") AND (");
			two.toStringBuffer(strb);
			strb.append(")");
		}
		@Override
		public int critCount() {
			
			return (one.critCount()+two.critCount());
		}
		@Override
		public byte[] getFieldCritValue(String fieldname, byte operator) {
			
			byte ba[]=one.getFieldCritValue(fieldname, operator);
			if(ba!=null)
				return ba;
			return two.getFieldCritValue(fieldname, operator);
		}
		@Override
		public boolean isSingle(){return false;}
	}
	private class OrSubCrit extends SubCrit{
		private OrSubCrit(QueryRequest request) {
			super(request);
		}
		Criteria one;
		Criteria two;
	
		@Override
		public void toByteBuffer(ByteBuff buf) throws Exception {
			one.toByteBuffer(buf);
			two.toByteBuffer(buf);
			buf.append(QueryRequest.OR_OP);
		}
		@Override
		public void toStringBuffer(StringBuffer strb) {
			strb.append("(");
			one.toStringBuffer(strb);
			strb.append(") OR (");
			two.toStringBuffer(strb);
			strb.append(")");
		}
		@Override
		public int critCount() {
			return  (one.critCount()+two.critCount());
		}
		@Override
		public byte[] getFieldCritValue(String fieldname, byte operator) {
			
			byte ba[]=one.getFieldCritValue(fieldname, operator);
			if(ba!=null)
				return ba;
			return two.getFieldCritValue(fieldname, operator);
		}
		@Override
		public boolean isSingle(){return false;}
	}
	private class AndNotSubCrit extends SubCrit{
		private AndNotSubCrit(QueryRequest request) {
			super(request);
		}
		Criteria one;
		Criteria two;
		
		@Override
		public void toByteBuffer(ByteBuff buf) throws Exception {
			one.toByteBuffer(buf);
			two.toByteBuffer(buf);
			buf.append(QueryRequest.AND_NOT_OP);
		}
		@Override
		public void toStringBuffer(StringBuffer strb) {
			strb.append("(");
			one.toStringBuffer(strb);
			strb.append(") AND NOT (");
			two.toStringBuffer(strb);
			strb.append(")");
		}
		@Override
		public int critCount() {
			
			return (one.critCount()+two.critCount());
		}
		@Override
		public byte[] getFieldCritValue(String fieldname, byte operator) {
			
			byte ba[]=one.getFieldCritValue(fieldname, operator);
			if(ba!=null)
				return ba;
			return two.getFieldCritValue(fieldname, operator);
		}
		@Override
		public boolean isSingle(){return false;}
	}
	private class OrNotSubCrit extends SubCrit{
		private OrNotSubCrit(QueryRequest request) {
			super(request);
		}
		Criteria one;
		Criteria two;
	
		@Override
		public void toByteBuffer(ByteBuff buf) throws Exception {
			one.toByteBuffer(buf);
			two.toByteBuffer(buf);
			buf.append(QueryRequest.OR_NOT_OP);
		}
		@Override
		public void toStringBuffer(StringBuffer strb) {
			strb.append("(");
			one.toStringBuffer(strb);
			strb.append(") OR NOT (");
			two.toStringBuffer(strb);
			strb.append(")");
		}
		@Override
		public int critCount() {
			return  (one.critCount()+two.critCount());
		}
		@Override
		public byte[] getFieldCritValue(String fieldname, byte operator) {
			
			byte ba[]=one.getFieldCritValue(fieldname, operator);
			if(ba!=null)
				return ba;
			return two.getFieldCritValue(fieldname, operator);
		}
		@Override
		public boolean isSingle(){return false;}
	}
}
