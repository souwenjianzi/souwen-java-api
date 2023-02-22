package cn.sowjz.souwen.v1.req_parser;

import java.io.StringReader;
import java.util.List;

import cn.sowjz.souwen.v1.BaseStructure;
import cn.sowjz.souwen.v1.SouwenClient;
import cn.sowjz.souwen.v1.db.struct.FieldInfo;
import cn.sowjz.souwen.v1.query.request.Criteria;
import cn.sowjz.souwen.v1.query.request.QueryRequest;
import cn.sowjz.souwen.v1.query.request.SubCrit;
import cn.sowjz.souwen.v1.req_parser.RequestParser.CmdType;
import cn.sowjz.souwen.v1.req_parser.RequestParser.OpType;
import cn.sowjz.souwen.v1.req_parser.RequestParser.SortType;
import cn.sowjz.souwen.v1.req_parser.RequestParser.SumType;
import cn.sowjz.souwen.v1.util.VConvert;

public abstract class RequestBuilder {

	
	public abstract QueryRequest getRequest();
	
	public static class Simple extends RequestBuilder{

		@Override
		public void askBegin(String tx) {
			System.out.println("askBegin:"+tx);
		}

		@Override
		public void queryCmd(CmdType tx) {
			System.out.println("queryCmd:"+tx);
		}
		@Override
		public void askNum(String tx) {
			System.out.println("askNum:"+tx);
		}
		@Override
		public void target(String tx) {
			System.out.println("target:"+tx);
		}
		@Override
		public void groupBegin(String tx) {
			System.out.println("groupBegin:"+tx);
		}
		@Override
		public void groupStep(String tx) {
			System.out.println("groupStep:"+tx);
		}
		@Override
		public void orderBy(SortType tx) {
			System.out.println("orderBy:"+tx);
		}
		@Override
		public void orderBy(SortType type,String func){
			System.out.println("orderBy:"+type+":"+func);
		}
		@Override
		public void sum(SumType type){
			System.out.println("sum:"+type);
		}
		@Override
		public void fieldSum(List<String> sumlist){
			System.out.println("sum:"+sumlist.toString());
		}
		@Override
		public QueryRequest getRequest() {
			return null;
		}

		@Override
		public void createRequest() {
			
		}

		@Override
		public SubCrit oneFieldCrit(String fn, OpType op, String value) {
			
			SubCrit sub=new PQryReq(fn+" "+op+" "+value).createSubCrit();
			return sub;
		}

		@Override
		public SubCrit oneFieldCrit(String image, OpType between, String beginV,
				String endV) {
			
			SubCrit sub=new PQryReq(image+" "+between+" "+beginV+","+endV).createSubCrit();
			return sub;
		}

		@Override
		public SubCrit oneFieldCrit(String image, String op,  List<String> value) {
			SubCrit sub=new PQryReq(image+" "+op+" "+value).createSubCrit();
			return sub;
		}
		@Override
		public void where(SubCrit subt) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void cube2f_max(String image) {
			System.out.println("cube2f_max:"+image);
			
		}

		@Override
		public void cube2fn(String image) {
			System.out.println("cube2fn:"+image);
			
		}

		@Override
		public void miningParams(List<String> list) {
			System.out.println("miningParams:"+list);
			
		}

		@Override
		public void simhash_threshold(String image) {
			System.out.println("simhash_threshold:"+image);
			
		}
		@Override
		public void table_name(String tablename) {
			System.out.println("tablename:"+tablename);
			
		}
	
		@Override
		public RunResult runRequest(SouwenClient ss) {
			// TODO Auto-generated method stub
			return null;
		}

		
	}
	
	
	
	public static class X extends RequestBuilder{

		BaseStructure sb;
		QueryRequest req=null;
		CmdType cmdType;
		public  X(BaseStructure sb){
			this.sb=sb;
			
		}
		public  X(QueryRequest req){
			this.req=req;
			sb=req.getBaseStructure();
		}

		@Override
		public void askBegin(String tx) {
			req.setStart(VConvert.str2Int(tx));
		}

