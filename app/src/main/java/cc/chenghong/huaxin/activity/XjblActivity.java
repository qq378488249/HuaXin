package cc.chenghong.huaxin.activity;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cc.chenghong.huaxin.App;
import cc.chenghong.huaxin.adapter.ZxysGridViewAdapter;
import cc.chenghong.huaxin.api.Api;
import cc.chenghong.huaxin.camera_code.UITools;
import cc.chenghong.huaxin.request.AsyncHttpParams;
import cc.chenghong.huaxin.request.AsyncHttpRequest;
import cc.chenghong.huaxin.thread.UpFilesThread;
import cc.chenghong.huaxin.utils.BitmapUtils;
import cc.chenghong.huaxin.utils.DataUtils;

/**
 * 新建病例
 */
public class XjblActivity extends BaseActivity {
    @ViewInject(R.id.et_name)
    EditText et_title;
    @ViewInject(R.id.tv_time)
    TextView tv_time;
    @ViewInject(R.id.iv)
    ImageView iv;
    @ViewInject(R.id.gv)
    GridView gv;

    DatePickerDialog datePickerDialog;
    Dialog dialog_iv;
    Calendar calendar;
    /**
     * 图片添加
     */
    static final String IV_ADD = ConsultActivity.IV_ADD;
    Dialog dialog;
    ArrayList<String> listGv = new ArrayList<String>();
    ZxysGridViewAdapter adapterGv;

    String photoPath = "";

    AsyncHttpParams requestParams = AsyncHttpParams.New();

    //进度弹窗
    Dialog dialog_progress;
    //进度弹窗文字
    TextView tv_progress;
    //上传文件线程
    UpFilesThread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.activity_xjbl);
        setTitleName("新建病例");
        initXutils();
        tv_time.setText(DataUtils.getData("yyyy-MM-dd"));
        calendar = Calendar.getInstance();
        tv_rigth.setVisibility(View.VISIBLE);
        tv_rigth.setText("保存");
        initGvAdapter();
        tv_rigth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewIsNull(et_title)||viewGetValue(et_title).trim().equals("")) {
                    toask("请输入标题");
                    return;
                }
