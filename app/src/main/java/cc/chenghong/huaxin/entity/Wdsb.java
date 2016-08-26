package cc.chenghong.huaxin.entity;

import cc.chenghong.huaxin.response.ListResponse;

/** 我的设备实体类
 * Created by Administrator on 2016/3/24.
 */
public class Wdsb extends ListResponse<Wdsb> {

    /**
     * id : 2
     * name : XX健康，挂壁式血糖仪
     * im_url :
     * status : 1
     * content :
     * is_select :
     * equipment_no :
     */

    private String id;
    private String name;
    private String im_url;
    private String status;
    private String content;
    private String is_select;
    private String equipment_no;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIm_url() {
        return im_url;
    }

    public void setIm_url(String im_url) {
        this.im_url = im_url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIs_select() {
        return is_select;
    }

    public void setIs_select(String is_select) {
        this.is_select = is_select;
    }

    public String getEquipment_no() {
        return equipment_no;
    }

    public void setEquipment_no(String equipment_no) {
        this.equipment_no = equipment_no;
    }
}
