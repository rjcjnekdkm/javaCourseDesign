package util;

import java.util.ArrayList;
import java.util.Random;

public class CodeUtil {

    public static String getCode(){
        //1.创建一个集合
        ArrayList<Character> list = new ArrayList<>();//52  索引的范围：0 ~ 51
        //添加字母 a - z  A - Z
        for (int i = 0; i < 26; i++) {
            list.add((char)('a' + i));//a - z
            list.add((char)('A' + i));//A - Z
        }

        //生成4个随机字母
        String result = "";
        Random r = new Random();
        for (int i = 0; i < 4; i++) {
            //获取随机索引
            int randomIndex = r.nextInt(list.size());
            char c = list.get(randomIndex);
            result = result + c;
        }

        //在后面拼接数字 0~9
        int number = r.nextInt(10);
        //把随机数字拼接到result的后面
        result = result + number;
        //把字符串变成字符数组
        char[] chars = result.toCharArray();//[A,B,C,D,5]
        //在字符数组中生成一个随机索引
        int index = r.nextInt(chars.length);
        char temp = chars[4];
        chars[4] = chars[index];
        chars[index] = temp;
        //1把字符数组再变回字符串
        String code = new String(chars);
        return code;
    }
}
