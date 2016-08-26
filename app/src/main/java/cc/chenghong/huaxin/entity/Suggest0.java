package cc.chenghong.huaxin.entity;

import java.util.List;

import cc.chenghong.huaxin.response.ListResponse;

/**健康预警
 * Created by Administrator on 2016/3/14.
 */
public class Suggest0 extends ListResponse<Suggest0>{
    private String id;

    private String user_id;

    private String content;

    private String created;

    private String status;

    private int type;

    private int value;

    private boolean isSelect = false;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
