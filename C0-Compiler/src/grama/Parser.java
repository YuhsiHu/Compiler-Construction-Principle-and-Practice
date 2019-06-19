package grama;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Stack;

import inter.FourElement;
import lex.LexAnalyse;
import lex.Word;
import lex.Error;
import inter.FourElement;

/**
 * 语法分析器
 * 
 * @author Hu Yuxi
 *
 */
public class Parser {

	private LexAnalyse lexAnalyse;// 词法分析器
	ArrayList<Word> wordList = new ArrayList<Word>();// 单词列表
	Stack<AnalyzeNode> analyseStack = new Stack<AnalyzeNode>();// 分析栈
	Stack<String> semanticStack = new Stack<String>();// 语义栈
	ArrayList<FourElement> fourElemList = new ArrayList<FourElement>();// 四元式列表
	ArrayList<Error> errorList = new ArrayList<Error>();// 错误信息列表
	StringBuffer bf;// 分析栈缓冲流
	int errorCount = 0;// 统计错误个数
	boolean graErrorFlag = false;// 语法分析出错标志
	int tempCount = 0;// 用于生成临时变量
	int fourElemCount = 0;// 统计四元式个数

	AnalyzeNode S, B, A, C, X, Y, R, Z, Z1, U, U1, E, E1, H, H1, G, M, D, L, L1, T, T1, F, O, P, Q;
	AnalyzeNode ADD_SUB, DIV_MUL, ADD, SUB, DIV, MUL, ASS_F, ASS_R, ASS_Q, ASS_U, TRAN_LF;
	AnalyzeNode SINGLE, SINGLE_OP, EQ, EQ_U1, COMPARE, COMPARE_OP, IF_FJ, IF_RJ, IF_BACKPATCH_FJ, IF_BACKPATCH_RJ;
	AnalyzeNode WHILE_FJ, WHILE_RJ, WHILE_BACKPATCH_FJ, FOR_FJ, FOR_RJ, FOR_BACKPATCH_FJ;
	AnalyzeNode top;// 当前栈顶元素
	Word firstWord;// 待分析单词
	String OP = null;
	String ARG1, ARG2, RES;
	Error error;
	// int if_fj,if_rj,while_fj,while_rj,for_fj,for_rj;
	Stack<Integer> if_fj, if_rj, while_fj, while_rj, for_fj, for_rj;// if while for 跳转地址栈
	Stack<String> for_op = new Stack<String>();

	/**
	 * 无参构造函数
	 */
	public Parser() {

	}

	/**
	 * 有参构造函数
	 * 
	 * @param lexAnalyse
	 */
	public Parser(LexAnalyse lexAnalyse) {
		this.lexAnalyse = lexAnalyse;
		this.wordList = lexAnalyse.wordList;
		init();
	}

	/**
	 * 
	 * @return
	 */
	private String newTemp() {
		tempCount++;
		return "T" + tempCount;
	}

