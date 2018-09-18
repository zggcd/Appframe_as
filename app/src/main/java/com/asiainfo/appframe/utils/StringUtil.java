package com.asiainfo.appframe.utils;

import java.util.Random;

public class StringUtil {

	public static boolean isEmpty(String str)
	{
		if (str == null || str.trim().equals(""))
			return true;
		else
			return false;
	}
	
	/**
	 * 生成任意长度的随机字符串
	 * @param length 表示生成字符串的长度  
	 * @return
	 */
	public static String getRandomString(int length) {
//	    String base = "abcdefghijklmnopqrstuvwxyz0123456789";     
	    String base = "0123456789";     
	    Random random = new Random();     
	    StringBuffer sb = new StringBuffer();     
	    for (int i = 0; i < length; i++) {     
	        int number = random.nextInt(base.length());     
	        sb.append(base.charAt(number));     
	    }     
	    return sb.toString();     
	 }
	
}
