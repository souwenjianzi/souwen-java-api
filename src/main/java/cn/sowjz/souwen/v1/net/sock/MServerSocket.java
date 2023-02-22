package cn.sowjz.souwen.v1.net.sock;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import cn.sowjz.souwen.v1.Constants;


class MServerSocket
{
	private ServerSocket ss;

	private int port;

	private String charset;
	private int timeout;

	/**
	 * 构造
	 * @throws IOException
	 */
	public MServerSocket(int timeout) throws IOException
	{
		charset = Constants.GBK;
		ss = new ServerSocket();
		this.timeout=timeout;
	}

	/**
	 * 构造
	 * @param port -- 段口号
	 * @throws IOException
	 */
	public MServerSocket(int port,int timeout) throws IOException
	{
		this(port, timeout,Constants.GBK);
	}

	/**
	 * 构造
	 * @param port -- 段口号
	 * @param charset -- 网络传输的字符集
	 * @throws IOException
	 */
	public MServerSocket(int port,int timeout, String charset) throws IOException
	{
		this.charset = charset;
		this.timeout=timeout;
		ss = new ServerSocket(port);
	}

	/**
	 * 返回网络传输字符集
	 * @return
	 */
	public String getCharset()
	{
		return charset;
	}

	/**
	 * 设置网络传输字符集
	 * @param charset -- 网络传输字符集
	 */
	public void setCharset(String charset)
	{
		this.charset = charset;
	}

	/**
	 * 得到侦听段口号
	 * @return
	 */
	public int getPort()
	{
		return port;
	}

	/**
	 * 设置侦听段口号
	 * @param port
	 */
	public void setPort(int port)
	{
		this.port = port;
	}

	/**
	 * 侦听并接受到此套接字的连接。此方法在进行连接之前一直阻塞
	 * @return
	 * @throws IOException
	 */
	public MSocket accept() throws IOException
	{
		Socket sock = ss.accept();
		MSocket mySock = new MSocket(timeout);
		mySock.setSocket(sock);
		mySock.setCharset(charset);
		return mySock;
	}

	/**
	 * 关闭此套接字。 如果此套接字有一个与之关联的通道，则关闭该通道。
	 */
	public void close()
	{
		if (ss != null)
		{
			try
			{
				ss.close();
			} catch (IOException e)
			{
				// ignore
			} finally
			{
				if (ss != null)
					ss = null;
			}
		}
	}

}
