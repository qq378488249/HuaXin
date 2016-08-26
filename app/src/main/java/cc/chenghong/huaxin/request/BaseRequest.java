package cc.chenghong.huaxin.request;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.graphics.Bitmap;
import android.os.DropBoxManager.Entry;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue.RequestFilter;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import cc.chenghong.huaxin.App;
import cc.chenghong.huaxin.response.ListResponse;

/**
 * Volley网络连接基类
 *
 * @author hcl
 */
public class BaseRequest {
    private static final String TAG = "BaseRequest";
    private static RequestQueue requestQueue;
    /**
     * 请求超时时间(毫秒)
     */
    private static int timeOut = 5000;
    /**
     * 若连接失败的重试次数
     */
    private static int retryCount = 0;

    /**
     * 构造方法
     */
    BaseRequest() {
        requestQueue = Volley.newRequestQueue(App.getInstance());
    }

    /**
     * 发送请求
     *
     * @param request
     */
    public static void addRequest(Request request) {
        getRequest();
        // 打印url
        Log.i(TAG, "请求地址：" + request.getUrl());
        // 打印头部参数
        try {
            Map<String, String> headers = request.getHeaders();
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    Log.i(TAG, "头部参数键：" + entry.getKey() +
                            ",头部参数值：" + entry.getValue());
                }
            }
        } catch (AuthFailureError e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        request.setRetryPolicy(new RetryPolicy() {

            @Override
            public void retry(VolleyError error) throws VolleyError {
                // TODO Auto-generated method stub

            }

            @Override
            public int getCurrentTimeout() {//超时时间
                // TODO Auto-generated method stub
                return timeOut;
            }

            @Override
            public int getCurrentRetryCount() {//若网络连接失败时重试的次数
                // TODO Auto-generated method stub
                return retryCount;
            }
        });
        request.setRequestQueue(requestQueue);
        request.setTag("tag");
        requestQueue.add(request);
    }

    /**
     * 关闭所有请求
     */
    public static void stop() {
        requestQueue.cancelAll("tag");
    }

    /**
     * 获取一个RequestQueue
     */
    public static void getRequest() {
        if (requestQueue == null) {
            new BaseRequest();
        }
    }

    public static void i(String s) {
        Log.i(TAG, s);
    }

    /**
     * 获取volley实例
     *
     * @return
     */
    public static RequestQueue getBaseRequest() {
        return requestQueue;
    }

    /**
     * 发送json格式的post请求
     *
     * @param url           请求地址
     * @param jsonParam     json格式的参数
     * @param headers       头部参数
     * @param listener      请求成功监听器
     * @param errorListener 请求错误监听器
     */
    public static void postJson(String url, JSONObject jsonParam, final Map<String, String> headers,
                                Listener listener, ErrorListener errorListener) {
        i("发送的json参数：" + jsonParam);
        JsonObjectRequest request = new JsonObjectRequest(Method.POST, url, jsonParam, listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };
        addRequest(request);
    }
    /**
     * 发送json格式的post请求
     *
     * @param url           请求地址
     * @param jsonParam     json格式的参数
     * @param listener      请求成功监听器
     * @param errorListener 请求错误监听器
     */
    public static void postJson(String url, JSONObject jsonParam,
                                Listener listener, ErrorListener errorListener) {
        i("发送的json参数：" + jsonParam);
        JsonObjectRequest request = new JsonObjectRequest(Method.POST, url, jsonParam, listener, errorListener) {};
        addRequest(request);
    }
    /**
     * 发送post请求
     *
     * @param url           请求地址
     * @param listener      请求成功监听器
     * @param errorListener 请求错误监听器
     */
    public static void post(String url,Listener listener, ErrorListener errorListener) {
        StringRequest request = new StringRequest(Method.POST, url,listener, errorListener) {};

        addRequest(request);
    }
    /**
     * 发送post请求
     *
     * @param url           请求地址
     * @param listener      请求成功监听器
     * @param errorListener 请求错误监听器
     */
    public static void post(String url,final Map<String,String> mapParam,Listener listener, ErrorListener errorListener) {
        StringRequest request = new StringRequest(Method.POST, url,listener, errorListener) {
            @Override
            protected Map<String, String> getPostParams() throws AuthFailureError {
                return mapParam;
            }
        };
        addRequest(request);
    }

    /**
     * 发送需要header参数的get请求
     *
     * @param url           请求地址
     * @param headers       头部参数
     * @param listener      请求成功监听器
     * @param errorListener 请求错误监听器
     */
    public static void get(String url, final Map<String, String> headers,
                           Listener listener, ErrorListener errorListener) {
        StringRequest stringRequest = new StringRequest(Method.GET, url, listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // TODO Auto-generated method stub
                return headers;
            }
        };
        addRequest(stringRequest);
    }

    /**
     * 发送普通get请求
     *
     * @param url           请求地址
     * @param listener      请求成功监听器
     * @param errorListener 请求错误监听器
     */
    public static void get(String url,
                           Listener listener, ErrorListener errorListener) {
        i("请求方式：get");
        StringRequest stringRequest = new StringRequest(Method.GET, url, listener, errorListener) {
        };
        addRequest(stringRequest);
    }

    /**
     * 请求网络图片
     * @param url
     * @param maxWidth
     * @param maxHeigth
     * @param listener
     * @param errorListener
     */
    public static void getImage(String url,int maxWidth,int maxHeigth,Listener listener,ErrorListener errorListener){
        ImageRequest imageRequest = new ImageRequest( url , listener, 0, 0,Bitmap.Config.RGB_565,errorListener);
        addRequest(imageRequest);
    }

}
