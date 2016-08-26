package cc.chenghong.huaxin.activity;

import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public class EhUtils {
	
	private static final String[] hexArr = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};

	
    /**
	 * 将十六进制字符串换为二进制数组
	 * @param hexString
	 * @return
	 */

	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {    
			return null;    
		}    
		hexString = hexString.toUpperCase();    
		int length = hexString.length() / 2;    
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];    
		for (int i = 0; i < length; i++) {    
			int pos = i * 2;    
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));    
		}    
		return d;    
	}  
	
	public static byte[] Swap2Byte(byte[] c) {    
		byte[] r = new byte[2];
		r[0] = c[1];
		r[1] = c[0];
		return r;    
	}
	
	public static char[] hexStringToChars(String hexString) {
		if (hexString == null || hexString.equals("")) {    
			return null;    
		}    
		hexString = hexString.toUpperCase();    
		int length = hexString.length() / 2;    
		char[] hexChars = hexString.toCharArray();
		char[] d = new char[length];    
		for (int i = 0; i < length; i++) {    
			int pos = i * 2;    
			d[i] = (char) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));    
		}    
		return d;    
	}
	
	public String hexStrToString(String hexString) {
		if (hexString == null || hexString.equals("")) {    
			return null;    
		}   
		hexString = hexString.toUpperCase(); 
		Log.i("lkl", "str = " + hexString);
		int length = hexString.length()/2;
		
		Log.i("record", "lenth = " + length);
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];    
		for(int i=0; i<length; i++){	
			d[i] = (byte)(charToByte(hexChars[2*i])<<4 | charToByte(hexChars[2*i+1]));
			if(d[i] == -1)
			{
				d[i] = 0;
				break;
			}
			Log.i("record", "i= " + i + ":" + d[i]);
		}
		String r = new String(d);
		return r;
	}  

	private static byte charToByte(char c) {    
		return (byte) "0123456789ABCDEF".indexOf(c);    
	}
	
	
	
	/**
	 * 将二进制数组换为十六进制字符串
	 * @param byt
	 * @return
	 */
	public static String byte2HexStr(byte[] byt){
		StringBuffer strRet = new StringBuffer();
		for(int i=0;i<byt.length;i++){ 
			strRet.append(hexArr[(byt[i] & 0xf0)/16]); 
			strRet.append(hexArr[byt[i] & 0x0f]); 
			//strRet.append(" ");
		} 
		return strRet.toString(); 
	}

	
	// char转byte

	private byte[] getBytes (char[] chars) {
	   Charset cs = Charset.forName("UTF-8");
	   CharBuffer cb = CharBuffer.allocate(chars.length);
	   cb.put (chars);
	                 cb.flip ();
	   ByteBuffer bb = cs.encode (cb);
	  
	   return bb.array();

	}
	
	private static int charToInt(char c) {    
		return (int) "0123456789".indexOf(c);    
	}
	
	public static int StringToInt(String str) {
		if (str == null || str.equals("")) {    
			return 0;    
		}    
		str = str.toUpperCase();    
		int length = str.length();    
		char[] hexChars = str.toCharArray();
		int[] d = new int[length];
		int r = 0;
		for (int i = 0; i < length; i++) {    
			 d[i] = charToInt(hexChars[i]);
			 r *= 10;
			 r += d[i];
		}
		
		Log.i("jinxin", "data lenth ===  " + r);
		return r;    
	} 
	
	public static String byte2PowerStr(byte byt){
		StringBuffer strRet = new StringBuffer();
		byte t = (byte) (256 - byt);
		if((byt & 0x80) == 0x80)
		{
			strRet.append('-');
			strRet.append((hexArr[t/10]));
			strRet.append((hexArr[t%10]));
		}

		return strRet.toString(); 
	}
	
	public static byte PowerStr2Byte(String str) {
		byte r = 0;
		if (str == null || str.equals("")) {    
			return 0;    
		}    
		char[] hexChars = str.toCharArray();   
  
		r = (byte) (charToByte(hexChars[1])*10 + charToByte(hexChars[2]));    
		r = (byte)(256-r);
		return r;    
	}
	
}
