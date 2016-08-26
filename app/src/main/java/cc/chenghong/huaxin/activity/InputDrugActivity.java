package cc.chenghong.huaxin.activity;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cc.chenghong.huaxin.App;
import cc.chenghong.huaxin.utils.DataUtils;
import cc.chenghong.huaxin.view.PickerView;

/**
 * 输入用药信息 20160310
 */
public class InputDrugActivity extends BaseActivity {
    @ViewInject(R.id.iv_top)
    ImageView iv_top;
    @ViewInject(R.id.iv_1)
    ImageView iv_1;
    @ViewInject(R.id.iv_drug)
    ImageView iv_drug;

    @ViewInject(R.id.tv_time)
    TextView tv_time;
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
    @ViewInject(R.id.tv_6)
    TextView tv_6;
    @ViewInject(R.id.tv_7)
    TextView tv_7;
    @ViewInject(R.id.tv_8)
    TextView tv_8;

    @ViewInject(R.id.ll_2)
    LinearLayout ll_2;//下午
    @ViewInject(R.id.ll_3)
    LinearLayout ll_3;//晚上
    @ViewInject(R.id.ll_drug)
    LinearLayout ll_drug;//用药

    TextView tv_title;

    Dialog dialog_name;
    TimePickerDialog dialog_time;
    TimePickerDialog dialog_time2;
    TimePickerDialog dialog_time3;
    String str="";
    String defult="";
    PickerView pv_name;

    List<String> list_sl = new ArrayList<String>();
    List<String> list_cs = new ArrayList<String>();
    List<String> list_dw = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.activity_intpu_drug);
        initXutils();
        init();
        setTitleName("输入用药信息");
        if (code == 1) {
            setTitleName("查看用药");
        }
    }

    private void init() {
        tv_time.setText(DataUtils.getData());
        Bitmap bitmap = (Bitmap)getIntent().getParcelableExtra("data");
        if(bitmap!=null){
            iv_1.setImageBitmap(bitmap);
        }
        for (int i = 0; i < 21; i++) {
            list_sl.add(""+ i);
        }
        for (int i = 1; i < 4; i++) {
            list_cs.add(""+ i);
        }
        for (int i = -1; i < 21; i++) {
            if (i== -1){
                list_dw.add("1/2");
            }
            else if (i== 0){
                list_dw.add("1/4");
            }else{
                list_dw.add(""+ i);
            }
        }
    }

    @OnClick({R.id.tv_1,R.id.tv_2,R.id.tv_3,R.id.tv_4,R.id.tv_5,R.id.tv_6,R.id.tv_7,R.id.tv_8,R.id.bt,R.id.ll_drug})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_1:
                set_select(tv_1);
                defult = "0";
                str = viewGetValue(tv_1);
                show("数量",list_sl);
                break;
            case R.id.tv_2:
                set_select(tv_2);
                defult = "1";
                str = viewGetValue(tv_2);
                show("次数",list_cs);
                break;
            case R.id.tv_3:
                set_select(tv_3);
                show_time();
                break;
            case R.id.tv_4:
                set_select(tv_4);
                str = viewGetValue(tv_4);
                show("单位", list_dw);
                break;
            case R.id.tv_5:
                set_select(tv_5);
                show_time2();
                break;
            case R.id.tv_6:
                set_select(tv_6);
                str = viewGetValue(tv_6);
                show("单位", list_dw);
                break;
            case R.id.tv_7:
                set_select(tv_7);
                show_time3();
                break;
            case R.id.tv_8:
                set_select(tv_8);
                str = viewGetValue(tv_8);
                show("单位", list_dw);
                break;
            case R.id.bt:
                App.toask("保存成功");
                finish();
                break;
            case R.id.ll_drug:
                if(iv_drug.isSelected()){
                    iv_drug.setSelected(false);
                }else{
                    iv_drug.setSelected(true);
                }
                break;
        }
    }

    private void show_time() {
        if(dialog_time == null){
            dialog_time = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    set_time(hourOfDay,minute);
                }
            },24,60,true);
            dialog_time.updateTime(8,0);
        }
        dialog_time.show();
    }

    private void show_time3() {
        if(dialog_time3 == null){
            dialog_time3 = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    set_time(hourOfDay,minute);
                }
            },24,60,true);
            dialog_time3.updateTime(18,0);
        }
        dialog_time3.show();
    }

    private void show_time2() {
        if(dialog_time2 == null){
            dialog_time2 = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    set_time(hourOfDay,minute);
                }
            },24,60,true);
            dialog_time2.updateTime(13,0);
        }
        dialog_time2.show();
    }

    public void show(String title,List list){
        if (dialog_name == null) {
            dialog_name = new Dialog(this, R.style.Dialog);
            dialog_name.setContentView(R.layout.dialog_select);
            tv_title = (TextView) dialog_name.findViewById(R.id.tv_title);
            pv_name = (PickerView) dialog_name.findViewById(R.id.pv);
            pv_name.setOnSelectListener(new PickerView.onSelectListener() {
                @Override
                public void onSelect(String text) {
                    str = text;
                }
            });
            dialog_name.findViewById(R.id.tv_yes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    set_select_value();
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
        tv_title.setText(title);
        pv_name.setData(list);
        for (int i = 0; i < list.size(); i++) {
            if (str.indexOf(list.get(i).toString()) >= 0) {
                pv_name.setSelected(i);//设置选中项
            }
        }
        dialog_name.show();
    }

    private void set_select_value(){
        if(tv_1.isSelected()){
            tv_1.setText(str);
        }else if(tv_2.isSelected()){
            if(str.equals("2")){
                ll_2.setVisibility(View.GONE);
                ll_3.setVisibility(View.VISIBLE);
            }else if(str.equals("3")){
                ll_2.setVisibility(View.VISIBLE);
                ll_3.setVisibility(View.VISIBLE);
            }else{
                ll_2.setVisibility(View.GONE);
                ll_3.setVisibility(View.GONE);
            }
            tv_2.setText(str);
        }else if (tv_4.isSelected()){
            tv_4.setText(str);
        }else if (tv_6.isSelected()) {
            tv_6.setText(str);
        }else if (tv_8.isSelected()) {
            tv_8.setText(str);
        }
    }

    private void set_select(TextView tv){
        tv_1.setSelected(false);
        tv_2.setSelected(false);
        tv_3.setSelected(false);
        tv_5.setSelected(false);
        tv_7.setSelected(false);
        tv_4.setSelected(false);
        tv_6.setSelected(false);
        tv_8.setSelected(false);
        tv.setSelected(true);
    }

    private void set_time(int hourOfDay,int minute){
        String h = "" + hourOfDay;
        String m = "" + minute;
        if (hourOfDay < 10){
            h = "0"+hourOfDay;
        }
        if (minute < 10){
            m = "0"+minute;
        }
        if(tv_3.isSelected()){
            tv_3.setText(h+":"+m);
        }
        if(tv_5.isSelected()){
            tv_5.setText(h+":"+m);
        }
        if(tv_7.isSelected()){
            tv_7.setText(h+":"+m);
        }
    }
}
