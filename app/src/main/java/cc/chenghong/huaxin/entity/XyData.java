package cc.chenghong.huaxin.entity;

/**
 * Created by Administrator on 2016/3/18.
 */
public class XyData {

    private String id;

    private String user_id;

    private String xl;//心率

    private String created;//创建时间

    private String ssy;//收缩压

    private String szy;//舒张压

    private float xt;//血糖

    private String time_name;//血糖测量时间

    private float tz;//体脂

    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
    public void setUser_id(String user_id){
        this.user_id = user_id;
    }
    public String getUser_id(){
        return this.user_id;
    }
    public void setXl(String xl){
        this.xl = xl;
    }
    public String getXl(){
        return this.xl;
    }
    public void setCreated(String created){
        this.created = created;
    }
    public String getCreated(){
        return this.created;
    }
    public void setSsy(String ssy){
        this.ssy = ssy;
    }
    public String getSsy(){
        return this.ssy;
    }
    public void setSzy(String szy){
        this.szy = szy;
    }
    public String getSzy(){
        return this.szy;
    }

    public String getTime_name() {
        return time_name;
    }

    public void setTime_name(String time_name) {
        this.time_name = time_name;
    }

    public float getXt() {
        return xt;
    }

    public void setXt(float xt) {
        this.xt = xt;
    }

    public float getTz() {
        return tz;
    }

    public void setTz(float tz) {
        this.tz = tz;
    }
}
