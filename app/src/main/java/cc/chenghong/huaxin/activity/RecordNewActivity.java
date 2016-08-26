package cc.chenghong.huaxin.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.ArrayList;

import cc.chenghong.huaxin.App;
import cc.chenghong.huaxin.api.Api;
import cc.chenghong.huaxin.entity.User;

public class RecordNewActivity extends BaseActivity {
    @ViewInject(R.id.ll_bar)
    LinearLayout ll_bar;
    @ViewInject(R.id.tv_center)
    TextView tv_center;
    @ViewInject(R.id.tv_name)
    TextView tv_name;
    @ViewInject(R.id.tv_age)
    TextView tv_age;
    @ViewInject(R.id.tv_weight)
    TextView tv_weight;
    @ViewInject(R.id.tv_height)
    TextView tv_height;
    @ViewInject(R.id.iv)
    ImageView iv;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_new);
        initXutils();
        statusBar(ll_bar);
        tv_center.setText("健康档案");
        init();
//        getUserData();
    }

    private void init() {
        user = App.getUser();
        if (user != null) {
            getUrlBitmap(user.getHead_url(), iv);
            setValue(tv_name, user.getNick_name(), 1);
            setValue(tv_age, user.getAge(), 2);
            setValue(tv_weight, user.getWeight(), 3);
            setValue(tv_height, user.getHeight(), 4);
        }
    }

    private void setValue(TextView tv, String str, int code) {
        if (str == null || str.equals("")) {
            tv.setText("未完善");
        } else {
            if (code == 1) {
                tv.setText(str);
            }
            if (code == 2) {
                tv.setText(str + "岁");
            }
            if (code == 3) {
                tv.setText(str + "KG");
            }
            if (code == 4) {
                tv.setText(str + "CM");
            }
        }
    }

    @OnClick({R.id.iv_back, R.id.ll1, R.id.ll2, R.id.ll3, R.id.iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv:
                if (!stringIsNull(App.getUser().getHead_url())) {
                    ArrayList<String> list = new ArrayList<String>();
                    list.add(App.getUser().getHead_url());
                    photoBrower(0, list);
                }
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll1:
                openActivity(GrxxActivity.class);
                break;
            case R.id.ll2:
                openActivity(YyxxActivity.class);
                break;
            case R.id.ll3:
                openActivity(TjxxActivity.class);
                break;
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
//                    finish();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                hideProgress();
                toask("加载失败" + throwable.getMessage());
//                finish();
            }
        });
    }

    void isNull(TextView tv, String s) {
        if (s == null || s.equals("")) {
            tv.setText("未完善");
        } else {
            tv.setText(s);
        }
    }

    @Override
    protected void onResume() {
        init();
        super.onResume();
    }
}
