package com.asiainfo.appframe.utils;

import java.util.UUID;

import org.vudroid.core.utils.MD5StringUtil;

import com.asiainfo.appframe.activity.WebControlerActivity;
import com.asiainfo.appframe.bean.AccessTokenResponse;
import com.asiainfo.appframe.bean.AuthInfoResult;
import com.asiainfo.appframe.bean.JumpToWebWindowResponse;
import com.asiainfo.appframe.bean.SDKJSType;
import com.asiainfo.appframe.net.ApiClient;
import com.asiainfo.appframe.permission.AddPermission;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class SDKUtil {
	
	private Context context;
	
	//SDK url
	public static String PortUrl;						//baseurl
	public static String InvokeUrl;					//小程序URL
	public static String ValidateCodeUrl;				//获取验证码
	public static String AreaCodeUrl;					//获取地区码
	public static String AccessTokenUrl;				//获取accesstoken，登录
	public static String refreshAccessTokenUrl;		//刷新accesstoken
	public static String UAccessTokenUrl;				//获取Uname相关的accesstoken
	
	public static String UploadImageUrl;				//图片上传接口
	
	public static String jumpToWebUrl;					//跳转web页
	
	//nomal url
	private static String UpdateDeviceInfoUrl;
	public static String RecordH5Invoke;
	
	public static String accessToken = "";
	public static String refreshToken = "";
	public static String phone_num = "";
	public static String staff_id = "";
	public static String app_id = "";
	public static String appSecret = "";
	public static String key_public = "";
	public static String teamKey = "";
	public static String mac;
	public static String expires_in;
	public static long start_time, end_time;
	public static String staff_code = "";
	public static String user_name = "";
	public static String area_code = "";
	
	public static String uuid;//app唯一识别号
	
	private static AddPermission addPermission;
	
	SharedPreferences mSP;
	private AuthHandler authHandler = null;
	private SDKAuthCallBack callback;
	
	private static SDKUtil sdkUtil = null;
	
	public static SDKUtil getInstance(Context context, SDKAuthCallBack callback){
		if(sdkUtil == null){
			sdkUtil = new SDKUtil(context, callback);
		}
		return sdkUtil;
	}
	
	private SDKUtil(Context context, SDKAuthCallBack callback){
		
		if(context == null){
			return;
		}
		
		this.context = context;
		this.callback = callback;
		this.app_id = context.getResources().getString(ResourceUtil.getStringId(context, "APP_ID"));
		this.appSecret = context.getResources().getString(ResourceUtil.getStringId(context, "APPSECRET"));
		this.key_public = context.getResources().getString(ResourceUtil.getStringId(context, "KEY_PUBLIC"));
		this.mac = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
		addPermission = new AddPermission((Activity)context);
		addPermission.addPermission(permissionsListener, AddPermission.CODE_PERMISSIONS_STORAGE);
		
	}
	
	public AddPermission.PermissionsListener permissionsListener = new AddPermission.PermissionsListener() {
		@Override
		public void onPermissionListener(boolean hasPermission, int code) {
			if(hasPermission){
				init();
				initData();
			}
		}
	};
	
	/**
	 * 重置，解决串联分页面不方便调用的问题
	 * @param callback
	 */
	public void change(Context context, SDKAuthCallBack callback){
		if(sdkUtil != null){
			this.callback = callback;
			this.context = context;
		}
	}
	
	/**
	 * 初始化配置信息
	 */
	private void init(){
		
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    	String androidId = "" + Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
    	String tmDevice = "" + tm.getDeviceId();
	    String tmSerial = "" + tm.getSimSerialNumber();
	    UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
		
		this.uuid = deviceUuid.toString();
		
		PortUrl = context.getResources().getString(ResourceUtil.getStringId(context, "PortUrl"));
//		InvokeUrl = context.getResources().getString(ResourceUtil.getStringId(context, "InvokeUrl"));
		InvokeUrl = PortUrl + "/gateway/special/invoke?";
		
		ValidateCodeUrl = PortUrl + "/gateway/auth/getAuthCode";
		AreaCodeUrl = PortUrl + "/gateway/auth/getAreaCode";
		AccessTokenUrl = PortUrl + "/gateway/auth/getAccessToken";
		UAccessTokenUrl = PortUrl + "/gateway/auth/getUAccessToken";
		refreshAccessTokenUrl = PortUrl + "/gateway/auth/updateAccessToken";
		jumpToWebUrl = PortUrl + "/gateway/app/invoke";
		
		UploadImageUrl = PortUrl + "/gateway/common/out/upload?";
		
		UpdateDeviceInfoUrl = PortUrl + "/gateway/auth/getDevice";
		RecordH5Invoke = PortUrl +"/gateway/common/out/recordH5Invoke";
		
		authHandler = new AuthHandler();
		mSP = context.getSharedPreferences("APPFRAME_SDK", Context.MODE_PRIVATE);
	}
	
	/**
	 * 初始化用户数据
	 */
	private void initData(){
		accessToken = mSP.getString("accessToken", "");
		refreshToken = mSP.getString("refreshToken", "");
		phone_num = mSP.getString("phone_num", "");
		staff_id = mSP.getString("staff_id", "");
		teamKey = mSP.getString("teamKey", "");
		staff_code = mSP.getString("staff_code", "");
		user_name = mSP.getString("user_name", user_name);
		area_code = mSP.getString("area_code", area_code);
	}
	
	/**
	 * SDK跳转web页
	 */
	public void invokeH5Ability(String storeName, Object obj, SDKJSType type){
//		Toast.makeText(context, accessToken, 3000).show();
		ApiClient.jumpToWebWindow(jumpToWebUrl + "?access_token=" + accessToken, authHandler , 5, storeName, type.getSignature(), type.getParams() );
	}
	
	/**
	 * SDK跳转web页,指定url后拼参数
	 */
	public void jumpToWebWindow(String url){
		Intent intent = new Intent(context, WebControlerActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("webUrl", url);
		intent.putExtras(bundle);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	
	/**
	 * SDK跳转web页,指定url后拼参数
	 * @param storeName 别名
	 */
	public void invokeMiniProgram(String storeName){
		if(!StringUtil.isEmpty(storeName) && !StringUtil.isEmpty(accessToken) ){
			Intent intent = new Intent(context, WebControlerActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("webUrl", InvokeUrl + "access_token=" + accessToken + "&abilityalias=" + storeName);
			intent.putExtras(bundle);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}
	}
	
	/**
	 * SDK跳转web页,指定url后拼参数
	 * @param storeName 别名
	 */
	public void invokeMiniProgram(){
		if(!StringUtil.isEmpty(accessToken) ){
			Intent intent = new Intent(context, WebControlerActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("webUrl", InvokeUrl + "access_token=" + accessToken + "&abilityalias=");
			intent.putExtras(bundle);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}
	}
	
	/**
	 * 获取验证码
	 */
	public void getValidateCode(String account){
		ApiClient.getValidateCode(ValidateCodeUrl, authHandler, 3, account.trim(), app_id);
	}
	
	/**
	 * 获取地区码
	 */
	public void getAreaCode(String account){
		ApiClient.getAreaCode(AreaCodeUrl, authHandler, 2, account);
	}
	
	/**
	 * 获取accessToken，登录
	 * @param phone_num 手机号
	 * @param pwd 验证码
	 * @param areaCode 地区码
	 * @param staff_id staff_id
	 */
	public void getAccessToken(String account, String smscode, String password, String areaCode){
		
		this.phone_num = account;
		this.staff_id = staff_id;
		
		Log.d("Appframe", "== APP_ID ==>" + app_id);
		Log.d("Appframe", "== APPSECRET ==>" + appSecret);
		
		String mac = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
		ApiClient.getAccessToken( AccessTokenUrl, authHandler, 1, account, smscode, password, app_id, areaCode, mac, staff_id, appSecret);
	}
	
	/**
	 * 刷新accresstoken,刷新
	 */
	public void refreshAccessToken(){
//		if(phone_num != null && phone_num.length() > 0){
			ApiClient.refreshAccessToken(refreshAccessTokenUrl, authHandler, 4, 0, phone_num, mac, refreshToken);
//		}
	}
	
	/**
	 * 获取Uname相关联的accesstoken
	 * @param phone_num	手机号
	 * @param staff_code 工号
	 * @param user_name	名称
	 * @param area_code	地区码
	 */
	public void getUAccessToken(String account, String staff_code, String user_name, String area_code){
		if( !StringUtil.isEmpty(account) && !StringUtil.isEmpty(staff_code) && !StringUtil.isEmpty(user_name) && !StringUtil.isEmpty(area_code)){
			this.phone_num = account;
			this.staff_code = staff_code;
			this.user_name = user_name;
			this.area_code = area_code;
			ApiClient.getUAccessToken(UAccessTokenUrl, authHandler, 7, account, staff_code, user_name, area_code, app_id, mac);
		}else{
			Toast.makeText(context, "传入参数有误", Toast.LENGTH_SHORT).show();
		}
	}
	
	private class AuthHandler extends Handler{
		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Gson gson = new Gson();
			switch (msg.what) {
			case 1:		//登录
				AccessTokenResponse response = gson.fromJson((String)msg.obj, AccessTokenResponse.class);
				if(response.getCode() == 1){
					String deviceId = SDKUtil.mac;
					String osName = android.os.Build.MODEL;
					String osPlatform = "ANDROID";
					String osVersion = android.os.Build.VERSION.RELEASE;
					String screenSize = CommonUtil.getWindowSize(context)[1] + "x" + CommonUtil.getWindowSize(context)[0];
					String deviceType = "3";
					ApiClient.updateDeviceInfo(SDKUtil.UpdateDeviceInfoUrl, authHandler, 6, deviceId, osName, osPlatform, osVersion, screenSize, deviceType);
					
					accessToken = response.getAccess_token();
					refreshToken = response.getRefreshtoken();
					String authInfo = gson.toJson(response.getAuthInfoResult());
					
					Editor editor = mSP.edit();
					editor.putString("accessToken", accessToken);
					editor.putString("refreshToken", refreshToken);
					editor.putString("phone_num", phone_num);
					editor.putString("staff_id", staff_id);
					editor.commit();
					
					JsonObject jo = new JsonObject();
					jo.addProperty("code", response.getCode());
					jo.addProperty("msg", response.getMsg());
					jo.addProperty("authInfoResult", authInfo);
					jo.addProperty("expires_in", response.getExpires_in());
					
					callback.onAccessTokenCallback(jo.toString());
				}else{
					callback.onAccessTokenCallback((String)msg.obj);
				}
				
				break;
				
			case 2:		//获取地区码
				
				String areaCodeInfo = (String) msg.obj;
				if(areaCodeInfo == null){
					Toast.makeText(context,"服务器异常", Toast.LENGTH_SHORT).show();
					callback.onError("服务器异常");
					return;
				}
				callback.onAreaCodeCallback(areaCodeInfo);
				break;
			case 3:		//获取验证码
				String validateCode = (String) msg.obj;
				if(validateCode != null){
					callback.onValidateCodeCallback(validateCode);
				}else{
					callback.onValidateCodeCallback(validateCode);
				}
				break;
				
			case 4:		//刷新accesstoken
				AccessTokenResponse accessTokenResponse = gson.fromJson((String)msg.obj, AccessTokenResponse.class);
				if(accessTokenResponse.getCode() == 1){
					
					accessToken = accessTokenResponse.getAccess_token();
					refreshToken = accessTokenResponse.getRefreshtoken();
					
					Editor editor = mSP.edit();
					editor.putString("accessToken", accessToken);
					editor.commit();
					
					JsonObject jo = new JsonObject();
					jo.addProperty("code", accessTokenResponse.getCode());
					jo.addProperty("msg", accessTokenResponse.getMsg());
					jo.addProperty("expires_in", accessTokenResponse.getExpires_in());
					
					callback.onRefreshAccessTokenCallback(jo.toString());
					
				}else{
					callback.onRefreshAccessTokenCallback((String)msg.obj);
				}
				break;
				
			case 5:
				try{
					JumpToWebWindowResponse jumpToWebWindowResponse = gson.fromJson((String)msg.obj, JumpToWebWindowResponse.class);
					if(jumpToWebWindowResponse.getCode() == 1){
						if(jumpToWebWindowResponse.getType().equals("H5")){
							String url = (String)jumpToWebWindowResponse.data;
							Intent intent = new Intent(context, WebControlerActivity.class);
							Bundle bundle = new Bundle();
							bundle.putSerializable("webUrl", url);
							intent.putExtras(bundle);
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							context.startActivity(intent);
						}
					}
				} catch (Exception e) {
					Toast.makeText(context, (String)msg.obj, Toast.LENGTH_SHORT).show();
				}
				
				break;
				
			case 7://获取Uname相关的额accesstoken
				AccessTokenResponse uRequest = gson.fromJson((String)msg.obj, AccessTokenResponse.class);
				if(uRequest.getCode() == 1){
					String deviceId = SDKUtil.mac;
					String osName = android.os.Build.MODEL;
					String osPlatform = "ANDROID";
					String osVersion = android.os.Build.VERSION.RELEASE;
					String screenSize = CommonUtil.getWindowSize(context)[1] + "x" + CommonUtil.getWindowSize(context)[0];
					String deviceType = "3";
					ApiClient.updateDeviceInfo(SDKUtil.UpdateDeviceInfoUrl, authHandler, 6, deviceId, osName, osPlatform, osVersion, screenSize, deviceType);
					
					accessToken = uRequest.getAccess_token();
					refreshToken = uRequest.getRefreshtoken();
					
					Editor editor = mSP.edit();
					editor.putString("accessToken", accessToken);
					editor.putString("refreshToken", refreshToken);
					editor.putString("phone_num", phone_num);
					editor.putString("staff_code", staff_code);
					editor.commit();
					
					JsonObject jo = new JsonObject();
					jo.addProperty("code", uRequest.getCode());
					jo.addProperty("msg", uRequest.getMsg());
					
					AuthInfoResult authInfoResult = new AuthInfoResult();
					authInfoResult.setPhone(phone_num);
					authInfoResult.setStaffcode(staff_code);
					authInfoResult.setStaffname(user_name);
					authInfoResult.setAreacode(area_code);
					
					String authInfo = gson.toJson(authInfoResult);
					jo.addProperty("authInfoResult", authInfo);
					jo.addProperty("expires_in", uRequest.getExpires_in());
					
					callback.onAccessTokenCallback(jo.toString());
				}else{
					callback.onAccessTokenCallback((String)msg.obj);
				}
				break;
			case 0:
				String errMsg = (String) msg.obj;
				callback.onError(errMsg);
				break;
			default:
				break;
			}
		}
	}
}
