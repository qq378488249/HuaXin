package cc.chenghong.huaxin.entity;

import java.util.List;

import cc.chenghong.huaxin.response.ObjectResponse;

/**
 * Created by hcl on 2016/3/31.
 */
public class Jkts extends ObjectResponse<Jkts> {
    private String id;

    private String title;

    private String content;

    private String created;

    private String status;

    private String img_url;

    private String small_title;

    private int view_count;

    private int like_count;

    private List<JktsContent> user_comment ;

    private String is_like;//是否点赞

    public String getIs_like() {
        return is_like;
    }

    public void setIs_like(String is_like) {
        this.is_like = is_like;
    }

    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
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
    public void setImg_url(String img_url){
        this.img_url = img_url;
    }
    public String getImg_url(){
        return this.img_url;
    }
    public void setSmall_title(String small_title){
        this.small_title = small_title;
    }
    public String getSmall_title(){
        return this.small_title;
    }

    public int getView_count() {
        return view_count;
    }

    public void setView_count(int view_count) {
        this.view_count = view_count;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public List<JktsContent> getUser_comment() {
        return user_comment;
    }

    public void setUser_comment(List<JktsContent> user_comment) {
        this.user_comment = user_comment;
    }
}
