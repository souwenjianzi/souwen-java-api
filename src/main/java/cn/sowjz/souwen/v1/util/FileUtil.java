package cn.sowjz.souwen.v1.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;



public class FileUtil
{
	public static String str2Filepath(String str)
	{
		if (str == null || str.trim().length() == 0)
			return str;
		String separator = System.getProperty("file.separator");
		if (!str.endsWith("\\") && !str.endsWith("/"))
			str += separator;
		return str;
	}

	public static String str2Webpath(String str)
	{
		if (str == null || str.trim().length() == 0)
			return str;
		if (!str.endsWith("/"))
			str += "/";
		return str;
	}

	/**
	 * 删除文件
	 * @param filename
	 */
	public static void delete(String filename)
	{
		if (filename != null)
			delete(new File(filename));
	}

	/**
	 * 删除文件
	 * @param filename
	 */
	public static void delete(File f)
	{
		if (f == null || !f.exists())
			return;

		if (f.delete())
			;// System.out.println("delete " + f.getAbsolutePath() + " OK!");
		else
			System.out.println("delete " + f.getAbsolutePath() + " Failed!");
	}

	/**
	 * 通过路径取得文件主名
	 * @param path
	 * @return
	 */
	public static String getname(String path)
	{
		int idx1 = path.lastIndexOf("/");
		int idx3 = path.lastIndexOf(".");
		if (idx3 == -1 || idx3 < idx1)
			return path;

		return path.substring(idx1 + 1, idx3);
	}

	/**
	 * 通过路径取得文件拓展名
	 * @param fname
	 * @return
	 */
	public static String getExtensionName(String fname)
	{
		int idx = fname.lastIndexOf(".");
		if (idx == -1)
			return null;
		return fname.substring(idx + 1, fname.length());
	}

	/**
	 * 创建文件的路径
	 * @param fname 文件名
	 */
	public static void mkparentPath(String fname)
	{
		mkparentPath(new File(fname));
	}

	public static void mkparentPath(File f)
	{
		File pf = f.getParentFile();
		if (pf != null && !pf.exists())
			pf.mkdirs();
	}

	/**
	 * 关闭
	 * @param in
	 * @param out
	 */
	public static void close(InputStream in, OutputStream out)
	{
		if (null != in)
		{
			try
			{
				in.close();
			} catch (Exception ex)
			{
				// ignore
				ex.printStackTrace();
			} finally
			{
				if (null != in)
					in = null;
			}
		}
		if (null != out)
		{
			try
			{
				out.close();
			} catch (Exception ex)
			{
				// ignore
			} finally
			{
				if (null != out)
					out = null;
			}
		}
	}

	/**
	 * 关闭
	 * @param reader
	 * @param writer
	 */
	public static void close(Reader reader, Writer writer)
	{
		if (null != reader)
		{
			try
			{
				reader.close();
			} catch (Exception ex)
			{
				// ignore
				ex.printStackTrace();
			} finally
			{
				if (null != reader)
					reader = null;
			}
		}
		if (null != writer)
		{
			try
			{
				writer.close();
			} catch (Exception ex)
			{
				// ignore
			} finally
			{
				if (null != writer)
					writer = null;
			}
		}
	}

	/**
	 * 判断扩展名
	 * @param fname
	 * @param extendName
	 * @return
	 */
	public static boolean isTheExtendNameFile(String fname, String extendName)
	{
		if (fname.length() < extendName.length() + 2)
			return false;

		int idx = fname.lastIndexOf(".");
		if (idx == -1)
			return false;

		if (!fname.substring(idx + 1, fname.length()).equalsIgnoreCase(extendName))
			return false;

		return true;
	}

	/**
	 * 判断文件主名是否相同(区分大小写)
	 * @param fname1
	 * @param fname2
	 * @return
	 */
	public static boolean fileNameEquals(String fname1, String fname2)
	{
		int idx1 = fname1.lastIndexOf(".");
		int idx2 = fname2.lastIndexOf(".");

		if (idx1 != -1)
			fname1 = fname1.substring(0, idx1);
		if (idx2 != -1)
			fname2 = fname2.substring(0, idx2);

		return fname1.equals(fname2);
	}

	public static void delAll(File srcDir, boolean flag) throws Exception
	{
		if (srcDir == null || !srcDir.exists())
			return;

		if (srcDir.isDirectory())
		{
			File[] fs = srcDir.listFiles();
			// 循环处理目录下的子目录
			for (int i = 0; i < fs.length; i++)
				delAll(fs[i], true);
		}
		if (flag)
		{
			srcDir.delete();
			// System.out.println("删除文件：" + srcDir.getAbsolutePath());
		}

	}

	public static void moveFile(File moveFile, File fromdir, File toDir)
	{
		String fileStr = StringUtil4Common.replace(moveFile.getAbsolutePath(), fromdir.getAbsolutePath(), toDir.getAbsolutePath());
		File newFile = new File(fileStr);
		newFile.getParentFile().mkdirs();
		moveFile.renameTo(newFile);
	}

	public static String makeFileNameValid(String pathname)
	{
		char[] chs = pathname.toCharArray();
		for (int i = 0; i < chs.length; i++)
		{
			if (chs[i] == ':')
				chs[i] = '.';
			else if (chs[i] == '*')
				chs[i] = 'X';
			else if (chs[i] == '?')
				chs[i] = '!';
			else if (chs[i] == '"')
				chs[i] = '\'';
			else if (chs[i] == '<')
				chs[i] = '(';
			else if (chs[i] == '>')
				chs[i] = ')';
			else if (chs[i] == '|')
				chs[i] = '_';
			else if (chs[i] == ' ')
				chs[i] = '_';
		}
		return new String(chs);
	}

	public static void main(String[] args)
	{
		// String fname1 = "a";
		// String fname2 = "aaa.ddddd";
		// System.out.println(FileUtil.fileNameEquals(fname1, fname2));
	}
	public static  Properties loadPropertiesFromFile(String fn,String charset)throws Exception
	{
		Properties props=new Properties();
	 	 BufferedReader in=new BufferedReader( new InputStreamReader(new FileInputStream(new File(fn)),charset));

		
		String line=in.readLine();
		while(line!=null)
		{
			line=line.trim();
			if(line.length()>0 && line.charAt(0)!='#')
			{
				int p=line.indexOf("=");
				if(p>0 )
				{
					if(p!=line.length()-1)
					{	
					  String k=line.substring(0,p).trim();
					  props.put(k,line.substring(p+1).trim());
					}else
						 props.put(line.substring(0,p),"");
				}	
			}	
			line=in.readLine();
		}	
		in.close();
		return props;
	}
	
	public static ByteBuff readToByteBuffer(String filename)throws Exception{
		return  readToByteBuffer(new FileInputStream(new File(filename)));
	}
	
	public static ByteBuff readToByteBuffer(InputStream in)throws Exception
	{
		ByteBuff bb=new ByteBuff();
		
		byte b[]=new byte[1024];
		int len=in.read(b);
		while(len>=0)
		{
		   bb.append(b,0,len);
		   len=in.read(b);
		}	
		in.close();
		return bb;
	}
}
