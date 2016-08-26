package cc.chenghong.huaxin.activity;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.lidroid.xutils.http.client.multipart.HttpMultipartMode;
import com.lidroid.xutils.http.client.multipart.MultipartEntity;
import com.lidroid.xutils.http.client.multipart.content.FileBody;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cc.chenghong.huaxin.App;
import cc.chenghong.huaxin.adapter.CommonAdapter;
import cc.chenghong.huaxin.adapter.ViewHolder;
import cc.chenghong.huaxin.api.Api;
import cc.chenghong.huaxin.comparator.SortComparator;
import cc.chenghong.huaxin.entity.Jkts;
import cc.chenghong.huaxin.entity.JktsContent;
import cc.chenghong.huaxin.request.AsyncHttpParams;
import cc.chenghong.huaxin.request.AsyncHttpRequest;
import cc.chenghong.huaxin.response.ObjectResponse;
import cc.chenghong.huaxin.utils.StringUtils;
import ligth_blue.view.NoScrollGridAdapter;

/**
 * 营养方案，运动建议，健康贴士
 */
public class Jkts1Activity extends BaseActivity {
    @ViewInject(R.id.ll_add)
    LinearLayout ll_add;
    @ViewInject(R.id.lv)
    ListView lv;
    //    @ViewInject(R.id.wv)
//    WheelView wv;
    @ViewInject(R.id.tv)
    TextView tv;

    TextView tv_title;
    TextView tv_time;
    TextView tv_content;
    TextView tv_zan;
    TextView tv_yuedu;
    LinearLayout ll_zan;
    LinearLayout ll_yuedu;
    ImageView iv;
    PopupWindow popupWindow;
    LinearLayout ll_head;//头布局
    EditText et_dialog;
    InputMethodManager imm;
    ImageView iv_add;
    String photoPath = "";

    String id = "";
    String UrlXx = "";//获取详细信息
    String UrlQxz = "";//取消赞
    String UrlDz = "";//点赞
    String UrlPl = "";//发言
    String UrlId = "";//添加发言id

