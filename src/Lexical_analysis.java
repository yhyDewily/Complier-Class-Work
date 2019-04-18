//词法分析
import util.FileUtil;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexical_analysis {

    static List<String> analysis = new ArrayList<>();

    static Map<String, String> keyWords = new HashMap<String, String>();
    static Map<String, String> OPERATION = new HashMap<String, String>();
    static Map<String, String> DELIMITER = new HashMap<String, String>() ;

    static {
        keyWords.put("begin", "beginsym");
        keyWords.put("call", "callsym");
        keyWords.put("const", "constsym");
        keyWords.put("do", "dosym");
        keyWords.put("end", "endsym");
        keyWords.put("if", "ifsym");
        keyWords.put("odd", "oddsym");
        keyWords.put("procedure", "proceduresym");
        keyWords.put("read", "readsym");
        keyWords.put("then", "thensym");
        keyWords.put("var", "varsym");
        keyWords.put("while", "whilesym");
        keyWords.put("write", "writesym");
        OPERATION.put("+", "plus");
        OPERATION.put("-", "minus");
        OPERATION.put("*", "times");
        OPERATION.put("/", "slash");
        OPERATION.put("=", "eql");
        OPERATION.put("#", "neq");
        OPERATION.put("<", "lss");
        OPERATION.put("<=", "leq");
        OPERATION.put(">", "gtr");
        OPERATION.put(">=", "geq");
        OPERATION.put(":=", "becomes");
        DELIMITER.put("(", "Lparen");
        DELIMITER.put(")", "Rparen");
        DELIMITER.put(",", "comma");
        DELIMITER.put(";", "semicolon");
        DELIMITER.put(".", "period");
    }

    private int opt(String str, int i) {
        int temp = i+1;
        String opt = String.valueOf(str.charAt(i));
        if (isDelimiter(opt)){
            return i;
        } else if (OPERATION.containsKey(opt) || opt.equals(":")){
            switch (opt) {
                case "<":
                    if (str.charAt(temp) == '=') {
                        System.out.println("(leq, <=)");
                        analysis.add("(leq, <=)");
                        return temp;
                    } else {
                        System.out.println("(lss, <)");
                        analysis.add("(lss, <)");
                        return i;
                    }
                case ">":
                    if (str.charAt(temp) == '=') {
                        System.out.println("(leq, >=)");
                        analysis.add("(leq, >=)");
                        return temp;
                    } else {
                        System.out.println("(lss, >)");
                        analysis.add("(lss, >)");
                        return i;
                    }
                case ":":
                    if (str.charAt(temp) == '=') {
                        System.out.println("(becomes, :=)");
                        analysis.add("(becomes, :=)");
                        return temp;
                    } else {
                        System.out.println("不合法符号");
                        System.exit(1);
                    }
                default:
                    System.out.println("(" + OPERATION.get(opt) + ", " + opt + ")");
                    analysis.add("(" + OPERATION.get(opt) + ", " + opt + ")");
                    return i;
            }

        } else {
            System.out.println("不合法符号");
            System.exit(1);
        }
        return i;
    }

    private boolean isDelimiter(String opt) {
        if (DELIMITER.containsKey(opt)){
            switch (opt){
                case "(":
                    System.out.println("(" + DELIMITER.get(opt) + ", " +  opt + ")");
                    analysis.add("(" + DELIMITER.get(opt) + ", " +  opt + ")");
                    return true;
                case ")":
                    System.out.println("(" + DELIMITER.get(opt) + ", " +  opt + ")");
                    analysis.add("(" + DELIMITER.get(opt) + ", " +  opt + ")");
                    return true;
                case ";":
                    System.out.println("(" + DELIMITER.get(opt) + ", " +  opt + ")");
                    analysis.add("(" + DELIMITER.get(opt) + ", " +  opt + ")");
                    return true;
                case ".":
                    System.out.println("(" + DELIMITER.get(opt) + ", " +  opt + ")");
                    analysis.add("(" + DELIMITER.get(opt) + ", " +  opt + ")");
                    return true;
                case ",":
                    System.out.println("(" + DELIMITER.get(opt) + ", " +  opt + ")");
                    analysis.add("(" + DELIMITER.get(opt) + ", " +  opt + ")");
                    return true;
            }
        }
        return false;
    }

    private int digit(String str, int i) {
        int temp = i+1;
        Pattern p = Pattern.compile("[+\\-*/><=#;),]");
        if (temp == str.length()) {
            System.out.println("(" + "number, " + str.charAt(i) + ")");
            analysis.add("(" + "number, " + str.charAt(i) + ")");
            return i;
        }
        while (temp < str.length()) {
            Matcher m = p.matcher(String.valueOf(str.charAt(temp)));
            if (Character.isDigit(str.charAt(temp))) {
                temp++;
            } else if(Character.isLetter(str.charAt(temp))) {
                System.out.println("不合法标识符");
                System.exit(1);
            } else {
                System.out.println("(" + "number, " + str.substring(i, temp) + ")");
                analysis.add("(" + "number, " + str.substring(i, temp) + ")");
                return --temp;
            }

        }
        System.out.println("(" + "number, " + str.substring(i, temp) + ")");
        analysis.add("(" + "number, " + str.substring(i, temp) + ")");
        return --temp;
    }

    //单词分析，是否关键词还是标识符
    private int letter(String str, int i) {
        int temp = i++;
        //如果已经到字符串最后一个，那么直接将该标识符放入map
        if (i == str.length()) {
            String word = String.valueOf(str.charAt(temp));
            String string = "(" + "ident, " + word + ")";
            System.out.println(string);
            analysis.add(string);
            return i;
        }
        //整行循环检测
        while (temp < str.length()){
            if (Character.isLetterOrDigit(str.charAt(temp))){
                temp++;
            } else {
                String word = str.substring(i-1, temp).toLowerCase();
                if (keyWords.containsKey(word)) {
                    System.out.println("(" + keyWords.get(word) + ", " + word + ")");
                    analysis.add(("(" + keyWords.get(word) + ", " + word + ")"));
                } else {
                    System.out.println("(" + "ident, " + word + ")");
                    analysis.add("(" + "ident, " + word + ")");
                }
                return --temp;
            }
        }
        String word = String.valueOf(str.substring(i-1,temp)).toLowerCase();
        if (keyWords.containsKey(word)) {
            System.out.println("(" + keyWords.get(word) + ", " + word + ")");
            analysis.add(("(" + keyWords.get(word) + ", " + word + ")"));
        } else {
            System.out.println("(" + "ident, " + word + ")");
            analysis.add(("(" + "ident, " + word + ")"));
        }
        return --temp;
    }

    public void SplitWord(String str) {
        //如果是空行直接返回
        if (str.trim().length() == 0) {
            return;
        }
        List<String> list = new ArrayList<>();
        //去除空格
        str = str.trim();
        //定义c用来遍历字符串的单词， firstChar用来判断字符串开头是否合法(
        char c, firstChar = str.charAt(0);
        FileUtil fu = new FileUtil();
        if (!fu.isFirstLegal(firstChar, str)) {
            System.out.println("有不合法标识符");
            System.exit(1);
        }
        //判断出现的其他符号
//        String regEx = "[_`~!@#$%^&*()+=|{}':;',\\-\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
//        Pattern p = Pattern.compile(regEx);
        //从头开始遍历
        for (int i=0;i<str.length();i++) {
            c = str.charAt(i);
//            Matcher m = p.matcher(String.valueOf(c));
            if (Character.isLetter(c)) {
                i = letter(str ,i);
            } else if (Character.isDigit(c)){
                i = digit(str, i);
            } else if (OPERATION.containsKey(String.valueOf(c)) ||
                    DELIMITER.containsKey(String.valueOf(c)) ||
                    c == ':') {
                i = opt(str, i);
            }
        }
    }

    public static void main(String args[]) {
        Lexical_analysis la = new Lexical_analysis();
        FileUtil fileUtil = new FileUtil();
        String series = "1";
        String infile = "E:\\SHU\\Compiler\\analysis\\src\\parsing_test_file\\text\\Text"+series+".txt";
        String outfile = "E:\\SHU\\Compiler\\analysis\\src\\parsing_test_file\\test\\Test"+series+".txt";
        List<String> strList = new ArrayList<String>();
        try {
            strList = fileUtil.readFile(infile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i=0;i<strList.size();i++) {
            la.SplitWord(strList.get(i));
        }
        FileWriter fwrite = null;
        try {
            fwrite = new FileWriter(outfile);
            BufferedWriter bw = new BufferedWriter(fwrite);
            for (String string : analysis) {
                bw.write(string);
                bw.newLine();
            }
            bw.flush();
            bw.close();
            fwrite.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