		@Override
		public void queryCmd(CmdType tx) throws ParseException {
			
			cmdType=tx;
			
			
			switch(tx){
			case SEARCH:if(req==null)req=new QueryRequest(sb);break;
			}
			
		}
		@Override
		public void askNum(String tx) {
			req.setAskNum(VConvert.str2Int(tx));
		}
		@Override
		public void target(String fn) {
			req.getHeader().setTargetFN(fn.toUpperCase());
		}
		@Override
		public void groupBegin(String tx) {
			req.getHeader().setGroupBegin(VConvert.str2Long(tx));
		}
		@Override
		public void groupStep(String tx) {
			req.getHeader().setGroupStep(VConvert.str2Long(tx));
		}
		@Override
		public void table_name(String tablename) {
				req.getHeader().setTableName(tablename);
			
		}
		@Override
		public void orderBy(SortType tx) {
			switch(tx){
			case time: req.setOrderByTime();break;
			case rela:req.setOrderByRela();break;
			case random:req.setOrderByRamdom();break;
			case time_asc:req.setOrderByTimeAse();break;
			}
		}
		@Override
		public void orderBy(SortType type,String func){
			if(func==null) return;
			
			switch(type){
			case field_desc:req.setOrderByFieldDesc(func.toUpperCase());break;
			case field_asc:req.setOrderByFieldAsc(func.toUpperCase());break;
			case heat:req.setOrderByFormula(func);break;
			}
		}
		@Override
		public void sum(SumType type){
			switch(type){
			case none:req.setSumType(QueryRequest.SumType.none); break;
			case count:req.setSumType(QueryRequest.SumType.count);break;
			case estimate:req.setSumType(QueryRequest.SumType.estimate);break;
			}
		}
		@Override
		public void fieldSum(List<String> sumlist) throws ParseException{
		}
		@Override
		public void cube2f_max(String tx) {
			req.getHeader().setCube2f_max(VConvert.str2Int(tx));
			
		}
		@Override
		public void cube2fn(String fn) {
			req.getHeader().setCube2fn(fn.toUpperCase().getBytes());
			
		}
		@Override
		public void simhash_threshold(String tx) {
			req.getHeader().setSimhash_threshold(VConvert.str2Byte(tx));
			
		}
		@Override
		public void miningParams(List<String> list) {
			
		}
		@Override
		public void createRequest(){
			
			if(req==null)
				req=new QueryRequest(sb);
		}
		@Override
		public QueryRequest getRequest() {
			return req;
		}
		@Override
		public SubCrit oneFieldCrit(String fn, OpType op, String value) throws ParseException {
			fn=fn.toUpperCase();
			FieldInfo info = sb.getInfos().find(fn);
			if(info==null)
				throw new ParseException("no such a field:"+fn);
			
			
			SubCrit sub = req.createSubCrit();
			
			
			switch(op){
			case INDEX:  	oneFieldCrit_index(sub,info,value); break;
			case GREATTHAN: oneFieldCrit_greatthan(sub,info,value); break;
			case LESSTHAN:	oneFieldCrit_lessthan(sub,info,value); break;
			case EQUAL:		oneFieldCrit_equal(sub,info,value); break;
			case UNEQUAL:	oneFieldCrit_unequal(sub,info,value); break;
			case GREATEQUAL:oneFieldCrit_greatequal(sub,info,value); break;
			case LESSEQUAL:	oneFieldCrit_lessequal(sub,info,value); break;
			case COMPARE:	oneFieldCrit_equal(sub,info,value); break;
			case BETWEEN:
			case IN:
			case NOTIN:
			case SEARCH:
			case BITAND:
			case MASK_EQUAL:
			default: throw new ParseException("ERROR "+op);
			}
			
		
			return sub;
		}

