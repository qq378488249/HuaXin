package cc.chenghong.huaxin.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cc.chenghong.huaxin.App;
import cc.chenghong.huaxin.api.Api;
import cc.chenghong.huaxin.dialog.AskDialog;
import cc.chenghong.huaxin.entity.Allergy;
import cc.chenghong.huaxin.entity.User;
import cc.chenghong.huaxin.response.BaseResponse;
import cc.chenghong.huaxin.thread.UpFileThread;
import cc.chenghong.huaxin.utils.BitmapUtils;
import cc.chenghong.huaxin.utils.ImageTool;
import cc.chenghong.huaxin.view.FlowLayout;
import cc.chenghong.huaxin.view.PickerView;
import cc.chenghong.huaxin.view.TagAdapter;
import cc.chenghong.huaxin.view.TagFlowLayout;

/**
 * 个人信息 20160321 ligth_blue
 */
public class GrxxActivity extends BaseActivity {
    @ViewInject(R.id.iv)
    ImageView iv;
    @ViewInject(R.id.tv1)
    TextView tv1;
    @ViewInject(R.id.tv2)
    TextView tv2;
    @ViewInject(R.id.tv3)
    TextView tv3;
    @ViewInject(R.id.tv4)
    TextView tv4;
    @ViewInject(R.id.tv5)
    TextView tv5;
    @ViewInject(R.id.tv6)
    TextView tv6;
    @ViewInject(R.id.tv7)
    TextView tv7;
    @ViewInject(R.id.tv8)
    TextView tv8;
    @ViewInject(R.id.tv9)
    TextView tv9;
    @ViewInject(R.id.tv10)
    TextView tv10;

    TextView tv_title;

    TextView tv;

    User user;
    List<String> list = new ArrayList<String>();
    Dialog dialog_iv;
    Dialog dialog_sex;
    Dialog dialog_name;//年龄。体重。身高弹窗
    Dialog dialog_select;//习惯，劳动强度，家用设备
    Dialog dialog_ld;//习惯，劳动强度，家用设备
    Dialog dialog_jy;//习惯，劳动强度，家用设备

    private List<String> list_age = new ArrayList<String>();//年龄集合
    private List<String> list_weight = new ArrayList<String>();//体重集合
    private List<String> list_height = new ArrayList<String>();//身高集合
    PickerView pv_name;//滚动选择器
    //上传图片路径的地址
    private String uploadImg = "";
    String str = "";//滚动选择器选择的数据

    List<Allergy> list_xg = new ArrayList<Allergy>();//习惯集合
    List<Allergy> list_ld = new ArrayList<Allergy>();//劳动强度集合
    List<Allergy> list_jy = new ArrayList<Allergy>();//家用设备集合

    TagAdapter<Allergy> tagAdapter;
    TagFlowLayout fl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.activity_grxx);
        initXutils();
        setTitleName("个人信息");
