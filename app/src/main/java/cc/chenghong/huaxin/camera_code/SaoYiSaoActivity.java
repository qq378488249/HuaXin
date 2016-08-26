package cc.chenghong.huaxin.camera_code;
//package cc.chenghong.opensesame.code.qr_codescan;
//
//
//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//public class SaoYiSaoActivity extends Activity {
//	private final static int SCANNIN_GREQUEST_CODE = 1;
//	/** 
//     * 显示扫描结果 
//     */  
//    private TextView mTextView ;  
//    /** 
//     * 显示扫描拍的图片 
//     */
//	private ImageView mImageView;
//	
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
//		
//		mTextView = (TextView) findViewById(R.id.result); 
//		mImageView = (ImageView) findViewById(R.id.qrcode_bitmap);
//		
//		//点击按钮跳转到二维码扫描界面，这里用的是startActivityForResult跳转  
//        //扫描完了之后调到该界面
//		Button mButton = (Button) findViewById(R.id.button1);
//		mButton.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent();
//				intent.setClass(SaoYiSaoActivity.this, cc.chenghong.opensesame.codeqr_codescan.bm.e_life.zxing.qr_codescan.MipcaActivityCapture.class);
//				System.out.println("111");
//				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
//				System.out.println("22222");
//				
//			}
//		});
//	}
//	
//	
//	@Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//		case SCANNIN_GREQUEST_CODE:
//			if(resultCode == RESULT_OK){
//				Bundle bundle = data.getExtras();
//				//显示扫描到的内容
//				String str = bundle.getString("result");
//				//正则
//				String s = "http";
//				mTextView.setText(str);
//				//显示
//				mImageView.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));
//				if(str.startsWith(s)){
//					Uri uri = Uri.parse(str);  
//					Intent it = new Intent(Intent.ACTION_VIEW, uri);  
//					startActivity(it);
//				}else{
//					Toast.makeText(SaoYiSaoActivity.this, "地址无效", 1).show();
//				}
//			}
//			break;
//		}
//    }	
//
//}
