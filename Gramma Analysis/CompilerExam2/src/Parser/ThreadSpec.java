package Parser;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;

public class ThreadSpec extends Tree {

	private String idName;
	
	/**
	 * 
	 * @param bufferedReader
	 * @param line
	 * @param writer
	 * @throws IOException
	 */
	public ThreadSpec(BufferedReader bufferedReader, int line, FileWriter writer,String fileName) throws IOException {
		 //语法规则为
        // ThreadSpec -->thread identifier [ features featureSpec ] [ flows flowSpec ] [ properties association; ] end identifier ;
        super("ThreadSpec", bufferedReader, line, writer,fileName);
        //首先构造自身节点的信息
        String currentLine = bf.readLine();
        lineNum++;
        boolean hasFeatures = false, hasFlow = false, hasProperties = false;
        
        if (currentLine==null || !(currentLine.startsWith("identifier"))) {
            System.out.println("2>>>ERROR APPEARED IN LINE " + lineNum + " : ThreadSpec STATEMENT INCOMPLETE\n");
            writer.write("ERROR APPEARED IN LINE " + lineNum + " : ThreadSpec STATEMENT INCOMPLETE\n");
            handleError();
        } else {
        	//词法分析结果类别不identifier时
        	//获取到identifier
            idName = thirdPart(currentLine);
            //while循环继续读取下文，直到遇到end identifier跳出循环
            while ((currentLine = bf.readLine()) != null) {
                lineNum++;
                if (currentLine.startsWith("features") && !hasFeatures) {
                    hasFeatures = true;
                    //需要添加子树节点
                    FeaturesSpec t = new FeaturesSpec(bf, lineNum, writer,fileName);
                    //增加一个featuresSpec子树
                    child.add(t);
                    //更新bf读取位置和lineNum             
                    this.bf = t.getBf();
                    lineNum = t.getLineNum();
                } else if (currentLine.startsWith("flows") && !hasFlow) {
                    hasFlow = true;
                    hasFeatures = true;
                    //需要添加子树节点
                    FlowSpec t = new FlowSpec(this.bf, lineNum, writer,fileName);
                    child.add(t);
                    //更新bf读取位置和lineNum        
                    this.bf = t.getBf();
                    lineNum = t.getLineNum();
                } else if (currentLine.startsWith("properties") && !hasProperties) {
                    hasProperties = true;
                    hasFeatures = true;
                    hasFlow = true;
                    //需要添加子树节点
                    Association t = new Association(bf, lineNum, writer,fileName);
                    child.add(t);
                    //更新bf读取位置和lineNum
                    bf = t.getBf();
                    lineNum = t.getLineNum();

                    currentLine = bf.readLine();
                    lineNum++;
                    if (!thirdPart(currentLine).equals(";")) {
                        System.out.println(3);
                        writer.write("ERROR APPEARED IN LINE " + lineNum + " : ThreadSpec STATEMENT INCOMPLETE\n");
                        handleError();
                    }
                } else if (currentLine.startsWith("end")) {
                    //无需添加子树
                    currentLine = bf.readLine();
                    lineNum++;
                    //获得end后的identifier
                    String id = thirdPart(currentLine);
                    if (!(currentLine.startsWith("identifier") && id.equals(idName))) {
                        System.out.println(4);
                        writer.write("ERROR APPEARED IN LINE " + lineNum + " : ThreadSpec STATEMENT INCOMPLETE\n");
                        handleError();
                    }
                    
                    currentLine = bf.readLine();
                    lineNum++;
                    if (!thirdPart(currentLine).equals(";")) {
                        System.out.println(5);
                        writer.write("ERROR APPEARED IN LINE " + lineNum + " : ThreadSpec STATEMENT INCOMPLETE\n");
                        handleError();
                    }
                    System.out.println(idName+"  "+lineNum);
                    break;
                } else {
                    System.out.println(6);
                    writer.write("ERROR APPEARED IN LINE " + lineNum + " : ThreadSpec STATEMENT INCOMPLETE\n");
                    handleError();
                }
            }
        }
    }
		

	/**
	 * 
	 * @return idName identifierName
	 */
	public String getIdName() {
		return idName;
	}

	@Override
	public String toString(int num) {
		String str = new String();
		for (int i = 0; i < num; i++) {
			str += "\t";
		}
		str = "---NodeType : " + super.type + "\r\n";
		for (Tree t : child) {
			for (int i = 0; i < num; i++) {
				str += "\t";
			}
			str += t.toString(num + 1);
		}
		return str;
	}
}
