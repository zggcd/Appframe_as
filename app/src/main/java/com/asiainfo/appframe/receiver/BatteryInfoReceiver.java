package com.asiainfo.appframe.receiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.asiainfo.appframe.webutil.BasePlugin;
import com.asiainfo.appframe.webutil.plugins.GetDevicePlugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

public class BatteryInfoReceiver extends BroadcastReceiver {

	BasePlugin plugin;
	String success = "";
	String fail = "";
	int what;
	JSONObject deviceInfo;
	
	public BatteryInfoReceiver(BasePlugin plugin, String success, String fail, JSONObject object) {
		this.plugin = plugin;
		this.success = success;
		this.fail = fail;
		this.what = what;
		this.deviceInfo = object;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
        	
        	int status = intent.getIntExtra( "status" , 0 );  
        	int health = intent.getIntExtra( "health" , 1 );  
        	boolean present = intent.getBooleanExtra( "present" , false );  
        	int level = intent.getIntExtra( "level" , 0 );  
        	int scale = intent.getIntExtra( "scale" , 0 );  
        	int plugged = intent.getIntExtra( "plugged" , 0 );  
        	int voltage = intent.getIntExtra( "voltage" , 0 );  
        	int temperature = intent.getIntExtra( "temperature" , 0 ); // 温度的单位是10℃  
        	String technology = intent.getStringExtra( "technology" );
        	
        	JSONArray newArray = new JSONArray();
        	JSONObject object = new JSONObject();
            try {
    			object.put("status", status);
    			object.put("health", health);
    			object.put("present", present);
    			object.put("level", level);
    			object.put("scale", scale);
    			object.put("plugged", plugged);
    			object.put("voltage", voltage);
    			object.put("temperature", temperature);
    			object.put("technology", technology);
    		} catch (JSONException e) {
    			// TODO Auto-generated catch block
    			plugin.callback("获取电池信息失败", fail);
    			e.printStackTrace();
    		}
            newArray.put(object);
        	((GetDevicePlugin)plugin).stopReceiver();
        	
        	try {
				deviceInfo.put("batteryInfo", newArray);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	/////////////////////////////////////////////
        	JSONObject resultObj = new JSONObject();
    		try {
    			resultObj.put("resultCode", 1);
    			resultObj.put("resultMsg", "成功");
    			resultObj.put("object", deviceInfo);
    		} catch (JSONException e) {
    		// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        	
        	plugin.callback(resultObj.toString(), success);
//            JSUtil.execCallback(pWebview, CallBackID, newArray, JSUtil.OK, false);
            
        }
	}

}
