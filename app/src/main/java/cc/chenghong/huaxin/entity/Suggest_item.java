package cc.chenghong.huaxin.entity;

/**
 * Created by Administrator on 2016/2/24.
 */
public class Suggest_item {
    private int id;
    private String title;
    private String count;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Suggest_item(int id, String title) {
        this.id = id;
        this.title = title;
    }
}
