import util.FileUtil;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Analysis_identity {

    static char optSingle[] = {'+', '-', '*', '/', '<', '>', '=', '#'};
    static String optDouble[] = {":=", "<=", ">="};
    static char End[] = {';', ',', '(', ')'};
    static Set<String> keyWords = new HashSet<String>(Arrays.asList("begin", "call", "const", "do", "end",
            "if", "odd", "procedure", "read", "then",
            "var", "while", "write"));
    static String code[] = {
            "beginsym", "callsym", "constsym", "dosym", "endsym",
            "ifsym", "oddsym", "proceduresym", "readsym", "thensym",
            "varsym", "whilesym", "writesym"
    };
    static Map<String, Integer> indent = new HashMap<String, Integer>();


    public void writeFIle(String filename, Map<String, Integer> idents) throws IOException {
        FileWriter fwrite = new FileWriter(filename);
        BufferedWriter bw = new BufferedWriter(fwrite);
        for (String keyword : idents.keySet()) {
            String strLine = "(" + keyword + ": " + idents.get(keyword) + ")";
            bw.write(strLine);
            bw.newLine();
        }
        bw.flush();
        bw.close();
        fwrite.close();
    }

    //将整行的字符串进行划分
    public void SplitWord(String str, Map<String, Integer> ident) {
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
            return;
        }
        //判断出现的其他符号
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        //从头开始遍历
        for (int i=0;i<str.length();i++) {
            c = str.charAt(i);
            Matcher m = p.matcher(String.valueOf(c));
            if (Character.isLetter(c)) {
                i = letter(str, ident ,i);
            } else if (Character.isDigit(c)){
                i = digit(str, ident, i);
            }
        }
    }

    private int opt(String str, Map<String, Integer> ident, int i) {
        return i;
    }

    private int digit(String str, Map<String, Integer> ident, int i) {
        int temp = i++;
        if (temp == str.length()) {
            return i;
        }
        while (temp < str.length()) {
            if (Character.isDigit(str.charAt(temp))) {
                temp++;
            } else return temp;
        }
        return --temp;
    }

    //单词分析，是否关键词还是标识符
    private int letter(String str, Map<String, Integer> ident, int i) {
        int temp = i++;
        //如果已经到字符串最后一个，那么直接将该标识符放入map
        if (temp == str.length()) {
            String word = String.valueOf(str.charAt(temp-1));
            if (indent.containsKey(word))
                ident.put(String.valueOf(word), ident.get(word)+1);
            else
                ident.put(String.valueOf(str.charAt(temp-1)), 1);
            return i;
        }
        //整行循环检测
        while (temp < str.length()){
            if (Character.isLetterOrDigit(str.charAt(temp))){
                temp++;
            } else {
                String word = str.substring(i-1, temp).toLowerCase();
                if (keyWords.contains(word)) return --temp;
                if (ident.containsKey(word)) {
                    ident.put(word, ident.get(word)+1);
                } else {
                    ident.put(word, 1);
                }
                return --temp;
            }
        }
        String word = String.valueOf(str.substring(i-1,temp)).toLowerCase();
        if (keyWords.contains(word)) return --temp;
        if (ident.containsKey(word)) {
            ident.put(word, ident.get(word)+1);
        } else {
            ident.put(word, 1);
        }
        return --temp;
    }

    public static void main(String[] args) {
        Analysis_identity analysis_identity = new Analysis_identity();
        FileUtil fileUtil = new FileUtil();
        String filename = "E:\\SHU\\Compiler\\analysis\\src\\text\\Test1.txt";
        String outfile = "E:\\SHU\\Compiler\\analysis\\src\\text\\output.txt";
        Map<String, Integer> idents = new HashMap<String, Integer>();
        List<String> strlist = new ArrayList<String>();
        try {
             strlist = fileUtil.readFile(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i=0;i<strlist.size();i++){
            analysis_identity.SplitWord(strlist.get(i), idents);
        }
        try {
            analysis_identity.writeFIle(outfile, idents);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        for(String key : idents.keySet()) {
//            System.out.println("(" + key +": " + idents.get(key) + ")");
//        }
    }
}
