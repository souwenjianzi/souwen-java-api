package cn.sowjz.souwen.v1.query.request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.sowjz.souwen.v1.db.struct.FieldInfo;
import cn.sowjz.souwen.v1.db.struct.FieldInfos;
import cn.sowjz.souwen.v1.query.txtexpr.TxtExprVerify;
import cn.sowjz.souwen.v1.util.ByteBuff;
import cn.sowjz.souwen.v1.util.DocValueHelper;
import cn.sowjz.souwen.v1.util.StringUtil4Common;
import cn.sowjz.souwen.v1.util.VConvert;



public class Criteria {
	protected final static Logger log = LoggerFactory.getLogger(Criteria.class);
	
	
	public final static byte EXPRMATCH=0;
	public final static byte GREATTHAN=1;
	public final static byte LESSTHAN=2;
	public final static byte EQUAL=3;
	public final static byte UNEQUAL=4;
	public final static byte GREATEQUAL=5;
	public final static byte LESSEQUAL=6;
	public final static byte BETWEEN=7;
	public final static byte IN=8;
	public final static byte COMPARE=9;
	public final static byte NOTIN=10;
	public final static byte FUZZYMATCH=11;
	public final static byte BITAND=12;
	public final static byte MASK_EQUAL=13;
	
	final static String [] operator_names={"EXPR_MATCH","GREATTHAN","LESSTHAN","EQUALS","UNEQUALS","GREATEQUAL","LESSEQUAL","BETWEEN","IN","COMPARE","NOTIN","FUZZY_MATCH","BITAND","MASK_EQUAL"};
	final static String opstr[]=new String[]{" : "," > "," < "," = "," != "," >= " ," <= " ," between "," in "," = "," notIn "," fuzzy ","&","&"};

	
	
	List <byte[]> fieldCrits; 
	
	QueryRequest request;
	public Criteria(QueryRequest request){
		fieldCrits=new ArrayList<byte[]>();
		this.request=request;
		
	}
	
	public boolean isEmpty() {
		
		return fieldCrits.size()==0;
	}

	/**
	 * int32 field between operate
	 * 
	 * @param fieldname
	 * @param beginV
	 * @param endV
	 * @throws Exception 
	 */
	public Criteria andBetween(String fieldname, int beginV, int endV) throws Exception {
		
		FieldInfo info = request.baseStru.getInfos().find(fieldname);
		if (null == info)
			throw new IllegalArgumentException("there is no field named: "
					+ fieldname);
		
		
		if(info.isIntField())
			addNewOne_int(info,BETWEEN,(int)beginV,(int)endV);	
		else if (info.isInt64Field())
			addNewOne_long(info,BETWEEN,(long)beginV,(long)endV);	
		else if ((info.isBitField()))
			addNewOne_byte(info,BETWEEN,(byte)beginV,(byte)endV);
		else
			throw new IllegalArgumentException("The field["+fieldname+"] doesn't support between operate.");
		return this;
	}
	public Criteria andBetween(String fieldname, byte beginV, byte endV) throws Exception {
		
		FieldInfo info = request.baseStru.getInfos().find(fieldname);
		if (null == info)
			throw new IllegalArgumentException("there is no field named: "
					+ fieldname);
		
		if(info.isIntField())
			addNewOne_int(info,BETWEEN,(int)beginV,(int)endV);	
		else if (info.isInt64Field())
			addNewOne_long(info,BETWEEN,(long)beginV,(long)endV);	
		else if ((info.isBitField()))
			addNewOne_byte(info,BETWEEN,beginV,endV);
		else
			throw new IllegalArgumentException("The field["+fieldname+"] doesn't support between operate.");
		
		return this;
	}
	
	/**
	 * int64 field between operate
	 * 
	 * @param fieldname
	 * @param beginV
	 * @param endV
	 * @throws Exception 
	 */
	public Criteria andBetween(String fieldname, long beginV, long endV) throws Exception {
		
		FieldInfo info = request.baseStru.getInfos().find(fieldname);
		if (null == info)
			throw new IllegalArgumentException("there is no field named: "
					+ fieldname);
	
		if(info.isInt32TimeField())
			addNewOne_int(info,BETWEEN, request.docValueHelper.timeToInt32(beginV, info.isUnsign()), request.docValueHelper.timeToInt32(endV, info.isUnsign()));
		else if(info.isIntField())
			addNewOne_int(info,BETWEEN,(int)beginV,(int)endV);	
		else if ((info.isBitField()))
			addNewOne_byte(info,BETWEEN,(byte)beginV,(byte)endV);
		else if (info.isInt64Field())
			addNewOne_long(info,BETWEEN,beginV,endV);	
		else	
			throw new IllegalArgumentException("The field["+fieldname+"] doesn't support between operate.");
		return this;
	}
	public Criteria andBetween(String fieldname, Date beginV, Date endV) throws Exception {
		
		FieldInfo info = request.baseStru.getInfos().find(fieldname);
		if (null == info)
			throw new IllegalArgumentException("there is no field named: "
					+ fieldname);
		
		
		if(!info.isTime())
			throw new IllegalArgumentException(
					"field["+fieldname+"] is not a time field");
		
		if(info.isInt32Field())
				addNewOne_int(info,BETWEEN,DocValueHelper.timeToInt32(beginV, info.isUnsign()),DocValueHelper.timeToInt32(endV, info.isUnsign()));	
		else if (info.isInt64Field())
				addNewOne_long(info,BETWEEN,beginV.getTime(),endV.getTime());	
		else	
			throw new IllegalArgumentException("Between operate must be int64 or int32 number field."+fieldname);
		
		return this;
	}
	
	public Criteria andBitAnd(String fieldname, byte num) throws Exception {
		if(request.baseStru.feedinfo.version%1000<132)
			throw new IllegalArgumentException("Version of isearch is low, it doesn't support this operator.");
		
		addOperation(fieldname,BITAND,num);
		return this;
	}
	
	public Criteria andBitAnd(String fieldname, int num) throws Exception {
	
		if(request.baseStru.feedinfo.version%1000<132)
			throw new IllegalArgumentException("Version of isearch is low, it doesn't support this operator.");
		addOperation(fieldname,BITAND,num);
		return this;
	}
	public Criteria andBitAnd(String fieldname, long num) throws Exception {
		if(request.baseStru.feedinfo.version%1000<132)
			throw new IllegalArgumentException("Version of isearch is low, it doesn't support this operator.");
		
		addOperation(fieldname,BITAND,num);
		return this;
	}
	public Criteria andBitAnd(String fieldname, byte value[]) throws Exception {
		if(request.baseStru.feedinfo.version%1000<148)
			throw new IllegalArgumentException("Version of isearch is low, it doesn't support this operator.");
		
		addOperation(fieldname,BITAND,value);
		return this;
	}
	
	
	public Criteria andEqual(String fieldname, int num) throws Exception {
		addOperation(fieldname,EQUAL,num);
		return this;
	}
	public Criteria andEqual(String fieldname, byte num) throws Exception {
		
		addOperation(fieldname,EQUAL,num);
		return this;
	}
	public Criteria andEqual(String fieldname, byte[]value) throws Exception {
		
		addOperation(fieldname,COMPARE,value);
		return this;
	}
	public Criteria andEqual(String fieldname, long num) throws Exception {
		
		addOperation(fieldname,EQUAL,num);
		return this;
		
	}
	public Criteria andEqual(String fieldname, Date one) throws Exception {
		addOperation(fieldname,one,EQUAL);
		return this;
	}
	
