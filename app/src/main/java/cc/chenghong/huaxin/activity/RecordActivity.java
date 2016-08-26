package cc.chenghong.huaxin.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;


//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;

import java.util.ArrayList;
import java.util.List;

import cc.chenghong.huaxin.dialog.ThreeButtonDialog;
import cc.chenghong.huaxin.entity.Allergy;
import cc.chenghong.huaxin.entity.Habit;
import cc.chenghong.huaxin.view.FlowLayout;
import cc.chenghong.huaxin.view.PickerView;
import cc.chenghong.huaxin.view.TagAdapter;
import cc.chenghong.huaxin.view.TagFlowLayout;

/**
 * 健康档案 ligth blue 20160224
 */
public class RecordActivity extends BaseActivity {
    @ViewInject(R.id.iv)
    ImageView iv;
    @ViewInject(R.id.iv_drug)
    ImageView iv_drug;
    @ViewInject(R.id.iv_test)
    ImageView iv_test;
    @ViewInject(R.id.tv_name)
    TextView tv_name;
    @ViewInject(R.id.tv_sex)
    TextView tv_sex;
    @ViewInject(R.id.tv_age)
    TextView tv_age;
    @ViewInject(R.id.tv_weight)
    TextView tv_weight;
    @ViewInject(R.id.tv_height)
    TextView tv_height;
    @ViewInject(R.id.tv_habit)
    TextView tv_habit;
    @ViewInject(R.id.tv_work)
    TextView tv_work;
    @ViewInject(R.id.tv_phone)
    TextView tv_phone;
    @ViewInject(R.id.tv_vip)
    TextView tv_vip;
    @ViewInject(R.id.tv_home)
    TextView tv_home;
    @ViewInject(R.id.ll_drug)
    LinearLayout ll_drug;
    @ViewInject(R.id.ll_drug_content)
    LinearLayout ll_drug_conten;//用药信息内容布局
    @ViewInject(R.id.ll_test_content)
    LinearLayout ll_test_content;//体检信息内容布局

    TagFlowLayout fl_hibat;//习惯流布局
    TagAdapter<Habit> adapter_hibat;//习惯适配器
    TagFlowLayout fl_word;//劳动强度流布局
    TagAdapter<Habit> adapter_word;//劳动强度适配器
    TagFlowLayout fl_allergy;//药物禁忌流布局
    TagAdapter<Allergy> adapter_allergy;//药物禁忌适配器

    private TextView tv_title;
    private TextView tv_title_tv;
    private final int num = -1;
    private String s_name = "姓名";//姓名
    private String s_phone = "";//电话
    private String str;//临时字符串
    private Dialog dialog_name;//性别，年龄，体重，身高dialog
    private Dialog dialog_habit;//习惯dialog
    private Dialog dialog_home;//家用设备dialog
    private Dialog dialog_work;//劳动强度dialog
    private Dialog dialog_guomin;//药物过敏史dialog
    private PickerView pv_name;
    private List<Habit> list_habit = new ArrayList<Habit>();//习惯集合
    private List<Habit> list_word = new ArrayList<Habit>();//劳动强度集合
    private List<String> list_sex = new ArrayList<String>();//性别集合
    private List<String> list_age = new ArrayList<String>();//年龄集合
    private List<String> list_weight = new ArrayList<String>();//体重集合
    private List<String> list_height = new ArrayList<String>();//身高集合
    private List<Allergy> list_allergy = new ArrayList<Allergy>();//药物过敏集合
    private List<Object> list_remove = new ArrayList<Object>();//移除集合
    //    private List
    //拍照，图库选取，取消按钮
    private ThreeButtonDialog threeButtonDialog;
    //上传图片路径的地址
    private String uploadImg = "";

    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.activity_record);
        initXutils();
        setTitleName("健康档案");
        init();
