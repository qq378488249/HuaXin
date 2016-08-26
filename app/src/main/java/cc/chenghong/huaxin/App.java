package cc.chenghong.huaxin;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import cc.chenghong.huaxin.activity.R;
import cc.chenghong.huaxin.camera_code.UITools;
import cc.chenghong.huaxin.entity.User;
import cc.chenghong.huaxin.request.AsyncHttpRequest;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by Administrator on 2016/2/23.
 */
public class App extends Application {
    /**
     * 保存变量的文件名
     */
    static String fileName = "huaxin";
    // 上下文
    static Application application;
    //asyncRequest初始化
    static AsyncHttpClient asyncHttpClient;
    /**
     * 主页面是否在运行
     */
    public static boolean isMainRun = false;

    @Override
    public void onCreate() {
        super.onCreate();
        this.application = this;
        UITools.init(this);
        AsyncHttpRequest.init();
        asyncHttpClient = new AsyncHttpClient();
        initImageLoader(getApplicationContext());
        initJpush();
    }

    /**
     * 初始化极光
     */
    private void initJpush() {
        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(this);
        builder.notificationDefaults = R.drawable.a_0034_;
        JPushInterface.setDefaultPushNotificationBuilder(builder);//设置默认通知栏图标
        Set<String> set = new HashSet<String>();
        set.add("abc");
        JPushInterface.setTags(getApplicationContext(), set, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                Log.i("JPush", "Jpush status: " + set);//状态
            }
        });

    }

    /**
     * 初始化ImageLoader
     */
    public static void initImageLoader(Context context) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, context.getString(R.string.app_name) + "/Cache");//获取到缓存的目录地址
        Log.d("cacheDir", cacheDir.getPath());
        //创建配置ImageLoader(所有的选项都是可选的,只使用那些你真的想定制)，这个可以设定在APPLACATION里面，设置为全局的配置参数
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.c6)//加载中显示的图片
                .showImageOnFail(R.drawable.c6)//加载失败显示的图片
                .cacheInMemory(true)//在内存中缓存
                .cacheOnDisk(true)//在磁盘（内存卡）中缓存
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(context)
                .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽
//                .discCacheExtraOptions(480, 800, Bitmap.CompressFormat.JPEG, 75, null) // Can slow ImageLoader, use it carefully (Better don't use it)设置缓存的详细信息，最好不要设置这个
                .threadPoolSize(5)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY)//线程优先级
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                .discCacheSize(50 * 1024 * 1024)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
                        //.discCacheFileNameGenerator(new HashCodeFileNameGenerator())//将保存的时候的URI名称用HASHCODE加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCacheFileCount(100) //缓存File最大数量
                .discCache(new UnlimitedDiscCache(cacheDir))//自定义缓存路径
                .defaultDisplayImageOptions(displayImageOptions)
                .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);//全局初始化此配置
    }

    /**
     * 保存一个变量
     *
     * @param name  变量名
     * @param value 变量值
     */
    public static void setString(String name, String value) {
        SharedPreferences sharedPreferences = application.getSharedPreferences(
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
        return application.getSharedPreferences(fileName, Context.MODE_PRIVATE)
                .getString(name, "");
    }

    /**
     * 清除一个变量
     *
     * @param name 变量名
     */
    public static void cleanString(String name) {
        application.getSharedPreferences(fileName, Context.MODE_PRIVATE).edit()
                .remove(name).commit();
    }

    public static void toask(Object str) {
        Toast.makeText(application, str + "", Toast.LENGTH_SHORT).show();
    }

    /**
     * 返回当前App的上下文
     *
     * @return application
     */
    public static Application getInstance() {
        return application;
    }

    /**
     * 返回当前AsyncHttpClient
     *
     * @return application
     */
    public static AsyncHttpClient getAsyncHttpClient() {
        return asyncHttpClient;
    }

//    public static void activity(){
//        StartActivity
//    }

    /**
     * 保存用户信息
     *
     * @param user
     */
    public static void setUser(User user) {
        setString("user", new Gson().toJson(user));
    }

    /**
     * 取出用户信息
     *
     * @return
     */
    public static User getUser() {
        if (getString("user").equals("")) {
            return null;
        }
        return new Gson().fromJson(getString("user"), User.class);
    }
}
