package cn.sowjz.souwen.v1.net.pool;

import java.util.HashMap;
import java.util.Map;

import cn.sowjz.souwen.v1.conf.SouwenConfig;
import cn.sowjz.souwen.v1.net.control.QueryConn;
import cn.sowjz.souwen.v1.net.control.QueryConn.QueryResultBody;

public class QueryConnBuilder extends ConnBuilder<QueryConn>{

	Map<String,QueryResultBody>cache =new HashMap<String,QueryResultBody>();
	@Override
	public QueryConn createConn(SouwenConfig conf) throws Exception {
		
		QueryConn c= new QueryConn(conf,cache);
		c.open();
		return c;
	}

}