		private void oneFieldCrit_lessequal(SubCrit sub, FieldInfo info,
				String value)throws ParseException {
			String fn=info.getName();
			switch(info.getType())
			{
			case FieldInfo.TYPE_BYTE:
			case FieldInfo.TYPE_SHORT:
			case FieldInfo.TYPE_INT32:
			case FieldInfo.TYPE_INT24:
					try {sub.andLessEqual(fn, VConvert.str2Int(value));} catch (Exception e) {throw new ParseException(e.getMessage());} break;
			case FieldInfo.TYPE_INT64:
			case FieldInfo.TYPE_SEQUENCE:
				try {sub.andLessEqual(fn, VConvert.str2Long(value));} catch (Exception e) {throw new ParseException(e.getMessage());} break;
			case FieldInfo.TYPE_BIT:
			case FieldInfo.TYPE_BIT2:
			case FieldInfo.TYPE_BIT4:
				try {sub.andLessEqual(fn, VConvert.str2Byte(value));} catch (Exception e) {throw new ParseException(e.getMessage());} break;
							

			default:throw new 	ParseException("字段["+fn+"]不支持 '<='运算");
			}
		}
		private void oneFieldCrit_greatequal(SubCrit sub, FieldInfo info,
				String value) throws ParseException{
			String fn=info.getName();
			switch(info.getType())
			{
			case FieldInfo.TYPE_BYTE:
			case FieldInfo.TYPE_SHORT:
			case FieldInfo.TYPE_INT32:
			case FieldInfo.TYPE_INT24:
					try {sub.andGreatEqual(fn, VConvert.str2Int(value));} catch (Exception e) {throw new ParseException(e.getMessage());} break;
			case FieldInfo.TYPE_INT64:
			case FieldInfo.TYPE_SEQUENCE:
				try {sub.andGreatEqual(fn, VConvert.str2Long(value));} catch (Exception e) {throw new ParseException(e.getMessage());} break;
			case FieldInfo.TYPE_BIT:
			case FieldInfo.TYPE_BIT2:
			case FieldInfo.TYPE_BIT4:
				try {sub.andGreatEqual(fn, VConvert.str2Byte(value));} catch (Exception e) {throw new ParseException(e.getMessage());} break;
							

			default:throw new 	ParseException("字段["+fn+"]不支持 '>='运算");
			}
		}
		private void oneFieldCrit_unequal(SubCrit sub, FieldInfo info,
				String value) throws ParseException{
			String fn=info.getName();
			switch(info.getType())
			{
			case FieldInfo.TYPE_BYTE:
			case FieldInfo.TYPE_SHORT:
			case FieldInfo.TYPE_INT32:
			case FieldInfo.TYPE_INT24:
					try {sub.andUnequal(fn, VConvert.str2Int(value));} catch (Exception e) {throw new ParseException(e.getMessage());} break;
			case FieldInfo.TYPE_INT64:
			case FieldInfo.TYPE_SEQUENCE:
				try {sub.andUnequal(fn, VConvert.str2Long(value));} catch (Exception e) {throw new ParseException(e.getMessage());} break;
			case FieldInfo.TYPE_BIT:
			case FieldInfo.TYPE_BIT2:
			case FieldInfo.TYPE_BIT4:
				try {sub.andUnequal(fn, VConvert.str2Byte(value));} catch (Exception e) {throw new ParseException(e.getMessage());} break;
							

			default:throw new 	ParseException("字段["+fn+"]不支持 '!='运算");
			}
		}
		private void oneFieldCrit_equal(SubCrit sub, FieldInfo info,
				String value) throws ParseException{
			String fn=info.getName();
			switch(info.getType())
			{
			case FieldInfo.TYPE_BYTE:
			case FieldInfo.TYPE_SHORT:
			case FieldInfo.TYPE_INT32:
			case FieldInfo.TYPE_INT24:
					try {sub.andEqual(fn, VConvert.str2Int(value));} catch (Exception e) {throw new ParseException(e.getMessage());} break;
			case FieldInfo.TYPE_INT64:
			case FieldInfo.TYPE_SEQUENCE:
				try {sub.andEqual(fn, VConvert.str2Long(value));} catch (Exception e) {throw new ParseException(e.getMessage());} break;
			case FieldInfo.TYPE_BIT:
			case FieldInfo.TYPE_BIT2:
			case FieldInfo.TYPE_BIT4:
				try {sub.andEqual(fn, VConvert.str2Byte(value));} catch (Exception e) {throw new ParseException(e.getMessage());} break;
							
			case FieldInfo.TYPE_BYTE16:
				try {sub.andEqual(fn, ArrayUtil.hexToByteArray(value));} catch (Exception e) {throw new ParseException(e.getMessage());} break;
				
			case FieldInfo.TYPE_VARCHAR:
			case FieldInfo.TYPE_CATEGORY:
			case FieldInfo.TYPE_VARCHAR_ARRAY:
			{	
				
				   try {
					sub.andEqual(fn, value);
				   } catch (Exception e) {
					   throw new ParseException(e.getMessage());
				   }
				}	break;
				
			default:throw new 	ParseException("字段["+fn+"]不支持 '='运算");
			}
		}
		private void oneFieldCrit_lessthan(SubCrit sub, FieldInfo info,
				String value) throws ParseException {
			String fn=info.getName();
			switch(info.getType())
			{
			case FieldInfo.TYPE_BYTE:
			case FieldInfo.TYPE_SHORT:
			case FieldInfo.TYPE_INT32:
			case FieldInfo.TYPE_INT24:
					try {sub.andLessThan(fn, VConvert.str2Int(value));} catch (Exception e) {throw new ParseException(e.getMessage());} break;
			case FieldInfo.TYPE_INT64:
			case FieldInfo.TYPE_SEQUENCE:
				try {sub.andLessThan(fn, VConvert.str2Long(value));} catch (Exception e) {throw new ParseException(e.getMessage());} break;
			case FieldInfo.TYPE_BIT:
			case FieldInfo.TYPE_BIT2:
			case FieldInfo.TYPE_BIT4:
				try {sub.andLessThan(fn, VConvert.str2Byte(value));} catch (Exception e) {throw new ParseException(e.getMessage());} break;
							

			default:throw new 	ParseException("字段["+fn+"]不支持 '<'运算");
			}
		}
		private void oneFieldCrit_greatthan(SubCrit sub, FieldInfo info,
				String value) throws ParseException {
			String fn=info.getName();
			switch(info.getType())
			{
			
			case FieldInfo.TYPE_BYTE:
			case FieldInfo.TYPE_SHORT:
			case FieldInfo.TYPE_INT32:
			case FieldInfo.TYPE_INT24:
					try {sub.andGreatThan(fn, VConvert.str2Int(value));} catch (Exception e) {throw new ParseException(e.getMessage());} break;
			case FieldInfo.TYPE_INT64:
			case FieldInfo.TYPE_SEQUENCE:
				try {sub.andGreatThan(fn, VConvert.str2Long(value));} catch (Exception e) {throw new ParseException(e.getMessage());} break;
			case FieldInfo.TYPE_BIT:
			case FieldInfo.TYPE_BIT2:
			case FieldInfo.TYPE_BIT4:
				try {sub.andGreatThan(fn, VConvert.str2Byte(value));} catch (Exception e) {throw new ParseException(e.getMessage());} break;
							

			default:throw new 	ParseException("字段["+fn+"]不支持 '>'运算");
			}
		
			
		}
		private void oneFieldCrit_index(SubCrit sub, FieldInfo info,
				String value) throws ParseException {
			String fn=info.getName();
			
			switch(info.getType())
			{
			
			case FieldInfo.TYPE_TEXT:
					try {sub.andExprMatch(fn, value);} catch (Exception e) {throw new ParseException(e.getMessage());}
					break;
			
			default:throw new 	ParseException("字段["+fn+"]不支持搜索运算");
			}
				
			
		}
		@Override
		public SubCrit oneFieldCrit(String fn, OpType op, String beginV,
				String endV) throws ParseException {
			
			fn=fn.toUpperCase();
			
			
				
			
			FieldInfo info = sb.getInfos().find(fn);
			if(info==null)
				throw new ParseException("no such a field:"+fn);
			
			SubCrit sub = req.createSubCrit();
		
			
			switch(op){
			
			case SEARCH:
				int rate=VConvert.str2Int(beginV.substring(1,beginV.length()-2));
				 try {sub.andFuzzyMatch(fn, endV, rate);} catch (Exception e) { throw new ParseException(e.getMessage());}
				return sub;
			case BITAND:
				oneFieldCrit_bitand(sub,info,beginV);
				return sub;
			case BETWEEN:
				oneFieldCrit_between(sub,info,beginV,endV);
				return sub;
			case MASK_EQUAL:
				oneFieldCrit_maskequal(sub,info,beginV,endV);
				return sub;
			}
			
			
		
			
			throw new ParseException("error op="+op);
			
			
			
			
			
		}

