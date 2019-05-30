package Parser;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

class PortType extends Tree {
    private String name;
    
    /**
     * 
     * @param bufferedReader
     * @param line
     * @param writer
     * @param curr
     * @throws IOException
     */
    public PortType(BufferedReader bufferedReader, int line, FileWriter writer,String curr,String fileName) throws IOException {
        //portType -->data port [ reference ] | event data port [ reference ]| event port
        super("PortType", bufferedReader, line, writer,fileName);
        String currentLine = curr;

        if (thirdPart(currentLine).equals("event")) {
            name = "event ";
            currentLine = bf.readLine();
            lineNum++;
        }
        
        if (thirdPart(currentLine).equals("data")) {
            if (name==null){
                name = "data port";
            }else
                name += "data port";
            currentLine = bf.readLine();
            lineNum++;
            
            if (thirdPart(currentLine).equals("port")) {
                //为了避免多读一行，用bufferedReader1去读下一个字符
                //BufferedReader readNext = new BufferedReader(new FileReader("./tokenOut.txt"));
            	BufferedReader readNext = new BufferedReader(new FileReader("./"+super.fileName));
            	
                String s = readToLineNum(readNext,lineNum);
                s = readNext.readLine();
                if (s.startsWith("identifier")) {
                    currentLine = bf.readLine();
                    lineNum++;
                    Reference t = new Reference(bf, lineNum, writer,currentLine,fileName);
                    child.add(t);
                    bf = t.getBf();
                    lineNum = t.getLineNum();
                }
            }
        } else if (thirdPart(currentLine).equals("port") && name.startsWith("event")) {
            name += "port";
        } else {
            System.out.println(14);
            writer.write("ERROR APPEARED IN LINE " + lineNum + " : portType STATEMENT INCOMPLETE\n");
            handleError();
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
        str += "PortTypeKind : "+name+"\r\n";
        for (Tree t : child) {
            for (int i = 0 ; i<num;i++) {
                str+="\t";
            }
            str += t.toString(num+1);
        }
        return str;
    }
}