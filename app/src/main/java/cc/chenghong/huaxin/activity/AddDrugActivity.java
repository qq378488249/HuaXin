package cc.chenghong.huaxin.activity;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

import cc.chenghong.huaxin.App;
import cc.chenghong.huaxin.adapter.ZxysGridViewAdapter;
import cc.chenghong.huaxin.api.Api;
import cc.chenghong.huaxin.camera_code.UITools;
import cc.chenghong.huaxin.entity.Yyxx0;
import cc.chenghong.huaxin.request.AsyncHttpParams;
import cc.chenghong.huaxin.request.AsyncHttpRequest;
import cc.chenghong.huaxin.thread.UpFilesThread;
import cc.chenghong.huaxin.utils.BitmapUtils;
import cc.chenghong.huaxin.view.PickerView;
import de.greenrobot.event.EventBus;
import ligth_blue.view.NoScrollGridView;

/**
 * 新建用药信息
 */
public class AddDrugActivity extends BaseActivity {
    @ViewInject(R.id.tv_name)
    TextView tv_name;
    @ViewInject(R.id.tv_zs)
    TextView tv_zs;
    @ViewInject(R.id.tv_zw)
    TextView tv_zw;
    @ViewInject(R.id.tv_ws)
    TextView tv_ws;
    @ViewInject(R.id.tv_count)
    TextView tv_count;
    @ViewInject(R.id.tv_cs)
    TextView tv_cs;
    @ViewInject(R.id.tv1a)
    TextView tv1a;
    @ViewInject(R.id.tv1b)
    TextView tv1b;
    @ViewInject(R.id.tv2a)
    TextView tv2a;
    @ViewInject(R.id.tv2b)
    TextView tv2b;
    @ViewInject(R.id.tv3a)
    TextView tv3a;
    @ViewInject(R.id.tv3b)
    TextView tv3b;
    @ViewInject(R.id.iv)
    ImageView iv;
    @ViewInject(R.id.iv_switch)
    ImageView iv_switch;
    @ViewInject(R.id.ll1)
    LinearLayout ll1;
    @ViewInject(R.id.ll2)
    LinearLayout ll2;
    @ViewInject(R.id.ll3)
    LinearLayout ll3;
    @ViewInject(R.id.gv)
    NoScrollGridView gv;

    Dialog dialog_name;
    TimePickerDialog dialog_time;
    TimePickerDialog dialog_time2;
    TimePickerDialog dialog_time3;

    List<String> list_sl = new ArrayList<String>();
    List<String> list_cs = new ArrayList<String>();
    List<String> list_dw = new ArrayList<String>();

    PickerView pv_name;
    String str;
    TextView tv;
    Dialog dialog_iv;
    String photoPath = "";
    String ypmc;

    //进度弹窗
    Dialog dialog_progress;
    //进度弹窗文字
    TextView tv_progress;
    //上传文件线程
    UpFilesThread thread;
    ArrayList<String> listGv = new ArrayList<String>();
    /**
     * 老图片的集合
     */
    ArrayList<String> listOldImgs = new ArrayList<String>();
    /**
     * 本地图片数量
     */
    int load_imgs_count = 0;
    BaseAdapter adapterGv;