	/**
	 * initial
	 */
	public void init() {
		S = new AnalyzeNode(AnalyzeNode.NONTERMINAL, "S", null);
		A = new AnalyzeNode(AnalyzeNode.NONTERMINAL, "A", null);
		B = new AnalyzeNode(AnalyzeNode.NONTERMINAL, "B", null);
		C = new AnalyzeNode(AnalyzeNode.NONTERMINAL, "C", null);
		X = new AnalyzeNode(AnalyzeNode.NONTERMINAL, "X", null);
		Y = new AnalyzeNode(AnalyzeNode.NONTERMINAL, "Y", null);
		Z = new AnalyzeNode(AnalyzeNode.NONTERMINAL, "Z", null);
		Z1 = new AnalyzeNode(AnalyzeNode.NONTERMINAL, "Z'", null);
		U = new AnalyzeNode(AnalyzeNode.NONTERMINAL, "U", null);
		U1 = new AnalyzeNode(AnalyzeNode.NONTERMINAL, "U'", null);
		E = new AnalyzeNode(AnalyzeNode.NONTERMINAL, "E", null);
		E1 = new AnalyzeNode(AnalyzeNode.NONTERMINAL, "E'", null);
		H = new AnalyzeNode(AnalyzeNode.NONTERMINAL, "H", null);
		H1 = new AnalyzeNode(AnalyzeNode.NONTERMINAL, "H'", null);
		G = new AnalyzeNode(AnalyzeNode.NONTERMINAL, "G", null);
		F = new AnalyzeNode(AnalyzeNode.NONTERMINAL, "F", null);
		D = new AnalyzeNode(AnalyzeNode.NONTERMINAL, "D", null);
		L = new AnalyzeNode(AnalyzeNode.NONTERMINAL, "L", null);
		L1 = new AnalyzeNode(AnalyzeNode.NONTERMINAL, "L'", null);
		T = new AnalyzeNode(AnalyzeNode.NONTERMINAL, "T", null);
		T1 = new AnalyzeNode(AnalyzeNode.NONTERMINAL, "T'", null);
		O = new AnalyzeNode(AnalyzeNode.NONTERMINAL, "O", null);
		P = new AnalyzeNode(AnalyzeNode.NONTERMINAL, "P", null);
		Q = new AnalyzeNode(AnalyzeNode.NONTERMINAL, "Q", null);
		R = new AnalyzeNode(AnalyzeNode.NONTERMINAL, "R", null);
		ADD_SUB = new AnalyzeNode(AnalyzeNode.ACTIONSIGN, "@ADD_SUB", null);
		ADD = new AnalyzeNode(AnalyzeNode.ACTIONSIGN, "@ADD", null);
		SUB = new AnalyzeNode(AnalyzeNode.ACTIONSIGN, "@SUB", null);
		DIV_MUL = new AnalyzeNode(AnalyzeNode.ACTIONSIGN, "@DIV_MUL", null);
		DIV = new AnalyzeNode(AnalyzeNode.ACTIONSIGN, "@DIV", null);
		MUL = new AnalyzeNode(AnalyzeNode.ACTIONSIGN, "@MUL", null);
		ASS_F = new AnalyzeNode(AnalyzeNode.ACTIONSIGN, "@ASS_F", null);
		ASS_R = new AnalyzeNode(AnalyzeNode.ACTIONSIGN, "@ASS_R", null);
		ASS_Q = new AnalyzeNode(AnalyzeNode.ACTIONSIGN, "@ASS_Q", null);
		ASS_U = new AnalyzeNode(AnalyzeNode.ACTIONSIGN, "@ASS_U", null);
		TRAN_LF = new AnalyzeNode(AnalyzeNode.ACTIONSIGN, "@TRAN_LF", null);
		SINGLE = new AnalyzeNode(AnalyzeNode.ACTIONSIGN, "@SINGLE", null);
		SINGLE_OP = new AnalyzeNode(AnalyzeNode.ACTIONSIGN, "@SINGLE_OP", null);
		EQ = new AnalyzeNode(AnalyzeNode.ACTIONSIGN, "@EQ", null);
		EQ_U1 = new AnalyzeNode(AnalyzeNode.ACTIONSIGN, "@EQ_U'", null);
		COMPARE = new AnalyzeNode(AnalyzeNode.ACTIONSIGN, "@COMPARE", null);
		COMPARE_OP = new AnalyzeNode(AnalyzeNode.ACTIONSIGN, "@COMPARE_OP", null);
		IF_FJ = new AnalyzeNode(AnalyzeNode.ACTIONSIGN, "@IF_FJ", null);
		IF_RJ = new AnalyzeNode(AnalyzeNode.ACTIONSIGN, "@IF_RJ", null);
		IF_BACKPATCH_FJ = new AnalyzeNode(AnalyzeNode.ACTIONSIGN, "@IF_BACKPATCH_FJ", null);
		IF_BACKPATCH_RJ = new AnalyzeNode(AnalyzeNode.ACTIONSIGN, "@IF_BACKPATCH_RJ", null);
		WHILE_FJ = new AnalyzeNode(AnalyzeNode.ACTIONSIGN, "@WHILE_FJ", null);
		WHILE_RJ = new AnalyzeNode(AnalyzeNode.ACTIONSIGN, "@WHILE_RJ", null);
		WHILE_BACKPATCH_FJ = new AnalyzeNode(AnalyzeNode.ACTIONSIGN, "@WHILE_BACKPATCH_FJ", null);
		FOR_FJ = new AnalyzeNode(AnalyzeNode.ACTIONSIGN, "@FOR_FJ", null);
		FOR_RJ = new AnalyzeNode(AnalyzeNode.ACTIONSIGN, "@FOR_RJ", null);
		FOR_BACKPATCH_FJ = new AnalyzeNode(AnalyzeNode.ACTIONSIGN, "@FOR_BACKPATCH_FJ", null);
		if_fj = new Stack<Integer>();
		if_rj = new Stack<Integer>();
		while_fj = new Stack<Integer>();
		while_rj = new Stack<Integer>();
		for_fj = new Stack<Integer>();
		for_rj = new Stack<Integer>();

	}

	/**
	 * LL1分析方法进行语法分析
	 */
	public void grammerAnalyse() {
		if (lexAnalyse.isFail())
			javax.swing.JOptionPane.showMessageDialog(null, "词法分析未通过，不能进行语法分析");
		// 开始语法分析
		bf = new StringBuffer();
		int gcount = 0;
		error = null;
		// S入栈
		analyseStack.add(0, S);
		analyseStack.add(1, new AnalyzeNode(AnalyzeNode.END, "#", null));
		semanticStack.add("#");
		
		while (!analyseStack.empty() && !wordList.isEmpty()) {
			bf.append("\r\n");
			bf.append("步骤" + gcount + "\t");
			if (gcount++ > 10000) {
				graErrorFlag = true;
				break;
			}
			
			top = analyseStack.get(0);// 当前栈顶元素
			firstWord = wordList.get(0);// 待分析单词
			if (firstWord.getValue().equals("#")// 正常结束
					&& top.name.equals("#")) {
				bf.append("\r\n");
				analyseStack.remove(0);
				wordList.remove(0);
			} else if (top.name.equals("#")) {
				analyseStack.remove(0);
				graErrorFlag = true;
				break;
			} else if (AnalyzeNode.isTerm(top)) {// 终结符时的处理
				termOP(top.name);
			} else if (AnalyzeNode.isNonterm(top)) {//非终结符时的处理
				nonTermOP(top.name);
			} else if (top.type.equals(AnalyzeNode.ACTIONSIGN)) {// 栈顶是动作符号时的处理
				actionSignOP();
			}

			//打印当前步骤信息
			bf.append("当前分析栈:");
			for (int i = 0; i < analyseStack.size(); i++) {
				bf.append(analyseStack.get(i).name);
			}
			bf.append("\t").append("余留符号串：");
			for (int j = 0; j < wordList.size(); j++) {
				bf.append(wordList.get(j).getValue());
			}
			bf.append("\t").append("语义栈:");
			for (int k = semanticStack.size() - 1; k >= 0; k--) {
				bf.append(semanticStack.get(k));
			}
		}
	}

