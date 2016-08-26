package cc.chenghong.huaxin.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.text.DecimalFormat;

import cc.chenghong.huaxin.App;

/**
 * 记录血压、血糖、体脂
 */
public class EditPressureActivity extends BaseActivity {
    @ViewInject(R.id.ll_2)
    LinearLayout ll_2;
    @ViewInject(R.id.ll_3)
    LinearLayout ll_3;
    @ViewInject(R.id.tv_1a)
    TextView tv_1a;
    @ViewInject(R.id.tv_1b)
    TextView tv_1b;
    @ViewInject(R.id.et1)
    EditText et1;
    @ViewInject(R.id.et2)
    EditText et2;
    @ViewInject(R.id.et3)
    EditText et3;
    @ViewInject(R.id.bt_submit)
    Button bt_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.activity_edit_pressure);
        setTitleName("血压");
        initXutils();
        tv_rigth.setText("保存");
        tv_rigth.setVisibility(View.VISIBLE);
        et1.setFocusable(true);
        et1.setFocusableInTouchMode(true);
        et1.requestFocus();
        if (code == 2) {
            ll_2.setVisibility(View.GONE);
            et2.setVisibility(View.GONE);
            ll_3.setVisibility(View.GONE);
            et3.setVisibility(View.GONE);
            et1.setHint("请输入血糖");
            tv_1a.setText("血糖");
            setTitleName("血糖");
            tv_1b.setText("mmol/L");
            et1.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            et1.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
            et1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
        } else if (code == 1) {
            et1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
            et2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
            et3.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
        } else {
            ll_2.setVisibility(View.GONE);
            et2.setVisibility(View.GONE);
            ll_3.setVisibility(View.GONE);
            et3.setVisibility(View.GONE);
            et1.setHint("请输入体脂");
            tv_1a.setText("体脂");
            setTitleName("体脂");
            tv_1b.setText("Kg");
            et1.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            et1.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
            et1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        }
    }

    @OnClick({R.id.tv_rigth})
    public void onClick(View v) {
        switch (code) {
            case 1:
                if (et1.getText().toString().equals("")) {
                    App.toask("请输入收缩压");
                    return;
                }
                if (viewValueFormatDouble(et1) < 30 || viewValueFormatDouble(et1) > 300 || viewValueFormatDouble(et2) < 30 || viewValueFormatDouble(et2) > 300) {
                    App.toask("血压范围：30-300");
                    return;
                }
                if (et2.getText().toString().equals("")) {
                    App.toask("请输入舒张压");
                    return;
                }
                if (et3.getText().toString().equals("")) {
                    App.toask("请输入心率");
                    return;
                }
                if (viewValueFormatDouble(et3) < 30 || viewValueFormatDouble(et3) > 200) {
                    App.toask("心率范围：30-200");
                    return;
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et1.getWindowToken(), 0);
                Intent intent = new Intent(this, RecordPressureActivity.class);
                intent.putExtra("data", et1.getText() + "/" + et2.getText() + " mmHg " + et3.getText() + " bmp");
                intent.putExtra("value", viewGetValue(et1) + "," + viewGetValue(et2) + "," + viewGetValue(et3));
                setResult(200, intent);
                finish();
                break;
            case 2:
                if (et1.getText().toString().equals("")) {
                    App.toask("请输入血糖");
                    return;
                }
                if (!checkMoney(et1)) {
                    App.toask("输入有误，最多只能输入2位小数");
                    return;
                }
                if (viewValueFormatDouble(et1) < 0 || viewValueFormatDouble(et1) > 40) {
                    App.toask("血糖范围：0-40");
                    return;
                }
                Intent intent1 = new Intent(this, RecordPressureActivity.class);
                intent1.putExtra("data", stringToDouble() + " mmol/L");
                setResult(200, intent1);
                finish();
                break;
            case 3:
                if (et1.getText().toString().equals("")) {
                    App.toask("请输入体脂");
                    return;
                }
                if (!checkMoney(et1)) {
                    App.toask("输入有误，最多只能输入2位小数");
                    return;
                }
                if (viewValueFormatDouble(et1) < 20 || viewValueFormatDouble(et1) > 500) {
                    App.toask("体脂范围：20-500");
                    return;
                }
                Intent intent3 = new Intent(this, RecordPressureActivity.class);
                intent3.putExtra("data", stringToDouble() + " Kg");
                setResult(200, intent3);
                finish();
//                RequestParams requestParams = new RequestParams();
//                requestParams.put("tz", viewGetValue(et1));
//                requestParams.put("user_id", App.getUser().getId());
//                progress("上传中...");
//                client().post(Api.tz_add, requestParams, new AsyncHttpResponseHandler() {
//                    @Override
//                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                        hideProgress();
//                        BaseResponse response = fromJson(bytes, BaseResponse.class);
//                        if (response.isSuccess()) {
//                            toask("上传成功");
//                            startActivity(new Intent(getApplicationContext(), HistoryActivity.class).putExtra("code",3));
//                        } else {
//                            toask("上传失败");
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//                        hideProgress();
//                        toask("上传失败" + throwable.toString());
//                    }
//                });
                break;
        }

    }

    // 检查金额是否有误
    boolean checkMoney(View view) {
        if (viewGetValue(view).equals("0.0")
                || viewGetValue(view).equals("0.")
                || viewGetValue(view).equals("0.00")) {
            return false;
        } else {
            String str = viewGetValue(view);
            String regex = "^[+]?(([1-9]\\d*[.]?)|(0.))(\\d{0,2})?$";//
            return str.matches(regex);
        }
    }

    private String stringToDouble() {
        // TODO Auto-generated method stub
        DecimalFormat df = new DecimalFormat("######0.00");
        return df.format(viewValueFormatDouble(et1));
    }
}
