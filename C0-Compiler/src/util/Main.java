package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import lex.LexAnalyse;
public class Main {

	public static void main(String[] arg) throws IOException {
		//获取源程序
		System.out.println("please input test file name : ");
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		String sourceCode=bf.readLine();
		//词法分析
		LexAnalyse lexAnalyze=new LexAnalyse();
		lexAnalyze.lexBegin(sourceCode);
		//语法分析
		//语义分析
	}
}
