package cc.chenghong.huaxin.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 文件下载工具(单线程下载)，支持断点续传
 * @author planet
 *
 */
public class FileDownloader {
	static final String TAG = FileDownloader.class.getSimpleName();
	/** 下载文件夹 */
	private File dir;
	/** 下载的文件 */
	private File file;
	/** 未下载完成的文件 */
	private File temp;
	/** 断点续传的信息文件(下载成功以后自动删除) */
	private File info;
	/** 下载url */
	private String url;
	/** 存储文件名 */
	private String fileName;
	/**
	 * 是否停止下载
	 */
	private boolean stop = false;
	/**
	 * 下载进度监听器, 返回的消息格式为: msg.obj = DownloadInfo
	 */
	private Handler handler;
	/**
	 * 是否正在下载
	 */
	private boolean downloading = false;

	/**
	 * 停止下载
	 */
	public void stop(){
		stop = true;
	}

	/**
	 * 正在下载中
	 * @return
	 */
	public boolean isDownloading() {
		return downloading;
	}

	/**
	 * 创建一个下载
	 * @param url 下载链接
	 * @param downloadDir 文件保存路径
	 * @param handler下载进度监听器, 返回的消息格式为: msg.obj = DownloadInfo
	 */
	public FileDownloader(String url, String fileName, File downloadDir, Handler handler) {
		this.handler = handler;
		this.url = url;
		this.dir = downloadDir;
		//如果文件名为空，自动从url截取文件名
		//if(fileName == null){
		//    this.fileName = url.substring(url.lastIndexOf("/"));
		// }else{
		this.fileName = fileName;
		//}
		this.file = new File(this.dir, this.fileName);
		//临时下载文件, 使用[url的MD5].temp作为文件名
		this.temp = new File(this.dir, fileName+".temp");
		//文件下载进度信息使用[url的MD5].info作为文件名
		this.info = new File(this.dir, fileName+".info");

		Log.i(TAG, "========== 下载文件 ============");
		Log.i(TAG, "下载路径: "+dir.getAbsolutePath()+" exist:"+dir.exists());
		Log.i(TAG, "临时文件: "+temp.getAbsolutePath()+" exist:"+temp.exists());
		Log.i(TAG, "下载文件: "+file.getAbsolutePath()+" exist:"+file.exists());
		Log.i(TAG, "info文件: "+info.getAbsolutePath()+" exist:"+info.exists());
		Log.i(TAG, "===============================");

		//如果info文件不存在且file不存在, 说明是新建下载
		if(!info.exists() && !file.exists()){
			createSimpleDownloadInfo();
		}else{
			Log.i(TAG, "续传文件");
		}
	}
	/**
	 * 获取下载的文件
	 * @return
	 */
	public File getFile() {
		return file;
	}

	/**
	 * 检查文件是否下载完毕
	 * @return
	 */
	public boolean isDownloadSuccess(){
		if(file.exists()) return true;
		return false;
		//DownloadInfo info = getDownloadInfo();
		//return info.compeleteSize == info.endPos;
	}

	/**
	 * 设置下载监听器
	 * @param handler下载进度监听器, 返回的消息格式为: msg.obj = DownloadInfo
	 */
	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	/**
	 * 开始下载
	 */
	public void start(){
		if(isDownloadSuccess()){
			Log.i(TAG, "文件已经下载完成: "+url);
			reSendMessage();
			return;
		}
		if(downloading){
			Log.i(TAG, "下载中... "+url);
			return;
		}
		stop = false;
		downloading = true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				try{
					//如果endPos==-1说明还没有开始下载，并且未曾获取过文件大小
					if(getDownloadInfo().endPos == -1){
						createDownloadInfo();
					}
					if(!info.exists()){
						Log.e(TAG, "文件下载失败! "+FileDownloader.this.url);
						downloading = false;
						return;
					}
					//开始下载
					HttpURLConnection connection = null;
					RandomAccessFile randomAccessFile = null;
					InputStream is = null;
					try {
						Log.i(TAG, "开始下载: "+FileDownloader.this.url);
						URL url = new URL(FileDownloader.this.url);
						connection = (HttpURLConnection) url.openConnection();
						connection.setConnectTimeout(5000);
						connection.setRequestMethod("GET");
						// 设置范围，格式为Range：bytes x-y;
						DownloadInfo downloadInfo = getDownloadInfo();
						connection.setRequestProperty("Range", "bytes="
								+ (downloadInfo.startPos + downloadInfo.compeleteSize) + "-" + downloadInfo.endPos);
						randomAccessFile = new RandomAccessFile(FileDownloader.this.temp, "rwd");
						randomAccessFile.seek(downloadInfo.startPos + downloadInfo.compeleteSize);
						// 将要下载的文件写到保存在保存路径下的文件中
						is = connection.getInputStream();
						byte[] buffer = new byte[4096];
						int length = -1;
						int saveInfoTrunkSize = 0;
						while ((length = is.read(buffer)) != -1) {
							randomAccessFile.write(buffer, 0, length);
							downloadInfo.compeleteSize += length;
							saveInfoTrunkSize += length;
							//更新断点续传信息
							if(saveInfoTrunkSize>=1024*1024){
								//数据下载量超过1M,更新downloadInfo文件
								saveInfoTrunkSize = 0;
								saveDownloadInfo(downloadInfo);
							}
							// 用消息将下载信息传给进度条，对进度条进行更新
							sendMessage(downloadInfo);
							if (stop) {
								downloading = false;
								//暂停以后更新downloadInfo
								saveDownloadInfo(downloadInfo);
								break;
							}
						}
						if(stop){
							Log.i(TAG, "下载中止");
							return;
						}
						//修改临时文件为正式文件
						temp.renameTo(file);
						//最后通知下载完成
						downloadInfo.downloadSuccess = true;
						sendMessage(downloadInfo);
						//删除info
						info.delete();
						//文件下载完成
						Log.i(TAG, "文件下载完成: "+FileDownloader.this.file.getAbsolutePath());
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						try {
							is.close();
							randomAccessFile.close();
							connection.disconnect();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				downloading = false;
			}
		}).start();
	}