		private void oneFieldCrit_maskequal(SubCrit sub, FieldInfo info,
				String beginV, String endV) throws ParseException {
			String fn=info.getName();
			
			switch(info.getType())
			{
			
			case FieldInfo.TYPE_INT64:
			case FieldInfo.TYPE_SEQUENCE:
			{	long begin=VConvert.str2Long(beginV);
				long end=VConvert.str2Long(endV);
			   try {
				sub.andMaskEqual(fn, begin, end);
			   } catch (Exception e) {
				   throw new ParseException(e.getMessage());
			   }
			}	break;
			
			
			     case FieldInfo.TYPE_INT32: 
			     case FieldInfo.TYPE_INT24: 
			     case FieldInfo.TYPE_BYTE : 
			     case FieldInfo.TYPE_SHORT :
			     {
			    	 int begin=VConvert.str2Int(beginV);
					int end=VConvert.str2Int(endV);
					   try {
						sub.andMaskEqual(fn, begin, end);
					   } catch (Exception e) {
						   throw new ParseException(e.getMessage());
					   }
			     }
			    	 break;
			     case FieldInfo.TYPE_BIT : 
			     case FieldInfo.TYPE_BIT2 :
			     case FieldInfo.TYPE_BIT4 :
			     {
			    	 int begin=VConvert.str2Int(beginV);
						int end=VConvert.str2Int(endV);
						   try {
							sub.andMaskEqual(fn, (byte)begin,(byte) end);
						   } catch (Exception e) {
							   throw new ParseException(e.getMessage());
						   }
			     }
			     break;
			     case FieldInfo.TYPE_BYTE16:
						try {sub.andMaskEqual(fn, ArrayUtil.hexToByteArray(beginV,16), ArrayUtil.hexToByteArray(endV,16));} catch (Exception e) {throw new ParseException(e.getMessage());} break;
			    default :throw new ParseException("field not support between:"+fn);
			}
		}
		private void oneFieldCrit_between(SubCrit sub, FieldInfo info,
				String beginV, String endV) throws ParseException {
			String fn=info.getName();
			
			switch(info.getType())
			{
			
			case FieldInfo.TYPE_INT64:
			case FieldInfo.TYPE_SEQUENCE:
			{	long begin=VConvert.str2Long(beginV);
				long end=VConvert.str2Long(endV);
			   try {
				sub.andBetween(fn, begin, end);
			   } catch (Exception e) {
				   throw new ParseException(e.getMessage());
			   }
			}	break;
			
			     case FieldInfo.TYPE_INT32: 
			     case FieldInfo.TYPE_INT24: 
			     case FieldInfo.TYPE_BYTE : 
			     case FieldInfo.TYPE_SHORT :
			     {
			    	 int begin=VConvert.str2Int(beginV);
					int end=VConvert.str2Int(endV);
					   try {
						sub.andBetween(fn, begin, end);
					   } catch (Exception e) {
						   throw new ParseException(e.getMessage());
					   }
			     }
			    	 break;
			     case FieldInfo.TYPE_BIT : 
			     case FieldInfo.TYPE_BIT2 :
			     case FieldInfo.TYPE_BIT4 :
			     {
			    	 int begin=VConvert.str2Int(beginV);
						int end=VConvert.str2Int(endV);
						   try {
							sub.andBetween(fn, (byte)begin,(byte) end);
						   } catch (Exception e) {
							   throw new ParseException(e.getMessage());
						   }
			     }
			     break;
			
			    default :throw new ParseException("field not support between:"+fn);
			}
			
		}
		private void oneFieldCrit_bitand(SubCrit sub, FieldInfo info,
				String beginV) throws ParseException {
			String fn=info.getName();
			
			switch(info.getType())
			{
			
			case FieldInfo.TYPE_INT64:
			case FieldInfo.TYPE_SEQUENCE:
			{	long begin=VConvert.str2Long(beginV);
			   try {
				sub.andBitAnd(fn, begin);
			   } catch (Exception e) {
				   throw new ParseException(e.getMessage());
			   }
			}	break;
			
			
			     case FieldInfo.TYPE_INT32: 
			     case FieldInfo.TYPE_INT24: 
			     case FieldInfo.TYPE_BYTE : 
			     case FieldInfo.TYPE_SHORT :
			     {
			    	 int begin=VConvert.str2Int(beginV);
					   try {
						   sub.andBitAnd(fn, begin);
					   } catch (Exception e) {
						   throw new ParseException(e.getMessage());
					   }
			     }
			    	 break;
			     case FieldInfo.TYPE_BIT : 
			     case FieldInfo.TYPE_BIT2 :
			     case FieldInfo.TYPE_BIT4 :
			     {
			    	 int begin=VConvert.str2Int(beginV);
						   try {
							   sub.andBitAnd(fn, begin);
						   } catch (Exception e) {
							   throw new ParseException(e.getMessage());
						   }
			     }
			     break;
			
			     case FieldInfo.TYPE_BYTE16:
						try {sub.andBitAnd(fn, ArrayUtil.hexToByteArray(beginV,16));} catch (Exception e) {throw new ParseException(e.getMessage());} break;
			     
			     
			    default :throw new ParseException("field not support between:"+fn);
			}
		}
		@Override
		public SubCrit oneFieldCrit(String fn, String op, List<String> value) throws ParseException {
			fn=fn.toUpperCase();
			
			if(op==null )
				throw new ParseException("ERROR OP:"+op);
			
			FieldInfo info = sb.getInfos().find(fn);
			if(info==null)
				throw new ParseException("no such a field:"+fn);
			
			SubCrit sub = req.createSubCrit();
		
			if(op.equalsIgnoreCase("in"))
				oneFieldCrit_in(sub,info,value);
			else if(op.equalsIgnoreCase("notin"))
				oneFieldCrit_notin(sub,info,value);
			else 
				throw new ParseException("ERROR OP:"+op);
			
			
			
			
			
			return sub;
			
			
		}
		private void oneFieldCrit_notin(SubCrit sub, FieldInfo info,
				List<String> value) throws ParseException {
			String fn=info.getName();
			
			switch(info.getType())
			{
			
//			case FieldInfo.TYPE_TEXT:
//			case FieldInfo.TYPE_ARTICLE:
			case FieldInfo.TYPE_VARCHAR:
			case FieldInfo.TYPE_CATEGORY:
			case FieldInfo.TYPE_VARCHAR_ARRAY:
			{	
				
				   try {
					sub.andNotIn(fn,value.toArray(new String[value.size()]));
				   } catch (Exception e) {
					   throw new ParseException(e.getMessage());
				   }
				}	break;
			case FieldInfo.TYPE_BYTE16:
			{	
				
				   try {
					sub.andNotIn(fn,ArrayUtil.toBytesArray(value));
				   } catch (Exception e) {
					   throw new ParseException(e.getMessage());
				   }
				}	break;
			case FieldInfo.TYPE_INT64:
			case FieldInfo.TYPE_SEQUENCE:
			{	
				
				
				
			   try {
				   
				sub.andNotIn(fn, ArrayUtil.toLongArray(value));
			   } catch (Exception e) {
				   throw new ParseException(e.getMessage());
			   }
			}	break;
			
			     case FieldInfo.TYPE_INT32: 
			     case FieldInfo.TYPE_INT24: 
			     case FieldInfo.TYPE_BYTE : 
			     case FieldInfo.TYPE_SHORT :
			     {
			    		
					   try {
						sub.andNotIn(fn, ArrayUtil.toIntArray(value));
					   } catch (Exception e) {
						   throw new ParseException(e.getMessage());
					   }
			     }
			    	 break;
			     case FieldInfo.TYPE_BIT : 
			     case FieldInfo.TYPE_BIT2 :
			     case FieldInfo.TYPE_BIT4 :
			     {
			    		
					   try {
						sub.andIn(fn, ArrayUtil.toByteArray(value));
					   } catch (Exception e) {
						   throw new ParseException(e.getMessage());
					   }
			     }
			     break;
			
			    default :throw new ParseException("field not support in operator:"+fn);
			}
		}
		private void oneFieldCrit_in(SubCrit sub, FieldInfo info,
				List<String> value) throws ParseException {
			String fn=info.getName();
			
			switch(info.getType())
			{
			
//			case FieldInfo.TYPE_TEXT:
//			case FieldInfo.TYPE_ARTICLE:
			case FieldInfo.TYPE_VARCHAR:
			case FieldInfo.TYPE_CATEGORY:
			case FieldInfo.TYPE_VARCHAR_ARRAY:
			{	
				
				   try {
					sub.andIn(fn,value.toArray(new String[value.size()]));
				   } catch (Exception e) {
					   throw new ParseException(e.getMessage());
				   }
				}	break;
			case FieldInfo.TYPE_BYTE16:
			{	
				
				   try {
					sub.andIn(fn,ArrayUtil.toBytesArray(value));
				   } catch (Exception e) {
					   throw new ParseException(e.getMessage());
				   }
				}	break;
			case FieldInfo.TYPE_INT64:
			case FieldInfo.TYPE_SEQUENCE:
			{	
				
				
				
			   try {
				   
				sub.andIn(fn, ArrayUtil.toLongArray(value));
			   } catch (Exception e) {
				   throw new ParseException(e.getMessage());
			   }
			}	break;
			
			     case FieldInfo.TYPE_INT32: 
			     case FieldInfo.TYPE_INT24: 
			     case FieldInfo.TYPE_BYTE : 
			     case FieldInfo.TYPE_SHORT :
			     {
			    		
					   try {
						sub.andIn(fn, ArrayUtil.toIntArray(value));
					   } catch (Exception e) {
						   throw new ParseException(e.getMessage());
					   }
			     }
			    	 break;
			     case FieldInfo.TYPE_BIT : 
			     case FieldInfo.TYPE_BIT2 :
			     case FieldInfo.TYPE_BIT4 :
			     {
			    		
					   try {
						sub.andIn(fn, ArrayUtil.toByteArray(value));
					   } catch (Exception e) {
						   throw new ParseException(e.getMessage());
					   }
			     }
			     break;
			 
			 		
			    default :throw new ParseException("field not support in operator:"+fn);
			}
		}
		@Override
		public void where(SubCrit subt) {
			req.mergeAnd(subt);
			
		}
		@Override
		public RunResult runRequest(SouwenClient ss) throws Exception {
			
			switch(cmdType){
			
			case SEARCH:	return new RunResult(req,cmdType,ss.query(req));
			}
			return null;
		}
	}

	
	public static class RunResult{
		
