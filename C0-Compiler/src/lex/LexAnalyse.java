package lex;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * 词法分析程序
 * @author Hu Yuxi
 *
 */
public class LexAnalyse {

	public ArrayList<Word> wordList = new ArrayList<Word>();//单词列表
	ArrayList<Error> errorList = new ArrayList<Error>();//错误列表
	int wordCount = 0;//单词总数
	int errorCount = 0;//错误总数
	boolean noteFlag = false;
	boolean lexErrorFlag = false;
	
	/**
	 * 无参构造函数
	 */
	public LexAnalyse() {

	}

	/**
	 * 有参构造函数
	 * @param str
	 */
	public LexAnalyse(String str) {
		lexAnalyse(str);
	}
	
	/**
	 * 词法分析入口
	 * @param sourceCode 源程序文件名
	 * @throws IOException
	 */
	public void lexBegin(String sourceCode) throws IOException {
		//LexAnalyse lex = new LexAnalyse();
		///lex.lexAnalyse1(sourceCode);
		//lex.outputWordList();
		lexAnalyse1(sourceCode);
		outputWordList();
	}
	
	/**
	 * 判断是否为数字
	 * 
	 * @param ch char
	 * @return isDigit
	 */
	private static boolean isDigit(char ch) {
		boolean isDigit = false;
		if ('0' <= ch && ch <= '9')
			isDigit = true;
		return isDigit;
	}

	/**
	 * 判断是否为整数
	 * 
	 * @param string word
	 * @return
	 */
	private static boolean isInteger(String word) {
		int i;
		boolean isInteger = false;
		for (i = 0; i < word.length(); i++) {
			if (Character.isDigit(word.charAt(i))) {
				continue;
			} else {
				break;
			}
		}
		if (i == word.length()) {
			isInteger= true;
		}
		return isInteger;
	}

	/**
	 * 判断字符串是否为单引号字符串数组
	 * 
	 * @param word word
	 * @return
	 */
	private static boolean isChar(String word) {
		boolean flag = false;
		int i = 0;
		char temp = word.charAt(i);
		
		if (temp == '\'') {
			for (i = 1; i < word.length(); i++) {
				temp = word.charAt(i);
				if (0 <= temp && temp <= 255)
					continue;
				else
					break;
			}
			if (i + 1 == word.length() && word.charAt(i) == '\'')
				flag = true;
		} else
			return flag;

		return flag;
	}

	/**
	 * 判断是否为a-z或A-Z字母
	 * 
	 * @param ch char
	 * @return isLetter
	 */
	private static boolean isLetter(char ch) {
		boolean flag = false;
		if (('a' <= ch && ch <= 'z') || ('A' <= ch && ch <= 'Z'))
			flag = true;
		return flag;
	}

	/**
	 * 判断是否为identifier
	 * 
	 * @param word
	 * @return isID
	 */
	private static boolean isID(String word) {
		boolean isID = false;
		int i = 0;
		if (Word.isKey(word))
			return isID;
		
		char temp = word.charAt(i);
		if (isLetter(temp) || temp == '_') {
			for (i = 1; i < word.length(); i++) {
				temp = word.charAt(i);
				if (isLetter(temp) || temp == '_' || isDigit(temp))
					continue;
				else
					break;
			}
			
			if (i >= word.length())
				isID = true;
		} else
			return isID;

		return isID;
	}

	/**
	 * 词法分析是否失败
	 * 
	 */
	public boolean isFail() {
		return lexErrorFlag;
	}