	/**
	 * 当前单词是终结符
	 * @param term
	 */
	private void termOP(String term) {
		//如果是单词类型标记为“整型常量”“字符常量”“标识符”或节点名就等于单词名
		if (firstWord.getType().equals(Word.INT_CONST) || firstWord.getType().equals(Word.CHAR_CONST)
				|| term.equals(firstWord.getValue())
				|| (term.equals("id") && firstWord.getType().equals(Word.IDENTIFIER))) {
			analyseStack.remove(0);
			wordList.remove(0);
		} else {
			errorCount++;
			analyseStack.remove(0);
			wordList.remove(0);
			error = new Error(errorCount, "语法错误", firstWord.line, firstWord);
			errorList.add(error);
			graErrorFlag = true;
		}

	}

	/**
	 * 当前单词是非终结符
	 * @param nonTerm
	 */
	private void nonTermOP(String nonTerm) {
		if (nonTerm.equals("Z'"))
			nonTerm = "1";
		if (nonTerm.equals("U'"))
			nonTerm = "2";
		if (nonTerm.equals("E'"))
			nonTerm = "3";
		if (nonTerm.equals("H'"))
			nonTerm = "4";
		if (nonTerm.equals("L'"))
			nonTerm = "5";
		if (nonTerm.equals("T'"))
			nonTerm = "6";
		switch (nonTerm.charAt(0)) {// 栈顶为非终结符的处理
		// N:S,B,A,C,,X,R,Z,Z’,U,U’,E,E’,H,H’,G,M,D,L,L’,T,T’,F,O,P,Q
		case 'S':
			if (firstWord.getValue().equals("void")) {
				analyseStack.remove(0);
				analyseStack.add(0, new AnalyzeNode(AnalyzeNode.TERMINAL, "void", null));
				analyseStack.add(1, new AnalyzeNode(AnalyzeNode.TERMINAL, "main", null));
				analyseStack.add(2, new AnalyzeNode(AnalyzeNode.TERMINAL, "(", null));
				analyseStack.add(3, new AnalyzeNode(AnalyzeNode.TERMINAL, ")", null));
				analyseStack.add(4, new AnalyzeNode(AnalyzeNode.TERMINAL, "{", null));
				analyseStack.add(5, A);
				analyseStack.add(6, new AnalyzeNode(AnalyzeNode.TERMINAL, "}", null));
			} else {
				errorCount++;
				analyseStack.remove(0);
				wordList.remove(0);
				error = new Error(errorCount, "主函数没有返回值", firstWord.getLine(), firstWord);
				errorList.add(error);
				graErrorFlag = true;
			}
			break;

		case 'A':
			if (firstWord.getValue().equals("int") || firstWord.getValue().equals("char")
					|| firstWord.getValue().equals("bool")) {
				analyseStack.remove(0);
				analyseStack.add(0, C);
				analyseStack.add(1, A);
			} else if (firstWord.getValue().equals("printf")) {
				analyseStack.remove(0);
				analyseStack.add(0, C);
				analyseStack.add(1, A);
			} else if (firstWord.getValue().equals("scanf")) {
				analyseStack.remove(0);
				analyseStack.add(0, C);
				analyseStack.add(1, A);
			} else if (firstWord.getValue().equals("if")) {
				analyseStack.remove(0);
				analyseStack.add(0, C);
				analyseStack.add(1, A);
			} else if (firstWord.getValue().equals("while")) {
				analyseStack.remove(0);
				analyseStack.add(0, C);
				analyseStack.add(1, A);
			} else if (firstWord.getValue().equals("for")) {
				analyseStack.remove(0);
				analyseStack.add(0, C);
				analyseStack.add(1, A);
			} else if (firstWord.getType().equals(Word.IDENTIFIER)) {
				analyseStack.remove(0);
				analyseStack.add(0, C);
				analyseStack.add(1, A);
			} else {
				analyseStack.remove(0);
			}
			break;

		case 'B':
			if (firstWord.getValue().equals("printf")) {
				analyseStack.remove(0);
				analyseStack.add(0, new AnalyzeNode(AnalyzeNode.TERMINAL, "printf", null));
				analyseStack.add(1, new AnalyzeNode(AnalyzeNode.TERMINAL, "(", null));
				analyseStack.add(2, P);
				analyseStack.add(3, new AnalyzeNode(AnalyzeNode.TERMINAL, ")", null));
				analyseStack.add(4, A);
				analyseStack.add(5, new AnalyzeNode(AnalyzeNode.TERMINAL, ";", null));

			} else if (firstWord.getValue().equals("scanf")) {
				analyseStack.remove(0);
				analyseStack.add(0, new AnalyzeNode(AnalyzeNode.TERMINAL, "scanf", null));
				analyseStack.add(1, new AnalyzeNode(AnalyzeNode.TERMINAL, "(", null));
				analyseStack.add(2, new AnalyzeNode(AnalyzeNode.TERMINAL, "id", null));
				analyseStack.add(3, new AnalyzeNode(AnalyzeNode.TERMINAL, ")", null));
				analyseStack.add(4, A);
				analyseStack.add(5, new AnalyzeNode(AnalyzeNode.TERMINAL, ";", null));
			} else if (firstWord.getValue().equals("if")) {
				analyseStack.remove(0);
				analyseStack.add(0, new AnalyzeNode(AnalyzeNode.TERMINAL, "if", null));
				analyseStack.add(1, new AnalyzeNode(AnalyzeNode.TERMINAL, "(", null));
				analyseStack.add(2, G);
				analyseStack.add(3, new AnalyzeNode(AnalyzeNode.TERMINAL, ")", null));
				analyseStack.add(4, IF_FJ);
				analyseStack.add(5, new AnalyzeNode(AnalyzeNode.TERMINAL, "{", null));
				analyseStack.add(6, A);
				analyseStack.add(7, new AnalyzeNode(AnalyzeNode.TERMINAL, "}", null));
				analyseStack.add(8, IF_BACKPATCH_FJ);
				analyseStack.add(9, IF_RJ);
				analyseStack.add(10, new AnalyzeNode(AnalyzeNode.TERMINAL, "else", null));
				analyseStack.add(11, new AnalyzeNode(AnalyzeNode.TERMINAL, "{", null));
				analyseStack.add(12, A);
				analyseStack.add(13, new AnalyzeNode(AnalyzeNode.TERMINAL, "}", null));
				analyseStack.add(14, IF_BACKPATCH_RJ);
			} else if (firstWord.getValue().equals("while")) {
				analyseStack.remove(0);
				analyseStack.add(0, new AnalyzeNode(AnalyzeNode.TERMINAL, "while", null));
				analyseStack.add(1, new AnalyzeNode(AnalyzeNode.TERMINAL, "(", null));
				analyseStack.add(2, G);
				analyseStack.add(3, new AnalyzeNode(AnalyzeNode.TERMINAL, ")", null));
				analyseStack.add(4, WHILE_FJ);
				analyseStack.add(5, new AnalyzeNode(AnalyzeNode.TERMINAL, "{", null));
				analyseStack.add(6, A);
				analyseStack.add(7, new AnalyzeNode(AnalyzeNode.TERMINAL, "}", null));
				analyseStack.add(8, WHILE_RJ);
				analyseStack.add(9, WHILE_BACKPATCH_FJ);
			} else if (firstWord.getValue().equals("for")) {
				analyseStack.remove(0);
				analyseStack.add(0, new AnalyzeNode(AnalyzeNode.TERMINAL, "for", null));
				analyseStack.add(1, new AnalyzeNode(AnalyzeNode.TERMINAL, "(", null));
				analyseStack.add(2, Y);
				analyseStack.add(3, Z);
				analyseStack.add(4, new AnalyzeNode(AnalyzeNode.TERMINAL, ";", null));
				analyseStack.add(5, G);
				analyseStack.add(6, FOR_FJ);
				analyseStack.add(7, new AnalyzeNode(AnalyzeNode.TERMINAL, ";", null));
				analyseStack.add(8, Q);
				analyseStack.add(9, new AnalyzeNode(AnalyzeNode.TERMINAL, ")", null));
				analyseStack.add(10, new AnalyzeNode(AnalyzeNode.TERMINAL, "{", null));
				analyseStack.add(11, A);
				analyseStack.add(12, SINGLE);
				analyseStack.add(13, new AnalyzeNode(AnalyzeNode.TERMINAL, "}", null));
				analyseStack.add(14, FOR_RJ);
				analyseStack.add(15, FOR_BACKPATCH_FJ);
			} else {
				analyseStack.remove(0);
			}
			break;

		case 'C':
			analyseStack.remove(0);
			analyseStack.add(0, X);
			analyseStack.add(1, B);
			analyseStack.add(2, R);
			break;

		case 'X':
			if (firstWord.getValue().equals("int") || firstWord.getValue().equals("char")
					|| firstWord.getValue().equals("bool")) {
				analyseStack.remove(0);
				analyseStack.add(0, Y);
				analyseStack.add(1, Z);
				analyseStack.add(2, new AnalyzeNode(AnalyzeNode.TERMINAL, ";", null));
			} else {
				analyseStack.remove(0);
			}
			break;

		case 'Y':
			if (firstWord.getValue().equals("int")) {
				analyseStack.remove(0);
				analyseStack.add(0, new AnalyzeNode(AnalyzeNode.TERMINAL, "int", null));
			} else if (firstWord.getValue().equals("char")) {
				analyseStack.remove(0);
				analyseStack.add(0, new AnalyzeNode(AnalyzeNode.TERMINAL, "char", null));
			} else if (firstWord.getValue().equals("bool")) {
				analyseStack.remove(0);
				analyseStack.add(0, new AnalyzeNode(AnalyzeNode.TERMINAL, "bool", null));
			} else {
				errorCount++;
				analyseStack.remove(0);
				wordList.remove(0);
				error = new Error(errorCount, "非法数据类型", firstWord.line, firstWord);
				errorList.add(error);
				graErrorFlag = true;
			}
			break;

		case 'Z':
			if (firstWord.getType().equals(Word.IDENTIFIER)) {
				analyseStack.remove(0);
				analyseStack.add(0, U);
				analyseStack.add(1, Z1);
			} else {
				errorCount++;
				analyseStack.remove(0);
				wordList.remove(0);
				error = new Error(errorCount, "非法标识符", firstWord.line, firstWord);
				errorList.add(error);
				graErrorFlag = true;
			}
			break;

		case '1':// z'
			if (firstWord.getValue().equals(",")) {
				analyseStack.remove(0);
				analyseStack.add(0, new AnalyzeNode(AnalyzeNode.TERMINAL, ",", null));
				analyseStack.add(1, Z);
			} else {
				analyseStack.remove(0);
			}
			break;

		case 'U':
			if (firstWord.getType().equals(Word.IDENTIFIER)) {
				analyseStack.remove(0);
				analyseStack.add(0, ASS_U);
				analyseStack.add(1, new AnalyzeNode(AnalyzeNode.TERMINAL, "id", null));
				analyseStack.add(2, U1);
			} else {
				errorCount++;
				analyseStack.remove(0);
				wordList.remove(0);
				error = new Error(errorCount, "非法标识符", firstWord.line, firstWord);
				errorList.add(error);
				graErrorFlag = true;
			}
			break;

		case '2':// U'
			if (firstWord.getValue().equals("=")) {
				analyseStack.remove(0);
				analyseStack.add(0, new AnalyzeNode(AnalyzeNode.TERMINAL, "=", null));
				analyseStack.add(1, L);
				analyseStack.add(2, EQ_U1);
			} else {
				analyseStack.remove(0);
			}
			break;

		case 'R':
			if (firstWord.getType().equals(Word.IDENTIFIER)) {
				analyseStack.remove(0);
				analyseStack.add(0, new AnalyzeNode(AnalyzeNode.ACTIONSIGN, "@ASS_R", null));
				analyseStack.add(1, new AnalyzeNode(AnalyzeNode.TERMINAL, "id", null));
				analyseStack.add(2, new AnalyzeNode(AnalyzeNode.TERMINAL, "=", null));
				analyseStack.add(3, L);
				analyseStack.add(4, EQ);
				analyseStack.add(5, new AnalyzeNode(AnalyzeNode.TERMINAL, ";", null));
			} else {
				analyseStack.remove(0);
			}
			break;

		case 'P':
			if (firstWord.getType().equals(Word.IDENTIFIER)) {
				analyseStack.remove(0);
				analyseStack.add(0, new AnalyzeNode(AnalyzeNode.TERMINAL, "id", null));
			} else if (firstWord.getType().equals(Word.INT_CONST)) {
				analyseStack.remove(0);
				analyseStack.add(0, new AnalyzeNode(AnalyzeNode.TERMINAL, "num", null));
			} else if (firstWord.getType().equals(Word.CHAR_CONST)) {
				analyseStack.remove(0);
				analyseStack.add(0, new AnalyzeNode(AnalyzeNode.TERMINAL, "ch", null));
			} else {
				errorCount++;
				analyseStack.remove(0);
				wordList.remove(0);
				error = new Error(errorCount, "不能输出的数据类型", firstWord.line, firstWord);
				errorList.add(error);
				graErrorFlag = true;
			}
			break;

		case 'E':
			if (firstWord.getType().equals(Word.IDENTIFIER)) {
				analyseStack.remove(0);
				analyseStack.add(0, H);
				analyseStack.add(1, E1);
			} else if (firstWord.getType().equals(Word.INT_CONST)) {
				analyseStack.remove(0);
				analyseStack.add(0, H);
				analyseStack.add(1, E1);
			} else if (firstWord.getValue().equals("(")) {
				analyseStack.remove(0);
				analyseStack.add(0, H);
				analyseStack.add(1, E1);
			} else {
				errorCount++;
				analyseStack.remove(0);
				wordList.remove(0);
				error = new Error(errorCount, "不能进行算术运算的数据类型", firstWord.line, firstWord);
				errorList.add(error);
				graErrorFlag = true;
			}
			break;

		case '3':// E'
			if (firstWord.getValue().equals("&&")) {
				analyseStack.remove(0);
				analyseStack.add(0, new AnalyzeNode(AnalyzeNode.TERMINAL, "&&", null));
				analyseStack.add(1, E);
			} else {
				analyseStack.remove(0);
			}
			break;

		case 'H':
			if (firstWord.getType().equals(Word.IDENTIFIER)) {
				analyseStack.remove(0);
				analyseStack.add(0, G);
				analyseStack.add(1, H1);
			} else if (firstWord.getType().equals(Word.INT_CONST)) {
				analyseStack.remove(0);
				analyseStack.add(0, G);
				analyseStack.add(1, H1);
			} else if (firstWord.getValue().equals("(")) {
				analyseStack.remove(0);
				analyseStack.add(0, G);
				analyseStack.add(1, H1);
			} else {
				errorCount++;
				analyseStack.remove(0);
				wordList.remove(0);
				error = new Error(errorCount, "不能进行算术运算的数据类型", firstWord.line, firstWord);
				errorList.add(error);
				graErrorFlag = true;
			}
			break;

		case '4':// H'
			if (firstWord.getValue().equals("||")) {
				analyseStack.remove(0);
				analyseStack.add(0, new AnalyzeNode(AnalyzeNode.TERMINAL, "||", null));
				analyseStack.add(1, E);
			} else {
				analyseStack.remove(0);
			}
			break;

		case 'D':
			if (firstWord.getValue().equals("==")) {
				analyseStack.remove(0);
				analyseStack.add(0, COMPARE_OP);
				analyseStack.add(1, new AnalyzeNode(AnalyzeNode.TERMINAL, "==", null));
			} else if (firstWord.getValue().equals("!=")) {
				analyseStack.remove(0);
				analyseStack.add(0, COMPARE_OP);
				analyseStack.add(1, new AnalyzeNode(AnalyzeNode.TERMINAL, "!=", null));

			} else if (firstWord.getValue().equals(">")) {
				analyseStack.remove(0);
				analyseStack.add(0, COMPARE_OP);
				analyseStack.add(1, new AnalyzeNode(AnalyzeNode.TERMINAL, ">", null));
			} else if (firstWord.getValue().equals("<")) {
				analyseStack.remove(0);
				analyseStack.add(0, COMPARE_OP);
				analyseStack.add(1, new AnalyzeNode(AnalyzeNode.TERMINAL, "<", null));
			} else {
				errorCount++;
				analyseStack.remove(0);
				wordList.remove(0);
				error = new Error(errorCount, "非法运算符", firstWord.line, firstWord);
				errorList.add(error);
				graErrorFlag = true;
			}
			break;

		case 'G':
			if (firstWord.getType().equals(Word.IDENTIFIER)) {
				analyseStack.remove(0);
				analyseStack.add(0, F);
				analyseStack.add(1, D);
				analyseStack.add(2, F);
				analyseStack.add(3, COMPARE);
			} else if (firstWord.getType().equals(Word.INT_CONST)) {
				analyseStack.remove(0);
				analyseStack.add(0, F);
				analyseStack.add(1, D);
				analyseStack.add(2, F);
				analyseStack.add(3, COMPARE);
			} else if (firstWord.getValue().equals("(")) {
				analyseStack.remove(0);
				analyseStack.add(0, new AnalyzeNode(AnalyzeNode.TERMINAL, "(", null));
				analyseStack.add(1, E);
				analyseStack.add(2, new AnalyzeNode(AnalyzeNode.TERMINAL, ")", null));
			} else if (firstWord.getValue().equals("!")) {
				analyseStack.remove(0);
				analyseStack.add(0, new AnalyzeNode(AnalyzeNode.TERMINAL, "!", null));
				analyseStack.add(1, E);
			} else {
				errorCount++;
				analyseStack.remove(0);
				wordList.remove(0);
				error = new Error(errorCount, "不能进行算术运算的数据类型或括号不匹配", firstWord.line, firstWord);
				errorList.add(error);
				graErrorFlag = true;
			}
			break;

		case 'L':
			if (firstWord.getType().equals(Word.IDENTIFIER)) {
				analyseStack.remove(0);
				analyseStack.add(0, T);
				analyseStack.add(1, L1);
				analyseStack.add(2, ADD_SUB);
			} else if (firstWord.getType().equals(Word.INT_CONST)) {
				analyseStack.remove(0);
				analyseStack.add(0, T);
				analyseStack.add(1, L1);
				analyseStack.add(2, ADD_SUB);
			} else if (firstWord.getValue().equals("(")) {
				analyseStack.remove(0);
				analyseStack.add(0, T);
				analyseStack.add(1, L1);
				analyseStack.add(2, ADD_SUB);
			} else {
				errorCount++;
				analyseStack.remove(0);
				wordList.remove(0);
				error = new Error(errorCount, "不能进行算术运算的数据类型或括号不匹配", firstWord.line, firstWord);
				errorList.add(error);
				graErrorFlag = true;
			}
			break;

		case '5':// l'
			if (firstWord.getValue().equals("+")) {
				analyseStack.remove(0);
				analyseStack.add(0, new AnalyzeNode(AnalyzeNode.TERMINAL, "+", null));
				analyseStack.add(1, L);
				analyseStack.add(2, ADD);
			} else if (firstWord.getValue().equals("-")) {
				analyseStack.remove(0);
				analyseStack.add(0, new AnalyzeNode(AnalyzeNode.TERMINAL, "-", null));
				analyseStack.add(1, L);
				analyseStack.add(2, SUB);
			} else {
				analyseStack.remove(0);
			}
			break;

		case 'T':
			if (firstWord.getType().equals(Word.IDENTIFIER)) {
				analyseStack.remove(0);
				analyseStack.add(0, F);
				analyseStack.add(1, T1);
				analyseStack.add(2, DIV_MUL);
			} else if (firstWord.getType().equals(Word.INT_CONST)) {
				analyseStack.remove(0);
				analyseStack.add(0, F);
				analyseStack.add(1, T1);
				analyseStack.add(2, DIV_MUL);
			} else if (firstWord.getValue().equals("(")) {
				analyseStack.remove(0);
				analyseStack.add(0, F);
				analyseStack.add(1, T1);
				analyseStack.add(2, DIV_MUL);
			} else {
				errorCount++;
				analyseStack.remove(0);
				wordList.remove(0);
				error = new Error(errorCount, "不能进行算术运算的数据类型", firstWord.line, firstWord);
				errorList.add(error);
				graErrorFlag = true;
			}
			break;

		case '6':// T'
			if (firstWord.getValue().equals("*")) {
				analyseStack.remove(0);
				analyseStack.add(0, new AnalyzeNode(AnalyzeNode.TERMINAL, "*", null));
				analyseStack.add(1, T);
				analyseStack.add(2, MUL);
			} else if (firstWord.getValue().equals("/")) {
				analyseStack.remove(0);
				analyseStack.add(0, new AnalyzeNode(AnalyzeNode.TERMINAL, "/", null));
				analyseStack.add(1, T);
				analyseStack.add(2, DIV);
			} else {
				analyseStack.remove(0);
			}
			break;

		case 'F':
			if (firstWord.getType().equals(Word.IDENTIFIER)) {
				analyseStack.remove(0);
				analyseStack.add(0, ASS_F);
				analyseStack.add(1, new AnalyzeNode(AnalyzeNode.TERMINAL, "id", null));
			} else if (firstWord.getType().equals(Word.INT_CONST)) {
				analyseStack.remove(0);
				analyseStack.add(0, ASS_F);
				analyseStack.add(1, new AnalyzeNode(AnalyzeNode.TERMINAL, "num", null));
			} else if (firstWord.getValue().equals("(")) {
				analyseStack.add(0, new AnalyzeNode(AnalyzeNode.TERMINAL, "(", null));
				analyseStack.add(1, L);
				analyseStack.add(2, new AnalyzeNode(AnalyzeNode.TERMINAL, ")", null));
				analyseStack.add(3, TRAN_LF);
			} else {
				analyseStack.remove(0);
			}
			break;

		case 'O':
			if (firstWord.getValue().equals("++")) {
				analyseStack.remove(0);
				analyseStack.add(0, new AnalyzeNode(AnalyzeNode.ACTIONSIGN, "@SINGLE_OP", null));
				analyseStack.add(1, new AnalyzeNode(AnalyzeNode.TERMINAL, "++", null));
			} else if (firstWord.getValue().equals("--")) {
				analyseStack.remove(0);
				analyseStack.add(0, new AnalyzeNode(AnalyzeNode.ACTIONSIGN, "@SINGLE_OP", null));
				analyseStack.add(1, new AnalyzeNode(AnalyzeNode.TERMINAL, "--", null));
			} else {
				analyseStack.remove(0);
			}
			break;

		case 'Q':// Q
			if (firstWord.getType().equals(Word.IDENTIFIER)) {
				analyseStack.remove(0);
				analyseStack.add(0, new AnalyzeNode(AnalyzeNode.ACTIONSIGN, "@ASS_Q", null));
				analyseStack.add(1, new AnalyzeNode(AnalyzeNode.TERMINAL, "id", null));
				analyseStack.add(2, new AnalyzeNode(AnalyzeNode.TERMINAL, "O", null));
			} else {
				analyseStack.remove(0);
			}
			break;

		}
	}

