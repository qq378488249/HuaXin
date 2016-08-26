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
import java.util.List;

import cc.chenghong.huaxin.activity.ConsultActivity;
import cc.chenghong.huaxin.api.Api;
import cc.chenghong.huaxin.response.ObjectResponse;
import cc.chenghong.huaxin.utils.StringUtils;

/**
 * 上传多文件线程
 * Created by hcl on 2016/5/4.
 */
public class UpFilesThread extends Thread {
    private Handler mHander;
    private List<String> mList;
    private InputStream content = null;
    private boolean isRun = false;//判断线程是否正在运行
    private boolean isNext = true;//判断是否上传文件，进行下一步操作
    private String old_imgs = "";//老的服务器图片路径

    public UpFilesThread(Handler handler, List<String> list) {
        this.mHander = handler;
        this.mList = list;
    }

    @Override
    public void run() {
//        if (){
//
//        }
        isRun = true;
        MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, "----------ThIs_Is_tHe_bouNdaRY_$", Charset.defaultCharset());
        for (String s : mList) {
            if (s.equals(ConsultActivity.IV_ADD)) {
                continue;
            }else if (s.indexOf("http:") != -1) {//数据集里面有服务器上的图片时，加到老图片里去
//                if (old_imgs.equals("")) {
//                    old_imgs += s;
//                } else {
//                    old_imgs += "," + s;
//                }
                old_imgs += "," + s;
            }else{
                multipartEntity.addPart("file[]", new FileBody(new File(s), "image/jpg"));
            }
        }
//                            multipartEntity.addPart("file", new FileBody(new File(photoPath), "image/jpg"));
        HttpPost request = new HttpPost(Api.file_uploads);
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
                    if (!old_imgs.equals("")) {
                        message.obj = objectResponse.data + old_imgs;
                    } else {
                        message.obj = objectResponse.data;
                    }
                    System.out.println(objectResponse.data);
                    System.out.println(mList.toString());
                    System.out.println(old_imgs.toString());
                    mHander.sendMessage(message);
                }
            } else {
                if (isNext) {
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
        if (content != null) {
            try {
                content.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isNext() {
        return isNext;
    }

    public void setIsNext(boolean isNext) {
        this.isNext = isNext;
    }
}
