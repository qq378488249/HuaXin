package cc.chenghong.huaxin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;

import cc.chenghong.huaxin.App;
import cn.jpush.android.api.JPushInterface;

public class StartActivity extends BaseActivity {
    @ViewInject(R.id.tv)
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        initXutils();
        String s = getResources().getString(R.string.app_name);
        tv.setText(s + "欢迎您");

        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (App.getUser() == null) {
                    startActivity(new Intent(StartActivity.this,
                            LoginActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(StartActivity.this,
                            MainActivity.class));
                    finish();
                }
            }
        }.sendEmptyMessageDelayed(0, 1500);// 延迟2000毫秒执行
    }

    @Override
    protected void onPause() {
        JPushInterface.onPause(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        JPushInterface.onResume(this);
        super.onRestart();
    }
}
