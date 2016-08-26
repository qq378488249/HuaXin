package cc.chenghong.huaxin.activity;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cc.chenghong.huaxin.App;
import cc.chenghong.huaxin.api.Api;
import cc.chenghong.huaxin.entity.MyEvent;
import cc.chenghong.huaxin.utils.DataUtils;
import cc.chenghong.huaxin.utils.SharedPreferencesUtils;
import cc.chenghong.huaxin.view.PickerView;
import de.greenrobot.event.EventBus;

/**
 * 记录血压、血糖、体脂
 */
public class RecordPressureActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.tv_date)
    TextView tv_date;
    @ViewInject(R.id.tv_time)
    TextView tv_time;
    @ViewInject(R.id.tv_pressure)
    TextView tv_pressure;
    @ViewInject(R.id.tv4)
    TextView tv4;
    @ViewInject(R.id.tv_left_4)
    TextView tv_left_4;
    @ViewInject(R.id.tv3)
    TextView tv3;
    @ViewInject(R.id.tv_line4)
    TextView tv_line4;
    @ViewInject(R.id.tv_line3)
    TextView tv_line3;

    @ViewInject(R.id.ll_4)
    LinearLayout ll_4;
    @ViewInject(R.id.ll_3)
    LinearLayout ll_3;

    private Dialog dialog_name;//性别，年龄，体重，身高dialog
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private List<String> list = new ArrayList<String>();//list

    String str;
    Calendar calendar;
    PickerView pv_name;
    String value;
    String s_data;
    String s_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.activity_record_pressure);
        setTitleName("记录血压");
        initXutils();

        calendar = Calendar.getInstance();
        tv_date.setText(DataUtils.getData("yyyy-MM-dd"));
        tv_time.setText(DataUtils.getData("HH:mm"));
        if (code == 2) {
            tv_line4.setVisibility(View.VISIBLE);
            ll_4.setVisibility(View.VISIBLE);
            setTitleName("记录血糖");

            tv3.setText("测量血糖");
//            list.add("早起");
//            list.add("餐前");
//            list.add("餐后");
//            list.add("睡前");
            list.add("空腹");
            list.add("早餐后2h");
            list.add("午餐前");
            list.add("午餐后2h");
            list.add("晚餐前");
            list.add("晚餐后2h");
            list.add("睡前");
            list.add("凌晨");
            list.add("随机");
            tv4.setText(list.get(0));
        }
        if (code == 3) {
            ll_3.setVisibility(View.GONE);
            tv_line3.setVisibility(View.GONE);
            tv_line4.setVisibility(View.VISIBLE);
            ll_4.setVisibility(View.VISIBLE);
            setTitleName("记录体脂");
            tv_left_4.setText("测量体脂");
            for (int i = 0; i < 5; i++) {
                list.add(i + "");
            }
        }
        EventBus.getDefault().register(this);
    }

    @OnClick({R.id.ll_1, R.id.ll_2, R.id.ll_3, R.id.ll_4, R.id.bt})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_1:
                show_date();
                break;
            case R.id.ll_2:
                show_time();
                break;
            case R.id.ll_3:
                Intent intent = new Intent(RecordPressureActivity.this, EditPressureActivity.class).putExtra("code", code);
                startActivityForResult(intent, 1000);
                break;
            case R.id.ll_4:
                if (code == 2) {
                    show();
                } else {
                    Intent intent1 = new Intent(RecordPressureActivity.this, EditPressureActivity.class).putExtra("code", code);
                    startActivityForResult(intent1, 1000);
                }
                break;
            case R.id.bt:
