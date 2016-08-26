package cc.chenghong.huaxin.activity;

import android.app.Activity;
import android.os.Bundle;

import cc.chenghong.huaxin.view.ProgressDialog;


/**
 * 带有旋转对话框的Activity
 * @author planet
 *
 */
public class ProgressActivity extends Activity {
	ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		progressDialog = new ProgressDialog(this);
	}
	
	/**
	 * 显示对话框
	 * @param message
	 */
	public void progress(String message){
		progressDialog.show(message);
	}
	/**
	 * UI现成显示对话框
	 * @param message
	 */
	public void postProgress(final String message){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				progress(message);
			}
		});
	}
	
	public void hideProgress(){
		progressDialog.hide();
	}
	
	@Override
	public void finish() {
		progressDialog.dismiss();
		super.finish();
	}

	public boolean isProgressShow(){
		return progressDialog.isShow();
	}
}
