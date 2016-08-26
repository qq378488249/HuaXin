package cc.chenghong.huaxin.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/16.
 */
public class Organization implements Serializable{

    private String id;

    private String p_id;

    private String name;

    private String created;

    private String status;

    private String content;

    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
    public void setP_id(String p_id){
        this.p_id = p_id;
    }
    public String getP_id(){
        return this.p_id;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
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
    public void setContent(String content){
        this.content = content;
    }
    public String getContent(){
        return this.content;
    }
}
