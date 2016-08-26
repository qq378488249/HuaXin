package cc.chenghong.huaxin.utils;

import android.util.Log;

public class LongLog {
	public static void i(String TAG, String str) {
		if(str.length() > 3000) {
			Log.i(TAG, str.substring(0, 3000));
			i(TAG, str.substring(3000));
		} else{
			Log.i(TAG, str);
		}
	}
}
