package cc.chenghong.huaxin.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;

public class ImageTool {
	public static final String tag = "ImageTool"; 
	/**
	 * 缩放图片，缩放之后放到临时目录
	 * @param maxLengthOfSide
	 * @param imageFile
	 * @param outFile
	 * @return
	 */
	public static boolean scale(int maxLengthOfSide, File imageFile, File outFile){
		Bitmap bitmap = scale(maxLengthOfSide, imageFile);
		//判断图片方向
		/*if(App.sdkVersion >= 7){
			//new MyExifInterface().getRotate(imageFile.getPath(), bitmap);
		}*/
		//将缩放的图片写入临时文件
		try {
			Log.i(tag, "outFile========="+outFile);
			FileOutputStream out = new FileOutputStream(outFile);
			if(bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)){
				out.flush();
				out.close();
				Log.i(tag, "压缩图片文件写入成功!");
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 读取缩小的图片
	 * @param path
	 * @param maxLenghtOfSide
	 * @return
	 */
	public static Bitmap readBitmap(String path, int maxLenghtOfSide){
		Log.i(tag, "--读取缩小的图片-- imageFile="+path);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高
		BitmapFactory.decodeFile(path, options);
		Log.i(tag, "outWidth="+options.outWidth+" outHeight="+options.outHeight);
		//如果图片大于maxLengthOfSide一倍以上，缩放读取
		if(options.outWidth > maxLenghtOfSide || options.outHeight > maxLenghtOfSide)
			if(options.outWidth > options.outHeight){
				options.inSampleSize = options.outWidth/maxLenghtOfSide;
			}else{
				options.inSampleSize = options.outHeight/maxLenghtOfSide;
			}
		//重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false哦
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		return bitmap;
	}
	
	public static Bitmap readBitmapFromAssets(Context context, String fileName, int maxLenghtOfSide){
		Log.i(tag, "--readBitmapFromAssets="+fileName);
		Bitmap bitmap = null;
		try{
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			// 获取这个图片的宽和高
			//BitmapFactory.decodeStream(context.getAssets().open(fileName), null, options);
			BitmapFactory.decodeStream(new FileInputStream(new File(fileName)), null, options);
			Log.i(tag, "outWidth="+options.outWidth+" outHeight="+options.outHeight);
			//如果图片大于maxLengthOfSide一倍以上，缩放读取
			if(options.outWidth > maxLenghtOfSide || options.outHeight > maxLenghtOfSide)
				if(options.outWidth > options.outHeight){
					options.inSampleSize = options.outWidth/maxLenghtOfSide;
				}else{
					options.inSampleSize = options.outHeight/maxLenghtOfSide;
				}
			//重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false哦
			options.inJustDecodeBounds = false;
			bitmap = BitmapFactory.decodeStream(new FileInputStream(new File(fileName)), null, options);
		}catch(Exception e){
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 创建略缩图
	 * @param source
	 * @param width
	 * @param height
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.FROYO)
	public static Bitmap createThumb(Bitmap source, int width, int height){
		return ThumbnailUtils.extractThumbnail(source, width, height);
	}

	/**
	 * 缩放图片，缩放之后放到临时目录
	 * @param maxLengthOfSide
	 * @param imageFile
	 * @param outFile
	 * @return
	 */
	public static Bitmap scale(int maxLengthOfSide, File imageFile){
		//Log.i(tag, "--缩放图片-- imageFile="+imageFile==null?"null":imageFile.getAbsolutePath());
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高
		BitmapFactory.decodeFile(imageFile.getPath(), options);
		//Log.i(tag, "outWidth="+options.outWidth+" outHeight="+options.outHeight);
		//如果图片大于maxLengthOfSide一倍以上，缩放读取
		if(options.outWidth > maxLengthOfSide || options.outHeight > maxLengthOfSide)
			if(options.outWidth > options.outHeight){
				options.inSampleSize = options.outWidth/maxLengthOfSide;
			}else{
				options.inSampleSize = options.outHeight/maxLengthOfSide;
			}
		//重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false哦
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getPath(), options);
		//Log.i(tag, "压缩读入：outWidth="+options.outWidth+" outHeight="+options.outHeight);
		//如果图片宽度或者高度仍然大于maxLengthOfSide，再次进行缩放
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		if(width > maxLengthOfSide || height > maxLengthOfSide){
			float scaleWidth;
			float scaleHeight;
			if(width > height){
				scaleWidth = ((float)maxLengthOfSide)/width;
				scaleHeight = scaleWidth;
			}else{
				scaleHeight = ((float)maxLengthOfSide)/height;
				scaleWidth = scaleHeight;
			}
			// 取得想要缩放的matrix参数
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,true);
			bitmap = newBitmap;
			//Log.i(tag, "二次压缩成功：width="+bitmap.getWidth()+" height="+bitmap.getHeight());
		}
		//Log.i(tag, "最终大小：width="+bitmap.getWidth()+" height="+bitmap.getHeight());
		//将缩放的图片写入临时文件
		return bitmap;
	}

	/**
	 * 缩放图片，缩放之后放到临时目录
	 * @param maxLengthOfSide
	 * @param imageFile
	 * @param outFile
	 * @return
	 */
	public static Bitmap scale(int maxLengthOfSide, Bitmap bitmap){
		Bitmap newBitmap = null;
		//如果图片宽度或者高度仍然大于maxLengthOfSide，进行缩放
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		//Log.i(tag, "--缩放图片-- width:"+width+" height="+height+ " maxLengthOfSide="+maxLengthOfSide);
		if(width > maxLengthOfSide || height > maxLengthOfSide){
			float scaleWidth;
			float scaleHeight;
			if(width > height){
				scaleWidth = ((float)maxLengthOfSide)/width;
				scaleHeight = scaleWidth;
			}else{
				scaleHeight = ((float)maxLengthOfSide)/height;
				scaleWidth = scaleHeight;
			}
			// 取得想要缩放的matrix参数
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,true);
			//Log.i(tag, "压缩成功：width="+bitmap.getWidth()+" height="+bitmap.getHeight());
		}else{
			newBitmap = bitmap;
		}
		//Log.i(tag, "最终大小：width="+bitmap.getWidth()+" height="+bitmap.getHeight());
		//将缩放的图片写入临时文件
		return newBitmap;
	}

