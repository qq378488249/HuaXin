package cc.chenghong.huaxin.entity;

import cc.chenghong.huaxin.response.ObjectResponse;

/** 健康建议
 * Created by hcl on 2016/5/2.
 */
public class Jkjy extends ObjectResponse<Jkjy>{
    /**
     * jkyj_count : 17
     * yyfa_count : 2
     * cxxx_count : 0
     * ydjy_count : 1
     * jkts_count : 0
     * fytx_count : 95
     */

    private String jkyj_count;
    private String yyfa_count;
    private String cxxx_count;
    private String ydjy_count;
    private String jkts_count;
    private String fytx_count;

    public String getJkyj_count() {
        return jkyj_count;
    }

    public void setJkyj_count(String jkyj_count) {
        this.jkyj_count = jkyj_count;
    }

    public String getYyfa_count() {
        return yyfa_count;
    }

    public void setYyfa_count(String yyfa_count) {
        this.yyfa_count = yyfa_count;
    }

    public String getCxxx_count() {
        return cxxx_count;
    }

    public void setCxxx_count(String cxxx_count) {
        this.cxxx_count = cxxx_count;
    }

    public String getYdjy_count() {
        return ydjy_count;
    }

    public void setYdjy_count(String ydjy_count) {
        this.ydjy_count = ydjy_count;
    }

    public String getJkts_count() {
        return jkts_count;
    }

    public void setJkts_count(String jkts_count) {
        this.jkts_count = jkts_count;
    }

    public String getFytx_count() {
        return fytx_count;
    }

    public void setFytx_count(String fytx_count) {
        this.fytx_count = fytx_count;
    }
}
