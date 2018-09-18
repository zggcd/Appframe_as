package com.asiainfo.appframe.webutil.plugins;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.asiainfo.appframe.webutil.BasePlugin;
import com.asiainfo.appframe.webutil.IPlugin;
import com.asiainfo.appframe.webutil.ItemOverViewControler;
import com.asiainfo.appframe.webutil.PluginManager;

public class MultipleWindowPlugin extends BasePlugin {

	String success = "";
	String fail = "";
	
	public MultipleWindowPlugin(IPlugin ecInterface, PluginManager pm) {
		super(ecInterface, pm);
	}

	@Override
	public void execute(String action, JSONArray args) {
		success = args.optString(0);
        fail = args.optString(1);
		if("newWindow".equals(action)){//开启行窗口
			int type = args.optInt(2);
			String url = args.optString(3);
			openNewWebView(type, url);//0:关联，1：不关联
		}else if("setWindowSession".equals(action)){//塞值
			String content = args.optString(2);
			setWindowSession(content);
		}else if("getWindowSession".equals(action)){//取值
			String windowId = args.optString(2);
			getWindowSession(windowId);
		}
	}
	
	@Override
	public void execute(String action, JSONArray args, String typeStr) {
		success = args.optString(0);
        fail = args.optString(1);
        String param = "";
        try {
        	if("newWindow".equals(action)){//开启行窗口
        		param = URLDecoder.decode(args.optString(2), "UTF-8");
    			JSONObject jsonObject = new JSONObject(param);
    			int type = jsonObject.getInt("type");
    			String url = jsonObject.getString("url");
    			
    			openNewWebView(type, url);//0:关联，1：不关联
    		}else if("setWindowSession".equals(action)){//塞值
    			param = URLDecoder.decode(args.optString(2), "UTF-8");
    			JSONObject jsonObject = new JSONObject(param);
    			String content = jsonObject.getString("content");
    			
    			setWindowSession(content);
    		}else if("getWindowSession".equals(action)){//取值
    			param = URLDecoder.decode(args.optString(2), "UTF-8");
    			JSONObject jsonObject = new JSONObject(param);
    			String windowId = jsonObject.getString("windowId");
    			
    			getWindowSession(windowId);
    		}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private ItemOverViewControler item;
	
	private void openNewWebView(int type, String url){
		item = mECInterface.addNewWebView(type, url);
		
		if(item == null){
			
			JSONObject resultObj = new JSONObject();
			try {
				resultObj.put("resultCode", 0);
				resultObj.put("resultMsg", "新窗口开启失败，窗体总数达到十个");
			} catch (JSONException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            callback(resultObj.toString(), fail);
			
		}else{
		
	        JSONObject obj = new JSONObject();
	        try {
				obj.put("windowId", item.windowID);			
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JSONObject resultObj = new JSONObject();
			try {
				resultObj.put("resultCode", 1);
				resultObj.put("resultMsg", "成功");
				resultObj.put("object", obj);
			} catch (JSONException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        callback(resultObj.toString(), success);
		}
	}
	
	private void setWindowSession(String content){
		boolean isSUccess = mECInterface.setWindowSession(content);
		if(isSUccess){
			JSONObject resultObj = new JSONObject();
			try {
				resultObj.put("resultCode", 1);
				resultObj.put("resultMsg", "成功");
			} catch (JSONException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        callback(resultObj.toString(), success);
		}else{
			JSONObject resultObj = new JSONObject();
			try {
				resultObj.put("resultCode", 0);
				resultObj.put("resultMsg", "失败");
			} catch (JSONException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            callback(resultObj.toString(), fail);
		}
	}
	
	private void getWindowSession(String windowID){
		String content = mECInterface.getWindowSession(windowID);
		if(content != null){
			JSONObject obj = new JSONObject();
	        try {
				obj.put("content", content);			
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JSONObject resultObj = new JSONObject();
			try {
				resultObj.put("resultCode", 1);
				resultObj.put("resultMsg", "成功");
				resultObj.put("object", obj);
			} catch (JSONException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        callback(resultObj.toString(), success);
		}else{
			JSONObject resultObj = new JSONObject();
			try {
				resultObj.put("resultCode", 0);
				resultObj.put("resultMsg", "取值失败");
			} catch (JSONException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            callback(resultObj.toString(), fail);
		}
		
	}

	@Override
	public void callback(String result, String type) {
		// TODO Auto-generated method stub
		mPluginManager.callBack(result, type);
	}

}