	public Criteria andEqual(String fieldname, String value)throws Exception
	{
		
		//equals(fieldname, new String[] { value });
		FieldInfo info = request.baseStru.getInfos().find(fieldname);
		if (null == info)
			throw new IllegalArgumentException("there is no field named: "
					+ fieldname);
		switch(info.getType())
		{
			case FieldInfo.TYPE_CATEGORY:
				addNewOne_String(info, IN, new String[] { value });
				return this;
		     case FieldInfo.TYPE_INT32: 
		     case FieldInfo.TYPE_INT24: 
		     case FieldInfo.TYPE_BYTE : 
		     case FieldInfo.TYPE_SHORT :
		     {
		    		 Integer id=request.baseStru.getFieldEnumId(fieldname, value);
		    		 if(id!=null)
		    			 addNewOne_int(info,EQUAL,id);	
		    		 else
		    			 addNewOne_int(info,EQUAL,Integer.parseInt(value));	
			    			 
		     }
		     return this;
		     case FieldInfo.TYPE_BIT : 
		     case FieldInfo.TYPE_BIT2 :
		     case FieldInfo.TYPE_BIT4 :
		     {
		    		 Integer id=request.baseStru.getFieldEnumId(fieldname, value);
		    		 if(id!=null)
		    			 addNewOne_byte(info,EQUAL,id.byteValue());	
		    		 else
		    			 log.warn("unkown value["+value+"] of "+fieldname );
		     }
		     return this;
		}
		_andEqualText(fieldname,new String[] { value });
		return this;
	}
	
	public Criteria andEqual(String fieldname, String[] value)throws Exception
	{
		FieldInfo info = request.baseStru.getInfos().find(fieldname);
		if (null == info)
			throw new IllegalArgumentException("there is no field named: "
					+ fieldname);
		
		switch(info.getType())
		{
			case FieldInfo.TYPE_CATEGORY:
				addNewOne_String(info, IN,  value);
				return this;
		     case FieldInfo.TYPE_INT32: 
		     case FieldInfo.TYPE_INT24: 
		     case FieldInfo.TYPE_BYTE : 
		     case FieldInfo.TYPE_SHORT :
		     {
		    	 List<Integer> l=new ArrayList<Integer>();
		    	 for(int i=0;i<value.length;i++)
		    	 {
		    		 Integer id=request.baseStru.getFieldEnumId(fieldname, value[i]);
		    		 if(id!=null)
		    			 l.add(id);
		    		 else
		    			 l.add(Integer.parseInt(value[i]));
		    			
		    		 
		    	 }
		    	 if(l.size()>0)
		    	 { 	 
		    		 int []vi=new int[l.size()];
		    		 for(int i=0;i<l.size();i++)
		    			 vi[i]=l.get(i).intValue();
		    		 addNewOne_int(info,IN,vi);
		    	 }
		     }
		     return this;
		     case FieldInfo.TYPE_BIT : 
		     case FieldInfo.TYPE_BIT2 :
		     case FieldInfo.TYPE_BIT4 :
		     {
		    	 List<Integer> l=new ArrayList<Integer>();
		    	 for(int i=0;i<value.length;i++)
		    	 {
		    		 Integer id=request.baseStru.getFieldEnumId(fieldname, value[i]);
		    		 if(id!=null)
		    			 l.add(id);
		    		 else
		    			 log.warn("unkown value["+value[i]+"] of "+fieldname );
		    		 
		    	 }
		    	 if(l.size()>0)
		    	 { 	 
		    		 byte []vi=new byte[l.size()];
		    		 for(int i=0;i<l.size();i++)
		    			 vi[i]=l.get(i).byteValue();
		    		 addNewOne_byte(info,IN,vi);
		    	 }
		     }
		     return this;
		}
		_andEqualText(fieldname,value);
		return this;
	}

	public Criteria andGreatEqual(String fieldname, int num) throws Exception {
		addOperation(fieldname,GREATEQUAL,num);
		return this;
	}
	public Criteria andGreatEqual(String fieldname, byte num) throws Exception {
		addOperation(fieldname,GREATEQUAL,num);
		return this;
	}
	// >= GE
	public Criteria andGreatEqual(String fieldname, long num) throws Exception {
			addOperation(fieldname,GREATEQUAL,num);
			return this;
	}
	public Criteria andGreatEqual(String fieldname, Date num) throws Exception {
			addOperation(fieldname,num,GREATEQUAL);
			return this;
	}
	
	
	public Criteria andGreatThan(String fieldname, int num) throws Exception {
		addOperation(fieldname,GREATTHAN,num);
		return this;
	}
	public Criteria andGreatThan(String fieldname, byte num) throws Exception {
		addOperation(fieldname,GREATTHAN,num);
		return this;
	}
	// > GT
	public Criteria andGreatThan(String fieldname, long num) throws Exception {
		addOperation(fieldname,GREATTHAN,num);
		return this;
		
	}
	public Criteria andGreatThan(String fieldname, Date num) throws Exception {
		
		addOperation(fieldname,num,GREATTHAN);
		return this;
	}
	
	
	
	
	
	public Criteria andIn(String fieldname, int[] values) throws Exception {
		addArray(fieldname,IN,values);
		return this;
	}
	public Criteria andIn(String fieldname, String[] values) throws Exception {
		andEqual(fieldname, values);
		return this;
	}	

	/**
	 * int64 field IN operate
	 * 
	 * @param fieldname
	 * @param values
	 * @throws Exception 
	 */
	public Criteria andIn(String fieldname, long[] values) throws Exception {
		addArray(fieldname,IN,values);
		return this;
	}
	public Criteria andIn(String fieldname, Date[] values) throws Exception {
		if (0 == values.length)
			throw new IllegalArgumentException("there values is empty. ");

		
		FieldInfo info = request.baseStru.getInfos().find(fieldname);
		if (null == info)
			throw new IllegalArgumentException("there is no field named: "
					+ fieldname);
		if (info.isInt64Field())
		{	
			byte ftv[]=new byte[7+8*values.length];
			insertStringTo(ftv,0,fieldname,2);
			ftv[2]=IN;
			insertIntTo(ftv,3,values.length);
			for(int i=0;i<values.length;i++)
				insertLongTo(ftv,7+i*8,values[i].getTime());		
	    	addNewOne(ftv);
		}
		else if (info.isInt32Field())
		{	
			byte ftv[]=new byte[7+4*values.length];
			insertStringTo(ftv,0,fieldname,2);
			ftv[2]=IN;
			insertIntTo(ftv,3,values.length);
			for(int i=0;i<values.length;i++)
				insertIntTo(ftv,7+i*4,DocValueHelper.timeToInt32(values[i], info.isUnsign()));		
	    	addNewOne(ftv);
		}
		else
		throw new IllegalArgumentException(
				"must be int64 number field or sequence Field."+fieldname);
		
		return this;
	}
	public Criteria andIn(String fieldname, byte[] values) throws Exception {
		addArray(fieldname,IN,values);
		return this;
	}
	
