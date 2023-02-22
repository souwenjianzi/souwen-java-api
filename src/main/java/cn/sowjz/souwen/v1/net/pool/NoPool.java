package cn.sowjz.souwen.v1.net.pool;

import cn.sowjz.souwen.v1.conf.SouwenConfig;
import cn.sowjz.souwen.v1.net.control.BaseConn;

public class NoPool<T extends BaseConn> implements ConnPool<T>{

	ConnBuilder<T> builder;
	public NoPool(ConnBuilder<T> builder)
	{
		this.builder=builder;
	}
	
	SouwenConfig conf;
	@Override
	public void initialize(SouwenConfig conf) throws Exception {
		this.conf=conf;
	}

	@Override
	public T getConn() throws Exception {
		T c= builder.createConn(conf);
		return c;
	}

	@Override
	public void releaseConn(T conn) throws Exception {
		conn.close();
	}

	@Override
	public void destroy() {
		
		
	}

	@Override
	public void pulse() {
		
		
	}

	@Override
	public void releaseConn(T conn, Exception e) throws Exception {		
		releaseConn(conn);
	}


	@Override
	public void setBuilder(ConnBuilder<T> builder) {
		this.builder = builder;
	}

}
