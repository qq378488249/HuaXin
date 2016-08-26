package cc.chenghong.huaxin.entity;

import java.util.List;

import cc.chenghong.huaxin.response.ListResponse;

/** 血糖
 * Created by Administrator on 2016/3/18.
 */
public class Xt extends ListResponse<Xt>{

    public List<HistoryCount> count;

    public int code;

    public List<XtData> data;
}
