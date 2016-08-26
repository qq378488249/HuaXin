package cc.chenghong.huaxin.utils;

import java.io.File;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/**
 * apk升级工具
 * @author planet 2014年9月28日
 */
public class VersionUpdateManager {
	static final String TAG = VersionUpdateManager.class.getSimpleName();
	static VersionUpdateManager instance;
	private Context context;
	/**
	 * 
	 */
	private long enqueue;
	/**
	 * 下载管理器
	 */
	private DownloadManager dm;

	public static VersionUpdateManager getInstance(Context context) {
		if(instance == null){
			instance = new VersionUpdateManager(context);
			context.registerReceiver(instance.receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
		}
		return instance;
	}

	public static void destroy(){
		try{
			if(instance != null){
				instance.context.unregisterReceiver(instance.receiver);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void downloadApk(String url){
		//如果系统大于2.3版本，调用系统的下载器，否则用浏览器打开
		if(Build.VERSION.SDK_INT >= 9){
			downloadApkViaDm(url);
		}else{
			Tools.openInBrowser(context, url);
		}
		toast("开始下载安装包");
	}

	private void toast(String msg){
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	private void downloadApkViaDm(String url){
		dm = (DownloadManager) context.getSystemService(Activity.DOWNLOAD_SERVICE);
		Request request = new Request(Uri.parse(url));
		enqueue = dm.enqueue(request);
		Log.i(TAG, "开始下载: id="+enqueue+" url="+url);
	}

	private VersionUpdateManager(Context context){
		this.context = context;
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@TargetApi(Build.VERSION_CODES.GINGERBREAD)
		@Override
		public void onReceive(final Context context, Intent intent) {
			String action = intent.getAction();
			if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
				Query query = new Query();
				query.setFilterById(enqueue);
				Cursor c = dm.query(query);
				if (c.moveToFirst()) {
					int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
					if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
						String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
						Uri uri = Uri.parse(uriString);
						Log.i(TAG, "apk下载成功:"+uriString);
						/*Intent installI = new Intent(Intent.ACTION_VIEW);
						installI.setDataAndType(uri, "application/vnd.android.package-archive");
						getContext().startActivity(installI);*/
						File file = new File(Environment.getExternalStorageDirectory().getPath()+"/yibaotong.apk");
						if(file.exists()) file.delete();
						FileUtils.copyFile(context, uri, file, new Handler(new Callback() {
							@Override
							public boolean handleMessage(Message msg) {
								if(msg.obj !=null && msg.obj instanceof File){
									toast("下载成功");
									File file = (File)msg.obj;
									Log.i(TAG, "apk保存路径:");
									Intent it = new Intent(Intent.ACTION_VIEW);
									it.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
									it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									context.startActivity(it);
								}else{
									toast("安装包下载失败");
								}
								return false;
							}
						}));
					}
				}
			}
		}
	};
}
