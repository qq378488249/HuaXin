package cc.chenghong.huaxin.request;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hcl on 2016/4/28.
 */
public class AsyncHttpParams {
    private Map<String,Object> map = new HashMap<String,Object>();

    public void put(String key, Object value) {
        if(key != null && value != null) {
            map.put(key, value);
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("saasdf").append(" ");
        stringBuffer.toString();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("aasdfsda").append(" ");
        stringBuilder.toString();
    }

    public Object get(String key) {
        if(key != null) {
            return map.get(key);
        }
        return null;
    }

    public void clear(){
        map.clear();
    }

    public void remove(String key) {
        map.remove(key);
    }

    public static AsyncHttpParams New() {
        return new AsyncHttpParams();
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }
}
