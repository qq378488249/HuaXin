package cc.chenghong.huaxin.entity;

import java.util.List;

import cc.chenghong.huaxin.response.ListResponse;

/**
 * Created by hcl on 2016/3/29.
 */
public class Ywgms extends ListResponse<Ywgms> {
    private String id;

    private String user_id;

    private String title;

    private String created;

    private String status;

    private String is_custom;

    private String is_select;


    public String getIs_select() {
        return is_select;
    }

    public void setIs_select(String is_select) {
        this.is_select = is_select;
    }

    private List<Ywgms_add> user_custom_data;//用户添加的药物过敏

    public List<Ywgms_add> getUser_custom_data() {
        return user_custom_data;
    }

    public void setUser_custom_data(List<Ywgms_add> user_custom_data) {
        this.user_custom_data = user_custom_data;
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
    public void setIs_custom(String is_custom){
        this.is_custom = is_custom;
    }
    public String getIs_custom(){
        return this.is_custom;
    }
}
