package cc.chenghong.huaxin.utils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/**
 * 对话框工具
 * @author planet 2014年9月28日
 */
public class DialogUtils {
	public static void comfirm(Context context, String title, String message, final OnClickListener positiveListener) {
		comfirm(context, title, message, positiveListener, null);
	}

	public static void comfirm(Context context, String title, String message, final OnClickListener positiveListener,
			final OnClickListener cancelListener) {
		try{
			Builder builder = new Builder(context);
			builder.setMessage(message);
			builder.setTitle(title);
			builder.setPositiveButton("确认", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					positiveListener.onClick(dialog, which);
				}
			});

			builder.setNegativeButton("取消", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					if(cancelListener != null){
						cancelListener.onClick(dialog, which);
					}
				}
			});
			builder.create().show();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