//                String str1 = viewGetValue(tv_date) + " " + viewGetValue(tv_time);
//                String str2 = DataUtils.getData("yyyy-MM-dd HH:mm");
//                if (DataUtils.data1_compare_data2(str1, str2, "yyyy-MM-dd HH:mm")) {
//                    toask("选择的时间不能大于当前时间");
//                    return;
//                }
                if (code == 1) {
                    if (value == null || value.equals("")) {
                        toask("请输入血压信息");
                        return;
                    }
                    RequestParams requestParams = new RequestParams();
                    String s[] = value.split(",");
                    requestParams.put("ssy", s[0]);
                    requestParams.put("szy", s[1]);
                    requestParams.put("xl", s[2]);
                    requestParams.put("user_id", App.getUser().getId());
                    requestParams.put("created", viewGetValue(tv_date) + " " + viewGetValue(tv_time));
                    progress("添加中...");
                    submit(Api.xy_add, requestParams);
                }
                if (code == 2) {
                    if (viewIsNull(tv_pressure)) {
                        toask("请输入血糖信息");
                        return;
                    }
                    RequestParams requestParams = new RequestParams();
                    requestParams.put("xt", viewGetValue(tv_pressure).substring(0, viewGetValue(tv_pressure).length() - 7));
                    requestParams.put("user_id", App.getUser().getId());
                    requestParams.put("time_name", viewGetValue(tv4));
                    requestParams.put("created", viewGetValue(tv_date) + " " + viewGetValue(tv_time));
                    progress("添加中...");
                    submit(Api.xt_add, requestParams);
                }
                if (code == 3) {
                    if (viewIsNull(tv_pressure)) {
                        toask("请输入体脂信息");
                        return;
                    }
                    RequestParams requestParams = new RequestParams();
                    requestParams.put("tz", viewGetValue(tv_pressure).substring(0, viewGetValue(tv_pressure).length() - 3));
                    requestParams.put("user_id", App.getUser().getId());
                    requestParams.put("created", viewGetValue(tv_date) + " " + viewGetValue(tv_time));
                    progress("添加中...");
                    submit(Api.tz_add, requestParams);
                }
                break;
        }
    }

    private void submit(String url, RequestParams requestParams) {
        progress("添加中...");
        client().post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                hideProgress();
                if (i == 200) {
                    App.toask("添加成功");
                    SharedPreferencesUtils.setString("code","2");
                    EventBus.getDefault().post(new MyEvent().code = code);
                    finish();

                } else {
                    App.toask("添加失败");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                no_network(throwable);
            }
        });
    }

    //显示日期
    private void show_date() {
        if (datePickerDialog == null) {
            datePickerDialog = new DatePickerDialog(this, null,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            //手动设置按钮
            datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "完成", new DialogInterface.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //通过mDialog.getDatePicker()获得dialog上的DatePicker组件，然后可以获取日期信息
                    DatePicker datePicker = datePickerDialog.getDatePicker();
                    int year = datePicker.getYear();
                    int month = datePicker.getMonth();
                    int day = datePicker.getDayOfMonth();
                    String m = (month + 1) + "";
                    String d = day + "";
                    if (month < 9) {
                        m = "0" + (month + 1);
                    }
                    if (day < 10) {
                        d = "0" + day;
                    }
//                    tv_date.setText(year + "-" + h + "-" + m + "");
                    String strData = DataUtils.getData("yyyy-MM-ddHH:mm");
                    String strData2 = year+"-"+m+"-"+d+viewGetValue(tv_time);
                    boolean b = DataUtils.data1_compare_data2(strData, strData2, "yyyy-MM-ddHH:mm");
                    System.out.println(b);
                    tv_date.setText(year+"-"+m+"-"+d);
                    if (!b) {
                        toask("选择的时间不能大于当前时间");
                        tv_date.setText(DataUtils.getData("yyyy-MM-dd"));
                        datePickerDialog.updateDate(Integer.valueOf(DataUtils.getData("yyyy")), Integer.valueOf(DataUtils.getData("MM")) - 1, Integer.valueOf(DataUtils.getData("dd")));
                    }
                }
            });
            //取消按钮，如果不需要直接不设置即可
            datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    System.out.println("BUTTON_NEGATIVE~~");
                }
            });
        }
        datePickerDialog.show();
//        datePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//
//            }
//        });
    }

    //显示时间
    private void show_time() {
        if (timePickerDialog == null) {
            timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String h = hourOfDay + "";
                    String m = minute + "";
                    if (hourOfDay < 10) {
                        h = "0" + hourOfDay;
                    }
                    if (minute < 10) {
                        m = "0" + minute;
                    }
//                    tv_time.setText(year + "-" + h + "-" + m + "");
                    String strData = DataUtils.getData("yyyy-MM-ddHH:mm");
                    String strData2 = viewGetValue(tv_date)+h+":"+m;
                    boolean b = DataUtils.data1_compare_data2(DataUtils.getData("yyyy-MM-ddHH:mm"), strData2, "yyyy-MM-ddHH:mm");
                    tv_time.setText(h + ":" + m);
                    if (!b) {
                        toask("选择的时间不能大于当前时间");
                        tv_time.setText(DataUtils.getData("HH:mm"));
                        timePickerDialog.updateTime(Integer.valueOf(DataUtils.getData("HH")),Integer.valueOf(DataUtils.getData("mm")));
                    }
                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        }
        timePickerDialog.show();
    }

    private void show() {
        if (viewIsNull(tv4)) {
            tv4.setText(list.get(0));
        }
        str = tv4.getText().toString();
        if (dialog_name == null) {
            dialog_name = new Dialog(this, R.style.Dialog);
            dialog_name.setContentView(R.layout.dialog_select);
            TextView tv_title = (TextView) dialog_name.findViewById(R.id.tv_title);
            tv_title.setText("时段");
            if (code == 3) {
                tv_title.setText("体脂");
            }
            pv_name = (PickerView) dialog_name.findViewById(R.id.pv);
            pv_name.setData(list);
            pv_name.setOnSelectListener(new PickerView.onSelectListener() {
                @Override
                public void onSelect(String text) {
                    str = text;
                }
            });
            dialog_name.findViewById(R.id.tv_yes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv4.setText(str);
                    dialog_name.dismiss();
                }
            });
            dialog_name.findViewById(R.id.tv_no).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog_name.dismiss();
                }
            });
        }

        for (int i = 0; i < list.size(); i++) {
            if (tv4.getText().toString().equals(list.get(i))) {
                pv_name.setSelected(i);//设置选中项
            }
        }

        dialog_name.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1000 && resultCode == 200) {
            if (code == 3) {
                tv4.setText(data.getStringExtra("data") + "");
            }
            value = data.getStringExtra("value");
            tv_pressure.setText(data.getStringExtra("data") + "");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(MyEvent event) {

    }
}
