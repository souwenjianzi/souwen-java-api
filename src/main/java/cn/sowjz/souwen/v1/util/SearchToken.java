package cn.sowjz.souwen.v1.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



public class SearchToken 
{
	
     public  static List<String> split(String searchstr)
     {
    	 if(searchstr==null)
    		 return null;
    	 Set<String> l=new HashSet<String>();
    	 int slen=searchstr.length();
     	 
    	 StringBuffer strb=null;
    	 StringBuffer strb2=new StringBuffer();
    	 boolean inQ=false;
    	 for(int i=0;i<slen;i++)
    	 {
    		 char c=searchstr.charAt(i);
    		 if(c=='"'){
    			 inQ=!inQ;
    			 if(inQ)
    				 strb=new StringBuffer();
    			 else{
    				if(i<slen-1 && searchstr.charAt(i+1)=='"'){
    					strb.append("\"");
    					i++;
    				}else{ 
    					l.add(strb.toString());
    					strb=null;
    				}	 
    			 }
    		 }else {
    			 if(inQ){
    				 strb.append(c);
    			 }else
    				 strb2.append(c);
    		 }
    	 } 
    	 
    	 
    	 if(strb!=null)
    		 l.add(strb.toString());
    	 
    	 split(strb2.toString(),l);
    	 return new ArrayList<String>(l);
     }	 
     private static  void split(String searchstr,Set<String> l)
     { 
    	 int slen=searchstr.length();
    	 int begin=0;
    	 for(int i=0;i<slen;i++)
    	 {
    		 char c=searchstr.charAt(i);
    		 if(c==' '|| c=='+'|| c=='-'|| c=='|'|| c=='('|| c==')'|| c=='*'||c==','||c=='{'||c=='}'||c=='['||c==']'||c=='%')
    		 {
    			 if(i>begin)
    			 {
    				 String k=searchstr.substring(begin, i);
    				 int klen=k.length();
    				 if(klen>2)
    				 {
    					 
    					 if( isDigit(k.charAt(klen-1)))
    					 {
    						 int p=k.indexOf("/");
    						 if(p>0){
    							 k=k.substring(0,p);
    							 l.add(k);
    							 begin=i+1;	 
    							 continue;
    						 }
    					 }	 
    				 }	 
    				 l.add(k);
    				 begin=i+1;	 
					 continue;
    			 }	 
    			 if(c=='*')
    			 {
    				 if(i<slen-1 && searchstr.charAt(i+1)=='[')
    				 {
    					int jend=i;
    					 
    					 for(int j=i+2;j<slen;j++)
    					 {
    						 char cj=searchstr.charAt(j);
    						 if(cj==']')
    						 {
    							 jend=j;break;
    						 }	 
    						 if(!(isDigit(cj)||isOperator(cj)))
    							 break;
    					 }
    					 if(jend>i)
    						 i=jend;
    				 }		 
    			 }	 
    			 begin=i+1;	 
    		 }
    		
    		 else	if(begin==i)
    		 {
    			 if(c=='A')
    			 {
    				 int p=forAny(searchstr,i,l);
    				 if(p>0)
    				 {	 i=p;
    				   begin=i+1;
    				 }  
    			 }	 
    			 else if(c=='N')
    			 { int p=forNear(searchstr,i,l);
				 	if(p>0)
				 	{	 i=p;
				 		begin=i+1;
				 	}  
				 }	 
    			 else if(c=='F')
    			 {
    				 int p=forFuzzy(searchstr,i,l);
 				 	if(p>0)
 				 	{	 i=p;
 				 		begin=i+1;
 				 	}  
    			 }	 
    		 }
    	 }	 
    	 if(begin<slen)
    	 {
    		 String k=searchstr.substring(begin, slen);
			 int klen=k.length();
			 if( isDigit(k.charAt(klen-1)))
			 {
					 int p=k.indexOf("/");
					 if(p>0){
						 k=k.substring(0,p);
						 l.add(k);
					 }else if(p<0){
						 l.add(k);
					 }
			 }	  
			 else	 
				 l.add(k);
    	 }	 
     }
     
