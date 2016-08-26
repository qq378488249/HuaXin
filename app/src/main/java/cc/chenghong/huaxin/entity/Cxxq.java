package cc.chenghong.huaxin.entity;

import cc.chenghong.huaxin.response.ObjectResponse;

/** 促销详情
 * Created by hcl on 2016/5/6.
 */
public class Cxxq extends ObjectResponse<Cxxq>{
    private String id;

    private String user_id;

    private String title;

    private String content;

    private String created;

    private String status;

    private String small_titile;

    private String img_url;

    private String view_count;

    public String getView_count() {
        return view_count;
    }

    public void setView_count(String view_count) {
        this.view_count = view_count;
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
    public void setContent(String content){
        this.content = content;
    }
    public String getContent(){
        return this.content;
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
    public void setSmall_titile(String small_titile){
        this.small_titile = small_titile;
    }
    public String getSmall_titile(){
        return this.small_titile;
    }
    public void setImg_url(String img_url){
        this.img_url = img_url;
    }
    public String getImg_url(){
        return this.img_url;
    }

}