//                String s1 = viewGetValue(tv_time);
//                String s2 = DataUtils.getData("yyyy-MM-dd");
//                boolean b = DataUtils.data1_compare_data2(s2, s1, "yyyy-MM-dd");
//                System.out.println(b);
//                if (!b) {
//                    toask("选择的时间不能大于当前时间");
//                    return;
//                }
                if (listGv.size() < 2) {//没有图片
                    requestParams.put("user_id", App.getUser().getId());
                    requestParams.put("title", viewGetValue(et_title).trim());
                    requestParams.put("time", viewGetValue(tv_time));
                    submit();
                } else {//选择了图片
                    show_progress("添加中...");
                    compressedImage();
                    new UpFilesThread(handler, listUpload).start();
//                    progress("添加中...");
//                    new Thread() {
//                        @Override
//                        public void run() {
//                            MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, "----------ThIs_Is_tHe_bouNdaRY_$", Charset.defaultCharset());
//                            for (String s : listGv) {
//                                if (s.equals("")){
//                                    continue;
//                                }
//                                multipartEntity.addPart("file[]", new FileBody(new File(s), "image/jpg"));
//                            }
//                            HttpPost request = new HttpPost(Api.file_uploads);
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
//                                Message message = Message.obtain();
//                                message.what = 404;
//                                handler.sendMessage(message);
//                                e.printStackTrace();
//                            } catch (IOException e) {
//                                // TODO Auto-generated catch block
//                                Message message = Message.obtain();
//                                message.what = 404;
//                                handler.sendMessage(message);
//                                e.printStackTrace();
//                            }
//                            super.run();
//                        }
//                    }.start();
                }
            }
        });
        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_date();
            }
        });
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listGv.size()<7){
                    show_iv();
                }else{
                    toask("最多只能选择6张照片");
                }
            }
        });
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
//                    DatePicker datePicker = datePickerDialog.getDatePicker();
//                    int year = datePicker.getYear();
//                    int month = datePicker.getMonth();
//                    int day = datePicker.getDayOfMonth();
//                    int month1 = datePicker.getMonth();
//                    int day1 = datePicker.getDayOfMonth();
//                    String h = (month + 1) + "";
//                    String m = day + "";
//                    if (month < 9) {
//                        h = "0" + (month + 1);
//                    }
//                    if (day < 10) {
//                        m = "0" + day;
//                    }
//                    tv_time.setText(year + "-" + h + "-" + m + "");
//                    String strData = DataUtils.getData("yyyy-MM-dd");
//                    String strData2 = viewGetValue(tv_time);
//                    boolean b = DataUtils.data1_compare_data2(DataUtils.getData("yyyy-MM-dd"), year + "-" + month1 + "-" + day1, "yyyy-MM-dd");
//                    if (!b) {
//                        toask("选择的时间不能大于当前时间");
//                        tv_time.setText(DataUtils.getData("yyyy-MM-dd"));
//                        datePickerDialog.updateDate(Integer.valueOf(DataUtils.getData("yyyy")), Integer.valueOf(DataUtils.getData("MM")) - 1, Integer.valueOf(DataUtils.getData("dd")));
//                    }
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
                    String strData = DataUtils.getData("yyyy-MM-dd");
                    String strData2 = year+"-"+m+"-"+d;
                    boolean b = DataUtils.data1_compare_data2(strData, strData2, "yyyy-MM-dd");
                    System.out.println(b);
                    tv_time.setText(strData2);
                    if (!b) {
                        toask("选择的时间不能大于当前时间");
                        tv_time.setText(DataUtils.getData("yyyy-MM-dd"));
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
                    if (listGv.size()<7){
                        takePhoto();
                    }else{
                        toask("最多只能选择6张照片");
                    }
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
        iv.setImageBitmap(BitmapFactory.decodeFile(photoPath));
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            hideProgress();
            if (dialog_progress!=null){
                dialog_progress.dismiss();
            }
            if (message.what == 200) {
                requestParams.put("user_id", App.getUser().getId());
                requestParams.put("title", viewGetValue(et_title).trim());
                requestParams.put("img_url", message.obj + "");
                requestParams.put("time", viewGetValue(tv_time));
                submit();
            } else {
                toask("上传图片失败");
            }
        }
    };

    void submit() {
        progress("添加中...");
        AsyncHttpRequest.post(Api.bl_add, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                hideProgress();
                if (i == 200) {
                    toask("添加成功");
                    finish();
                } else {
                    toask("添加失败");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                no_network(throwable);
            }
        });
    }

    private void initGvAdapter() {
        listGv.add(IV_ADD);
        if (adapterGv == null) {
            adapterGv = new ZxysGridViewAdapter(this, listGv);
        }
        gv.setAdapter(adapterGv);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == listGv.size() - 1) {//如果是最后一个，说明是添加图片按钮
                    if (listGv.size()<7){
                        show_iv();
                    }else{
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
            View view = dialog.findViewById(R.id.ll);
            setViewWidth(view,getWidth() - UITools.dip2px(40));
//            iv_dialog = dialog.findViewById(R.id.iv).setVisibility(View.GONE);
//            iv_dialog.setVisibility(View.GONE);
//            ViewGroup.LayoutParams lp = iv_dialog.getLayoutParams();
//            lp.width = getWidth();
//            lp.height = getHeight() / 2;
//            iv_dialog.setLayoutParams(lp);
            dialog.findViewById(R.id.tv_no).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
//        if (listGv != null) {
//            if (listGv.get(i) != null) {
//                ImageLoader.getInstance().loadImage(listGv.get(i), iv_dialog);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent mIntent) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 2) {//选择图片成功
                listGv.clear();
                ArrayList<String> listString = mIntent.getStringArrayListExtra("data");
                listGv.addAll(listString);
                listGv.remove(IV_ADD);
                listGv.add(IV_ADD);
                adapterGv.notifyDataSetChanged();
            }
        }
        super.onActivityResult(requestCode, resultCode, mIntent);
    }
}