    Yyxx0 yyxx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.activity_add_drug);
        EventBus.getDefault().register(this);
        initXutils();
        setTitleName("新建用药信息");
        tv_rigth.setVisibility(View.VISIBLE);
        tv_rigth.setText("保存");
        iv_switch.setSelected(true);
        for (int i = 0; i < 21; i++) {
            list_sl.add("" + i);
        }
        for (int i = 1; i < 4; i++) {
            list_cs.add("" + i);
        }
        for (int i = -1; i < 21; i++) {
            if (i == -1) {
//                list_dw.add("1/2");
                list_dw.add("0.25");
            } else if (i == 0) {
//                list_dw.add("1/4");
                list_dw.add("0.5");
            } else {
                list_dw.add("" + i);
            }
        }
        initGvAdapter();
        if (code == 2) {//修改用药信息
            setTitleName("修改用药信息");
            yyxx = (Yyxx0) getIntent().getSerializableExtra("data");
            updateUi(yyxx);
        }
    }

    private void updateUi(Yyxx0 data) {
        if (yyxx != null) {
            tv_name.setText(yyxx.getTitle() + "");
            tv_count.setText(yyxx.getBuy_num() + "");
            tv_cs.setText(yyxx.getEat_num() + "");

            if (yyxx.getIs_notice() != null && yyxx.getIs_notice().equals("1")) {
                iv_switch.setSelected(true);
            } else {
                iv_switch.setSelected(false);
            }

            if (yyxx.getZaoshang_time() != null) {
                show_time();
                dialog_time.dismiss();
                String s[] = yyxx.getZaoshang_time().split(":");
//                int h = 0;
//                int m = 0;
//                if (Integer.valueOf(s[0])<10){
//                    h = Integer.valueOf(s[0].substring(1));
//                }
//                if (Integer.valueOf(s[1])<10){
//                    h = Integer.valueOf(s[1].substring(1));
//                }
                dialog_time.updateTime(Integer.valueOf(s[0]), Integer.valueOf(s[1]));
                setSjd(Integer.valueOf(s[0]), tv_zs);
                tv1a.setText(yyxx.getZaoshang_time() + "");
                tv1b.setText(yyxx.getZaoshang_num() + "");
                ll1.setVisibility(View.VISIBLE);
            } else {
                ll1.setVisibility(View.GONE);
            }
            if (yyxx.getZhongwu_time() != null) {
                show_time2();
                dialog_time2.dismiss();
                String s[] = yyxx.getZhongwu_time().split(":");
                dialog_time2.updateTime(Integer.valueOf(s[0]), Integer.valueOf(s[1]));
                ll2.setVisibility(View.VISIBLE);
                setSjd(Integer.valueOf(s[0]), tv_zw);
                tv2a.setText(yyxx.getZhongwu_time() + "");
                tv2b.setText(yyxx.getZhongwu_num() + "");
            } else {
                ll2.setVisibility(View.GONE);
            }
            if (yyxx.getWanshang_time() != null) {
                show_time3();
                dialog_time3.dismiss();
                String s[] = yyxx.getWanshang_time().split(":");
                dialog_time3.updateTime(Integer.valueOf(s[0]), Integer.valueOf(s[1]));
                ll3.setVisibility(View.VISIBLE);
                setSjd(Integer.valueOf(s[0]), tv_ws);
                tv3a.setText(yyxx.getWanshang_time() + "");
                tv3b.setText(yyxx.getWanshang_num() + "");
            } else {
                ll3.setVisibility(View.GONE);
            }

            ArrayList<String> mList = new ArrayList<>();
            if (yyxx.getImg_url() != null && !yyxx.getImg_url().equals("")) {
                String srr[] = yyxx.getImg_url().split(",");
                listGv.clear();
                for (String s : srr) {
                    listGv.add(s);
                    listOldImgs.add(s);
                }
                listGv.add(IV_ADD);
                listOldImgs.add(IV_ADD);
                adapterGv.notifyDataSetChanged();
            }

            tv_cs.setText(yyxx.getEat_num() + "");
        }
    }

    private void initGvAdapter() {
        if (listGv.size() == 0) {
            listGv.add(IV_ADD);
        }
        if (adapterGv == null) {
            adapterGv = new ZxysGridViewAdapter(this, listGv);
        }
        gv.setAdapter(adapterGv);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == listGv.size() - 1) {//如果是最后一个，说明是添加图片按钮
                    if (listGv.size() < 7) {
                        show_iv();
                    } else {
                        toask("最多只能选择6张照片");
                    }
                } else {
                    show(i);
                }
            }
        });
    }

    ImageView iv_dialog;

    private void show(final int i) {
        if (dialog == null) {
            dialog = new Dialog(this, R.style.Dialog);
            dialog.setContentView(R.layout.dialog_image);
            dialog.findViewById(R.id.iv).setVisibility(View.GONE);
            LinearLayout ll = (LinearLayout) dialog.findViewById(R.id.ll);
            setViewWidth(ll, getWidth() - UITools.dip2px(40));
            dialog.findViewById(R.id.tv_no).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
//        if (listGv != null) {
//            if (listGv.get(i) != null) {
//                if (listGv.get(i).indexOf("http:") != -1) {//网络图片
//                    com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(listGv.get(i), iv_dialog);
//                } else {//本地图片
//                    ImageLoader.getInstance().loadImage(listGv.get(i), iv_dialog);
//                }
//            }
//        }
        dialog.findViewById(R.id.tv_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listGv.remove(i);
                adapterGv.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void show_progress(String s) {
        if (dialog_progress == null) {
            dialog_progress = new Dialog(this, R.style.Dialog);
            dialog_progress.setContentView(R.layout.dialog_progress);
            LinearLayout ll = (LinearLayout) dialog_progress.findViewById(R.id.ll);
            tv_progress = (TextView) dialog_progress.findViewById(R.id.tv_content);
            setViewLayoutParams(ll, getWidth() / 3 * 2, getHeight() / 5);
            dialog_progress.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (thread != null && thread.isRun()) {
                        try {
                            thread.close();
//                    thread.interrupt();
//                            thread = null;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        if (s.equals("")) {
            tv_progress.setText("加载中...");
        } else {
            tv_progress.setText(s);
        }
        dialog_progress.show();
    }

    public void show(String title, List list) {
        if (dialog_name == null) {
            dialog_name = new Dialog(this, R.style.Dialog);
            dialog_name.setContentView(R.layout.dialog_select);
            tv = (TextView) dialog_name.findViewById(R.id.tv_title);
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
        tv.setText(title);
        pv_name.setData(list);
        for (int i = 0; i < list.size(); i++) {
            if (str.equals(list.get(i).toString())) {
                pv_name.setSelected(i);//设置选中项
            }
        }
        dialog_name.show();
    }

    private void show_time() {
        if (dialog_time == null) {
            dialog_time = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    set_time(hourOfDay, minute);
                }
            }, 24, 60, true);
            dialog_time.updateTime(8, 0);
        }
        dialog_time.show();
    }

    private void show_time3() {
        if (dialog_time3 == null) {
            dialog_time3 = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    set_time(hourOfDay, minute);
                }
            }, 24, 60, true);
            dialog_time3.updateTime(18, 0);
        }
        dialog_time3.show();
    }

    private void show_time2() {
        if (dialog_time2 == null) {
            dialog_time2 = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    set_time(hourOfDay, minute);
                }
            }, 24, 60, true);
            dialog_time2.updateTime(13, 0);
        }
        dialog_time2.show();
    }

    private void set_time(int hourOfDay, int minute) {
        String h = "" + hourOfDay;
        String m = "" + minute;
        if (hourOfDay < 10) {
            h = "0" + hourOfDay;
        }
        if (minute < 10) {
            m = "0" + minute;
        }
        if (tv1a.isSelected()) {
            setSjd(hourOfDay, tv_zs);
            tv1a.setText(h + ":" + m);
        }
        if (tv2a.isSelected()) {
            setSjd(hourOfDay, tv_zw);
            tv2a.setText(h + ":" + m);
        }
        if (tv3a.isSelected()) {
            setSjd(hourOfDay, tv_ws);
            tv3a.setText(h + ":" + m);
        }
    }

    /**
     * 设置时间段
     *
     * @param hours
     * @param tv
     */
    void setSjd(int hours, TextView tv) {
        if (hours >= 0 && hours < 11) {
            tv.setText("早上");
        }
        if (hours >= 11 && hours < 13) {
            tv.setText("中午");
        }
        if (hours >= 13 && hours < 18) {
            tv.setText("下午");
        }
        if (hours >= 18) {
            tv.setText("晚上");
        }
    }

    private void set_select_value() {
        if (tv_count.isSelected()) {
            tv_count.setText(str);
        } else if (tv_cs.isSelected()) {
            if (str.equals("2")) {
                ll1.setVisibility(View.VISIBLE);
                ll2.setVisibility(View.GONE);
                ll3.setVisibility(View.VISIBLE);
            } else if (str.equals("3")) {
                ll1.setVisibility(View.VISIBLE);
                ll2.setVisibility(View.VISIBLE);
                ll3.setVisibility(View.VISIBLE);
            } else if (str.equals("1")) {
                ll1.setVisibility(View.VISIBLE);
                ll2.setVisibility(View.GONE);
                ll3.setVisibility(View.GONE);
            }
            if (!iv_switch.isSelected()) {//未选择服药提醒
                ll1.setVisibility(View.GONE);
                ll2.setVisibility(View.GONE);
                ll3.setVisibility(View.GONE);
            }
            tv_cs.setText(str);
        } else if (tv1b.isSelected()) {
            tv1b.setText(str);
        } else if (tv2b.isSelected()) {
            tv2b.setText(str);
        } else if (tv3b.isSelected()) {
            tv3b.setText(str);
        }
    }

    List<String> listUpload = new ArrayList<>();

    private void compressedImage() {
        listUpload.clear();
        listUpload.addAll(listGv);
        for (int i = 0; i < listGv.size(); i++) {
            if (listGv.get(i).indexOf("http:") == -1 && !listGv.get(i).equals(BaseActivity.IV_ADD)) {//本地目录
                String strImage = BitmapUtils.savaImage2SD(listGv.get(i));
                listUpload.set(i, strImage);
            }
        }
    }

    @OnClick({R.id.iv, R.id.iv_switch, R.id.ll1, R.id.ll2, R.id.ll3, R.id.tv_name, R.id.tv_count,
            R.id.tv_cs, R.id.tv1a, R.id.tv1b, R.id.tv2a, R.id.tv2b, R.id.tv3a, R.id.tv3b, R.id.tv_rigth})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_name:
                Intent intent = newIntent(YpmcActivity.class);
                startActivityForResult(intent, 1000);
                break;
            case R.id.tv_rigth:
                if (viewIsNull(tv_name)) {
                    toask("请选择药品名称");
                    return;
                }
                if (code == 2) {//修改用药信息
                    if (listOldImgs != null && listGv.size() == listOldImgs.size() && listGv.containsAll(listOldImgs)) {//没有更改图片
                        submit(null);
                    } else {
                        if (listGv.size() < 2) {//删除了所有图片
                            submit("");
                        } else {
                            load_imgs_count = 0;
                            for (String s : listGv) {
                                if (s.indexOf("http:") == -1 && !s.equals(IV_ADD)) {
                                    load_imgs_count++;
                                }
                            }
                            System.out.println(load_imgs_count);
                            if (load_imgs_count > 0) {//添加了新的本地图片
                                show_progress("修改中...");
                                compressedImage();
                                thread = new UpFilesThread(handler, listUpload);
                                thread.start();
                            } else {//只删除了服务器上的图片，未添加新图片
                                String str = "";
                                for (String s : listGv) {
                                    if (s.equals(IV_ADD)) continue;
                                    if (str.equals("")) {
                                        str = s;
                                    } else {
                                        str += "," + s;
                                    }
                                }
                                submit(str);
                            }
                        }
                    }
                } else {
                    if (listGv.size() < 2) {//没有上传图片
                        submit(null);
                    } else {
                        if (code == 2) {
                            show_progress("修改中...");
                        } else {
                            show_progress("添加中...");
                        }
                        compressedImage();
                        thread = new UpFilesThread(handler, listUpload);
                        thread.start();
                    }
                }
                break;
            case R.id.tv_count:
                setSelect(tv_count);
                str = viewGetValue(tv_count);
                show("购买数量", list_sl);
                break;
            case R.id.tv_cs:
                setSelect(tv_cs);
                str = viewGetValue(tv_cs);
                show("服用剂量", list_cs);
                break;
            case R.id.iv_switch:
                if (iv_switch.isSelected()) {
                    iv_switch.setSelected(false);
                    ll1.setVisibility(View.GONE);
                    ll2.setVisibility(View.GONE);
                    ll3.setVisibility(View.GONE);
                } else {
                    iv_switch.setSelected(true);
                    int cs = Integer.valueOf(viewGetValue(tv_cs));
                    switch (cs) {
                        case 1:
                            ll1.setVisibility(View.VISIBLE);
                            ll2.setVisibility(View.GONE);
                            ll3.setVisibility(View.GONE);
                            break;
                        case 2:
                            ll1.setVisibility(View.VISIBLE);
                            ll2.setVisibility(View.GONE);
                            ll3.setVisibility(View.VISIBLE);
                            break;
                        case 3:
                            ll1.setVisibility(View.VISIBLE);
                            ll2.setVisibility(View.VISIBLE);
                            ll3.setVisibility(View.VISIBLE);
                            break;
                    }
                }
                break;
            case R.id.tv1a:
                setSelect(tv1a);
                show_time();
                break;
            case R.id.tv1b:
                setSelect(tv1b);
                str = viewGetValue(tv1b);
                show("药品剂量", list_dw);
                break;
            case R.id.tv2a:
                setSelect(tv2a);
                show_time2();
                break;
            case R.id.tv2b:
                setSelect(tv2b);
                str = viewGetValue(tv2b);
                show("药品剂量", list_dw);
                break;
            case R.id.tv3a:
                setSelect(tv3a);
                show_time3();
                break;
            case R.id.tv3b:
                setSelect(tv3b);
                str = viewGetValue(tv3b);
                show("药品剂量", list_dw);
                break;
            case R.id.iv:
                show_iv();
                break;
        }
    }

    void setSelect(TextView tv) {
        tv_count.setSelected(false);
        tv_cs.setSelected(false);
        tv1b.setSelected(false);
        tv2b.setSelected(false);
        tv3b.setSelected(false);
        tv1a.setSelected(false);
        tv2a.setSelected(false);
        tv3a.setSelected(false);
        tv.setSelected(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent mIntent) {
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case 1000:
                    ypmc = (String) mIntent.getSerializableExtra("data");
                    tv_name.setText(ypmc);
                    break;
                case 2:
                    listGv.clear();
                    ArrayList<String> listString = mIntent.getStringArrayListExtra("data");
                    listGv.addAll(listString);
                    listGv.remove(IV_ADD);
                    listGv.add(IV_ADD);
                    if (adapterGv != null) adapterGv.notifyDataSetChanged();
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, mIntent);
    }

    void show_iv() {
        if (dialog_iv == null) {
            dialog_iv = new Dialog(this, R.style.Dialog);
            dialog_iv.setContentView(R.layout.dialog_grxx_sex);
            TextView tv = (TextView) dialog_iv.findViewById(R.id.tv_title);
            tv.setText("添加图片");
            dialog_iv.findViewById(R.id.ll_left).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    takePhoto();
                    dialog_iv.dismiss();
                }
            });
            dialog_iv.findViewById(R.id.ll_rigth).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = newIntent(PhotoSelectActivity.class);
                    intent.putExtra("data", listGv);
                    startActivityForResult(intent, 2);
                    dialog_iv.dismiss();
                }
            });
            dialog_iv.findViewById(R.id.iv_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog_iv.dismiss();
                }
            });
        }
        dialog_iv.show();
    }

    @Override
    protected void onPhotoTaked(String photoPath) {
        this.photoPath = photoPath;
        listGv.add(photoPath);
        listGv.remove(IV_ADD);
        listGv.add(IV_ADD);
        adapterGv.notifyDataSetChanged();
//        iv.setImageBitmap(BitmapFactory.decodeFile(photoPath));
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            hideProgress();
            if (message.what == 200) {
//                toask(message.obj);
                if (dialog_progress != null && dialog_progress.isShowing()) {
                    dialog_progress.dismiss();
                }
                submit(message.obj + "");
            } else {
                hideProgress();
                toask("操作失败");
            }
        }
    };

    public void onEventMainThread(String event) {
        ypmc = event;
        tv_name.setText(ypmc);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    void submit(String ima) {
        AsyncHttpParams requestParams = new AsyncHttpParams();
        String url = Api.yyxx_add;
        if (code == 2) {//修改用药信息
            url = Api.yyxx_updata;
            progress("修改中...");
            requestParams.put("id", yyxx.getId());
        } else {
            progress("添加中...");
            requestParams.put("user_id", App.getUser().getId());
        }
        requestParams.put("eat_num", viewGetValue(tv_cs));
        requestParams.put("title", viewGetValue(tv_name));
        requestParams.put("buy_num", viewGetValue(tv_count));
        if (iv_switch.isSelected()) {//服药提醒
            requestParams.put("is_notice", "1");
            if (viewGetValue(tv_cs).equals("1")) {
                requestParams.put("zaoshang_time", viewGetValue(tv1a));
                requestParams.put("zaoshang_num", viewGetValue(tv1b));
            }
            if (viewGetValue(tv_cs).equals("2")) {
                requestParams.put("zaoshang_time", viewGetValue(tv1a));
                requestParams.put("zaoshang_num", viewGetValue(tv1b));
                requestParams.put("wanshang_time", viewGetValue(tv3a));
                requestParams.put("wanshang_num", viewGetValue(tv3b));
            }
            if (viewGetValue(tv_cs).equals("3")) {
                requestParams.put("zaoshang_time", viewGetValue(tv1a));
                requestParams.put("zaoshang_num", viewGetValue(tv1b));
                requestParams.put("zhongwu_time", viewGetValue(tv2a));
                requestParams.put("zhongwu_num", viewGetValue(tv2b));
                requestParams.put("wanshang_time", viewGetValue(tv3a));
                requestParams.put("wanshang_num", viewGetValue(tv3b));
            }
        } else {
            requestParams.put("is_notice", "0");
        }
        if (ima != null) {
            requestParams.put("img_url", ima);
        }
        AsyncHttpRequest.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                hideProgress();
                if (i == 200) {
                    if (code == 2) {
                        toask("修改成功");
                    } else {
                        toask("添加成功");
                    }
                    finish();
                } else {
                    if (code == 2) {
                        toask("修改失败");
                    } else {
                        toask("添加失败");
                    }
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                no_network(throwable);
            }
        });
    }
}
