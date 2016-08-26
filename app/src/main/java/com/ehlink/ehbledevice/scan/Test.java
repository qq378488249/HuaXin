package com.ehlink.ehbledevice.scan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hcl on 2016/5/11.
 */
public class Test {
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String str = "26 44 5a 20 37 20 1e" +
                " 30 33 30 32 31 36 30 32 34 33 20 38 31 20 30 20 32 39 39 31 20 31 33 33 20 31 34 34 20 1e" +
                " 30 33 30 31 31 36 32 31 33 33 20 31 35 30 20 30 20 33 30 37 34 20 32 34 31 20 31 34 34 20 1e" +
                " 30 33 30 31 31 36 32 30 32 37 20 31 30 31 20 30 20 32 39 38 36 20 31 36 32 20 31 34 34 20 1e" +
                " 30 33 30 31 31 36 31 38 35 38 20 38 33 20 30 20 33 30 30 30 20 31 33 36 20 31 34 34 20 1e" +
                " 30 32 32 39 31 36 32 31 34 37 20 38 34 20 30 20 33 31 39 36 20 31 34 36 20 31 34 34 20 1e" +
                " 30 32 32 39 31 36 32 30 34 31 20 31 34 34 20 30 20 33 35 34 30 20 32 36 32 20 31 34 34 20 1e" +
                " 30 32 32 39 31 36 31 39 35 36 20 31 33 31 20 30 20 33 35 37 30 20 32 34 32 20 31 34 34 20 1e" +
                " 06 35 37 30 36 39 0d ";
        String value = str.replace(" ", "");
        value = value.substring(14, value.length() - 14);
        String[] result = value.split("1e");
//        System.out.println("\n");
//        System.out.println(value);
        List<Xt> list = new ArrayList<Xt>();
//        string2Xt("03021602430831002991013301440");
        for (int i = 0; i < result.length; i++) {
            System.out.println(result[i]);
            System.out.println(toString(result[i]));
            System.out.println(string2Xt(toString(result[i])));
        }

    }

    static String toString(String str) {
        char[] bytes = str.toCharArray();
        String result = "";
        for (int i = 0; i < bytes.length; i++) {
//            if ((bytes[i] == '2' && bytes[i + 1] == '0') || (bytes[i] == '3' && bytes[i + 1] == '0')) {//20,30不用解析
//                continue;
//            } else {
            if (i % 2 == 0) {
                continue;
            } else {
                if ((bytes[i] == '2' && bytes[i + 1] == '0')) {//20不用解析
                    continue;
                }
                result += bytes[i];
            }
//            }
        }
        return result;
    }

    static String string2Xt(String s) {
        Xt xt = new Xt();
        xt.time = "20" + s.substring(4, 6) + "-" + s.substring(0, 2) + "-" + s.substring(2, 4) + " " + s.substring(6, 8) + ":" + s.substring(8, 10);
        xt.xt = (Double.valueOf(s.substring(10, 13)) / 18) + "";
        String s2 = Double.valueOf(s.substring(10, 13)) / 18 + "";
        String[] s1 = s2.split("\\.");
        if (s1.length > 1) {
            xt.xt = s1[0] + "." + s1[1].substring(0, 1);
        }
        //测量时间段
        String timeName = s.substring(13, 15);
        if (timeName.equals("00")) {
            xt.time_name = "餐前";
        } else if (timeName.equals("10")) {
            xt.time_name = "餐后";
        } else if (timeName.equals("12")) {
            xt.time_name = "餐前";
        } else if (timeName.equals("40")) {
            xt.time_name = "无效记录";
        } else{
            xt.time_name = "无效记录";
        }
        return xt.toString();
//        System.out.println(xt);
    }
}
