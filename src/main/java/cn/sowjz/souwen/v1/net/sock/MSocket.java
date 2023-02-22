package cn.sowjz.souwen.v1.net.sock;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;





public class MSocket
{
	protected final static Logger log = LoggerFactory.getLogger(MSocket.class);
	private String ip;

	private int port;

	private Socket socket;

	private InputStream in;

	private OutputStream out;

	//private String charset;

	private byte[] buf;

	public static final Object obj = new String("locker");

	int timeout=0;
	/**
	 * 构造
	 */
	public MSocket(int timeout)
	{
		//charset = "GBK";
		buf = new byte[1024];
		this.timeout=timeout;
		try {
			//if(SearchSystem.initok())
			  // timeout=SearchSystem.getInstance().getCfg().getSocketTimeOut();
			
		} catch (Exception e) {
			
		}
	}

	/**
	 * 构造
	 * @param ip -- ip地址
	 * @param port -- 端口号
	 * @param charset -- 字符集
	 */
	public MSocket(String ip, int port,int timeout)
	{
		this(ip, port, timeout, 1024);
	}

	/**
	 * 构造
	 * @param ip -- ip地址
	 * @param port -- 端口号
	 * @param charset -- 字符集
	 * @param bufsize -- 缓存大小
	 */
	public MSocket(String ip, int port,int timeout,  int bufsize)
	{
		this.ip = ip;
		this.port = port;
		//this.charset = charset;
		buf = new byte[bufsize];
		
		this.timeout=timeout;
	}

	/**
	 * 得到IP
	 * @return -- IP地址
	 */
	public String getIp()
	{
		return ip;
	}

	/**
	 * 设置IP地址
	 * @param ip -- 要设置的IP
	 */
	public void setIp(String ip)
	{
		this.ip = ip;
	}

	/**
	 * 得到端口号
	 * @return -- 端口号
	 */
	public int getPort()
	{
		return port;
	}

	/**
	 * 设置端口号
	 * @param port -- 端口号
	 */
	public void setPort(int port)
	{
		this.port = port;
	}

	/**
	 * 设置底层套接字
	 * @param socket -- 要设置的套接字
	 * @throws IOException
	 */
	void setSocket(Socket socket) throws IOException
	{
		this.socket = socket;
		if(timeout>0)
		  socket.setSoTimeout(timeout*1000);
		
		this.port = socket.getPort();
		this.ip = socket.getInetAddress().getHostAddress();
		this.in = socket.getInputStream();
		this.out = socket.getOutputStream();
	}

	/**
	 * 建立连接
	 * @throws IOException
	 */
	public void connect() throws IOException
	{
//		synchronized (obj)
//		{
			socket = new Socket(ip, port);
			if(timeout>0)
				  socket.setSoTimeout(timeout*1000);
			in = socket.getInputStream();
			out = socket.getOutputStream();
			log.info("MySocket connect finish");
//		}
			
	}

	/**
	 * 设置字符集
	 * @param charset
	 */
	public void setCharset(String charset)
	{
		//this.charset = charset;
	}

	/**
	 * 得到字符集
	 * @return
	 */
	public String getCharset()
	{
		return null;//charset;
	}

	/**
	 * receive a byte data
	 * @return
	 * @throws IOException
	 */
	public byte recvByte() throws IOException
	{
		recvBytes(1);
		return buf[0];
	}

	/**
	 * receive a short data
	 * @return
	 * @throws IOException
	 */
	public short recvShort() throws IOException
	{
		recvBytes(2);
		short relt = 0;
		for (int i = 0; i < 2; i++)
			relt += (short) ((buf[i] & 0x00ff) << (i * 8));
		return relt;
	}

	/**
	 * receive a int data
	 * @return
	 * @throws IOException
	 */
	public int recvInt() throws IOException
	{
		recvBytes(4);
		int relt = 0;
		for (int i = 0; i < 4; i++)
			relt += (long) (((int) (buf[i]) & 0xff) << (i * 8));
		// relt += (int) ((buf[i] & 0x00ff) << (i * 8));
		return relt;
	}

