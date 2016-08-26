package cc.chenghong.huaxin.entity;

/**
 * Created by Administrator on 2016/3/9.
 * 习惯实体类
 */
public class Habit {
    private int id;
    private String name;
    private boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Habit(int id, String name, boolean isSelect) {
        this.id = id;
        this.name = name;
        this.isSelect = isSelect;
    }
}
