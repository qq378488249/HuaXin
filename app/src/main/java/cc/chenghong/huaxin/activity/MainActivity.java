package cc.chenghong.huaxin.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.ArrayList;

import cc.chenghong.huaxin.App;
import cc.chenghong.huaxin.api.Api;
import cc.chenghong.huaxin.dialog.AskDialog;
import cc.chenghong.huaxin.entity.User;
import cc.chenghong.huaxin.jpush.ExampleUtil;
import cc.chenghong.huaxin.request.AsyncHttpParams;
import cc.chenghong.huaxin.request.AsyncHttpRequest;
import cn.jpush.android.api.JPushInterface;

/**
 * 于160826上传到githua
 * 主页面 hcl 20160224
 */
public class MainActivity extends BaseActivity {
    @ViewInject(R.id.iv)
    ImageView iv;
    @ViewInject(R.id.iv_top)
    ImageView iv_top;
    @ViewInject(R.id.ll_1)
    LinearLayout ll_1;
    @ViewInject(R.id.ll_content1)
    LinearLayout ll_content1;
    @ViewInject(R.id.ll_content2)
    LinearLayout ll_content2;
    @ViewInject(R.id.ll_2)
    LinearLayout ll_2;
    @ViewInject(R.id.ll_3)
    LinearLayout ll_3;
    @ViewInject(R.id.ll_4)
    LinearLayout ll_4;
    @ViewInject(R.id.tv_sugar)
    TextView tv_sugar;
    @ViewInject(R.id.tv_press)
    TextView tv_press;
    @ViewInject(R.id.ll_bar1)
    LinearLayout ll_bar1;
    @ViewInject(R.id.tv_name)
    TextView tv_name;
    @ViewInject(R.id.tv_phone)
    TextView tv_phone;
    @ViewInject(R.id.tv_exit)
    TextView tv_exit;

    PopupWindow popupWindow;
    private Dialog dialogComplete;
    long exitTime = 0;
    User user;

    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static boolean isForeground = false;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    User u;
    private AskDialog dialogExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initXutils();
        App.isMainRun = true;
//        if (!isComplete()) {
//            showComplete();
//        }
        final String jgid = JPushInterface.getRegistrationID(getApplicationContext());
        JPushInterface.getUdid(getApplicationContext());
        u = App.getUser();
        if (!stringIsNull(jgid) && stringIsNull(u.getJgid())) {//如果有极光id，并且用户未设置极光id
            AsyncHttpParams asyncHttpParams = new AsyncHttpParams();
            asyncHttpParams.put("id", u.getId());
            asyncHttpParams.put("jgid", jgid);
            AsyncHttpRequest.post(Api.user_update, asyncHttpParams, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (statusCode == 200) {
                        u.setJgid(jgid);
                        App.setUser(u);//保存用户
//                        System.out.println(u.toString());
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    no_network(error);
                }
            });
        }
//        System.out.println(s);
        statusBar(ll_bar1);
        init();
        registerMessageReceiver();

        Display display = getWindowManager().getDefaultDisplay();
        int height = display.getHeight();
        int width = display.getWidth();
//        int gdc = height - width;
//        int i230 = UITools.dp2px(220);
//        System.out.println(gdc+""+i230);
//        if (height - width > UITools.dp2px(230)){//说明屏幕高度减宽度的差足够显示头像，昵称等信息
//            LinearLayout.LayoutParams lp1 = (LinearLayout.LayoutParams) ll_content1.getLayoutParams();
//            lp1.height = height - width;
//            lp1.height = (int) (height*(1 - 0.618));
//            ll_content1.setLayoutParams(lp1);
//
        LinearLayout.LayoutParams lp2 = (LinearLayout.LayoutParams) ll_content2.getLayoutParams();
        lp2.height = width;
        ll_content2.setLayoutParams(lp2);
//        }

        tv_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_exit();
