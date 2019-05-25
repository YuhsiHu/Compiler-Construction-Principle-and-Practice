package lex;

public class Error {
	int id;// 错误序号；
	String info;// 错误信息；
	int line;// 错误所在行
	Word word;// 错误的单词

	/**
	 * 无参构造函数
	 */
	public Error() {

	}

	/**
	 * 有参构造函数
	 * 
	 * @param id
	 * @param info
	 * @param line
	 * @param word
	 */
	public Error(int id, String info, int line, Word word) {
		this.id = id;
		this.info = info;
		this.line = line;
		this.word = word;
	}

	/**
	 * toString
	 */
	public String toString() {
		String str = id + "\t" + info + "\t\t" + line + "\t" + word.value;
		return str;
	}
}
