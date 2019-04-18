import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * @program: projectTest1
 * @description:
 * @author: lyh
 * @create: 2019-04-01 15:15
 **/
public class Test {
    static void splitword(List<String> word){//Vector<String> word
        String temp = null;
        try{
            InputStreamReader isr = new InputStreamReader(new FileInputStream("E:\\SHU\\Compiler\\analysis\\src\\parsing_test_file\\test\\Test0.txt"), "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String lineTxt;
            while ((lineTxt = br.readLine()) != null) {
                int flag = 0;
                int len = lineTxt.length();
                for(int i=0;i < len;i++){
                    char c = lineTxt.charAt(i);
                    if(((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))){
                        if(c <= 'Z'){
                            c -= -32;
                        }
                        if(flag != 1 && flag !=2){
                            word.add(temp);
                            temp = "";
                        }
                        temp = temp + c;
                        flag = 1;
                    }
                    else if((c >= '0' && c <= '9')){
                        if(flag != 1 && flag !=2){
                            word.add(temp);
                            temp = "";
                        }
                        temp = temp + c;
                        flag = 2;
                    }
                    else if(c == ' ' || c == '\t'){
                        flag = 0;
                    }
                    else {
                        if(flag != 3 || c != '='){
                            word.add(temp);
                            temp = "";
                        }
                        temp = temp + c;
                        flag = 3;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        List<String> reserved_word = new ArrayList<>();  //初始化保留字
        reserved_word.add("begin");
        reserved_word.add("call");
        reserved_word.add("const");
        reserved_word.add("do");
        reserved_word.add("end");
        reserved_word.add("if");
        reserved_word.add("odd");
        reserved_word.add("procedure");
        reserved_word.add("read");
        reserved_word.add("then");
        reserved_word.add("var");
        reserved_word.add("while");
        reserved_word.add("write");

        //分离出的词
        List<String> words = new ArrayList<>();
        splitword(words);

        Map<String,Integer> result_word = new HashMap<>(); //提取标识符和出现次数
        Pattern p = Pattern.compile("[0-9]*[A-Za-z]+[0-9]*");
        for(String str : words){
            if(str != null){
                Matcher m = p.matcher(str);
                boolean isValid = m.matches();
                int key=0;
                if(isValid){
                    Integer times = 0;
                    for(String str1 : reserved_word){
                        if(str.equals(str1))
                            break;
                        key++;
                    }
                    if(key == 13){
                        for(String str2 : words){
                            if(str.equals(str2))
                                times++;
                        }
                        result_word.put(str,times);
                    }
                }
            }
        }
        try {
            FileOutputStream fos = new FileOutputStream("E:\\SHU\\Compiler\\analysis\\src\\text\\output.txt");
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            osw.write(result_word.toString() + "\n");
            osw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.print(result_word + "\n");
    }
}

