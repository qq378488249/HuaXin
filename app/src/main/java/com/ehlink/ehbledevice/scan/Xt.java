package com.ehlink.ehbledevice.scan;

/**
 * Created by hcl on 2016/5/11.
 */
public class Xt {
    public String time;
    public  String xt;
    public String time_name;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getXt() {
        return xt;
    }

    public void setXt(String xt) {
        this.xt = xt;
    }

    public String getTime_name() {
        return time_name;
    }

    public void setTime_name(String time_name) {
        this.time_name = time_name;
    }

    @Override
    public String toString() {
        return "Xt{" +
                "time='" + time + '\'' +
                ", xt='" + xt + '\'' +
                ", time_name='" + time_name + '\'' +
                '}';
    }
}