	public Criteria andIn(String fieldname,byte[][]values)throws Exception
	{
		if (0 == values.length)
			throw new IllegalArgumentException("there values is empty. ");
		FieldInfo info = request.baseStru.getInfos().find(fieldname);
		if (null == info)
			throw new IllegalArgumentException("there is no field named: "
					+ fieldname);
		if (!(info.isByte16Field()))
			throw new IllegalArgumentException(
					"Equal operate must be byte16 field ."+fieldname);

		byte ftv[]=new byte[7+values.length*16];
		insertStringTo(ftv,0,fieldname,2);
		ftv[2]=IN;
		insertIntTo(ftv,3,values.length);
		for(int j=0;j<values.length;j++)
		{
			int begin=7+j*16;
			for(int i=0;i<16;i++)
			   ftv[i+begin]=values[j][i];
		}
		addNewOne(ftv);
		return this;
	}
	
	
	

	

	
	public Criteria andExprMatch(String fieldname, String expression) throws Exception {
		
		FieldInfo info = request.baseStru.getInfos().find(fieldname);
		if (null == info)
			throw new IllegalArgumentException("there is no field named: "
					+ fieldname);
		if (!info.isTextField() )
			throw new IllegalArgumentException(
					"the operate must be indexed text field ."+fieldname);

		
		
		TxtExprVerify tv=new TxtExprVerify(expression);
		if(!tv.isPassed())
			throw new Exception("表达式错误:"+tv.getErrMessage(" >>","<< "));
		

		
		
		addNewOne_String(info,EXPRMATCH,expression);
		return this;
	}
	/**
	 * 不计位置匹配长串，模拟搜索引擎。rate为匹配比率 1-100
	 * */
	public Criteria andFuzzyMatch(String fieldname, String txt,int rate) throws Exception {
		
		FieldInfo info = request.baseStru.getInfos().find(fieldname);
		if (null == info)
			throw new IllegalArgumentException("there is no field named: "
					+ fieldname);
		if (!info.isTextField() )
			throw new IllegalArgumentException(
					"the operate must be  text field ."+fieldname);

		txt = beforeDeal(txt);
		
				
		byte []vb=request.str2bytes.toBytes(txt);
		
		byte ftv[]=new byte[9+vb.length];
		insertStringTo(ftv,0,fieldname,2);
		ftv[2]=FUZZYMATCH;
		ftv[3]=(byte)rate;
		insertByteArrayTo(ftv,4,vb);
		
		

		replaceOne(ftv);
		return this;
	}
	
	// <= LE
	public Criteria andLessEqual(String fieldname, int num) throws Exception {
		addOperation(fieldname,LESSEQUAL,num);
		return this;
	}
	public Criteria andLessEqual(String fieldname, byte num) throws Exception {
		addOperation(fieldname,LESSEQUAL,num);
		return this;
	}
	// <= LE
	public Criteria andLessEqual(String fieldname, long num) throws Exception {
			addOperation(fieldname,LESSEQUAL,num);
			return this;
	}
	public Criteria andLessEqual(String fieldname, Date num) throws Exception {
		     addOperation(fieldname,num,LESSEQUAL);	
		     return this;
	}
	public Criteria andLessThan(String fieldname, long num) throws Exception {
		addOperation(fieldname,LESSTHAN,num);
		return this;
	}
	public Criteria andLessThan(String fieldname, Date num) throws Exception {
		addOperation(fieldname,num,LESSTHAN);
		return this;
	}
	
	// < LT
	public Criteria andLessThan(String fieldname, int num) throws Exception {
		addOperation(fieldname,LESSTHAN,num);
		return this;
	}
	public Criteria andLessThan(String fieldname, byte num) throws Exception {
		addOperation(fieldname,LESSTHAN,num);
		return this;
	}
	/**
	 * int32 field IN operate
	 * 
	 * @param fieldname
	 * @param values
	 * @throws Exception 
	 */
	public Criteria andNotIn(String fieldname, int[] values) throws Exception {
		if(request.baseStru.feedinfo.version%1000<132)
			throw new IllegalArgumentException("Version of isearch is low, it doesn't support this operator.");
			
		addArray(fieldname,NOTIN,values);
		return this;
	}
	
	
	
	public Criteria andNotIn(String fieldname, byte[] values) throws Exception {
		if(request.baseStru.feedinfo.version%1000<132)
			throw new IllegalArgumentException("Version of isearch is low, it doesn't support this operator.");
		
		addArray(fieldname,NOTIN,values);
		return this;
	}

