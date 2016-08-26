package cc.chenghong.huaxin.utils;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * 邮件工具包
 * @author planet
 */
public class MailUtils {
	/**
	 * 调用系统发送邮件
	 * @param context
	 * @param title
	 * @param file
	 */
	public static void sendEmail(Context context, String title, File file){
		/*Intent testN = new Intent(Intent.ACTION_SEND);
		testN.putExtra(Intent.EXTRA_SUBJECT, title);
		testN.putExtra(Intent.EXTRA_STREAM, file.getAbsolutePath());
		context.startActivity(Intent.createChooser(testN, "发送"));*/
		Intent returnIt = new Intent(Intent.ACTION_SEND);

		//String[] tos = { "admin@www.linuxidc.com" }; //send to someone
		//String[] ccs = { "root@www.linuxidc.com" };  //Carbon Copy to someone
		//returnIt.putExtra(Intent.EXTRA_EMAIL, tos);
		//returnIt.putExtra(Intent.EXTRA_CC, ccs);
		returnIt.putExtra(Intent.EXTRA_TEXT, title);
		returnIt.putExtra(Intent.EXTRA_SUBJECT, title);
		Uri uri = Uri.fromFile(file);
		returnIt.putExtra(Intent.EXTRA_STREAM, uri);
		returnIt.setType("image/jpeg");
		returnIt.setType("message/rfc882");
		Intent.createChooser(returnIt, "发送邮件");
		context.startActivity(returnIt);
	}
}
