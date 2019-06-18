package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import grama.Parser;
import inter.TransToAssembly;
import lex.LexAnalyse;

/**
 * 主函数
 * 
 * @author Hu Yuxi
 *
 */
public class Main {

	/**
	 * 待编译的C语言程序应直接存放在工程目录下
	 * 
	 * @param arg
	 * @throws IOException
	 */
	public static void main(String[] arg) throws IOException {
		// 获取源程序
		System.out.println("please input test file name : ");
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		String sourceCode = bf.readLine();
		// 词法分析
		LexAnalyse lexAnalyze = new LexAnalyse();
		lexAnalyze.lexBegin(sourceCode);
		// 语法分析
		Parser parser = new Parser(lexAnalyze);
		parser.grammerAnalyse();
		parser.outputLL1();
		// 语义分析,生成中间代码
		parser.outputFourElem();
		// 读取中间代码文件后转化为汇编输出
		TransToAssembly transToAssembly = new TransToAssembly("./output/FourElement.txt");

	}
}
