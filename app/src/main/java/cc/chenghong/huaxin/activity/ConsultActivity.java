package cc.chenghong.huaxin.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

import cc.chenghong.huaxin.App;
import cc.chenghong.huaxin.adapter.CommonAdapter;
import cc.chenghong.huaxin.adapter.ViewHolder;
import cc.chenghong.huaxin.adapter.ZxysGridViewAdapter;
import cc.chenghong.huaxin.api.Api;
import cc.chenghong.huaxin.camera_code.UITools;
import cc.chenghong.huaxin.entity.Page;
import cc.chenghong.huaxin.entity.Wdzx;
import cc.chenghong.huaxin.loader.ImageLoader;
import cc.chenghong.huaxin.thread.UpFilesThread;
import cc.chenghong.huaxin.utils.BitmapUtils;
import cc.chenghong.huaxin.view.RefreshViewPD;
import ligth_blue.view.NoScrollGridAdapter;
import ligth_blue.view.NoScrollGridView;

/**
 * 健康咨询
 */
public class ConsultActivity extends BaseActivity {
    @ViewInject(R.id.lv)//我的咨询
            ListView lv;
    @ViewInject(R.id.rv)
    RefreshViewPD rv;
    @ViewInject(R.id.iv)//
            ImageView iv;
    @ViewInject(R.id.ll1_content)//咨询医生
            LinearLayout ll1_content;
    @ViewInject(R.id.ll1)
    LinearLayout ll1;
    @ViewInject(R.id.ll2)
    LinearLayout ll2;
    @ViewInject(R.id.et_title)
    EditText et_title;
    @ViewInject(R.id.et_content)
    EditText et_content;
    @ViewInject(R.id.gv)
    NoScrollGridView gv;

    RequestParams requestParams = new RequestParams();
    Dialog dialog_iv;
    List<Wdzx> list = new ArrayList<Wdzx>();
    CommonAdapter<Wdzx> adapter;
    Page<Wdzx> page = new Page<Wdzx>(0, 10);
    ArrayList<String> listGv = new ArrayList<String>();
    ArrayList<String> listUpload = new ArrayList<String>();
    BaseAdapter adapterGv;
    //图片本地路径
    String photoPath = "";
    //是否点击的是我的咨询
    boolean isWdzx;
    /**
     * 图片添加
     */
    public final static String IV_ADD = "ADD";
    Dialog dialog;
    //进度弹窗
    Dialog dialog_progress;
    //进度弹窗文字
    TextView tv_progress;
    //上传文件线程
    UpFilesThread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.activity_consult);
        setTitleName("咨询医生");
        initXutils();
        ll1.setSelected(true);
        tv_rigth.setVisibility(View.VISIBLE);
        tv_rigth.setText("提交");
        initGvAdapter();
