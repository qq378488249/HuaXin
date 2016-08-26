package cc.chenghong.huaxin.utils;

import android.os.Handler;
/**
 * 循环执行Runnable
 * @author planet
 *
 */
public class Interval {
	/**
	 * 设置循环
	 * @param runnable
	 * @param millisec
	 * @return
	 */
	public static ObjectHandler setInterval(Runnable runnable, int millisec){
		ObjectHandler handler = new ObjectHandler(runnable);
		handler.postDelayed(new ObjectRunnable(handler, millisec){
			@Override
			public void run() {
				ObjectHandler handler = (ObjectHandler)obj;
				int millisec = (Integer)obj2;
				Runnable runnable = (Runnable) handler.obj1;
				runnable.run();
				handler.postDelayed(this, millisec);
			}
		}, millisec);
		return handler;
	}
	
	/**
	 * 取消循环
	 * @param thread
	 */
	public static void clearInterval(ObjectHandler handler){
		handler.removeCallbacks((Runnable)handler.obj1);
	}
}
