package lex;

import java.util.ArrayList;


/**
 * 单词类
 * 
 *  1单词序号 
 *  2单词的值 
 *  3单词类型 
 *  4单词所在行 
 *  5单词是否合法
 */
public class Word {
	public final static String KEY = "关键字";
	public final static String OPERATOR = "运算符";
	public final static String INT_CONST = "整形常量";
	public final static String CHAR_CONST = "字符常量";
	public final static String BOOL_CONST = "布尔常量";
	public final static String IDENTIFIER = "标志符";
	public final static String BOUNDARYSIGN = "界符";
	public final static String END = "结束符";
	public final static String UNIDEF = "未知类型";
	
	public static ArrayList<String> key = new ArrayList<String>();// 关键字集合
	public static ArrayList<String> boundarySign = new ArrayList<String>();// 界符集合
	public static ArrayList<String> operator = new ArrayList<String>();// 运算符集合
	
	static {
		//运算符集合
		Word.operator.add("+");
		Word.operator.add("-");
		Word.operator.add("++");
		Word.operator.add("--");
		Word.operator.add("*");
		Word.operator.add("/");
		Word.operator.add(">");
		Word.operator.add("<");
		Word.operator.add(">=");
		Word.operator.add("<=");
		Word.operator.add("==");
		Word.operator.add("!=");
		Word.operator.add("=");
		Word.operator.add("&&");
		Word.operator.add("||");
		Word.operator.add("!");
		Word.operator.add(".");
		Word.operator.add("?");
		Word.operator.add("|");
		Word.operator.add("&");
		//界符集合
		Word.boundarySign.add("(");
		Word.boundarySign.add(")");
		Word.boundarySign.add("{");
		Word.boundarySign.add("}");
		Word.boundarySign.add(";");
		Word.boundarySign.add(",");
		//关键字集合
		Word.key.add("void");
		Word.key.add("main");
		Word.key.add("int");
		Word.key.add("char");
		Word.key.add("if");
		Word.key.add("else");
		Word.key.add("while");
		Word.key.add("for");
		Word.key.add("printf");
		Word.key.add("scanf");
	}
	
	int id;// 单词序号
	String value;// 单词的值
	String type;// 单词类型
	int line;// 单词所在行
	boolean flag = true;//单词是否合法

	/**
	 * 无参构造函数
	 */
	public Word() {

	}

	/**
	 * 
	 * @param id 序号
	 * @param value 值
	 * @param type 类型
	 * @param line 行号
	 */
	public Word(int id, String value, String type, int line) {
		this.id = id;
		this.value = value;
		this.type = type;
		this.line = line;
	}

	/**
	 * 
	 * @param word 单词
	 * @return 是否为关键字
	 */
	public static boolean isKey(String word) {
		return key.contains(word);
	}

	/**
	 * 
	 * @param word 单词
	 * @return 是否为运算符
	 */
	public static boolean isOperator(String word) {
		return operator.contains(word);
	}

	/**
	 * 
	 * @param word 单词
	 * @return 是否为界符
	 */
	public static boolean isBoundarySign(String word) {
		return boundarySign.contains(word);
	}

	/**
	 * 
	 * @param word 单词
	 * @return 是否为算术运算符
	 */
	public static boolean isArOP(String word) {
		if ((word.equals("+") || word.equals("-") || word.equals("*") || word.equals("/")))
			return true;
		else
			return false;
	}

	/**
	 * 
	 * @param word 单词
	 * @return 是否为布尔运算符
	 */
	public static boolean isBoolOP(String word) {
		if ((word.equals(">") || word.equals("<") || word.equals("==")|| word.equals("!=") || word.equals("!") || word.equals("&&") || word.equals("||")))
			return true;
		else
			return false;
	}
}
