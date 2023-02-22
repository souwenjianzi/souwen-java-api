package cn.sowjz.souwen.v1.net.pool;

import cn.sowjz.souwen.v1.conf.SouwenConfig;
import cn.sowjz.souwen.v1.net.control.BaseConn;

public interface ConnPool<T extends BaseConn> {
	
	public void initialize(SouwenConfig conf)throws Exception;
	public T getConn()throws Exception;
	public void releaseConn(T conn)throws Exception;
	public void releaseConn(T conn,Exception e)throws Exception;
	public void destroy();
	public void pulse();
	public void setBuilder(ConnBuilder<T> builder) ;
	
}
