package com.asiainfo.appframe.net;

import java.util.Date;
import java.util.Map;

import com.asiainfo.appframe.net.logic.AbilityRequest;
import com.asiainfo.appframe.net.logic.AppFrontUiRequest;
import com.asiainfo.appframe.net.logic.CheckUpgradeRequest;
import com.asiainfo.appframe.net.logic.GetAccessTokenRequest;
import com.asiainfo.appframe.net.logic.GetAreaCodeRequest;
import com.asiainfo.appframe.net.logic.GetAutlPwdRequest;
import com.asiainfo.appframe.net.logic.GetClickUrlRequest;
import com.asiainfo.appframe.net.logic.GetPostNumRequest;
import com.asiainfo.appframe.net.logic.GetTeamKeyRequest;
import com.asiainfo.appframe.net.logic.GetUAccessTokenRequest;
import com.asiainfo.appframe.net.logic.GetUiInfo;
import com.asiainfo.appframe.net.logic.GetValidateCodeRequest;
import com.asiainfo.appframe.net.logic.JumpToWebWindowRequest;
import com.asiainfo.appframe.net.logic.ModifyPwdRequest;
import com.asiainfo.appframe.net.logic.QrcodeScanRequest;
import com.asiainfo.appframe.net.logic.RecordH5InvokeRequest;
import com.asiainfo.appframe.net.logic.RefreshAccessTokenRequest;
import com.asiainfo.appframe.net.logic.ResetPwdRequest;
import com.asiainfo.appframe.net.logic.UploadDeviceInfoRequest;
import com.asiainfo.appframe.net.logic.msgpush.MsgPushAuthRequest;
import com.asiainfo.appframe.utils.HttpUtil;
import com.asiainfo.appframe.webutil.plugins.AbilityPlugin;

import android.os.Handler;
import android.widget.TextView;

/**
 * 接口调用逻辑
 * 
 * @author Stiven
 *
 */
public class ApiClient {

	/**
	 * 关闭现有的请求
	 */
	public static void stop(){
		HttpUtil.cancelAllRequests(true);
	};
	
	/**
	 * 获取首页配置信息
	 * @param handler
	 * @param what
	 * @param app_id
	 * @param appsecret
	 */
	public static void getUiInfo(Handler handler, int what, String app_id, String au_id, String appsecret){
		GetUiInfo getUiInfo = new GetUiInfo(handler, what, app_id, au_id, appsecret);
		getUiInfo.start();
	}
	
	/**
	 * 获取accesstoken
	 * @param handler
	 * @param what
	 * @param phone_num
	 * @param password
	 * @param app_id
	 * @param area_code
	 * @param mac
	 * @param staff_code
	 * @param app_secret
	 */
	public static void getAccessToken(Handler handler, int what, 
			String phone_num, String password, String basecode, String app_id, String area_code, String mac, String staff_code, String app_secret, String staff_id){
		GetAccessTokenRequest request = new GetAccessTokenRequest(handler, what, phone_num, password, basecode, app_id, area_code, mac, staff_code, app_secret, staff_id);
		request.start();
		
	}
	
	/**
	 * 获取accesstoken		指定URL
	 * @param handler
	 * @param what
	 * @param phone_num
	 * @param password
	 * @param app_id
	 * @param area_code
	 * @param mac
	 * @param staff_code
	 * @param app_secret
	 */
	public static void getAccessToken(String url, Handler handler, int what, 
			String phone_num, String password, String basecode, String app_id, String area_code, String mac, String staff_code, String app_secret, String staff_id){
		GetAccessTokenRequest request = new GetAccessTokenRequest(url, handler, what, phone_num, password, basecode, app_id, area_code, mac, staff_code, app_secret, staff_id);
		request.start();
	}
	
	/**
	 * 获取accesstoken		指定URL
	 * @param handler
	 * @param what
	 * @param phone_num
	 * @param password
	 * @param app_id
	 * @param area_code
	 * @param mac
	 * @param staff_code
	 * @param app_secret
	 */
	public static void getAccessToken(String url, Handler handler, int what, 
			String phone_num, String password, String basecode, String app_id, String area_code, String mac, String staff_code, String app_secret){
		GetAccessTokenRequest request = new GetAccessTokenRequest(url, handler, what, phone_num, password, basecode, app_id, area_code, mac, staff_code, app_secret, "");
		request.start();
	}
	
