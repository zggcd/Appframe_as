package com.asiainfo.appframe.webutil.plugins;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.webkit.WebView;
import android.widget.Toast;

import com.asiainfo.appframe.CommonApplication;
import com.asiainfo.appframe.utils.SDKUtil;
import com.asiainfo.appframe.utils.StringUtil;
import com.asiainfo.appframe.webutil.BasePlugin;
import com.asiainfo.appframe.webutil.IPlugin;
import com.asiainfo.appframe.webutil.PluginManager;

public class UserPlugin extends BasePlugin {

	public UserPlugin(IPlugin ecInterface, PluginManager pm) {
		super(ecInterface, pm);
		
	}

	@Override
	public void execute(String action, JSONArray args) {
		String success = args.optString(0);
        String fail = args.optString(1);

		if ("GetAccessToken".equals(action)) {//获取设备信息
			GetAccessToken(success, fail);
        }
		
	}
	
	@Override
	public void execute(String action, JSONArray args, String type) {
		String success = args.optString(0);
        String fail = args.optString(1);

		if ("GetAccessToken".equals(action)) {//获取设备信息
			GetAccessToken(success, fail);
        }
		
	}

	private void GetAccessToken(String success, String fail) {
		
        JSONArray newArray = new JSONArray();
        
//        if(StringUtil.isEmpty(CommonApplication.accessToken)){
//        	newArray.put(SDKUtil.accessToken);
//        }else{
//        	newArray.put(CommonApplication.accessToken);
//        }
//		
		JSONObject object = new JSONObject();
        try {
        	if(StringUtil.isEmpty(SDKUtil.accessToken)){
            	object.put("accessToken", SDKUtil.accessToken);
//            	Toast.makeText(mPluginManager.getContext(), "SDKUtil.accessToken = " + SDKUtil.accessToken, 2000).show();
            }else{
            	object.put("accessToken", SDKUtil.accessToken);
//            	Toast.makeText(mPluginManager.getContext(), "CommonApplication.accessToken = " + CommonApplication.accessToken, 2000).show();
            }
		} catch (JSONException e0) {
			e0.printStackTrace();
		}
        
        JSONObject resultObj = new JSONObject();
		try {
			resultObj.put("resultCode", 1);
			resultObj.put("resultMsg", "");
			resultObj.put("object", object);
		} catch (JSONException e0) {
			e0.printStackTrace();
		}
		callback(resultObj.toString(), success);
		
	}

	@Override
	public void callback(String result, String type) {
		mPluginManager.callBack(result, type);
	}

}
