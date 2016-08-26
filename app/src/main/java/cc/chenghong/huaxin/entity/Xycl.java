package cc.chenghong.huaxin.entity;

import cc.chenghong.huaxin.response.ObjectResponse;

/** 血糖测量，血压测量，体脂测量实体类
 * Created by hcl on 2016/4/15.
 */
public class Xycl extends ObjectResponse<Xycl>{

    private String id;

    private String user_id;

    private String tz;

    private String xt;

    private String time_name;

    private String xl;

    private String ssy;

    private String szy;

    private String created;

    public void setId(String id){
        this.id = id;
    }

    public String getXt() {
        return xt;
    }

    public void setXt(String xt) {
        this.xt = xt;
    }

    public String getTime_name() {
        return time_name;
    }

    public void setTime_name(String time_name) {
        this.time_name = time_name;
    }

    public String getXl() {
        return xl;
    }

    public void setXl(String xl) {
        this.xl = xl;
    }

    public String getSsy() {
        return ssy;
    }

    public void setSsy(String ssy) {
        this.ssy = ssy;
    }

    public String getSzy() {
        return szy;
    }

    public void setSzy(String szy) {
        this.szy = szy;
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
    public void setTz(String tz){
        this.tz = tz;
    }
    public String getTz(){
        return this.tz;
    }
    public void setCreated(String created){
        this.created = created;
    }
    public String getCreated(){
        return this.created;
    }
}
