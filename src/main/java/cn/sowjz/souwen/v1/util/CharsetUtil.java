package cn.sowjz.souwen.v1.util;

public class CharsetUtil
{
	public static boolean isUtf8(ByteBuff content)
	{
		int len = content.length();
		byte[] bb = content.array();
		int num1 = 0;
		int num2=0;
		int other=0;
		for (int i = 0; i < len; i++) {
			int b=isutfbyte(bb[i]);
			switch(b){
			case 1: num2++;break;
			case 2: num1++;break;
			case 3:num1+=2;break;
			case 4:num1+=3;break;
			case 5:num1+=4;break;
			case 6:num1+=5;break;
			case -1:other++;
			}
			
		}
		if(other>(len>>10))
			return false;
		if (num1>=num2*0.98 && num1<=num2*1.02)
			return true;
		return false;
	}
	private static int isutfbyte(byte b) {
		if ((b & 0x80) == 0)
			return 0;
		if ((b & 0xc0) == 0x80)
			return 1;
		if ((b & 0xe0) == 0xc0)
			return 2;
		if ((b & 0xf0) == 0xe0)
			return 3;
		if ((b & 0xf8) == 0xf0)
			return 4;
		if ((b & 0xfc) == 0xf8)
			return 5;
		if ((b & 0xfe) == 0xfc)
			return 6;
		return -1;
	}
	

}
