package cn.sowjz.souwen.v1.tools.htmlparse;

public class BaseParse {

	 Handler handler;
	  String[] mask;

	  boolean handleComment=false;
	  boolean handleText=true;

	  public BaseParse(Handler handler)
	  { this(handler,null);
	  }
	  public BaseParse(Handler handler,String[] mask)
	  {  this.handler=handler;
	     this.mask=mask;

	     handleText=handler.isHandleText();
	     handleComment=handler.isHandleComment();
	  }

	  private boolean isend=false;
	  private String keyWord;

	  public void parse(String html)
	  { parsehtml(html);
	    handler.finish();
	  }
	  public void parsehtml(String html)
	  { if(html==null)return;

	    int p0=0,p1=0;

	    while(p1!=-1 && p1<html.length())
	    {     	
	      p0=html.indexOf('<',p1);
	      if(p0==-1) break;

	      text(html,p1,p0);

	      int p2=comment(html,p0);
	      if(p2>0){ p1=p2;continue;}


	      p1=checkIsEnd(html,p0+1);
	      if(p1==-1) break;

	      p1=getKeyWord(html,p1);
	      if(p1<0)
	      {
	        p1=p0+1;
	         if(handleText)handler.text("<");
	        continue;
	      }

	      p1=findTagEnd(html,p1);
	      if(p1!=-1)
	      { p1++;
	        if(!inMask()&& !"script".equalsIgnoreCase(keyWord))
	        	continue;
	        String str=html.substring(p0,p1);
	        if(isend) handler.end(keyWord,str,p0);
	        else
	        {  handler.start(keyWord,str,p0);
	           //if(!"a".equalsIgnoreCase(keyWord)&&isSelfClosed(str))
	          if(isSelfClosed(str))
	             handler.end(keyWord,"",p0);
	           else if("script".equalsIgnoreCase(keyWord))
	           {
	        	   int p3=findScriptEnd(html,p1);
	        	   if(p3>0)
	        	   {
	        		  if(inMask())
	        		     text(html,p1,p3);
	        		   p1=p3;
	        	   }	   
	           }	   
	        }
	      }

	    }
	    text(html,(p1==-1)?p0:p1,html.length());

	  }
	  private int findScriptEnd(String html, int p1)
		{
			int len=html.length();
			for(int i=p1;i<len;i++)
			{
				if(html.charAt(i)=='<')
				{
					int p2=checkIsEnd(html,i+1);
					if(p2<0) continue;
					
					if(len-p2>=6)
					{ String k=html.substring(p2, p2+6);
					  if("script".equalsIgnoreCase(k))
					  { 
						  int p3=findTagEnd(html,p2+6);
						  if(p3<0)
							  return -1;
						  return i;
					  
					  }
					} else return -1;
				}	
				
			}	
			return -1;
		}
	  /**
	   * isSelfClosed
	   *
	   * @param str String
	   * @return boolean
	   */
	  private boolean isSelfClosed(String str)
	  { for(int i=str.length()-2;i>0;i--)
	    {  char c=str.charAt(i);
	       if(!isBlank(c))
	       { if(c=='/') return true;
	         return false;
	       }
	    }
	    return false;
	  }

	  /**
	   * text
	   *
	   * @param html String
	   * @param p1 int
	   * @param p0 int
	   */
	  protected void text(String html, int p0, int p1)
	  {  if(!handleText) return;
	      handler.text(html.substring(p0,p1));
	      return;
	  }

	  /**
	   * htmlDecode
	   *
	   * @param string String
	   * @return String
	   */
	  

	  /**
	   * comment
	   *
	   * @param html String
	   * @param p0 int
	   * @return int
	   */
	  private int comment(String html, int p)
	  { if(html.startsWith("<!--",p))
	    { int p1=html.indexOf("-->",p+2);
	      if(p1==-1)return -1;
	      p1+=3;
	      if(handleComment)
	         handler.comment(html.substring(p,p1),p);
	      return p1;
	    }
	    return -1;
	  }

	  /**
	   * inMask
	   *
	   * @return boolean
	   */
	  private boolean inMask()
	  { if(keyWord==null)return false;
	    if(mask==null) return true;
	    for(int i=0;i<mask.length;i++)
	    { if(keyWord.equalsIgnoreCase(mask[i]))return true;
	    }
	    return false;
	  }

	  /**
	   * findTagEnd
	   *
	   * @param html String
	   * @param p1 int
	   * @return int
	   */
	  private int findTagEnd(String html, int p)
	  { if(p==-1||p>=html.length())return -1;
	    char match='>';
	    boolean lastIsEqual=false;
	    for(int i=p;i<html.length();i++)
	    { char c=html.charAt(i);

	      if(c==match)
	      { if(match=='>')return i;
	        if (match=='\''||match=='"')match='>';
	      }
	      else if((c=='\''||c=='"')&&lastIsEqual)
	      { if(match=='>') match=c;
	      }

	      if(c=='=')  lastIsEqual=true;
	      else if(!isBlank(c)) lastIsEqual =false;

	    }
	    return -1;
	  }

	  /**
	   * getKeyWord
	   *
	   * @param html String
	   * @param p1 int
	   * @return int
	   */
	  protected int getKeyWord(String html, int p)
	  { keyWord=null;
	    if(p==-1||p>=html.length())return -1;

	    for(int i=p;i<html.length();i++)
	    { char c=html.charAt(i);

	      if(isKeyStopFlag(c))
	      { keyWord=html.substring(p,i).toLowerCase();
	         return i;
	      }
	      if(!isKeyWordChar(c,i-p))return -2;
	    }
	    return -1;
	  }

	  /**
	   * isKeyWordChar
	   *
	   * @param c char
	   * @param i int
	   * @return boolean
	   */
	  protected boolean isKeyWordChar(char c, int i)
	  { if(c>='a'&&c<='z') return true;
	    if(c>='A'&&c<='Z') return true;
	    if(c=='_')return true;
	    if(c==':')return true;
	    if(c=='.')return true;
	    if(i>0&&c>='0'&&c<='9')return true;
	    if(i==0&&c=='!')return true;
	    return false;
	  }

	  /**
	   * checkIsEnd
	   *
	   * @param html String
	   * @param p0 int
	   * @return int
	   */
	  protected int checkIsEnd(String html, int p)
	  { if(p>=html.length())return -1;

	    for(int i=p;i<html.length();i++)
	    { char c=html.charAt(i);
	      if(!isBlank(c))
	      { if(c=='/'){ isend=true;return i+1;}
	        else{ isend=false;   return i;}
	      }
	    }
	    return -1;
	  }

	  /**
	   * isBlank
	   *
	   * @param c char
	   * @return boolean
	   */
	  protected boolean isBlank(char c)
	  { if(c==' '||c=='\t'||c=='\n'||c=='\r') return true;
	    return false;
	  }
	  protected boolean isKeyStopFlag(char c)
	  { if(c==' '||c=='\t'||c=='\n'||c=='\r'||c=='>'||c=='/') return true;
	    return false;
	  }

	  public static void main(String [] argv)
	  { BaseParse p=new BaseParse(new HtmlHandler(),null);
	  
//	    System.out.println(p.isSelfClosed("<sdgsdg>"));
//	    System.out.println(p.isSelfClosed("<sdgsdg/>"));
//	    System.out.println(p.isSelfClosed("<sdgsdg/ >"));
	  p.parse("<a>sdfsd</a><script>sdf=sdf</script>");
	  }

}
