package cn.sowjz.souwen.v1.query.txtexpr;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TxtExprVerify {

	String expr;
	public TxtExprVerify(String expr)
	{
		this.expr=expr;
	}
	
	String message;
	int err_loca=-1;
	int err_len=0;
	
	public boolean  isPassed()
	{
		if(expr==null || expr.trim().length()==0)
		{
			message="表达式为空";
			return false;
		}	
		if(tabCheck_doubleQuoation()==false)
			return false;		
		
		
		
		List<TExprEle> phraselist=new ArrayList<TExprEle>();
		List<TExprEle> oplist=new ArrayList<TExprEle>(); 
		Stack<TExprEle> stack=new Stack<TExprEle>();
		
		stack.push(new TExprEle(0,0,0,null));
		TExprToken t=new TExprToken(expr);
		
		boolean lastIsValue=false;
		
		while(t.hasNext())
		{	TExprEle te=t.next();
		    if(te.type==TExprToken.E_AND||te.type==TExprToken.E_NOT||te.type==TExprToken.E_OR)
		    {
		    	if(!lastIsValue)
		    	  return errAt(te);	
		    	lastIsValue=false;
		    }else if(te.type==TExprToken.E_VALUE)	
		    {
		    	if(lastIsValue)
		        {  message="缺少运算符";    	err_loca=te.begin-1;  	err_len=te.len;
		           return false;
		        }
			    lastIsValue=true;
		    }	
		    
		   //System.out.println(te);
		   
		    if(!te.verify())
		    {
		    	message=te.getErrMsg();    	err_loca=te.getErrLoca();  	err_len=te.getErrLen();
		    	return false;
		    }
		    
		    int type=te.type;
//		    int phrasenum=0;
		    if(type==TExprToken.E_VALUE)
			{
				oplist.add(te);
//	            phrasenum++;
				phraselist.add(te);
			}else if(type==TExprToken.E_RIGHT)
			{
			   if(stack.isEmpty()) 
			   { message="符号错误";    	err_loca=te.begin;  	err_len=te.len;
		    	 return false;
		       }
	           for(TExprEle  t1=stack.pop();t1.type!=TExprToken.E_LEFT;)
	           {   oplist.add(t1);
				   
	              if(stack.isEmpty())
	              {  message="符号错误";    	err_loca=te.begin;  	err_len=te.len;
	 		    	 return false;
	              }	  
	              t1=stack.pop();
	           }
			}
			else
			{ if(stack.isEmpty())
			  {  message="符号错误";    	err_loca=te.begin;  	err_len=te.len;
	    	     return false;
               }	 
			TExprEle  op=stack.pop();
	           for(;isp(op.type)>icp(type);)
	           {  oplist.add(op);
	               if(stack.isEmpty())
	               {  message="符号错误";    	err_loca=te.begin;  	err_len=te.len;
		    	     return false;
	               }   
	             op=stack.pop();
	           }
	           stack.push(op);
	           stack.push(te);
			}
		}
//		if(stack.isEmpty())
//		{  message="符号错误";    	err_loca=te.begin;  	err_len=te.len;
//	      return false;
//        } 
		while(!stack.isEmpty())
		{  TExprEle a=stack.pop();
		    if(a.type!=0)
			 oplist.add(a);
	         if(a.type==TExprToken.E_LEFT||a.type==TExprToken.E_RIGHT)
	         {
	        	 message="符号错误";    	err_loca=a.begin;  	err_len=a.len;
	        	 return false;
	         }
		}
		
		return check(oplist);
		  
		
	}
	protected boolean check(List<TExprEle> oplist) 
	{
		
		boolean runArray[]=new boolean[oplist.size()];
		int opnum=oplist.size();
		
		
		
		int rAcur=0;
		int rAsize=opnum*2;
	    for(int i=0;i<opnum;i++)
	    { 
	    	TExprEle  t=oplist.get(i);
		   if(t.type==TExprToken.E_AND)
		   {   if(rAcur==0)return errAt(t);
		       --rAcur;
			   if(rAcur==0)return errAt(t);
			   --rAcur;
			    runArray[rAcur++]=(true);
				if(rAcur>=rAsize)return errAt(t);
		   }
		   else if(t.type==TExprToken.E_NOT)
		   {   if(rAcur==0)return errAt(t);
		   --rAcur;
			   if(rAcur==0)return errAt(t);
			   --rAcur;
			    runArray[rAcur++]=(true);
				if(rAcur>=rAsize)return errAt(t);
		   }
		   else if(t.type==TExprToken.E_OR)
		   {
			   if(rAcur==0)return errAt(t);
			   --rAcur;
			   if(rAcur==0)return errAt(t);
			   --rAcur;
			    runArray[rAcur++]=(true);
				if(rAcur>=rAsize)return errAt(t);
		   }
		   else if(t.type>=TExprToken.E_VALUE)
		   {   runArray[rAcur++]=(true);
				if(rAcur>=rAsize)return errAt(t);
		   }
		   else
		   {   
			   return errAt(t);
		   }

	    }
	    if(rAcur!=1)
	    {	
	    	 message="存在不能定位错误，建议缩短表达式排除错误";    	err_loca=-1;  	err_len=0;
	    	return false;
	    
	    }
		return true;
	}
	public boolean errAt(TExprEle t) 
	{
		 message="符号错误";    	err_loca=t.begin;  	err_len=t.len;
    	 return false;
	}
	public static  int isp(int a)
	{
		switch(a)
		{
		case 0:return 0;
		case TExprToken.E_LEFT:return 1;
		case TExprToken.E_AND:return 5;
		case TExprToken.E_NOT:return 5;
		case TExprToken.E_OR:return 3;
		case TExprToken.E_RIGHT:return 8;

		}
		return 0;
	}
	public static  int icp(int a)
	{
		switch(a)
		{
		case 0:return 0;
		case TExprToken.E_LEFT:return 8;
		case TExprToken.E_AND:return 4;
		case TExprToken.E_NOT:return 4;
		case TExprToken.E_OR:return 2;
		case TExprToken.E_RIGHT:return 1;
		}
		return 0;
	}
	public boolean tabCheck_doubleQuoation() 
	{
		int num=0;
		for(int i=0;i<expr.length();i++)
		{
			char c=expr.charAt(i);
//			if(c=='\t')
//			{
//				err_loca=i;
//				err_len=1;
//				message="表达式中含有TAB字符，请用空格键替换";
//				return false;
//			}	
			if(c=='"')
				num++;
		}	
		
		if((num%2)==1){
			message="双引号没有成对出现";
			return false;
		}
		
		return true;
	}
	public String getErrMessage(String prefix,String subfix)
	{
		StringBuffer strb=new StringBuffer();
		strb.append(message);
		if(err_loca>=0)
		{
			strb.append(" ：  ");
			int begin=err_loca-8;
			if(begin<0)
				begin=0;
			strb.append(expr.substring(begin, err_loca));
			strb.append(prefix);
			if(err_len>0)
				strb.append(expr.substring( err_loca,err_loca+err_len));
			else
				strb.append(" ");
			int end=err_loca+err_len+8;
			if(end>expr.length())
				end=expr.length();
			strb.append(subfix);
			strb.append(expr.substring( err_loca+err_len,end));
		} 	
		return strb.toString();
	}
	public static void main(String [] argv)throws Exception
	{
		//String expr="北京+上+田径 (篮球|球) NEAR~10.2(\"win nt\",安卓,IOS)";
		//String expr="北京||上海+田径 (篮球|球) FUZZY2,3(\"win nt\",安卓,IOS)";
		//String expr="(\"HOUSE OF VANS\")";
		String expr="//@";
		TxtExprVerify tv=new TxtExprVerify(expr);
		if(tv.isPassed())
			System.out.println(" passed");
		else
			System.out.println(tv.getErrMessage(" >>","<< "));
	}
}
