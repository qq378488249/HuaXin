package cc.chenghong.huaxin.utils;

import com.google.gson.Gson;

/**
 * Created by hcl on 2016/4/29.
 */
public class GsonUtils {

    public static <T> T parserTFromJson(String jsonStr,Class<T> clazz) {
        return new Gson().fromJson(jsonStr,clazz);
    }

}
