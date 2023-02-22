package cn.sowjz.souwen.v1.tools;

import cn.sowjz.souwen.v1.util.VConvert;

public class BitBuf {

	byte[] buf;
	public BitBuf(int len){
		buf=new byte[len];
	}
	public BitBuf(byte[] buf){
		this.buf=buf;
	}
	
	public void set(int at,boolean on){
		int n=at>>3;
		if(n<0 || n>=buf.length)
			return;
		int p=at & 7;
		
		if(on)
			buf[n] |=  0x1 << p;
		else	
			buf[n] &= ~(0x1 << p);
			
	}
	public boolean get(int at){
		int n=at>>3;
		if(n<0 || n>=buf.length)
			return false;
		return ((buf[n]>>(at &7))&1)==1;
	}
	
	public byte[] toByteArray(){
		return buf;
	}
	
	public String toString(){
		return VConvert.byteArrayToHex(buf);
	}
	
	public static void main(String[] argv)throws Exception{
		BitBuf b=new BitBuf(16);
		
		for(int i=0;i<128;i++){
			System.out.print(b.get(i)+" ");
			b.set(i, true); 
			System.out.print(b);
			System.out.println(" " +b.get(i));
			
		}
		System.out.println("------------");
		for(int i=0;i<128;i++){
			System.out.print(b.get(i)+" ");
			b.set(i, false);
			System.out.print(b);
			System.out.println(" " +b.get(i));
		}
	}
	
}
