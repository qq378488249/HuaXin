package cc.chenghong.huaxin.activity;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

import cc.chenghong.huaxin.App;
import cc.chenghong.huaxin.adapter.CommonAdapter;
import cc.chenghong.huaxin.adapter.ViewHolder;
import cc.chenghong.huaxin.api.Api;
import cc.chenghong.huaxin.camera_code.UITools;
import cc.chenghong.huaxin.entity.Reply_data;
import cc.chenghong.huaxin.entity.Wdzx;
import cc.chenghong.huaxin.entity.Zxxq;
import cc.chenghong.huaxin.request.AsyncHttpParams;
import cc.chenghong.huaxin.request.AsyncHttpRequest;
import cc.chenghong.huaxin.thread.UpFileThread;
import cc.chenghong.huaxin.utils.BitmapUtils;
import ligth_blue.view.NoScrollGridAdapter;

/**
 * 咨询详情
 */
public class Zxxq1Activity extends BaseActivity {
    @ViewInject(R.id.lv)
    ListView lv;
    @ViewInject(R.id.ll_add)
    LinearLayout ll_add;

    TextView tv_name;
    TextView tv_time;
    TextView tv_title;
    TextView tv_content;
    ImageView iv;
    EditText et_dialog;
    LinearLayout ll_head;
    ImageView iv_add;

    PopupWindow popupWindow;
    InputMethodManager imm;
    CommonAdapter<Reply_data> adapter;
    List<Reply_data> list = new ArrayList<Reply_data>();
    //图片本地路径
    String photoPath = "";

