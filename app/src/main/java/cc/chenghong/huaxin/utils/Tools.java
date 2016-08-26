package cc.chenghong.huaxin.utils;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.telephony.TelephonyManager;
/**
 * 基本工具类
 * @author JiaYe 2014年7月18日
 *
 */
public class Tools {
	static final String TAG = "Tools";

	/**
	 * 获取当前应用程序的版本名称(versionName)
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context){
		String appVersion = "";
		PackageManager manager = context.getPackageManager();  
		try {  
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);  
			appVersion = info.versionName; // 版本名   
			int currentVersionCode = info.versionCode; // 版本号   
			System.out.println(currentVersionCode + " " + appVersion);  
		} catch (NameNotFoundException e) {  
			// TODO Auto-generated catch blockd   
			e.printStackTrace();  
		}
		return appVersion;  
	}

	public static int getVersionCode(Context context){
		int appVersion = 1;
		PackageManager manager = context.getPackageManager();  
		try {
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);  
			appVersion = info.versionCode; // 版本号   
		} catch (NameNotFoundException e) {  
			e.printStackTrace();  
		}
		return appVersion;  
	}

	/**
	 * 例如 1.0.0.2转换成1002
	 * @param version
	 * @return
	 */
	public static int getVersionFromName(String version){
		String d = version.replaceAll(".", "");
		while(d.length()<4){
			d += "0";
		}
		return Integer.valueOf(d);
	}

	/**
	 * 浏览器打开网址
	 * @param context
	 * @param url
	 */
	public static void openInBrowser(Context context,String url){
		Intent intent1= new Intent();        
		intent1.setAction("android.intent.action.VIEW");    
		Uri content_url = Uri.parse(url);   
		intent1.setData(content_url);  
		context.startActivity(intent1);
	}

	/**
	 * 判断Network是否开启(包括移动网络和wifi)
	 * @return
	 */
	public static boolean isNetworkEnabled(Context context) {
		return (isWIFIEnabled(context) || isTelephonyEnabled(context));
	}

	/**
	 * 判断移动网络是否开启
	 * @return
	 */
	public static boolean isTelephonyEnabled(Context context) {
		boolean enable = false;
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		if (telephonyManager != null) {
			if (telephonyManager.getNetworkType() != TelephonyManager.NETWORK_TYPE_UNKNOWN) {
				enable = true;
			}
		}
		return enable;
	}

	/**
	 * 判断wifi是否开启
	 */
	public static boolean isWIFIEnabled(Context context) {
		boolean enable = false;
		WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		if(wifiManager.isWifiEnabled()) {
			enable = true;
		}
		return enable;
	}

	/**
	 * 判断是否联网
	 */
	public static boolean isConnection(Context context) {
		boolean enable = false;
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = connManager.getActiveNetworkInfo();
		if(netInfo != null) {
			enable = true;
		}
		return enable;
	}

	/**
	 * 发短信
	 * @param phoneNumber
	 * @param smsContent
	 */
	public static void sendMessage(Context context, String phoneNumber, String smsContent){ 
		Uri smsToUri = Uri.parse("smsto:"+phoneNumber);// 联系人地址 
		Intent mIntent = new Intent(Intent.ACTION_SENDTO,
				smsToUri); 
		mIntent.putExtra("sms_body", smsContent);// 短信内容 
		context.startActivity(mIntent); 
	}
	/**
	 * 打电话
	 * @param phoneNumber
	 */
	public static void call(Context context, String phoneNumber){
		Intent intent = new Intent(  
				Intent.ACTION_DIAL, Uri.parse("tel:"+phoneNumber));  
		context.startActivity(intent);  
	}

	public static void setBadge(Context context, int count) {
		String launcherClassName = getLauncherClassName(context);
		if (launcherClassName == null) {
			return;
		}
		Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
		intent.putExtra("badge_count", count);
		intent.putExtra("badge_count_package_name", context.getPackageName());
		intent.putExtra("badge_count_class_name", launcherClassName);
		context.sendBroadcast(intent);
	}

	public static String getLauncherClassName(Context context) {

		PackageManager pm = context.getPackageManager();

		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);

		List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
		for (ResolveInfo resolveInfo : resolveInfos) {
			String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
			if (pkgName.equalsIgnoreCase(context.getPackageName())) {
				String className = resolveInfo.activityInfo.name;
				return className;
			}
		}
		return null;
	}

	/**
	 * 获取文件或者文件夹大小
	 * @param f
	 * @return
	 */
	public static long getFileSize(File f){
		long size = 0;
		if(!f.isDirectory()){
			return f.length();
		}
		File flist[] = f.listFiles();
		for (int i = 0; i < flist.length; i++){
			if (flist[i].isDirectory()){
				size = size + getFileSize(flist[i]);
			}else{
				size = size + flist[i].length();
			}
		}
		return size;
	}

	/**
	 * 清除缓存文件夹
	 * @param context
	 * @return
	 */
	public static long clearCacheDir(Context context){
		long cacheDirBytes = 0;
		long extCacheDirBytes = 0;
		try{
			cacheDirBytes = getFileSize(context.getCacheDir());
			deleteFilesByDirectory(context.getCacheDir());
		}catch(Exception e){ e.printStackTrace(); }

		try{
			extCacheDirBytes = getFileSize(context.getExternalCacheDir());
			deleteFilesByDirectory(context.getExternalCacheDir());
		}catch(Exception e){ e.printStackTrace(); }

		return cacheDirBytes+extCacheDirBytes;
	}

	/**
	 * 获取缓存文件夹文件大小
	 * @param context
	 * @return
	 */
	public static long getCacheDirSize(Context context){
		long cacheDirBytes = 0;
		long extCacheDirBytes = 0;
		try{
			cacheDirBytes = getFileSize(context.getCacheDir());
		}catch(Exception e){}

		try{
			extCacheDirBytes = getFileSize(context.getExternalCacheDir());
		}catch(Exception e){}

		return cacheDirBytes+extCacheDirBytes;
	}

	/** * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * * @param directory */
	public static void deleteFilesByDirectory(File directory) {
		if (directory != null && directory.exists() && directory.isDirectory()) {
			for (File item : directory.listFiles()) {
				item.delete();
			}
		}
	}

	/**
	 * 获取设备唯一ID
	 * @param context
	 * @return
	 */
	public static String getUniqueId(Context context){
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE); 
		String imei=tm.getDeviceId();
		String simSerialNumber = tm.getSimSerialNumber();
		String androidId =android.provider.Settings.Secure.getString(context.getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);
		if(androidId == null) androidId = "";
		if(simSerialNumber == null) simSerialNumber = "";
		UUID deviceUuid =new UUID(androidId.hashCode(), ((long)imei.hashCode() << 32) |simSerialNumber.hashCode());
		String uniqueId= deviceUuid.toString(); 
		return uniqueId;
	}
	
	/**
	 * 手机号码验证
	 * @param mobileNo
	 * @return
	 */
	public static boolean isMobileNo(String mobileNo){
        Pattern p = Pattern  
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");  
        Matcher m = p.matcher(mobileNo);  
        return m.matches();  
    }

	/**
	 * 是否正在通话中
	 * @param context
	 * @return
	 */
	public static boolean isCallActive(Context context){
		AudioManager manager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		if(manager.getMode()==AudioManager.MODE_IN_CALL){
			return true;
		}else{
			return false;
		}
	}
}
