package cn.sowjz.souwen.v1.query.highlight;

import java.util.List;

import cn.sowjz.souwen.v1.query.response.XWord;
import cn.sowjz.souwen.v1.util.StringUtil4Common;

public class HighLight
{
	private StringBuffer strb;

	private HighLightUtil hlUtil;

	public HighLight()
	{
		strb = new StringBuffer();
		hlUtil = new HighLightUtil();
	}

	public String highLight(String text, String keyStr, String startMark, String endMark, int reltLen)
	{
		if (keyStr == null)
			keyStr = "";
		
		if (keyStr.length() == 0)
		{
			int l = text.length() > reltLen ? reltLen : text.length();
			return hlUtil.simpleRemoveHtmlTag(text.substring(0, l) + "...");
		}
		SearchKey[] keys = hlUtil.getKeys(text, keyStr);
		
		return highLight(text,keys,startMark,endMark,reltLen);
	}
	public String highLight(String text, SearchKey keys[], String startMark, String endMark, int reltLen)
	{
		if (strb.length() > 0)
			strb.delete(0, strb.length());

		if (text == null)
			text = "";
		

		if (startMark == null)
			startMark = "";
		if (endMark == null)
			endMark = "";
		
		
		if (keys == null || keys.length == 0)
		{
			int len = text.length() > reltLen ? reltLen : text.length();
			return hlUtil.simpleRemoveHtmlTag(text.substring(0, len));
		}

		if (reltLen > text.length())
		{
		
			String reltstr = hlUtil.simpleRemoveHtmlTag(text);
			for (int i = 0; i < keys.length; i++)
			{
				String key = keys[i].getKey();
				reltstr = StringUtil4Common.addMarkIgnoreCase(hlUtil,reltstr, key, startMark, endMark);
			}
			return reltstr;
		}
		int tmpLen = reltLen, allLen = reltLen;
		int start = 0, end = 0, i = 0;

		for (i = 0; i < keys.length; i++)
		{
			int fixlen = allLen / (2 * (keys.length - i));

			start = (keys[i].getFirstPos() - fixlen) > end ? (keys[i].getFirstPos() - fixlen) : end;
			if (keys[i].getFirstPos() > start)
			{
				if (start > end)
					strb.append(" ...");

				String substr = text.substring(start, keys[i].getFirstPos());
				allLen -= (keys[i].getFirstPos() - start);
				strb.append(substr);
			}
			fixlen += fixlen - (keys[i].getFirstPos() + keys[i].length() - start);

			if (keys[i].getFirstPos() + keys[i].length() + fixlen > start)
			{
				end = (keys[i].getFirstPos() + keys[i].length() + fixlen) > text.length() ? text.length() : (keys[i].getFirstPos() + keys[i].length() + fixlen);

				int begin = keys[i].getFirstPos() + keys[i].length();
				if (begin < start)
				{
					begin = start;
				} else
				{
					strb.append(keys[i].getKey());
					allLen -= keys[i].length();
				}

				if (begin < end)
				{
					String substr = text.substring(begin, end);
					strb.append(substr);
					allLen -= (end - begin);
				}
			}

			if (end >= text.length())
				break;
		}

		int tmp = 0;
		if (strb.length() < tmpLen)
		{
			tmp = end;
			end = (end + (tmpLen - strb.length()) > text.length()) ? text.length() : end + (tmpLen - strb.length());
			strb.append(text.substring(tmp, end));
		}
		if (strb.length() < tmpLen)
		{
			end = tmp;
			tmp = (end - (tmpLen - strb.length())) > 0 ? (end - (tmpLen - strb.length())) : 0;
			strb.append(text.substring(tmp, end));
		}
		String relt = strb.toString();
		relt = StringUtil4Common.replace(relt, '\r', ' ');
		relt = hlUtil.simpleRemoveHtmlTag(relt);

		for (i = 0; i < keys.length; i++)
			relt = StringUtil4Common.addMarkIgnoreCase(hlUtil,relt, keys[i].getKey(), startMark, endMark);

		return relt;
	}

	public String highLight4searchOp(String text, List<XWord> ws, String startMark,
			String endMark, int reltLen) {
		
		SearchKey[] keys = hlUtil.getKeys(text, ws);
		return highLight(text,keys,startMark,endMark,reltLen);
	}
	public static void main(String[] args)
	{
		HighLight hl = new HighLight();
		String startMark = "<span class=\"hl\">";
		String endMark = "</span>";
		//String text = "19545 每月48,000,000企业电子信函爆发推广 网络直接传媒完整新策略 详情：Http://www.8o88.org 解决您的网络人气、迅速持久的提高网络营销业绩直接方案 提供对贵公司网络广";
		//String keyStr = "您";
		
		//String text = "\\\\192.168.0.193\\d$\\detect_data\\1002\\broad\\20050620-07\\cmpp\\256636.dat [XML] DATA_CMPP 211.154.46.2 49913 211.136.91.228 7890 1119250523 00-E0-FC-0A-A4-A5 581818 13687358488 256635.htm 0 177 [XML] DATA_CMPP 210.77.38.168 48878 211.137.160.12 7890 1119250523 00-E0-FC-0A-A4-A5 05252 13602126236 256638.htm 177 163 [XML] DATA_CMPP 202.106.157.17 1555 211.136.91.228 7890 1119250523 00-E0-FC-0A-A4-A5 9501 13941033561 256639.htm 340 160 [XML] DATA_CMPP 61.135.132.57 44016 211.136.";
		String text = "版本管理工具CVS windows win 客户端的使用方法 安装客户端，\\\\192.168.0.103\\tortois_cvs\\TortoiseCVS-1.8.0-RC3.exe 在本地创建一个文件夹来准备保存工程文件(以下假设为mycvs) 进入mycvs目录点击右键菜单项 CVS　Checkout…　 在弹出的对话框中填写相应项的内容(要注意使用自己的帐号) 项目填写完成后点击ok按钮,就可以取得相应的工程文件。如果想取一个特定的版本的文件，等待工程文件取到本地文件夹之后，在工程文件夹点击右键菜单 在弹出的对话框中选择相应的版本号，目前transformer最新的版本号为transformer_V0_2 选择完成后点击ok按钮即可。相应的文件修改完成后（文件修改后图标会变成红色），点击右键菜单项 CVS Commit即可提交版本 ";
		String keyStr = "win";
		int reltLen = 200;
		long time = System.currentTimeMillis();
		String hlText = hl.highLight(text, keyStr, startMark, endMark, reltLen);
		System.out.println("耗时：" + (System.currentTimeMillis() - time) / 1000);
		System.out.println(hlText);

	}


}
