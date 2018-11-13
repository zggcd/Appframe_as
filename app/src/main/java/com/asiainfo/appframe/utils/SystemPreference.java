package com.asiainfo.appframe.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.asiainfo.appframe.data.Constants;

/* =========================================================
 * 
 * @version V1.0
 * =========================================================
 */
public class SystemPreference {
	
	/**
	 * 保存用户登陆错误的次数，连续登陆错误到达3次时，需要显示验证码
	 * @param ctx
	 * @param userName
	 * @param value
	 */
	public static void setLoginErrorCount(Context ctx, String userName, int value){
		SharedPreferences myPreference = ctx.getSharedPreferences(
				userName, 0);
		myPreference.edit().putInt("errorCount", value).commit();
	}
	
	/**
	 * 获取用户联系登陆错误的次数，连续登陆错误到达3次时，需要显示验证码
	 * @param ctx
	 * @param userName
	 * @return
	 */
	public static int getLoginErrorCount(Context ctx, String userName){
		SharedPreferences myPreference = ctx.getSharedPreferences(
				userName, 0);
		return myPreference.getInt("errorCount", 0);
	}

	public static String getString(Context ctx, String settingName) {

		SharedPreferences myPreference = ctx.getSharedPreferences(
				Constants.getInstance().PREFS_NAME, 0);
		String strReturn = myPreference.getString(settingName, null);
		if (strReturn != null) {
			try {
				strReturn = Des3Util.decode(strReturn);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return strReturn;
	};

	public static int getInt(Context ctx, String settingName) {

		SharedPreferences myPreference = ctx.getSharedPreferences(
				Constants.getInstance().PREFS_NAME, 0);

		return myPreference.getInt(settingName, 0);
	};

	public static Boolean getBoolean(Context ctx, String settingName) {

		SharedPreferences myPreference = ctx.getSharedPreferences(
				Constants.getInstance().PREFS_NAME, 0);

		return myPreference.getBoolean(settingName, false);
	};

	public static Boolean setString(Context ctx, String settingName,
                                    String value) {
		SharedPreferences myPreference = ctx.getSharedPreferences(
				Constants.getInstance().PREFS_NAME, 0);

		try {
			myPreference.edit().putString(settingName, Des3Util.encode(value)).commit();
			return true;
		} catch (Exception E) {
			return false;
		}
	};

	public static Boolean setInt(Context ctx, String settingName, int value) {
		SharedPreferences myPreference = ctx.getSharedPreferences(
				Constants.getInstance().PREFS_NAME, 0);

		try {
			myPreference.edit().putInt(settingName, value).commit();
			return true;
		} catch (Exception E) {
			return false;
		}
	};

	public static Boolean setBoolean(Context ctx, String settingName,
                                     Boolean value) {
		SharedPreferences myPreference = ctx.getSharedPreferences(
				Constants.getInstance().PREFS_NAME, 0);
		try {
			myPreference.edit().putBoolean(settingName, value).commit();
			return true;
		} catch (Exception E) {
			return false;
		}
	};

	public static void remove(Context ctx, String settingName) {
		SharedPreferences myPreference = ctx.getSharedPreferences(
				Constants.getInstance().PREFS_NAME, 0);
		myPreference.edit().remove(settingName).commit();
	};

}