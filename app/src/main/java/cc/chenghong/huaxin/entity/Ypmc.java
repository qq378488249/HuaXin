package cc.chenghong.huaxin.entity;

import java.io.Serializable;

import cc.chenghong.huaxin.response.ListResponse;

/**
 * Created by hcl on 2016/3/29.
 */
public class Ypmc extends ListResponse<Ypmc> {
    private String id;

    private String title;

    private String created;

    private String status;

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
}
