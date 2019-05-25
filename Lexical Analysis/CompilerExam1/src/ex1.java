import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
/**
 * 词法分析程序
 * @author Yuxi Hu
 *
 */
public class ex1 {
	public static ArrayList<String> keyword;// 关键字
	public static ArrayList<String> assign;// 专用符号
	public static int[] state;// 状态

	/**
	 * init
	 **/
	public static void init() {
		// 关键字
		keyword = new ArrayList<String>();
		keyword.add("thread");
		keyword.add("features");
		keyword.add("flows");
		keyword.add("properties");
		keyword.add("end");
		keyword.add("none");
		keyword.add("in");
		keyword.add("out");
		keyword.add("data");
		keyword.add("port");
		keyword.add("event");
		keyword.add("paramenter");
		keyword.add("flow");
		keyword.add("source");
		keyword.add("sink");
		keyword.add("path");
		keyword.add("constant");
		keyword.add("access");
		// 专用符号
		assign = new ArrayList<String>();
		assign.add("=>");
		assign.add("+=>");
		assign.add(";");
		assign.add(":");
		assign.add("::");
		assign.add("{");
		assign.add("}");
		assign.add("->");
		// 根据状态转换图得知有9个状态
		state = new int[9];
	}

	/**
	 * 
	 * @param c
	 *            char
	 * @return if char is digit
	 */
	public static boolean isDigit(char c) {
		if (c >= '0' && c <= '9')
			return true;
		return false;
	}

	/**
	 * 
	 * @param c
	 *            char
	 * @return if char is letter
	 */
	public static boolean isLetter(char c) {
		if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'))
			return true;
		return false;
	}

	/**
	 * 
	 * @param cchar
	 * @return if it is char or digit
	 */
	public static boolean isLetterOrDigit(char c) {
		return isDigit(c) || isLetter(c);
	}

