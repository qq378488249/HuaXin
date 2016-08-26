package cc.chenghong.huaxin.entity;

/**
 * Created by Administrator on 2016/3/14.
 */
public class Test {
    private int id;
    private int iv_id;
    private String str;
    private String str2;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public int getIv_id() {
        return iv_id;
    }

    public void setIv_id(int iv_id) {
        this.iv_id = iv_id;
    }

    public Test(int id, int iv_id, String str) {
        this.id = id;
        this.iv_id = iv_id;
        this.str = str;
    }

    public Test(int id, int iv_id, String str, String str2) {
        this.id = id;
        this.iv_id = iv_id;
        this.str = str;
        this.str2 = str2;
    }

    public String getStr2() {
        return str2;
    }

    public void setStr2(String str2) {
        this.str2 = str2;
    }
}
