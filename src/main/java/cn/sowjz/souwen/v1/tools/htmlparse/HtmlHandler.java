package cn.sowjz.souwen.v1.tools.htmlparse; 

public class HtmlHandler implements Handler
{

  protected boolean handleComment=false;
  protected boolean handleText=true;

  public HtmlHandler()
  {
  }
  public void start(String name,String value,int p)
  {
  }
  public void end(String name,String value,int p)
  {
  }
  public void comment(String text,int p)
  {
  }
  public void text(String text)
  {
  }
  public void finish()
  {}

  /**
   * isHandleComment
   *
   * @return boolean
   */
  public boolean isHandleComment()
  {
    return handleComment;
  }

  /**
   * isHandleText
   *
   * @return boolean
   */
  public boolean isHandleText()
  {
    return handleText;
  }
}
