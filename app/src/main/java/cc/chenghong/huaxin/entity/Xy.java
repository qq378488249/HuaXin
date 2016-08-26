package cc.chenghong.huaxin.entity;

import java.util.List;

import cc.chenghong.huaxin.response.ObjectResponse;

/** 血压
 * Created by Administrator on 2016/3/18.
 */
public class Xy {

    public List<HistoryCount> count;

    public int code;

    public List<XyData> data;

    public List<HistoryCount> getCount() {
        return count;
    }

    public void setCount(List<HistoryCount> count) {
        this.count = count;
    }

    public List<XyData> getData() {
        return data;
    }

    public void setData(List<XyData> data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