		CmdType cmdType;
		Object result;
		QueryRequest req;
		public RunResult(QueryRequest req,CmdType cmdType,Object result){
			this.req=req;
			this.cmdType=cmdType;
			this.result=result;
		}
		
		
		public CmdType getCmdType() {
			return cmdType;
		}
		public Object getResult() {
			return result;
		}


		public QueryRequest getQueryRequest() {
			return req;
		}
		
	}
	
	public static class PQryReq extends QueryRequest{
		
		
		String label;
		public PQryReq(String label) {
			this.label=label;
		}

		@Override
		public SubCrit createSubCrit() {
			return new PSubCrit(this);
		}

		@Override
		public String toString() {
			return label;
		}

		public  class PSubCrit extends SubCrit{

			protected PSubCrit(QueryRequest request) {
				super(request);
			}

			@Override
			public String toString() {
				return label;
			}

			@Override
			public void toStringBuffer(StringBuffer strb) {
				strb.append(label) ;
			}

			@Override
			public SubCrit and(Criteria bt) {
				label= label +" .and. "+bt.toString();
				System.out.println(label);
				return super.and(bt);
			}

			@Override
			public SubCrit or(Criteria bt) {
				label= label +" .or. "+bt.toString();
				System.out.println(label);
				return super.or(bt);
			}

