package Parser;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;

class Association extends Tree {
    private String idName;
    double decimal;

    /**
     * 
     * @param bufferedReader
     * @param line
     * @param writer
     * @throws IOException
     */
    public Association(BufferedReader bufferedReader, int line, FileWriter writer,String fileName) throws IOException {
        //association -->[ identifier :: ] identifier splitter [ constant ] access decimal | none
        //splitter-->  => | +=>
        super("Association", bufferedReader, line, writer,fileName);
        String currentLine = bf.readLine();
        lineNum++;
        
//        if(currentLine==null) {
//        	System.out.println("currentLine is null");
//        }else {
//        	System.out.println("currentLineNum:"+lineNum+" "+currentLine);
//        }
        
        //匹配identifier嵌套
        if (thirdPart(currentLine).equals("none")) {
            idName = "none";
        } else if (currentLine.startsWith("identifier")) {
            //需要判断是identifier :: identifier 还是identifier
            idName = thirdPart(currentLine);
            currentLine = bf.readLine();
            lineNum++;
            if (thirdPart(currentLine).equals("::")) {
                currentLine = bf.readLine();
                lineNum++;
                if (currentLine.startsWith("identifier")) {
                    idName += "::" + thirdPart(currentLine);
                    currentLine = bf.readLine();
                    lineNum++;
                } else {
                    System.out.println(25);
                    writer.write("ERROR APPEARED IN LINE " + lineNum + " : Association STATEMENT INCOMPLETE : identifier LOST\n");
                    System.out.println("ERROR APPEARED IN LINE " + lineNum + " : Association STATEMENT INCOMPLETE : identifier LOST\n");
                    handleError();
                }
            }

            //匹配两种splitter
            if (! (currentLine.contains("=>") || currentLine.contains("+=>"))) {
                System.out.println(26);
                writer.write("ERROR APPEARED IN LINE " + lineNum + " : Association STATEMENT INCOMPLETE : => or +=> LOST\n");
                System.out.println("ERROR APPEARED IN LINE " + lineNum + " : Association STATEMENT INCOMPLETE : => or +=> LOST\n");
                handleError();
            }
            
            currentLine = bf.readLine();
            lineNum++;
            
            //匹配constant
            if (thirdPart(currentLine).equals("constant")) {
                currentLine = bf.readLine();
                lineNum++;
            }
            
            //匹配access
            if (!thirdPart(currentLine).equals("access")) {
                System.out.println(27);
                writer.write("ERROR APPEARED IN LINE " + lineNum + " : Association STATEMENT INCOMPLETE : access LOST\n");
                System.out.println("ERROR APPEARED IN LINE " + lineNum + " : Association STATEMENT INCOMPLETE : access LOST\n");
                handleError();
            }
            currentLine = bf.readLine();
            lineNum++;
            
            if (currentLine.startsWith("Digit")) {
                decimal = Double.parseDouble(thirdPart(currentLine));
            } else {
                System.out.println(28);
                writer.write("ERROR APPEARED IN LINE " + lineNum + " : Association STATEMENT INCOMPLETE : Digit LOST\n");
                handleError();
            }
        } else {
            System.out.println(29);
            writer.write("ERROR APPEARED IN LINE " + lineNum + " : Association STATEMENT INCOMPLETE : This is not an association\n");
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
        str += "identifierName : "+idName+"\r\n";
        if (decimal != 0.0) {
            for (int i = 0; i < num; i++) {
                str += "\t";
            }
            str += "decimal : " + decimal + "\r\n";
        }
        for (Tree t : child) {
            for (int i = 0 ; i<num;i++) {
                str+="\t";
            }
            str += t.toString(num+1);
        }
        return str;
    }

}