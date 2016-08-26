package cc.chenghong.huaxin.entity;

import java.io.Serializable;

/**
 * 药物过敏史实体类
 * Created by thin blue on 2016/2/29.
 */
public class Allergy implements Serializable{

    private String p_id;

    private String title;

    private String status;
    private String is_select;
    private int id;//编号
    private String name;//姓名
    private boolean isSelect;//是否选中

    public String getP_id() {
        return p_id;
    }

    public String getIs_select() {
        return is_select;
    }

    public void setIs_select(String is_select) {
        this.is_select = is_select;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    public Allergy(int id, String name, boolean isSelect) {
        this.id = id;
        this.name = name;
        this.isSelect = isSelect;
    }

    public Allergy(String is_select, String title) {
        this.is_select = is_select;
        this.title = title;
    }

    public Allergy(boolean isSelect, String name) {
        this.isSelect = isSelect;
        this.name = name;
    }

}
