package Parser;

import java.io.*;

/**
 * 语法分析程序
 * 入口,读写词法分析程序的tokenOut.txt并初始化分析树
 * @author Hu Yuxi
 *
 */
public class util {

	/**
	 * main
	 * @param arg
	 * @throws IOException
	 */
    public static void main(String[] arg) throws IOException {

    	System.out.println("please input test file name : ");
    	BufferedReader bf1 = new BufferedReader(new InputStreamReader(System.in));
		String fileName = new String();
		// 输入读取的文件
		fileName = bf1.readLine();
		// 打开文件
		File file = new File(fileName);
		FileReader fileReader = new FileReader(file);
    	
        //FileReader fileReader = new FileReader("./tokenOut.txt");//词法分析程序的输出结果为tokenOut.txt
        BufferedReader bf = new BufferedReader(fileReader);
        FileWriter writer = new FileWriter("./SyntaxOut.txt");//语法分析程序的结果将输出到SyntaxOut.txt
        //构造根节点
        Tree t = new Tree(bf,0,writer,fileName);
        String printNode = t.toString(1);
        writer.write(printNode);
        fileReader.close();
        bf.close();
        writer.close();
    }  
    
}

