package cc.chenghong.huaxin.entity;

import cc.chenghong.huaxin.response.ListResponse;

/**
 * 服药提醒实体类
 * Created by Administrator on 2016/3/16.
 */
public class Suggest5 extends ListResponse<Suggest5>{
    private String id;

    private String user_id;

    private String title;

    private String buy_num;

    private String created;

    private String status;

    private String is_notice;

    private String eat_num;

    private String img_url;

    private String zaoshang_time;

    private String zaoshang_num;

    private String zhongwu_time;

    private String zhongwu_num;

    private String wanshang_time;

    private String wanshang_num;

    private boolean isSelect;//是否选中

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBuy_num() {
        return buy_num;
    }

    public void setBuy_num(String buy_num) {
        this.buy_num = buy_num;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIs_notice() {
        return is_notice;
    }

    public void setIs_notice(String is_notice) {
        this.is_notice = is_notice;
    }

    public String getEat_num() {
        return eat_num;
    }

    public void setEat_num(String eat_num) {
        this.eat_num = eat_num;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getZaoshang_time() {
        return zaoshang_time;
    }

    public void setZaoshang_time(String zaoshang_time) {
        this.zaoshang_time = zaoshang_time;
    }

    public String getZaoshang_num() {
        return zaoshang_num;
    }

    public void setZaoshang_num(String zaoshang_num) {
        this.zaoshang_num = zaoshang_num;
    }

    public String getZhongwu_time() {
        return zhongwu_time;
    }

    public void setZhongwu_time(String zhongwu_time) {
        this.zhongwu_time = zhongwu_time;
    }

    public String getZhongwu_num() {
        return zhongwu_num;
    }

    public void setZhongwu_num(String zhongwu_num) {
        this.zhongwu_num = zhongwu_num;
    }

    public String getWanshang_time() {
        return wanshang_time;
    }

    public void setWanshang_time(String wanshang_time) {
        this.wanshang_time = wanshang_time;
    }

    public String getWanshang_num() {
        return wanshang_num;
    }

    public void setWanshang_num(String wanshang_num) {
        this.wanshang_num = wanshang_num;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }
}
