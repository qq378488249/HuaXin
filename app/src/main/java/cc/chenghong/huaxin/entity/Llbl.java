package cc.chenghong.huaxin.entity;

import cc.chenghong.huaxin.response.ListResponse;

/** 浏览病例实体类
 * Created by hcl on 2016/3/30.
 */
public class Llbl extends ListResponse<Llbl>{
    private String id;

    private String user_id;

    private String title;

    private String created;

    private String status;

    private String time;

    private String img_url;

    private boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

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
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }
    public void setCreated(String created){
        this.created = created;
    }
    public String getCreated(){
        return this.created;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public String getStatus(){
        return this.status;
    }
    public void setTime(String time){
        this.time = time;
    }
    public String getTime(){
        return this.time;
    }
    public void setImg_url(String img_url){
        this.img_url = img_url;
    }
    public String getImg_url(){
        return this.img_url;
    }
}
