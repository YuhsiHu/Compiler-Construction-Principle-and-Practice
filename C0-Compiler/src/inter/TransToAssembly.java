package inter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.TextArea;
import java.awt.Toolkit;
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

import javax.swing.JPanel;

public class TransToAssembly {

	// private static final long serialVersionUID = 8766059377195109228L;
	private static String title;
	private static String fileName;
	private ArrayList<String> assemblyList;

	/**
	 * 无参构造函数
	 */
	public TransToAssembly() {

	}

	/**
	 * 有参构造函数
	 * 
	 * @param fileName
	 */
	public TransToAssembly(String fileName) {
		this.fileName = fileName;
		assemblyList = new ArrayList<String>();
		try {
			String str[] = readFile(fileName).split("\n");
			for (int i = 2; i < str.length; i++) {
				String temp[] = str[i].split(",");

				if (temp[0].charAt(temp[0].length() - 1) == '=') {

					String temp1 = temp[0].substring(0, temp[0].length() - 2) + " MOV "
							+ temp[3].substring(0, temp[3].length() - 1) + "," + temp[1] + "\n";
					assemblyList.add(temp1);
				} else if (temp[0].charAt(temp[0].length() - 1) == '+' && temp[0].charAt(temp[0].length() - 2) == '+') {
					String temp1 = temp[0].substring(0, temp[0].length() - 3) + " INC " + temp[1] + "\n";
					assemblyList.add(temp1);
				} else if (temp[0].charAt(temp[0].length() - 1) == '+') {
					String temp1 = temp[0].substring(0, temp[0].length() - 2) + " ADD "
							+ temp[3].substring(0, temp[3].length() - 1) + "," + temp[1] + "\n";
					assemblyList.add(temp1);
				} else if (temp[0].charAt(temp[0].length() - 1) == '-') {
					String temp1 = temp[0].substring(0, temp[0].length() - 2) + " SUB "
							+ temp[3].substring(0, temp[3].length() - 1) + "," + temp[1] + "\n";
					assemblyList.add(temp1);
				} else if (temp[0].charAt(temp[0].length() - 1) == '*') {
					String temp1 = temp[0].substring(0, temp[0].length() - 2) + " MUL "
							+ temp[3].substring(0, temp[3].length() - 1) + "," + temp[1] + "\n";
					assemblyList.add(temp1);
				} else if (temp[0].charAt(temp[0].length() - 1) == '/') {
					String temp1 = temp[0].substring(0, temp[0].length() - 2) + " DIV "
							+ temp[3].substring(0, temp[3].length() - 1) + "," + temp[1] + "\n";
					assemblyList.add(temp1);
				} else if (temp[0].charAt(temp[0].length() - 1) == 'J' && temp[0].charAt(temp[0].length() - 2) == 'R') {
					String temp1 = temp[0].substring(0, temp[0].length() - 3) + " JMP "
							+ temp[3].substring(0, temp[3].length() - 1) + "\n";
					assemblyList.add(temp1);
				} else if (temp[0].charAt(temp[0].length() - 1) == 'J' && temp[0].charAt(temp[0].length() - 2) == 'F') {
					String temp1 = temp[0].substring(0, temp[0].length() - 3) + " JZ "
							+ temp[3].substring(0, temp[3].length() - 1) + "\n";
					assemblyList.add(temp1);
				} else if (temp[0].charAt(temp[0].length() - 1) == '>') {
					String temp1 = temp[0].substring(0, temp[0].length() - 2) + " JG "
							+ temp[3].substring(0, temp[3].length() - 1) + "\n";
					assemblyList.add(temp1);
				} else if (temp[0].charAt(temp[0].length() - 1) == '<') {
					String temp1 = temp[0].substring(0, temp[0].length() - 2) + " JL "
							+ temp[3].substring(0, temp[3].length() - 1) + "\n";
					assemblyList.add(temp1);
				}
			}
			// 输出到文件中
			outputAssemblyResult();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 输出汇编结果到txt中
	 * 
	 * @throws IOException
	 */
	private void outputAssemblyResult() throws IOException {
		File file = new File("./output/");
		if (!file.exists()) {
			file.mkdirs();
			file.createNewFile();// 如果这个文件不存在就创建它
		}
		String path = file.getAbsolutePath();
		FileOutputStream fos = new FileOutputStream(path + "/AssemblyLanguage.txt");
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		OutputStreamWriter osw1 = new OutputStreamWriter(bos, "utf-8");
		PrintWriter pw1 = new PrintWriter(osw1);
		for (String str : assemblyList) {
			pw1.println(str);
		}
		pw1.close();
	}

	/**
	 * 读取中间代码文件
	 * 
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	private String readFile(String filename) throws IOException {
		StringBuilder sbr = new StringBuilder();
		String str;
		FileInputStream fis = new FileInputStream(filename);
		BufferedInputStream bis = new BufferedInputStream(fis);
		InputStreamReader isr = new InputStreamReader(bis, "UTF-8");
		BufferedReader in = new BufferedReader(isr);
		while ((str = in.readLine()) != null) {
			sbr.append(str).append('\n');
		}
		in.close();
		return sbr.toString();
	}

	public static String getFileName() {
		return fileName;
	}

	public static void setFileName(String fileName) {
		TransToAssembly.fileName = fileName;
	}

}