	/**
	 * int64 field IN operate
	 * 
	 * @param fieldname
	 * @param values
	 * @throws Exception 
	 */
	public Criteria andNotIn(String fieldname, long[] values) throws Exception {
		if(request.baseStru.feedinfo.version%1000<132)
			throw new IllegalArgumentException("Version of isearch is low, it doesn't support this operator.");
		
		addArray(fieldname,NOTIN,values);
		return this;
	}
	public Criteria andNotIn(String fieldname, Date[] values) throws Exception {
		if(request.baseStru.feedinfo.version%1000<132)
			throw new IllegalArgumentException("Version of isearch is low, it doesn't support this operator.");
		
		if (0 == values.length)
			throw new IllegalArgumentException("there values is empty. ");

		
		FieldInfo info = request.baseStru.getInfos().find(fieldname);
		if (null == info)
			throw new IllegalArgumentException("there is no field named: "
					+ fieldname);
		if (info.isInt64Field())
		{	
			byte ftv[]=new byte[7+8*values.length];
			insertStringTo(ftv,0,fieldname,2);
			ftv[2]=NOTIN;
			insertIntTo(ftv,3,values.length);
			for(int i=0;i<values.length;i++)
				insertLongTo(ftv,7+i*8,values[i].getTime());		
	    	addNewOne(ftv);
		}
		else if (info.isInt32Field())
		{	
			byte ftv[]=new byte[7+4*values.length];
			insertStringTo(ftv,0,fieldname,2);
			ftv[2]=IN;
			insertIntTo(ftv,3,values.length);
			for(int i=0;i<values.length;i++)
				insertIntTo(ftv,7+i*4,DocValueHelper.timeToInt32(values[i], info.isUnsign()));		
	    	addNewOne(ftv);
		}
		else
		throw new IllegalArgumentException(
				"must be int64 number field or sequence Field."+fieldname);
		return this;
	}
	public Criteria andNotIn(String fieldname,byte[][]values)throws Exception
	{
		if(request.baseStru.feedinfo.version%1000<132)
			throw new IllegalArgumentException("Version of isearch is low, it doesn't support this operator.");
		
		if (0 == values.length)
			throw new IllegalArgumentException("there values is empty. ");
		FieldInfo info = request.baseStru.getInfos().find(fieldname);
		if (null == info)
			throw new IllegalArgumentException("there is no field named: "
					+ fieldname);
		if (!(info.isByte16Field()))
			throw new IllegalArgumentException(
					" must be byte16 field ."+fieldname);

		byte ftv[]=new byte[7+values.length*16];
		insertStringTo(ftv,0,fieldname,2);
		ftv[2]=NOTIN;
		insertIntTo(ftv,3,values.length);
		for(int j=0;j<values.length;j++)
		{
			int begin=7+j*16;
			for(int i=0;i<16;i++)
			   ftv[i+begin]=values[j][i];
		}
		addNewOne(ftv);
		return this;
	}
	public Criteria andNotIn(String fieldname, String[] values)throws Exception
	{
		
		if(request.baseStru.feedinfo.version%1000<132)
			throw new IllegalArgumentException("Version of isearch is low, it doesn't support this operator.");
		
		FieldInfo info = request.baseStru.getInfos().find(fieldname);
		if (null == info)
			throw new IllegalArgumentException("there is no field named: "
					+ fieldname);
		switch(info.getType())
		{
			case FieldInfo.TYPE_CATEGORY:
				_andNotCategory(fieldname, values);
				return this;
		     case FieldInfo.TYPE_INT32: 
		     case FieldInfo.TYPE_INT24: 
		     case FieldInfo.TYPE_BYTE : 
		     case FieldInfo.TYPE_SHORT :
		     {
		    	 List<Integer> l=new ArrayList<Integer>();
		    	 for(int i=0;i<values.length;i++)
		    	 {
		    		 Integer id=request.baseStru.getFieldEnumId(fieldname, values[i]);
		    		 if(id!=null)
		    			 l.add(id);
		    		 else
		    			l.add(Integer.parseInt(values[i]));
		    		 
		    	 }
		    	 if(l.size()>0)
		    	 { 	 
		    		 int []vi=new int[l.size()];
		    		 for(int i=0;i<l.size();i++)
		    			 vi[i]=l.get(i).intValue();
		    		 andNotIn(fieldname,vi);
		    	 }
		     }
		     return this;
		     case FieldInfo.TYPE_BIT : 
		     case FieldInfo.TYPE_BIT2 :
		     case FieldInfo.TYPE_BIT4 :
		     {
		    	 List<Integer> l=new ArrayList<Integer>();
		    	 for(int i=0;i<values.length;i++)
		    	 {
		    		 Integer id=request.baseStru.getFieldEnumId(fieldname, values[i]);
		    		 if(id!=null)
		    			 l.add(id);
		    		 else
		    			 log.warn("unkown value["+values[i]+"] of "+fieldname );
		    		 
		    	 }
		    	 if(l.size()>0)
		    	 { 	 
		    		 byte []vi=new byte[l.size()];
		    		 for(int i=0;i<l.size();i++)
		    			 vi[i]=l.get(i).byteValue();
		    		 andNotIn(fieldname,vi);
		    	 }
		     }
		     return this;
		}
		_andNotEqualText(fieldname,values);
		return this;
	}
	

	public Criteria andUnequal(String fieldname, int num) throws Exception {
		addOperation(fieldname,UNEQUAL,num);
		return this;
	}
	public Criteria andUnequal(String fieldname, byte num) throws Exception {
		addOperation(fieldname,UNEQUAL,num);
		return this;
	}
	// != NE
	public Criteria andUnequal(String fieldname, long num) throws Exception {
			addOperation(fieldname,UNEQUAL,num);
			return this;
	}
	public Criteria andUnequal(String fieldname, Date num) throws Exception {
			addOperation(fieldname,num,UNEQUAL);
			return this;
	}
	public Criteria andUnequal(String fieldname, byte[] value) throws Exception {
		addOperation(fieldname,UNEQUAL,value);
		return this;
	}
	public Criteria andMaskEqual(String fieldname, byte mask, byte equalTo) throws Exception {

		if(request.baseStru.feedinfo.version%1000<167)
			throw new IllegalArgumentException("Version of isearch is low, it doesn't support this operator.");
		
		FieldInfo info = request.baseStru.getInfos().find(fieldname);
		if (null == info)
			throw new IllegalArgumentException("there is no field named: "
					+ fieldname);
		
		if(info.isIntField())
			addNewOne_int(info,MASK_EQUAL,(int)mask,(int)equalTo);	
		else if (info.isInt64Field())
			addNewOne_long(info,MASK_EQUAL,(long)mask,(long)equalTo);	
		else if ((info.isBitField()))
			addNewOne_byte(info,MASK_EQUAL,mask,equalTo);
		else
			throw new IllegalArgumentException("The field["+fieldname+"] doesn't support MASK_EQUAL operate.");
		
		return this;
	}
	public Criteria andMaskEqual(String fieldname, int mask, int equalTo) throws Exception {
		
		if(request.baseStru.feedinfo.version%1000<167)
			throw new IllegalArgumentException("Version of isearch is low, it doesn't support this operator.");
		
		FieldInfo info = request.baseStru.getInfos().find(fieldname);
		if (null == info)
			throw new IllegalArgumentException("there is no field named: "
					+ fieldname);
		
		if(info.isIntField())
			addNewOne_int(info,MASK_EQUAL,(int)mask,(int)equalTo);	
		else if (info.isInt64Field())
			addNewOne_long(info,MASK_EQUAL,(long)mask,(long)equalTo);	
		else if ((info.isBitField()))
			addNewOne_byte(info,MASK_EQUAL,(byte)mask,(byte)equalTo);
		else
			throw new IllegalArgumentException("The field["+fieldname+"] doesn't support MASK_EQUAL operate.");
		
		return this;
	}
	public Criteria andMaskEqual(String fieldname, long mask, long equalTo) throws Exception {
		if(request.baseStru.feedinfo.version%1000<167)
			throw new IllegalArgumentException("Version of isearch is low, it doesn't support this operator.");
		
		FieldInfo info = request.baseStru.getInfos().find(fieldname);
		if (null == info)
			throw new IllegalArgumentException("there is no field named: "
					+ fieldname);
		
		if(info.isIntField())
			addNewOne_int(info,MASK_EQUAL,(int)mask,(int)equalTo);	
		else if (info.isInt64Field())
			addNewOne_long(info,MASK_EQUAL,(long)mask,(long)equalTo);	
		else if ((info.isBitField()))
			addNewOne_byte(info,MASK_EQUAL,(byte)mask,(byte)equalTo);
		else
			throw new IllegalArgumentException("The field["+fieldname+"] doesn't support MASK_EQUAL operate.");
		return this;
	}
	public Criteria andMaskEqual(String fieldname, byte mask[], byte equalTo[]) throws Exception {
		if(request.baseStru.feedinfo.version%1000<167)
			throw new IllegalArgumentException("Version of isearch is low, it doesn't support this operator.");
		
		FieldInfo info = request.baseStru.getInfos().find(fieldname);
		if (null == info)
			throw new IllegalArgumentException("there is no field named: "
					+ fieldname);
		
		if(info.isByte16Field())
			addNewOne_byte16s(info,MASK_EQUAL,mask,equalTo);	
	
		else
			throw new IllegalArgumentException("The field["+fieldname+"] doesn't support MASK_EQUAL operate.");
		return this;
	}
	
	
	
