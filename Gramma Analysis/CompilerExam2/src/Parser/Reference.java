package Parser;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

class Reference extends Tree {
    private String idName;

    /**
     * 
     * @param bufferedReader
     * @param line
     * @param writer
     * @param current
     * @throws IOException
     */
    public Reference(BufferedReader bufferedReader, int line, FileWriter writer, String current,String fileName) throws IOException {	
        //reference -->[ packageName :: ]  identifier
        //packageName --> { identifier :: }  identifier

        //type标记reference式子构成
        // type = 1时 reference --> packageName :: identifier，idName 实际是packageName中的内容
        //type = 2时，reference --> identifier，idName是Reference中的内容
        super("reference", bufferedReader, line, writer,fileName);
        BufferedReader readNext = new BufferedReader(new FileReader("./"+super.fileName));
        String currentLine = current;
        
        //s记录currentLine下一行
        String s = readToLineNum(readNext,lineNum);
        s = readNext.readLine();
        //如果下一行是::则说明有packageName
        if (thirdPart(s).equals("::")) {
            while (currentLine.startsWith("identifier") && thirdPart(s).equals("::")) {
                if (idName==null){
                    idName = thirdPart(currentLine)+"::";
                }else {
                    idName += thirdPart(currentLine) + "::";
                }
                currentLine = bf.readLine();
                lineNum++;
                currentLine = bf.readLine();
                lineNum++;
                s = readNext.readLine();
                s = readNext.readLine();
            }
        }
        //已经读取到了id:: id::  所以保留了一行identifier，只需要br继续读一行
        if (!currentLine.startsWith("identifier")) {
            System.out.println(30);
            writer.write("ERROR APPEARED IN LINE " + lineNum + " : PackageName STATEMENT INCOMPLETE\n");
            handleError();
        }
        //没有读取到Reference外的内容
        idName += thirdPart(currentLine);
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
        str += "identifierPath : "+idName+"\r\n";
        for (Tree t : child) {
            for (int i = 0 ; i<num;i++) {
                str+="\t";
            }
            str += t.toString(num+1) + "\r\n";
        }
        return str;
    }
}