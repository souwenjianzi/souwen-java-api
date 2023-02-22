package cn.sowjz.souwen.v1.query.txtexpr;

public class TExprToken {

	
	String string;
	int _len;
	int _p=0;
	
	TExprEle curele;
	TExprEle nextele=null;
	
	
	//String _value;
	int _type=0;
	//int _type2=0;
	
	
	public TExprToken(String expr)
	{
		string=expr;
		_len=expr.length();
		
	}
	
	public final static int  E_ERROR =	0;
	public final static int E_AND	=	1;
	public final static int E_OR	=	2;
	public final static int E_LEFT	=	3;
	public final static int E_RIGHT	=	4;
	public final static int E_NOT	=	5;
	public final static int E_VALUE	=	6;	
	public final static String [] eleType=new String[]{"E_ERROR","E_AND","E_OR","E_LEFT","E_RIGHT","E_NOT","E_VALUE"};
	
	public TExprEle next()
	{  
		
	  
	   return curele;
	}
	
	
	public boolean hasNext()
	{
		if(nextele!=null)
	 	{   curele=nextele;
	 	    nextele=null; 
	 	    _type=curele.type;
			return true;
		}
		
		if(_p>=_len)return false;

		int lasttype=_type;

	   char s1=string.charAt(_p);
	   while(s1==' ')
	   {   _p++;
	   	   if(_p>=_len) return false;	
	       s1= string.charAt(_p);
	   }
	   if(_p>=_len) return false;

	   char c=string.charAt(_p);

       if(c=='-')
	   {   _type=E_NOT; 
	      if(lasttype==0||lasttype==E_LEFT||lasttype==E_NOT||lasttype==E_AND ||lasttype==E_OR)
	       { _type=E_ERROR;  curele=new TExprEle(_type,_p,1,string.substring(_p,_p+1)); _p++; return true;} 
	      curele=new TExprEle(_type,_p,1,string.substring(_p,_p+1));
	       _p++;return true;
	   }   
	   else if(c=='+')
	   {   _type=E_AND; 
	      if(lasttype==0||lasttype==E_LEFT||lasttype==E_AND ||lasttype==E_NOT||lasttype==E_OR)
	      { _type=E_ERROR;curele=new TExprEle(_type,_p,1,string.substring(_p,_p+1));  _p++;return true;}
	      curele=new TExprEle(_type,_p,1,string.substring(_p,_p+1));
	       _p++;return true;
	   }   
	   else if(c=='(')
	   { 
		   if(lasttype==E_RIGHT||lasttype==E_VALUE)
		   { _type=E_AND; curele=new TExprEle(_type,_p-1,1,string.substring(_p-1,_p)); return true;} 
		   _type=E_LEFT; curele=new TExprEle(_type,_p,1,string.substring(_p,_p+1));
		   _p++;return true;
	   }
	   else if(c==')') 
	   {
		    if(lasttype==0||lasttype==E_LEFT||lasttype==E_AND||lasttype==E_NOT ||lasttype==E_OR)
		    { _type=E_ERROR; curele=new TExprEle(_type,_p,1,string.substring(_p,_p+1)); _p++;return true;} 
		   _type=E_RIGHT; curele=new TExprEle(_type,_p,1,string.substring(_p,_p+1)); _p++;return true;
	   }
	   else if(c=='|') 
	   {_type=E_OR;
	     if(lasttype==0||lasttype==E_LEFT||lasttype==E_AND ||lasttype==E_NOT||lasttype==E_OR)
	     { _type=E_ERROR; curele=new TExprEle(_type,_p,1,string.substring(_p,_p+1)); _p++;return true;}
	     curele=new TExprEle(_type,_p,1,string.substring(_p,_p+1));
	     _p++;return true;
	   }
	   else
	   {
		   if(_type==E_VALUE||_type==E_RIGHT)
		   { _type=E_AND;  curele=new TExprEle(_type,_p,0," "); return true;
		   }
		   _type=E_VALUE;
		   int v_begin=_p;
		  
		  

		   while(_p<_len)
		   {  char c1=string.charAt(_p);
		      int clen=1;

		     if(c1==' ')
			 { 
			    curele=new TExprEle(_type,v_begin,_p-v_begin,string.substring(v_begin,_p));
			    _p+=clen;
			    return true;
			 }
			 if(c1=='+')
			 { 
				 curele=new TExprEle(_type,v_begin,_p-v_begin,string.substring(v_begin,_p));
				 nextele=new TExprEle(E_AND,_p,1,string.substring(_p,_p+1));
				 _p+=clen;return true;

			 } if(c1=='-')
			 { 
				 curele=new TExprEle(_type,v_begin,_p-v_begin,string.substring(v_begin,_p));
				 nextele=new TExprEle(E_NOT,_p,1,string.substring(_p,_p+1));
				 _p+=clen;return true;

			 }
			 if(c1=='|')
			 { 
				 curele=new TExprEle(_type,v_begin,_p-v_begin,string.substring(v_begin,_p));
				 nextele=new TExprEle(E_OR,_p,1,string.substring(_p,_p+1));
				 _p+=clen;return true;

			 }
			 if(c1=='(')
			 { 
				 curele=new TExprEle(_type,v_begin,_p-v_begin,string.substring(v_begin,_p));
				 nextele=new TExprEle(E_LEFT,_p,1,string.substring(_p,_p+1));
				 _p+=clen;return true;
			 }	 
			 if(c1==')')
			 { 
				 curele=new TExprEle(_type,v_begin,_p-v_begin,string.substring(v_begin,_p));
				 nextele=new TExprEle(E_RIGHT,_p,1,string.substring(_p,_p+1));
				 _p+=clen;return true;
			 }

			  if(c1=='\"')
	          {  
				  int __p=_p+1;
				  while(__p<_len){
					  if(string.charAt(__p)=='\"'){
						  _p=__p;
						  break;
					  }
					  __p++;
				  }
	          }

			  if(c1=='A' && _len-_p>4)
			  {
				  int __p=_p+1;
				  if(string.charAt(__p++)=='N' &&string.charAt(__p++)=='Y' )
				  {	  
					  for(int i=__p;i<_len;i++)
					  {
						  char ci=string.charAt(i);
						  if(ci==' '||ci=='-'||ci=='+'||ci=='|')
							  break;
						  if(ci=='(')
						  {
							  
							  int p=findRightBracket(string,i+1);
							  if(p>0)
							    _p=p;
							  
							  break;
						  }	  
					  }	  
					 
				  }
			  }
			  else if(c1=='N'&& _len-_p>5)
			  {
				  int __p=_p+1;
				  if(string.charAt(__p++)=='E' &&string.charAt(__p++)=='A'&&string.charAt(__p++)=='R' )
				  {	  
					  for(int i=__p;i<_len;i++)
					  {
						  char ci=string.charAt(i);
						  if(ci==' '||ci=='-'||ci=='+'||ci=='|')
						   break;
						  if(ci=='(')
						  {
							  int p=findRightBracket(string,i+1);
							  if(p>0)
								  _p=p;
						  
							  break;
						  }	  
					  }	  
				  }
			  }
			   else if(c1=='F'&& _len-_p>6)
			  {
				  int __p=_p+1;
				  if(string.charAt(__p++)=='U' &&string.charAt(__p++)=='Z'&&string.charAt(__p++)=='Z'&&string.charAt(__p++)=='Y' )
				  {	 
					  for(int i=__p;i<_len;i++)
					  {
						  char ci=string.charAt(i);
						  if(ci==' '||ci=='-'||ci=='+'||ci=='|')
						   break;
						  if(ci=='(')
						  {
							  int p=findRightBracket(string,i+1);
							  if(p>0)
								  _p=p;
						  
							  break;
						  }	  
					  }	  
				  }
			  }
			 _p+=clen;


		   }
		   curele=new TExprEle(_type,v_begin,_p-v_begin,string.substring(v_begin,_p));
	   }
	   return true;

	}
	private int findRightBracket(String txt, int begin) {
		int len=txt.length();
		boolean inQ=false;
		for(int i=begin;i<len;i++){
			char c=txt.charAt(i);
			switch(c){
			case '"': inQ=!inQ; break;
			case ')':if(!inQ){
					return i;
				}
			}
		}
		
		return -1;
	}


	public int getType()
	{
	  return _type;
	}
	
	
	public static void main(String [] argv)throws Exception
	{
		//String expr="北京+上海-+田径(篮球|球) ANYa(\"win )nt\",安卓,IOS)";
		//String expr="ANYa(\"win )nt\",安卓,IOS)+ds";
		String expr="(\"HOUSE OF VANS\")";
		TExprToken t=new TExprToken(expr);
		while(t.hasNext())
			System.out.println(t.next());
	}
}
