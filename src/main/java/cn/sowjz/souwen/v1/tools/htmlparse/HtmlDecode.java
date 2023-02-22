package cn.sowjz.souwen.v1.tools.htmlparse;





public class HtmlDecode
{
	public static  String decode(String str)
	  {
		  if(str==null)return null;
		    
		    StringBuffer strb=new StringBuffer(str.length());
		    for(int i=0;i<str.length();i++)
		    {
		    	char c=str.charAt(i);
		    	//if(c=='\n') strb.append(" ");
		    	//else if(c=='\r') strb.append(" ");
		    	//else 
		    	if(c=='&')
		    	{
		    	    int p=str.indexOf(";", i);
		    	    if(p==-1 ||p-i>10)
		    	    	strb.append(c);
		    	    else
		    	    {
		    	    	char c2=str.charAt(i+1);
		    	    	if(c2=='#')
		    	    	{
		    	    		String s=str.substring(i+2,p);
		    	    		int code=convert_getint(s);
		    	    		if(code==0)
		    	    			strb.append(" ");
		    	    		else 
		    	    		{
		    	    			int b=code;
		    	    			int len=0;
		    	    			while(b>0)
		    	    			{
		    	    				b=b>>8;
		    	    				len++;
		    	    			}	
		    	    			byte bb[]=new byte[len];
		    	    			for(int j=0;j<len;j++)
		    	    			{
		    	    				bb[len-j-1]= (byte) ((code>>(8*j))&0xff); 
		    	    			}	
		    	    			try {
		    	    				if(code<256)
		  							  strb.append(new String(bb,"ISO-8859-1"));
		      	    				else
		      	    				  strb.append(new String(bb,"unicode"));
								} catch (Exception e) {
									strb.append(" ");
								}
		    	    		}	
		    	    		
		    	    	}else
		    	    	{
		    	    		String s=str.substring(i+1,p).toLowerCase();
		    	    		if("nbsp".equals(s))strb.append(" ");
		    	    		else if("amp".equals(s))strb.append("&");   
		    	    		else if("lt".equals(s))strb.append("<");    
		    	    		else if("gt".equals(s))strb.append(">");    
		    	    		else if("brvbar".equals(s))strb.append("?");
		    	    		else if("quot".equals(s))strb.append("\""); 
		    	    		else if("middot".equals(s))strb.append("��");
		    	    		else if("bull".equals(s))strb.append("��");  
		    	    		else if("ldquo".equals(s))strb.append("��"); 
		    	    		else if("rdquo".equals(s))strb.append("��"); 
		    	    		else if("lsquo".equals(s))strb.append("��"); 
		    	    		else if("rsquo".equals(s))strb.append("��"); 
		    	    		else if("hellip".equals(s))strb.append("��");
		    	    		else strb.append(" ");
		    	    		
		    	    	}	
		    	    	i=p;
		    	    }	
		    	}else
		    	{
		    		strb.append(c);
		    	}	
		    }	
		    return strb.toString();
	  }

	private static int convert_getint(String s) {
		int i=0;
	     if(s==null) return 0;
	     try
	     { i=Integer.parseInt(s);
	     }catch(Exception e){return 0;}
	     return i;
	}
	
	
}
