package com.asiainfo.appframe.webutil.plugins;

import java.util.Locale;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.asiainfo.appframe.CommonApplication;
import com.asiainfo.appframe.receiver.BatteryInfoReceiver;
import com.asiainfo.appframe.utils.CommonUtil;
import com.asiainfo.appframe.webutil.BasePlugin;
import com.asiainfo.appframe.webutil.IPlugin;
import com.asiainfo.appframe.webutil.PluginManager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.View;
import android.webkit.WebView;

/**
 * 获取设备信息插件
 * @author Stiven
 *
 */
public class GetDevicePlugin extends BasePlugin {

	public JSONObject deviceInfo = new JSONObject();
	
	/**
     * 设备信息获取插件构造
     * 
     * @param ecInterface
     * @param webView
     * @param pm
     */
    public GetDevicePlugin(IPlugin ecInterface,PluginManager pm) {
        super(ecInterface, pm);
    }

	@Override
	public void execute(String action, JSONArray args) {
		String success = args.optString(0);
        String fail = args.optString(1);

		if ("GetDeviceInfo".equals(action)) {//获取设备信息
			getDeviceInfo(success, fail);
        }else if("GetSystemInfo".equals(action)){//获取系统信息
        	getSystemInfo(success, fail);
        }else if("GetNetworkInfo".equals(action)){//获取网络信息
        	getNetworkInfo(success, fail);
        }
		
	}
	
	@Override
	public void execute(String action, JSONArray args, String type) {
		String success = args.optString(0);
        String fail = args.optString(1);

		if ("GetDeviceInfo".equals(action)) {//获取设备信息
			getDeviceInfo(success, fail);
        }else if("GetSystemInfo".equals(action)){//获取系统信息
        	getSystemInfo(success, fail);
        }else if("GetNetworkInfo".equals(action)){//获取网络信息
        	getNetworkInfo(success, fail);
        }
		
	}
	
