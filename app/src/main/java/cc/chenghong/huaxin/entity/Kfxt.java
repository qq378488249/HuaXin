package cc.chenghong.huaxin.entity;

import java.io.Serializable;

import cc.chenghong.huaxin.response.ListResponse;

/**
 * Created by Administrator on 2016/3/23.
 */
public class Kfxt extends ListResponse<Kfxt> {
    private int id;
    private String user_id;
    private String value;
    private String created;
    private int status;
    private boolean isDelete;
    private boolean isSelect;
    private String time;
    private double xt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getXt() {
        return xt;
    }

    public void setXt(double xt) {
        this.xt = xt;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setIsDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }
}
