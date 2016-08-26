package cc.chenghong.huaxin.request;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import java.util.Map;

/**
 * AsyncHttp连接基类
 * Created by Administrator on 2016/3/16.
 */
public class AsyncHttpRequest extends AsyncHttpClient {
    public static final String TAG = AsyncHttpRequest.class.getSimpleName();
    private static AsyncHttpClient client;
    private static RequestParams params;
    /**
     * 获取一个连接器，如果没有就新建一个
     *
     * @return
     */
    public static AsyncHttpClient getClient() {
        if (client == null) {
            client = new AsyncHttpRequest();
        }
        return client;
    }
    /**
     * 获取一个连接器，如果没有就新建一个
     *
     * @return
     */
    public static void init() {
        if (client == null) {
            client = new AsyncHttpRequest();
        }
    }

    /**
     * 发送post请求
     *
     * @param url
     * @param asyncHttpParams
     * @param AsyncHttpResponseHandler
     */
    public static void post(String url, RequestParams asyncHttpParams, AsyncHttpResponseHandler AsyncHttpResponseHandler) {
//        log(url, "POST", null, asyncHttpParams);
        client.post(url, params, AsyncHttpResponseHandler);
    }

    public static void post(String url, ResponseHandler responseHandler) {
        log(url, "POST", null, null);
        client.post(url,responseHandler);
    }

    public static void post(String url, AsyncHttpParams asyncHttpParams, ResponseHandlerInterface asyncHttpResponseHandler) {
        log(url, "POST", null, asyncHttpParams);
        client.post(url, params, asyncHttpResponseHandler);
    }

    /**
     * 发送post请求
     *
     * @param url
     * @param AsyncHttpResponseHandler
     */
    public static void post(String url, AsyncHttpResponseHandler AsyncHttpResponseHandler) {
        log(url, "POST", null, null);
        client.post(url, AsyncHttpResponseHandler);
    }

    /**
     * 发送带头部参数的post请求
     *
     * @param url
     * @param headers
     * @param asyncHttpParams
     * @param AsyncHttpResponseHandler
     */
    public static void post(String url, Map<String, String> headers, AsyncHttpParams asyncHttpParams, AsyncHttpResponseHandler AsyncHttpResponseHandler) {
        log(url, "POST", headers, asyncHttpParams);
        client.post(url, params, AsyncHttpResponseHandler);
    }

    private static void log_i(String str) {
        Log.i(TAG, str);
    }

    private static void log(String url, String requestMode, Map<String, String> headers, AsyncHttpParams asyncHttpParams) {
        getClient();
        if (url != null) {
            log_i("请求地址：" + url);
        }
        if (requestMode != null) {
            log_i("请求方式：" + requestMode);
        }
        if (headers != null) {
            client.removeAllHeaders();//清空所有头部参数
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                log_i("头部键：" + entry.getKey() + ",头部值：" + entry.getValue());
                client.addHeader(entry.getKey(), entry.getValue());
            }
        }
        if (asyncHttpParams != null) {
            params = new RequestParams();
            for (Map.Entry<String, Object> entry : asyncHttpParams.getMap().entrySet()) {
                log_i("请求参数键：" + entry.getKey() + ",请求参数值：" + entry.getValue());
                params.put(entry.getKey(), entry.getValue());
            }
        }
    }

}
