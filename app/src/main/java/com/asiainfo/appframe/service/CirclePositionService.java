package com.asiainfo.appframe.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.asiainfo.appframe.CommonApplication;
import com.asiainfo.appframe.bean.SimpleLocationInfo;
import com.asiainfo.appframe.utils.BaiduLocationManager;
import com.asiainfo.appframe.utils.LocationUtil;
import com.asiainfo.appframe.utils.BaiduLocationManager.LocationCallBack;
import com.asiainfo.appframe.webutil.plugins.GetLocationPlugin;
import com.baidu.location.BDLocation;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

/**
 * 循环定位
 * @author Stiven
 *
 */
public class CirclePositionService extends Service {
	
	private Context mContext;

	static CirclePositionService instance;
	
	//data
	public static List<SimpleLocationInfo> mList_info = new ArrayList<SimpleLocationInfo>();
	public static Map<String, List<SimpleLocationInfo>> mMap_list_info = new HashMap<String, List<SimpleLocationInfo>>();
	
	public static boolean isSurvival = false;//service是否存在
	public static String success = "";
	public static String fail = "";
	public static GetLocationPlugin mPlugin;
	public static String firstTime = "";		//每次轨迹记录开始的时间
	
	@SuppressLint("SimpleDateFormat")
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mContext = this;
		instance = this;
		startService(new Intent(this, CirclePositionEnableService.class));
		isSurvival = true;
		init();
	}
	
	public static void setPlugin(GetLocationPlugin plugin){
		mPlugin = plugin;
		System.out.println(mPlugin.toString());
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		success = intent.getStringExtra("success");
		fail = intent.getStringExtra("fail");
		return super.onStartCommand(intent, flags, startId);
		
	}
	private static BaiduLocationManager localManager;
	public void init(){
//		android.os.Debug.waitForDebugger();
		localManager = new BaiduLocationManager(
				mContext, new LocationCallBack() {
                    @Override
                    public void onLocationChange(BDLocation location) {
                    	
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        
                        long time = System.currentTimeMillis();
                        Date date = new Date(time);
                        SimpleLocationInfo info = new SimpleLocationInfo();
                        info.timestamp = format.format(date);
                        info.lat = latitude;
                        info.lon = longitude;
                        mList_info.add(info);
                        
                        if(mList_info.size() == 1){
                        	JSONObject obj = new JSONObject();
                            try {
    							obj.put("trailId", "trail_" + time);
    							firstTime = "trail_" + time;
    						} catch (JSONException e) {
    							// TODO Auto-generated catch block
    							mPlugin.callback("位置信息获取失败", fail);
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
//                            Toast.makeText(CirclePositionService.this, resultObj.toString(), Toast.LENGTH_SHORT).show();

                            mPlugin.callback(resultObj.toString(), success);
                        }
                        
                        if(mList_info.size() >= 15){
                        	mMap_list_info.put(firstTime, mList_info);
                        	mList_info.clear();
                        	firstTime = "";
                        	localManager.closeLocationTask();
                        }
                        
//                        Toast.makeText(CirclePositionService.this, "定位成功: " + latitude + " | " + longitude + " | " + mList_info.size(), Toast.LENGTH_SHORT).show();
                    }

					@Override
					public void onLocationFail(String msg) {
						// TODO Auto-generated method stub
						JSONObject resultObj = new JSONObject();
        				try {
        					resultObj.put("resultCode", 0);
        					resultObj.put("resultMsg", "失败");
        				} catch (JSONException e) {
        				// TODO Auto-generated catch block
        					e.printStackTrace();
        				}
//                        Toast.makeText(CirclePositionService.this, resultObj.toString(), Toast.LENGTH_SHORT).show();

                        mPlugin.callback(resultObj.toString(), fail);
					}
                });
        localManager.openLocationTask();
	}
	
	/**
	 * 获取再次开启轨迹记录的信息
	 * @return
	 */
	public static String isGetTrailInfo(){
		String result = "";
		if(mList_info.size() > 0){
            result = "正在记录轨迹，请稍后...";
		}else{
			localManager.openLocationTask();
		}
		
		return result;
	}
	
	/**
	 * 获取轨迹记录
	 * @param trailId
	 * @return
	 */
	public static List<SimpleLocationInfo> getTrail(String trailId, int coortype){
		
//		List<SimpleLocationInfo> result = new ArrayList<SimpleLocationInfo>();
		if(isSurvival){
			if(mList_info.size() > 0){
				if(firstTime.equals(trailId)){
					localManager.closeLocationTask();
					List<SimpleLocationInfo> list_info = new ArrayList<SimpleLocationInfo>();
					list_info.addAll(mList_info);
					mMap_list_info.put(firstTime, list_info);
					mList_info.clear();
					firstTime = "";
					return list_info;
				}else{
					if(mMap_list_info.size() > 0){
						return mMap_list_info.get(trailId);
					}else{
						return null;
					}
				}
			}else{
				if(mMap_list_info.size() > 0){
					return mMap_list_info.get(trailId);
				}else{
					return null;
				}
			}
		}
		return null;
		
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		/**
		 * RPC:远程调用
		 */
		return new MyBinder();
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		instance = null;
		isSurvival = false;
	}
	
	public class MyBinder extends Binder{
		
	}

}
