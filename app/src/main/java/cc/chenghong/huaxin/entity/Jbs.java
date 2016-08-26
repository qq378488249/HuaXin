package cc.chenghong.huaxin.entity;

import java.util.List;

import cc.chenghong.huaxin.response.ListResponse;

/** 疾病史
 * Created by Administrator on 2016/3/24.
 */
public class Jbs extends ListResponse<Jbs>{
    private String id;

    private String p_id;

    private String title;

    private String status;

    private String user_select_data ;

    private int user_select_count;

    private String name;

    private int count;

    private List<Allergy> son_data;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getP_id() {
        return p_id;
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

    public String getUser_select_data() {
        return user_select_data;
    }

    public void setUser_select_data(String user_select_data) {
        this.user_select_data = user_select_data;
    }

    public int getUser_select_count() {
        return user_select_count;
    }

    public void setUser_select_count(int user_select_count) {
        this.user_select_count = user_select_count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Allergy> getSon_data() {
        return son_data;
    }

    public void setSon_data(List<Allergy> son_data) {
        this.son_data = son_data;
    }
}
