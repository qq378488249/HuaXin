package cc.chenghong.huaxin.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.chenghong.huaxin.App;
import cc.chenghong.huaxin.adapter.MyGridViewAdapter;
import cc.chenghong.huaxin.entity.User;
import cc.chenghong.huaxin.request.AsyncHttpParams;
import cc.chenghong.huaxin.utils.DataUtils;
import cn.jpush.android.api.JPushInterface;
import ligth_blue.photoBrowse.ImagePagerActivity;

/**
 * 基类activity hcl 20160224
 */
public class BaseActivity extends BaseCaptureActivity {
    public String TAG = getClass().getSimpleName();// 标签
    protected View contentView;// 子容器
    protected LinearLayout ll_content;// 父容器
    public TextView tv_center;// 标题
    public LinearLayout ll_bar;//状态栏
    public ImageView iv_back;// 后退按钮
    public ImageView iv_rigth;// 右边按钮
    public Context context;// 上下文
    public TextView tv_rigth;//右边文字
    public Button bt_rigth;//右边按钮
    /**
     * 1血压，2血糖,3体脂
     */
    public int code;
    public static String fileName = "huaxin";

    public static final int NONE = 0;
    /**
     * 拍照
     */
    public static final int PHOTOHRAPH = 1;
    /**
     * 缩放
     */
    public static final int PHOTOZOOM = 2;
    /**
     * 结果
     */
    public static final int PHOTORESOULT = 3;// 结果
    /**
     * 图片类型
     */
    public static final String IMAGE_UNSPECIFIED = "image/*";
    /**
     * 本地图片路径
     */
    String imagePath;

    AsyncHttpParams requestParams = new AsyncHttpParams();
    Dialog dialog_iv_add;
    Dialog dialog;

    /**
     * 图片删除弹窗
     */
    Dialog dialog_photo_delete;
    ImageView iv_dialog;
    /**
     * 空图片添加地址
     */
    public static final String IV_ADD = "ADD";

    /**
     * 图片删除弹窗
     */
    protected void show_photo_delete(final int i, final List<String> list, final MyGridViewAdapter adapter) {
        if (dialog_photo_delete == null) {
            dialog_photo_delete = new Dialog(this, R.style.Dialog);
            dialog_photo_delete.setContentView(R.layout.dialog_image);
            iv_dialog = (ImageView) dialog_photo_delete.findViewById(R.id.iv);
            ViewGroup.LayoutParams lp = iv_dialog.getLayoutParams();
            lp.width = getWidth();
            lp.height = getHeight() / 2;
            iv_dialog.setLayoutParams(lp);

            dialog_photo_delete.findViewById(R.id.tv_yes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(i);
                    adapter.notifyDataSetChanged();
                    dialog_photo_delete.dismiss();
                }
            });
            dialog_photo_delete.findViewById(R.id.tv_no).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog_photo_delete.dismiss();
                }
            });
        }
        if (list != null && list.get(i) != null) {
            cc.chenghong.huaxin.loader.ImageLoader.getInstance().loadImage(list.get(i), iv_dialog);
        }
        dialog_photo_delete.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
