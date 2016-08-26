package cc.chenghong.huaxin.entity;

/**
 * Created by Administrator on 2016/3/18.
 */
public class XtData {
    private String id;

    private String user_id;

    private String xt;//血糖

    private String tz;//体脂

    private String created;

    private String time_name;

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
    public void setXt(String xt){
        this.xt = xt;
    }
    public String getXt(){
        return this.xt;
    }
    public void setCreated(String created){
        this.created = created;
    }
    public String getCreated(){
        return this.created;
    }
    public void setTime_name(String time_name){
        this.time_name = time_name;
    }
    public String getTime_name(){
        return this.time_name;
    }
}
