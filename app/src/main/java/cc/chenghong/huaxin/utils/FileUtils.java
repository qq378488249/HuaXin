package cc.chenghong.huaxin.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;

public class FileUtils {
	static final String TAG = FileUtils.class.getSimpleName();

	/**
	 * 对象保存到文件
	 * @param obj
	 * @param path
	 */
	public static void saveToFile(Serializable obj, String path){
		try {
			FileOutputStream fos = new FileOutputStream(path);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(obj);
			oos.flush();
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从文件读取对象
	 * @param filePath
	 * @param clazz
	 * @return
	 */
	public static <T> T readFromFile(String filePath, Class<T> clazz) {
		try {
			FileInputStream fis = new FileInputStream(filePath);
			ObjectInputStream ois = new ObjectInputStream(fis);
			T obj = (T)ois.readObject();
			ois.close();
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 检查sd卡是否存在
	 * @return
	 */
	public static boolean hasSdcard() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * sd卡根目录创建文件夹
	 * @param name
	 */
	public static void createExternalDir(String name) {
		if(hasSdcard()){
			File file = new File(Environment.getExternalStorageDirectory(), name);
			file.mkdir();
		}
	}

	/**
	 * 获取sd卡根目录的文件夹，如果文件夹不存在创建新文件夹
	 * @param name
	 * @return
	 */
	public static File getExternalDir(String name){
		if(hasSdcard()){
			File file = new File(Environment.getExternalStorageDirectory(), name);
			if(!file.exists()){
				file.mkdir();
			}
			return file;
		}
		return null;
	}

	/**
	 * 获取sd卡根目录创建文件
	 * @param name
	 * @return
	 */
	public static File createFileExternal(String fileName){
		if(hasSdcard()){
			File file = new File(Environment.getExternalStorageDirectory(), fileName);
			return file;
		}
		return null;
	}
	
	public static void writeToFile(String fileName, String str){
		File file = createFileExternal(fileName);
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writeToFile(file, str);
	}
	
	public static void writeToFile(File file, String str){
		try {
			FileWriter fw = new FileWriter(file);
			fw.write(str);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 复制文件
	 * @param context
	 * @param uri
	 * @param dest
	 * @param handler
	 */
	public static void copyFile(final Context context,final Uri uri, final File dest, final Handler handler){
		try{
			new Thread(new Runnable() {
				@Override
				public void run() {
					copyfile(openStream(context, uri), dest);
					Message msg = handler.obtainMessage();
					msg.obj = dest;
					handler.sendMessage(msg);
				}
			}).start();;
		}catch(Exception e){
			handler.sendEmptyMessage(0);
		}
	}

	/**
	 * 打开Uri的文件流
	 * @param context
	 * @param uri
	 * @return
	 */
	public static InputStream openStream(Context context, Uri uri){
		InputStream is = null;
		try {
			is = context.getContentResolver().openInputStream(uri);
			Log.i(TAG, "文件流打开成功，路径="+uri.getPath());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return is;
	}

	/**
	 * 复制文件
	 * @param srFile
	 * @param dtFile
	 */
	public static void copyfile(String srFile, String dtFile){
		try{
			File f1 = new File(srFile);
			File f2 = new File(dtFile);
			InputStream in = new FileInputStream(f1);
			OutputStream out = new FileOutputStream(f2);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0){
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
			System.out.println("File copied.");
		}
		catch(FileNotFoundException ex){
			System.out.println(ex.getMessage() + " in the specified directory.");
			System.exit(0);
		}
		catch(IOException e){
			System.out.println(e.getMessage());			
		}
	}
	
	public static void copyfile(File src, File dest){
		try {
			copyfile(new FileInputStream(src), dest);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 复制文件
	 * @param in
	 * @param dest
	 * @return
	 */
	public static boolean copyfile(InputStream in, File dest){
		try{
			OutputStream out = new FileOutputStream(dest);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0){
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
			System.out.println("文件复制成功.");
			return true;
		}catch(FileNotFoundException ex){
			System.out.println("文件复制失败:"+ex.getMessage() + " in the specified directory.");
		}catch(IOException e){
			System.out.println("文件复制失败:"+e.getMessage());			
		}
		return false;
	}
}
