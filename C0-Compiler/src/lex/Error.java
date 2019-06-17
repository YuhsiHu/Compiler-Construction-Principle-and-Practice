package lex;

/**
 * 语法分析报错信息类
 * @author Hu Yuxi
 *
 */
public class Error {
	private int id;// 错误序号；
	private String info;// 错误信息；
	private int line;// 错误所在行
	private Word word;// 错误的单词

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
		this.setId(id);
		this.setInfo(info);
		this.setLine(line);
		this.setWord(word);
	}

	/**
	 * toString
	 */
	public String toString() {
		String str = getId() + "\t" + getInfo() + "\t\t" + getLine() + "\t" + getWord().getValue();
		return str;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public Word getWord() {
		return word;
	}

	public void setWord(Word word) {
		this.word = word;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
}
