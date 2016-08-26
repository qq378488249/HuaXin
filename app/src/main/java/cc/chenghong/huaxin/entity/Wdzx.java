package cc.chenghong.huaxin.entity;

import cc.chenghong.huaxin.response.ListResponse;

/** 我的咨询实体类
 * Created by hcl on 2016/3/28.
 */
public class Wdzx extends ListResponse<Wdzx>{
    private String id;

    private String user_id;

    private String employee_id;

    private String title;

    private String content;

    private String img_url;

    private String created;

    private String is_do;

    private String status;

    private int reply_count;

    private Employee employee;

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
    public void setEmployee_id(String employee_id){
        this.employee_id = employee_id;
    }
    public String getEmployee_id(){
        return this.employee_id;
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
    public void setImg_url(String img_url){
        this.img_url = img_url;
    }
    public String getImg_url(){
        return this.img_url;
    }
    public void setCreated(String created){
        this.created = created;
    }
    public String getCreated(){
        return this.created;
    }
    public void setIs_do(String is_do){
        this.is_do = is_do;
    }
    public String getIs_do(){
        return this.is_do;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public String getStatus(){
        return this.status;
    }
    public void setReply_count(int reply_count){
        this.reply_count = reply_count;
    }
    public int getReply_count(){
        return this.reply_count;
    }
    public void setEmployee(Employee employee){
        this.employee = employee;
    }
    public Employee getEmployee(){
        return this.employee;
    }
}
