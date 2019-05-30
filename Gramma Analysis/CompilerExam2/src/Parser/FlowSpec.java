package Parser;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;

class FlowSpec extends Tree {
    String idName;

    /**
     * 
     * @param bufferedReader
     * @param line
     * @param writer
     * @throws IOException
     */
    public FlowSpec(BufferedReader bufferedReader, int line, FileWriter writer,String fileName) throws IOException {
        //语法规则为
        //flowSpec -->flowSourceSpec| flowSinkSpec| flowPathSpec| none;
        super("FlowSpec", bufferedReader, line, writer,fileName);
        String currentLine = bf.readLine();
        lineNum++;
        
        if (thirdPart(currentLine).equals("none")) {
            idName = "none";
            currentLine = bf.readLine();
            lineNum++;
            if (!thirdPart(currentLine).equals(";")) {
                System.out.println(15);
                writer.write("ERROR APPEARED IN LINE " + lineNum + " : FlowSpec STATEMENT INCOMPLETE\n");
                handleError();
                handleError();
            }
        } else if (currentLine.startsWith("identifier")) {
            //将第一个identifier保存
            idName = thirdPart(currentLine);
            //无法判断是哪个,所以继续分析
            //flowSourceSpec -->identifier : flow source identifier [ { { association } } ] ;
            //flowSinkSpec -->identifier : flow sink identifier[ { { association } } ] ;
            //flowPathSpec -->identifier : flow path identifier ->identifier;
            currentLine = bf.readLine();
            lineNum++;
            if (!thirdPart(currentLine).equals(":")) {
                System.out.println(16);
                writer.write("ERROR APPEARED IN LINE " + lineNum + " : FlowSpec STATEMENT INCOMPLETE\n");
                handleError();
            } else {
            	//匹配 :
                idName += " :";
            }
            currentLine = bf.readLine();
            lineNum++;
            
            if (!thirdPart(currentLine).equals("flow")) {
                System.out.println(17);
                writer.write("ERROR APPEARED IN LINE " + lineNum + " : FlowSpec STATEMENT INCOMPLETE\n");
                handleError();
            } else {
                idName += " flow";
            }
            currentLine = bf.readLine();
            lineNum++;
            
            if (thirdPart(currentLine).equals("source") || thirdPart(currentLine).equals("sink")) {
                /**
                 * 因为
                 * flowSourceSpec -->identifier : flow source identifier [ { { association } } ] ;
                 * flowSinkSpec -->identifier : flow sink identifier[ { { association } } ] ;
                 * 两个结构基本相同，就放到一起分析
                **/
                if (thirdPart(currentLine).equals("source")) {
                    super.type = "flowSourceSpec";
                } else {
                    super.type = "flowSinkSpec";
                }
                
                //将source或sink添加进idName片段
                idName += " " + thirdPart(currentLine);
                currentLine = bf.readLine();
                lineNum++;
                
                if (!currentLine.startsWith("identifier")) {
                    System.out.println(18);
                    writer.write("ERROR APPEARED IN LINE " + lineNum + " : " + super.type + "STATEMENT INCOMPLETE\n");
                    handleError();
                } else {
                    //将第二处identifier添加进idName片段保存
                    idName += " " + thirdPart(currentLine);
                }
                currentLine = bf.readLine();
                lineNum++;
                //匹配{
                if (thirdPart(currentLine).equals("{")) {
                    while (!thirdPart(currentLine).equals("}")) {
                        //分析association信息
                        Association t = new Association(bf, lineNum, writer,fileName);
                        child.add(t);
                        //updateBf(t.getLineNum());
                        bf = t.getBf();
                        lineNum = t.getLineNum();

                        currentLine = bf.readLine();
                        lineNum++;
                    }
                    currentLine = bf.readLine();
                    lineNum++;
                }
               //匹配是否有;
                if (!thirdPart(currentLine).equals(";")) {
                    System.out.println(19);
                    writer.write("ERROR APPEARED IN LINE " + lineNum + " : " + super.type + "STATEMENT INCOMPLETE\n");
                    handleError();
                }
            } else if (thirdPart(currentLine).equals("path")) {
            	/**
            	 * flowPathSpec -->identifier : flow path identifier ->identifier;
            	 */
                idName += " path";
                currentLine = bf.readLine();
                lineNum++;
                
                if (!currentLine.startsWith("identifier")) {
                    System.out.println(20);
                    writer.write("ERROR APPEARED IN LINE " + lineNum + " : FlowPathSpec STATEMENT INCOMPLETE\n");
                    handleError();
                } else {
                	//将第二处identifier添加进idName片段保存
                    idName += " " + thirdPart(currentLine);
                }
                currentLine = bf.readLine();
                lineNum++;
                //匹配->
                if (thirdPart(currentLine).equals("->")) {
                    idName += " -> ";
                } else {
                    System.out.println(21);
                    writer.write("ERROR APPEARED IN LINE " + lineNum + " : FlowPathSpec STATEMENT INCOMPLETE\n");
                    handleError();
                }
                currentLine = bf.readLine();
                lineNum++;
                
                if (!currentLine.startsWith("identifier")) {
                    System.out.println(22);
                    writer.write("ERROR APPEARED IN LINE " + lineNum + " : FlowPathSpec STATEMENT INCOMPLETE\n");
                    handleError();
                } else {
                	//将第三处identifier添加进idName片段保存
                    idName += thirdPart(currentLine);
                }
                currentLine = bf.readLine();
                lineNum++;
                //匹配;
                if (!thirdPart(currentLine).equals(";")) {
                    System.out.println(23);
                    writer.write("ERROR APPEARED IN LINE " + lineNum + " : FlowPathSpec STATEMENT INCOMPLETE\n");
                    handleError();
                }
            } else {
                System.out.println(24);
                writer.write("ERROR APPEARED IN LINE " + lineNum + " : FlowPathSpec STATEMENT INCOMPLETE\n");
                handleError();
            }
        }
    }
    
    @Override
    public String toString(int num) {
        String str = new String();
        for (int i = 0 ; i<num;i++) {
            str+="\t";
        }
        str =  "---NodeType : "+super.type+"\r\n";
        for (int i = 0 ; i<num;i++) {
            str+="\t";
        }
        str += "FlowSpecKind : "+idName+"\r\n";
        for (Tree t : child) {
            for (int i = 0 ; i<num;i++) {
                str+="\t";
            }
            str += t.toString(num+1);
        }
        return str;
    }
}