	/**
	 * 发送一个通知
	 * @param downloadInfo
	 */
	private void sendMessage(DownloadInfo downloadInfo){
		if(FileDownloader.this.handler != null){
			try{
				Message message = Message.obtain();
				message.what = 1;
				message.obj = downloadInfo;
				FileDownloader.this.handler.sendMessage(message);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	/**
	 * 文件已经下载完，此方法可以重新发送DownloadInfo到handler
	 * @param downloadInfo
	 */
	public void reSendMessage(){
		if(FileDownloader.this.handler != null){
			try{
				Message message = Message.obtain();
				message.what = 1;
				DownloadInfo downloadInfo = getDownloadInfo();
				//文件下载完成以后已经删除info文件, 需要重新创建info传递过去
				if(downloadInfo == null && file.exists()){
					downloadInfo = new DownloadInfo();
					downloadInfo.downloadSuccess = true;
					downloadInfo.url = url;
					downloadInfo.fileName = file.getAbsolutePath();
				}
				message.obj = downloadInfo;
				FileDownloader.this.handler.sendMessage(message);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	/**
	 * 创建一个空的DownloadInfo
	 */
	private void createSimpleDownloadInfo(){
		DownloadInfo downloadInfo = new DownloadInfo();
		//开始点0 结束点总长度 完成0
		//downloadInfo.compeleteSize = 0;
		//downloadInfo.startPos = 0;
		downloadInfo.endPos = -1;
		downloadInfo.url = this.url;
		downloadInfo.fileName = file.getAbsolutePath();
		saveDownloadInfo(downloadInfo);
	}

	/**
	 * 第一次下载文件, 获取文件大小, 创建临时文件
	 * @throws IOException 
	 */
	private void createDownloadInfo() throws IOException {
		//读取文件大小
		URL url = new URL(this.url);
		HttpURLConnection connection = (HttpURLConnection) url
				.openConnection();
		connection.setConnectTimeout(5000);
		connection.setRequestMethod("GET");
		int fileSize = connection.getContentLength();
		//存储到临时文件
		RandomAccessFile accessFile = new RandomAccessFile(this.temp, "rwd");
		accessFile.setLength(fileSize);
		accessFile.close();
		connection.disconnect();
		//保存断点续传信息
		DownloadInfo downloadInfo = getDownloadInfo();
		//开始点0 结束点总长度 完成0
		//downloadInfo.compeleteSize = 0;
		//downloadInfo.startPos = 0;
		downloadInfo.endPos = fileSize-1;
		downloadInfo.url = this.url;
		saveDownloadInfo(downloadInfo);
		Log.i(TAG, "DownloadInfo创建完成!");
	}

	/**
	 * 保存断点续传信息
	 * @param downloadInfo
	 */
	public void saveDownloadInfo(DownloadInfo downloadInfo){
		try {
			FileOutputStream fos = new FileOutputStream(this.info.getAbsolutePath());
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(downloadInfo);
			oos.flush();
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取断点续传信息
	 * @return
	 */
	public DownloadInfo getDownloadInfo() {
		try {
			if(this.info.exists()){
				FileInputStream fis = new FileInputStream(this.info.getAbsolutePath());
				ObjectInputStream ois = new ObjectInputStream(fis);
				DownloadInfo info = (DownloadInfo)ois.readObject();
				ois.close();
				return info;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