	/**
	 * 对动作符号的处理
	 */
	private void actionSignOP() {
		if (top.name.equals("@ADD_SUB")) {
			if (OP != null && (OP.equals("+") || OP.equals("-"))) {
				ARG2 = semanticStack.pop();
				ARG1 = semanticStack.pop();
				RES = newTemp();
				FourElement fourElem = new FourElement(++fourElemCount, OP, ARG1, ARG2, RES);
				fourElemList.add(fourElem);
				L.value = RES;
				semanticStack.push(L.value);
				OP = null;
			}
			analyseStack.remove(0);

		} else if (top.name.equals("@ADD")) {
			OP = "+";
			analyseStack.remove(0);
		} else if (top.name.equals("@SUB")) {
			OP = "-";
			analyseStack.remove(0);
		} else if (top.name.equals("@DIV_MUL")) {
			if (OP != null && (OP.equals("*") || OP.equals("/"))) {
				ARG2 = semanticStack.pop();
				ARG1 = semanticStack.pop();
				RES = newTemp();
				FourElement fourElem = new FourElement(++fourElemCount, OP, ARG1, ARG2, RES);
				fourElemList.add(fourElem);
				T.value = RES;
				semanticStack.push(T.value);
				OP = null;
			}
			analyseStack.remove(0);
		} else if (top.name.equals("@DIV")) {
			OP = "/";
			analyseStack.remove(0);
		} else if (top.name.equals("@MUL")) {
			OP = "*";
			analyseStack.remove(0);
		} else if (top.name.equals("@TRAN_LF")) {
			F.value = L.value;
			analyseStack.remove(0);
		} else if (top.name.equals("@ASS_F")) {
			F.value = firstWord.getValue();
			semanticStack.push(F.value);
			analyseStack.remove(0);
		} else if (top.name.equals("@ASS_R")) {
			R.value = firstWord.getValue();
			semanticStack.push(R.value);
			analyseStack.remove(0);
		} else if (top.name.equals("@ASS_Q")) {
			Q.value = firstWord.getValue();
			semanticStack.push(Q.value);
			analyseStack.remove(0);
		} else if (top.name.equals("@ASS_U")) {
			U.value = firstWord.getValue();
			semanticStack.push(U.value);
			analyseStack.remove(0);
		} else if (top.name.equals("@SINGLE")) {
			if (for_op.peek() != null) {
				ARG1 = semanticStack.pop();
				RES = ARG1;
				FourElement fourElem = new FourElement(++fourElemCount, for_op.pop(), ARG1, "/", RES);
				fourElemList.add(fourElem);
			}
			analyseStack.remove(0);
		} else if (top.name.equals("@SINGLE_OP")) {
			for_op.push(firstWord.getValue());
			analyseStack.remove(0);
		} else if (top.name.equals("@EQ")) {
			OP = "=";
			ARG1 = semanticStack.pop();
			RES = semanticStack.pop();
			FourElement fourElem = new FourElement(++fourElemCount, OP, ARG1, "/", RES);
			fourElemList.add(fourElem);
			OP = null;
			analyseStack.remove(0);
		} else if (top.name.equals("@EQ_U'")) {
			OP = "=";
			ARG1 = semanticStack.pop();
			RES = semanticStack.pop();
			FourElement fourElem = new FourElement(++fourElemCount, OP, ARG1, "/", RES);
			fourElemList.add(fourElem);
			OP = null;
			analyseStack.remove(0);
		} else if (top.name.equals("@COMPARE")) {
			ARG2 = semanticStack.pop();
			OP = semanticStack.pop();
			ARG1 = semanticStack.pop();
			RES = newTemp();
			FourElement fourElem = new FourElement(++fourElemCount, OP, ARG1, ARG2, RES);
			fourElemList.add(fourElem);
			G.value = RES;
			semanticStack.push(G.value);
			OP = null;
			analyseStack.remove(0);
		} else if (top.name.equals("@COMPARE_OP")) {
			D.value = firstWord.getValue();
			semanticStack.push(D.value);
			analyseStack.remove(0);
		} else if (top.name.equals("@IF_FJ")) {
			OP = "FJ";
			ARG1 = semanticStack.pop();
			FourElement fourElem = new FourElement(++fourElemCount, OP, ARG1, "/", RES);
			if_fj.push(fourElemCount);
			fourElemList.add(fourElem);
			OP = null;
			analyseStack.remove(0);
		} else if (top.name.equals("@IF_BACKPATCH_FJ")) {
			backpatch(if_fj.pop(), fourElemCount + 2);
			analyseStack.remove(0);
		} else if (top.name.equals("@IF_RJ")) {
			OP = "RJ";
			FourElement fourElem = new FourElement(++fourElemCount, OP, "/", "/", "/");
			if_rj.push(fourElemCount);
			fourElemList.add(fourElem);
			OP = null;
			analyseStack.remove(0);
		} else if (top.name.equals("@IF_BACKPATCH_RJ")) {
			backpatch(if_rj.pop(), fourElemCount + 1);
			analyseStack.remove(0);
		} else if (top.name.equals("@WHILE_FJ")) {
			OP = "FJ";
			ARG1 = semanticStack.pop();
			FourElement fourElem = new FourElement(++fourElemCount, OP, ARG1, "/", "/");
			while_fj.push(fourElemCount);
			fourElemList.add(fourElem);
			OP = null;
			analyseStack.remove(0);
		} else if (top.name.equals("@WHILE_RJ")) {
			OP = "RJ";
			RES = (while_fj.peek() - 1) + "";
			FourElement fourElem = new FourElement(++fourElemCount, OP, "/", "/", RES);
			for_rj.push(fourElemCount);
			fourElemList.add(fourElem);
			OP = null;
			analyseStack.remove(0);
		} else if (top.name.equals("@WHILE_BACKPATCH_FJ")) {
			backpatch(while_fj.pop(), fourElemCount + 1);
			analyseStack.remove(0);
		} else if (top.name.equals("@FOR_FJ")) {
			OP = "FJ";
			ARG1 = semanticStack.pop();
			FourElement fourElem = new FourElement(++fourElemCount, OP, ARG1, "/", "/");
			for_fj.push(fourElemCount);
			fourElemList.add(fourElem);
			OP = null;
			analyseStack.remove(0);
		} else if (top.name.equals("@FOR_RJ")) {
			OP = "RJ";
			RES = (for_fj.peek() - 1) + "";
			FourElement fourElem = new FourElement(++fourElemCount, OP, "/", "/", RES);
			for_rj.push(fourElemCount);
			fourElemList.add(fourElem);
			OP = null;
			analyseStack.remove(0);
		} else if (top.name.equals("@FOR_BACKPATCH_FJ")) {
			backpatch(for_fj.pop(), fourElemCount + 1);
			analyseStack.remove(0);
		}

	}