			@Override
			public SubCrit andNot(Criteria bt) {
				label= label +" .and not. "+bt.toString();
				System.out.println(label);
				return super.andNot(bt);
			}

			@Override
			public SubCrit orNot(Criteria bt) {
				label= label +" .or not. "+bt.toString();
				System.out.println(label);
				return super.orNot(bt);
			}
			
		}
		
	
		
	}
	

	public abstract void askBegin(String image);


	public abstract void queryCmd(CmdType search) throws ParseException;


	public abstract void askNum(String image);


	public abstract void target(String image);


	public abstract void groupBegin(String image);


	public abstract void groupStep(String image);


	public abstract void orderBy(SortType rela);

	public abstract void orderBy(SortType fieldAsc,String func);


	public abstract void sum(SumType estimate);


	public abstract void fieldSum(List<String> sumlist) throws ParseException;


	public abstract void createRequest();
	public abstract SubCrit oneFieldCrit(String image, OpType op, String value)throws ParseException;
	public abstract SubCrit oneFieldCrit(String image, OpType between, String beginV,String endV) throws ParseException ;
	public abstract SubCrit oneFieldCrit(String image, String op, List<String> value) throws ParseException;

	public abstract void where(SubCrit subt);

	public abstract void cube2f_max(String image) ;
	public abstract void cube2fn(String image);
	public abstract void miningParams(List<String> list);
	public abstract void simhash_threshold(String image);
	public abstract RunResult runRequest(SouwenClient ss) throws Exception;
	public abstract void table_name(String image);

