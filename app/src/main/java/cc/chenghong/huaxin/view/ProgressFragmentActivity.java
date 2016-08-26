package cc.chenghong.huaxin.view;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import cc.chenghong.huaxin.view.ProgressDialog;


/**
 * 带有旋转对话框的Activity
 * @author planet
 *
 */
public class ProgressFragmentActivity extends FragmentActivity {
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
}
