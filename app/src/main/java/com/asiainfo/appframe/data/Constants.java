package com.asiainfo.appframe.data;

import android.annotation.SuppressLint;
import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class Constants {

	private static Constants constants = null;

	public List<Activity> activityList = new ArrayList<Activity>();
	// 记录活动的activity name
	public List<String> activityNameList = new ArrayList<String>();

	// 应用资源保存根目录
	@SuppressLint("SdCardPath")
	public static final String BASE_DIR = "/sdcard/UniDesk/";
	public static final String PHOTO_DIR = "/Appframe/Photo/";
	public static final String MEDIA_DIR = "/Appframe/Media/";
	public static Constants getInstance() {
		if (constants == null) {
			constants = new Constants();
		}
		return constants;
	}

	//button标识
	public static final int BTN_FLAG_HOME = 0x01;
	public static final int BTN_FLAG_TASK = 0x01 << 1;
	public static final int BTN_FLAG_MSG = 0x01 << 2;
	public static final int BTN_FLAG_MINE = 0x01 << 3;

	//fragment标识
	public static final String FRAGMENT_FLAG_HOME = "首页";
	public static final String FRAGMENT_FLAG_TASK = "任务";
	public static final String FRAGMENT_FLAG_MSG = "消息";
	public static final String FRAGMENT_FLAG_MINE = "我的";

	public final String SHAREDPREFERENCES_NAME = "first_pref";
	// 存储文件
	public String PREFS_NAME = "SOMS.com";
	public String desktopApkUrl;
	/**
	 * 忽略更新
	 */
	public static final String IGNORE = "ignore";
	public static final String desktopUrl = "https://ztyx.telecomjs.com/Android";

//	public String getUiLayout;
//	public String getAccessToken;
//	public String refreshAccessToken;
//	public String preUiLayout;
//	public String getAreaCode;
//	public String getValidateCode;
//	public String getPostNum;
//	public String getTeamKey;
	
}
