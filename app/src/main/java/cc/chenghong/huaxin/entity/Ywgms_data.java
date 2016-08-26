package cc.chenghong.huaxin.entity;

import java.io.Serializable;

/** 药物过敏史实体类
 * Created by hcl on 2016/3/29.
 */
public class Ywgms_data implements Serializable{
    private String id;

    private String user_id;

    private String created;

    private String status;

    private String is_custom;

    private String yyxx_ywgms_id;

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
    public void setYyxx_ywgms_id(String yyxx_ywgms_id){
        this.yyxx_ywgms_id = yyxx_ywgms_id;
    }
    public String getYyxx_ywgms_id(){
        return this.yyxx_ywgms_id;
    }
}
