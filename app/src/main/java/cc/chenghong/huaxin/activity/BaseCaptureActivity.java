package cc.chenghong.huaxin.activity;

import java.io.File;
import java.util.UUID;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import cc.chenghong.huaxin.App;
import cc.chenghong.huaxin.dialog.ThreeButtonDialog;
import cc.chenghong.huaxin.utils.ImageTool;

/**
 * 照相Activity基类
 * @author planet 2014年9月23日
 */
public abstract class BaseCaptureActivity extends ProgressActivity{
	private String TAG="BaseAbActivity";
	//用户选取的原始图片
	protected File CapturePhotoFile;
	//用户选取的裁剪后的图片
	protected File CropPhotoFile;
	/* 用来标识请求照相功能的activity */
	protected static final int CAMERA_WITH_DATA = 3023;
	/* 用来标识请求gallery的activity */
	protected static final int PHOTO_PICKED_WITH_DATA = 3021;
	/**
	 * 裁剪图片返回
	 */
	protected static final int PHOTO_CROP = 3022;
	/* 拍照的照片存储位置 */
	protected File PHOTO_DIR = null;
	
	private ThreeButtonDialog buttonDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PHOTO_DIR = getExternalCacheDir();
		CapturePhotoFile = new File(PHOTO_DIR, "tmp_capture.jpg");
		
		buttonDialog = new ThreeButtonDialog(this)
		.setFirstButtonText("拍照").setBtn1Listener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				takePhoto();
			}
		}).setThecondButtonText("相册").setBtn2Listener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				pickPhoto();
			}
		}).autoHide();
	}
	
	/**
	 * 显示选择照片对话框
	 */
	public void showPhotoDialog(){
		buttonDialog.show();
	}

	/**
	 * 从相册选择照片
	 */
	protected void pickPhoto(){
		try {
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
			intent.setType("image/*");
			startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			App.toask("没有找到照片");
		}
	}

	/**
	 * 拍照获取图片
	 * 拍照以后的图片保存到mCurrentPhotoFile
	 */
	protected void takePhoto() {
		//		String status = Environment.getExternalStorageState();
		//		// 判断是否有SD卡,如果有sd卡存入sd卡在说，没有sd卡直接转换为图片
		//		if (!status.equals(Environment.MEDIA_MOUNTED)) {
		//			App.toast("没有可用的存储卡");
		//			return;
		//		}
		try {
			//CapturePhotoFile = new File(PHOTO_DIR, UUID.randomUUID()+".jpg");
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
			intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(CapturePhotoFile));
			startActivityForResult(intent, CAMERA_WITH_DATA);
		} catch (Exception e) {
			App.toask("未找到系统相机程序");
		}
	}

	/**
	 * 图片拍照以后回调
	 * @param photoPath
	 */
	protected abstract void onPhotoTaked(String photoPath);

	/**
	 * 描述：因为调用了Camera和Gally所以要判断他们各自的返回情况, 他们启动时是这样的startActivityForResult
	 */
	protected void onActivityResult(int requestCode, int resultCode,
			Intent mIntent) {
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		//从相册选择图片返回
		case PHOTO_PICKED_WITH_DATA:
			Uri uri = mIntent.getData();
			String currentFilePath = getPath(uri);
			//如果图片不在存储卡中，读取图片到缓存文件夹
			if(currentFilePath==null || currentFilePath.length() == 0){
				Log.i(TAG, "图片不在存储卡中！！！！》》》》》》》》》》》》》》");
				progress("正在读取图片, 请稍后...");
				ImageTool.saveToFile(this, uri, CapturePhotoFile, new Handler(
						new Callback() {
							@Override
							public boolean handleMessage(Message arg0) {
								hideProgress();
								if (CapturePhotoFile.exists()) {
									cropImage();
								}
								return true;
							}
						}));
			}else{
				//如果图片存在，直接读取
				CapturePhotoFile = new File(currentFilePath);
				if (CapturePhotoFile.exists()) {
					cropImage();
				}
			}
			break;
			//拍照返回
		case CAMERA_WITH_DATA:
			cropImage();
			break;
			//裁剪返回(裁剪返回以后，默认保存到CropPhotoFile)
		case PHOTO_CROP:
			if(CropPhotoFile!=null && CropPhotoFile.exists()){
				String path = CropPhotoFile.getAbsolutePath();
				Log.i("i", "裁剪后得到的图片的路径是 = " + path);
				onPhotoTaked(path);
			}
			break;
		}
	}

	/**
	 * 从相册得到的url转换为SD卡中图片路径
	 */
	public String getPath(Uri uri) {
		if (uri.getAuthority()==null || uri.getAuthority().length() ==0 ) {
			return null;
		}
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String path = cursor.getString(column_index);
		return path;
	}

	/**
	 * 裁剪图片
	 */
	public void cropImage(){
		Log.i(TAG, ">>>>>>>>>>>>>\n裁剪图片\n>>>>>>>>>>>>>>>>");
		try{
			Intent intent = new Intent("com.android.camera.action.CROP");
			//intent.setType("image/*");
			intent.setDataAndType(Uri.fromFile(CapturePhotoFile), "image/*");
			intent.putExtra("crop", "true");
			//intent.putExtra("aspectX", 1);
			//intent.putExtra("aspectY", 1);
			//intent.putExtra("outputX", 222);
			//intent.putExtra("outputY", 222);
			intent.putExtra("return-data", false);
			//intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
			CropPhotoFile = new File(PHOTO_DIR, UUID.randomUUID()+".jpg");
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(CropPhotoFile));
			startActivityForResult(intent, PHOTO_CROP);
		}catch(Exception e){
			e.printStackTrace();
			if(CapturePhotoFile.exists()){
				String path = CapturePhotoFile.getAbsolutePath();
				Log.i("i", "获取的图片的路径是 = " + path);
				onPhotoTaked(path);
			}
		}
	}
	
	@Override
	public void finish() {
		buttonDialog.dismiss();
		super.finish();
	}
}
