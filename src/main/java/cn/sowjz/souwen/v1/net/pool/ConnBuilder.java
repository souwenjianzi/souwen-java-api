package cn.sowjz.souwen.v1.net.pool;

import cn.sowjz.souwen.v1.conf.SouwenConfig;
import cn.sowjz.souwen.v1.net.control.BaseConn;

public abstract class ConnBuilder<T extends BaseConn> {

	public abstract T createConn(SouwenConfig conf)throws Exception;
}
