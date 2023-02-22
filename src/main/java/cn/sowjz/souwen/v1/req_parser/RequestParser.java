/* Generated By:JavaCC: Do not edit this line. RequestParser.java */
package cn.sowjz.souwen.v1.req_parser;

import java.io.StringReader;
import java.util.*;
import cn.sowjz.souwen.v1.query.request.SubCrit;

public class RequestParser implements RequestParserConstants {


    static public enum SumType { none,count,estimate }
     static public enum SortType { time,rela,random,COPIES,time_asc,field_desc,field_asc,heat }
     static public enum CmdType { SEARCH,GROUP,DISTINCT,CUBE,KEYWORDS,WORDCLOUD,WAM,CLUSTER,UNITED }
     static public enum OpType { INDEX,GREATTHAN,LESSTHAN,EQUAL,UNEQUAL,GREATEQUAL,LESSEQUAL,BETWEEN,IN,COMPARE,NOTIN,SEARCH,BITAND,MASK_EQUAL }
     static public enum SubRunType { AND,ANDNOT,OR,ORNOT }


        public static void main(String [] args) throws Exception {
                String sql="SEARCH [0,1000] orderby=time sum= count WHERE TI : '((\u5a11\u5825\u69fb|\u5a11\u5825\u69fb\u675e\ue6e2\u5a11\u5825\u69fb\u935b\u69ba\u5a11\u5825\u69fb\u95c3\u7132\u5a11\u5825\u69fb\u95c3\u71b7\u61b3|119|\u940f\ue0a4\u4f28|\u940f\ue0a2\u5a0d|\u74a7\u98ce\u4f00|\u942b\ufffd\u940f\u73c5\u6fb6\u8fa9\u4f00\u9416\u55d9\u5667|\u9415\u51aa\u578e|\u95c2\ue046\u578e)+(\u9357\u693e\u542b|\u941c\u52ec\ue11f|\u7ec9\ufe3d\u6a0a|\u5be4\u6d2a\u5064))' AND PU between 1644163200,1644249600 AND RE == 0";
                RequestParser t=new RequestParser(new StringReader(sql));

                t.fillTo(new RequestBuilder.Simple());
        }


        private String removeQuote(String tx) {
                        if(tx==null) return tx;
                        int len=tx.length();

                        if(tx.length()>=2&& tx.charAt(0)=='\u005c''&& tx.charAt(len-1)=='\u005c'')
                        {
                            StringBuffer strb=new StringBuffer();
                            char last=0;
                                for(int i=1;i<len-1;i++) {
                                        char c=tx.charAt(i);
                                        if(last!='\u005c'' || c!='\u005c'')
                                           strb.append(c);
                                    last=c;
                                }
                                return strb.toString();
                        }
                        return tx;
    }

