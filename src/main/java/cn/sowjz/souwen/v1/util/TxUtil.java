package cn.sowjz.souwen.v1.util;

public class TxUtil {

	public static String visibleChars(String tx){
		if(tx==null) return null;
		int len=tx.length();
		if(len==0) return tx;
		
		StringBuffer strb=new StringBuffer();
		for(int i=0;i<len;i++)
		{
			char c=tx.charAt(i);
			switch((int)c){
			case  0x80:			case  0x81:			case  0x82:			case  0x83:			case  0x84:
			case  0x85:			case  0x86:			case  0x87:			case  0x88:			case  0x89:			case  0x8a:
			case 	0x8b:			case  0x8c:			case  0x8d:			case  0x8e:			case  0x8f:			case  0x90:
			case  0x91:			case  0x92:			case  0x93:			case  0x94:			case  0x95:			case  0x96:
			case  0x97:			case  0x98:			case  0x99:			case 0x9a:			case  0x9b:			case  0x9c:
			case  0x9d:			case  0x9e:			case  0x9f:			case  0x606:
			case  0x607:			case  0x608:			case  0x609:			case  0x60a:			case  0x63b:
			case  0x63c:			case  0x63d:			case  0x63e:			case  0x63f:		
			case  0x76e:			case  0x76f:			case  0x770:			case  0x771:
			case  0x772:			case  0x773:			case  0x774:			case  0x775:			case  0x776:			case 	0x777:
			case  0x778:			case  0x779:			case  0x77a:			case  0x77b:			case  0x77c:			case  0x77d:
			case  0x77e:			case  0x77f:				case  0x200b:			case  0x200c:			case  0x200d:			case  0x200e:
			case 	0x200f:					case  0x2029:			case  0x202a:			case  0x202b:			case  0x202c:			case  0x202d:
			case  0x202e:				case  0xfeff:		
				break;
				
			case 0xa0:
			case 0x2000: case 0x2001:case 0x2002:case 0x2003:case 0x2004:case 0x2005:case 0x2006:case 0x2007:case 0x2008:case 0x2009:case 0x200a:
				case 0x3000:
				strb.append(' ');
				break;
			default:
					strb.append(c);
			}
		}	
		return strb.toString();
	}
	
	public static void main(String[] argv)throws Exception{
		
		int aa[]=new int[]{0x80,0x81,0x82,0x83,0x84,0x85,0x86,0x87,0x88,0x89,0x8a,0x8b,0x8c,0x8d,0x8e,0x8f,0x90,0x91,0x92,0x93,0x94,0x95,0x96,0x97,0x98,0x99,0x9a,0x9b,0x9c,0x9d,0x9e,0x9f,0xad,0x606,0x607,0x608,0x609,0x60a,0x63b,0x63c,0x63d,0x63e,0x63f,0x76e,0x76f,0x770,0x771,0x772,0x773,0x774,0x775,0x776,0x777,0x778,0x779,0x77a,0x77b,0x77c,0x77d,0x77e,0x77f,0x200b,0x200c,0x200d,0x200e,0x200f,0x2029,0x202a,0x202b,0x202c,0x202d,0x202e,0xfeff};
		for(int i=0;i<aa.length;i++){
			char c=(char)aa[i];
			System.out.println(Integer.toHexString(aa[i])+": --"+c+"--");
		}
	}
	
}