	/**
	 * 获取uname相关的accesstoken		指定URL
	 * @param handler
	 * @param what
	 */
	public static void getUAccessToken(String url, Handler handler, int what, 
			String phone_num, String staff_code, String user_name, String area_code, String app_id, String mac){
		GetUAccessTokenRequest request = new GetUAccessTokenRequest(url, handler, what, phone_num, staff_code, user_name, area_code, app_id, mac);
		request.start();
	}
	
	/**
	 * 更新accessToken
	 * @param handler
	 * @param what
	 * @param location
	 * @param phone_num
	 * @param mac
	 * @param refresh_token
	 * @param staff_code
	 */
	public static void refreshAccessToken(Handler handler, int what, int location,
			String phone_num, String mac, String refresh_token, String staff_code){
		RefreshAccessTokenRequest request = new RefreshAccessTokenRequest(handler, what, location, phone_num, mac,refresh_token);
		request.start();
		
	}
	
	/**
	 * 更新accessToken		指定url
	 * @param handler
	 * @param what
	 * @param location
	 * @param phone_num
	 * @param mac
	 * @param refresh_token
	 * @param staff_code
	 */
	public static void refreshAccessToken(String url, Handler handler, int what, int location,
			String phone_num, String mac, String refresh_token){
		RefreshAccessTokenRequest request = new RefreshAccessTokenRequest(url, handler, what, location, phone_num, mac,refresh_token);
		request.start();
		
	}
	
	/**
	 * 加载页、登录页图片信息
	 * @param handler
	 * @param what
	 * @param app_id
	 * @param appsecret
	 */
	public static void preUiLayout(Handler handler, int what, String app_id){
		com.asiainfo.appframe.net.logic.preUiLayout resquest = new com.asiainfo.appframe.net.logic.preUiLayout(handler, what, app_id);
		resquest.start();
	}
	
	/**
	 * 获取用户地区码
	 * @param handler
	 * @param what
	 * @param username
	 */
	public static void getAreaCode(Handler handler, int what, String username){
		GetAreaCodeRequest resquest = new GetAreaCodeRequest(handler, what, username);
		resquest.start();
	}
	
	/**
	 * 获取用户地区码		指定URL
	 * @param handler
	 * @param what
	 * @param username
	 */
	public static void getAreaCode(String url, Handler handler, int what, String username){
		GetAreaCodeRequest resquest = new GetAreaCodeRequest(url, handler, what, username);
		resquest.start();
	}
	
	/**
	 * 获取验证码
	 * @param handler
	 * @param what
	 * @param phone_num
	 */
	public static void getValidateCode(Handler handler, int what, String phone_num, String app_id){
		GetValidateCodeRequest resquest = new GetValidateCodeRequest(handler, what, phone_num, app_id);
		resquest.start();
	}
	
	/**
	 * 获取验证码		指定url
	 * @param handler
	 * @param what
	 * @param phone_num
	 */
	public static void getValidateCode(String url, Handler handler, int what, String phone_num, String app_id){
		GetValidateCodeRequest resquest = new GetValidateCodeRequest(url, handler, what, phone_num, app_id);
		resquest.start();
	}

	/**
	 * 重置密码
	 * @param handler
	 * @param what
	 * @param nPassword
	 */
	public static void resetPwd(String url, Handler handler, int what, String username, String smscode, String nPassword){
		ResetPwdRequest resquest = new ResetPwdRequest(url, handler, what, username, smscode, nPassword);
		resquest.start();
	}

	/**
	 * 生成随机密码
	 */
	public static void getAutoPwd(String url, Handler handler, int what){
		GetAutlPwdRequest resquest = new GetAutlPwdRequest(url, handler, what);
		resquest.start();
	}

	/**
	 * 扫码登录判断
	 */
	public static void qrcodeScan(String url, Handler handler, int what, String accessToken, String qrCode){
		QrcodeScanRequest resquest = new QrcodeScanRequest(url, handler, what, accessToken, qrCode);
		resquest.start();
	}