		// -----------------------------------------------------------------
	//@Override
	public void  toByteBuffer(ByteBuff buf) throws Exception {
			
			if(fieldCrits.size()==0)
				throw new Exception("no any Critrion defined");
			
		
			
			buf.append(fieldCrits.size());
			for(int i=0;i<fieldCrits.size();i++)
			{
				byte[] bb=(byte[])fieldCrits.get(i);
				buf.append(bb);
			}	

			
	}
	public int getFieldCritNum() {
			return fieldCrits.size();
	}	
	
	//@Override
	public void toStringBuffer(StringBuffer strb) 
	{
	
		
		FieldInfos infos=request.baseStru.getInfos();
		
		for(int i=0;i<fieldCrits.size();i++)
		{
			if(i>0)strb.append(" AND ");
			
			byte[] bb=(byte[])fieldCrits.get(i);
			FieldInfo fi=infos.find(new String(bb,0,2));
			//strb.append(" ");
			strb.append(fi.getName());
			strb.append(opstr[bb[2]]);
			
			if(fi.isInt64Field()|| fi.isSequenceField())
			{
				if(bb[2]==BETWEEN)
				{
					for(int j=0;j<2;j++)
					{	
						if(j>0)strb.append(",");
					    strb.append(VConvert.bytes2Long(bb, 3+8*j));
					}
				}else if(bb[2]==IN||bb[2]==NOTIN)
				{
					int len=VConvert.bytes2Int(bb, 3);
					strb.append("[");
					for(int j=0;j<len;j++)
					{	
						if(j>0)strb.append(",");
					    strb.append(VConvert.bytes2Long(bb, 7+8*j));
					}
					strb.append("]");
				}else if(bb[2]==MASK_EQUAL){
						strb.append("0x").append(Long.toHexString(VConvert.bytes2Long(bb, 3)));
					    strb.append("=");
					    strb.append("0x").append(Long.toHexString(VConvert.bytes2Long(bb, 3+8)));
				}
				else if(bb[2]==BITAND){
					strb.append("0x").append(Long.toHexString(VConvert.bytes2Long(bb, 3)));
				    strb.append("!=0");
				}
				else	
					 strb.append(VConvert.bytes2Long(bb, 3));
			}else if(fi.isIntField())
			{
				if(bb[2]==BETWEEN)
				{
					for(int j=0;j<2;j++)
					{	
						if(j>0)strb.append(",");
					    strb.append(VConvert.bytes2Int(bb, 3+4*j));
					}
				} else if(bb[2]==IN||bb[2]==NOTIN)
				{
					int len=VConvert.bytes2Int(bb, 3);
					strb.append("[");
					for(int j=0;j<len;j++)
					{	
						if(j>0)strb.append(",");
					    strb.append(VConvert.bytes2Int(bb, 7+4*j));
					}
					strb.append("]");
				}else if(bb[2]==MASK_EQUAL){
					strb.append("0x").append(Integer.toHexString(VConvert.bytes2Int(bb, 3)));
				    strb.append("=");
				    strb.append("0x").append(Integer.toHexString(VConvert.bytes2Int(bb, 3+4)));
				}else if(bb[2]==BITAND){
					strb.append("0x").append(Integer.toHexString(VConvert.bytes2Int(bb, 3)));
				    strb.append("!=0");
				}
				else
					 strb.append(VConvert.bytes2Int(bb, 3));
			}else 	if(fi.isBitField())
			{
				if(bb[2]==BETWEEN)
				{
					for(int j=0;j<2;j++)
					{	
						if(j>0)strb.append(",");
					    strb.append(bb[3+j]);
					}
				}else if(bb[2]==IN||bb[2]==NOTIN)
				{   strb.append("[");
					int len=VConvert.bytes2Int(bb, 3);
					for(int j=0;j<len;j++)
					{	
						if(j>0)strb.append(",");
					    strb.append(bb[7+j]);
					}
					strb.append("]");
				}else if(bb[2]==MASK_EQUAL){
					strb.append("0x").append(Integer.toHexString(Byte.toUnsignedInt(bb[3])));
				    strb.append("=");
				    strb.append("0x").append(Integer.toHexString(Byte.toUnsignedInt(bb[ 4])));
				}else if(bb[2]==BITAND){
					strb.append("0x").append(Integer.toHexString(Byte.toUnsignedInt(bb[3])));
				    strb.append("!=0");
				}
					else
					 strb.append(bb[3]);
			}	else if(fi.isByte16Field() )
			{
				
				
				if(bb[2]==BETWEEN)
				{
					for(int j=0;j<2;j++)
					{	
						if(j>0)strb.append(",");
					    strb.append(VConvert.byteArrayToHex(bb, 3+16*j,16));
					}
				}else if(bb[2]==IN||bb[2]==NOTIN)
				{	strb.append("[");
					int len=VConvert.bytes2Int(bb, 3);
					for(int j=0;j<len;j++)
					{	
						if(j>0)strb.append(",");
					    strb.append(VConvert.byteArrayToHex(bb, 7+16*j,16));
					}
					strb.append("]");
				}else if(bb[2]==MASK_EQUAL){
					strb.append(VConvert.byteArrayToHex(bb, 3,16));
				    strb.append("=");
				    strb.append(VConvert.byteArrayToHex(bb, 3+16,16));
				}
				else if(bb[2]==BITAND){
					strb.append(VConvert.byteArrayToHex(bb, 3,16));
				    strb.append("!=0");
				}
				else
					strb.append(VConvert.byteArrayToHex(bb, 3,16));
			} else if(fi.isStringField())
			{
				if(bb[2]==IN || bb[2]==NOTIN)
				{
					int size=VConvert.bytes2Int(bb, 3);
					int p=7;
					strb.append("['");
					for(int j=0;j<size;j++)
					{	
						if(j>0)strb.append("','");
						int len=VConvert.bytes2Int(bb, p);
						p+=4;
						try{
						  doubleQuotation(new String(bb,p,len-1,request.charset),strb);
					   
						}catch(Exception e){}
					    p+=len;
					}
					strb.append("']");
				}else if(bb[2]==FUZZYMATCH){
					int rate=bb[3];
					 strb.append(rate).append("%:");
					int len=VConvert.bytes2Int(bb,4);
					try{
					   strb.append("'");
					   doubleQuotation(new String(bb, 8,len-1,request.charset),strb);
					   strb.append("'");
					}catch(Exception e){}
					
				}
				else 
				{	
					int len=VConvert.bytes2Int(bb,3);
					try{
					   strb.append("'");
					   doubleQuotation(new String(bb, 7,len-1,request.charset),strb);
					   strb.append("'");
					}catch(Exception e){}
				}
			}	
			
		}	
		

	}
	

	
	public byte[]getFieldCritValue(String fieldname,byte operator){
		byte ff[]=fieldname.getBytes();
		for(int i=0;i<fieldCrits.size();i++)
		{
			byte[] bb=(byte[])fieldCrits.get(i);
			if(bb[0]==ff[0]&& bb[1]==ff[1]&& bb[2]==operator)
				return bb;
		}
		return null;
	}