  final public void fillTo(RequestBuilder tb) throws ParseException {
         Token t0;
 SubCrit subt;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case SEARCH:
    case DISTINCT:
    case GROUP:
    case CUBE:
    case KEYWORDS:
    case WORDCLOUD:
    case WAM:
    case CLUSTER:
      queryCluse(tb);
      break;
    case UNITED:
      jj_consume_token(UNITED);
      tb.queryCmd(CmdType.UNITED);
      jj_consume_token(LPAREN);
      queryCluse(tb);
      label_1:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case COMMA:
          ;
          break;
        default:
          jj_la1[0] = jj_gen;
          break label_1;
        }
        jj_consume_token(COMMA);
        queryCluse(tb);
      }
      jj_consume_token(RPAREN);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case FROM:
        jj_consume_token(FROM);
        t0 = jj_consume_token(TABLE_NAME);
    tb.table_name(t0.image);
        break;
      default:
        jj_la1[1] = jj_gen;
        ;
      }
      break;
    default:
      jj_la1[2] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    jj_consume_token(WHERE);
    tb.createRequest();
    subt = whereClause(tb);
    jj_consume_token(0);
     tb.where(subt);
  }

  final public void fillOnlyWhereTo(RequestBuilder tb) throws ParseException {
  SubCrit subt;
  tb.createRequest();
    subt = whereClause(tb);
    jj_consume_token(0);
   tb.where(subt);
  }

  final public void queryCluse(RequestBuilder tb) throws ParseException {
  Token t0;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case SEARCH:
      jj_consume_token(SEARCH);
      tb.queryCmd(CmdType.SEARCH);
      ask_order_sum(tb);
      break;
    case DISTINCT:
      jj_consume_token(DISTINCT);
          tb.queryCmd(CmdType.DISTINCT);
      ask_order_sum(tb);
      params(tb);
      break;
    case GROUP:
      jj_consume_token(GROUP);
     tb.queryCmd(CmdType.GROUP);
      jj_consume_token(asknum);
      jj_consume_token(EQ);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NUMBER:
        t0 = jj_consume_token(NUMBER);
                tb.askNum(t0.image);
        break;
      case ALL:
        jj_consume_token(ALL);
                {tb.askNum("0");}
        break;
      default:
        jj_la1[3] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      params(tb);
      break;
    case CUBE:
      jj_consume_token(CUBE);
       tb.queryCmd(CmdType.CUBE);
      jj_consume_token(asknum);
      jj_consume_token(EQ);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NUMBER:
        t0 = jj_consume_token(NUMBER);
                tb.askNum(t0.image);
        break;
      case ALL:
        jj_consume_token(ALL);
                {tb.askNum("0");}
        break;
      default:
        jj_la1[4] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      jj_consume_token(CONCAT_OP);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NUMBER:
        t0 = jj_consume_token(NUMBER);
                tb.cube2f_max(t0.image);
        break;
      case ALL:
        jj_consume_token(ALL);
                {tb.cube2f_max("0");}
        break;
      default:
        jj_la1[5] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      params_cube(tb);
      break;
    case KEYWORDS:
      jj_consume_token(KEYWORDS);
          tb.queryCmd(CmdType.KEYWORDS);
      ask_order_sum(tb);
      params(tb);
      break;
    case WORDCLOUD:
    case WAM:
    case CLUSTER:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case WORDCLOUD:
        jj_consume_token(WORDCLOUD);
          tb.queryCmd(CmdType.WORDCLOUD);
        break;
      case WAM:
        jj_consume_token(WAM);
        tb.queryCmd(CmdType.WAM);
        break;
      case CLUSTER:
        jj_consume_token(CLUSTER);
        tb.queryCmd(CmdType.CLUSTER);
        break;
      default:
        jj_la1[6] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      ask_order_sum(tb);
      params_mining(tb);
      break;
    default:
      jj_la1[7] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case FROM:
      jj_consume_token(FROM);
      t0 = jj_consume_token(TABLE_NAME);
    tb.table_name(t0.image);
      break;
    default:
      jj_la1[8] = jj_gen;
      ;
    }
  }

  final public void params_mining(RequestBuilder tb) throws ParseException {
    Token t0;
    List< String> list=new ArrayList< String>();
    jj_consume_token(params);
    jj_consume_token(EQ);
    jj_consume_token(LSBRACE);
    t0 = field_name();
         tb.target(t0.image);
    label_2:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMA:
        ;
        break;
      default:
        jj_la1[9] = jj_gen;
        break label_2;
      }
      jj_consume_token(COMMA);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NUMBER:
        t0 = jj_consume_token(NUMBER);
         list.add(t0.image);
        break;
      case FLOAT:
        t0 = jj_consume_token(FLOAT);
         list.add(t0.image);
        break;
      default:
        jj_la1[10] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    jj_consume_token(RSBRACE);
      tb.miningParams(list);
  }

  final public Token number() throws ParseException {
   Token t0;
    t0 = jj_consume_token(NUMBER);
      {if (true) return t0;}
    throw new Error("Missing return statement in function");
  }

  final public void params_cube(RequestBuilder tb) throws ParseException {
    Token t0;
    List< String> sumlist=new ArrayList< String>();
    jj_consume_token(params);
    jj_consume_token(EQ);
    jj_consume_token(LSBRACE);
    t0 = field_name();
         tb.target(t0.image);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LPAREN:
      jj_consume_token(LPAREN);
      t0 = jj_consume_token(NUMBER);
                tb.groupBegin(t0.image);
      jj_consume_token(COMMA);
      t0 = jj_consume_token(NUMBER);
                tb.groupStep(t0.image);
      jj_consume_token(RPAREN);
      break;
    default:
      jj_la1[11] = jj_gen;
      ;
    }
    jj_consume_token(COMMA);
    t0 = field_name();
         tb.cube2fn(t0.image);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case COMMA:
      jj_consume_token(COMMA);
      jj_consume_token(SUM);
      jj_consume_token(LPAREN);
      t0 = field_name();
      sumlist.add(t0.image.toUpperCase());
      label_3:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case COMMA:
          ;
          break;
        default:
          jj_la1[12] = jj_gen;
          break label_3;
        }
        jj_consume_token(COMMA);
        t0 = field_name();
           sumlist.add(t0.image.toUpperCase());
      }
      jj_consume_token(RPAREN);
        tb.fieldSum(sumlist);
      break;
    default:
      jj_la1[13] = jj_gen;
      ;
    }
    jj_consume_token(RSBRACE);
  }

  final public void params(RequestBuilder tb) throws ParseException {
    Token t0;
    List< String> sumlist=new ArrayList< String>();
    jj_consume_token(params);
    jj_consume_token(EQ);
    jj_consume_token(LSBRACE);
    t0 = field_name();
         tb.target(t0.image);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LPAREN:
      jj_consume_token(LPAREN);
      t0 = jj_consume_token(NUMBER);
                tb.groupBegin(t0.image);
      jj_consume_token(COMMA);
      t0 = jj_consume_token(NUMBER);
                tb.groupStep(t0.image);
      jj_consume_token(RPAREN);
      break;
    default:
      jj_la1[14] = jj_gen;
      ;
    }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case COMMA:
      jj_consume_token(COMMA);
      jj_consume_token(SUM);
      jj_consume_token(LPAREN);
      t0 = field_name();
      sumlist.add(t0.image);
      label_4:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case COMMA:
          ;
          break;
        default:
          jj_la1[15] = jj_gen;
          break label_4;
        }
        jj_consume_token(COMMA);
        t0 = field_name();
           sumlist.add(t0.image);
      }
      jj_consume_token(RPAREN);
        tb.fieldSum(sumlist);
      break;
    default:
      jj_la1[16] = jj_gen;
      ;
    }
    jj_consume_token(RSBRACE);
  }

  final public Token field_name() throws ParseException {
   Token t0;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case FIELD:
      t0 = jj_consume_token(FIELD);
      break;
    case OR:
      t0 = jj_consume_token(OR);
      break;
    default:
      jj_la1[17] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
      {if (true) return t0;}
    throw new Error("Missing return statement in function");
  }

  final public void ask_order_sum(RequestBuilder tb) throws ParseException {
    Token t0;
     Token t1;
    jj_consume_token(LSBRACE);
    t0 = jj_consume_token(NUMBER);
     tb.askBegin(t0.image);
    jj_consume_token(COMMA);
    t0 = jj_consume_token(NUMBER);
          tb.askNum(t0.image);
    jj_consume_token(RSBRACE);
    label_5:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ORDERBY:
      case SUM:
        ;
        break;
      default:
        jj_la1[18] = jj_gen;
        break label_5;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ORDERBY:
        jj_consume_token(ORDERBY);
        jj_consume_token(EQ);
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case TIME:
          jj_consume_token(TIME);
     tb.orderBy(SortType.time);
          break;
        case rela:
          jj_consume_token(rela);
 tb.orderBy(SortType.rela);
          break;
        case random:
          jj_consume_token(random);
 tb.orderBy(SortType.random);
          break;
        case COPIES:
          jj_consume_token(COPIES);
 tb.orderBy(SortType.COPIES);
          break;
        case time_asc:
          jj_consume_token(time_asc);
 tb.orderBy(SortType.time_asc);
          break;
        case field_desc:
          jj_consume_token(field_desc);
          jj_consume_token(LPAREN);
          t0 = jj_consume_token(QUOTED);
          jj_consume_token(RPAREN);
 tb.orderBy(SortType.field_desc,removeQuote(t0.image));
          break;
        case field_asc:
          jj_consume_token(field_asc);
          jj_consume_token(LPAREN);
          t0 = jj_consume_token(QUOTED);
          jj_consume_token(RPAREN);
 tb.orderBy(SortType.field_asc,removeQuote(t0.image));
          break;
        case heat:
          jj_consume_token(heat);
          jj_consume_token(LPAREN);
          t0 = jj_consume_token(QUOTED);
          jj_consume_token(RPAREN);
 tb.orderBy(SortType.heat,removeQuote(t0.image));
          break;
        default:
          jj_la1[19] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
        break;
      case SUM:
        jj_consume_token(SUM);
        jj_consume_token(EQ);
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case none:
          t0 = jj_consume_token(none);
    tb.sum(SumType.none);
          break;
        case COUNT:
          t0 = jj_consume_token(COUNT);
 tb.sum(SumType.count);
          break;
        case estimate:
          t0 = jj_consume_token(estimate);
 tb.sum(SumType.estimate);
          break;
        default:
          jj_la1[20] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
        break;
      default:
        jj_la1[21] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  }

  final public SubCrit whereClause(RequestBuilder tb) throws ParseException {
    SubCrit subt1;
    SubCrit subt2;
    subt1 = expression(tb);
    label_6:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case OR:
        ;
        break;
      default:
        jj_la1[22] = jj_gen;
        break label_6;
      }
      if (jj_2_1(2)) {
        jj_consume_token(OR);
        subt2 = expression(tb);
          subt1=subt1.or(subt2);
      } else {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case OR:
          jj_consume_token(OR);
          jj_consume_token(NOT);
          subt2 = expression(tb);
          subt1=subt1.orNot(subt2);
          break;
        default:
          jj_la1[23] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
      }
    }
       {if (true) return subt1;}
    throw new Error("Missing return statement in function");
  }

  final public SubCrit expression(RequestBuilder tb) throws ParseException {
    SubCrit subt1;
    SubCrit subt2;
    subt1 = primary(tb);
    label_7:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case AND:
        ;
        break;
      default:
        jj_la1[24] = jj_gen;
        break label_7;
      }
      if (jj_2_2(2)) {
        jj_consume_token(AND);
        jj_consume_token(NOT);
        subt2 = primary(tb);
           subt1=subt1.andNot(subt2);
      } else {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case AND:
          jj_consume_token(AND);
          subt2 = primary(tb);
       subt1=subt1.mergeAnd(subt2);
          break;
        default:
          jj_la1[25] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
      }
    }
       {if (true) return subt1;}
    throw new Error("Missing return statement in function");
  }

  final public SubCrit primary(RequestBuilder tb) throws ParseException {
  Token t;
  SubCrit subt;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case OR:
    case host_sn:
    case FIELD:
      subt = one_field(tb);
 {if (true) return subt;}
      break;
    case LPAREN:
      jj_consume_token(LPAREN);
      subt = whereClause(tb);
      jj_consume_token(RPAREN);
 {if (true) return subt;}
      break;
    default:
      jj_la1[26] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public SubCrit one_field(RequestBuilder tb) throws ParseException {
  Token t=null;
  Token t2=null;
   Token t3=null;
  OpType op=null;
  List< String> list=new ArrayList< String>();
  String value;
  String field_name;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case FIELD:
      t = jj_consume_token(FIELD);
                    field_name=t.image;
      break;
    case OR:
      t = jj_consume_token(OR);
                   field_name=t.image;
      break;
    case host_sn:
      t = jj_consume_token(host_sn);
                        field_name="00";
      break;
    default:
      jj_la1[27] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case EQ:
    case EQ2:
    case NE:
    case NE2:
    case LT:
    case LE:
    case GT:
    case GE:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case EQ:
        jj_consume_token(EQ);
              op=OpType.EQUAL;
        break;
      case EQ2:
        jj_consume_token(EQ2);
                op=OpType.EQUAL;
        break;
      case NE:
        jj_consume_token(NE);
               op=OpType.UNEQUAL;
        break;
      case NE2:
        jj_consume_token(NE2);
                op=OpType.UNEQUAL;
        break;
      case LT:
        jj_consume_token(LT);
               op=OpType.LESSTHAN;
        break;
      case LE:
        jj_consume_token(LE);
               op=OpType.LESSEQUAL;
        break;
      case GT:
        jj_consume_token(GT);
               op=OpType.GREATTHAN;
        break;
      case GE:
        jj_consume_token(GE);
               op=OpType.GREATEQUAL;
        break;
      default:
        jj_la1[28] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NUMBER:
        t3 = jj_consume_token(NUMBER);
           value=t3.image;
        break;
      case QUOTED:
        t3 = jj_consume_token(QUOTED);
            value=removeQuote(t3.image);
        break;
      default:
        jj_la1[29] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case SLASH:
        jj_consume_token(SLASH);
        t3 = jj_consume_token(NUMBER);
          tb.simhash_threshold(t3.image);
        break;
      default:
        jj_la1[30] = jj_gen;
        ;
      }
       {if (true) return tb.oneFieldCrit(field_name,op,value);}
      break;
    case between:
      jj_consume_token(between);
      t2 = jj_consume_token(NUMBER);
      jj_consume_token(COMMA);
      t3 = jj_consume_token(NUMBER);
                         {if (true) return tb.oneFieldCrit(field_name,OpType.BETWEEN, t2.image,t3.image);}
      break;
    case FIELD:
    case NOTIN:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case FIELD:
        t2 = jj_consume_token(FIELD);
        break;
      case NOTIN:
        t2 = jj_consume_token(NOTIN);
        break;
      default:
        jj_la1[31] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      jj_consume_token(LSBRACE);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case QUOTED:
        t3 = jj_consume_token(QUOTED);
          list.add(removeQuote(t3.image));
        break;
      case NUMBER:
        t3 = jj_consume_token(NUMBER);
            list.add(t3.image);
        break;
      default:
        jj_la1[32] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      label_8:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case COMMA:
          ;
          break;
        default:
          jj_la1[33] = jj_gen;
          break label_8;
        }
        jj_consume_token(COMMA);
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case QUOTED:
          t3 = jj_consume_token(QUOTED);
          list.add(removeQuote(t3.image));
          break;
        case NUMBER:
          t3 = jj_consume_token(NUMBER);
            list.add(t3.image);
          break;
        default:
          jj_la1[34] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
      }
      jj_consume_token(RSBRACE);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case SLASH:
        jj_consume_token(SLASH);
        t3 = jj_consume_token(NUMBER);
          tb.simhash_threshold(t3.image);
        break;
      default:
        jj_la1[35] = jj_gen;
        ;
      }
    {if (true) return tb.oneFieldCrit(field_name,t2.image,list);}
      break;
    case COLON:
      t2 = jj_consume_token(COLON);
      t3 = jj_consume_token(QUOTED);
    {if (true) return tb.oneFieldCrit(field_name,OpType.INDEX,removeQuote(t3.image));}
      break;
    case SEARCH_TEXT:
      t2 = jj_consume_token(SEARCH_TEXT);
      t3 = jj_consume_token(QUOTED);
    {if (true) return tb.oneFieldCrit(field_name,OpType.SEARCH,t2.image,removeQuote(t3.image));}
      break;
    case AMP_OP:
      jj_consume_token(AMP_OP);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NUMBER:
        t2 = jj_consume_token(NUMBER);
        break;
      case HEX:
        t2 = jj_consume_token(HEX);
        break;
      default:
        jj_la1[36] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case EQ:
        jj_consume_token(EQ);
                                              op=OpType.MASK_EQUAL;
        break;
      case NE2:
        jj_consume_token(NE2);
                                                                              op= OpType.BITAND ;
        break;
      default:
        jj_la1[37] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NUMBER:
        t3 = jj_consume_token(NUMBER);
        break;
      case HEX:
        t3 = jj_consume_token(HEX);
        break;
      default:
        jj_la1[38] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
     {if (true) return tb.oneFieldCrit(field_name,op,t2.image,t3.image);}
      break;
    default:
      jj_la1[39] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  private boolean jj_2_1(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_1(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(0, xla); }
  }

  private boolean jj_2_2(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_2(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(1, xla); }
  }

  private boolean jj_3R_13() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_14()) {
    jj_scanpos = xsp;
    if (jj_3R_15()) {
    jj_scanpos = xsp;
    if (jj_3R_16()) return true;
    }
    }
    return false;
  }

  private boolean jj_3R_16() {
    if (jj_scan_token(host_sn)) return true;
    return false;
  }

  private boolean jj_3R_15() {
    if (jj_scan_token(OR)) return true;
    return false;
  }

  private boolean jj_3R_12() {
    if (jj_scan_token(LPAREN)) return true;
    return false;
  }

  private boolean jj_3R_9() {
    if (jj_3R_10()) return true;
    return false;
  }

  private boolean jj_3_2() {
    if (jj_scan_token(AND)) return true;
    if (jj_scan_token(NOT)) return true;
    return false;
  }

  private boolean jj_3R_10() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_11()) {
    jj_scanpos = xsp;
    if (jj_3R_12()) return true;
    }
    return false;
  }

  private boolean jj_3R_11() {
    if (jj_3R_13()) return true;
    return false;
  }

  private boolean jj_3R_14() {
    if (jj_scan_token(FIELD)) return true;
    return false;
  }

  private boolean jj_3_1() {
    if (jj_scan_token(OR)) return true;
    if (jj_3R_9()) return true;
    return false;
  }

  /** Generated Token Manager. */
  public RequestParserTokenManager token_source;
  JavaCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private Token jj_scanpos, jj_lastpos;
  private int jj_la;
  private int jj_gen;
  final private int[] jj_la1 = new int[40];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static private int[] jj_la1_2;
  static {
      jj_la1_init_0();
      jj_la1_init_1();
      jj_la1_init_2();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x0,0x0,0x1ff00,0x20000,0x20000,0x20000,0xe000,0xff00,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x8,0x4000080,0x3fc0000,0x38000000,0x4000080,0x8,0x8,0x4,0x4,0x8,0x8,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,};
   }
   private static void jj_la1_init_1() {
      jj_la1_1 = new int[] {0x1000,0x0,0x0,0x100,0x100,0x100,0x0,0x0,0x0,0x1000,0x300,0x4000,0x1000,0x1000,0x4000,0x1000,0x1000,0x400,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x4404,0x404,0xff00000,0x108,0x20000000,0x400,0x108,0x1000,0x108,0x20000000,0x900,0x900000,0x900,0xff00402,};
   }
   private static void jj_la1_init_2() {
      jj_la1_2 = new int[] {0x0,0x40,0x0,0x0,0x0,0x0,0x0,0x0,0x40,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x20,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x2d,};
   }
  final private JJCalls[] jj_2_rtns = new JJCalls[2];
  private boolean jj_rescan = false;
  private int jj_gc = 0;

  /** Constructor with InputStream. */
  public RequestParser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public RequestParser(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new JavaCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new RequestParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 40; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 40; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor. */
  public RequestParser(java.io.Reader stream) {
    jj_input_stream = new JavaCharStream(stream, 1, 1);
    token_source = new RequestParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 40; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 40; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor with generated Token Manager. */
  public RequestParser(RequestParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 40; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(RequestParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 40; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      if (++jj_gc > 100) {
        jj_gc = 0;
        for (int i = 0; i < jj_2_rtns.length; i++) {
          JJCalls c = jj_2_rtns[i];
          while (c != null) {
            if (c.gen < jj_gen) c.first = null;
            c = c.next;
          }
        }
      }
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  static private final class LookaheadSuccess extends java.lang.Error { }
  final private LookaheadSuccess jj_ls = new LookaheadSuccess();
  private boolean jj_scan_token(int kind) {
    if (jj_scanpos == jj_lastpos) {
      jj_la--;
      if (jj_scanpos.next == null) {
        jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
      } else {
        jj_lastpos = jj_scanpos = jj_scanpos.next;
      }
    } else {
      jj_scanpos = jj_scanpos.next;
    }
    if (jj_rescan) {
      int i = 0; Token tok = token;
      while (tok != null && tok != jj_scanpos) { i++; tok = tok.next; }
      if (tok != null) jj_add_error_token(kind, i);
    }
    if (jj_scanpos.kind != kind) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) throw jj_ls;
    return false;
  }


/** Get the next Token. */
  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  private int[] jj_expentry;
  private int jj_kind = -1;
  private int[] jj_lasttokens = new int[100];
  private int jj_endpos;

  private void jj_add_error_token(int kind, int pos) {
    if (pos >= 100) return;
    if (pos == jj_endpos + 1) {
      jj_lasttokens[jj_endpos++] = kind;
    } else if (jj_endpos != 0) {
      jj_expentry = new int[jj_endpos];
      for (int i = 0; i < jj_endpos; i++) {
        jj_expentry[i] = jj_lasttokens[i];
      }
      jj_entries_loop: for (java.util.Iterator<?> it = jj_expentries.iterator(); it.hasNext();) {
        int[] oldentry = (int[])(it.next());
        if (oldentry.length == jj_expentry.length) {
          for (int i = 0; i < jj_expentry.length; i++) {
            if (oldentry[i] != jj_expentry[i]) {
              continue jj_entries_loop;
            }
          }
          jj_expentries.add(jj_expentry);
          break jj_entries_loop;
        }
      }
      if (pos != 0) jj_lasttokens[(jj_endpos = pos) - 1] = kind;
    }
  }

  /** Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[72];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 40; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
          if ((jj_la1_2[i] & (1<<j)) != 0) {
            la1tokens[64+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 72; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    jj_endpos = 0;
    jj_rescan_token();
    jj_add_error_token(0, 0);
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  final public void enable_tracing() {
  }

  /** Disable tracing. */
  final public void disable_tracing() {
  }

  private void jj_rescan_token() {
    jj_rescan = true;
    for (int i = 0; i < 2; i++) {
    try {
      JJCalls p = jj_2_rtns[i];
      do {
        if (p.gen > jj_gen) {
          jj_la = p.arg; jj_lastpos = jj_scanpos = p.first;
          switch (i) {
            case 0: jj_3_1(); break;
            case 1: jj_3_2(); break;
          }
        }
        p = p.next;
      } while (p != null);
      } catch(LookaheadSuccess ls) { }
    }
    jj_rescan = false;
  }

  private void jj_save(int index, int xla) {
    JJCalls p = jj_2_rtns[index];
    while (p.gen > jj_gen) {
      if (p.next == null) { p = p.next = new JJCalls(); break; }
      p = p.next;
    }
    p.gen = jj_gen + xla - jj_la; p.first = token; p.arg = xla;
  }

  static final class JJCalls {
    int gen;
    Token first;
    int arg;
    JJCalls next;
  }

}