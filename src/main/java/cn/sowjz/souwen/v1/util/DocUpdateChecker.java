package cn.sowjz.souwen.v1.util;

import java.util.List;

import cn.sowjz.souwen.v1.db.struct.FieldInfo;
import cn.sowjz.souwen.v1.db.struct.FieldInfos;
import cn.sowjz.souwen.v1.doc.Doc;

public class DocUpdateChecker {

	String message;
	public boolean canQuickUpdate(Doc one){
		FieldInfos finfos = one.getFieldsInfos();
		List<FieldInfo>infos= finfos.getInfos();
		for(FieldInfo fi:infos)
		{
			String fn=fi.getName();
			switch(fi.getType())
			{
			case  FieldInfo.TYPE_INT32:
			case  FieldInfo.TYPE_INT24:
			case  FieldInfo.TYPE_BYTE:
			case  FieldInfo.TYPE_SHORT:
			{
				Integer v = one.getAsInteger(fn);
				if (v != null)
				{
					if(finfos.getSortno()==fi.getSn())
					{	message="cannot update the value of field "+fi.getName();
						return false;
					}
				}	
				break;
			}
			case  FieldInfo.TYPE_INT64:
			{
				Long v = one.getAsLong(fn);
				if (v != null)
				{
					if(finfos.getSortno()==fi.getSn())
					{	message="cannot update the value of field "+fi.getName();
						return false;
					}
				}	
				break;
			}
			case  FieldInfo.TYPE_SEQUENCE:
			{
				Long v = one.getAsLong(fn);
				if (v != null)
				{	message="cannot update the value of field "+fi.getName();
					return false;
				}
				break;
			}	
			case  FieldInfo.TYPE_BYTE16:
			{
			
				break;
			}	
			case  FieldInfo.TYPE_BINARY:
			{
				byte []bb=one.getAsbyteArray(fn);
				if(bb!=null)
				{	message="cannot update the value of field "+fi.getName();
					return false;
				} 
				break;
			}
			case  FieldInfo.TYPE_BIT:
			case  FieldInfo.TYPE_BIT2:
			case  FieldInfo.TYPE_BIT4:
			{
				
				break;
			}
			case  FieldInfo.TYPE_TEXT:
			case  FieldInfo.TYPE_VARCHAR:
			case  FieldInfo.TYPE_CATEGORY:
			case  FieldInfo.TYPE_VARCHAR_ARRAY:	
			case  FieldInfo.TYPE_CLOB:
			{
				String v = one.getAsString(fn);

				if (v!=null && v.length()>0)
				{	message="cannot update the value of field "+fi.getName();
					return false;
				}
				break;
			}
			
	
				default: {	message="unkown type for field "+fi.getName();
							return false;
					}
			}
		}	
		return true;
	}
	public String getMessage() {
		return message;
	}
	
	
	
}
