package cc.chenghong.huaxin.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import cc.chenghong.huaxin.App;
import cc.chenghong.huaxin.api.Api;
import cc.chenghong.huaxin.entity.Xycl;

/**
 * 血压测量/血糖测量/体脂测量
 */
public class PressureTest1Activity extends BaseActivity {
    @ViewInject(R.id.bt_submit)
    Button bt_submit;
    @ViewInject(R.id.bt_up)
    Button bt_up;
    @ViewInject(R.id.bt_load)
    Button bt_load;
    @ViewInject(R.id.bt)
    Button bt;
    @ViewInject(R.id.ll)
    LinearLayout ll;
    @ViewInject(R.id.ll_top)
    LinearLayout ll_top;
    @ViewInject(R.id.ll_bar1)
    LinearLayout ll_bar1;
    @ViewInject(R.id.ll_1)
    LinearLayout ll_1;
    @ViewInject(R.id.ll_2)
    LinearLayout ll_2;
    @ViewInject(R.id.ll_3)
    LinearLayout ll_3;
    @ViewInject(R.id.tv_time)
    TextView tv_time;
    @ViewInject(R.id.tv_center)
    TextView tv_center;
    @ViewInject(R.id.tv_title)
    TextView tv_title;
    @ViewInject(R.id.tv_1)
    TextView tv_1;
    @ViewInject(R.id.tv_2)
    TextView tv_2;
    @ViewInject(R.id.tv_3)
    TextView tv_3;
    @ViewInject(R.id.tv_4)
    TextView tv_4;
    @ViewInject(R.id.tv_5)
    TextView tv_5;
    @ViewInject(R.id.tv_num)
    TextView tv_num;