	/**
	 * 修改密码
	 * @param url
	 * @param handler
	 * @param what
	 * @param accesstoken
	 * @param password
	 * @param nPassword
	 * @param First
	 */
	public static void modifyPwd(String url, Handler handler, int what, String accesstoken, String password, String nPassword, int First){
		ModifyPwdRequest resqest = new ModifyPwdRequest(url, handler, what, accesstoken, password, nPassword, First);
		resqest.start();
	}

	/**
	 * 获取首页信息  新版
	 * @param url
	 * @param handler
	 * @param what
	 * @param accesstoken
	 */
	public static void getAppFrontUI(String url, Handler handler, int what, String accesstoken){
		AppFrontUiRequest request = new AppFrontUiRequest(url, handler, what, accesstoken);
		request.start();
	}


	/**
	 * 获取验证码		指定url
	 * @param handler
	 * @param what
	 * @param phone_num
	 */
	public static void getResetPwdAuthCode(String url, Handler handler, int what, String phone_num, String app_id){
		GetValidateCodeRequest resquest = new GetValidateCodeRequest(url, handler, what, phone_num, app_id);
		resquest.start();
	}
	
	/**
	 * 获取验证码
	 * @param handler
	 * @param what
	 * @param phone_num
	 */
	public static void getPostNum(Handler handler, int what, String abilityalias, TextView tv){
		GetPostNumRequest resquest = new GetPostNumRequest(handler, what, abilityalias, tv);
		resquest.start();
	}
	
	/**
	 * 获取跳转URL			和获取验证码同接口
	 * @param handler
	 * @param what
	 * @param phone_num
	 */
	public static void getClickUrl(Handler handler, int what, String abilityalias){
		GetClickUrlRequest resquest = new GetClickUrlRequest(handler, what, abilityalias);
		resquest.start();
	}
	
	/**
	 * 获取teamkey
	 * @param handler
	 * @param what
	 * @param secret
	 */
	public static void getTeamKey(Handler handler, int what, String secret){
		GetTeamKeyRequest request = new GetTeamKeyRequest(handler, what, secret);
		request.start();
	}
	
	public static void jumpToWebWindow(String url, Handler handler, int what, String storeName, String signature, Map<String, Object> param){
		JumpToWebWindowRequest request = new JumpToWebWindowRequest(url, handler, what, storeName, signature, param);
		request.start();
	}
	
	/**
	 * 获取能力信息返回给前端
	 * @param url
	 * @param handler
	 * @param what
	 * @param abilityalias
	 * @param signature
	 * @param param
	 */
	public static void getAbilityInfo(String url, Handler handler, int what, String abilityalias, String signature, String param){
		AbilityRequest request = new AbilityRequest(url, handler, what, abilityalias, signature, param);
		request.start();
	}
	
	public static void updateDeviceInfo(String url, Handler handler, int what, String deviceId, String osName, String osPlatform, String osVersion, String screenSize, String deviceType){
		UploadDeviceInfoRequest request = new UploadDeviceInfoRequest(url, handler, what, deviceId, osName, osPlatform, osVersion, screenSize, deviceType);
		request.start();
	}
	
	public static void recordH5Invoke(String url, Handler handler, int what, String requestUrl, Date requestTime, Date responseTime, String responseText, String responseStatus, String app_id){
		RecordH5InvokeRequest request = new RecordH5InvokeRequest(url, handler, what, requestUrl, requestTime, responseTime, responseText, responseStatus, app_id);
		request.start();
	}
	
	public static void getMsgPushAuthRequest(String url, Handler handler, int what, String version, String staff_id, String app_code, String area_code, int timestamp, String ip, String signature ){
		MsgPushAuthRequest request= new MsgPushAuthRequest(url, handler, what, version, staff_id, app_code, area_code, timestamp, ip, signature);
		request.start();
	}

	/**
	 * 检测版本更新接口
	 * @param handler
	 * @param what
	 */
	public static void UpgradeVersion(Handler handler, int what, String curVersionNum){
		CheckUpgradeRequest request = new CheckUpgradeRequest(handler, what, curVersionNum);
		request.start();
	}
	
}
