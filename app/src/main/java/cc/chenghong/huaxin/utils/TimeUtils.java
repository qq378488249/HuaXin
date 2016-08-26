package cc.chenghong.huaxin.utils;

public class TimeUtils {
	/**
	 * 天包含的毫秒
	 */
	final static long dayMs = 1000*60*60*24;
	/**
	 * 小时包含的毫秒
	 */
	final static long hourMs = 1000*60*60;
	/**
	 * 分钟包含的毫秒
	 */
	final static long minMs = 1000*60;
	/**
	 * 秒包含的毫秒
	 */
	final static long secMs = 1000;

	/**
	 * 获取指定毫秒数 包含的天数
	 * @param ms
	 * @return
	 */
	public static int getDay(long ms){
		return (int) (ms / dayMs);
	}

	/**
	 * 获取指定毫秒数 包含的(去掉天数以后的)小时
	 * @param ms
	 * @return
	 */
	public static int getHour(long ms){
		long myTotalMs = ms;
		int day = (int) (myTotalMs / dayMs);
		//如果超过了天，减去天
		myTotalMs -= dayMs*day;
		int hour = (int) (myTotalMs / hourMs);
		return hour;
	}

	/**
	 * 获取指定毫秒数 包含的(去掉天、小时以后的)分钟
	 * @param ms
	 * @return
	 */
	public static int getMin(long ms){
		long myTotalMs = ms;
		int day = (int) (myTotalMs / dayMs);
		//如果超过了天，减去天
		myTotalMs -= dayMs*day;
		int hour = (int) (myTotalMs / hourMs);
		myTotalMs -= hourMs*hour;
		int min = (int) (myTotalMs / minMs);
		return min;
	}

	/**
	 * 获取指定毫秒数 包含的(去掉天、小时、分钟以后的)秒
	 * @param ms
	 * @return
	 */
	public static int getMs(long ms){
		long myTotalMs = ms;
		int day = (int) (myTotalMs / dayMs);
		//如果超过了天，减去天
		myTotalMs -= dayMs*day;
		//超过了小时减去小时
		int hour = (int) (myTotalMs / hourMs);
		myTotalMs -= hourMs*hour;
		//超过了分钟减去分钟
		int min = (int) (myTotalMs / minMs);
		myTotalMs -= minMs*min;
		int sec = (int) (myTotalMs / secMs);
		return sec;
	}

	/**
	 * 存储到文件的计数器<br />
	 * 返回上次计数时间到现在，还剩余多少秒
	 * @param key
	 * @param totalSeconds 总共多少秒
	 * @return
	 */
	public static int getRemainingSeconds(String key, int totalSeconds){
		long saveTime;
		int remainSeconds = 0;
		if(!SharedPreferencesHelper.contain(key)){
			saveTime = System.currentTimeMillis();
			SharedPreferencesHelper.saveLong(key, System.currentTimeMillis());
		}else{
			saveTime = SharedPreferencesHelper.getLong(key);
		}
		//计算经过的秒数
		int elapse = (int) ((System.currentTimeMillis()-saveTime)/1000);
		remainSeconds = totalSeconds - elapse;
		//时间小于0, 删除key
		if(remainSeconds <= 0){
			SharedPreferencesHelper.remove(key);
		}
		return remainSeconds;
	}
}