    String getUrl = "";
    Dialog dialog;
    TextView tv_ask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pressure_test);
        initXutils();
        statusBar(ll_bar1);
        tv_center.setText("血压测量");
        set_ll(ll_1);
        getUrl = Api.xy_get_new;
        if (code == 2) {
            tv_center.setText("血糖测量");
            set_ll(ll_2);
            ll_top.setBackgroundResource(R.drawable.pic08);
            getUrl = Api.xt_get_new;
        }
        if (code == 3) {
            tv_center.setText("体脂测量");
            ll_top.setBackgroundResource(R.drawable.pic06);
            set_ll(ll_3);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ll_top.getLayoutParams();
            layoutParams.height = (int) (getHeight() * 0.618);
            ll_top.setLayoutParams(layoutParams);
            tv_title.setVisibility(View.GONE);
            getUrl = Api.tz_get_new;
        }
        getData(true);
    }

    private void getData(final boolean b) {
        if (b) load();
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id", App.getUser().getId());
        client().post(getUrl, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                hideProgress();
                if (i == 200) {
//                    if (b)load_succeed();
                    Xycl data = fromJson(bytes, Xycl.class);
                    init(data);
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

    private void init(Xycl x) {
        tv_title.setVisibility(View.VISIBLE);
        System.out.println(x.toString());
        switch (code) {
            case 1:
                if (x.data == null) {
                    setTv(tv_1, "");
                    setTv(tv_2, "");
                    setTv(tv_3, "");
                    return;
                }
                String str = ssyCompareSzy(ssy(x.data.getSsy()), szy(x.data.getSzy()));
                tv_title.setText(str);
                if (str.equals("偏低")) {
                    tv_title.setBackgroundResource(R.drawable.pd);
                }
                if (str.equals("正常")) {
                    tv_title.setBackgroundResource(R.drawable.zc);
                }
                if (str.equals("一级")) {
                    tv_title.setBackgroundResource(R.drawable.yiji);
                }
                if (str.equals("二级")) {
                    tv_title.setBackgroundResource(R.drawable.erji);
                }
                if (str.equals("三级")) {
                    tv_title.setBackgroundResource(R.drawable.sanji);
                }
                setTv(tv_1, x.data.getSsy());
                setTv(tv_2, x.data.getSzy());
                setTv(tv_3, x.data.getXl());
                break;
            case 2:
                bt_load.setVisibility(View.VISIBLE);
                bt_up.setVisibility(View.VISIBLE);
                bt.setVisibility(View.VISIBLE);
                if (x.data == null || x.data.getTime_name() == null || x.data.getXt() == null) {
                    setTv(tv_4, "");
                    setTv(tv_5, "");
                    return;
                } else {
                    setTv(tv_4, x.data.getTime_name());
                    setTv(tv_5, x.data.getXt());
                }
                String str2 = xt(x.data.getXt(), x.data.getTime_name());
                tv_title.setText(str2);
                if (str2.equals("偏低")) {
                    tv_title.setBackgroundResource(R.drawable.pd);
                }
                if (str2.equals("正常")) {
                    tv_title.setBackgroundResource(R.drawable.zc);
                }
                if (str2.equals("偏高")) {
                    tv_title.setBackgroundResource(R.drawable.sanji);
                }
                break;
            case 3:
                String s = "";
                if (App.getUser().getAge() == null) {
                    if (s.equals("")) {
                        s = "年龄";
                    } else {
                        s += "，年龄";
                    }
                }
                if (App.getUser().getGender() == null || App.getUser().getGender().equals("-1")) {
                    if (s.equals("")) {
                        s = "性别";
                    } else {
                        s += "，性别";
                    }
                }
                if (App.getUser().getHeight() == null) {
                    if (s.equals("")) {
                        s = "身高";
                    } else {
                        s += "，身高";
                    }
                }

                if (stringIsNull(App.getUser().getAge()) || stringIsNull(App.getUser().getHeight()) || App.getUser().getGender().equals("-1")) {
                    show(s + "（必填）");
                }

                tv_title.setVisibility(View.GONE);
                if (x.data == null || x.data.getTz() == null) {
                    setTv(tv_num, "");
                } else {
                    setTv(tv_num, x.data.getTz());
                }
                break;
        }
        if (x != null && x.data != null && x.data.getCreated() != null) {
            setTv(tv_time, x.data.getCreated());
        }
    }

    private void show(String str) {
        if (dialog == null) {
            dialog = new Dialog(this, R.style.Dialog);
            dialog.setContentView(R.layout.dialog_ask);
            tv_ask = (TextView) dialog.findViewById(R.id.tv_content);
            dialog.findViewById(R.id.tv_yes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    openActivity(GrxxActivity.class);
                }
            });
            dialog.findViewById(R.id.tv_no).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    finish();
                }
            });
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                }
            });
        }
        tv_ask.setText(str);
        dialog.show();
    }

    @OnClick({R.id.iv_rigth, R.id.iv_back, R.id.bt})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_rigth:
//                if (code == 3){
//                    startActivity(new Intent(PressureTestActivity.this,EditPressureActivity.class).putExtra("code",code));
//                }else{
//                    startActivity(new Intent(PressureTestActivity.this,RecordPressureActivity.class).putExtra("code",code));
//                }
                startActivity(new Intent(PressureTest1Activity.this, RecordPressureActivity.class).putExtra("code", code));
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.bt://历史记录
                startActivity(new Intent(PressureTest1Activity.this, HistoryActivity.class).putExtra("code", code));
                break;
            case R.id.bt_load://蓝牙上传
//                startActivity(new Intent(PressureTestActivity.this, HistoryActivity.class).putExtra("code", code));
                break;
            case R.id.bt_up://蓝牙上传
//                startActivity(new Intent(PressureTestActivity.this, HistoryActivity.class).putExtra("code", code));
                break;
        }
    }

    private void set_ll(LinearLayout ll) {
        ll_1.setVisibility(View.GONE);
        ll_2.setVisibility(View.GONE);
        ll_3.setVisibility(View.GONE);
        ll.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onRestart() {
        getData(false);
        super.onRestart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