     private static int forFuzzy(String str, int start, Set<String> l)
     {
 		int slen=str.length();
 		if(slen-start<8)
 			return 0;
 		if(!(str.charAt(start+1)=='U' && str.charAt(start+2)=='Z'
 			   && str.charAt(start+3)=='Z'&& str.charAt(start+4)=='Y' ))
 			return 0;
 		
 		int p1=str.indexOf("(",start+5);
 		int p2=str.indexOf(")",p1+1);
 		if(p1<0 || p2<0) return 0;
 		
 		for(int i=start+5;i<p1;i++)
 		{
 			char cc=str.charAt(i);
 			if(!(isDigit(cc)||isOperator(cc)))
 				return 0;
 		}	
 		
 		int begin=p1+1;
 		for(int i=p1+1;i<p2;i++)
 		{
 			char cc=str.charAt(i);
            if(isHZ(cc))
            {
            	if(i>begin)
            	{
            		l.add(str.substring(begin, i));
            		begin=i;
            	}
            	
            	if(i<p2-1)
            	{	if(isHZ(str.charAt(i+1)))
            	      l.add(str.substring(i, i+2));
            	}else
            	{
            		
            		  l.add(str.substring(i, i+1));
            	}	
            	begin=i+1;
            }else
            {
            	 if(cc==' '||cc==','||cc=='.'||cc=='"'||cc=='\'')
            	 {
            		 if(i>begin)
            		 {	 
            		   l.add(str.substring(begin, i));
            	       begin=i+1;
            		 } 
            	 } 
            }	
 			
 		}	
 			
 		return p2;
 	}
     

	private static boolean isHZ(char cc) 
	{
		if(cc>0 && cc<128)	return false;
		return true;
	}

	private static int forNear(String str, int start, Set<String> l)
     {
 		int slen=str.length();
 		if(slen-start<7)
 			return 0;
 		if(!(str.charAt(start+1)=='E' && str.charAt(start+2)=='A'&& str.charAt(start+3)=='R' ))
 			return 0;
 		
 		int p1=str.indexOf("(",start+4);
 		int p2=str.indexOf(")",p1+1);
 		if(p1<0 || p2<0) return 0;
 		
 		for(int i=start+4;i<p1;i++)
 		{
 			char cc=str.charAt(i);
 			if(!(isDigit(cc)||isOperator(cc)))
 				return 0;
 		}	
 		
 			
 		return p1;
 	}

	private static int forAny(String str, int start, Set<String> l)
     {
		int slen=str.length();
		if(slen-start<6)
			return 0;
		if(!(str.charAt(start+1)=='N' && str.charAt(start+2)=='Y' ))
			return 0;
		
		int p1=str.indexOf("(",start+3);
		int p2=str.indexOf(")",p1+1);
		if(p1<0 || p2<0) return 0;
		
		for(int i=start+3;i<p1;i++)
		{
			char cc=str.charAt(i);
			if(!(isDigit(cc)||isOperator(cc)))
				return 0;
		}	
		

		return p1;
	}

	private static boolean isDigit(char ch)
     {
		if(ch>='0' && ch<='9')
			return true;
		return false;
	}
    private static boolean isOperator(char ch)
    {
       if(ch==',')return true;
       if(ch=='/')return true;
       if(ch=='_')return true;
       return false;
    }
    
	public static void main(String[] argv)throws Exception
     {
		 //System.out.println(SearchToken.split("福克斯/3"));
    	// System.out.println(SearchToken.split(" AN*[df3]Y a  中国/2  北*京 + ( 上*[33]图 | jinag | \"中 国人\") "));
    	// System.out.println(SearchToken.split(" NEAR_6/2(中国,\"windows xp\",服务) ANY2(中国,\"windows xp\",服务) + \"windows xp2\"/2 + sdfs/33 +gggg"));
    	 //System.out.println(SearchToken.split(" FUZZY_6/2(了)FUZZY_6/2(中国人民站起来a了) FUZZY_6,2(中国人windows xp服务)"));
		
		System.out.println(SearchToken.split("[三星%的三星] 和三星 "));
		System.out.println(SearchToken.split("{三星,的三星} 和三星 "));
     }
     
}
