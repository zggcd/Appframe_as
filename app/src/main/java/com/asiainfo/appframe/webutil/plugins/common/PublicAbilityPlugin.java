package com.asiainfo.appframe.webutil.plugins.common;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.asiainfo.appframe.bean.AccessTokenResponse;
import com.asiainfo.appframe.net.ApiClient;
import com.asiainfo.appframe.utils.SDKUtil;
import com.asiainfo.appframe.utils.StringUtil;
import com.asiainfo.appframe.webutil.BasePlugin;
import com.asiainfo.appframe.webutil.IPlugin;
import com.asiainfo.appframe.webutil.PluginManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class PublicAbilityPlugin extends BasePlugin {

	private String success = "";
	private String fail = "";
	private String alias = "";
	private String signature = "";
	private String userParam = "";
	
	private CommonPluginManager manager;
	
	public PublicAbilityPlugin(IPlugin ecInterface, PluginManager pm) {
		super(ecInterface, pm);
		manager = new CommonPluginManager((Context) ecInterface);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(String action, JSONArray args) {
		// TODO Auto-generated method stub
		success = args.optString(0);
		fail = args.optString(1);
		if("initial".equals(action)){
			alias = args.optString(2);
			signature = args.optString(3);
			userParam = args.optString(4);
			initial();
		}
	}
	
	/**
	 * 原生调用能力执行该方法
	 * @param alias
	 * @param signature
	 * @param userParam
	 */
	public void execute(String alias, String signature, String userParam){
		if( !StringUtil.isEmpty(alias) && !StringUtil.isEmpty(signature) && !StringUtil.isEmpty(userParam)){
			this.alias = alias;
			this.signature = signature;
			this.userParam = userParam;
			initialNew();
		}
	}
	
	private void initial(){
		URLDecoder decoder = new URLDecoder();
		try {
			userParam = decoder.decode(userParam, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		manager.execute(mPluginManager, success, fail, alias, signature, userParam);
		ApiClient.getAbilityInfo(SDKUtil.jumpToWebUrl + "?access_token=" + SDKUtil.accessToken, handler, 1, alias, signature, userParam);
	}
	
	/**
	 * 原声传递的userparam没有经过utf-8编码
	 */
	private void initialNew(){
		ApiClient.getAbilityInfo(SDKUtil.jumpToWebUrl + "?access_token=" + SDKUtil.accessToken, handler, 2, alias, signature, userParam);
	}
	
	private MyHandler handler = new MyHandler();
	
	private class MyHandler extends Handler{
		public MyHandler() {
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Gson gson = new Gson();
			switch (msg.what) {
			case 1:	//调用能力完成的回调
				try {
					JSONObject resultObj = new JSONObject();
					JSONObject json = new JSONObject((String)msg.obj);
//					Log.d("");
					int code = json.getInt("code");
					if(code == 1){//成功返回，并且accesstoken有效
						switch (json.getString("type")) {
						case "JS_API":
							manager.execute(mPluginManager, success, fail, new JSONObject(json.getString("data")).getString("jsapiName"), signature, userParam);
							break;
						case "H5":
							resultObj.put("resultCode", 1);
							resultObj.put("resultMsg", "成功");
							resultObj.put("object", json.getString("data"));
							
							callback(resultObj.toString(), success);
							break;
						case "INTERFACE_REMOTE":
							resultObj.put("resultCode", 1);
							resultObj.put("resultMsg", "成功");
							resultObj.put("object", json);
							
							callback(resultObj.toString(), success);
							break;
						default:
							JSONObject resultFail = new JSONObject();
							try {
								resultFail.put("resultCode", 0);
								resultFail.put("resultMsg", "失败");
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							callback(resultFail.toString(), fail);
							break;
								
						}
					}else if(code == 1002 || code == 1003){//token过期  code是 1002     token无效是 1003
						//刷新token
						ApiClient.refreshAccessToken(SDKUtil.refreshAccessTokenUrl, handler, 3, 0, SDKUtil.phone_num, SDKUtil.mac, SDKUtil.refreshToken);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				
				break;
			case 2://原生调用能力完成的回调
				
				break;
				
			case 3://刷新accesstoken返回
				AccessTokenResponse accessTokenResponse = gson.fromJson((String)msg.obj, AccessTokenResponse.class);
				if(accessTokenResponse.getCode() == 1){//刷新成功
					
					SDKUtil.accessToken = accessTokenResponse.getAccess_token();
					SDKUtil.refreshToken = accessTokenResponse.getRefreshtoken();
					
					SharedPreferences sp = ((Context)mECInterface).getSharedPreferences("APPFRAME_SDK", Context.MODE_PRIVATE);
					Editor editor = sp.edit();
					editor.putString("accessToken", SDKUtil.accessToken);
					editor.commit();
					
					ApiClient.getAbilityInfo(SDKUtil.jumpToWebUrl + "?access_token=" + SDKUtil.accessToken, handler, 1, alias, signature, userParam);
					
				}else{//刷新失败，发送广播
					
					Intent intent = new Intent();
			        //设置广播的名字（设置Action）
			        intent.setAction("com.asiainfo.appframe.955");
			        intent.putExtra("data", (String)msg.obj);
			        ((Context)mECInterface).sendBroadcast(intent);
				}
				break;
				
			case 0:
				JSONObject resultFail = new JSONObject();
				try {
					resultFail.put("resultCode", 0);
					resultFail.put("resultMsg", "失败");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				callback(resultFail.toString(), fail);
				break;

			default:
				break;
			}
		}
	}

	@Override
	public void callback(String result, String type) {
		// TODO Auto-generated method stub
		mPluginManager.callBack(result, type);
		if(mPluginManager.getAndroidResult() != null){
			mECInterface.onAbilityResult(result);
		}
	}

}
