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
	
	public String getUiLayout;
	public String getAccessToken;
	public String refreshAccessToken;
	public String preUiLayout;
	public String getAreaCode;
	public String getValidateCode;
	public String getPostNum;
	public String getTeamKey;
	
}