	/**
	 * receive a long data
	 * @return
	 * @throws IOException
	 */
	public long recvLong() throws IOException
	{
		recvBytes(8);
		long relt = 0;
		for (int i = 0; i < 8; i++)
			relt += (long) (((long) (buf[i]) & 0xff) << (i * 8));
		// relt += (long) ((buf[i] & 0x00ff) << (i * 8));
		return relt;
	}

	/**
	 * receive a byte array data, and received length equals len
	 * @param len
	 * @return
	 * @throws IOException
	 */
	private void recvBytes(int len) throws IOException
	{
		if (len >= buf.length)
			buf = new byte[len << 1];

		int recvLen = 0; // 实际接收服务器每次返回数据的长度
		recvLen = in.read(buf, 0, len);
		if(recvLen<0) throw new IOException("stream closed");
		while (recvLen < len)
		{	int l=in.read(buf, recvLen, len - recvLen);
		    if(l<0) throw new IOException("stream closed");
			recvLen += l;
		
		}
	}

	public void recvBytes(byte[] b, int len) throws IOException
	{
		int recvLen = 0; // 实际接收服务器每次返回数据的长度
		recvLen = in.read(b, 0, len);
		if(recvLen<0) throw new IOException("stream closed");
		while (recvLen < len)
		{	int l=in.read(b, recvLen, len - recvLen);
		    if(l<0) throw new IOException("stream closed");
		    recvLen += l;
		}
	}

	/**
	 * receive a string data
	 * @param len
	 * @return
	 * @throws IOException
	 */
/*	public String recvStr() throws IOException
	{
		int len = recvInt();
		recvBytes(len);
		return new String(buf, 0, len, charset);
	}
*/
	/**
	 * send a byte data
	 * @param v
	 * @throws IOException
	 */
	public void send(byte v) throws IOException
	{
		buf[0] = v;
		send(buf, 1);
	}

	/**
	 * send a short data
	 * @param v
	 * @throws IOException
	 */
	public void send(short v) throws IOException
	{
		for (int i = 0; i < 2; i++)
			buf[i] = (byte) ((v >> (i * 8)) & 0x00ff);
		send(buf, 2);
	}

	/**
	 * send a int data
	 * @param v
	 * @throws IOException
	 */
	public void send(int v) throws IOException
	{
		for (int i = 0; i < 4; i++)
			buf[i] = (byte) ((v >> (i * 8)) & 0x00ff);
		send(buf, 4);
	}

	/**
	 * send a long data
	 * @param v
	 * @throws IOException
	 */
	public void send(long v) throws IOException
	{
		for (int i = 0; i < 8; i++)
			buf[i] = (byte) ((v >> (i * 8)) & 0x00ff);
		send(buf, 8);
	}

	/**
	 * send a byte array data
	 * @param b
	 * @param len
	 * @throws IOException
	 */
	public void send(byte[] b, int len) throws IOException
	{
		send(b, 0, len);
	}

	/**
	 * send len bytes data, from start of a byte array
	 * @param b
	 * @param start
	 * @param len
	 * @throws IOException
	 */
	public void send(byte[] b, int start, int len) throws IOException
	{
		out.write(b, start, len);
	}

	/**
	 * send a string data
	 * @param str
	 * @throws IOException
	 */
/*	public void send(String str) throws IOException
	{
		byte[] tmp = str.getBytes(charset);
		send(tmp.length);
		send(tmp, tmp.length);
	}
*/
	/**
	 * release the resource
	 */
	public void close()
	{
		try
		{
			if (socket != null)
				socket.close();
		} catch (IOException e)
		{
			log.equals("MySocket close error:"+e);
		} finally
		{
			if (socket != null)
				socket = null;
		}
		log.info("MySocket close finish");
	}

	/**
	 * @return Returns the buf.
	 */
	public byte[] getBuf()
	{
		return buf;
	}


}
