package cc.chenghong.huaxin.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 * @author thin_blue
 *
 */
public class DataUtils {
	public static final String yyyy_MM_dd = "yyyy-mm-dd";
	/**
	 * 获取指定格式的当前时间
	 * @param str 时间格式
	 * @return 字符串类型的当前时间
	 */
	public static String getData(String str){
		SimpleDateFormat sdf = new SimpleDateFormat(str);
		return sdf.format(new Date());
	}
	
	/**
	 * 获取"年-月-日 时(24小时制):分:秒"格式的当前时间
	 * @return 字符串类型的当前时间
	 */
	public static String getData(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}

	/**
	 * 返回时间1是否大于时间2
	 * @param t1
	 * @param t2
	 * @param str_sdf 时间格式
	 * @return true s1大于或等于s2 ，否则反之
	 * @throws ParseException
	 */
	public static boolean data1_compare_data2(String t1,String t2,String str_sdf){
		if (t1.equals(t2)){
			return true;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(str_sdf);
		long result = 0;
		try {
			Date d1 = sdf.parse(t1);
			Date d2 = sdf.parse(t2);
			result = d1.getTime()-d2.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result > 0;
	}
}
