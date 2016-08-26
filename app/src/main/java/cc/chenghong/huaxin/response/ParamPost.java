package cc.chenghong.huaxin.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * 将key、value自动拼装成HashMap
 * @author planet
 */
public class ParamPost extends EntityPost{
    private static final long serialVersionUID = 918959283941524749L;
    public HashMap<String, String> params = new HashMap<String, String>();
    /**
     * 路径
     */
    public List<String> pathes = new ArrayList<String>();

    public ParamPost() {
    }
    
    /**
     * 一个参数
     * @param key
     * @param value
     */
    public ParamPost(String key, Object value) {
    	add(key, value);
    }
    
    /**
     * 两个参数
     * @param key1
     * @param value1
     * @param key2
     * @param value2
     */
    public ParamPost(String key1, Object value1, String key2, Object value2) {
        add(key1, value1);
        add(key2, value2);
    }
    
    /**
     * 3个参数
     * @param key1
     * @param value1
     * @param key2
     * @param value2
     * @param key3
     * @param value3
     */
    public ParamPost(String key1, Object value1, String key2, Object value2, String key3, Object value3) {
        add(key1, value1);
        add(key2, value2);
        add(key3, value3);
    }
    
    public static ParamPost build(){
        return new ParamPost();
    }
    
    /**
     * 一个参数
     * @param key
     * @param value
     * @return
     */
    public static ParamPost build(String key, Object value) {
        return new ParamPost(key, String.valueOf(value));
    }
    
    /**
     * 两个参数
     * @param key1
     * @param value1
     * @param key2
     * @param value2
     * @return
     */
    public static ParamPost build(String key1, Object value1, String key2, Object value2) {
        return new ParamPost(key1, value1, key2, value2);
    }
    
    /**
     * 三个参数
     * @param key1
     * @param value1
     * @param key2
     * @param value2
     * @param key3
     * @param value3
     * @return
     */
    public static ParamPost build(String key1, Object value1, String key2, Object value2, String key3, Object value3) {
        return new ParamPost(key1, value1, key2, value2, key3, value3);
    }
    
    /**
     * 添加一个参数
     * @param key
     * @param value
     * @return
     */
    public ParamPost add(String key, Object value){
        params.put(key, value==null? null : String.valueOf(value));
        return this;
    }
    
    /**
     * 添加一个路径 { /path }
     * @param path
     * @return
     */
    public ParamPost path(Object path){
    	pathes.add(String.valueOf(path));
    	return this;
    }
    
    public List<String> getPathes() {
		return pathes;
	}
    
    public String get(String key){
    	return params.get(key);
    }
    
    /**
     * 获取map
     * @return
     */
    public HashMap<String, String> getParams() {
        return params;
    }
    
    public String toJsonString(){
    	return new Gson().toJson(params);
    }
}
