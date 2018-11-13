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
	public static String ResetPwdAuthUrl;				//获取重置密码验证码
	public static String ModifyPwdAuthUrl;				//修改密码url
	public static String ResetPwdUrl;				//重置密码
	public static String GetAPPFrontUI;				//获取首页布局信息    新
	public static String AreaCodeUrl;					//获取地区码
	public static String AccessTokenUrl;				//获取accesstoken，登录
	public static String refreshAccessTokenUrl;		//刷新accesstoken
	public static String UAccessTokenUrl;				//获取Uname相关的accesstoken
	public static String UploadImageUrl;				//图片上传接口
	public static String jumpToWebUrl;					//跳转web页
	public static String GetAutoPwdUrl;					//获取随机密码
	public static String QrcodeScan;					//扫描二维码
	public static String QrcodeLogin;					//扫码登录确认
	public static String QrcodeCancel;					//取消扫码登录
	public static String sessionUrl;					//会话接口
	public static String creatSessionUrl;				//创建会话接口
	public static String queryVersionUpdateInfoUrl;				//检测更新接口

	//nomal url
	private static String UpdateDeviceInfoUrl;
	public static String RecordH5Invoke;

	public static String GetUiLayout = "http://61.160.128.138:9512/gateway/app/ui/home";
	public static String GetAccessToken = "http://61.160.128.138:9512/gateway/auth/getAccessToken";
	public static String RefreshAccessToken = "http://61.160.128.138:9512/gateway/auth/updateAccessToken";
	public static String PreUiLayout = "http://61.160.128.138:9512/gateway/app/ui/pre";
	public static String GetAreaCode = "http://61.160.128.138:9512/gateway/auth/getAreaCode";
	public static String GetValidateCode = "http://61.160.128.138:9512/gateway/auth/getAuthCode";
	public static String GetPostNum = "http://61.160.128.138:9512/gateway/app/invoke";
	public static String GetTeamKey = "http://61.160.128.138:9512/gateway/app/getTeamKey";

	public static String accessToken = "";
	public static String refreshToken = "";
	public static String authInfoResult = "";//用户登录返回信息
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
	public static String packageVersion;//版本号
	public static String packageVersionName;//版本名称

	private static AddPermission addPermission;

	public static SharedPreferences mSP;
	private AuthHandler authHandler = null;
	//登录信息回调
	private SDKAuthCallBack callback;
	private SDKResetPwdCallback resetPwdCallback = null;
	private SDKModifyPwdCallback modifyPwdCallback = null;
	private SDKRandomPasswordCallback randomPasswordCallback = null;
	private SDKQrcodeCallback qrcodeCallback = null;

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
		this.packageVersion = CommonUtil.getLocalVersion(context).versionCode + "";
		this.packageVersionName = CommonUtil.getLocalVersion(context).versionName + "";
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
	 * 设置重置密码回调
	 * @param callback
	 */
	public void setSDKResetPwdCallback(SDKResetPwdCallback callback){
		this.resetPwdCallback = callback;
	}

	/**
	 * 设置修改密码回调
	 * @param modifyPwdCallback
	 */
	public void setSDKModifyPwdCallback(SDKModifyPwdCallback modifyPwdCallback){
		this.modifyPwdCallback = modifyPwdCallback;
	}

	/**
	 * 获取随机密码
	 * @param callback
	 */
	public void setSDKRandomPasswordCallback(SDKRandomPasswordCallback callback){
		this.randomPasswordCallback = callback;
	}

	public void setSDKQrcodeCallback(SDKQrcodeCallback callback){
		this.qrcodeCallback = callback;
	}

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
		ResetPwdAuthUrl = PortUrl + "/gateway/auth/getAuthCode";//和登录获取验证码接口一样
		ModifyPwdAuthUrl = PortUrl + "/gateway/auth/modifyPwd";//修改密碼
		ResetPwdUrl = PortUrl + "/gateway/auth/resetPwd";//重置密碼接口
		GetAPPFrontUI = PortUrl + "/gateway/app/ui/getAppFrontUI";//获取首页布局信息   新
		AreaCodeUrl = PortUrl + "/gateway/auth/getAreaCode";
		AccessTokenUrl = PortUrl + "/gateway/auth/getAccessToken";
		UAccessTokenUrl = PortUrl + "/gateway/auth/getUAccessToken";
		refreshAccessTokenUrl = PortUrl + "/gateway/auth/updateAccessToken";
		jumpToWebUrl = PortUrl + "/gateway/app/invoke";
		UploadImageUrl = PortUrl + "/gateway/common/out/upload?";
		UpdateDeviceInfoUrl = PortUrl + "/gateway/auth/getDevice";
		RecordH5Invoke = PortUrl +"/gateway/common/out/recordH5Invoke";
		GetAutoPwdUrl = PortUrl +"/gateway/auth/getRandomPwd";
		QrcodeScan = PortUrl +"/gateway/auth/qrcodeScan";
		QrcodeLogin = PortUrl +"/gateway/auth/qrcodeLogin";
		QrcodeCancel = PortUrl +"/gateway/auth/qrcodeCancel";

		GetUiLayout = PortUrl + "/gateway/app/ui/home";
		GetAccessToken = PortUrl + "/gateway/auth/getAccessToken";
		RefreshAccessToken = PortUrl + "/gateway/auth/updateAccessToken";
		PreUiLayout = PortUrl + "/gateway/app/ui/pre";
		GetAreaCode = PortUrl + "/gateway/auth/getAreaCode";
		GetValidateCode = PortUrl + "/gateway/auth/getAuthCode";
		GetPostNum = PortUrl + "/gateway/app/invoke";
		GetTeamKey = PortUrl + "/gateway/app/getTeamKey";

		creatSessionUrl = "https://ztyx.telecomjs.com/gateway/createSession";
		sessionUrl = "https://ztyx.telecomjs.com/gateway/bizReq";
		queryVersionUpdateInfoUrl = "http://ztyx.telecomjs.com/ecs_appmanager/service/http/queryVersionUpdateInfo.do?";

		authHandler = new AuthHandler();
		mSP = context.getSharedPreferences("APPFRAME_SDK", Context.MODE_PRIVATE);
	}
	
	/**
	 * 初始化用户数据
	 */
	private void initData(){
		accessToken = mSP.getString("accessToken", "");
		refreshToken = mSP.getString("refreshToken", "");
		authInfoResult = mSP.getString("authInfoResult", "");
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
	 * @param account	手机号
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

	/**
	 * 获取重置密码验证码
	 * @param account
	 */
	public void getResetPwdAuthcode(String account){
		if (resetPwdCallback == null){
			return;
		}
		ApiClient.getValidateCode(ResetPwdAuthUrl, authHandler, 11, account.trim(), app_id);
	}

	/**
	 * 重置密码
	 * @param username	账号
	 * @param smscode   手机号
	 * @param nPassword 新密码
	 */
	public void resetPwd(String username,String smscode, String nPassword){
		if (resetPwdCallback == null){
			return;
		}
		ApiClient.resetPwd(ResetPwdUrl, authHandler, 12, username, smscode, nPassword);
	}

	/**
	 * 获取随机密码
	 */
	public void getAutoPwd(){
		ApiClient.getAutoPwd(GetAutoPwdUrl, authHandler, 14);
	}

	/**
	 * 扫码后调用接口
	 * @param accessToken
	 * @param qrCode
	 */
	public void qrcodeScan(String accessToken, String qrCode){
		ApiClient.qrcodeScan(QrcodeScan, authHandler, 15, accessToken, qrCode);
	}

	/**
	 * 扫码后确认登录
	 * @param accessToken
	 * @param qrCode
	 */
	public void qrcodeLogin(String accessToken, String qrCode){
		ApiClient.qrcodeScan(QrcodeLogin, authHandler, 16, accessToken, qrCode);
	}

	/**
	 * 扫码后取消登录
	 * @param accessToken
	 * @param qrCode
	 */
	public void qrcodeCancel(String accessToken, String qrCode){
		ApiClient.qrcodeScan(QrcodeCancel, authHandler, 17, accessToken, qrCode);
	}

	/**
	 * 修改密码
	 * @param accessToken	账号
	 * @param password   手机号
	 * @param nPassword 新密码
	 * @param First 根据getAccesstoken获取的staff_state,0填0，其他填1
	 */
	public void modifyPwd(String accessToken,String password, String nPassword, int First){
		if (modifyPwdCallback == null){
			return;
		}
		ApiClient.modifyPwd(ResetPwdUrl, authHandler, 13, accessToken, password, nPassword, First);
	}

	/**
	 * 获取首页布局信息
	 * http://61.160.128.138:9512/gateway/app/ui/getAppFrontUI
	 * @param accessToken
	 */
	public void getAppFrontUI(String accessToken){
		ApiClient.getAppFrontUI("http://61.160.128.138:9512/gateway/app/ui/getAppFrontUI", authHandler, 14, SDKUtil.accessToken);
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
					authInfoResult = authInfo;
					
					Editor editor = mSP.edit();
					editor.putString("accessToken", accessToken);
					editor.putString("refreshToken", refreshToken);
					editor.putString("authInfoResult", authInfoResult);
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
//					refreshToken = accessTokenResponse.getRefreshtoken();
					
					Editor editor = mSP.edit();
					editor.putString("accessToken", accessToken);
//					editor.putString("refreshToken", refreshToken);
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

			case 11:
				//获取验证码
				String resetPwdAuthCode = (String) msg.obj;
				if(resetPwdAuthCode != null){
					resetPwdCallback.onAuthCodeSuccess(resetPwdAuthCode);
				}else{
					resetPwdCallback.onAuthCodeSuccess(resetPwdAuthCode);
				}
				break;

			case 12://重置密碼
				String resultResetPwd = (String) msg.obj;
				if (resultResetPwd != null){
					resetPwdCallback.onResetPwdSuccess(resultResetPwd);
				}
				break;

			case 13://修改密码
				String resultModify = (String) msg.obj;
				if (resultModify != null){
					modifyPwdCallback.onModifyPwdCallback(resultModify);
				}
				break;
			case 14://获取随机密码
				String resultAutoPwd = (String) msg.obj;
				if (resultAutoPwd != null){
					randomPasswordCallback.onRandomPasswordSuccess(resultAutoPwd);
				}
				break;

			case 15://扫码登录
				String resultQrcode = (String) msg.obj;
				if (!StringUtil.isEmpty(resultQrcode)){
					qrcodeCallback.onQrcodeScanCallback(resultQrcode);
				}
				break;
			case 16://扫码登录确认
				String resultQrcodeLogin = (String) msg.obj;
				if (!StringUtil.isEmpty(resultQrcodeLogin)){
					qrcodeCallback.onQrcodeLoginCallback(resultQrcodeLogin);
				}
				break;
			case 17://扫码登录取消
				String resultQrcodeCancel= (String) msg.obj;
				if (!StringUtil.isEmpty(resultQrcodeCancel)){
					qrcodeCallback.onQrcodeCancelCallback(resultQrcodeCancel);
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

	public static void cleanUserInfo(){
		if (mSP != null){
			Editor editor = mSP.edit();
			editor.putString("accessToken", "");
			editor.putString("refreshToken", "");
			editor.putString("authInfoResult", "");
			editor.putString("phone_num", "");
			editor.putString("staff_id", "");
			editor.commit();
		}

	}
}
