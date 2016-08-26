package cc.chenghong.huaxin.entity;

import java.util.List;

import cc.chenghong.huaxin.response.ListResponse;

/**
 * Created by hcl on 2016/3/30.
 */
public class Fys  {

    private String total;

    private List<Allergy> data;

    private String id;

    private String p_id;

    private String title;

    private String status;

    private int is_select;

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
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public String getStatus(){
        return this.status;
    }
    public void setIs_select(int is_select){
        this.is_select = is_select;
    }
    public int getIs_select(){
        return this.is_select;
    }

    public List<Allergy> getData() {
        return data;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public void setData(List<Allergy> data) {
        this.data = data;
    }
}