	/**
	 * sql:SEARCH [0,20] orderby=heat('IN+BJ') sum= count where ((TX : 'BY' AND RQ > 0 AND BI between 1,2) AND (((BJ = 1) AND NOT (AU = '分林会''员涉,abc%')) OR (BK = 2))) AND NOT (AL in '分林会''员涉,abc%')
	 * */
	public static QueryRequest fromSql(BaseStructure ss,String sql) throws Exception{
		RequestParser tp=new RequestParser(new StringReader(sql));
		 RequestBuilder tb= new RequestBuilder.X(ss);
        tp.fillTo(tb);
        return tb.getRequest();
	}
	
	public static QueryRequest fromSql(RequestBuilder tb,String sql) throws Exception{
		RequestParser tp=new RequestParser(new StringReader(sql));
	    tp.fillTo(tb);
        return tb.getRequest();
	}
	/**
	 * sql:((TX : 'BY' AND RQ > 0 AND BI between 1,2) AND (((BJ = 1) AND NOT (AU = '分林会''员涉,abc%')) OR (BK = 2))) AND NOT (AL in '分林会''员涉,abc%')
	 * */
	public static QueryRequest fromWhere(QueryRequest  blank_req,String sql) throws Exception{
		RequestParser tp=new RequestParser(new StringReader(sql));
		 RequestBuilder tb= new RequestBuilder.X(blank_req);
        tp.fillOnlyWhereTo(tb);
        return tb.getRequest();
	}
	/**
	 * sql:((TX : 'BY' AND RQ > 0 AND BI between 1,2) AND (((BJ = 1) AND NOT (AU = '分林会''员涉,abc%')) OR (BK = 2))) AND NOT (AL in '分林会''员涉,abc%')
	 * */
	public static QueryRequest fromWhere(BaseStructure ss,String sql) throws Exception{
		RequestParser tp=new RequestParser(new StringReader(sql));
		 RequestBuilder tb= new RequestBuilder.X(ss);
        tp.fillOnlyWhereTo(tb);
        return tb.getRequest();
	}


	

	public static RunResult runSql(SouwenClient ss,String sql) throws Exception{
		RequestParser tp=new RequestParser(new StringReader(sql));
		 RequestBuilder tb= new RequestBuilder.X(ss);
        tp.fillTo(tb);
        return tb.runRequest(ss);
	}


	

	
	


	




	





}
