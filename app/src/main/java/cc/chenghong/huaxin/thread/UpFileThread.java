package cc.chenghong.huaxin.thread;

import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.lidroid.xutils.http.client.multipart.HttpMultipartMode;
import com.lidroid.xutils.http.client.multipart.MultipartEntity;
import com.lidroid.xutils.http.client.multipart.content.FileBody;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import cc.chenghong.huaxin.api.Api;
import cc.chenghong.huaxin.response.ObjectResponse;
import cc.chenghong.huaxin.utils.StringUtils;

/**
 * 上传单文件线程
 * Created by hcl on 2016/5/4.
 */
public class UpFileThread extends Thread {
    private Handler mHander;
    private String filePath;
    private InputStream content = null;
    private boolean isRun = false;//判断线程是否正在运行
    private boolean isNext = true;//判断是否上传文件，进行下一步操作

    public UpFileThread(Handler handler, String filePath) {
        this.mHander = handler;
        this.filePath = filePath;
    }

    @Override
    public void run() {
        isRun = true;
        MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, "----------ThIs_Is_tHe_bouNdaRY_$", Charset.defaultCharset());
        multipartEntity.addPart("file", new FileBody(new File(filePath), "image/jpg"));
//                            multipartEntity.addPart("file", new FileBody(new File(photoPath), "image/jpg"));
        HttpPost request = new HttpPost(Api.file_upload);
        request.setEntity(multipartEntity);
        request.addHeader("Content-Type", "multipart/form-data;boundary=----------ThIs_Is_tHe_bouNdaRY_$");
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse response;
        try {
            response = httpClient.execute(request);
            HttpEntity responseEntity = response.getEntity();
            int resCode = response.getStatusLine().getStatusCode();
            content = responseEntity.getContent();
            //result.setResult(StringUtils.readString(content));
            String result = StringUtils.readString(content);
            ObjectResponse<String> objectResponse = new Gson().fromJson(result, ObjectResponse.class);
            Message message = Message.obtain();
            if (objectResponse.isSuccess()) {
                if (isNext) {
                    message.what = 200;
                    message.obj = objectResponse.data;
                    mHander.sendMessage(message);
                }
            } else {
                if (isNext()) {
                    message.what = 404;
                    mHander.sendMessage(message);
                }
            }
            isRun = false;
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            isRun = false;
            if (isNext() == true) {
                Message message = Message.obtain();
                message.what = 404;
                mHander.sendMessage(message);
            }
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            isRun = false;
            if (isNext() == true) {
                Message message = Message.obtain();
                message.what = 404;
                mHander.sendMessage(message);
            }
            e.printStackTrace();
        } finally {
            try {
                if (content != null) {
                    content.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.run();
    }

    /**
     * 返回线程的运行状态
     *
     * @return
     */
    public boolean isRun() {
        return isRun;
    }

    public void setIsRun(boolean isRun) {
        this.isRun = isRun;
    }

    public void close() {
        isNext = false;
        try {
            if (content != null) {
                content.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isNext() {
        return isNext;
    }

    public void setIsNext(boolean isNext) {
        this.isNext = isNext;
    }
}
