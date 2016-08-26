package cc.chenghong.huaxin.entity;

/**
 * Created by Administrator on 2016/3/18.
 */
public class TzData {
    private String id;

    private String user_id;

    private String tz;

    private String created;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_id() {
        return this.user_id;
    }

    public void setTz(String tz) {
        this.tz = tz;
    }

    public String getTz() {
        return this.tz;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCreated() {
        return this.created;
    }
}
