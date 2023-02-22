package cn.sowjz.souwen.v1.tools.htmlparse;

public interface Handler
{
  public void start(String name,String value,int p);
  public void end(String name,String value,int p);
  public void comment(String text,int p);
  public void text(String text);
  public void finish() ;

  public boolean isHandleComment();
  public boolean isHandleText();

}