	/**
	 * 
	 * @param str 
	 * @param line line
	 */
	public void analyse(String str, int line) {
		int beginIndex;//开始索引
		int endIndex;//结束索引
		int index = 0;
		int length = str.length();
		Word word = null;
		Error error;
		char temp;
		
		while (index < length) {
			temp = str.charAt(index);
			if (!noteFlag) {
				if (isLetter(temp) || temp == '_') {
					/**
					 * 开头是字母或下划线
					 */
					beginIndex = index;
					index++;
					// temp=str.charAt(index);
					while ((index < length)
							&& (!Word.isBoundarySign(str.substring(index,index + 1)))
							&& (!Word.isOperator(str.substring(index, index + 1)))
							&& (str.charAt(index) != ' ')
							&& (str.charAt(index) != '\t')
							&& (str.charAt(index) != '\r')
							&& (str.charAt(index) != '\n')) {
						      index++;
						// temp=str.charAt(index);
					}					
					endIndex = index;
					//到此为止分割出了一个单词,赋值入word
					word = new Word();
					wordCount++;
					word.id = wordCount;
					word.line = line;
					word.setValue(str.substring(beginIndex, endIndex));
					//判断word类型
					if (Word.isKey(word.getValue())) {
						word.setType(Word.KEY);
					} else if (isID(word.getValue())) {
						word.setType(Word.IDENTIFIER);
					} else {
						//未知单词
						word.setType(Word.UNIDEF);
						word.flag = false;
						errorCount++;
						error = new Error(errorCount, "errinfo", word.line, word);
						errorList.add(error);
						lexErrorFlag = true;
					}
					index--;
				} else if (isDigit(temp)) {
					/**
					 * 开头是数字
					 */
					beginIndex = index;
					index++;
					// temp=str.charAt(index);
					while ((index < length)
							&& (!Word.isBoundarySign(str.substring(index,
									index + 1)))
							&& (!Word.isOperator(str
									.substring(index, index + 1)))
							&& (str.charAt(index) != ' ')
							&& (str.charAt(index) != '\t')
							&& (str.charAt(index) != '\r')
							&& (str.charAt(index) != '\n')) {
						index++;
						// temp=str.charAt(index);
					}				
					endIndex = index;
					//获得新单词赋值入word
					word = new Word();
					wordCount++;
					word.id = wordCount;
					word.line = line;
					word.setValue(str.substring(beginIndex, endIndex));
					//判断单词类型
					if (isInteger(word.getValue())) {
						word.setType(Word.INT_CONST);
					} else {
						word.setType(Word.UNIDEF);
						word.flag = false;
						errorCount++;
						error = new Error(errorCount, "errorinfo", word.line, word);
						errorList.add(error);
						lexErrorFlag = true;
					}
					index--;
				} else if (String.valueOf(str.charAt(index)).equals("'")) {
					// flag=true;
					beginIndex = index;
					index++;
					temp = str.charAt(index);
					while (index < length && (0 <= temp && temp <= 255)) {
						if (String.valueOf(str.charAt(index)).equals("'"))
							break;
						index++;
						// temp=str.charAt(index);
					}
					if (index < length) {
						endIndex = index;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.setValue(str.substring(beginIndex, endIndex));
						word.setType(Word.CHAR_CONST);
						// flag=true;
						// word.flag=flag;
						index--;
					} else {
						endIndex = index;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.setValue(str.substring(beginIndex, endIndex));
						word.setType(Word.UNIDEF);
						word.flag = false;
						errorCount++;
						error = new Error(errorCount, "erroinfo", word.line, word);
						errorList.add(error);
						lexErrorFlag = true;
						index--;
					}
				} else if (temp == '=') {
					//TODO
					beginIndex = index;
					index++;
					if (index < length && str.charAt(index) == '=') {
						endIndex = index + 1;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.setValue(str.substring(beginIndex, endIndex));
						word.setType(Word.OPERATOR);
					} else {
						// endIndex=index;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.setValue(str.substring(index - 1, index));
						word.setType(Word.OPERATOR);
						index--;
					}
				} else if (temp == '!') {
					/**
					 * 开头是感叹号
					 */
					beginIndex = index;
					index++;
					if (index < length && str.charAt(index) == '=') {
						endIndex = index + 1;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.setValue(str.substring(beginIndex, endIndex));
						word.setType(Word.OPERATOR);
						index++;
					} else {
						// endIndex=index;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.setValue(str.substring(index - 1, index));
						word.setType(Word.OPERATOR);
						index--;
					}
				} else if (temp == '&') {
					beginIndex = index;
					index++;
					if (index < length && str.charAt(index) == '&') {
						endIndex = index + 1;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.setValue(str.substring(beginIndex, endIndex));
						word.setType(Word.OPERATOR);
					} else {
						// endIndex=index;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.setValue(str.substring(index - 1, index));
						word.setType(Word.OPERATOR);
						index--;
					}
				} else if (temp == '|') {
					beginIndex = index;
					index++;
					if (index < length && str.charAt(index) == '|') {
						endIndex = index + 1;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.setValue(str.substring(beginIndex, endIndex));
						word.setType(Word.OPERATOR);
					} else {
						// endIndex=index;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.setValue(str.substring(index - 1, index));
						word.setType(Word.OPERATOR);
						index--;
					}
				} else if (temp == '+') {
					beginIndex = index;
					index++;
					if (index < length && str.charAt(index) == '+') {
						endIndex = index + 1;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.setValue(str.substring(beginIndex, endIndex));
						word.setType(Word.OPERATOR);

					} else {
						// endIndex=index;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.setValue(str.substring(index - 1, index));
						word.setType(Word.OPERATOR);
						index--;
					}
				} else if (temp == '-') {
					beginIndex = index;
					index++;
					if (index < length && str.charAt(index) == '-') {
						endIndex = index + 1;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.setValue(str.substring(beginIndex, endIndex));
						word.setType(Word.OPERATOR);
					} else {
						// endIndex=index;
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.setValue(str.substring(index - 1, index));
						word.setType(Word.OPERATOR);
						index--;
					}
				} else if (temp == '/') {
					index++;
					if (index < length && str.charAt(index) == '/')
						break;
					/*
					 * { index++; while(str.charAt(index)!='\n'){ index++; } }
					 */
					else if (index < length && str.charAt(index) == '*') {
						noteFlag = true;
					} else {
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.setValue(str.substring(index - 1, index));
						word.setType(Word.OPERATOR);
					}
					index--;
				} else {
					switch (temp) {
					case ' ':
					case '\t':
					case '\r':
					case '\n':
						word = null;
						break;
					case '[':
					case ']':
					case '(':
					case ')':
					case '{':
					case '}':
					case ',':
					case '"':
					case '.':
					case ';':
						// case '+':
						// case '-':
					case '*':
						// case '/':
					case '%':
					case '>':
					case '<':
					case '?':
					case '#':
						word = new Word();
						word.id = ++wordCount;
						word.line = line;
						word.setValue(String.valueOf(temp));
						if (Word.isOperator(word.getValue()))
							word.setType(Word.OPERATOR);
						else if (Word.isBoundarySign(word.getValue()))
							word.setType(Word.BOUNDARYSIGN);
						else
							word.setType(Word.END);
						break;
					default:
						word = new Word();
						wordCount++;
						word.id = wordCount;
						word.line = line;
						word.setValue(String.valueOf(temp));
						word.setType(Word.UNIDEF);
						word.flag = false;
						errorCount++;
						error = new Error(errorCount, "Unknown Type", word.line, word);
						errorList.add(error);
						lexErrorFlag = true;
					}
				}
			} else {
				int i = str.indexOf("*/");
				if (i != -1) {
					noteFlag = false;
					index = i + 2;
					continue;
				} else
					break;
			}
			if (word == null) {
				index++;
				continue;
			}

			wordList.add(word);
			index++;
		}
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public ArrayList<Word> lexAnalyse(String str) {
		String buffer[];
		buffer = str.split("\n");
		int line = 1;
		
		for (int i = 0; i < buffer.length; i++) {
			analyse(buffer[i].trim(), line);
			line++;
		}
		
		if (!wordList.get(wordList.size() - 1).getType().equals(Word.END)) {
			Word word = new Word(++wordCount, "#", Word.END, line++);
			wordList.add(word);
		}
		return wordList;
	}

	/**
	 * 
	 * @param filePath filePath
	 * @return wordList
	 * @throws IOException
	 */
	public ArrayList<Word> lexAnalyse1(String filePath) throws IOException {
		FileInputStream fis = new FileInputStream(filePath);
		BufferedInputStream bis = new BufferedInputStream(fis);
		InputStreamReader isr = new InputStreamReader(bis, "utf-8");
		BufferedReader inbr = new BufferedReader(isr);
		String str = "";
		int line = 1;
		while ((str = inbr.readLine()) != null) {
			// System.out.println(str);
			analyse(str.trim(), line);
			line++;
		}
		inbr.close();
		if (!wordList.get(wordList.size() - 1).getType().equals(Word.END)) {
			Word word = new Word(++wordCount, "#", Word.END, line++);
			wordList.add(word);
		}
		return wordList;
	}

	/**
	 * 
	 * @return wordList path
	 * @throws IOException
	 */
	public String outputWordList() throws IOException {
		File file = new File("./output/");
		//文件夹不存在就新建
		if (!file.exists()) {
			file.mkdirs();
			file.createNewFile();
		}
		//获取绝对路径
		String path = file.getAbsolutePath();
		//开一个文件输出流
		FileOutputStream fos = new FileOutputStream(path + "/wordList.txt");
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		OutputStreamWriter osw1 = new OutputStreamWriter(bos, "utf-8");
		PrintWriter pw1 = new PrintWriter(osw1);
		//开始输出!
		pw1.println("序号\t单词\t\t类型\t\t行号\t合法\t");
		Word word;
		for (int i = 0; i < wordList.size(); i++) {
			word = wordList.get(i);
			pw1.println(word.id + "\t" + word.getValue() + "\t\t" + word.getType() + "\t\t" + word.line + "\t" + word.flag);
		}
		//是否有error
		if (lexErrorFlag) {
			Error error;
			pw1.println("Lexical Analyse failed");
			//打印error列表
			for (int i = 0; i < errorList.size(); i++) {
				error = errorList.get(i);
				pw1.println(error.getId() + "\t" + error.getInfo() + "\t\t" + error.getLine()
						+ "\t" + error.getWord().getValue());
			}
		} else {
			pw1.println("Lexical Analyse succeed");
		}
		pw1.close();
		return path + "/wordList.txt";
	}

	/**
	 * @return the wordList
	 */
	public ArrayList<Word> getWordList() {
		return wordList;
	}
	
	

}