	/**
	 * 
	 * @param i
	 * @param res
	 */
	private void backpatch(int i, int res) {
		FourElement temp = fourElemList.get(i - 1);
		temp.setResult(res + "");
		fourElemList.set(i - 1, temp);
	}

	/**
	 * 输出LL(1)分析步骤
	 * 
	 * @return
	 * @throws IOException
	 */
	public String outputLL1() throws IOException {
		File file = new File("./output/");
		if (!file.exists()) {
			file.mkdirs();
			file.createNewFile();// 如果这个文件不存在就创建它
		}
		String path = file.getAbsolutePath();
		FileOutputStream fos = new FileOutputStream(path + "/LL1.txt");
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		OutputStreamWriter osw1 = new OutputStreamWriter(bos, "utf-8");
		PrintWriter pw1 = new PrintWriter(osw1);
		pw1.println(bf.toString());
		bf.delete(0, bf.length());
		if (graErrorFlag) {
			Error error;
			pw1.println("错误信息如下：");
			pw1.println("错误序号\t错误信息\t错误所在行 \t错误单词");
			for (int i = 0; i < errorList.size(); i++) {
				error = errorList.get(i);
				pw1.println(error.getId() + "\t" + error.getInfo() + "\t\t" + error.getLine() + "\t"
						+ error.getWord().getValue());
			}
		} else {
			pw1.println("语法分析通过：");
		}
		pw1.close();
		return path + "/LL1.txt";
	}

	/**
	 * 输出中间代码四元式
	 * 
	 * @return
	 * @throws IOException
	 */
	public String outputFourElem() throws IOException {
		File file = new File("./output/");
		if (!file.exists()) {
			file.mkdirs();
			file.createNewFile();// 如果这个文件不存在就创建它
		}
		String path = file.getAbsolutePath();
		FileOutputStream fos = new FileOutputStream(path + "/FourElement.txt");
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		OutputStreamWriter osw1 = new OutputStreamWriter(bos, "utf-8");
		PrintWriter pw1 = new PrintWriter(osw1);
		pw1.println("生成的四元式如下");
		pw1.println("序号（OP,ARG1，ARG2，RESULT）");
		FourElement temp;
		for (int i = 0; i < fourElemList.size(); i++) {
			temp = fourElemList.get(i);
			pw1.println(temp.getId() + "(" + temp.getOp() + "," + temp.getArg1() + "," + temp.getArg2() + ","
					+ temp.getResult() + ")");
		}
		pw1.close();
		return path + "/FourElement.txt";
	}

}
