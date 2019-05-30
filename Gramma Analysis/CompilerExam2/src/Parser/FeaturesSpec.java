package Parser;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;

class FeaturesSpec extends Tree {
    private String idName;
    private String IOtype;
    
    /**
     * 
     * @param bufferedReader
     * @param line 
     * @param writer
     * @throws IOException
     */
    public FeaturesSpec(BufferedReader bufferedReader, int line, FileWriter writer,String fileName) throws IOException {
    	//语法规则为 featureSpec-->portSpec
    	//portSpec --> identifier : IOtype portType [ { { association } } ] ;
        super("FeatureSpec",bufferedReader, line, writer,fileName);
        String currentLine = bf.readLine();
        lineNum++;
        
        if (thirdPart(currentLine).equals("none")) {
            idName = "none";
            currentLine = bf.readLine();
            lineNum++;
            if (!thirdPart(currentLine).equals(";")) {
                System.out.println(7);
                writer.write("ERROR APPEARED IN LINE " + lineNum + " : FeatureSpec STATEMENT INCOMPLETE\n");
                handleError();
            }
        } else {
            if (!currentLine.startsWith("identifier")) {
            	//不是identifier开头则报错
                System.out.println(8);
                writer.write("ERROR APPEARED IN LINE " + lineNum + " : FeatureSpec STATEMENT INCOMPLETE\n");
                handleError();
            }
            //获取identifier
            idName = thirdPart(currentLine);
            currentLine = bf.readLine();
            lineNum++;
            
            if (!thirdPart(currentLine).equals(":")) {
            	//后跟不是:则报错
                System.out.println(9);
                writer.write("ERROR APPEARED IN LINE " + lineNum + " : Parameter STATEMENT INCOMPLETE\n");
                handleError();
            }
            currentLine = bf.readLine();
            lineNum++;
            //分析完了IOtype
            if (thirdPart(currentLine).equals("in")) {
                IOtype = "in";
                currentLine = bf.readLine();
                lineNum++;
                /**
                 *  in -> in
                 *  out -> out 
                 *  则为in out的IOtype
                 */
                if (thirdPart(currentLine).equals("out")) {
                    IOtype = "in out";
                    currentLine = bf.readLine();
                    lineNum++;
                }
            } else if (thirdPart(currentLine).equals("out")) {
                IOtype = "out";
                currentLine = bf.readLine();
                lineNum++;
            } else {
                System.out.println(10);
                writer.write("ERROR APPEARED IN LINE " + lineNum + " : Parameter STATEMENT INCOMPLETE\n");
                handleError();
            }
            //如果IOtype后面是data或者event则是PortType
            if (thirdPart(currentLine).equals("data") || thirdPart(currentLine).equals("event")) {
                super.type = "portType";
                PortType t = new PortType(bf, lineNum, writer, currentLine,fileName);
                child.add(t);
                bf = t.getBf();
                lineNum = t.getLineNum();

                currentLine = bf.readLine();
                lineNum++;

                if (thirdPart(currentLine).equals("{")) {
                    while (!thirdPart(currentLine).equals("}")) {
                        //Association返回时没有读取下一行，还处于Association的结束行
                        Association t1 = new Association(bf, lineNum, writer,fileName);
                        child.add(t1);
                        bf = t1.getBf();
                        lineNum = t1.getLineNum();

                        currentLine = bf.readLine();
                        lineNum++;
                    }
                    currentLine = bf.readLine();
                    lineNum++;
                }

                if (!thirdPart(currentLine).equals(";")) {
                    System.out.println(11);
                    writer.write("ERROR APPEARED IN LINE " + lineNum + " : Parameter STATEMENT INCOMPLETE\n");
                    handleError();
                }
            } else if (thirdPart(currentLine).equals("parameter")) {
            	//ParameterSpec
                super.type = "Parameter";
                currentLine = bf.readLine();
                lineNum++;
                //若以identifier开头，则一定含有reference
                if (currentLine.startsWith("identifier")) {
                    Reference t = new Reference(bf,lineNum,writer,currentLine,fileName);
                    child.add(t);
                    bf = t.getBf();
                    lineNum  = t.getLineNum();
                    currentLine = bf.readLine();
                    lineNum++;
                }
                //匹配{
                if (thirdPart(currentLine).equals("{")) {
                    while (!thirdPart(currentLine).equals("}")) {
                        //Association返回时没有读取下一行，还处于Association的结束行
                        Association t1 = new Association(bf, lineNum, writer,fileName);
                        child.add(t1);
                        bf = t1.getBf();
                        lineNum = t1.getLineNum();
                        currentLine = bf.readLine();
                        lineNum++;
                    }
                    currentLine = bf.readLine();
                    lineNum++;
                }

                //匹配;
                if (!thirdPart(currentLine).equals(";")) {
                    System.out.println(12);
                    writer.write("ERROR APPEARED IN LINE " + lineNum + " : Parameter STATEMENT INCOMPLETE\n");
                    handleError();
                }
            } else {
                System.out.println(13);
                writer.write("ERROR APPEARED IN LINE " + lineNum + " : Parameter STATEMENT INCOMPLETE\n");
                handleError();
            }
        }
    }

    @Override
    public String toString (int num) {
        String str = new String();
        for (int i = 0 ; i<num;i++) {
            str+="\t";
        }
        str =  "---NodeType : "+super.type+"\r\n";
        for (int i = 0 ; i<num;i++) {
            str+="\t";
        }
        str += "identifier : " + idName +"\r\n";
        for (int i = 0 ; i<num;i++) {
            str+="\t";
        }
        str += "IOtype : " + IOtype +"\r\n";
        for (Tree t : child) {
            for (int i = 0 ; i<num;i++) {
                str+="\t";
            }
            str += t.toString(num+1);
        }
        return str;
    }
}