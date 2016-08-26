package cc.chenghong.huaxin.utils;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;

/**
 * 图片内存缓存
 * @author planet
 *
 */
public class ImageMemCache {
    public static final String TAG = "ImageMemCache";
    private static ImageMemCache mImageLoader;
    private LruCache<String, Bitmap> mMemoryCache;
    private LooperThread mLooperThread;
    
    /**
     * 内存大小 = maxMemory / sizeDenominator
     * @param sizeDenominator
     */
    public ImageMemCache(int sizeDenominator){
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / sizeDenominator;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize);
        mLooperThread = new LooperThread();
        mLooperThread.setPriority(Thread.NORM_PRIORITY-1);
        mLooperThread.start();
        //等待线程启动
        while(!mLooperThread.started){}
    }
    
    private ImageMemCache(){
        this(8);
    }

    public static void destory(){
        if(mImageLoader != null){
            mImageLoader.mMemoryCache.evictAll();
            mImageLoader.mMemoryCache = null;
            mImageLoader.mLooperThread.quit();
            mImageLoader = null;
        }
    }

    public static synchronized ImageMemCache getInstance(int sizeDenominator){
        if(mImageLoader == null){
            mImageLoader = new ImageMemCache(sizeDenominator);
        }
        return mImageLoader;
    }
    
    public static synchronized ImageMemCache getInstance(){
        return getInstance(8);
    }

    /**
     * 缓存bitmap到内存
     * @param key
     * @param bitmap
     */
    public void add(String key, Bitmap bitmap) {
        if (get(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    /**
     * 内存中清除bitmap
     * @param url 图片的url
     */
    public void remove(String key) {
        try{
            Bitmap b = mMemoryCache.remove(key);
            b.recycle();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取到图片
     * @param key
     * @return
     */
    public Bitmap get(String key) {
        if(mMemoryCache == null || key == null) return null;
        return mMemoryCache.get(key);
    }
    
    public LooperThread getmLooperThread() {
        return mLooperThread;
    }

    /**
     * 处理线程
     * @author planet
     */
    public class LooperThread extends Thread{
        private Handler mHandler;
        private boolean started = false;

        public Handler getHandler(){
            return mHandler;
        }

        public void quit(){
            mHandler.postAtFrontOfQueue(new Runnable() {

                @Override
                public void run() {
                    mHandler.getLooper().quit();
                }
            });
        }

        public void run() {
            Looper.prepare();
            this.mHandler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    Log.i(TAG, "handleMessage:"+msg.what);
                    return false;
                }
            });
            Log.i(TAG, "Looper开始");
            started = true;
            Looper.loop();
            Log.i(TAG, "Looper结束");
        }
    };
}