//                openActivity(LoginActivity.class);
//                App.setUser(null);
//                finish();
            }
        });

    }

    private void show_exit() {
//        if (dialogExit == null) {
//            dialogExit = new Dialog(this, R.style.Dialog);
//            dialogExit.setContentView(R.layout.dialog_ask);
//            LinearLayout ll = (LinearLayout) dialogExit.findViewById(R.id.ll);
//            setViewWidth(ll,getWidth() - UITools.dip2px(40));
//            TextView tvTitle = (TextView) dialogExit.findViewById(R.id.tv_title);
//            TextView tvContent = (TextView) dialogExit.findViewById(R.id.tv_content);
//            tvTitle.setText("提示");
//            tvContent.setText("确定退出当前账号？");
//            dialogExit.findViewById(R.id.tv_yes).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    openActivity(LoginActivity.class);
//                    App.setUser(null);
//                    dialogExit.dismiss();
//                    finish();
//                }
//            });
//            dialogExit.findViewById(R.id.tv_no).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialogExit.dismiss();
//                }
//            });
//        }
        if (dialogExit == null) {
            dialogExit = new AskDialog(this);
            dialogExit.setTvTitle("提示");
            dialogExit.setTvContent("确定退出当前账号？");
            dialogExit.setTvYesOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openActivity(LoginActivity.class);
                    App.setUser(null);
                    dialogExit.dismiss();
                    finish();
                }
            });
            dialogExit.setTvNoOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogExit.dismiss();
                }
            });
        }
        dialogExit.show();
    }

    private void init() {
        user = App.getUser();
        tv_name.setText("您好");
        if (user != null) {
//            tv_name.setText(user.getNick_name() + "");
            tv_phone.setText(user.getMobile() + "");
            if (!stringIsNull(user.getNick_name())) {
                tv_phone.setText(user.getNick_name());
            }
            getUrlBitmap(user.getHead_url(), iv);
        }
    }

    @OnClick({R.id.iv, R.id.ll_1, R.id.ll_2, R.id.ll_3, R.id.ll_4, R.id.fl, R.id.iv_top, R.id.tv_sugar, R.id.tv_press})
    public void onclick(View v) {
        if (!isComplete()) {
            showComplete();
            return;
        }
        switch (v.getId()) {
            case R.id.iv:
                if (!stringIsNull(App.getUser().getHead_url())) {
                    ArrayList<String> list = new ArrayList<String>();
                    list.add(App.getUser().getHead_url());
                    photoBrower(0, list);
                }
                break;
            case R.id.ll_1:
                startActivity(new Intent(MainActivity.this, MeasureActivity.class));
                break;
            case R.id.ll_2:
                startActivity(new Intent(MainActivity.this, SuggestActivity.class));
                break;
            case R.id.ll_3:
                startActivity(new Intent(MainActivity.this, ConsultActivity.class));
                break;
            case R.id.ll_4:
                startActivity(new Intent(MainActivity.this, RecordNewActivity.class));
                break;
            case R.id.iv_top:
                showPopupWindow(v);
                break;
        }
    }

    public void showPopupWindow(View v) {
        if (popupWindow == null) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(this);
            //right_pop為泡泡的布局
            View view = mLayoutInflater.inflate(R.layout.popwindows_main, null);
            // 第一个参数导入泡泡的view，后面两个指定宽和高
            popupWindow = new PopupWindow(view, getWindowManager().getDefaultDisplay().getWidth() / 2, getWindowManager().getDefaultDisplay().getHeight() / 5);
            // 设置此参数获得焦点，否则无法点击
            popupWindow.setFocusable(true);
            // 设置点击窗口外边窗口消失，
            //下面两句位置不能颠倒，不然无效！（经本机测试 不知道别人如何）必须设置backgroundDrawable()
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setOutsideTouchable(true);
            //快速测血糖
            view.findViewById(R.id.tv_sugar).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, TestSugarActivity.class));
                    popupWindow.dismiss();
                }
            });
            //快速测血压
            view.findViewById(R.id.tv_press).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, TestPressActivity.class));
                    popupWindow.dismiss();
                }
            });
        }
        // 弹窗一般有两种展示方法，用showAsDropDown()和showAtLocation()两种方法实现。
        // 以这个v为anchor（可以理解为锚，基准），在下方弹出
        v.getLeft();
        int x = -getWindowManager().getDefaultDisplay().getWidth() / 2 + 50;
        popupWindow.showAsDropDown(v, x, 0);
//        popupWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.TOP,getWindowManager().getDefaultDisplay().getWidth()/3,100);
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            toask("再按一次退出程序");
            exitTime = System.currentTimeMillis();
            return;
        } else {
            finish();
            return;
        }
    }

    public void getUserData() {
        progress("加载中...");
        RequestParams reques = new RequestParams();
        reques.put("id", App.getUser().getId());
        client().post(Api.user_get, reques, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                hideProgress();
                User u = fromJson(bytes, User.class);
                if (u.isSuccess()) {
                    toask("加载成功");
                    user = u.data;
                    init();
                } else {
                    toask("加载失败");
                    user = App.getUser();
                    init();
//                    finish();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                hideProgress();
                toask("加载失败" + throwable.getMessage());
                user = App.getUser();
                init();
//                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        isForeground = true;
        init();
        super.onResume();
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!ExampleUtil.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
            }
        }
    }

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(mMessageReceiver);
        App.isMainRun = false;
        super.onDestroy();
    }

    @Override
    protected void onRestart() {//重绘
        super.onRestart();
//        if (!isComplete()){
//            showComplete();
//        }
    }
}
