package cc.chenghong.huaxin.client;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.net.URI;

/**
 * Created by Administrator on 2016/3/8.
 */
public class BaseClient {

    public static void post(String url,RequestParams requestParams){
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {//成功的回调

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {//失败的回调

            }
        });
    }

    public static void postJson(String url,RequestParams requestParams){
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {//成功的回调

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {//失败的回调

            }
        });
    }
}