	private void addOperation(String fieldname, Date one,byte operator) throws Exception {
		
		FieldInfo info = request.baseStru.getInfos().find(fieldname);
		if (null == info)
			throw new IllegalArgumentException("there is no field named: "
					+ fieldname);
		
		if(!info.isTime())
			throw new IllegalArgumentException("field["+fieldname+"] is not a time field");
		
		if(info.isInt32Field())
				addNewOne_int(info,operator,DocValueHelper.timeToInt32(one, info.isUnsign()));	
		else if (info.isInt64Field())
				addNewOne_long(info,operator,one.getTime());	
		else	throw new IllegalArgumentException(
				operator_names[operator]+" operate must be int64 or int32 field."+fieldname);
	}
	private void addOperation(String fieldname,byte operator, byte num) throws Exception {
		
		FieldInfo info = request.baseStru.getInfos().find(fieldname);
		if (null == info)
			throw new IllegalArgumentException("there is no field named: "
					+ fieldname);
	
		
		if(info.isIntField())
			addNewOne_int(info,operator,(int)num);	
		else if (info.isInt64Field())
			addNewOne_long(info,operator,(long)num);	
		else if ((info.isBitField()))
			addNewOne_byte(info,operator,(byte)num);
		else
			throw new IllegalArgumentException("The field["+fieldname+"] doesn't support "+operator_names[operator]+" operate.");
	}
	private void addOperation(String fieldname,byte operator, int num) throws Exception {
		
		FieldInfo info = request.baseStru.getInfos().find(fieldname);
		if (null == info)
			throw new IllegalArgumentException("there is no field named: "
					+ fieldname);
		
		if(info.isIntField())
			addNewOne_int(info,operator,(int)num);	
		else if (info.isInt64Field())
			addNewOne_long(info,operator,(long)num);	
		else if ((info.isBitField()))
			addNewOne_byte(info,operator,(byte)num);
		else
			throw new IllegalArgumentException("The field["+fieldname+"] doesn't support "+operator_names[operator]+" operate.");
	}
	private void addOperation(String fieldname,byte operator, long num) throws Exception {
		
		FieldInfo info = request.baseStru.getInfos().find(fieldname);
		if (null == info)
			throw new IllegalArgumentException("there is no field named: "
					+ fieldname);
		
		if(info.isInt32TimeField())
			addNewOne_int(info,operator, request.docValueHelper.timeToInt32(num, info.isUnsign()));
		
		
		if(info.isIntField())
			addNewOne_int(info,operator,(int)num);	
		else if (info.isInt64Field())
			addNewOne_long(info,operator,(long)num);	
		else if ((info.isBitField()))
			addNewOne_byte(info,operator,(byte)num);
		else
			throw new IllegalArgumentException("The field["+fieldname+"] doesn't support "+operator_names[operator]+" operate.");
	}
	private void addOperation(String fieldname, byte operator, byte[] value) throws Exception {
		FieldInfo info = request.baseStru.getInfos().find(fieldname);
		if (null == info)
			throw new IllegalArgumentException("there is no field named: "
					+ fieldname);
		if (!(info.isByte16Field()))
			throw new IllegalArgumentException(
					"Equal operate must be byte16 field ."+fieldname);

		byte ftv[]=new byte[19];
		insertStringTo(ftv,0,fieldname,2);
		ftv[2]=operator;
		for(int i=0;i<16;i++)
			ftv[i+3]=value[i];
		addNewOne(ftv);
		
	}

	//@Override
	public int critCount() {
		return 1;
	}

	
	
	public String toString()
	{
		
		StringBuffer strb=new StringBuffer();
		toStringBuffer(strb);
		return strb.toString();
	}

	public boolean merge(SubCrit bt) {
		List <byte[]>l1= fieldCrits; 
		List <byte[]>l2= bt.fieldCrits; 
		
		for(byte[] b1:l1){
			for(byte[] b2:l2){
				if(b1[0]==b2[0] && b1[1]==b2[1])
					return false;
			}
		}
		fieldCrits.addAll(bt.fieldCrits);
		return true;
	}