//        user = App.getUser();
        getUserData(true);
        iv_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!App.isMainRun) {
                    startActivity(MainActivity.class);
                    finish();
                } else {
                    finish();
                }
            }
        });
    }

    private void init() {

        for (int i = 1; i < 121; i++) {
            list_age.add(i + "");
        }
        for (int i = 20; i < 220; i++) {
            list_weight.add(i + "");
        }
        for (int i = 50; i < 251; i++) {
            list_height.add(i + "");
        }
//        System.out.println(user.toString());
        getUrlBitmap(user.getHead_url(), iv);
        isNull(tv1, user.getNick_name());
        if (user.getGender() == null || user.getGender().equals("-1")) {
            tv2.setText("未完善");
        } else if (user.getGender().equals("1")) {
            tv2.setText("男");
        } else {
            tv2.setText("女");
        }
        isNull(tv3, user.getAge());
        isNull(tv4, user.getWeight());
        isNull(tv5, user.getHeight());

        Allergy a;

        //习惯
        if (list_xg == null || list_xg.size() == 0) {
            a = new Allergy(false, "抽烟");
            list_xg.add(a);
            a = new Allergy(false, "喝酒");
            list_xg.add(a);
            a = new Allergy(false, "无");
            list_xg.add(a);
        }

        //劳动强度
        if (list_ld == null || list_ld.size() == 0) {
            a = new Allergy(false, "卧床休息");
            list_ld.add(a);
            a = new Allergy(false, "轻体力");
            list_ld.add(a);
            a = new Allergy(false, "中体力");
            list_ld.add(a);
            a = new Allergy(false, "重体力");
            list_ld.add(a);
        }

        //家用设备
        if (list_jy == null || list_jy.size() == 0) {
            a = new Allergy(false, "血压仪");
            list_jy.add(a);
            a = new Allergy(false, "血糖仪");
            list_jy.add(a);
            a = new Allergy(false, "无");
            list_jy.add(a);
        }

        isNull(tv6, user.getHabit());
        isNull(tv7, user.getLabour());
        isNull(tv8, user.getUrgent_mobile());
        isNull(tv9, user.getCard());
        isNull(tv10, user.getEquipment());
    }

    void isNull(TextView tv, String s) {
        if (s == null || s.equals("")) {
            tv.setText("未完善");
        } else {
            tv.setText(s);
        }
    }

    @OnClick({R.id.iv, R.id.ll2, R.id.ll1, R.id.ll1, R.id.ll3, R.id.ll4, R.id.ll5, R.id.ll6, R.id.ll7, R.id.ll8, R.id.ll9, R.id.ll10})
    public void onClick(View view) {
        if (user == null) {
            showAsk();
            return;
        }
        switch (view.getId()) {
            case R.id.iv:
                show_iv();
                break;
            case R.id.ll1:
                Intent intent = new Intent(this, XgxmActivity.class);
                intent.putExtra("code", 1);
                startActivity(intent);
                break;
            case R.id.ll2:
                show_sex();
                break;
            case R.id.ll3:
                set_select(tv3);
                if (viewGetValue(tv3).equals("未完善")) {
                    str = "40";
                } else {
                    str = tv3.getText().toString();
                }
                show_name("年龄（岁）", list_age, str);
                break;
            case R.id.ll4:
                set_select(tv4);
                if (viewGetValue(tv4).equals("未完善")) {
                    str = "60";
                } else {
                    str = tv4.getText().toString();
                }
                show_name("体重（千克）", list_weight, str);
                break;
            case R.id.ll5:
                set_select(tv5);
                if (viewGetValue(tv5).equals("未完善")) {
                    str = "170";
                } else {
                    str = tv5.getText().toString();
                }
                show_name("身高（厘米）", list_height, str);
                break;
            case R.id.ll6:
                show_select("选择习惯", list_xg, viewGetValue(tv6), 1);
                break;
            case R.id.ll7:
                show_ld(viewGetValue(tv7));
                break;
            case R.id.ll8:
                Intent intent8 = new Intent(this, XgxmActivity.class);
                intent8.putExtra("code", 4);
                startActivity(intent8);
                break;
            case R.id.ll9:
                Intent intent9 = new Intent(this, XgxmActivity.class);
                intent9.putExtra("code", 11);
                startActivity(intent9);
                break;
            case R.id.ll10:
                show_jy(viewGetValue(tv10));
                break;
        }
    }

    private void show_sex() {
        if (dialog_sex == null) {
            dialog_sex = new Dialog(this, R.style.Dialog);
            dialog_sex.setContentView(R.layout.dialog_grxx_sex);

            TextView tv = (TextView) dialog_sex.findViewById(R.id.tv_title);
            tv.setText("选择性别");
            TextView tv_1 = (TextView) dialog_sex.findViewById(R.id.tv_left);
            tv_1.setText("男");
            TextView tv_2 = (TextView) dialog_sex.findViewById(R.id.tv_rigth);
            tv_2.setText("女");

            ImageView iv1 = (ImageView) dialog_sex.findViewById(R.id.iv_left);
            iv1.setImageResource(R.drawable.b_0009);
            ImageView iv2 = (ImageView) dialog_sex.findViewById(R.id.iv_rigth);
            iv2.setImageResource(R.drawable.b_0008);

            dialog_sex.findViewById(R.id.ll_left).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RequestParams requestParams = new RequestParams();
                    requestParams.put("id", user.getId());
                    requestParams.put("gender", "1");
//                    progress("修改中...");
                    client().post(Api.user_update, requestParams, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int i, Header[] headers, byte[] bytes) {
                            hideProgress();
                            BaseResponse response = fromJson(bytes, BaseResponse.class);
                            if (response.isSuccess()) {
                                toask("修改成功");
                                tv2.setText("男");
                                user.setGender("1");
                                App.setUser(user);
                            } else {
                                toask("修改失败");
                            }
                        }

                        @Override
                        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                            no_network(throwable);
                        }
                    });
                    dialog_sex.dismiss();
                }
            });
            dialog_sex.findViewById(R.id.ll_rigth).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RequestParams requestParams = new RequestParams();
                    requestParams.put("id", user.getId());
                    requestParams.put("gender", "0");
