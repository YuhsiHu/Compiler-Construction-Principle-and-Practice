package grama;

import java.util.ArrayList;

import lex.Word;

/**
 * 分析栈节点类 String type;//节点类型 String name;//节点名 Object value;//节点值
 */
public class AnalyzeNode {
	public final static String NONTERMINAL = "非终结符";
	public final static String TERMINAL = "终结符";
	public final static String ACTIONSIGN = "动作符";
	public final static String END = "结束符";

	static ArrayList<String> nonterminal = new ArrayList<String>();// 非终结符集合
	static ArrayList<String> actionSign = new ArrayList<String>();// 动作符集合

	static {
		// N:S,B,A,C,,X,R,Z,Z’,U,U’,E,E’,H,H’,G,M,D,L,L’,T,T’,F,O,P,Q
		nonterminal.add("S");
		nonterminal.add("A");
		nonterminal.add("B");
		nonterminal.add("C");
		nonterminal.add("D");
		nonterminal.add("E");
		nonterminal.add("F");
		nonterminal.add("G");
		nonterminal.add("H");
		nonterminal.add("L");
		nonterminal.add("M");
		nonterminal.add("O");
		nonterminal.add("P");
		nonterminal.add("Q");
		nonterminal.add("X");
		nonterminal.add("Y");
		nonterminal.add("Z");
		nonterminal.add("R");
		nonterminal.add("U");
		nonterminal.add("Z'");
		nonterminal.add("U'");
		nonterminal.add("E'");
		nonterminal.add("H'");
		nonterminal.add("L'");
		nonterminal.add("T");
		nonterminal.add("T'");
		actionSign.add("@ADD_SUB");
		actionSign.add("@ADD");
		actionSign.add("@SUB");
		actionSign.add("@DIV_MUL");
		actionSign.add("@DIV");
		actionSign.add("@MUL");
		actionSign.add("@SINGLE");
		actionSign.add("@SINGTLE_OP");
		actionSign.add("@ASS_R");
		actionSign.add("@ASS_Q");
		actionSign.add("@ASS_F");
		actionSign.add("@ASS_U");
		actionSign.add("@TRAN_LF");
		actionSign.add("@EQ");
		actionSign.add("@EQ_U'");
		actionSign.add("@COMPARE");
		actionSign.add("@COMPARE_OP");
		actionSign.add("@IF_FJ");
		actionSign.add("@IF_BACKPATCH_FJ");
		actionSign.add("@IF_RJ");
		actionSign.add("@IF_BACKPATCH_RJ");
		actionSign.add("@WHILE_FJ");
		actionSign.add("@WHILE_BACKPATCH_FJ");
		actionSign.add("@IF_RJ");
		actionSign.add("@FOR_FJ");
		actionSign.add("@FOR_RJ");
		actionSign.add("@FOR_BACKPATCH_FJ");
	}

	String type;// 节点类型
	String name;// 节点名
	String value;// 节点值

	/**
	 * 判断是不是非终结符
	 * 
	 * @param node
	 * @return
	 */
	public static boolean isNonterm(AnalyzeNode node) {
		return nonterminal.contains(node.name);
	}

	/**
	 * 判断是不是终结符
	 * 
	 * @param node
	 * @return
	 */
	public static boolean isTerm(AnalyzeNode node) {
		return Word.isKey(node.name) || Word.isOperator(node.name) || Word.isBoundarySign(node.name)
				|| node.name.equals("id") || node.name.equals("num") || node.name.equals("ch");
	}

	/**
	 * 判断是不是动作符
	 * 
	 * @param node
	 * @return
	 */
	public static boolean isActionSign(AnalyzeNode node) {
		return actionSign.contains(node.name);
	}

	/**
	 * 无参构造函数
	 */
	public AnalyzeNode() {

	}

	/**
	 * 有参构造函数
	 * 
	 * @param type
	 *            类型
	 * @param name
	 *            名字
	 * @param value
	 *            值
	 */
	public AnalyzeNode(String type, String name, String value) {
		this.type = type;
		this.name = name;
		this.value = value;
	}

}
