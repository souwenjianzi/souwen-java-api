package cn.sowjz.souwen.v1.query.txtexpr;


public class TxtExprReplace {
	
	String expr;
	public TxtExprReplace(String expr) {
		this.expr=expr;
	}


	public static class ReplacePolicy{
		
		public String word(String w)
		{
			return w;
		}
		
		public String getErrHitPrefix(){
			return " >>";
		}
		public String getErrHitSubfix(){
			return "<< ";
		}
	}
	
	
	String errMsg;
	public String replace(ReplacePolicy replacePolicy) {
		TxtExprVerify  tv=new TxtExprVerify(expr);
		if(!tv. isPassed())
		{
			errMsg=tv.getErrMessage(replacePolicy.getErrHitPrefix(),replacePolicy.getErrHitSubfix() );
			return null;
		}	
		
		
		StringBuffer strb=new StringBuffer();
		TExprToken t=new TExprToken(expr);
		while(t.hasNext())
		{	
			TExprEle ele = t.next();
			switch(ele.type)
			{
			case TExprToken.E_AND: strb.append("+");break;
			case TExprToken.E_LEFT:strb.append("(");break;
			case TExprToken.E_NOT:strb.append("-");break;
			case TExprToken.E_OR: strb.append("|");break;
			case TExprToken.E_RIGHT:strb.append(")");break;
			case TExprToken.E_VALUE: strb.append(replacePolicy.word(ele.value));break;
			}
			
		}
		return strb.toString();
	}
	
	public String getErrorMessage() {
		return errMsg;
	}
	
	public static void main(String [] argv)throws Exception
	{
		//String expr="北京+上海-+田径(篮球|球) ANY(\"win nt\",安卓,IOS)";
		String expr="北京|android|上海+田径 (篮球|球) FUZZY2,3(\"win nt\",安卓,IOS,android)";
		TxtExprReplace tr=new TxtExprReplace(expr);
		
		String  new_one=tr.replace(new ReplacePolicy(){

			@Override
			public String word(String w) {
				System.out.println("word:"+w);
				if("android".equals(w))
					return "(android|安卓)";
				else if("球".equals(w))
					return "(ball|球)";
				return w;
			}
			
		});
		if(new_one!=null)
			System.out.println("new expr"+new_one);
		else
			System.out.println("ERROR:"+tr.getErrorMessage());
		
	}



	


	

	
}