	/**
	 * 根据uri获取图片路径
	 * @param context
	 * @param uri
	 * @return
	 */
	@Deprecated
	public static String getImagePathFromUri(Context context, Uri uri){
		//Log.i("getURI", "获取到的uri========="+uri);
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(uri, null, null, null, null);
		if(cursor == null) return null;
		if(cursor.moveToFirst()){
			try{
				return cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{

		}
		return null;
	}

	/**
	 * 图片复制到指定文件
	 * @param uri 要复制的图片
	 * @param dest 目标文件
	 * @param handler
	 */
	public static void saveToFile(final Context context,
			final Uri uri, final File dest, final Handler handler){
		try{
			new Thread(new Runnable() {
				@Override
				public void run() {
					try{
						FileUtils.copyfile(FileUtils.openStream(context, uri), dest);
					}catch(Exception e){
						e.printStackTrace();
					}
					//通知调用者
					handler.sendEmptyMessage(0);
				}
			}).start();
		}catch(Exception e){
			handler.sendEmptyMessage(0);
		}
	}

	/**
	 * 图片保存到文件
	 * @param context
	 * @param bmp
	 * @param dest
	 * @param handler 保存成功以后会收到消息: handler.sendEmptyMessage(0);
	 */
	public static void saveToFile(final Context context,
			final Bitmap bmp, final File dest, final Handler handler){
		try{
			new Thread(new Runnable() {
				@Override
				public void run() {
					try{
						FileUtils.copyfile(new ByteArrayInputStream(bmpToByteArray(bmp)), dest);
					}catch(Exception e){
						e.printStackTrace();
					}
					//通知调用者
					handler.sendEmptyMessage(0);
				}
			}).start();
		}catch(Exception e){
			handler.sendEmptyMessage(0);
		}
	}
	
	public static void saveToFileSync(	final Bitmap bmp, final File dest){
		FileUtils.copyfile(new ByteArrayInputStream(bmpToByteArray(bmp)), dest);
	}
	
	public static void saveToFileSync(	final Bitmap bmp, final File dest, Bitmap.CompressFormat format){
		FileUtils.copyfile(new ByteArrayInputStream(bmpToByteArray(bmp, format)), dest);
	}

	/**
	 * 图片压缩到指定文件
	 * @param uri 要压缩的图片
	 * @param dest 目标文件
	 * @param maxSide 图片压缩宽度
	 * @param handler
	 */
	public static void scaleToFile(final Context context,
			final Uri uri, final File dest, final int maxSide, final Handler handler){
		try{
			new Thread(new Runnable() {

				@Override
				public void run() {
					//URI关联的文件复制到临时文件夹
					File tmpFile = new File(context.getCacheDir().getPath()+"/"+UUID.randomUUID()+".png");
					try{
						if(tmpFile.exists()) tmpFile.delete();
						tmpFile.createNewFile();
					}catch(Exception e){
						e.printStackTrace();
						handler.sendEmptyMessage(0);
						return;
					}
					//Log.i(tag, "临时文件创建成功");
					FileUtils.copyfile(FileUtils.openStream(context, uri), tmpFile);
					//压缩到目标文件
					scale(maxSide, tmpFile, dest);
					//通知调用者
					handler.sendEmptyMessage(0);
					tmpFile.delete();
				}
			}).start();
		}catch(Exception e){
			handler.sendEmptyMessage(0);
		}
	}

	/**
	 * Bitmap转换成字节数组
	 * @param bitmap
	 * @param format
	 * @return
	 */
	public static byte[] bmpToByteArray(Bitmap bitmap, Bitmap.CompressFormat format){
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(format, 100, stream);
		byte[] byteArray = stream.toByteArray();
		return byteArray;
	}

	/**
	 * Bitmap转换成字节数组
	 * @param bitmap
	 * @return
	 */
	public static byte[] bmpToByteArray(Bitmap bitmap){
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		return byteArray;
	}

	/*	public static boolean copyfile(InputStream in, File dest){
		try{
			OutputStream out = new FileOutputStream(dest);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0){
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
			//System.out.println("文件复制成功.");
			return true;
		}catch(FileNotFoundException ex){
			//System.out.println("文件复制失败:"+ex.getMessage() + " in the specified directory.");
		}catch(IOException e){
			//System.out.println("文件复制失败:"+e.getMessage());			
		}
		return false;
	}

	public static InputStream openStream(Context context, Uri uri){
		InputStream is = null;
		try {
			is = context.getContentResolver().openInputStream(uri);
			//Log.i(tag, "文件流打开成功，路径="+uri.getPath());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return is;
	}
	 */
	public static void openPath(Context context, Uri uri){
		InputStream is = null;
		try {
			is = context.getContentResolver().openInputStream(uri);
			//Convert your stream to data here
			is.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/** 水平方向模糊度 10*/
	private static float hRadius = 4;
	/** 竖直方向模糊度 10 */
	private static float vRadius = 4;
	/** 模糊迭代度 7 */
	private static int iterations = 3;

	/**
	 * 高斯模糊
	 */
	public static Drawable BoxBlurFilter(Bitmap bmp) {
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		int[] inPixels = new int[width * height];
		int[] outPixels = new int[width * height];
		Bitmap bitmap = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888);
		bmp.getPixels(inPixels, 0, width, 0, 0, width, height);
		for (int i = 0; i < iterations; i++) {
			blur(inPixels, outPixels, width, height, hRadius);
			blur(outPixels, inPixels, height, width, vRadius);
		}
		blurFractional(inPixels, outPixels, width, height, hRadius);
		blurFractional(outPixels, inPixels, height, width, vRadius);
		bitmap.setPixels(inPixels, 0, width, 0, 0, width, height);
		Drawable drawable = new BitmapDrawable(bitmap);
		return drawable;
	}

	public static void blur(int[] in, int[] out, int width, int height,
			float radius) {
		int widthMinus1 = width - 1;
		int r = (int) radius;
		int tableSize = 2 * r + 1;
		int divide[] = new int[256 * tableSize];

		for (int i = 0; i < 256 * tableSize; i++)
			divide[i] = i / tableSize;

		int inIndex = 0;

		for (int y = 0; y < height; y++) {
			int outIndex = y;
			int ta = 0, tr = 0, tg = 0, tb = 0;

			for (int i = -r; i <= r; i++) {
				int rgb = in[inIndex + clamp(i, 0, width - 1)];
				ta += (rgb >> 24) & 0xff;
				tr += (rgb >> 16) & 0xff;
				tg += (rgb >> 8) & 0xff;
				tb += rgb & 0xff;
			}

			for (int x = 0; x < width; x++) {
				out[outIndex] = (divide[ta] << 24) | (divide[tr] << 16)
						| (divide[tg] << 8) | divide[tb];

				int i1 = x + r + 1;
				if (i1 > widthMinus1)
					i1 = widthMinus1;
				int i2 = x - r;
				if (i2 < 0)
					i2 = 0;
				int rgb1 = in[inIndex + i1];
				int rgb2 = in[inIndex + i2];

				ta += ((rgb1 >> 24) & 0xff) - ((rgb2 >> 24) & 0xff);
				tr += ((rgb1 & 0xff0000) - (rgb2 & 0xff0000)) >> 16;
				tg += ((rgb1 & 0xff00) - (rgb2 & 0xff00)) >> 8;
			tb += (rgb1 & 0xff) - (rgb2 & 0xff);
			outIndex += height;
			}
			inIndex += width;
		}
	}

	public static void blurFractional(int[] in, int[] out, int width,
			int height, float radius) {
		radius -= (int) radius;
		float f = 1.0f / (1 + 2 * radius);
		int inIndex = 0;

		for (int y = 0; y < height; y++) {
			int outIndex = y;

			out[outIndex] = in[0];
			outIndex += height;
			for (int x = 1; x < width - 1; x++) {
				int i = inIndex + x;
				int rgb1 = in[i - 1];
				int rgb2 = in[i];
				int rgb3 = in[i + 1];

				int a1 = (rgb1 >> 24) & 0xff;
				int r1 = (rgb1 >> 16) & 0xff;
				int g1 = (rgb1 >> 8) & 0xff;
				int b1 = rgb1 & 0xff;
				int a2 = (rgb2 >> 24) & 0xff;
				int r2 = (rgb2 >> 16) & 0xff;
				int g2 = (rgb2 >> 8) & 0xff;
				int b2 = rgb2 & 0xff;
				int a3 = (rgb3 >> 24) & 0xff;
				int r3 = (rgb3 >> 16) & 0xff;
				int g3 = (rgb3 >> 8) & 0xff;
				int b3 = rgb3 & 0xff;
				a1 = a2 + (int) ((a1 + a3) * radius);
				r1 = r2 + (int) ((r1 + r3) * radius);
				g1 = g2 + (int) ((g1 + g3) * radius);
				b1 = b2 + (int) ((b1 + b3) * radius);
				a1 *= f;
				r1 *= f;
				g1 *= f;
				b1 *= f;
				out[outIndex] = (a1 << 24) | (r1 << 16) | (g1 << 8) | b1;
				outIndex += height;
			}
			out[outIndex] = in[width - 1];
			inIndex += width;
		}
	}

	public static int clamp(int x, int a, int b) {
		return (x < a) ? a : (x > b) ? b : x;
	}

	/**
	 * 图片存储到系统相册
	 * @param context
	 * @param title 标题
	 * @param description 描述
	 * @param bitmap
	 * @param sendBroadCast 是否发送通知广播用来刷新相册
	 * @return
	 */
	public static boolean storeImage(Context context, String title, String description, Bitmap bitmap, boolean sendBroadCast){
		boolean success = false;
		try{
			ContentResolver cr = context.getContentResolver();
			CapturePhotoUtils.insertImage(cr, bitmap, title, description);
			success = true;
		}catch(Exception e){
			e.printStackTrace();
			success = false;
		}
		if(sendBroadCast && success){
			try{
				context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return success;
	}

	/**
	 * 读取图片文件的某个区域
	 * @param pathName
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
	public static Bitmap getBitmapRegion(String pathName, int left, int top, int right, int bottom){
		try {
			BitmapRegionDecoder bitmapRegionDecoder = BitmapRegionDecoder.newInstance(pathName, false);
			Rect rect = new Rect(left, top, right, bottom);
			Bitmap bitmap = bitmapRegionDecoder.decodeRegion(rect, null);
			return bitmap;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 返回一个数据的回调
	 * @author planet
	 *
	 */
	public interface ObjectHandler{
		public void onData(Object data);
	}

	/**
	 * 异步读取图片的某个区域
	 * @param pathName
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 * @param objectHandler
	 */
	public static void getBitmapRegionAsync(String pathName, int left, int top, int right, int bottom,
			final ObjectHandler objectHandler){
		Rect rect = new Rect(left, top, right, bottom);
		final Handler handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message arg0) {
				objectHandler.onData(arg0.obj);
				return false;
			}
		});
		new Thread(new ObjectRunnable(pathName, rect) {
			@Override
			public void run() {
				Rect rect = (Rect)obj2;
				String pathName = (String)obj;
				Bitmap data = getBitmapRegion(pathName, rect.left, rect.top, rect.right, rect.bottom);
				//objectHandler.onData(data);
				Message msg = handler.obtainMessage();
				msg.obj = data;
				handler.sendMessage(msg);
			}
		}).start();
	}

	/**
	 * 获取图片宽和高
	 * @param filePath
	 * @return Point.x=宽度, point.y=高度
	 */
	public static Point detectPictureSize(String filePath){
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高
		BitmapFactory.decodeFile(filePath, options);
		return new Point(options.outWidth, options.outHeight);
	}
	
	/**
	 * 获取图片宽高
	 * @param context
	 * @param filePath
	 * @return Point.x=宽度, point.y=高度
	 */
	public static Point detectPictureSizeAssets(Context context, String filePath){
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高
		//BitmapFactory.decodeFile(filePath, options);
		try {
			BitmapFactory.decodeStream(context.getAssets().open(filePath), null, options);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Point(options.outWidth, options.outHeight);
	}
}