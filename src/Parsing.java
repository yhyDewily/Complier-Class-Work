//语法分析
import util.FileUtil;

import java.io.IOException;
import java.util.*;

public class Parsing {

    private static FileUtil fileUtil = new FileUtil();

    private static List<String> startToken;
    private static List<String> token;
    private static Map<String, String> predictmap;
    private static List<String> tokenList;   //存储词法分析结果得到的编码
    private static String sym;  //用于遍历输入的词法分析结果的每个编码
    private static int sym_index =0;     //遍历tokenList的索引

    Parsing () {
        tokenList = new ArrayList<String>();
        sym = "";
        token = new ArrayList<>(Arrays.asList(
                "beginsym", "callsym", "constsym", "dosym", "endsym",
                "ifsym", "oddsym", "proceduresym", "readsym", "thensym",
                "varsym", "whilesym", "writesym","plus","minus", "times",
                "slash", "eql", "neq", "lss", "leq", "gtr", "geq", "becomes",
                "Lparen", "Rparen", "comma", "semicolon", "period", "number", "ident"
        ));
        startToken = new ArrayList<>(Arrays.asList(
                "Lparen", "ident", "number", "plus", "minus"
        ));
        predictmap = new HashMap<>();
        predictmap.put("plus", "opt");
        predictmap.put("minus", "opt");
        predictmap.put("times", "opt");
        predictmap.put("slash", "opt");
        predictmap.put("eql", "opt");
        predictmap.put("neq", "opt");
        predictmap.put("lss", "opt");
        predictmap.put("leq", "opt");
        predictmap.put("gtr", "opt");
        predictmap.put("geq", "opt");
        predictmap.put("becomes", "opt");
        predictmap.put("Lparen", "delimiter");
        predictmap.put("Rparen", "delimiter");
        predictmap.put("comma", "delimiter");
        predictmap.put("semicolon", "delimiter");
        predictmap.put("period", "delimiter");
        predictmap.put("number", "number");
        predictmap.put("ident", "ident");
    }


    private String getToken(String string) {
        String temp = string.replace("(", "");
        List<String> list = Arrays.asList(temp.split(","));
        return list.get(0);
    }

    private static boolean isToken(String string) {
        return token.contains(string);
    }

    private void getSym(){
        if (sym_index < tokenList.size()){
            sym = tokenList.get(sym_index++);
        } else {
            fileUtil.outputFile("越界");
            System.out.println("越界");
            System.exit(1);
        }
    }

    private int expression() {
        if(sym.equals("plus") || sym.equals("minus")) {  //处理 [+|-]
            getSym();
            if (predictmap.get(sym).equals("opt") || predictmap.get(sym).equals("delimiter")) {
                fileUtil.outputFile("表达式开头+|-后面出错");
                System.out.println("表达式开头+|-后面出错");
                System.exit(1);
            }
            term();                //处理<项>
        } else {
            term();                //处理<项>
        }
        while (sym.equals("plus") || sym.equals("minus")) {
            if (sym_index >= tokenList.size()) {
                fileUtil.outputFile("+ | -后没有标识符或数字");
                System.out.println("+ | -后没有标识符或数字");
                System.exit(1);
            }
            getSym();
            term();
        }
        return 0;
    }

    private int term() {
        factor();     //处理<因子>
        while (sym.equals("times") || sym.equals("slash")) {
            getSym();
            if (sym.equals("times") || sym.equals("slash") ||
                sym.equals("plus") || sym.equals("minus")) {
                fileUtil.outputFile("<项>里面出现多余的运算符号");
                System.out.println("<项>里面出现多余的运算符号");
                System.exit(1);
            }
            factor();
        }
        return 0;
    }

    private int factor() {
        if (sym.equals("ident")) {
            if (sym_index == tokenList.size()) return 0;
            getSym();
        } else if (sym.equals("number")) {
            if (sym_index == tokenList.size()) return 0;
            getSym();
        } else if (sym.equals("Lparen")) {
            getSym();
            expression();
            if(sym.equals("Rparen")) {
                if (sym_index == tokenList.size()) return 0;
                getSym();
                return 0;
            }
            else {
                fileUtil.outputFile("缺少右括号");
                System.out.println("缺少右括号");
                System.exit(1);
            }
        } else {
            fileUtil.outputFile("+ | -号后面未跟数字或标识符");
            System.out.println("+ | -号后面未跟数字或标识符");
            System.exit(1);
        }
        return 0;
    }

    public static void main(String args[]) {
        Parsing parsing = new Parsing();
        String infile = "E:\\SHU\\Compiler\\analysis\\src\\parsing_test_file\\test\\Test1.txt";
        List<String> strList = new ArrayList<String>();
        try {
            strList = fileUtil.readFile(infile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String string : strList) {
            String temp = parsing.getToken(string);
            if (isToken(temp))
            tokenList.add(parsing.getToken(string));
            else {
                System.out.println("有不合法编码");
                System.exit(1);
            }
        }
        parsing.getSym();
        if (parsing.expression() == 0) {
            fileUtil.outputFile("语法正确");
            System.out.println("语法正确");
        }
    }



}
