/* Generated By:JavaCC: Do not edit this line. RequestParserConstants.java */
package cn.sowjz.souwen.v1.req_parser;


/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface RequestParserConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int AND = 2;
  /** RegularExpression Id. */
  int OR = 3;
  /** RegularExpression Id. */
  int NOT = 4;
  /** RegularExpression Id. */
  int ORNOT = 5;
  /** RegularExpression Id. */
  int ANDNOT = 6;
  /** RegularExpression Id. */
  int ORDERBY = 7;
  /** RegularExpression Id. */
  int SEARCH = 8;
  /** RegularExpression Id. */
  int DISTINCT = 9;
  /** RegularExpression Id. */
  int GROUP = 10;
  /** RegularExpression Id. */
  int CUBE = 11;
  /** RegularExpression Id. */
  int KEYWORDS = 12;
  /** RegularExpression Id. */
  int WORDCLOUD = 13;
  /** RegularExpression Id. */
  int WAM = 14;
  /** RegularExpression Id. */
  int CLUSTER = 15;
  /** RegularExpression Id. */
  int UNITED = 16;
  /** RegularExpression Id. */
  int ALL = 17;
  /** RegularExpression Id. */
  int TIME = 18;
  /** RegularExpression Id. */
  int rela = 19;
  /** RegularExpression Id. */
  int random = 20;
  /** RegularExpression Id. */
  int COPIES = 21;
  /** RegularExpression Id. */
  int time_asc = 22;
  /** RegularExpression Id. */
  int field_desc = 23;
  /** RegularExpression Id. */
  int field_asc = 24;
  /** RegularExpression Id. */
  int heat = 25;
  /** RegularExpression Id. */
  int SUM = 26;
  /** RegularExpression Id. */
  int none = 27;
  /** RegularExpression Id. */
  int COUNT = 28;
  /** RegularExpression Id. */
  int estimate = 29;
  /** RegularExpression Id. */
  int WHERE = 30;
  /** RegularExpression Id. */
  int params = 31;
  /** RegularExpression Id. */
  int asknum = 32;
  /** RegularExpression Id. */
  int between = 33;
  /** RegularExpression Id. */
  int host_sn = 34;
  /** RegularExpression Id. */
  int QUOTED = 35;
  /** RegularExpression Id. */
  int CAPITAL = 36;
  /** RegularExpression Id. */
  int LETTER = 37;
  /** RegularExpression Id. */
  int DIGIT = 38;
  /** RegularExpression Id. */
  int DIGITS = 39;
  /** RegularExpression Id. */
  int NUMBER = 40;
  /** RegularExpression Id. */
  int FLOAT = 41;
  /** RegularExpression Id. */
  int FIELD = 42;
  /** RegularExpression Id. */
  int HEX = 43;
  /** RegularExpression Id. */
  int COMMA = 44;
  /** RegularExpression Id. */
  int PERIOD = 45;
  /** RegularExpression Id. */
  int LPAREN = 46;
  /** RegularExpression Id. */
  int RPAREN = 47;
  /** RegularExpression Id. */
  int LBRACE = 48;
  /** RegularExpression Id. */
  int RBRACE = 49;
  /** RegularExpression Id. */
  int LSBRACE = 50;
  /** RegularExpression Id. */
  int RSBRACE = 51;
  /** RegularExpression Id. */
  int EQ = 52;
  /** RegularExpression Id. */
  int EQ2 = 53;
  /** RegularExpression Id. */
  int NE = 54;
  /** RegularExpression Id. */
  int NE2 = 55;
  /** RegularExpression Id. */
  int LT = 56;
  /** RegularExpression Id. */
  int LE = 57;
  /** RegularExpression Id. */
  int GT = 58;
  /** RegularExpression Id. */
  int GE = 59;
  /** RegularExpression Id. */
  int STAR = 60;
  /** RegularExpression Id. */
  int SLASH = 61;
  /** RegularExpression Id. */
  int PLUS = 62;
  /** RegularExpression Id. */
  int MINUS = 63;
  /** RegularExpression Id. */
  int COLON = 64;
  /** RegularExpression Id. */
  int CONCAT_OP = 65;
  /** RegularExpression Id. */
  int SEARCH_TEXT = 66;
  /** RegularExpression Id. */
  int AMP_OP = 67;
  /** RegularExpression Id. */
  int SIMHASH_EQ = 68;
  /** RegularExpression Id. */
  int NOTIN = 69;
  /** RegularExpression Id. */
  int FROM = 70;
  /** RegularExpression Id. */
  int TABLE_NAME = 71;

  /** Lexical state. */
  int DEFAULT = 0;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"AND\"",
    "\"OR\"",
    "\"NOT\"",
    "\"ORNOT\"",
    "\"ANDNOT\"",
    "\"orderby\"",
    "\"SEARCH\"",
    "\"DISTINCT\"",
    "\"GROUP\"",
    "\"CUBE\"",
    "\"KEYWORDS\"",
    "\"WORDCLOUD\"",
    "\"WAM\"",
    "\"CLUSTER\"",
    "\"UNITED\"",
    "\"ALL\"",
    "\"time\"",
    "\"rela\"",
    "\"random\"",
    "\"COPIES\"",
    "\"time_asc\"",
    "\"field_desc\"",
    "\"field_asc\"",
    "<heat>",
    "\"sum\"",
    "\"none\"",
    "\"count\"",
    "\"estimate\"",
    "\"where\"",
    "\"params\"",
    "\"asknum\"",
    "\"between\"",
    "\"host_sn\"",
    "<QUOTED>",
    "<CAPITAL>",
    "<LETTER>",
    "<DIGIT>",
    "<DIGITS>",
    "<NUMBER>",
    "<FLOAT>",
    "<FIELD>",
    "<HEX>",
    "\",\"",
    "\".\"",
    "\"(\"",
    "\")\"",
    "\"{\"",
    "\"}\"",
    "\"[\"",
    "\"]\"",
    "\"=\"",
    "\"==\"",
    "\"<>\"",
    "\"!=\"",
    "\"<\"",
    "\"<=\"",
    "\">\"",
    "\">=\"",
    "\"*\"",
    "\"/\"",
    "\"+\"",
    "\"-\"",
    "\":\"",
    "\"|\"",
    "<SEARCH_TEXT>",
    "\"&\"",
    "<SIMHASH_EQ>",
    "\"notin\"",
    "\"from\"",
    "<TABLE_NAME>",
  };

}
