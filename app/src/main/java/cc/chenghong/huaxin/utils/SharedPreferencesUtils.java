package cc.chenghong.huaxin.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import cc.chenghong.huaxin.App;
import cc.chenghong.huaxin.activity.R;

/**
 * SharedPreferences工具类
 * Created by hcl on 2014/3/25.
 */
public class SharedPreferencesUtils {
    /**
     * 文件名
     */
    private static String fileName = "app";
    /**
     * 上下文
     */
    private static Context application = App.getInstance();
    /**
     * 实例化
     */
//    public static void init(Context context){
//        application = context;
//        fileName = context.getResources().getString(R.string.app_name);
//        System.out.println(fileName);
//    }

    /**
     * 保存一个变量
     *
     * @param name  变量名
     * @param value 变量值
     */
    public static void setString(String name, String value) {
        application.getSharedPreferences(
                fileName, Context.MODE_PRIVATE)
                .edit().putString(name, value).commit();
    }

    /**
     * 取出一个变量
     *
     * @param name 变量名
     */
    public static String getString(String name) {
        return application.getSharedPreferences(fileName, Context.MODE_PRIVATE)
                .getString(name, "");
    }

    /**
     * 清除一个变量
     *
     * @param name 变量名
     */
    public static void cleanString(String name) {
        application.getSharedPreferences(fileName, Context.MODE_PRIVATE).edit()
                .remove(name).commit();
    }

    /**
     * 保存对象为JSON
     * @param key
     * @param b
     */
    public static void saveJSON(String key, Object o) {
        SharedPreferences.Editor editer = application.getSharedPreferences(fileName, 0).edit();
        String json = new Gson().toJson(o);
        Log.i("spm", json);
        editer.putString(key, json);
        editer.commit();
    }

    /**
     * 获取JSON对象
     */
    public static <T> T getJSON(String key, Class<T> clazz) {
        if(!contain(key)) return null;
        String json = getString(key);
        return new Gson().fromJson(json, clazz);
    }

    /**
     * 检查对应的值是否存在
     * @param key
     * @return
     */
    public static boolean contain(String key) {
        SharedPreferences sp = application.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        return sp.contains(key);
    }
}
