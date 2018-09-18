package com.asiainfo.appframe.webutil.plugins;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;

import com.asiainfo.appframe.bean.JumpToWebWindowResponse;
import com.asiainfo.appframe.net.ApiClient;
import com.asiainfo.appframe.utils.SDKUtil;
import com.asiainfo.appframe.webutil.BasePlugin;
import com.asiainfo.appframe.webutil.IPlugin;
import com.asiainfo.appframe.webutil.PluginManager;
import com.google.gson.Gson;

public class AbilityPlugin extends BasePlugin {

	private String success = "";
	private String fail = "";
	
	public AbilityPlugin(IPlugin ecInterface, PluginManager pm) {
		super(ecInterface, pm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(String action, JSONArray args) {
		// TODO Auto-generated method stub
		success = args.optString(0);
		fail = args.optString(1);
		if("initial".equals(action)){
			String alias = args.optString(2);
			String signature = args.optString(3);
			String userParam = args.optString(4);
			initial(alias, signature, userParam);
		}
	}
	
	private void initial(String alias, String signature, String userParam){
		URLDecoder decoder = new URLDecoder();
		try {
			userParam = decoder.decode(userParam, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ApiClient.getAbilityInfo(SDKUtil.jumpToWebUrl + "?access_token=" + SDKUtil.accessToken, handler, 1, alias, signature, userParam);
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
			switch (msg.what) {
			case 1:	//调用能力完成的回调
				
				JSONObject resultObj = new JSONObject();
				try {
					JSONObject json = new JSONObject((String)msg.obj);
					resultObj.put("resultCode", 1);
					resultObj.put("resultMsg", "成功");
					resultObj.put("object", json);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				callback(resultObj.toString(), success);
//				
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
	}

}
