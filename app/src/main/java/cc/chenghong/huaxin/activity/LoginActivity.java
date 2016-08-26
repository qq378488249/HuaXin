package cc.chenghong.huaxin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import cc.chenghong.huaxin.App;
import cc.chenghong.huaxin.api.Api;
import cc.chenghong.huaxin.entity.Shop;
import cc.chenghong.huaxin.entity.User;
import cc.chenghong.huaxin.request.AsyncHttpParams;
import cc.chenghong.huaxin.request.AsyncHttpRequest;

public class LoginActivity extends BaseActivity {
    //    @ViewInject(R.id.tv_submit)
//    TextView tv_submit;
    @ViewInject(R.id.tv)
    TextView tv;
    @ViewInject(R.id.tv_shop)
    TextView tv_shop;
    @ViewInject(R.id.tv_treaty)
    TextView tv_treaty;
    @ViewInject(R.id.iv_treaty)
    ImageView iv_treaty;
    @ViewInject(R.id.et)
    EditText et;
    @ViewInject(R.id.iv_1)
    TextView iv_1;

    private Shop shop = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initXutils();
        et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
//        et.setText("18973006800");
        et.setText("");
        et.setSelection(viewGetValue(et).length());
        autoSelect(iv_treaty);
    }

    @OnClick({R.id.tv_submit, R.id.ll, R.id.tv_treaty, R.id.iv_treaty})
    public void onclick(View v) {
        switch (v.getId()) {
            case R.id.tv_submit:
//                toask(isPhoneNum(et));
                if (!isPhoneNum(et)) {
                    toask("请输入正确的手机号");
                    return;
                }
                if (shop == null) {
                    App.toask("请选择门店");
                    return;
                }
                if (!iv_treaty.isSelected()) {
                    App.toask("请仔细阅读服务协议");
                    return;
                }
                login();
                break;
            case R.id.ll:
                Intent intent = new Intent(this, SelectShopActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.tv_treaty:
                openActivity(AgreementActivity.class);
//                App.toask("协议");
                break;
            case R.id.iv_treaty:
                autoSelect(iv_treaty);
                break;
        }
    }

    private void login() {
        JSONObject json = new JSONObject();
        try {
            json.put("mobile", viewGetValue(et));
            json.put("organization_id", shop.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        progress("登陆中...");
//        RequestParams params = new RequestParams();
//        params.add("mobile", viewGetValue(et));
//        params.add("organization_id", shop.getId());

        AsyncHttpParams asyncHttpParams = AsyncHttpParams.New();
        asyncHttpParams.put("mobile", viewGetValue(et));
        asyncHttpParams.put("organization_id", shop.getId());
        AsyncHttpRequest.post(Api.user_login, asyncHttpParams, new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(int i,Header[] headers, byte[] str) {
                hideProgress();
                User response = fromJson(str, User.class);
                System.out.println(response.toString());
                if (i==200) {
                    toask("登陆成功");
                    setString("login", "1");//保存用户
                    App.setUser(response.data);
                    if (!isComplete()){
                        startActivity(GrxxActivity.class);
                    }else {
                        startActivity(MainActivity.class);
                    }
                    finish();
                } else {
                    App.toask("登陆失败");
                }
            }

//            @Override
//            public void onSuccess(int var1, Object data) {
//
//            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                no_network(throwable);
            }
        });
//        client().post(Api.user_login, params, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                String s = new String(bytes);
//                User response = fromJson(s, User.class);
//                if (response.isSuccess()) {
//                    App.toask("登陆成功");
//                    setString("login", "1");//保存用户
//                    App.setUser(response.data);
//                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                    finish();
//                } else {
//                    App.toask("登陆失败");
//                }
//            }
//
//            @Override
//            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//                App.toask("登陆失败");
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent mIntent) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            shop = (Shop) mIntent.getSerializableExtra("data");
            tv_shop.setText(shop.getName());
        }
    }
}
