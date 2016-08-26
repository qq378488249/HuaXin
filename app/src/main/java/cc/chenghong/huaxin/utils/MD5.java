package cc.chenghong.huaxin.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {    
    public static String getMD5(String val){    
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md5.update(val.getBytes());    
        byte[] m = md5.digest();
        return getString(m);    
    }
    private static String getString(byte[] b){
    	String ret = "";
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			ret += hex.toUpperCase();
		}
		return ret;
    }
}