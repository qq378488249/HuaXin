package cc.chenghong.huaxin.entity;

import java.util.ArrayList;
import java.util.List;
import cc.chenghong.huaxin.adapter.CommonAdapter;

/** 分页工具类
 * Created by hcl on 2016/3/16.
 */
public class Page<T> {

    public List<T> list = new ArrayList<T>();//数据集合
    public CommonAdapter<T> adapter;//适配器
    public int page_index;//页数
    public int page_size;//每页个数

    public Page(int page_index, int page_size) {
        this.page_index = page_index;
        this.page_size = page_size;
    }

    /**
     * 下一页
     */
    public int nextPage(){
        return page_index++;
    }

    /**
     * 第一页
     * @return
     */
    public int firstPage(){
        return page_index=0;
    }

    /**
     * 是否是第一页
     * @return
     */
    public boolean isFirstPage(){
        return page_index == 0;
    }

}