	private String beforeDeal(String value) {
		value = StringUtil4Common.replace(value, "\r", " ");
		value = StringUtil4Common.replace(value, "\n", " ");
		return value;
	}
	public void doubleQuotation(String tx, StringBuffer strb) {
		for(int i=0;i<tx.length();i++){
			char c=tx.charAt(i);
			if(c=='\'')
				strb.append(c);
			strb.append(c);
		}
	}
	private void addNewOne(byte[] ftv) 
	{
		for(int i=0;i<fieldCrits.size();i++)
		{
			byte[]b=(byte[])fieldCrits.get(i);
			if(b[0]==ftv[0] &&b[1]==ftv[1]){
			   log.warn("duplicated field critrion of :"+new String(b,0,2));
			   fieldCrits.remove(i);
			   break;
			}	
		}	
		fieldCrits.add(ftv);
	}
	private void addNewOne_byte(FieldInfo info,byte op,byte one,byte two) {
		byte ftv[]=new byte[5];
		insertStringTo(ftv,0,info.getName(),2);
		ftv[2]=op;
		ftv[3]=one;
		ftv[4]=two;
		
		addNewOne(ftv);
	}
	private void addNewOne_byte(FieldInfo info,byte op,byte num) {
		byte ftv[]=new byte[4];
		insertStringTo(ftv,0,info.getName(),2);
		ftv[2]=op;
		ftv[3]=num;
		
		addNewOne(ftv);
	}
	private void addNewOne_byte(FieldInfo info,byte op,byte values[]){
		byte ftv[]=new byte[7+values.length];
		insertStringTo(ftv,0,info.getName(),2);
		ftv[2]=op;
		insertIntTo(ftv,3,values.length);
		for(int i=0;i<values.length;i++)
		  ftv[7+i]=values[i];
		
		addNewOne(ftv);
	}
	private void addNewOne_byte16s(FieldInfo info,byte op,byte values1[],byte values2[]){
		byte ftv[]=new byte[35];
		insertStringTo(ftv,0,info.getName(),2);
		ftv[2]=op;
		for(int i=0;i<16;i++)
		  ftv[3+i]=values1[i];
		for(int i=0;i<16;i++)
			  ftv[19+i]=values2[i];
			
		addNewOne(ftv);
	}
	private void addNewOne_int(FieldInfo info,byte op,int one,int two)throws Exception {
		byte ftv[]=new byte[11];
		insertStringTo(ftv,0,info.getName(),2);
		ftv[2]=op;
		insertIntTo(ftv,3,one);
		insertIntTo(ftv,7,two);
		addNewOne(ftv);
	}
	private void addNewOne_int(FieldInfo info,byte op,int num)throws Exception {
		byte ftv[]=new byte[7];
		insertStringTo(ftv,0,info.getName(),2);
		ftv[2]=op;
		insertIntTo(ftv,3,num);
		addNewOne(ftv);
	}
	private void addNewOne_int(FieldInfo info,byte op,int values[])throws Exception {
		byte ftv[]=new byte[7+4*values.length];
		insertStringTo(ftv,0,info.getName(),2);
		ftv[2]=op;
		insertIntTo(ftv,3,values.length);
		for(int i=0;i<values.length;i++)
		   insertIntTo(ftv,7+i*4,values[i]);
		
		addNewOne(ftv);
	}
	private void addNewOne_long(FieldInfo info,byte op,long one,long two)throws Exception {
		byte ftv[]=new byte[19];
		insertStringTo(ftv,0,info.getName(),2);
		ftv[2]=op;
		insertLongTo(ftv,3,one);
		insertLongTo(ftv,11,two);
		addNewOne(ftv);
	}
	private void addNewOne_long(FieldInfo info,byte op,long num)throws Exception {
		byte ftv[]=new byte[11];
		insertStringTo(ftv,0,info.getName(),2);
		ftv[2]=op;
		insertLongTo(ftv,3,num);
		addNewOne(ftv);
	}	
	private void addNewOne_long(FieldInfo info,byte op,long values[])throws Exception {
		byte ftv[]=new byte[7+8*values.length];
		insertStringTo(ftv,0,info.getName(),2);
		ftv[2]=op;
		insertIntTo(ftv,3,values.length);
		for(int i=0;i<values.length;i++)
		   insertLongTo(ftv,7+i*8,values[i]);
		
		addNewOne(ftv);
	}
	private void addNewOne_String(FieldInfo info,byte op,String value)throws Exception {
		value = beforeDeal(value);
		byte []vb=request.str2bytes.toBytes(value);
		
		byte ftv[]=new byte[8+vb.length];
		insertStringTo(ftv,0,info.getName(),2);
		ftv[2]=op;
		insertByteArrayTo(ftv,3,vb);

		replaceOne(ftv);
	}
	private void addNewOne_String(FieldInfo info,byte op,String values[])throws Exception {
		for (int i = 0; i < values.length; i++)
			values[i] = beforeDeal(values[i]);

		int tl=7;
		for(int i=0;i<values.length;i++)
			tl+=request.str2bytes.toBytes(values[i]).length+5;
		
		byte ftv[]=new byte[tl];
		insertStringTo(ftv,0,info.getName(),2);
		ftv[2]=op;
		insertIntTo(ftv,3,values.length);
		
		int a=7;
		for(int i=0;i<values.length;i++)
		{	byte[]bb=request.str2bytes.toBytes(values[i]);
			insertByteArrayTo(ftv,a,bb);
			a+=bb.length+5;
		}
		addNewOne(ftv);
	}
	private void replaceOne(byte[] ftv) throws Exception 
	{
		for(int i=0;i<fieldCrits.size();i++)
		{
			byte[]b=(byte[])fieldCrits.get(i);
			if(b[0]==ftv[0] &&b[1]==ftv[1])
			{	fieldCrits.set(i, ftv);
				return;
			}
		}	
		fieldCrits.add(ftv);
	}


	private void insertIntTo(byte[] ftv,int begin, int v) 
	{
		for (int i = 0; i < 4; i++)
			ftv[begin+i] = (byte) ((v >> (i * 8)) & 0x00ff);
	}
	private void insertLongTo(byte[] ftv,int begin, long v) 
	{
		for (int i = 0; i < 8; i++)
			ftv[begin+i] = (byte) ((v >> (i * 8)) & 0x00ff);
	}
	private void insertStringTo(byte[] ftv,int begin, String v,int strlen) 
	{
		byte[] bb=v.getBytes();
		
		for(int i=0;i<strlen;i++)
		{
			ftv[begin+i]=bb[i];
		}	
	}
	private void insertByteArrayTo(byte[] ftv,int begin, byte[] v) 
	{
		insertIntTo(ftv,begin,v.length+1);
		
		for(int i=0;i<v.length;i++)
		{
			ftv[begin+4+i]=v[i];
		}	
		int a=begin+4+v.length;
		ftv[a]=(byte)0;
		
	}
	
	
	private void addArray(String fieldname,byte oparetor,  int[] values) throws Exception {
		if (0 == values.length)
			throw new IllegalArgumentException("there values is empty. ");

		
		FieldInfo info = request.baseStru.getInfos().find(fieldname);
		if (null == info)
			throw new IllegalArgumentException("there is no field named: "
					+ fieldname);
		
		
		
		if(info.isIntField())
		{
			
			
			addNewOne_int(info,oparetor,values);
			return;
		}
		if(info.isInt64Field())
		{
			long []iv=new long[values.length];
			for(int i=0;i<values.length;i++)
				iv[i]=values[i];
			
			addNewOne_long(info,oparetor,iv);
			return;
		}
		
	    if (!(info.isBitField()))
			throw new IllegalArgumentException(	"value or "+operator_names[oparetor]+" operation doesn't match field["+fieldname+"].");
		
	    if(info.getType()==FieldInfo.TYPE_BIT)
	    {
	    	byte v=0;
	    	for(int i=0;i<values.length;i++)
	    	{
	    		v|=(1<<values[i]);
	    	}	
	    	if(v==3)
	    		return;
	    }else 	 if(info.getType()==FieldInfo.TYPE_BIT2)
	    {
	    	int v=0;
	    	for(int i=0;i<values.length;i++)
	    	{
	    		v|=(1<<values[i]);
	    	}	
	    	if(v==15)
	    		return;
	    }else 	 if(info.getType()==FieldInfo.TYPE_BIT4)
	    {
	    	int v=0;
	    	for(int i=0;i<values.length;i++)
	    	{
	    		v|=(1<<values[i]);
	    	}	
	    	if(v==65535)
	    		return;
	    }
	    
	    byte []iv=new byte[values.length];
		for(int i=0;i<values.length;i++)
			iv[i]=(byte) values[i];
		
	    addNewOne_byte(info,oparetor,iv);
		
	}
	

