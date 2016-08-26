package cc.chenghong.huaxin.entity;

import java.io.Serializable;

/**
 * Created by hcl on 2016/3/31.
 */
public class JktsContent implements Serializable {

    private String id;

    private String user_id;

    private String message_jkts_id;

    private String created;

    private String content;

    private User user;

    private String img_url;

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
    public void setMessage_jkts_id(String message_jkts_id){
        this.message_jkts_id = message_jkts_id;
    }
    public String getMessage_jkts_id(){
        return this.message_jkts_id;
    }
    public void setCreated(String created){
        this.created = created;
    }
    public String getCreated(){
        return this.created;
    }
    public void setContent(String content){
        this.content = content;
    }
    public String getContent(){
        return this.content;
    }
}
