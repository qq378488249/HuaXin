package cc.chenghong.huaxin.entity;

import java.io.Serializable;

/** 咨询详情内容实体类
 * Created by hcl on 2016/3/28.
 */
public class Reply_data implements Serializable {
    private String id;

    private String user_consultation_id;

    private String type;

    private String content;

    private String created;

    private String img_url;

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
    public void setUser_consultation_id(String user_consultation_id){
        this.user_consultation_id = user_consultation_id;
    }
    public String getUser_consultation_id(){
        return this.user_consultation_id;
    }
    public void setType(String type){
        this.type = type;
    }
    public String getType(){
        return this.type;
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
}