	private void addArray(String fieldname,byte oparetor, byte[] values) throws Exception {
		if (0 == values.length)
			throw new IllegalArgumentException("there values is empty. ");

		
		FieldInfo info = request.baseStru.getInfos().find(fieldname);
		if (null == info)
			throw new IllegalArgumentException("there is no field named: "
					+ fieldname);
		
		if(info.isIntField())
		{
			int []iv=new int[values.length];
			for(int i=0;i<values.length;i++)
				iv[i]=values[i];
			
			addNewOne_int(info,oparetor,iv);
			return;
		}
		if(info.isInt64Field())
		{
			long []iv=new long[values.length];
			for(int i=0;i<values.length;i++)
				iv[i]=values[i];
			
			addNewOne_long(info,oparetor,iv);
			return;
		}
		
	    if (!(info.isBitField()))
			throw new IllegalArgumentException(	"value or "+operator_names[oparetor]+" operation doesn't match field["+fieldname+"].");
		
	    if(info.getType()==FieldInfo.TYPE_BIT)
	    {
	    	byte v=0;
	    	for(int i=0;i<values.length;i++)
	    	{
	    		v|=(1<<values[i]);
	    	}	
	    	if(v==3)
	    		return;
	    }else 	 if(info.getType()==FieldInfo.TYPE_BIT2)
	    {
	    	int v=0;
	    	for(int i=0;i<values.length;i++)
	    	{
	    		v|=(1<<values[i]);
	    	}	
	    	if(v==15)
	    		return;
	    }else 	 if(info.getType()==FieldInfo.TYPE_BIT4)
	    {
	    	int v=0;
	    	for(int i=0;i<values.length;i++)
	    	{
	    		v|=(1<<values[i]);
	    	}	
	    	if(v==65535)
	    		return;
	    }
	    
	    addNewOne_byte(info,oparetor,values);
	}
	
	
	private void addArray(String fieldname,byte oparetor,  long[] values) throws Exception {
		if (0 == values.length)
			throw new IllegalArgumentException("there values is empty. ");

		
		FieldInfo info = request.baseStru.getInfos().find(fieldname);
		if (null == info)
			throw new IllegalArgumentException("there is no field named: "
					+ fieldname);
		
		if(info.isInt32TimeField())
		{
			int vv[]=new int[values.length];
			for(int i=0;i<values.length;i++)
				vv[i]=request.docValueHelper.timeToInt32(values[i], info.isUnsign());
			addNewOne_int(info,oparetor,vv);
			return;
		}
		
		if(info.isIntField())
		{
			int []iv=new int[values.length];
			for(int i=0;i<values.length;i++)
				iv[i]=(int) values[i];
			
			addNewOne_int(info,oparetor,iv);
			return;
		}
		if(info.isInt64Field())
		{
			addNewOne_long(info,oparetor,values);
			return;
		}
		
	    if (!(info.isBitField()))
			throw new IllegalArgumentException(	"value or "+operator_names[oparetor]+" operation doesn't match field["+fieldname+"].");
		
	    if(info.getType()==FieldInfo.TYPE_BIT)
	    {
	    	byte v=0;
	    	for(int i=0;i<values.length;i++)
	    	{
	    		v|=(1<<values[i]);
	    	}	
	    	if(v==3)
	    		return;
	    }else 	 if(info.getType()==FieldInfo.TYPE_BIT2)
	    {
	    	int v=0;
	    	for(int i=0;i<values.length;i++)
	    	{
	    		v|=(1<<values[i]);
	    	}	
	    	if(v==15)
	    		return;
	    }else 	 if(info.getType()==FieldInfo.TYPE_BIT4)
	    {
	    	int v=0;
	    	for(int i=0;i<values.length;i++)
	    	{
	    		v|=(1<<values[i]);
	    	}	
	    	if(v==65535)
	    		return;
	    }
	    byte []iv=new byte[values.length];
		for(int i=0;i<values.length;i++)
			iv[i]=(byte) values[i];
		
	    addNewOne_byte(info,oparetor,iv);
	    
	}
	/**
	 * category field IN operate
	 * 
	 * @param fieldname
	 * @param values
	 * @throws Exception 
	
	private void _andCategory(String fieldname, String[] vs)
			throws Exception {
		if (0 == vs.length)
			throw new IllegalArgumentException("there values is empty. "+fieldname);

		
		FieldInfo info = request.ss.getInfos().find(fieldname);
		if (null == info)
			throw new IllegalArgumentException("there is no field named: "
					+ fieldname);
		if (!info.isCateField())
			throw new IllegalArgumentException(
					"Category operate must be category field."+fieldname);

		
		Arrays.sort(vs);
//		for(int i=0;i<vs.length;i++)
//			System.out.println(vs[i]);
		
		List<String> list=new ArrayList<String>(vs.length);
		for(int i=0;i<vs.length;i++)
		  if(!cateIn(vs[i],list))
			  list.add(vs[i]);
		
		String values[]=list.toArray(new String[list.size()]);
		
		
		addNewOne_String(info, IN, values);
	} */
	private boolean cateIn(String one, List<String> list) {
		if(list==null || list.size()==0) return false;
		
		for(String a:list)
		{
			if(a.equals(one)) return true;
			if(one.startsWith(a) )
			{
				char c=one.charAt(a.length());
				if(c==':') return true;
			}	
		}	
		return false;
	}
	// -----------------------------------------------------------------
	
	private void _andEqualText(String fieldname, String[] values)
		throws Exception {
		
		if (0 == values.length)
			throw new IllegalArgumentException("there values is empty. "+fieldname);

		
		FieldInfo info = request.baseStru.getInfos().find(fieldname);
		if (null == info)
			throw new IllegalArgumentException("there is no field named: "
					+ fieldname);
		if (!(info.isVarcharField()||info.getType()==FieldInfo.TYPE_VARCHAR_ARRAY))
			throw new IllegalArgumentException(
					" the field must be varchar or keywords field ."+fieldname);

		addNewOne_String(info,IN,values);
	}
	private void _andNotCategory(String fieldname, String[] vs)
			throws Exception {
		if (0 == vs.length)
			throw new IllegalArgumentException("there values is empty. "+fieldname);

		
		FieldInfo info = request.baseStru.getInfos().find(fieldname);
		if (null == info)
			throw new IllegalArgumentException("there is no field named: "
					+ fieldname);
		if (!info.isCateField())
			throw new IllegalArgumentException(
					"Category operate must be category field."+fieldname);

		Arrays.sort(vs);
//		for(int i=0;i<vs.length;i++)
//			System.out.println(vs[i]);
		
		List<String> list=new ArrayList<String>(vs.length);
		for(int i=0;i<vs.length;i++)
		  if(!cateIn(vs[i],list))
			  list.add(vs[i]);
		
		String values[]=list.toArray(new String[list.size()]);
		
		addNewOne_String(info,NOTIN,values);
		
	
	}
	private void _andNotEqualText(String fieldname, String[] values)
			throws Exception {
			
			if (0 == values.length)
				throw new IllegalArgumentException("there values is empty. "+fieldname);

			
			FieldInfo info = request.baseStru.getInfos().find(fieldname);
			if (null == info)
				throw new IllegalArgumentException("there is no field named: "
						+ fieldname);
			if (!(info.isVarcharField()||info.getType()==FieldInfo.TYPE_VARCHAR_ARRAY))
				throw new IllegalArgumentException(
						" the field must be varchar or keywords field ."+fieldname);

			addNewOne_String(info,NOTIN,values);

		}
}
