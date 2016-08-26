package cc.chenghong.huaxin.utils;

import android.os.Handler;
import android.os.Message;

public class ObjectHandler extends Handler{
    public Object obj1;
    public Object obj2;
    public ObjectHandler(Object obj1) {
        this.obj1 = obj1;
    }
    public ObjectHandler(Object obj1, Object obj2) {
        this.obj1 = obj1;
        this.obj2 = obj2;
    }
    
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
    }
}
