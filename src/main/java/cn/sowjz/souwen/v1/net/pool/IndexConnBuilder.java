package cn.sowjz.souwen.v1.net.pool;

import cn.sowjz.souwen.v1.conf.SouwenConfig;
import cn.sowjz.souwen.v1.net.control.IndexConn;

public class IndexConnBuilder extends ConnBuilder<IndexConn> {

	@Override
	public IndexConn createConn(SouwenConfig conf) throws Exception {
		
		IndexConn c= new IndexConn(conf);
		c.open();
		return c;
	}

}
