package cn.sowjz.souwen.v1.tools.htmlparse;



public class IHtml2Txt extends HtmlHandler
{

  boolean inSelect=false;
  boolean inStyle=false;
  StringBuffer strb;
  boolean inScript=false;
  boolean justReturn=true;
  
  public IHtml2Txt()
  { strb=new StringBuffer(5120);
  }

  boolean intr=false;
  public void start(String name,String value,int p)
  {
	if("script".equalsIgnoreCase(name)) inScript=true;  
	else if(inScript) return;
	
    if("p".equalsIgnoreCase(name)) addReturnToText();
    else if("br".equalsIgnoreCase(name)) addReturnToText();
    else if("table".equalsIgnoreCase(name)) addReturnToText();
    else if("tr".equalsIgnoreCase(name)){intr=true; addReturnToText();}
    else if("th".equalsIgnoreCase(name)){intr=true; addReturnToText();}
    else if("div".equalsIgnoreCase(name)) addReturnToText();
    else if("hr".equalsIgnoreCase(name)) addReturnToText();
    else if("style".equalsIgnoreCase(name))inStyle=true;
  }

  /**
   * addReturnToText
   */
  private void addReturnToText()
  { 
	  if(intr)return;
	if(justReturn)return;
    strb.append("\n");
    justReturn=true;
  }

  public void end(String name,String value,int p)
  {
	  if("script".equalsIgnoreCase(name)) inScript=false;
	  else if(inScript)return;
	  
    if("p".equalsIgnoreCase(name)) addReturnToText();
    else if("div".equalsIgnoreCase(name)) addReturnToText();
    else if("tr".equalsIgnoreCase(name)){intr=false; addReturnToText();}
    else if("th".equalsIgnoreCase(name)){intr=false; addReturnToText();}
    else if("h1".equalsIgnoreCase(name)){ addReturnToText();}
    else if("td".equalsIgnoreCase(name)){ strb.append("\t");;}
    else if("h2".equalsIgnoreCase(name)){ addReturnToText();}
    else if("h3".equalsIgnoreCase(name)){ addReturnToText();}
    else if("h4".equalsIgnoreCase(name)){ addReturnToText();}
    else if("center".equalsIgnoreCase(name)){ addReturnToText();}
   
    else if("style".equalsIgnoreCase(name))inStyle=false;
  }

  public void text(String text)
  {
    if(inSelect)return;
    if(inScript)return;
    if(inStyle)return;
    
    int len=text.length();
    if(len==0)return;
    
    
    for(int i=0;i<len;i++)
    {
    	char c=text.charAt(i);
    	if(c=='&')
    	{
    		
    		if(len-i>=6 && "nbsp;".equalsIgnoreCase(text.substring(i+1, i+6)))
    		{
    			strb.append(" "); i+=5;
    		}
    		else if(len-i>=5&& "amp;".equalsIgnoreCase(text.substring(i+1, i+5)))
    		{
    			strb.append("&"); i+=4;
    		}
    		else if(len-i>=4&& "lt;".equalsIgnoreCase(text.substring(i+1, i+4)))
    		{
    			strb.append("<"); i+=3;
    		}
    		else if(len-i>=4 && "gt;".equalsIgnoreCase(text.substring(i+1, i+4)))
    		{
    			strb.append(">"); i+=3;
    		}
    	}else
    	{
    		strb.append(c);
    	}	
    }	
    justReturn=false;
  }



public String convert(String html)
  {  BaseParse parse=new BaseParse(this,null);
     parse.parse(html);
     return strb.toString();
  }

  public static String parse(String html)
  { IHtml2Txt h2t=new IHtml2Txt();
    return h2t.convert(html);
  }
  
  public static void main(String [] argv)throws Exception
  {  IHtml2Txt h2t=new IHtml2Txt();
         
    String html="1<table:dse>2<table><p></tr>3</table></table>5&nbsp;&gt;&lt;&amp;<img src=''> asadsa";
//     HtmlParse parse=new HtmlParse(pt,new String[]{"table","script"});
     long t1=System.currentTimeMillis();
     String text=h2t.convert(html);
     System.out.println("text="+text);
     System.out.println("total time="+(System.currentTimeMillis()-t1));
  }




}