	/**
	 * main
	 * 
	 * @param arg
	 * @throws IOException
	 */
	public static void main(String[] arg) throws IOException {
		init();
		System.out.println("please input test file name : ");
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		String input = new String();
		FileWriter writer = new FileWriter("./output.txt");

		// 输入读取的文件
		input = bf.readLine();
		// 打开文件
		File file = new File(input);
		FileReader filereader = new FileReader(file);
		StringBuffer buffer = new StringBuffer();
		// 向output.txt写，\r\n是Windows下txt换行
		writer.write("read file" + input + "\r\n");
		BufferedReader reader = new BufferedReader(filereader);

		// 记录行号
		int lineNum = 0;
		String currentLine = new String();

		// 判断当前行是否读取完成
		while ((currentLine = reader.readLine()) != null) {
			System.out.println(currentLine);
			// 每次读取行后，行号自增
			lineNum++;
			// 将字符串转换为字符数组
			char[] charArr = currentLine.toCharArray();
			// 遍历字符数组
			int i = 0;
			while (i < currentLine.length()) {
				// 开始START状态为状态0，详见状态转换图
				int state = 0;
				// 只有到终止DONE状态8，才可以退出循环
				while (state != 8) {
					switch (state) {
					// 状态-1为报错状态，到达状态-1后，报错，再跳到状态8进行结束
					/**********case -1**********/
					case -1:
						System.out.println("8");
						writer.write("#########ERROR APPEARED IN " + lineNum + "#########\r\n");
						// 将i赋值为字符串结尾，使i跳出循环，保证每行最多报一次错误
						i = currentLine.length();
						state = 8;
						// 状态0为初始状态
						
					/**********case 0**********/	
					case 0:
						//末尾如果是空格，i++之后还会跳到这里来，需要换行了
						if(i==currentLine.length()) {
							state=8;
							break;
						}
						// 若i未越界
						if (i < currentLine.length()) {
							// 打印到控制台的信息为状态跳转的过程
							// 读取到空格和制表符后，仍然在状态0
							if (charArr[i] == ' ' || charArr[i] == '\t') {
								// start state
								System.out.print("0 -> ");
								state = 0;
							} else if (charArr[i] == '-') {
								System.out.print("1 -> ");
								// state MIN
								state = 1;
							} else if (charArr[i] == '+') {
								System.out.print("2 -> ");
								// state PLUS
								state = 2;
							} else if (isDigit(charArr[i])) {
								System.out.print("3 -> ");
								// state INNUM
								// 因为后面还有可能有数字，所以不换行
								buffer.append("Digit -> " + charArr[i]);
								state = 3;
							} else if (isLetter(charArr[i])) {
								System.out.print("6 -> ");
								// state INID
								// 因为后面还有可能有字母，所以不换行
								buffer.append(charArr[i]);
								state = 6;
							} else if (charArr[i] == '=' && i + 1 < currentLine.length() && charArr[i + 1] == '>') {
								// 向后忽略一位
								System.out.println("8");
								i++;
								writer.write("assign -> =>" + "\r\n");
								state = 8;
							} else if (charArr[i] == ';' || charArr[i] == '{' || charArr[i] == '}') {
								writer.write("assign -> " + charArr[i] + "\r\n");
								System.out.println("8");
								state = 8;
							} else if (charArr[i] == ':') {
								if (i + 1 < currentLine.length() && charArr[i + 1] == ':') {
									// 判断是否为两个冒号，如果是，最长匹配，返回
									i++;
									writer.write("assign -> ::\r\n");
									System.out.println("8");
									state = 8;
								} else {
									// 一个冒号
									writer.write("assign -> :\r\n");
									System.out.println("8");
									state = 8;
								}
							} else {
								System.out.print("-1 -> ");
								writer.write("error type : unknown symbol\r\n");
								state = -1;
							}
							i++;
							//System.out.println("i:"+i)
						} else
							break;
						break;
					
					/**********case 1**********/
					case 1:
						if (i < currentLine.length()) {
							if (charArr[i] == '>') {
								// 专用符号
								writer.write("assign -> ->\r\n");
								// DONE
								System.out.println("8");
								state = 8;
							} else if (isDigit(charArr[i])) {
								// 数字
								buffer.append("Digit -> -" + charArr[i]);
								System.out.print("3 -> ");
								state = 3;
							} else {
								System.out.print("-1 -> ");
								writer.write("error type : not negative number or assign \r\n");
								state = -1;
							}
							i++;
						} else
							break;
						break;
					
                    /**********case 2**********/
					case 2:
						if (i < currentLine.length()) {
							if (charArr[i] == '=' && i + 1 < currentLine.length() && charArr[i + 1] == '>') {
								i++;
								writer.write("assign -> =>\r\n");
								System.out.println("8");
								state = 8;
							} else if (isDigit(charArr[i])) {
								buffer.append("Digit -> " + charArr[i]);
								System.out.print("3 -> ");
								state = 3;
							} else {
								System.out.print("-1 -> ");
								writer.write("error type : not positive number or assign \r\n");
								state = -1;
							}
							i++;
						} else
							break;
						break;
					
					/**********case 3**********/
					case 3:
						if (i < currentLine.length()) {
							if (charArr[i] == '.') {
								buffer.append(".");
								System.out.print("4 -> ");
								state = 4;
							} else if (isDigit(charArr[i])) {
								buffer.append(charArr[i]);
								System.out.print("3 -> ");
								state = 3;
								if (i == currentLine.length() - 1) {
									buffer = new StringBuffer();
									writer.write("error type : dot isn't contained in number \r\n");
									state = -1;
								}
							} else {
								// 如果数字发现问题，则清空buffer中的序列（不能将错误的分析结果输出），并报错
								buffer = new StringBuffer();
								System.out.print("-1 -> ");
								writer.write("error type : dot isn't contained in number \r\n");
								state = -1;
							}
							i++;
						} else
							break;
						break;
						
					/**********case 4**********/
					case 4:
						if (i < currentLine.length()) {
							if (isDigit(charArr[i])) {
								buffer.append(charArr[i]);
								System.out.print("5 -> ");
								state = 5;
							} else {
								// 如果数字发现问题，则清空buffer中的序列（不能将错误的分析结果输出），并报错
								buffer = new StringBuffer();
								System.out.print("-1 -> ");
								writer.write("error type : there must be at least one number behind dot \r\n");
								state = -1;
							}
							i++;
						} else
							break;
						break;
						
					/**********case 5**********/
					case 5:
						if (i < currentLine.length()) {
							if (isDigit(charArr[i])) {
								buffer.append(charArr[i]);
								System.out.print("5 -> ");
								state = 5;
								if (i == currentLine.length() - 1) {
									buffer.append("\r\n");
									writer.write("number -> " + String.valueOf(buffer));
									buffer = new StringBuffer();
									state = 8;
								}
							} else {
								// 后面不是数字，则将前面数字输出，并完成
								buffer.append("\r\n");
								writer.write(String.valueOf(buffer));
								writer.flush();
								buffer = new StringBuffer();
								System.out.println("8");
								state = 8;
								// 因为后面有i++,所以现在回滚前一个字符,判断当前字符
								i--;
							}
							i++;
						} else
							break;
						break;
						
					/**********case 6**********/
					case 6:
						if (i < currentLine.length()) {
							if (charArr[i] == '_') {
								buffer.append("_");
								System.out.print("7 -> ");
								state = 7;
							} else if (isLetterOrDigit(charArr[i])) {
								buffer.append(charArr[i]);
								System.out.println(charArr[i]);
								System.out.print("6 -> ");
								state = 6;

								// 判断当前字符串是否已经结束。因为没有\0作为标志位，所以当最后为identifier时，会阻塞在状态6
								if (i == currentLine.length() - 1) {
									String word = String.valueOf(buffer);
									boolean isKeyWord = false;
									for (String s : keyword) {
										if (s.equals(word)) {
											isKeyWord = true;
											writer.write(s+" -> " + String.valueOf(buffer) + "\r\n");
											break;
										}
									}
									
									if (!isKeyWord) {
										writer.write("identifier -> " + String.valueOf(buffer) + "\r\n");
									}
									
									buffer = new StringBuffer();
									System.out.println("8 -> currentLine finish");
									state = 8;
								}
							} else {
								// 根据最长匹配，认定前文字符为正确字符，所以将token输出
								String word = String.valueOf(buffer);
								boolean isKeyWord = false;
								for (String s : keyword) {
									if (s.equals(word)) {
										isKeyWord = true;
										writer.write(s+" -> " + String.valueOf(buffer) + "\r\n");
										break;
									}
								}
								if (!isKeyWord) {
									writer.write("identifier -> " + String.valueOf(buffer) + "\r\n");
								}
								buffer = new StringBuffer();
								System.out.println(8);
								state = 8;
								// 因为后面有i++,所以现在回滚前一个字符,判断当前字符
								i--;
							}
							i++;
						} else
							break;
						break;
						
					/**********case 7**********/
					case 7:
						if (i < currentLine.length()) {
							if (isLetterOrDigit(charArr[i])) {
								buffer.append(charArr[i]);
								System.out.print("6 -> ");
								state = 6;
								// 判断当前字符串是否已经结束。因为没有\0作为标志位，所以当最后为identifier_X时，会阻塞在状态6
								if (i == currentLine.length() - 1) {
									buffer.append('\n');
									writer.write("identifier -> " + String.valueOf(buffer));
									buffer = new StringBuffer();
									System.out.println("8 -> currentLine finish");
									state = 8;
								}
							} else {
								buffer = new StringBuffer();
								System.out.print("-1 -> ");
								writer.write(
										"error type : there must be at least one letter or digit behind underline \r\n");
								state = -1;
							}
							i++;
						} else
							break;
						break;
						
					/**********case 8**********/
					case 8:
						break;
					}
				}
			}
		}

		writer.close();
		filereader.close();
		reader.close();
		bf.close();
	}
}