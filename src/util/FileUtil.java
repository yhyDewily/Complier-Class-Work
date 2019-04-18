package util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    //读取文件内容，分行存取到list中
    public List<String> readFile(String filename) throws IOException {
        List<String> list = new ArrayList<String>();
        FileReader fread = new FileReader(filename);
        BufferedReader bf = new BufferedReader(fread);
        String strLine = bf.readLine();
        while (strLine != null ){
            list.add(strLine);
            strLine = bf.readLine();
        }
        bf.close();
        fread.close();
        return list;
    }

    public void outputFile(String text) {
        String filename = "E:\\SHU\\Compiler\\analysis\\src\\parsing_test_file\\output\\output1.txt";
        FileWriter fwrite;
        try {
           fwrite = new FileWriter(filename);
            BufferedWriter bw = new BufferedWriter(fwrite);
            bw.write(text);
            bw.flush();
            bw.close();
            fwrite.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //判断开头字符是否合法
    public boolean isFirstLegal(char firstChar, String str) {
        char c;
        if (Character.isLetter(firstChar)) {
            return true;
        } else if (Character.isDigit(firstChar)){
            for (int i=1;i<str.length();i++){
                c = str.charAt(i);
                //如果是数字则继续
                if (Character.isDigit(c)) {
                }
                //如果遇到运算符
                else if ( c == '=' || c == '<' || c == '>' || c == '#') {
                    switch (c){
                        case '=':
                            return Character.isLetter(str.charAt(i + 1));
                        case '#':
                            return Character.isLetter(str.charAt(i + 1));
                        case '<':
                            return str.charAt(i + 1) == '=' || Character.isLetter(str.charAt(i + 1));
                        case '>':
                            return str.charAt(i + 1) == '=' || Character.isLetter(str.charAt(i + 1));
                    }
                } else if (Character.isLetter(c)) {
                    System.out.println("不合法标识符");
                    return false;
                }
            }
        } else return firstChar == '(' || firstChar == ')' ||
                    firstChar == ',' || firstChar == ';' ||
                    firstChar == '.' || firstChar == '+' || firstChar == '-';
        return true;
    }
}
