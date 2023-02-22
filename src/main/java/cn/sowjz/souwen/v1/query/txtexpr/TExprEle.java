package cn.sowjz.souwen.v1.query.txtexpr;

public class TExprEle
{
	int type;
	int begin;
	int len;
	String value;

	public TExprEle(int t,int b,int l,String v)
	{
		type=t;
		begin=b;
		len=l;
		value=v;
	}

	
	
	public String toString()
	{
		return TExprToken.eleType[type]+" <"+begin+","+len+"> "+value;
	}



	String err_msg;
	int err_loca;
	int err_len;
	public boolean verify() 
	{
		if(type==TExprToken.E_ERROR)
		{
			err_msg="存在不被接受的字符";err_loca=0;	err_len=len;
			return false;
		}	
		if(type==TExprToken.E_VALUE)
		{
//			if(value.startsWith("/"))
//			{
//				err_msg="存在错误的字符";
//				err_loca=0;
//				err_len=1;
//				return false;
//			}	
			if(value.startsWith("ANY"))
				return verify_Any();
			if(value.startsWith("NEAR"))			
		        return verify_Near();
			if(value.startsWith("FUZZY"))			
		        return verify_Fuzzy();
		}	
		
		return true;
	}



	private boolean verify_Fuzzy() {
		int a=value.indexOf("(");
		if(a<0)
			return true;
		if(a==5)
		{
			err_msg="Fuzzy函数定义不完整";err_loca=0;err_len=6;
			return false;
		}else
		{
			int d=5;
			if(value.charAt(d)=='_')
				d++;
			if(d==a)
			{
				err_msg="Fuzzy函数定义不完整";	err_loca=0;	err_len=7;
				return false;
			}	
			//if(!isDigit(value,d,a))
			{
				int e=find(value,d,a,'/');
				int f=find(value,d,a,',');
				if(e==-1&&f==-1||e==d||f==d)
				{
					err_msg="Fuzzy函数中不能识别的字符";err_loca=d;err_len=a-d;
					return false;
				}	
				int g=(e>0)?e:f;
						
				if(!isDigit(value,d,g))
				{  err_msg="Fuzzy函数中不能识别的字符";err_loca=d;	err_len=g-d;
				   return false;
				}	
				if(!isDigit(value,g+1,a))
				{  err_msg="Fuzzy函数中不能识别的字符";err_loca=g+1;	err_len=a-g-1;
				   return false;
				}
			}
		}	
		int b=value.indexOf(")",a);
		if(b<0)
			return true;
		if(b!=value.length()-1)
		{
			err_msg="存在错误的字符";
			err_loca=b+1;
			err_len=value.length()-b-1;
			return false;
		}	
		if(verifyFuncParams(a+1,value.substring(a+1, b))==false)
		{
			
			return false;
		}	
		return true;
	}



	private boolean verify_Near() {
		int a=value.indexOf("(");
		if(a<0)
			return true;
		if(a==4)
		{
			err_msg="Near函数定义不完整";
			err_loca=0;
			err_len=5;
			return false;
		}else
		{
			int d=4;
			char dc=value.charAt(d);
			if(dc=='_'||dc=='~')
				d++;
			if(d==a)
			{
				err_msg="Near函数定义不完整";	err_loca=0;	err_len=6;
				return false;
			}	
			if(dc=='~' && !isIn(value,d,a,"0123456789.")){
				err_msg="Near函数中不能识别的字符";err_loca=d;err_len=a-d;
				return false;
			}
			else if(dc!='~' &&!isDigit(value,d,a))
			{
				int e=find(value,d,a,'/');
				if(e==-1||e==d)
				{
					err_msg="Near函数中不能识别的字符";err_loca=d;err_len=a-d;
					return false;
				}	
				if(!isDigit(value,d,e))
				{  err_msg="Near函数中不能识别的字符";err_loca=d;	err_len=e-d;
				   return false;
				}	
				if(!isDigit(value,e+1,a))
				{  err_msg="Near函数中不能识别的字符";err_loca=e+1;	err_len=a-e-1;
				   return false;
				}
			}
			
		}	
		int b=value.indexOf(")",a);
		if(b<0)
			return true;
		if(b!=value.length()-1)
		{
			err_msg="存在错误的字符";
			err_loca=b+1;
			err_len=value.length()-b-1;
			return false;
		}	
		if(verifyFuncParams(a+1,value.substring(a+1, b))==false)
		{
			
			return false;
		}	
		return true;
	}



	private boolean isIn(String txt, int begin, int end, String vv) {
		if(begin==end)return false;
		for(int i=begin;i<end;i++)
		{
			char c=txt.charAt(i);
			if(vv.indexOf(c)<0)
				return false;
		}	
		return true;
	}



	private int find(String txt, int begin, int end, char ch) {
		
		for(int i=begin;i<end;i++)
		{
			if(txt.charAt(i)==ch)
				return i;
		}	
		return -1;
	}



	private boolean isDigit(String txt, int begin, int end)
	{
		if(begin==end)return false;
		for(int i=begin;i<end;i++)
		{
			char c=txt.charAt(i);
			if(c<'0'||c>'9')
				return false;
		}	
		return true;
	}



	private boolean verify_Any() {
		int a=value.indexOf("(");
		if(a<0)
			return true;
		if(a==3)
		{
			err_msg="Near函数定义不完整,需要定义命中数";
			err_loca=3;
			err_len=a-3;
			return false;
		}else
		{
			int d=3;
			
			if(!isDigit(value,d,a))
			{
					err_msg="ANY函数中不能识别的字符";err_loca=d;err_len=a-d;
					return false;
			}
		}	
		int b=value.indexOf(")",a);
		if(b<0)
			return true;
		if(b!=value.length()-1)
		{
			err_msg="存在错误的字符";
			err_loca=b+1;
			err_len=value.length()-b-1;
			return false;
		}	
		if(verifyFuncParams(a+1,value.substring(a+1, b))==false)
		{
			
			return false;
		}	
		return true;
	}



	private boolean verifyFuncParams(int start, String txt)
	{
		//System.out.println(txt);
		//do nothing
		return true;
	}



	public String getErrMsg() 
	{
		return err_msg;
	}



	public int getErrLoca() {
		
		return err_loca+begin;
	}



	public int getErrLen() {
		
		return err_len;
	}
}