	/**
	 * 获取
	 * @param success
	 * @param fail
	 */
	private void getSystemInfo(String success, String fail){
		TelephonyManager tm = (TelephonyManager) mPluginManager.getContext().getSystemService(Context.TELEPHONY_SERVICE);
		JSONObject object = new JSONObject();
		
		try {
			object.put("name", android.os.Build.MANUFACTURER);
			object.put("version", android.os.Build.VERSION.RELEASE);
			object.put("language",Locale.getDefault().getLanguage());
			object.put("vendor", android.os.Build.BRAND);
			object.put("network", tm.getNetworkType());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/////////////////////////////////////////////
		JSONObject resultObj = new JSONObject();
		try {
			resultObj.put("resultCode", 1);
			resultObj.put("resultMsg", "成功");
			resultObj.put("object", object);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		callback(resultObj.toString(), success);
	}
	
	/**
	 * 获取网络信息
	 * @param success
	 * @param fail
	 */
	private void getNetworkInfo(String success, String fail){
		TelephonyManager tm = (TelephonyManager) mPluginManager.getContext().getSystemService(Context.TELEPHONY_SERVICE);
		JSONObject object = new JSONObject();
		try {
			object.put("network", tm.getNetworkType());
			object.put("bluetoothInfo", getBluetoothInfo());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/////////////////////////////////////////////
		JSONObject resultObj = new JSONObject();
		try {
			resultObj.put("resultCode", 1);
			resultObj.put("resultMsg", "成功");
			resultObj.put("object", object);
		} catch (JSONException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		}
		callback(resultObj.toString(), success);
	}
	
	@Override
	public void callback(String result, String type) {
		mPluginManager.callBack(result, type);
	}
	
	private void getDeviceInfo(String success, String fail){
		TelephonyManager tm = (TelephonyManager) mPluginManager.getContext().getSystemService(Context.TELEPHONY_SERVICE);
		 
	    final String tmDevice, tmSerial, tmPhone, androidId;
	    tmDevice = "" + tm.getDeviceId();
	    tmSerial = "" + tm.getSimSerialNumber();
	    androidId = "" + android.provider.Settings.Secure.getString(mPluginManager.getContext().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
	 
	    UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
	    String uniqueId = deviceUuid.toString();
		
		JSONObject object = new JSONObject();
		try {
			object.put("model", android.os.Build.MODEL);//设备型号
			object.put("vendor", android.os.Build.BRAND);//设备厂商
			object.put("IMEI", tm.getDeviceId());
			object.put("UUID", uniqueId);
			object.put("IMSI", tm.getLine1Number());
			object.put("resolution", CommonUtil.getWindowSize(mPluginManager.getContext())[1] + "x" + CommonUtil.getWindowSize(mPluginManager.getContext())[0] );
			object.put("DPI", CommonUtil.getWindowDensity(mPluginManager.getContext())[0]);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		deviceInfo = object;
		getBatteryInfo(success, fail);
	}
	
	BatteryInfoReceiver receiver = null;
	/**
     * 获取电池信息
     */
	private void getBatteryInfo(String success, String fail){
		if(receiver != null){
			(CommonApplication.getInstance()).unregisterReceiver(receiver);
			receiver = null;
		}
    	receiver = new BatteryInfoReceiver(this, success, fail, deviceInfo);
    	IntentFilter filter = new IntentFilter();
    	filter.addAction(Intent.ACTION_BATTERY_CHANGED);
    	(mPluginManager.getContext()).registerReceiver(receiver, filter);
	}
	
	/**
	 * 停止电池状态的监听
	 */
	public void stopReceiver(){
		if(receiver != null){
			(mPluginManager.getContext()).unregisterReceiver(receiver);
			receiver = null;
		}
	}

	private JSONArray getBluetoothInfo(){
		int isBlueCon = 0;
		String message = "";
		BluetoothAdapter ba;                   //蓝牙适配器  
		ba = BluetoothAdapter.getDefaultAdapter();   
		  
		//蓝牙适配器是否存在，即是否发生了错误  
		if (ba == null){
		   isBlueCon = -1;     //error 
		   message = "该设备不支持蓝牙";
		}else if(ba.isEnabled()){
		   int a2dp = ba.getProfileConnectionState(BluetoothProfile.A2DP);              //可操控蓝牙设备，如带播放暂停功能的蓝牙耳机  
		   int headset = ba.getProfileConnectionState(BluetoothProfile.HEADSET);        //蓝牙头戴式耳机，支持语音输入输出  
		   int health = ba.getProfileConnectionState(BluetoothProfile.HEALTH);          //蓝牙穿戴式设备  
		  
		   //查看是否蓝牙是否连接到三种设备的一种，以此来判断是否处于连接状态还是打开并没有连接的状态  
		   int flag = -1;  
		   if (a2dp == BluetoothProfile.STATE_CONNECTED) {  
		      flag = a2dp;
		   } else if (headset == BluetoothProfile.STATE_CONNECTED) {  
		      flag = headset; 
		   } else if (health == BluetoothProfile.STATE_CONNECTED) {  
		      flag = health; 
		   }  
		   //说明连接上了三种设备的一种  
		   if (flag != -1){  
		      isBlueCon = 1;            //discontinued  
		      message = "蓝牙已连接";
		   }else if (flag == -1){  
			  ConnectivityManager cm = (ConnectivityManager) CommonApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);//获取系统的连接服务
		      @SuppressWarnings("deprecation")
			NetworkInfo netInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH);  
		  
		      if (netInfo == null) {  
		         isBlueCon = 1;     //discontinued  
		      } else {  
		    	  //public enum State {CONNECTING, CONNECTED, SUSPENDED, DISCONNECTING, DISCONNECTED, UNKNOWN}
		         State blt  = netInfo.getState();
		         switch (blt) {
					case CONNECTING:
						isBlueCon = BluetoothProfile.STATE_CONNECTING;
						message = "蓝牙连接中";
						break;
					case CONNECTED:
						isBlueCon = BluetoothProfile.STATE_CONNECTED;
						message = "蓝牙已连接";
						break;
					case SUSPENDED:
						isBlueCon = BluetoothProfile.STATE_CONNECTING;
						message = "蓝牙已连接";
						break;
					case DISCONNECTING:
						isBlueCon = BluetoothProfile.STATE_DISCONNECTING;
						message = "蓝牙未连接";
						break;
					case DISCONNECTED:
						isBlueCon = BluetoothProfile.STATE_DISCONNECTED;
						message = "蓝牙未连接";
						break;
					case UNKNOWN:
						isBlueCon = -1;
						break;
	
					default:
						break;
					}
//		         isBlueCon = BluetoothProfile.STATE_CONNECTED;         //系统内部，返回连接与否  
		      }  
		   }  
		} else{
			isBlueCon = -1;     //蓝牙不存在或者蓝牙未开启
			message = "蓝牙未开启";
		}
		
		JSONArray newArray = new JSONArray();
        JSONObject object = new JSONObject();
        try {
			object.put("btState", isBlueCon);
			object.put("message", message);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        newArray.put(object);
        
        return newArray;
        
	}
}
