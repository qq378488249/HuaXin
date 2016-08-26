package cc.chenghong.huaxin.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/18.
 */
public class HistoryCount implements Serializable{

    private String date;

    private String value;

    private double xl;

    private double ssy;

    private double szy;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public double getXl() {
        return xl;
    }

    public void setXl(double xl) {
        this.xl = xl;
    }

    public double getSsy() {
        return ssy;
    }

    public void setSsy(double ssy) {
        this.ssy = ssy;
    }

    public double getSzy() {
        return szy;
    }

    public void setSzy(double szy) {
        this.szy = szy;
    }
}