    Wdzx w;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.activity_zxxq1);
        getClass();
        initXutils();
        setTitleName("咨询详情");
        w = (Wdzx) getIntent().getSerializableExtra("data");
        if (w != null) {
            getData(true);
            ll_head = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.lv_head_zxxq, null);
            tv_content = (TextView) ll_head.findViewById(R.id.tv_content);
            tv_name = (TextView) ll_head.findViewById(R.id.tv_name);
            tv_time = (TextView) ll_head.findViewById(R.id.tv_time);
            tv_title = (TextView) ll_head.findViewById(R.id.tv_title);
            tv_content = (TextView) ll_head.findViewById(R.id.tv_content);
            iv = (ImageView) ll_head.findViewById(R.id.iv);

            tv_name.setText(formatName(App.getUser().getMobile()));
            if (!stringIsNull(App.getUser().getHead_url())) {
                getUrlBitmap(App.getUser().getHead_url(), iv);
            }

            tv_time.setText(formatDate(w.getCreated()));
            tv_title.setText(w.getTitle());
            tv_content.setText(w.getContent());
        }
        ll_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show();
            }
        });
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    }

    private void getData(final boolean b) {
        requestParams.put("id", w.getId());
        if (b) progress("加载中...");
        AsyncHttpRequest.post(Api.consultation_get, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                hideProgress();
                String s = new String(bytes);
                System.out.println(s);
                Zxxq z = fromJson(bytes, Zxxq.class);
                if (z.isSuccess()) {
//                    if (b) toask("加载成功");
                    list.clear();
                    list.addAll(z.data.getReply_data());
//                    list.addAll(z.getReply_data());
                    if (adapter == null) {
                        initAdapter();
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    toask("加载失败");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                no_network(throwable);
            }
        });
    }

    private void initAdapter() {
        adapter = new CommonAdapter<Reply_data>(this, list, R.layout.lv_item_zxxq) {
            @Override
            public void convert(ViewHolder helper, Reply_data item, int position) {
                ImageView iv = helper.getView(R.id.iv);
                ImageView iv_zj = helper.getView(R.id.iv_zj);
                GridView gv = helper.getView(R.id.gv);
                if (w.getEmployee().getIs_expert().equals("1")) {//专家
                    iv_zj.setVisibility(View.VISIBLE);
                } else {
                    iv_zj.setVisibility(View.GONE);
                }
                if (item.getType().equals("1")) {//医生
                    getUrlBitmap(w.getImg_url(), iv);
                    helper.setText(R.id.tv_name, w.getEmployee().getNickname());
                } else {//用户
                    getUrlBitmap(App.getUser().getHead_url(), iv);
                    helper.setText(R.id.tv_name, formatName(App.getUser().getMobile()));
                }
                if (item.getCreated() == null) {
                    helper.setText(R.id.tv_time, "");
                } else {
                    helper.setText(R.id.tv_time, formatDate(item.getCreated()));
                }
                helper.setText(R.id.tv_content, item.getContent() + "");
                if (!stringIsNull(item.getImg_url())) {
                    String[] imgs = item.getImg_url().split(",");
                    final ArrayList<String> list = new ArrayList<>();
                    for (String img : imgs) {
                        list.add(img);
                    }
                    gv.setAdapter(new NoScrollGridAdapter(getContext(),list));
                    gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            photoBrower(position,list);
                        }
                    });
                    gv.setVisibility(View.VISIBLE);
                }else{
                    gv.setVisibility(View.GONE);
                }
            }
        };
        lv.addHeaderView(ll_head);
        lv.setAdapter(adapter);
    }

    private void show() {
        if (popupWindow == null) {
            //right_pop為泡泡的布局
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_fy, null);
            // 第一个参数导入泡泡的view，后面两个指定宽和高
            popupWindow = new PopupWindow(view, getWindowManager().getDefaultDisplay().getWidth(), UITools.dp2px(220), true);
            // 设置此参数获得焦点，否则无法点击
            popupWindow.setFocusable(true);
            //软键盘不会挡着popupwindow
            popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            // 设置点击窗口外边窗口消失，
            //下面两句位置不能颠倒，不然无效！（经本机测试 不知道别人如何）必须设置backgroundDrawable()
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setOutsideTouchable(true);
            et_dialog = (EditText) view.findViewById(R.id.et);
            iv_add = (ImageView) view.findViewById(R.id.iv_add);
            view.findViewById(R.id.iv).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
            view.findViewById(R.id.iv_add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    show_iv_add();
                    pickPhoto();
                    popupWindow.dismiss();
                }
            });

            view.findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (viewIsNull(et_dialog)) {
                        toask("请输入发言内容");
                        return;
                    }
                    if (viewGetValue(et_dialog).trim().equals("")) {
                        toask("发言内容不能为空");
                        return;
                    }
                    if (!photoPath.equals("")) {//说明上传了图片
                        progress("发言中...");
                        String upload = BitmapUtils.savaImage2SD(photoPath);
                        UpFileThread upFileThread = new UpFileThread(handler,upload);
                        upFileThread.start();
//                        new Thread() {
//                            @Override
//                            public void run() {
//                                MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, "----------ThIs_Is_tHe_bouNdaRY_$", Charset.defaultCharset());
//                                multipartEntity.addPart("file", new FileBody(new File(upload), "image/jpg"));
//                                HttpPost request = new HttpPost(Api.file_upload);
//                                request.setEntity(multipartEntity);
//                                request.addHeader("Content-Type", "multipart/form-data;boundary=----------ThIs_Is_tHe_bouNdaRY_$");
//                                DefaultHttpClient httpClient = new DefaultHttpClient();
//                                HttpResponse response;
//                                try {
//                                    response = httpClient.execute(request);
//                                    HttpEntity responseEntity = response.getEntity();
//                                    int resCode = response.getStatusLine().getStatusCode();
//                                    InputStream content = responseEntity.getContent();
//                                    //result.setResult(StringUtils.readString(content));
//                                    String result = StringUtils.readString(content);
//                                    ObjectResponse<String> objectResponse = fromJson(result, ObjectResponse.class);
//                                    Message message = Message.obtain();
//                                    if (objectResponse.isSuccess()) {
//                                        message.what = 200;
//                                        message.obj = objectResponse.data;
//                                    } else {
//                                        message.what = 404;
//                                    }
//                                    handler.sendMessage(message);
//                                } catch (ClientProtocolException e) {
//                                    // TODO Auto-generated catch block
//                                    Message message = Message.obtain();
//                                    message.what = 404;
//                                    handler.sendMessage(message);
//                                    e.printStackTrace();
//                                } catch (IOException e) {
//                                    // TODO Auto-generated catch block
//                                    Message message = Message.obtain();
//                                    message.what = 404;
//                                    handler.sendMessage(message);
//                                    e.printStackTrace();
//                                }
//                                super.run();
//                            }
//                        }.start();
                    } else {
                        AsyncHttpParams requestParams = AsyncHttpParams.New();
                        requestParams.put("id", w.getId());
                        requestParams.put("content", viewGetValue(et_dialog).trim());
                        submit(requestParams);
                    }
                    popupWindow.dismiss();
                }
            });
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    setAlpha(1f);
                }
            });
        }
        // 弹窗一般有两种展示方法，用showAsDropDown()和showAtLocation()两种方法实现。
        // 以这个v为anchor（可以理解为锚，基准），在下方弹出
        popupInputMethodWindow();
        setAlpha(0.5f);
        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    private void popupInputMethodWindow() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 0);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 200){
                AsyncHttpParams requestParams = AsyncHttpParams.New();
                requestParams.put("id", w.getId());
                requestParams.put("content", viewGetValue(et_dialog).trim());
                requestParams.put("img_url", msg.obj);
                submit(requestParams);
            }else{
                toask("发言失败");
            }
        }
    };

    @Override
    protected void onPhotoTaked(String photoPath) {
        this.photoPath = photoPath;
        show();
//        popupWindow.setFocusable(true);
//        popupInputMethodWindow();
        iv_add.setImageBitmap(BitmapFactory.decodeFile(photoPath));
    }

    private  void submit(AsyncHttpParams requestParams){
        progress("发言中...");
        AsyncHttpRequest.post(Api.consultation_reply, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                hideProgress();
                if (i == 200) {
                    toask("发言成功");
                    iv_add.setImageResource(R.drawable.jh);
                    et_dialog.setText("");
                    photoPath = "";
                    getData(false);
                } else {
                    toask("发言失败");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                no_network(throwable);
            }
        });
    }

}