    Jkts jkts;
    CommonAdapter<JktsContent> adapter;
    List<JktsContent> list = new ArrayList<JktsContent>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.activity_test);
        initXutils();
        switch (code) {
            case 1:
                setTitleName("营养方案");
                UrlXx = Api.yyfa_get;
                UrlDz = Api.yyfa_like_add;
                UrlQxz = Api.yyfa_like_cancel;
                UrlPl = Api.yyfa_comment_add;
                UrlId = "message_yyfa_id";
                break;
            case 3:
                setTitleName("运动建议");
                UrlXx = Api.ydjy_get;
                UrlDz = Api.ydjy_like_add;
                UrlQxz = Api.ydjy_like_cancel;
                UrlPl = Api.ydjy_comment_add;
                UrlId = "message_ydjy_id";
                break;
            case 4://健康贴士
                setTitleName("健康贴士");
                UrlXx = Api.jkts_get;
                UrlDz = Api.jkts_like_add;
                UrlQxz = Api.jkts_like_cancel;
                UrlPl = Api.jkts_comment_add;
                UrlId = "message_jkts_id";
                break;
        }
        id = getIntent().getStringExtra("id");
        if (id != null && !id.equals("")) {
            getData(true);
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
        if (b) load();
        AsyncHttpParams requestParams = AsyncHttpParams.New();
        requestParams.put("id", id);
        requestParams.put("user_id", App.getUser().getId());
        AsyncHttpRequest.post(UrlXx, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                hideProgress();
                if (i == 200) {
//                    if (b) load_succeed();
                    jkts = fromJson(bytes, Jkts.class);
                    jkts = jkts.data;
                    list.clear();
                    list.addAll(jkts.getUser_comment());
//                    Collections.reverse(list); // 倒序排列
                    Collections.sort(list, new SortComparator());
                    if (adapter == null) {
                        initAdapter();
                    }
                    adapter.notifyDataSetChanged();
                    if (!b) {
                        if (list.size()>0)lv.setSelection(0);}
                    init();
                } else {
                    load_fail();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                no_network(throwable);
            }
        });
    }

    private void initAdapter() {
        adapter = new CommonAdapter<JktsContent>(this, list, R.layout.lv_item_zxxq) {
            @Override
            public void convert(ViewHolder helper, JktsContent item, int position) {
                ImageView iv = helper.getView(R.id.iv);
                GridView gv = helper.getView(R.id.gv);
                if (item.getUser() != null && item.getUser().getHead_url() != null && !item.getUser().getHead_url().equals("")) {
                    getUrlBitmap(item.getUser().getHead_url(), iv);
                    helper.setText(R.id.tv_name, formatName(item.getUser().getMobile()));
                }
                helper.setText(R.id.tv_time, formatDate(item.getCreated()));
                helper.setText(R.id.tv_content, item.getContent());
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
        ll_head = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.lv_head_jkts, null);
        tv_title = (TextView) ll_head.findViewById(R.id.tv_title);
        tv_time = (TextView) ll_head.findViewById(R.id.tv_time);
        tv_content = (TextView) ll_head.findViewById(R.id.tv_content);
        ll_head.findViewById(R.id.ll_zan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//点赞
                if (tv_zan.isSelected()) {
                    qxz();
                } else {
                    dz();
//                    DatePickerDialog
                }
            }
        });
        tv_zan = (TextView) ll_head.findViewById(R.id.tv_zan);
        tv_yuedu = (TextView) ll_head.findViewById(R.id.tv_yuedu);
        iv = (ImageView) ll_head.findViewById(R.id.iv);
        lv.addHeaderView(ll_head);
        lv.setAdapter(adapter);
    }

    private void init() {
        tv_time.setText(jkts.getCreated());
        tv_title.setText(jkts.getTitle());
        tv_yuedu.setText(jkts.getView_count() + "");
        tv_zan.setText(jkts.getLike_count() + "");
        tv_content.setText(Html.fromHtml(jkts.getContent()+""));
//        tv_content.setText(StringUtils.html2Android(jkts.getContent()));
        if (!stringIsNull(jkts.getImg_url())) {
            getUrlBitmap(jkts.getImg_url(), iv);
        }
        if (jkts.getIs_like().equals("0")) {
            tv_zan.setTextColor(getResources().getColor(R.color.black));
            tv_zan.setSelected(false);
        } else {
            tv_zan.setTextColor(getResources().getColor(R.color.blue_title));
            tv_zan.setSelected(true);
        }
    }

    /**
     * 取消赞
     */
    void qxz() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id", App.getUser().getId());
        if (code == 1) {
            requestParams.put("message_yyfa_id", id);
        }
        if (code == 3) {
            requestParams.put("message_ydjy_id", id);
        }
        if (code == 4) {
            requestParams.put("message_jkts_id", id);
        }
        client().post(UrlQxz, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                toask("取消赞成功");
                tv_zan.setSelected(false);
                tv_zan.setTextColor(getResources().getColor(R.color.black));
                int count = jkts.getLike_count();
                count = count - 1;
                if (count < 1) {
                    count = 0;
                }
                jkts.setLike_count(count);
                tv_zan.setText(count + "");
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                no_network(throwable);
            }
        });
    }

    /**
     * 点赞
     */
    void dz() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id", App.getUser().getId());
        if (code == 1) {
            requestParams.put("message_yyfa_id", id);
        }
        if (code == 3) {
            requestParams.put("message_ydjy_id", id);
        }
        if (code == 4) {
            requestParams.put("message_jkts_id", id);
        }
        client().post(UrlDz, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                toask("点赞成功");
                tv_zan.setTextColor(getResources().getColor(R.color.blue_title));
                tv_zan.setSelected(true);
                int count = jkts.getLike_count();
                count = count + 1;
                jkts.setLike_count(count);
                tv_zan.setText(count + "");
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                no_network(throwable);
            }
        });
    }

    private void show() {
        if (popupWindow == null) {
            //right_pop為泡泡的布局
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_yyfa, null);
            // 第一个参数导入泡泡的view，后面两个指定宽和高
            popupWindow = new PopupWindow(view, getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT, true);
            //加图片
//            popupWindow = new PopupWindow(view, getWindowManager().getDefaultDisplay().getWidth(), UITools.dp2px(220), true);
            // 设置此参数获得焦点，否则无法点击
            popupWindow.setFocusable(true);
            //软键盘不会挡着popupwindow
            popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            // 设置点击窗口外边窗口消失，
            //下面两句位置不能颠倒，不然无效！（经本机测试 不知道别人如何）必须设置backgroundDrawable()
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setOutsideTouchable(true);
            et_dialog = (EditText) view.findViewById(R.id.et);
//            iv_add = (ImageView) view.findViewById(R.id.iv_add);
//
//            view.findViewById(R.id.iv_add).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    show_iv_add();
//                    popupWindow.dismiss();
//                }
//            });
            view.findViewById(R.id.iv).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
                        new Thread() {
                            @Override
                            public void run() {
                                MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, "----------ThIs_Is_tHe_bouNdaRY_$", Charset.defaultCharset());
                                multipartEntity.addPart("file", new FileBody(new File(photoPath), "image/jpg"));
                                HttpPost request = new HttpPost(Api.file_upload);
                                request.setEntity(multipartEntity);
                                request.addHeader("Content-Type", "multipart/form-data;boundary=----------ThIs_Is_tHe_bouNdaRY_$");
                                DefaultHttpClient httpClient = new DefaultHttpClient();
                                HttpResponse response;
                                try {
                                    response = httpClient.execute(request);
                                    HttpEntity responseEntity = response.getEntity();
                                    int resCode = response.getStatusLine().getStatusCode();
                                    InputStream content = responseEntity.getContent();
                                    //result.setResult(StringUtils.readString(content));
                                    String result = StringUtils.readString(content);
                                    ObjectResponse<String> objectResponse = fromJson(result, ObjectResponse.class);
                                    Message message = Message.obtain();
                                    if (objectResponse.isSuccess()) {
                                        message.what = 200;
                                        message.obj = objectResponse.data;
                                    } else {
                                        message.what = 404;
                                    }
                                    handler.sendMessage(message);
                                } catch (ClientProtocolException e) {
                                    // TODO Auto-generated catch block
                                    Message message = Message.obtain();
                                    message.what = 404;
                                    handler.sendMessage(message);
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    Message message = Message.obtain();
                                    message.what = 404;
                                    handler.sendMessage(message);
                                    e.printStackTrace();
                                }
                                super.run();
                            }
                        }.start();
                    } else {
                        AsyncHttpParams requestParams = AsyncHttpParams.New();
                        requestParams.put("user_id", App.getUser().getId());
                        requestParams.put(UrlId, id);
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

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 200){
                AsyncHttpParams requestParams = AsyncHttpParams.New();
                requestParams.put(UrlId, jkts.getId());
                requestParams.put("user_id", App.getUser().getId());
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
        AsyncHttpRequest.post(UrlPl, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                hideProgress();
                if (i == 200) {
                    toask("发言成功");
                    et_dialog.setText("");
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
