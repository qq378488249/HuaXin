package cc.chenghong.huaxin.entity;

/**血压
 * Created by Administrator on 2016/3/3.
 */
public class Pressure {
    private int id;
    private String str1;
    private String str2;
    private String str3;
    private String time;
    private String str7;

    public Pressure(int id, String str1, String str2, String str3,String time, String str7) {
        this.id = id;
        this.str1 = str1;
        this.str7 = str7;
        this.time = time;
        this.str2 = str2;
        this.str3 = str3;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStr1() {
        return str1;
    }

    public void setStr1(String str1) {
        this.str1 = str1;
    }

    public String getStr2() {
        return str2;
    }

    public void setStr2(String str2) {
        this.str2 = str2;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Pressure(int id, String str1, String str2, String time) {
        this.id = id;
        this.str1 = str1;
        this.str2 = str2;
        this.time = time;
    }

    public String getStr3() {
        return str3;
    }

    public String getStr7() {
        return str7;
    }
}
