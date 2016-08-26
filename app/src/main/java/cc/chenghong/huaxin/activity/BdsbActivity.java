package cc.chenghong.huaxin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import cc.chenghong.huaxin.App;
import cc.chenghong.huaxin.api.Api;
import cc.chenghong.huaxin.camera_code.MipcaActivityCapture;
import cc.chenghong.huaxin.entity.Wdsb;
import cc.chenghong.huaxin.request.AsyncHttpParams;
import cc.chenghong.huaxin.request.AsyncHttpRequest;

/**
 * 绑定设备
 */
public class BdsbActivity extends BaseActivity {
    @ViewInject(R.id.et)
    EditText et;
    @ViewInject(R.id.tv)
    TextView tv;
    @ViewInject(R.id.bt)
    Button bt;

    Wdsb wdsb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.activity_bdsb);
        initXutils();
        wdsb = (Wdsb) getIntent().getSerializableExtra("data");
        if (wdsb != null) {
            tv.setText("如果您已经拥有一个" + wdsb.getName() + "，设备号位于设备的背面，请反转设备，并输入十四位条码数字。");
        } else {
            tv.setText("如果您已经拥有一个血糖仪，设备号位于设备的背面，请反转设备，并输入十四位条码数字。");
        }
        et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(14)});
        setTitleName("绑定设备");
        iv_rigth.setVisibility(View.VISIBLE);
        iv_rigth.setImageResource(R.drawable.b_0005);
        iv_rigth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityForResult(MipcaActivityCapture.class, 1);
            }
        });
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewIsNull(et)){
                    toask(et.getHint());
                    return;
                }
                AsyncHttpParams asyncHttpParams = AsyncHttpParams.New();
                asyncHttpParams.put("user_id", App.getUser().getId());
                asyncHttpParams.put("id", wdsb.getId());
                asyncHttpParams.put("equipment_no", viewGetValue(et));
                progress("绑定中...");
                AsyncHttpRequest.post(Api.equipment_add, asyncHttpParams, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                        hideProgress();
                        if (i == 200) {
                            toask("绑定成功");
                            finish();
                        } else {
                            toask("绑定失败");
                        }
                    }

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                        no_network(throwable);
                    }
                });
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent mIntent) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (mIntent.getStringExtra("data") != null && !mIntent.getStringExtra("data").equals("")) {
                et.setText(mIntent.getStringExtra("data"));
            }
        }
        super.onActivityResult(requestCode, resultCode, mIntent);
    }
}