//        initXutils();
        initBaseView();
        code = getIntent().getIntExtra("code", 1);
    }

    @Override
    protected void onPhotoTaked(String photoPath) {

    }

    protected void initBaseView() {
        // TODO Auto-generated method stub
        context = this;
//        registerReceiver(logout, new IntentFilter("logout"));
        tv_center = (TextView) findViewById(R.id.tv_center);
        ll_bar = (LinearLayout) findViewById(R.id.ll_bar);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_rigth = (ImageView) findViewById(R.id.iv_rigth);
        ll_content = (LinearLayout) findViewById(R.id.ll_content);
        tv_rigth = (TextView) findViewById(R.id.tv_rigth);
        bt_rigth = (Button) findViewById(R.id.bt_rigth);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                onclick_iv_back();
            }
        });
        if (App.getUser() != null) {//说明已经保存了用户的登录信息
            requestParams.put("user_id", App.getUser().getId());
            requestParams.put("id", App.getUser().getId());
        }
    }

    /**
     * 广播接收器，当收到logout广播时关闭页面
     */
    BroadcastReceiver logout = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            finish();
        }
    };

    /**
     * 后退按钮事件
     */
    public void onclick_iv_back() {
        // TODO Auto-generated method stub
        finish();
    }

    /**
     * 设置标题
     *
     * @param string
     */
    public void setTitleName(String string) {
        tv_center.setText(string);
    }

    /**
     * 加入页面内容布局
     *
     * @param layoutId 布局编号
     */
    protected void contentView(int layoutId) {
        contentView = getLayoutInflater().inflate(layoutId, null);
        if (ll_content.getChildCount() > 0) {
            ll_content.removeAllViews();
        }
        if (contentView != null) {
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            ll_content.addView(contentView, params);
        }
    }

    /**
     * 初始化xutils
     */
    public void initXutils() {
        TAG = getClass().getSimpleName();
        com.lidroid.xutils.ViewUtils.inject(this);//初始化xutils框架
        statusBar(ll_bar);
    }

    /**
     * 输出 i 级日志信息
     *
     * @param o
     */
    public void log_i(Object o) {
        Log.i(TAG, o + "");
    }

    /**
     * 输出 w 级日志信息
     *
     * @param o
     */
    public void log_w(Object o) {
        Log.w(TAG, o + "");
    }

    /**
     * 提供给子类重写的点击事件方法
     *
     * @param view
     */
    public void onClick(View view) {
    }

    /**
     * 判断控件是否为空
     *
     * @param view
     * @return true控件为空，false控件不为空
     */
    public boolean viewIsNull(View view) {
        TextView tv = (TextView) view;
        return tv.getText().toString().equals("");
    }

    public void viewIsNull(View view, boolean b) {
        TextView tv = (TextView) view;
        if (b) {
            toask(tv.getHint());
            return;
        }
    }

    /**
     * 把控件内的值转化为double型
     *
     * @param view
     * @return double
     */
    public double viewValueFormatDouble(View view) {
        double i = 0;
        TextView tv = (TextView) view;
        if (viewIsNull(view)) {
            i = 0;
        } else {
            i = Double.valueOf(tv.getText().toString());
        }
        return i;
    }

    /**
     * 获取控件的值
     *
     * @param view
     * @return 控件的值
     */
    public String viewGetValue(View view) {
        TextView tv = (TextView) view;
        return tv.getText().toString();
    }

    /**
     * 初始化沉浸状态栏
     */
    protected void statusBar(LinearLayout ll) {
        //透明状态栏
        if (ll != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight()));
                ll.setVisibility(View.VISIBLE);
                Window window = getWindow();
                // Translucent status bar
                window.setFlags(
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            } else {
                ll.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 获取系统状态栏高度
     *
     * @return 系统状态栏高度
     */
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 打开软键盘
     *
     * @param et 获取焦点的编辑框
     */
    public void openInput(EditText et) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 关闭软键盘
     *
     * @param et 关闭编辑框的键盘
     */
    public void closeInput(EditText et) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

    /**
     * 保存一个变量
     *
     * @param name  变量名
     * @param value 变量值
     */
    public static void setString(String name, String value) {
        SharedPreferences sharedPreferences = App.getInstance().getSharedPreferences(
                fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(name, value).commit();
    }

    /**
     * 取出一个变量
     *
     * @param name 变量名
     */
    public static String getString(String name) {
        return App.getInstance().getSharedPreferences(fileName, Context.MODE_PRIVATE)
                .getString(name, "");
    }

    /**
     * 清除一个变量
     *
     * @param name 变量名
     */
    public static void cleanString(String name) {
        App.getInstance().getSharedPreferences(fileName, Context.MODE_PRIVATE).edit()
                .remove(name).commit();
    }

    /**
     * 字符串转化为实体类
     *
     * @param strJson
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T fromJson(String strJson, Class<T> clazz) {
        log_i(strJson);
        return new Gson().fromJson(strJson, clazz);
    }

    /**
     * data数组转化为实体类
     *
     * @param bytes
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T fromJson(byte[] bytes, Class<T> clazz) {
        String strJson = new String(bytes);
        log_i(new String(bytes));
        return new Gson().fromJson(strJson, clazz);
    }

    /**
     * json转化为实体类
     *
     * @param strJson
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T fromJson(JSONObject strJson, Class<T> clazz) {
        log_i(strJson);
        return new Gson().fromJson(strJson.toString(), clazz);
    }

    /**
     * 2016 03 15
     * 设置控件的选择状态，若当前为true则设为false，否则设为true
     *
     * @param view
     */
    public void autoSelect(View view) {
        if (view.isSelected()) {
            view.setSelected(false);
        } else {
            view.setSelected(true);
        }
    }

    /**
     * 2016 03 15
     * 判断控件里的值是否为手机号
     *
     * @param view
     * @return false 手机号正确
     */
    boolean isPhoneNum(View view) {
        String value = viewGetValue(view);
//        String regExp = "^1[3-8]\\\\d{9}$";
        String regExp = "^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$";
//        String regExp = "^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(value);
//        if (!m.find()) {
//            App.toask("请输入正确的手机号");
//        }
        return m.find();
    }

    /**
     * 获取登陆的用户
     *
     * @return 用户
     */
    User getUser() {
        if (getString("user").equals("")) {
//            App.toask("登陆已失效");
            return null;
        } else {
            return fromJson(getString("user"), User.class);
        }
    }

    /**
     * 弹窗信息
     *
     * @param message 弹窗信息
     */
    void toask(Object message) {
        App.toask(message + "");
    }

    /**
     * 获取网络图片
     *
     * @param url
     * @return bitmap
     */
    public Bitmap returnBitMap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 获取网络图片资源
     *
     * @param url
     * @return
     */
    public static Bitmap getHttpBitmap(String url) {
        URL myFileURL;
        Bitmap bitmap = null;
        try {
            myFileURL = new URL(url);
            //获得连接
            HttpURLConnection conn = (HttpURLConnection) myFileURL.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(5000);
            //连接设置获得数据流
            conn.setDoInput(true);
            //不使用缓存
            conn.setUseCaches(false);
            //这句可有可无，没有影响
            //conn.connect();
            //得到数据流
            InputStream is = conn.getInputStream();
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            //关闭数据流
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;

    }

    /**
     * 请求网络图片
     *
     * @param url
     * @param iv
     */
    void getUrlBitmap(Object url, ImageView iv) {
        if (url == null || url.equals("")) {
            return;
        }
        //显示图片的配置
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.c6)
                .showImageOnFail(R.drawable.c6)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().displayImage(url.toString(), iv, options);
//        BaseRequest.getImage(url, 0, 0, new Response.Listener<Bitmap>() {
//            @Override
//            public void onResponse(Bitmap bitmap) {
//                iv.setImageBitmap(bitmap);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
////                toask("加载图片失败");
//            }
//        });
    }

    /**
     * @return
     */
    AsyncHttpClient client() {
        return App.getAsyncHttpClient();
    }

    /**
     * 跳转页面
     *
     * @param cls
     */
    void openActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }

    /**
     * 跳转页面
     *
     * @param clazz
     */
    void openActivity(Class<?> clazz, int code) {
        startActivity(new Intent(this, clazz).putExtra("code", code));
    }

    /**
     * 跳转页面
     *
     * @param cls
     * @param requestCode
     * @param code
     */
    void openActivityForResult(Class<?> cls, int requestCode, int code) {
        startActivityForResult(new Intent(this, cls).putExtra("code", code), requestCode);
    }

    /**
     * 跳转页面
     *
     * @param cls
     * @param requestCode
     */
    void openActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(new Intent(this, cls), requestCode);
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

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 400);
        intent.putExtra("outputY", 400);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTORESOULT);
    }

    /**
     * 拍照
     */
    void paiZhao() {
        File file = Environment.getExternalStorageDirectory();
        if (file == null) {
            toask("储存卡不可用");
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
//                file, "huaxin" + DataUtils.getData("yyyyMMdd_mmHHss") + ".jpg")));
        imagePath = file + "huaxin" + DataUtils.getData("yyyyMMdd_mmHHss") + ".jpg";
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imagePath);
        startActivityForResult(intent, PHOTOHRAPH);
    }

    /**
     * 相册
     */
    void xiangCe() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                IMAGE_UNSPECIFIED);
        startActivityForResult(intent, PHOTOZOOM);
    }

    String formatDate(String str) {
        if (str == null) {
            return "";
        }
        return str.substring(5, str.length() - 3);
    }

    String formatName(String str) {
        if (str == null) {
            return "";
        }
        return str.substring(0, 3) + "***" + str.substring(8, str.length());
    }

    /**
     * 加载成功
     */
    void load_succeed() {
        hideProgress();
        toask("加载成功");
    }

    /**
     * 加载失败
     */
    void load_fail() {
        hideProgress();
        toask("加载失败");
    }

    /**
     * 加载中...
     */
    void load() {
        progress("加载中...");
    }

    /**
     * 判断字符串是否为空或者空字符串
     *
     * @param str
     * @return true代表为空或空字符串
     */
    boolean stringIsNull(Object str) {
        if (str == null || str.equals("")) {
            return true;
        }
        return false;
    }

    String stringToDouble(View view) {
        // TODO Auto-generated method stub
        TextView tv = (TextView) view;
        DecimalFormat df = new DecimalFormat("######0.00");
        return df.format(Double.valueOf(viewGetValue(tv)));
    }

    /**
     * 浏览图片
     *
     * @param position
     * @param urls2
     */
    public void photoBrower(int position, ArrayList<String> urls2) {
        Intent intent = new Intent(this, ImagePagerActivity.class);
        // 图片url
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        this.startActivity(intent);
    }

    @Override
    protected void onResume() {
        JPushInterface.onResume(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        JPushInterface.onPause(this);
        super.onPause();
    }

//    List<T> fromJson(String s, T t) {
////        List<T> list = new ArrayList<T>();
////        list = new Gson().fromJson(s, new TypeToken<List<T>>() {
////        }.getType());
//        return new Gson().fromJson(s, new TypeToken<List<T>>() {
//        }.getType());
//    }

    public void setTv(TextView tv, Object str) {
        if (str == null) {
            tv.setText("");
        } else {
            int i = 1;
            tv.setText(str + "");
        }
    }

    public void no_network() {
        hideProgress();
        toask("网络未连接");
    }

    public void no_network(Throwable throwable) {
        hideProgress();
        toask("网络未连接，请链接网络！");
//        if (throwable instanceof ConnectException) {
//            toask("网络未连接，请链接网络！");
//        } else if (throwable instanceof TimeoutException) {
//            toask("请求超时");
//        } else if (throwable instanceof NetworkErrorException) {
//            toask("网络未连接");
//        } else {
//            toask("网络未连接");
//        }
    }

    public int getWidth() {
        return getWindowManager().getDefaultDisplay().getWidth();
    }

    public int getHeight() {
        return getWindowManager().getDefaultDisplay().getHeight();
    }

    /**
     * 收缩压比较舒张压
     *
     * @param s1
     * @param s2
     * @return
     */
    String ssyCompareSzy(String s1, String s2) {
        String result = "偏低";
        int i1 = string2int(s1);
        int i2 = string2int(s2);
        if (i1 > i2) {
            result = s1;
        } else {
            result = s2;
        }
        return result;
    }

    /**
     * 计算收缩压
     *
     * @param str1
     * @return
     */
    String szy(String str1) {
        int i = 0;
        if (!stringIsNull(str1)) {
            i = Integer.valueOf(str1);
        }
        String result = "偏低";
        if (i < 60) {
            result = "偏低";
        } else if (90 <= i && i <= 99) {
            result = "一级";
        } else if (100 <= i && i <= 109) {
            result = "二级";
        } else if (110 <= i) {
            result = "三级";
        } else {
            result = "正常";
        }
        return result;
    }

    /**
     * 计算舒张压
     *
     * @param str2
     * @return
     */
    String ssy(String str2) {
        int i = 0;
        if (!stringIsNull(str2)) {
            i = Integer.valueOf(str2);
        }
        String result = "";
        if (i < 90) {
            result = "偏低";
        } else if (140 <= i && i <= 159) {
            result = "一级";
        } else if (160 <= i && i <= 179) {
            result = "二级";
        } else if (180 <= i) {
            result = "三级";
        } else {
            result = "正常";
        }
        return result;
    }

    int string2int(String str) {
        int i = -1;
        if (str.equals("正常")) {
            i = -1;
        }
        if (str.equals("偏低")) {
            i = 0;
        }
        if (str.equals("一级")) {
            i = 1;
        }
        if (str.equals("二级")) {
            i = 2;
        }
        if (str.equals("三级")) {
            i = 3;
        }
        return i;
    }

    /**
     * 计算血糖
     *
     * @param str
     * @return
     */
    String xt(String str, String time) {
        String result = "";
        float i = 0;
        if (!stringIsNull(str)) {
            i = Float.valueOf(str);
        }
        if (time.equals("早起") || time.equals("餐前")) {
            if (i < 2.8) {
                result = "偏低";
            } else if (i > 6.1) {
                result = "偏高";
            } else {
                result = "正常";
            }
        } else {
            if (i < 2.8) {
                result = "偏低";
            } else if (i > 7.8) {
                result = "偏高";
            } else {
                result = "正常";
            }
        }
        return result;
    }

    String tz(String kg) {
        String result = "";
        if (stringIsNull(App.getUser().getHeight()) || stringIsNull(App.getUser().getGender())) {
            show_ask();
        } else {
            float bmi = 0;
            float tz = 0;
            float sg = 0;
            if (!stringIsNull(kg)) {
                tz = Float.valueOf(kg);
            }
            if (!stringIsNull(App.getUser().getHeight())) {
                sg = Float.valueOf(App.getUser().getHeight());
            }
            bmi = tz / (sg / 100 * sg / 100);
            if (bmi < 18.5) {
                result = "偏瘦";
            } else if (bmi >= 18.5 && bmi <= 23.9) {
                result = "正常";
            } else {
                result = "偏胖";
            }
        }
        return result;
    }

    protected void show_ask() {
        if (dialog == null) {
            dialog = new Dialog(this, R.style.Dialog);
            dialog.setContentView(R.layout.dialog_ask);
            TextView tvTitle = (TextView) dialog.findViewById(R.id.tv_title);
            TextView tvContent = (TextView) dialog.findViewById(R.id.tv_content);
            TextView tvYes = (TextView) dialog.findViewById(R.id.tv_yes);
            TextView tvNo = (TextView) dialog.findViewById(R.id.tv_no);
            tvYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openActivity(GrxxActivity.class);
                }
            });
            tvNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        dialog.show();
    }

    /**
     * 设置当前窗口的透明度（1不透明，0全透明）
     *
     * @param alpha
     */
    public void setAlpha(float alpha) {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = alpha;
        getWindow().setAttributes(params);
    }

    /**
     * 获取一个意图
     *
     * @param cls
     * @return
     */
    public Intent newIntent(Class<?> cls) {
        return new Intent(this, cls);
    }

    public void show_iv_add() {
        if (dialog_iv_add == null) {
            dialog_iv_add = new Dialog(this, R.style.Dialog);
            dialog_iv_add.setContentView(R.layout.dialog_grxx_sex);
            TextView tv = (TextView) dialog_iv_add.findViewById(R.id.tv_title);
            tv.setText("添加图片");
            dialog_iv_add.findViewById(R.id.ll_left).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    takePhoto();
                    dialog_iv_add.dismiss();
                }
            });
            dialog_iv_add.findViewById(R.id.ll_rigth).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pickPhoto();
                    dialog_iv_add.dismiss();
                }
            });
            dialog_iv_add.findViewById(R.id.iv_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog_iv_add.dismiss();
                }
            });
        }
        dialog_iv_add.show();
    }

    /**
     * 获得当前活动的上下文
     *
     * @return
     */
    public Context getContext() {
        return this;
    }

    /**
     * 设置控件的宽度
     *
     * @param view
     * @param width
     */
    public void setViewWidth(View view, int width) {
        setViewLayoutParams(view, width, 0);
    }

    /**
     * 设置控件的高度
     *
     * @param view
     * @param height
     */
    public void setViewHeigth(View view, int height) {
        setViewLayoutParams(view, 0, height);
    }

    /**
     * 设置控件的宽度和高度
     *
     * @param view
     * @param width
     * @param height
     */
    public void setViewLayoutParams(View view, int width, int height) {
        if (view == null) {
            throw new NullPointerException("view can't be null");
        }
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (width > 1) {
            lp.width = width;
        }
        if (height > 1) {
            lp.height = height;
        }
        view.setLayoutParams(lp);
    }

    /**
     * 新建一个意图
     * @param strAcCls
     * @return
     */
    protected Intent newIntent(String strAcCls) {
        return new Intent(this, strAcCls.getClass());
    }

    public void startActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
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

    protected Dialog dialogComplete;
    protected TextView tvComplete;

    protected void showComplete() {
        if (dialogComplete == null) {
            dialogComplete = new Dialog(this, R.style.Dialog);
            dialogComplete.setContentView(R.layout.dialog_ask);
            tvComplete = (TextView) dialogComplete.findViewById(R.id.tv_content);
            dialogComplete.findViewById(R.id.tv_yes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(GrxxActivity.class);
                    dialogComplete.dismiss();
                }
            });
            dialogComplete.findViewById(R.id.tv_no).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogComplete.dismiss();
                }
            });
        }
        tvComplete.setText(str_complete);
        dialogComplete.show();
    }

    protected String str_complete = "";

    /**
     * 是否已完善个人信息(默认返回false)
     *
     * @return
     */
    protected boolean isComplete() {
        boolean b = false;
        str_complete = "";
        User mUser = App.getUser();
//        b = string2boolean(mUser.getNick_name(), "姓名");
//        if (stringIsNull(mUser.getGender()) || mUser.getGender().equals("-1")) {
//            if (stringIsNull(str_complete)) {
//                str_complete = "性别";
//            } else {
//                str_complete += "，性别";
//            }
//        } else {
//            b = true;
//        }
        //只要有任何一个未完善，就返回false
        boolean b1 = string2boolean(mUser.getNick_name(), "姓名");
        boolean b2 = string2boolean(mUser.getGender(), "性别");
//        if (mUser.getGender().equals("-1")) {
//            b2 = false;
//        }
        boolean b3 = string2boolean(mUser.getAge(), "年龄");
        boolean b4 = string2boolean(mUser.getWeight(), "体重");
        boolean b5 = string2boolean(mUser.getHeight(), "身高");
        if (!b1 || !b2 || !b3 || !b4 || !b5) {
            b = false;
        } else {
            b = true;
        }
//        b = string2boolean(mUser.getAge(), "年龄");
//        b = string2boolean(mUser.getWeight(), "体重");
//        b = string2boolean(mUser.getHeight(), "身高");
        return b;
    }

    protected boolean string2boolean(String str, String value) {
        boolean b = false;
        if (stringIsNull(str) || str.equals("-1")) {
            b = false;
            if (stringIsNull(str_complete)) {
                str_complete = value;
            } else {
                str_complete += "，" + value;
            }
        } else {
            b = true;
        }
        return b;
    }

}
