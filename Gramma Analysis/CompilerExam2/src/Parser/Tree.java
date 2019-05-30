package Parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class Tree {

	protected String type; // 节点类型
	protected LinkedList<Tree> child;// 子节点
	protected BufferedReader bf;
	protected int lineNum;// 行号
	protected String fileName;//tokenOut文件名
	protected FileWriter writer;

	/**
	 * 抽象语法树构造函数
	 * 
	 * @param bufferedReader
	 * @param line
	 *            行号
	 * @param writer
	 * @throws IOException
	 */
	public Tree(BufferedReader bufferedReader, int line, FileWriter writer,String fileName) throws IOException {
		// 根节点初始化
		type = new String();
		child = new LinkedList<Tree>();
		this.bf = bufferedReader;
		this.lineNum = line;
		this.writer = writer;
		this.fileName=fileName;
		String currentLine = new String();

		System.out.println(this.fileName);
		
		// 构造分析树
		while ((currentLine = bf.readLine()) != null) {
			lineNum++;
			if (currentLine.startsWith("thread")) {
				//Thread
				ThreadSpec t = new ThreadSpec(bf, lineNum, writer,fileName);
				child.add(t);
				// 更新bf读取位置和lineNum
				// updateBf(t.getLineNum());
				bf = t.getBf();
				lineNum = t.getLineNum();
			} else {
				System.out.println("1>>>ERROR APPEARED IN LINE " + lineNum + " : STATEMENT INCOMPLETE\r\n");
				writer.write("ERROR APPEARED IN LINE " + lineNum + " : STATEMENT INCOMPLETE\r\n");
				handleError();
			}
		}
	}

	/**
	 * 
	 * @param num
	 *            缩进个数
	 * @return
	 */
	public String toString(int num) {
		String str = "---NodeType : root\r\n";
		for (Tree t : child) {
			for (int i = 0; i < num; i++) {
				str += "\t";
			}
			str += t.toString(num + 1);
		}
		return str;
	}

	/**
	 * 非根节点构造
	 * 
	 * @param type
	 * @param bf
	 * @param line
	 * @param writer
	 */
	protected Tree(String type, BufferedReader bf, int line, FileWriter writer,String fileName) {
		// 非根节点
		this.type = type;
		child = new LinkedList<Tree>();
		this.bf = bf;
		this.lineNum = line;
		this.writer = writer;
		this.fileName=fileName;
	}

	/**
	 * 返回词法分析程序一行第三部分,即内容
	 * 
	 * @param line
	 * @return 词
	 */
	public String thirdPart(String line) {
		if(line!=null) {
		// 返回词法分析一行中第三部分
		StringTokenizer st = new StringTokenizer(line, " ");
		String curr = new String();
		st.nextToken();
		st.nextToken();
		curr = st.nextToken();
		return curr;
		}else
			return "NULL";
	}

	/**
	 * 读到某一行
	 * @param bufferedReader
	 * @param lineNum 读到第lineNum-1行
	 * @return
	 * @throws IOException
	 */
	public String readToLineNum(BufferedReader bufferedReader, int lineNum) throws IOException {
		String str = new String();
		for (int i = 1; i < lineNum; i++) {
			bufferedReader.readLine();
		}
		str = bufferedReader.readLine();
		return str;
	}
	
	public void handleError() throws IOException {
		String printNode = this.toString(1);
        writer.write(printNode);
        bf.close();
        writer.close();
		System.exit(lineNum);
	}

	/**
	 * 
	 * @return line number 行号
	 */
	public int getLineNum() {
		return lineNum;
	}

	/**
	 * 
	 * @return bf bufferedReader
	 */
	public BufferedReader getBf() {
		return bf;
	}

	/**
	 * 
	 * @return child 子节点
	 */
	public LinkedList<Tree> getChild() {
		return child;
	}
	
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
}
