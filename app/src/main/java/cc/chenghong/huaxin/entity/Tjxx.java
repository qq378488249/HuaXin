package cc.chenghong.huaxin.entity;

import cc.chenghong.huaxin.response.ObjectResponse;

/**
 * Created by hcl on 20 private String6/3/28.
 */
public class Tjxx extends ObjectResponse<Tjxx> {
        private String kfxt;
        private String zdgc;
        private String gysz;
        private String gmdzdb;
        private String thxhdb;
        private String jbs_count;

    public String getKfxt() {
        return kfxt;
    }

    public void setKfxt(String kfxt) {
        this.kfxt = kfxt;
    }

    public String getZdgc() {
        return zdgc;
    }

    public void setZdgc(String zdgc) {
        this.zdgc = zdgc;
    }

    public String getGysz() {
        return gysz;
    }

    public void setGysz(String gysz) {
        this.gysz = gysz;
    }

    public String getGmdzdb() {
        return gmdzdb;
    }

    public void setGmdzdb(String gmdzdb) {
        this.gmdzdb = gmdzdb;
    }

    public String getThxhdb() {
        return thxhdb;
    }

    public void setThxhdb(String thxhdb) {
        this.thxhdb = thxhdb;
    }

    public String getJbs_count() {
        return jbs_count;
    }

    public void setJbs_count(String jbs_count) {
        this.jbs_count = jbs_count;
    }

    public String getFys_count() {
        return fys_count;
    }

    public void setFys_count(String fys_count) {
        this.fys_count = fys_count;
    }

    private String fys_count;
}
