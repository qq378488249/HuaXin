package cc.chenghong.huaxin.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;

/**
 * Lru & Disk Cache
 * @author planet
 *
 */
public class ImageLoader {
	public static final String TAG = "ImageLoader";
	private static ImageLoader mImageLoader;
	private LruCache<String, Bitmap> mMemoryCache;
	private Context mContext;
	private LooperThread mLooperThread;
	public static final String CacheDir = "/image_loader_cache";

	private Handler mHandler = new Handler();

	private ImageLoader(){
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		final int cacheSize = maxMemory / 8;
		mMemoryCache = new LruCache<String, Bitmap>(cacheSize);
		mLooperThread = new LooperThread();
		mLooperThread.setPriority(Thread.NORM_PRIORITY-1);
		mLooperThread.start();
		//等待线程启动
		while(!mLooperThread.started){}
	}

	public static void destory(){
		if(mImageLoader != null){
			mImageLoader.mHandler = null;
			mImageLoader.mMemoryCache.evictAll();
			mImageLoader.mMemoryCache = null;
			mImageLoader.mLooperThread.quit();
			mImageLoader = null;
		}
	}

	/**
	 * 删除sd卡缓存
	 * @param url
	 */
	public void deleteFromDisk(final String url){
		mLooperThread.getHandler().post(new Runnable() {
			@Override
			public void run() {
				try{
					if(testType(url) == ImageType.LOCAL){
						new File(url).delete();
						Log.i(TAG, "删除了文件(LOCAL):"+url);
					}else{
						String md5 = MD5.getMD5(url);
						//目录不存在创建目录
						File cacheDir = new File(mContext.getCacheDir()+CacheDir);
						if(!cacheDir.exists()){
							return;
						}
						//从cache文件夹读取图片文件
						File bmp = new File(mContext.getCacheDir()+CacheDir+"/"+md5);
						if(bmp.exists()){
							Log.i(TAG, "删除了文件:"+url);
							bmp.delete();
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 加载图片
	 * @param url
	 * @param receiver
	 * @param readCacheDir 是否从cacheDir文件夹读取
	 * @param maxWidth
	 * @param fileSpec 读取图片以后保存到文件
	 * @param disableMemCache 禁止从内存读取缓存
	 */
	public void loadBitmap(final String url, final BitmapReceiver receiver, final boolean readCacheDir, final int maxWidth, final FileSpec fileSpec,
			boolean disableMemCache){
		if(!disableMemCache){
			Bitmap bm = getBitmapFromMemCache(url);
			if(bm != null && !bm.isRecycled()){
				if(fileSpec != null){
					fileSpec.url = url;
					String md5 = MD5.getMD5(url);
					fileSpec.file =  new File(mContext.getCacheDir()+CacheDir+"/"+md5);
				}
				receiver.receive(url, bm, true);
				//Log.i(TAG, "cache exist");
				return;
			}
		}

		//Log.i(TAG, "cache not exist");
		mLooperThread.getHandler().postAtFrontOfQueue(new Runnable() {
			@Override
			public void run() {
				if(receiver.getCurrentUrl() != null && !receiver.getCurrentUrl().equals(url)){
					return;
				}
				if(url == null){
					sendError(url, null, receiver);
					return;
				}
				//目录不存在创建目录
				File cacheDir = new File(mContext.getCacheDir()+CacheDir);
				if(!cacheDir.exists()){
					cacheDir.mkdir();
				}
				String md5 = MD5.getMD5(url);
				//从cache文件夹读取图片文件
				File bmp = null;
				if(testType(url) == ImageType.LOCAL){
					bmp = new File(url);
				}else{
					bmp = new File(mContext.getCacheDir()+CacheDir+"/"+md5);
				}
				Bitmap diskBm = null;
				//如果指定不读去cache缓存，立即下载图片
				if(!readCacheDir || bmp==null || !bmp.exists()){
					try {
						//图片不存在从网络下载
						sendDownloadStart(url, receiver);
						Log.i(TAG, "url=========================="+url);
						bmp = new File(downloadImage(url));
						sendDownloadComplete(url, receiver);
						Log.i(TAG, "sendDownloadComplete!!!!!!!!");
					} catch (Exception e) {
						sendError(url, e.getMessage(), receiver);
						if(bmp!=null && bmp.exists()){
							bmp.delete();
						}
						e.printStackTrace();
						return;
					}
				}
				if(bmp != null && bmp.exists()){
					if(receiver.getCurrentUrl() == null || receiver.getCurrentUrl().equals(url)){
						if(maxWidth > 0){
							Options opts = new Options();
							opts.inJustDecodeBounds = true;
							BitmapFactory.decodeFile(bmp.getAbsolutePath(), opts);
							opts.inSampleSize = opts.outWidth/maxWidth;
							//Log.i(TAG, "缩放inSampleSize="+opts.inSampleSize);
							opts.inJustDecodeBounds = false;
							diskBm = BitmapFactory.decodeFile(bmp.getAbsolutePath(), opts);
						}else{
							diskBm = BitmapFactory.decodeFile(bmp.getAbsolutePath());
						}

						if(url == null || diskBm == null){
							sendError(url, null, receiver);
							if(bmp.exists()){
								bmp.delete();
							}
						}else{
							addBitmapToMemoryCache(url, diskBm);
							if(fileSpec != null){
								fileSpec.url = url;
								fileSpec.file =  bmp;
							}
							sendReceive(url, diskBm, receiver);
						}
					}else{
						Log.i(TAG, "BitmapReceiver中的url不匹配,不通知receive，不放入缓存");
					}
				}else{
					sendError(url, null, receiver);
				}
			}
		});
	}

	/**
	 * 加载图片
	 * @param url
	 * @param receiver
	 * @param maxWidth 读取到内存中的图片最大宽度(以节省内存)
	 */
	public void loadBitmap(final String url, final BitmapReceiver receiver, int maxWidth){
		loadBitmap(url, receiver, true, maxWidth, null, false);
	}

	/**
	 * 加载图片
	 * @param url
	 * @param receiver
	 * @param maxWidth 读取到内存中的图片最大宽度(以节省内存)
	 * @param disableMemCache 是否禁止读取内存缓存
	 */
	public void loadBitmap(final String url, final BitmapReceiver receiver, int maxWidth, boolean disableMemCache){
		loadBitmap(url, receiver, true, maxWidth, null, disableMemCache);
	}

	public void loadBitmap(final String url, final BitmapReceiver receiver){
		loadBitmap(url, receiver, true, -1, null, false);
	}

	public void loadBitmap(final String url, final BitmapReceiver receiver, FileSpec spec){
		loadBitmap(url, receiver, true, -1, spec, false);
	}

	/**
	 * 图片加载失败
	 * @param url
	 * @param error
	 * @param receiver
	 */
	private void sendError(final String url, final String error, final BitmapReceiver receiver){
		if(mHandler != null)
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					receiver.error(url, error);
				}
			});
	}

	/**
	 * 图片加载成功
	 * @param url
	 * @param bm
	 * @param receiver
	 */
	private void sendReceive(final String url, final Bitmap bm, final BitmapReceiver receiver){
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				receiver.receive(url, bm, false);
			}
		});
	}

	private void sendDownloadStart(final String url, final BitmapReceiver receiver){
		Log.i(TAG, "sendDownloadStart "+url);
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				receiver.onDownloadStart(url);
			}
		});
	}

	private void sendDownloadComplete(final String url, final BitmapReceiver receiver){
		Log.i(TAG, "sendDownloadComplete "+url);
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				receiver.onDownloadComplete(url);
			}
		});
	}

	public static synchronized ImageLoader getInstance(Context context){
		if(mImageLoader == null){
			mImageLoader = new ImageLoader();
			mImageLoader.mContext = context;
		}
		return mImageLoader;
	}

	private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null) {
			mMemoryCache.put(key, bitmap);
		}
	}

	/**
	 * 内存中清除bitmap
	 * @param url 图片的url
	 */
	public void removeBitmapFromMemoryCache(String url) {
		try{
			Bitmap b = mMemoryCache.remove(url);
			//b.recycle();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private Bitmap getBitmapFromMemCache(String key) {
		if(mMemoryCache == null || key == null) return null;
		return mMemoryCache.get(key);
	}

	/**
	 * 图片接收
	 * @author planet
	 *
	 */
	public interface BitmapReceiver{
		/**
		 * 从SD卡或者网络获取返回
		 * @param url
		 * @param bm
		 * @param inmem 是否在内存中
		 */
		public void receive(String url, Bitmap bm, boolean inmem);
		public void error(String url, String msg);
		/**
		 * 用于对比url, 返回空不对比, url不想等不加载到内存
		 * @return
		 */
		public String getCurrentUrl();
		public void onDownloadStart(String url);
		public void onDownloadComplete(String url);
	}

	/**
	 * 网络、SD卡处理线程
	 * @author planet
	 *
	 */
	private class LooperThread extends Thread{
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

	/**
	 * url类型
	 * @author 
	 *
	 */
	public enum ImageType{
		/**
		 * 网络文件
		 */
		HTTP,
		/**
		 * assets文件
		 */
		ASSETS,
		/**
		 * 资源文件
		 */
		RESOURCE,
		/**
		 * 本地文件
		 */
		LOCAL
	}
	
	/**
	 * 判断文件类型
	 * @param url
	 * @return
	 */
	public ImageType testType(String url){
		//http开头的是网络图片
		if(url.startsWith("http://") || url.startsWith("https://")){
			return ImageType.HTTP;
		}else if(url.startsWith("assets:")){
			return ImageType.ASSETS;
		}else if(url.indexOf("/") == -1){
			//资源图片
			return ImageType.RESOURCE;
		}else{
			//图片文件
			return ImageType.LOCAL;
		}
	}
	/**
	 * 下载文件(如果文件是在sd卡直接读取)
	 * @param url
	 * @return
	 * @throws Exception
	 */
	private String downloadImage(String url) throws Exception{
		Log.i(TAG, "StartDownload:"+url);
		String filePath = null;
		ImageType imageType = testType(url);
		//http开头的是网络图片
		if(imageType == ImageType.HTTP){
			File file = new File(mContext.getCacheDir()+CacheDir+"/"+MD5.getMD5(url));
			file.createNewFile();
			FileOutputStream outStream = new FileOutputStream(file);
			HttpURLConnection conn = (HttpURLConnection)new URL(url).openConnection();
			conn.setConnectTimeout(5 * 1000);
			conn.setRequestMethod("GET");
			//new Header("User-Agent","Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; .NET CLR 2.0.50727; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; .NET4.0C)")
			conn.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; .NET CLR 2.0.50727; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; .NET4.0C)");
			int resCode = conn.getResponseCode();
			Log.i(TAG, "responseCode:"+resCode+" "+url);
			if(conn.getResponseCode()==200){
				InputStream inStream = conn.getInputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while( (len = inStream.read(buffer)) !=-1 ){
					outStream.write(buffer, 0, len);
				}
				outStream.close();
				inStream.close();
				filePath = file.getPath();
				Log.i(TAG, "下载成功:"+url);
			}else{
				outStream.close();
				file.delete();
				throw new Exception("文件下载失败！resCode="+resCode);
			}
		}else if(imageType == ImageType.ASSETS){
			File file = new File(mContext.getCacheDir()+CacheDir+"/"+MD5.getMD5(url));
			file.createNewFile();
			FileOutputStream outStream = new FileOutputStream(file);
			//assets开头的是assets文件夹的图片
			InputStream inStream = mContext.getAssets().open(url.replaceFirst("assets:", ""));
			if(inStream != null){
				byte[] buffer = new byte[1024];
				int len = 0;
				while( (len = inStream.read(buffer)) !=-1 ){
					outStream.write(buffer, 0, len);
				}
				outStream.close();
				inStream.close();
				filePath = file.getPath();
				Log.i(TAG, "assets下载成功:"+url);
			}else{
				outStream.close();
				file.delete();
				throw new Exception("assets文件下载失败！"+url);
			}
		}
		//资源图片
		else if(imageType == ImageType.RESOURCE){
			int res = -1;
			try{
				res = Integer.parseInt(url);
			}catch(Exception e){
				e.printStackTrace();
			}
			File file = new File(mContext.getCacheDir()+CacheDir+"/"+MD5.getMD5(url));
			file.createNewFile();
			FileOutputStream outStream = new FileOutputStream(file);
			if(res != -1){
				InputStream inStream = mContext.getResources().openRawResource(res);
				byte[] buffer = new byte[1024];
				int len = 0;
				while( (len = inStream.read(buffer)) !=-1 ){
					outStream.write(buffer, 0, len);
				}
				outStream.close();
				inStream.close();
				filePath = file.getPath();
				Log.i(TAG, "资源文件下载成功:"+url);
			}else{
				outStream.close();
				file.delete();
				throw new Exception("资源文件下载失败！"+url);
			}
		}else{
			filePath = url;
			/*//图片文件
			file = new File(url);
			if(file.exists()){
				InputStream inStream = new FileInputStream(file);
				byte[] buffer = new byte[1024];
				int len = 0;
				while( (len = inStream.read(buffer)) !=-1 ){
					outStream.write(buffer, 0, len);
				}
				outStream.close();
				inStream.close();
				Log.i(TAG, "下载成功:"+url);
			}else{
				outStream.close();
				file.delete();
				throw new Exception("图片文件不存在！"+url);
			}*/
		}
		return filePath;
	}

	public class FileSpec{
		public File file;
		public String url;
	}
}
