package cn.sowjz.souwen.v1;

public class Constants {
	
	
	public final static String GBK="GBK";
	
	public final static String Reserved_Words[]=new String[]{"{","}","[","]","(",")","+","|","-","*","NEAR","ANY","FUZZY","/","%"};
	
	/**
	 * 操作成功
	 */
	public static final int SUCCESS = 0;

	
	public static final int EXAMINE = 96;
	/**
	 * 检测是否能够正常加载
	 */
	public static final int ADD_INDEX_TEST = 98;
	/**
	 * 检测连接是否正常
	 */
	public static final int CONNECTION_TEST = 99;
	/**
	 * 创建库操作
	 */
	public static final int DB_CREATE = 100;
	/**
	 * 查询库结构
	 */
	public static final int DB_DESC = 102;
	/**
	 * 删除segment操作
	 */
	public static final int DB_EMPTY = 103; 
	/**
	 * 远程加载文章操作
	 */
	public static final int DOC_ADD = 106; 

	/**
	 * 检索文章操作
	 */
	public static final int DOC_SEARCH = 109;


	/**
	 * 提交操作
	 */
	public static final int COMMIT = 110;

	/**
	 * 停止服务端操作
	 */
	public static final int STOP_SERVER = 111;
	/**
	 * 删除文章操作
	 */
	public static final int DOC_DEL = 113;
	
	/**
	 * 不分组统计操作
	 */
	public static final int DOC_COUNT = 121;
	
	public static final int MAX_FEED_SIZE = 125;
	
	public static final int MINING_TOKEN=150;
	
	public static final int UPDATE_DOC=180;
	/**
	 * 应答信号量
	 */
	public static final int ACK = 211;
	public static final int LOOPACK = 212;
}