//        log_i(TAG);
    }

    private void init() {
        Habit habit;
        habit = new Habit(i++, "抽烟", false);
        list_habit.add(habit);
        habit = new Habit(i++, "喝酒", false);
        list_habit.add(habit);
        habit = new Habit(i++, "无", false);
        list_habit.add(habit);
        threeButtonDialog = new ThreeButtonDialog(this).setFirstButtonText("拍照")
                .setBtn1Listener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        takePhoto();
                    }
                }).setThecondButtonText("相册")
                .setBtn2Listener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        pickPhoto();
                    }
                }).autoHide();
        list_sex.add("男");
        list_sex.add("女");
        for (int i = 1; i < 121; i++) {
            list_age.add(i + "");
        }
        for (int i = 20; i < 220; i++) {
            list_weight.add(i + "");
        }
        for (int i = 50; i < 251; i++) {
            list_height.add(i + "");
        }

        Allergy allergy;
        allergy = new Allergy(1, "青霉素类1", true);
        list_allergy.add(allergy);
        allergy = new Allergy(3, "青霉素类3", true);
        list_allergy.add(allergy);
        Allergy allergy1 = new Allergy(2, "青霉素类2", false);
        list_allergy.add(allergy1);
    }

    @OnClick({R.id.iv, R.id.tv_name, R.id.tv_sex, R.id.tv_age, R.id.tv_weight, R.id.tv_height, R.id.tv_habit,
            R.id.ll_work, R.id.ll_phone, R.id.ll_vip, R.id.ll_home, R.id.ll_drug, R.id.ll_chakan, R.id.ll_paizhao,
            R.id.ll_yuyin, R.id.ll_guomin, R.id.ll_jinji, R.id.ll_test})
    public void onclick(View v) {
        switch (v.getId()) {
            case R.id.iv:
                threeButtonDialog.show();
                break;
            case R.id.tv_name:
//                show_name();
                Intent intent = new Intent(RecordActivity.this, EditActivity.class);
                intent.putExtra("code", 1);
                intent.putExtra("value", s_name);
                startActivityForResult(intent, 1000);
                break;
            case R.id.tv_sex:
                if (tv_sex.getText().toString().equals("性别")) {
                    str = "男";
                    show_name("性别", list_sex, "男");
                } else {
                    str = tv_sex.getText().toString();
                    show_name("性别", list_sex, str);
                }
                select(tv_sex);
                break;
            case R.id.tv_age:
                if (tv_age.getText().toString().equals("年龄")) {
                    str = "30";
                    show_name("年龄", list_age, "30");
                } else {
                    str = tv_age.getText().toString();
                    show_name("年龄", list_age, str);
                }
                select(tv_age);
                break;
            case R.id.tv_weight:
                if (tv_weight.getText().toString().equals("体重")) {
                    str = "65";
                    show_name("体重（公斤）", list_weight, "65");
                } else {
                    str = tv_weight.getText().toString();
                    show_name("体重（公斤）", list_weight, str);
                }
                select(tv_weight);
                break;
            case R.id.tv_height:
                if (tv_height.getText().toString().equals("身高")) {
                    str = "170";
                    show_name("身高（厘米）", list_height, "170");
                } else {
                    str = tv_height.getText().toString();
                    show_name("身高（厘米）", list_height, str);
                }
                select(tv_height);
                break;
            case R.id.tv_habit:
                show_habit1();
                break;
            case R.id.ll_work:
                show_work();
                break;
            case R.id.ll_phone:
                Intent intent1 = new Intent(RecordActivity.this, EditActivity.class);
                intent1.putExtra("code", 2);
                intent1.putExtra("value", s_phone);
                startActivityForResult(intent1, 1000);
                break;
            case R.id.ll_vip:
                Intent intent2 = new Intent(RecordActivity.this, EditActivity.class);
                intent2.putExtra("code", 3);
                intent2.putExtra("value", tv_vip.getText().toString());
                startActivityForResult(intent2, 1000);
                break;
            case R.id.ll_home:
                show_home();
                break;
            case R.id.ll_drug:
                if (iv_drug.isSelected()) {
                    iv_drug.setSelected(false);
                    ll_drug_conten.setVisibility(View.GONE);
                } else {
                    iv_drug.setSelected(true);
                    ll_drug_conten.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.ll_chakan:
                startActivity(new Intent(RecordActivity.this, LookMyDrugActivity.class));
                break;
            case R.id.ll_paizhao:
                startActivity(new Intent(RecordActivity.this, BrowseDrugActivity.class));
                break;
            case R.id.ll_yuyin:
                startActivity(new Intent(RecordActivity.this, AddDrugActivity.class));
                break;
            case R.id.ll_guomin:
                show_guomin();
                break;
            case R.id.ll_jinji:

                break;
            case R.id.ll_test:
                if (ll_test_content.isSelected()) {
                    ll_test_content.setSelected(false);
                    ll_test_content.setVisibility(View.GONE);
                } else {
                    ll_test_content.setSelected(true);
                    ll_test_content.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    //药物过敏dialog
    private void show_guomin() {
        if (dialog_guomin == null) {
            dialog_guomin = new Dialog(this, R.style.Dialog);
            dialog_guomin.setContentView(R.layout.dialog_tv_n);
            TextView tv_title_tv = (TextView) dialog_guomin.findViewById(R.id.tv_title);
            tv_title_tv.setText("药物过敏史");
            TextView tv_no = (TextView) dialog_guomin.findViewById(R.id.tv_no);
            tv_no.setText("清除");
            dialog_guomin.findViewById(R.id.tv_add).setVisibility(View.VISIBLE);
            fl_allergy = (TagFlowLayout) dialog_guomin.findViewById(R.id.fl);
            adapter_allergy = new TagAdapter<Allergy>(list_allergy) {
                @Override
                public View getView(FlowLayout parent, int position, Allergy allergy) {
                    TextView textView = (TextView) getLayoutInflater().inflate(R.layout.tv_select, null);
                    if (allergy.isSelect()) {
//                        textView.setSelected(true);
                        textView.setBackgroundResource(R.drawable.shape_round_blue);
                    } else {
//                        textView.setSelected(false);
                        textView.setBackgroundResource(R.drawable.shape_round_gray_white);
                    }
                    textView.setText(allergy.getName());
                    return textView;
                }
            };
            fl_allergy.setAdapter(adapter_allergy);
            fl_allergy.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                @Override
                public boolean onTagClick(View view, int position, FlowLayout parent) {
                    if (list_allergy.get(position).isSelect()) {
                        list_allergy.get(position).setIsSelect(false);
                    } else {
                        list_allergy.get(position).setIsSelect(true);
                    }
                    adapter_allergy.notifyDataChanged();
                    return true;
                }
            });
//            fl_allergy.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
//                @Override
//                public void onSelected(Set<Integer> selectPosSet) {
//                    RecordActivity.this.setTitle("choose:" + selectPosSet.toString());
//                }
//            });
            dialog_guomin.findViewById(R.id.tv_yes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog_guomin.dismiss();
                }
            });
            dialog_guomin.findViewById(R.id.tv_no).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean b = true;//判断是否已经选中了过敏药物
                    for (int i = 0; i < list_allergy.size(); i++) {
                        if (list_allergy.get(i).isSelect()) {
                            b = false;
                            break;
                        }
                    }
                    if (b) {
                        dialog_guomin.dismiss();
                    }
                    if (list_allergy.size() <= 2) {
                        for (int i = 0; i < list_allergy.size(); i++) {
                            list_allergy.get(i).setIsSelect(false);
                        }
                        if (b) {
                            dialog_guomin.dismiss();
                        }
                        adapter_allergy.notifyDataChanged();
                    } else {
                        list_remove.clear();
                        for (int i = 0; i < list_allergy.size(); i++) {
                            if (i > 1) {
                                list_remove.add(list_allergy.get(i));
                            }
                            list_allergy.get(i).setIsSelect(false);
                        }
//                        list_allergy.remove(list_remove);
                        list_allergy.removeAll(list_remove);
                        adapter_allergy.notifyDataChanged();
                    }
                }
            });
            dialog_guomin.findViewById(R.id.tv_add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(RecordActivity.this, EditActivity.class);
                    intent1.putExtra("code", 4);
                    intent1.putExtra("id", list_allergy.get(list_allergy.size() - 1).getId());
                    list_allergy.size();
                    startActivityForResult(intent1, 1000);
//                    dialog_guomin.dismiss();
                }
            });
        }
        dialog_guomin.show();
    }

    private void show_habit1() {
        if (dialog_habit == null) {
            dialog_habit = new Dialog(this, R.style.Dialog);
            dialog_habit.setContentView(R.layout.dialog_tv_n);
            TextView tv_title_tv = (TextView) dialog_habit.findViewById(R.id.tv_title);
            tv_title_tv.setText("习惯");
            TextView tv_no = (TextView) dialog_habit.findViewById(R.id.tv_no);
            tv_no.setText("清除");
            fl_hibat = (TagFlowLayout) dialog_habit.findViewById(R.id.fl);
            adapter_hibat = new TagAdapter<Habit>(list_habit) {
                @Override
                public View getView(FlowLayout parent, int position, Habit allergy) {
                    TextView textView = (TextView) getLayoutInflater().inflate(R.layout.tv_select, null);
                    if (allergy.isSelect()) {
//                        textView.setSelected(true);
                        textView.setBackgroundResource(R.drawable.shape_round_blue);
                    } else {
//                        textView.setSelected(false);
                        textView.setBackgroundResource(R.drawable.shape_round_gray_white);
                    }
                    textView.setText(allergy.getName());
                    return textView;
                }
            };
            fl_hibat.setAdapter(adapter_hibat);
            fl_hibat.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                @Override
                public boolean onTagClick(View view, int position, FlowLayout parent) {
                    if (list_habit.get(position).isSelect()) {
                        list_habit.get(position).setIsSelect(false);
                    } else {
                        list_habit.get(position).setIsSelect(true);
                    }
                    if (position == 0 || position == 1) {
                        list_habit.get(list_habit.size() - 1).setIsSelect(false);
                    }
                    if (position == 2) {
                        list_habit.get(0).setIsSelect(false);
                        list_habit.get(1).setIsSelect(false);
                    }
                    adapter_hibat.notifyDataChanged();
                    return true;
                }
            });
            dialog_habit.findViewById(R.id.tv_yes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog_habit.dismiss();
                    str = "";
                    int count = 0;
                    for (int i = 0; i < list_habit.size(); i++) {//判断一共有多少个被选中了
                        if (list_habit.get(i).isSelect()) {
                            count++;
                        }
                    }
                    if (count == 2) {//如果选中了2个，必定是吸烟喝酒
                        str = "吸烟/喝酒";
                    } else {//否则说明只选了一个
                        for (int i = 0; i < list_habit.size(); i++) {//判断一共有多少个被选中了
                            if (list_habit.get(i).isSelect()) {
                                str = list_habit.get(i).getName();
                            }
                        }
                    }
                    tv_habit.setText(str);
                }
            });
            dialog_habit.findViewById(R.id.tv_no).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog_habit.dismiss();
                    str = "";
                    int count = 0;
                    for (int i = 0; i < list_habit.size(); i++) {//判断一共有多少个被选中了
                        if (list_habit.get(i).isSelect()) {
                            count++;
                        }
                    }
                    if (count == 2) {//如果选中了2个，必定是吸烟喝酒
                        str = "吸烟/喝酒";
                    } else if (count == 1) {//如果只选了一个
                        for (int i = 0; i < list_habit.size(); i++) {//判断一共有多少个被选中了
                            if (list_habit.get(i).isSelect()) {
                                str = list_habit.get(i).getName();
                            }
                        }
                    } else {//如果一个都没选
                        str = "无";
                    }
                    tv_habit.setText(str);
                }
            });
        }
        dialog_habit.show();
    }

    //习惯dialog
    private void show_habit(String title) {
        if (dialog_habit == null) {
            dialog_habit = new Dialog(this, R.style.Dialog);
            dialog_habit.setContentView(R.layout.dialog_tv);
            tv_title_tv = (TextView) dialog_habit.findViewById(R.id.tv_title);
            FlowLayout fl = (FlowLayout) dialog_habit.findViewById(R.id.fl);
            final TextView tv1 = (TextView) dialog_habit.findViewById(R.id.tv1);
            final TextView tv2 = (TextView) dialog_habit.findViewById(R.id.tv2);
            final TextView tv3 = (TextView) dialog_habit.findViewById(R.id.tv3);
            tv1.setText("吸烟");
            tv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tv1.isSelected()) {
                        tv1.setSelected(false);
                    } else {
                        tv1.setSelected(true);
                        tv3.setSelected(false);
                    }
                }
            });
            tv2.setText("喝酒");
            tv2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tv2.isSelected()) {
                        tv2.setSelected(false);
                    } else {
                        tv2.setSelected(true);
                        tv3.setSelected(false);
                    }
                }
            });
            tv3.setText("无");
            tv3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tv3.isSelected()) {
                        tv3.setSelected(false);
                    } else {
                        tv3.setSelected(true);
                        tv2.setSelected(false);
                        tv1.setSelected(false);
                    }
                }
            });
            dialog_habit.findViewById(R.id.tv_yes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    str = "";
                    if (tv1.isSelected() && tv2.isSelected()) {
                        str = tv1.getText().toString() + "/" + tv2.getText().toString();
                    } else if (tv1.isSelected()) {
                        str += tv1.getText().toString();
                    } else if (tv2.isSelected()) {
                        str = tv2.getText().toString();
                    } else {
                        str = "无";
                    }
                    dialog_habit.dismiss();
                    tv_habit.setText(str);
                }
            });
            dialog_habit.findViewById(R.id.tv_no).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog_habit.dismiss();
                }
            });
        }
        tv_title_tv.setText(title);
        dialog_habit.show();
    }

    //劳动强度dialog
    private void show_work() {
        if (dialog_work == null) {
            dialog_work = new Dialog(this, R.style.Dialog);
            dialog_work.setContentView(R.layout.dialog_tv);
            TextView tv_title_work = (TextView) dialog_work.findViewById(R.id.tv_title);
            tv_title_work.setText("劳动强度");
            FlowLayout fl = (FlowLayout) dialog_work.findViewById(R.id.fl);
            final TextView tv1 = (TextView) dialog_work.findViewById(R.id.tv1);
            final TextView tv2 = (TextView) dialog_work.findViewById(R.id.tv2);
            final TextView tv3 = (TextView) dialog_work.findViewById(R.id.tv3);
            final TextView tv4 = (TextView) dialog_work.findViewById(R.id.tv4);
            tv4.setVisibility(View.VISIBLE);
            tv1.setText("卧床休息");
            tv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tv1.isSelected()) {
                        tv1.setSelected(false);
                    } else {
                        tv1.setSelected(true);
                        tv2.setSelected(false);
                        tv3.setSelected(false);
                        tv4.setSelected(false);
                    }
                }
            });
            tv2.setText("轻体力");
            tv2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tv2.isSelected()) {
                        tv2.setSelected(false);
                    } else {
                        tv2.setSelected(true);
                        tv1.setSelected(false);
                        tv3.setSelected(false);
                        tv4.setSelected(false);
                    }
                }
            });
            tv3.setText("中体力");
            tv3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tv3.isSelected()) {
                        tv3.setSelected(false);
                    } else {
                        tv3.setSelected(true);
                        tv1.setSelected(false);
                        tv2.setSelected(false);
                        tv4.setSelected(false);
                    }
                }
            });
            tv4.setText("重体力");
            tv4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tv4.isSelected()) {
                        tv4.setSelected(false);
                    } else {
                        tv4.setSelected(true);
                        tv2.setSelected(false);
                        tv3.setSelected(false);
                        tv1.setSelected(false);
                    }
                }
            });
            dialog_work.findViewById(R.id.tv_yes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tv1.isSelected()) {
                        tv_work.setText(tv1.getText().toString());
                    }
                    if (tv2.isSelected()) {
                        tv_work.setText(tv2.getText().toString());
                    }
                    if (tv3.isSelected()) {
                        tv_work.setText(tv3.getText().toString());
                    }
                    if (tv4.isSelected()) {
                        tv_work.setText(tv4.getText().toString());
                    }
                    dialog_work.dismiss();
                }
            });
            dialog_work.findViewById(R.id.tv_no).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog_work.dismiss();
                }
            });
        }
        dialog_work.show();
    }

    // 家庭设备dialog
    private void show_home() {
        if (dialog_home == null) {
            dialog_home = new Dialog(this, R.style.Dialog);
            dialog_home.setContentView(R.layout.dialog_tv);
            TextView tv_title_tv = (TextView) dialog_home.findViewById(R.id.tv_title);
            tv_title_tv.setText("家用设备");
            FlowLayout fl = (FlowLayout) dialog_home.findViewById(R.id.fl);
            final TextView tv1 = (TextView) dialog_home.findViewById(R.id.tv1);
            final TextView tv2 = (TextView) dialog_home.findViewById(R.id.tv2);
            final TextView tv3 = (TextView) dialog_home.findViewById(R.id.tv3);
            tv1.setText("血压计");
            tv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tv1.isSelected()) {
                        tv1.setSelected(false);
                    } else {
                        tv1.setSelected(true);
                        tv3.setSelected(false);
                    }
                }
            });
            tv2.setText("血糖计");
            tv2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tv2.isSelected()) {
                        tv2.setSelected(false);
                    } else {
                        tv2.setSelected(true);
                        tv3.setSelected(false);
                    }
                }
            });
            tv3.setText("无");
            tv3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tv3.isSelected()) {
                        tv3.setSelected(false);
                    } else {
                        tv3.setSelected(true);
                        tv2.setSelected(false);
                        tv1.setSelected(false);
                    }
                }
            });
            dialog_home.findViewById(R.id.tv_yes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    str = "";
                    if (tv1.isSelected() && tv2.isSelected()) {
                        str = tv1.getText().toString() + "/" + tv2.getText().toString();
                    } else if (tv1.isSelected()) {
                        str += tv1.getText().toString();
                    } else if (tv2.isSelected()) {
                        str = tv2.getText().toString();
                    } else {
                        str = "无";
                    }
                    dialog_home.dismiss();
                    tv_home.setText(str);
                }
            });
            dialog_home.findViewById(R.id.tv_no).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog_home.dismiss();
                }
            });
        }
        dialog_home.show();
    }

    /**
     * 显示滚动选择器
     *
     * @param title  标题
     * @param list   显示的选项
     * @param defult 默认显示的选项
     */
    private void show_name(String title, final List list, final String defult) {
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
//                    if (list.equals(list_sex)) {
//                        tv.setText(str);
//                    } else if (list.equals(list_age)) {
//                        tv.setText(str + "岁");
//                    } else if (list.equals(list_weight)) {
//                        tv.setText(str + "公斤");
//                    } else {
//                        tv.setText(str + "厘米");
//                    }
//                    switch (tv.getId()) {
//                        case R.id.tv_age:
//                            tv.setText(str + "岁");
//                            break;
//                        case R.id.tv_weight:
//                            tv.setText(str + "公斤");
//                            break;
//                        case R.id.tv_height:
//                            tv.setText(str + "厘米");
//                            break;
//                    }
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
            if (defult.indexOf(list.get(i).toString()) >= 0) {
                pv_name.setSelected(i);//设置选中项
            }
        }
        dialog_name.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            switch (resultCode) {
                case 1://姓名
                    String s = data.getStringExtra("value");
                    s_name = s;
                    if (s.length() > 5) {
                        s = s.substring(0, 4) + "...";
                    }
                    tv_name.setText(s);
                    break;
                case 2://紧急联系人
                    String s2 = data.getStringExtra("value");
                    s_phone = s2;
                    tv_phone.setText(s_phone);
                    break;
                case 3://会员卡号
                    tv_vip.setText(data.getStringExtra("value"));
                    break;
                case 4:
                    list_allergy.add((Allergy) data.getSerializableExtra("value"));
                    adapter_allergy.notifyDataChanged();
                    break;
            }
        }
    }

    /**
     * 设置显示一个滚动项
     *
     * @param title  标题
     * @param defult tv的默认值
     * @param first  第一次选中的值
     * @param list   选项集合
     * @param tv     修改的tv
     */
    public void set_select(String title, String defult, String first, List list, TextView tv) {
        if (tv.getText().toString().equals(defult)) {
//            show_name(title, list, first, tv);
        } else {
//            show_name(title, list, tv.getText().toString(), tv);
        }
    }

    public void select(TextView tv) {
        tv_sex.setSelected(false);
        tv_age.setSelected(false);
        tv_weight.setSelected(false);
        tv_height.setSelected(false);
        tv.setSelected(true);
    }

    public void set_select_value() {
        if (tv_sex.isSelected()) {
            tv_sex.setText(str);
        } else if (tv_age.isSelected()) {
            tv_age.setText(str + "岁");
            if (str.indexOf("岁") >= 0) {
                tv_age.setText(str);
            }
        } else if (tv_weight.isSelected()) {
            tv_weight.setText(str + "公斤");
            if (str.indexOf("公斤") >= 0) {
                tv_weight.setText(str);
            }
        } else if (tv_height.isSelected()) {
            tv_height.setText(str + "厘米");
            if (str.indexOf("厘米") >= 0) {
                tv_height.setText(str);
            }
        }
    }

    @Override
    protected void onPhotoTaked(final String photoPath) {
        String path = photoPath;//图片路径
        String urla = null;//服务器地址
        uploadImg = photoPath;
//        //超级学童地址
        final String xt = "http://139.196.18.81/file_system/v1/file/upload";
        //华新地址
        final String hx = "http://121.199.9.217/ligaoxin/app/v1/file/upload";
        //广香花开地址
        String gx = "http://121.42.27.136:80/gxhk/app/v1/file/upload";
        progress("上传中...");
//        new Thread(){
//            @Override
//            public void run() {
//                MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE,"----------ThIs_Is_tHe_bouNdaRY_$", Charset.defaultCharset());
//                multipartEntity.addPart("file",new FileBody(new File(photoPath),"image/jpg"));
//                HttpPost request = new HttpPost(Api.file_upload);
//                request.setEntity(multipartEntity);
//                request.addHeader("Content-Type","multipart/form-data;boundary=----------ThIs_Is_tHe_bouNdaRY_$");
//                DefaultHttpClient httpClient = new DefaultHttpClient();
//                HttpResponse response;
//                try {
//                    response = httpClient.execute(request);
//                    HttpEntity responseEntity = response.getEntity();
//                    int resCode = response.getStatusLine().getStatusCode();
//                    InputStream content = responseEntity.getContent();
//                    //result.setResult(StringUtils.readString(content));
//                    String result = StringUtils.readString(content);
//                    ObjectResponse objectResponse =fromJson(result, ObjectResponse.class);
//                    Message message =Message.obtain();
//                    message.what = 200;
//                    message.obj = objectResponse.data;
//                    handler.sendMessage(message);
//                } catch (Exception e) {
//                    // TODO Auto-generated catch block
//                    Message message =Message.obtain();
//                    message.what = 404;
//                    handler.sendMessage(message);
//                    e.printStackTrace();
//                }
//
//                super.run();
//            }
//        }.start();

//        MyRequestParams params = new MyRequestParams();
//        try {
//            File file = new File(photoPath);
//            params.put("file",file);
//            System.out.println();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//
//        AsyncHttpClient client = new AsyncHttpClient();
//        client.addHeader("Content-Type","multipart/form-data;boundary=----------ThIs_Is_tHe_bouNdaRY_$");
//        progress("上传中...");
//        com.loopj.android.http.RequestParams requestParams = new com.loopj.android.http.RequestParams();
//        requestParams.put("file",new FileBody(new File(photoPath),"image/jpg"));
//        client.post(hx,requestParams, new AsyncHttpResponseHandler() {
//
//            @Override
//            public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                hideProgress();
////                toask(bytes.length);
//                String s = new String(bytes);
//                toask(s);
//                log_i(s);
//                log_w(s);
//                System.out.print(s);
////                BaseResponse baseResponse = fromJson(s.split("<br />")[s.split("<br />").length-1],BaseResponse.class);
////                if(baseResponse.isSuccess()){
////                    App.toask("上传成功");
////                }else{
////                    App.toask("上传失败"+baseResponse.getMessage());
////                }
//            }
//
//            @Override
//            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//                hideProgress();
//                toask(new String(bytes) + "");
//                toask(throwable.toString());
//                App.toask("上传失败");
//            }
//        });
    }

    android.os.Handler handler = new android.os.Handler(){

        @Override
        public void handleMessage(Message message) {
            hideProgress();
            if(message.what == 200){
                toask("上传成功");
//                ObjectResponse objectResponse = (ObjectResponse) message.obj;
//                getUrlBitmap(message.obj.toString(),iv);
                iv.setImageBitmap(BitmapFactory.decodeFile(uploadImg));
            }else{
                toask("上传失败");
            }
        }
    };
}