//                    progress("修改中...");
                    client().post(Api.user_update, requestParams, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int i, Header[] headers, byte[] bytes) {
                            hideProgress();
                            BaseResponse response = fromJson(bytes, BaseResponse.class);
                            if (response.isSuccess()) {
                                toask("修改成功");
                                tv2.setText("女");
                                user.setGender("0");
                                App.setUser(user);
                            } else {
                                toask("修改失败");
                            }
                        }

                        @Override
                        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                            no_network(throwable);
                        }
                    });
                    dialog_sex.dismiss();
                }
            });
            dialog_sex.findViewById(R.id.iv_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog_sex.dismiss();
                }
            });
        }
        dialog_sex.show();
    }

    void show_iv() {
        if (dialog_iv == null) {
            dialog_iv = new Dialog(this, R.style.Dialog);
            dialog_iv.setContentView(R.layout.dialog_grxx_sex);
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
                    pickPhoto();
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
            if (defult.equals(list.get(i).toString())) {
                pv_name.setSelected(i);//设置选中项
            }
        }
        dialog_name.show();
    }

    private void set_select_value() {
        progress("修改中...");
        RequestParams requestParams = new RequestParams();
        requestParams.put("id", App.getUser().getId());
        if (tv3.isSelected()) {
            requestParams.put("age", str);
            client().post(Api.user_update, requestParams, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    hideProgress();
                    if (i == 200) {
                        toask("修改成功");
                        tv3.setText(str);
                        user.setAge(str);
                        App.setUser(user);
                    } else {
                        toask("修改失败");
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    no_network(throwable);
                }
            });
        }
        if (tv4.isSelected()) {
            requestParams.put("weight", str);
            client().post(Api.user_update, requestParams, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    hideProgress();
                    if (i == 200) {
                        toask("修改成功");
                        tv4.setText(str);
                        user.setWeight(str);
                        App.setUser(user);
                    } else {
                        toask("修改失败");
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    no_network(throwable);
                }
            });
        }
        if (tv5.isSelected()) {
            requestParams.put("height", str);
            client().post(Api.user_update, requestParams, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    hideProgress();
                    if (i == 200) {
                        toask("修改成功");
                        tv5.setText(str);
                        user.setHeight(str);
                        App.setUser(user);
                    } else {
                        toask("修改失败");
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    no_network(throwable);
                }
            });
        }
    }

    private void set_select(View view) {
        tv3.setSelected(false);
        tv4.setSelected(false);
        tv5.setSelected(false);
        view.setSelected(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent mIntent) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 3) {
            tv1.setText(mIntent.getStringExtra("data"));
        }
        switch (requestCode) {
            //从相册选择图片返回
            case PHOTO_PICKED_WITH_DATA:
                Uri uri = mIntent.getData();
                String currentFilePath = getPath(uri);
                //如果图片不在存储卡中，读取图片到缓存文件夹
                if (currentFilePath == null || currentFilePath.length() == 0) {
                    Log.i(TAG, "图片不在存储卡中！！！！》》》》》》》》》》》》》》");
                    progress("正在读取图片, 请稍后...");
                    ImageTool.saveToFile(this, uri, CapturePhotoFile, new Handler(
                            new Handler.Callback() {
                                @Override
                                public boolean handleMessage(Message arg0) {
                                    hideProgress();
                                    if (CapturePhotoFile.exists()) {
                                        cropImage();
                                    }
                                    return true;
                                }
                            }));
                } else {
                    //如果图片存在，直接读取
                    CapturePhotoFile = new File(currentFilePath);
                    if (CapturePhotoFile.exists()) {
                        cropImage();
                    }
                }
                break;
            //拍照返回
            case CAMERA_WITH_DATA:
                cropImage();
                break;
            //裁剪返回(裁剪返回以后，默认保存到CropPhotoFile)
            case PHOTO_CROP:
                if (CropPhotoFile != null && CropPhotoFile.exists()) {
                    String path = CropPhotoFile.getAbsolutePath();
                    Log.i("i", "裁剪后得到的图片的路径是 = " + path);
                    progress("上传中...");
                    uploadImg = BitmapUtils.savaImage2SD(path);
                    UpFileThread upFileThread = new UpFileThread(handler, uploadImg);
                    upFileThread.start();
//                    new Thread() {
//                        @Override
//                        public void run() {
//                            MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, "----------ThIs_Is_tHe_bouNdaRY_$", Charset.defaultCharset());
//                            multipartEntity.addPart("file", new FileBody(new File(uploadImg), "image/jpg"));
//                            HttpPost request = new HttpPost(Api.file_upload);
//                            request.setEntity(multipartEntity);
//                            request.addHeader("Content-Type", "multipart/form-data;boundary=----------ThIs_Is_tHe_bouNdaRY_$");
//                            DefaultHttpClient httpClient = new DefaultHttpClient();
//                            HttpResponse response;
//                            try {
//                                response = httpClient.execute(request);
//                                HttpEntity responseEntity = response.getEntity();
//                                int resCode = response.getStatusLine().getStatusCode();
//                                InputStream content = responseEntity.getContent();
//                                //result.setResult(StringUtils.readString(content));
//                                String result = StringUtils.readString(content);
//                                ObjectResponse<String> objectResponse = fromJson(result, ObjectResponse.class);
//                                Message message = Message.obtain();
//                                if (objectResponse.isSuccess()) {
//                                    message.what = 200;
//                                    message.obj = objectResponse.data;
//                                } else {
//                                    message.what = 404;
//                                }
//                                handler.sendMessage(message);
//                            } catch (ClientProtocolException e) {
//                                // TODO Auto-generated catch block
////                                hideProgress();
//                                Message message = Message.obtain();
//                                message.what = 404;
//                                handler.sendMessage(message);
//                                e.printStackTrace();
//                            } catch (IOException e) {
//                                // TODO Auto-generated catch block
////                                hideProgress();
//                                Message message = Message.obtain();
//                                message.what = 404;
//                                handler.sendMessage(message);
//                                e.printStackTrace();
//                            }
//                            super.run();
//                        }
//                    }.start();
                }
                break;
        }
    }

    android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(final Message message) {
            hideProgress();
            if (message.what == 200) {
//                toask("上传成功");
                RequestParams params = new RequestParams();
                params.put("id", user.getId());
                params.put("head_url", message.obj.toString());
                progress("上传中...");
                final String s_head = message.obj.toString();
                client().post(Api.user_update, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                        hideProgress();
                        BaseResponse response = fromJson(bytes, BaseResponse.class);
                        if (response.isSuccess()) {
                            toask("修改成功");
                            iv.setImageBitmap(BitmapFactory.decodeFile(uploadImg));
                            user.setHead_url(s_head);
                            App.setUser(user);
                        } else {
                            toask("修改失败");
                        }
                    }

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                        no_network(throwable);
                    }
                });
            } else {
                toask("上传失败");
            }
        }
    };

    AskDialog askDialog;

    public void getUserData(final boolean b) {
        if (b) progress("加载中...");
        RequestParams reques = new RequestParams();
        reques.put("id", App.getUser().getId());
        client().post(Api.user_get, reques, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                hideProgress();
                User u = fromJson(bytes, User.class);
                if (u.isSuccess()) {
//                    if (b)toask("加载用户资料成功");
                    user = u.data;
                    App.setUser(user);
                    init();
                } else {
                    showAsk();
                    user =null;
//                    toask("加载用户资料失败");
//                    init();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//                no_network(throwable);
                user =null;
                showAsk();
//                init();
            }
        });
    }

    boolean isReload = false;
    private void showAsk() {
        if (askDialog == null) {
            askDialog = new AskDialog(this,"提示","加载用户资料失败，是否重新加载？");
            askDialog.setTvNo("否").setTvNoOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    isReload = false;
                    askDialog.dismiss();
                    finish();
                }
            });
            askDialog.setTvYes("是").setTvYesOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    isReload = true;
                    getUserData(true);
                    askDialog.dismiss();
                }
            });
            askDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (!isReload){
                        finish();
                    }
                }
            });
        }
        askDialog.show();
    }

    void show_select(String title, final List<Allergy> list, String str, final int index) {
        for (Allergy allergy : list) {
            allergy.setIsSelect(false);
        }
        if (str != null && !str.equals("")) {
            String str1[] = str.split(",");
            for (int i = 0; i < list.size(); i++) {
                for (String s : str1) {
                    if (s.equals(list.get(i).getName())) {
                        list.get(i).setIsSelect(true);
                    }
                }
            }
        }

        if (dialog_select == null) {
            dialog_select = new Dialog(this, R.style.Dialog);
            dialog_select.setContentView(R.layout.dialog_tv_n);
            tv = (TextView) dialog_select.findViewById(R.id.tv_title);
            fl = (TagFlowLayout) dialog_select.findViewById(R.id.fl);
            tagAdapter = new TagAdapter<Allergy>(list) {
                @Override
                public View getView(FlowLayout parent, int position, Allergy allergy) {
                    TextView textView = (TextView) getLayoutInflater().inflate(R.layout.tv_select, null);
                    textView.setBackgroundResource(R.drawable.selector_record);
                    if (allergy.isSelect()) {
                        textView.setBackgroundResource(R.drawable.shape_round_blue);
                        textView.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        textView.setBackgroundResource(R.drawable.shape_round_gray_white);
                        textView.setTextColor(getResources().getColor(R.color.black));
                    }
                    textView.setText(allergy.getName());
                    return textView;
                }
            };
            fl.setAdapter(tagAdapter);
            fl.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                @Override
                public boolean onTagClick(View view, int position, FlowLayout parent) {
                    if (index == 1 || index == 3) {//习惯
                        if (position == 0 || position == 1) {
                            list.get(list.size() - 1).setIsSelect(false);
                            if (list.get(position).isSelect()) {
                                list.get(position).setIsSelect(false);
                            } else {
                                list.get(position).setIsSelect(true);
                            }
                        } else {
                            for (int i = 0; i < list.size(); i++) {
                                list.get(i).setIsSelect(false);
                            }
                            list.get(position).setIsSelect(true);
                        }

                    }
                    if (index == 2) {//劳动强度
                        for (int i = 0; i < list.size(); i++) {
                            list.get(i).setIsSelect(false);
                        }
                        list.get(position).setIsSelect(true);
                    }
                    tagAdapter.notifyDataChanged();
                    return false;
                }
            });
            dialog_select.findViewById(R.id.tv_yes).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    String s = "";
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).isSelect()) {
                            if (s.equals("")) {
                                s += list.get(i).getName();
                            } else {
                                s += "," + list.get(i).getName();
                            }
                        }
                    }
                    if (s.equals(viewGetValue(tv6))) {
                        dialog_select.dismiss();
                        return;
                    }
                    String url = "";
                    RequestParams params = new RequestParams();
                    if (index == 1) {
                        params.put("habit", s);
                    }
                    if (index == 2) {
                        params.put("labour", s);
                    }
                    if (index == 3) {
                        params.put("equipment", s);
                    }
                    params.put("id", user.getId());
                    progress("修改中...");
                    final String finalS = s;
                    client().post(Api.user_update, params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int i, Header[] headers, byte[] bytes) {
                            hideProgress();
                            if (i == 200) {
                                toask("修改成功");
                                tv6.setText(finalS);
                            } else {
                                toask("修改失败");
                            }
                        }

                        @Override
                        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                            no_network(throwable);
                        }
                    });
                    dialog_select.dismiss();
                }
            });
            dialog_select.findViewById(R.id.tv_no).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog_select.dismiss();
                }
            });
        }
        tagAdapter.notifyDataChanged();
        tv.setText(title);
        dialog_select.show();
    }

    TagFlowLayout fl_ld;

    void show_ld(String str) {
        for (Allergy allergy : list_ld) {
            allergy.setIsSelect(false);
        }
        if (str != null && !str.equals("")) {
            String str1[] = str.split(",");
            for (int i = 0; i < list_ld.size(); i++) {
                for (String s : str1) {
                    if (s.equals(list_ld.get(i).getName())) {
                        list_ld.get(i).setIsSelect(true);
                    }
                }
            }
        }

        if (dialog_ld == null) {
            dialog_ld = new Dialog(this, R.style.Dialog);
            dialog_ld.setContentView(R.layout.dialog_tv_n);
            TextView tv = (TextView) dialog_ld.findViewById(R.id.tv_title);
            tv.setText("选择劳动强度");
            fl_ld = (TagFlowLayout) dialog_ld.findViewById(R.id.fl);

            dialog_ld.findViewById(R.id.tv_yes).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    String s = "";
                    for (int i = 0; i < list_ld.size(); i++) {
                        if (list_ld.get(i).isSelect()) {
                            if (s.equals("")) {
                                s += list_ld.get(i).getName();
                            } else {
                                s += "," + list_ld.get(i).getName();
                            }
                        }
                    }
                    if (s.equals(viewGetValue(tv10))) {
                        dialog_ld.dismiss();
                        return;
                    }
                    String url = "";
                    RequestParams params = new RequestParams();
                    params.put("labour", s);
                    params.put("id", user.getId());
                    progress("修改中...");
                    final String finalS = s;
                    client().post(Api.user_update, params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int i, Header[] headers, byte[] bytes) {
                            hideProgress();
                            if (i == 200) {
                                toask("修改成功");
                                tv7.setText(finalS);
                            } else {
                                toask("修改失败");
                            }
                        }

                        @Override
                        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                            no_network(throwable);
                        }
                    });
                    dialog_ld.dismiss();
                }
            });
            dialog_ld.findViewById(R.id.tv_no).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog_ld.dismiss();
                }
            });
        }
        final TagAdapter<Allergy> tagAdapter_ld = new TagAdapter<Allergy>(list_ld) {
            @Override
            public View getView(FlowLayout parent, int position, Allergy allergy) {
                TextView textView = (TextView) getLayoutInflater().inflate(R.layout.tv_select, null);
                textView.setBackgroundResource(R.drawable.selector_record);
                if (allergy.isSelect()) {
                    textView.setBackgroundResource(R.drawable.shape_round_blue);
                    textView.setTextColor(getResources().getColor(R.color.white));
                } else {
                    textView.setBackgroundResource(R.drawable.shape_round_gray_white);
                    textView.setTextColor(getResources().getColor(R.color.black));
                }
                textView.setText(allergy.getName());
                return textView;
            }
        };
        fl_ld.setAdapter(tagAdapter_ld);
        fl_ld.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                for (int i = 0; i < list_ld.size(); i++) {
                    list_ld.get(i).setIsSelect(false);
                }
                list_ld.get(position).setIsSelect(true);
                tagAdapter_ld.notifyDataChanged();
                return false;
            }
        });
        dialog_ld.show();
    }

    TagFlowLayout fl_jy;

    void show_jy(String str) {
        for (Allergy allergy : list_jy) {
            allergy.setIsSelect(false);
        }
        if (str != null && !str.equals("")) {
            String str1[] = str.split(",");
            for (int i = 0; i < list_jy.size(); i++) {
                for (String s : str1) {
                    if (s.equals(list_jy.get(i).getName())) {
                        list_jy.get(i).setIsSelect(true);
                    }
                }
            }
        }

        if (dialog_jy == null) {
            dialog_jy = new Dialog(this, R.style.Dialog);
            dialog_jy.setContentView(R.layout.dialog_tv_n);
            TextView tv = (TextView) dialog_jy.findViewById(R.id.tv_title);
            tv.setText("家用设备");
            fl_jy = (TagFlowLayout) dialog_jy.findViewById(R.id.fl);

            dialog_jy.findViewById(R.id.tv_yes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String s = "";
                    for (int i = 0; i < list_jy.size(); i++) {
                        if (list_jy.get(i).isSelect()) {
                            if (s.equals("")) {
                                s += list_jy.get(i).getName();
                            } else {
                                s += "," + list_jy.get(i).getName();
                            }
                        }
                    }
                    if (s.equals(viewGetValue(tv10))) {
                        dialog_jy.dismiss();
                        return;
                    }
                    String url = "";
                    RequestParams params = new RequestParams();
                    params.put("equipment", s);
                    params.put("id", user.getId());
                    progress("修改中...");
                    final String finalS = s;
                    client().post(Api.user_update, params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int i, Header[] headers, byte[] bytes) {
                            hideProgress();
                            if (i == 200) {
                                toask("修改成功");
                                tv10.setText(finalS);
                            } else {
                                toask("修改失败");
                            }
                        }

                        @Override
                        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                            no_network(throwable);
                        }
                    });
                    dialog_jy.dismiss();
                }
            });
            dialog_jy.findViewById(R.id.tv_no).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog_jy.dismiss();
                }
            });
        }
        final TagAdapter<Allergy> tagAdapter = new TagAdapter<Allergy>(list_jy) {
            @Override
            public View getView(FlowLayout parent, int position, Allergy allergy) {
                TextView textView = (TextView) getLayoutInflater().inflate(R.layout.tv_select, null);
                textView.setBackgroundResource(R.drawable.selector_record);
                if (allergy.isSelect()) {
                    textView.setBackgroundResource(R.drawable.shape_round_blue);
                    textView.setTextColor(getResources().getColor(R.color.white));
                } else {
                    textView.setBackgroundResource(R.drawable.shape_round_gray_white);
                    textView.setTextColor(getResources().getColor(R.color.black));
                }
                textView.setText(allergy.getName());
                return textView;
            }
        };
        fl_jy.setAdapter(tagAdapter);
        fl_jy.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (position == 0 || position == 1) {
                    list_jy.get(list_jy.size() - 1).setIsSelect(false);
                    if (list_jy.get(position).isSelect()) {
                        list_jy.get(position).setIsSelect(false);
                    } else {
                        list_jy.get(position).setIsSelect(true);
                    }
                } else {
                    for (int i = 0; i < list_jy.size(); i++) {
                        list_jy.get(i).setIsSelect(false);
                    }
                    list_jy.get(position).setIsSelect(true);
                }
                tagAdapter.notifyDataChanged();
                return false;
            }
        });
        dialog_jy.show();
    }

    @Override
    protected void onRestart() {
        getUserData(false);
        super.onRestart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!App.isMainRun) {
            startActivity(MainActivity.class);
            finish();
        } else {
            finish();
        }
    }
}