//        show_progress("");
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

    private void initGvAdapter() {
        if (listGv.size() == 0) {
            listGv.add(IV_ADD);
        }
        if (adapterGv == null) {
            adapterGv = new ZxysGridViewAdapter(this, listGv);
        }
//            adapterGv = new CommonAdapter<String>(this, listGv, R.layout.gv_item_photo) {
//                @Override
//                public void convert(ViewHolder helper, String item, int position) {
//                    ImageView iv = helper.getView(R.id.iv);
//                    setViewHeigth(iv, (getWidth() - UITools.dip2px(20)) / 4);
//                    iv.setImageResource(R.drawable.pictures_no);
//                    if (item.equals(IV_ADD)) {
//                        System.out.println(listGv.size());
//                        iv.setImageResource(R.drawable.jh);
//                    } else {
//                        System.out.println(listGv.size());
//                        ImageLoader.getInstance().loadImage(item, iv);
//                    }
//                }
//            };
//        }
        gv.setAdapter(adapterGv);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                toask(i + "\n" + listGv.get(i));
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
            LinearLayout ll = (LinearLayout) dialog.findViewById(R.id.ll);
            setViewWidth(ll,getWidth() - UITools.dip2px(40));
            iv_dialog = (ImageView) dialog.findViewById(R.id.iv);
            iv_dialog.setVisibility(View.GONE);
//            setViewLayoutParams(iv_dialog, getWidth(), getHeight() / 3 * 2);
            dialog.findViewById(R.id.tv_no).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
        if (listGv != null) {
            if (listGv.get(i) != null) {
                ImageLoader.getInstance().loadImage(listGv.get(i), iv_dialog);
            }
        }
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


    private void getData() {
        requestParams.put("page_index", page.page_index);
        requestParams.put("page_size", page.page_size);
        requestParams.put("user_id", App.getUser().getId());
        progress("加载中...");
        client().post(Api.consultation_page, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                hideProgress();
                Wdzx suggest0 = fromJson(bytes, Wdzx.class);
                if (suggest0.isSuccess()) {
//                    toask("加载成功");
                    if (suggest0.data.size() == 0) {
                        toask("暂无数据");
                        return;
                    }
                    if (page.page_index == 0) {
                        list.clear();
                        list.addAll(suggest0.data);
                    } else {
                        list.addAll(suggest0.data);
                    }
                    rv.setSelected(true);
                    if (adapter == null) {
                        initAdapter();
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    toask("加载失败");
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                no_network(throwable);
            }
        });
    }

    private void initAdapter() {
        rv.openPullDown();
        rv.openPullUp();
        rv.setListViewScrollListener(lv);
        rv.addOnSnapListener(new RefreshViewPD.OnSnapListener() {
            @Override
            public void onSnapToTop(int distance) {
                page.firstPage();
                getData();
            }

            @Override
            public void onSnapToBottom(int distance) {
                page.nextPage();
                getData();
            }
        });
        adapter = new CommonAdapter<Wdzx>(this, list, R.layout.lv_item_my_consult1) {
            @Override
            public void convert(ViewHolder helper, Wdzx item, final int position) {
                ImageView iv = helper.getView(R.id.iv);
                getUrlBitmap(App.getUser().getHead_url(), iv);
                if (App.getUser().getMobile() == null || App.getUser().getMobile().equals("")) {
                    toask("未获取到登录手机号，请重新登录");
                } else {
                    helper.setText(R.id.tv_name, App.getUser().getMobile().substring(0, 3) + "***" + App.getUser().getMobile().substring(8, App.getUser().getMobile().length()));
                }
                helper.setText(R.id.tv_time, formatDate(item.getCreated()));
                helper.setText(R.id.tv_title, item.getTitle());
                helper.setText(R.id.tv_content, item.getContent());
                helper.setText(R.id.tv_count, item.getReply_count());
                NoScrollGridView gv = helper.getView(R.id.gv);
                final ArrayList<String> list_urls = new ArrayList<String>();
                if (item.getImg_url() != null) {
                    String s[] = item.getImg_url().split(",");
                    for (int i = 0; i < s.length; i++) {
                        list_urls.add(s[i]);
                    }
                }
                gv.setAdapter(new NoScrollGridAdapter(ConsultActivity.this, list_urls));
                gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        photoBrower(i, list_urls);
                    }
                });
                helper.getView(R.id.ll).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), Zxxq1Activity.class);
                        intent.putExtra("data", list.get(position));
                        startActivity(intent);
                    }
                });
            }
        };
        lv.setAdapter(adapter);
    }

    @OnClick({R.id.ll1, R.id.ll2, R.id.tv_rigth, R.id.iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll1:
                setTitleName("咨询医生");
                ll1.setSelected(true);
                ll2.setSelected(false);
                rv.setVisibility(View.GONE);
                ll1_content.setVisibility(View.VISIBLE);
                tv_rigth.setVisibility(View.VISIBLE);
                break;
            case R.id.ll2:
                setTitleName("我的咨询");
                if (!rv.isSelected()) {
                    getData();
                }
                ll1.setSelected(false);
                ll2.setSelected(true);
//                rv.setSelected(true);
                rv.setVisibility(View.VISIBLE);
                ll1_content.setVisibility(View.GONE);
                tv_rigth.setVisibility(View.GONE);
                break;
            case R.id.iv:
                show_iv();
                break;
            case R.id.tv_rigth:
                if (viewIsNull(et_title)||viewGetValue(et_title).trim().equals("")) {
                    toask("请输入标题");
                    return;
                }
                if (viewIsNull(et_content)||viewGetValue(et_content).trim().equals("")) {
                    toask("请输入内容");
                    return;
                }

                if (listGv.size() > 1) {//说明上传了图片
//                    progress("上传图片中...");

                    show_progress("上传图片中...");
                    compressedImage();
                    System.out.println(listGv.toString());
                    thread = new UpFilesThread(handler, listUpload);
                    thread.start();
//                    thread.stop();
//                    new Thread() {
//                        @Override
//                        public void run() {
//                            MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, "----------ThIs_Is_tHe_bouNdaRY_$", Charset.defaultCharset());
//                            for (String s : listGv) {
//                                if (s.equals(IV_ADD)) {
//                                    continue;
//                                }
//                                multipartEntity.addPart("file[]", new FileBody(new File(s), "image/jpg"));
//                            }
////                            multipartEntity.addPart("file", new FileBody(new File(photoPath), "image/jpg"));
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
                } else {
                    Intent intent = newIntent(XzysActivity.class);
                    intent.putExtra("title", viewGetValue(et_title).trim());
                    intent.putExtra("content", viewGetValue(et_content).trim());
                    intent.putExtra("img_url", "");
                    startActivityForResult(intent, 1);
                }
                break;
        }
    }

    /**
     * 压缩图片
     */
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

    void show_iv() {
        if (dialog_iv == null) {
            dialog_iv = new Dialog(this, R.style.Dialog);
            dialog_iv.setContentView(R.layout.dialog_grxx_sex);
            TextView tv = (TextView) dialog_iv.findViewById(R.id.tv_title);
            tv.setText("选择上传途径");
            dialog_iv.findViewById(R.id.ll_left).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    try {
//                        //CapturePhotoFile = new File(PHOTO_DIR, UUID.randomUUID()+".jpg");
//                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(CapturePhotoFile));
//                        startActivityForResult(intent, CAMERA_WITH_DATA);
//                    } catch (Exception e) {
//                        App.toask("未找到系统相机程序");
//                    }
//                    if (listGv.size()<7){
                    takePhoto();
//                    }else{
//                        toask("最多只能选择6张照片");
//                    }
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

    public void hideDialogProgress() {
        dialog_progress.dismiss();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            hideDialogProgress();
            if (message.what == 200) {
                toask("上传图片成功");
                Intent intent = newIntent(XzysActivity.class);
                intent.putExtra("title", viewGetValue(et_title).trim());
                intent.putExtra("content", viewGetValue(et_content).trim());
                intent.putExtra("img_url", message.obj + "");
                startActivityForResult(intent, 1);
            } else if (message.what == 201) {//上传图片成功，但是用户取消操作
//                toask("上传图片成功，但是用户取消操作");
            } else {
                if (thread != null && !thread.isNext()) {//上传图片失败，并且用户取消操作
                    return;
                }
                toask("上传图片失败");
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent mIntent) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1://选择医生成功
                    listGv.clear();
                    listGv.add(IV_ADD);
                    et_title.setText("");
                    et_content.setText("");
                    adapterGv.notifyDataSetChanged();
                    setTitleName("我的咨询");
                    if (!rv.isSelected()) {
                        getData();
                    }
                    ll1.setSelected(false);
                    ll2.setSelected(true);
                    rv.setSelected(true);
                    rv.setVisibility(View.VISIBLE);
                    ll1_content.setVisibility(View.GONE);
                    tv_rigth.setVisibility(View.GONE);
                    getData();
                    break;
                case 2://相册选择图片成功
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

    private boolean isShowDialogProgress() {
        if (dialog_progress != null && dialog_progress.isShowing()) {
            return true;
        }
        return false;
    }

}
