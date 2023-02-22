package cn.sowjz.souwen.v1.db.struct;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import cn.sowjz.souwen.v1.util.ByteBuff;
import cn.sowjz.souwen.v1.util.VConvert;


public class FieldInfosUtil
{
    public static FieldInfos loadFromFile(String fn)throws Exception
    {
       FileInputStream in=new FileInputStream(new File(fn));
       
       byte bb[]=new byte[16];
       in.read(bb, 0, 4);
       
       int num=VConvert.bytes2Int(bb);
       
       FieldInfos fis=new FieldInfos(num);
       
       for(int i=0;i<num;i++)
       {
    	   in.read(bb, 0, 16);
    	   fis.add(new FieldInfo(bb));
       }	   
       in.read(bb, 0, 4);
       int s=VConvert.bytes2Int(bb);
       fis.setSortno(s);
       in.close();
       return fis;
    }
    
    public static String genCode(FieldInfos finfos){
    	StringBuffer strb=new StringBuffer();
    	
    	strb.append("\tpublic static void create_config()throws Exception	{\n\t\tFieldInfos infos = new FieldInfos();\n");
    	for(int i=0;i<finfos.size();i++)
    	{
    		FieldInfo fi=finfos.get(i);
    		String type=FieldInfo.types[fi.getType()-1]+",";
    		for(int j=type.length();j<20;j++)
    			type=type+" ";
    		strb.append("\t\tinfos.add(new FieldInfo(").append(String.valueOf(i)).append(i<10?" ":"")
    			.append(", \"").append(fi.getName()).append("\", ").append("FieldInfo.").append(type);
    		
    		
    		switch(fi.getType())
    		{
    		case FieldInfo.TYPE_SEQUENCE:
        			strb.append(fi.getFlag()).append(", ");break;
        			default:
        				strb.append("   ");break;
    		}
    		strb.append(fi.isCached()?"true, ":"false,")
    		.append(fi.isUnsign()?"true, ":"false,").append(fi.isTime()?"true, ":"false,")
    		.append(fi.isStore()?"true ":"false").append("));");
    		strb.append("\n");
    	}	
    	strb.append("\n\t\tinfos.setSortnoByName(\"").append(finfos.get(finfos.getSortno()).getName()).append("\");\n");
    	strb.append("\n\t\tFieldInfosUtil.saveFile(infos, \"searchlib.config\");");
    	strb.append("\n\t}\n");
		return strb.toString();
    }
    
    public static void renumberSN(FieldInfos infos){
    	int n=0;
		for (FieldInfo info : infos.getInfos())
		{	info.setSn(n++);
		}
    }
    
    public static void saveFile(FieldInfos infos,String filename)throws Exception {
		
//		FieldInfo time_fi = infos.get(infos.getSortno());
//		if(time_fi.isInt32Field())
//			time_fi.setUnsign(true);
		
		
		for(int i=1;i<infos.size();i++)
		{
			FieldInfo fii=infos.get(i);
			for(int j=0;j<i;j++)
			{
				FieldInfo fij=infos.get(j);
				if(fii.getName().equals(fij.getName()))
					throw new Exception(fii.getName()+" used twice.");
			}	
		}	
		
		ByteBuff buf = new ByteBuff();
		// num (c:char)
		buf.append((int) infos.size());
		// info
		int n=0;
		for (FieldInfo info : infos.getInfos())
		{	info.setSn(n++);
			switch(info.getType()){
			case FieldInfo.TYPE_TEXT:  break;
			case FieldInfo.TYPE_VARCHAR:
			case FieldInfo.TYPE_INT32: 
			case FieldInfo.TYPE_INT64:
			case FieldInfo.TYPE_CATEGORY:
			case FieldInfo.TYPE_SEQUENCE:
			case FieldInfo.TYPE_BYTE16:
			case FieldInfo.TYPE_BYTE:
			case FieldInfo.TYPE_SHORT:
			case FieldInfo.TYPE_BIT:
			case FieldInfo.TYPE_BIT2:
			case FieldInfo.TYPE_BIT4:
			case FieldInfo.TYPE_VARCHAR_ARRAY:
			case FieldInfo.TYPE_CLOB:
			case FieldInfo.TYPE_BINARY:break; 
			}
			buf.append(info.toByteBuffer("ISO-8859-1"));
		}
		// sortno(c:char)
		buf.append((int) infos.getSortno());
		
		FileOutputStream out=new FileOutputStream(new File(filename));
		
		out.write(buf.array(),0,buf.getUsed());
		out.close();
		
	}
    public static void main(String[] argv)throws Exception
    {
    	FieldInfos f=FieldInfosUtil.loadFromFile("C:\\isearch\\db\\searchlib.config");
    	System.out.println(genCode(f));
    }
}
