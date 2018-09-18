package com.asiainfo.appframe.webutil.plugins;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.webkit.WebView;
import android.widget.Toast;

import com.asiainfo.appframe.activity.LocationChooseActivity;
import com.asiainfo.appframe.bean.SimpleLocationInfo;
import com.asiainfo.appframe.permission.AddPermission;
import com.asiainfo.appframe.service.CirclePositionService;
import com.asiainfo.appframe.service.CirclePositionService.MyBinder;
import com.asiainfo.appframe.utils.BaiduLocationManager;
import com.asiainfo.appframe.utils.LocationUtil;
import com.asiainfo.appframe.utils.BaiduLocationManager.LocationCallBack;
import com.asiainfo.appframe.webutil.BasePlugin;
import com.asiainfo.appframe.webutil.IPlugin;
import com.asiainfo.appframe.webutil.PluginManager;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.search.core.PoiInfo;
import com.google.gson.Gson;

public class GetLocationPlugin extends BasePlugin {

	/** REQUEST_CODE：扫码 */
	private static final int REQUEST_CODE_LOCATION_MAP = 8000;
	
	private BaiduLocationManager localManager;
	
	String mStrSuccessCallBack;
	String mStrFailedCallBack;
	
	public GetLocationPlugin(IPlugin ecInterface, PluginManager pm) {
		super(ecInterface, pm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(final String action, final JSONArray args) {
		// TODO Auto-generated method stub
		final String success = args.optString(0);
        final String fail = args.optString(1);
        
        
        AddPermission addPermission = new AddPermission((Activity)mECInterface);
        addPermission.addPermission(new AddPermission.PermissionsListener() {
            @Override
            public void onPermissionListener(boolean hasPermission, int code) {
            	if ("GetLocation".equals(action)) {
        			int coortype = args.optInt(2);
        			getLocation(success, fail, coortype);
                } else if("trailStart".equals(action)){
        			trailStart(success, fail);
        		} else if("getTrail".equals(action)){
        			String trailId = args.optString(2);
        			int coortype = args.optInt(3);
        			getTrail(success, fail, trailId, coortype);
        		}
            }
        }, AddPermission.CODE_PERMISSIONS_LOCATION);
	}
	
	@Override
	public void execute(final String action, final JSONArray args, final String type) {
		super.execute(action, args, type);
		mStrSuccessCallBack = args.optString(0);
		mStrFailedCallBack = args.optString(1);
        
        AddPermission addPermission = new AddPermission((Activity)mECInterface);
        addPermission.addPermission(new AddPermission.PermissionsListener() {
            @Override
            public void onPermissionListener(boolean hasPermission, int code) {
            	String param = "";
            	if ("GetLocation".equals(action)) {
        			try {
        				param = URLDecoder.decode(args.optString(2), "UTF-8");
        				JSONObject jsonObject = new JSONObject(param);
        				int coortype = jsonObject.getInt("coordsType");
        				getLocation(mStrSuccessCallBack, mStrFailedCallBack, coortype);
        			} catch (UnsupportedEncodingException e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			} catch (JSONException e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			}
        			
                } else if("trailStart".equals(action)){
        			trailStart(mStrSuccessCallBack, mStrFailedCallBack);
        		} else if("getTrail".equals(action)){
        			try {
        				param = URLDecoder.decode(args.optString(2), "UTF-8");
        				JSONObject jsonObject = new JSONObject(param);
        				
        				String trailId = jsonObject.getString("trailId");
        				int coortype = jsonObject.getInt("coordsType");
        				getTrail(mStrSuccessCallBack, mStrFailedCallBack, trailId, coortype);
        			} catch (UnsupportedEncodingException e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			} catch (JSONException e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			}
        			
        		}else if("locationChoose".equals(action)){
        			Intent intent = new Intent((Context) mECInterface, LocationChooseActivity.class);
        			mECInterface.startActivityForResult(GetLocationPlugin.this, intent,
        					REQUEST_CODE_LOCATION_MAP);
        		}
            }
        }, AddPermission.CODE_PERMISSIONS_LOCATION);
        
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == REQUEST_CODE_LOCATION_MAP) {
				PoiInfo poiInfo = (PoiInfo)intent.getExtras().get("result");
//				PoiInfo poiInfo = (PoiInfo)intent.getSerializableExtra("result");
				if(poiInfo == null){
					return;
				}
				JSONObject obj = new JSONObject();
				try {
					
					obj.put("lon", poiInfo.location.longitude);
					obj.put("lat", poiInfo.location.latitude);
					obj.put("alt", 0);
					obj.put("verAccuracy", 0);
					obj.put("horAccuracy", 0);
					obj.put("speed", 0);
					obj.put("timeStamp", "");
					obj.put("country", "");
					obj.put("name", poiInfo.name);
					obj.put("locality", poiInfo.address);
					obj.put("subLocality", poiInfo.address);
					obj.put("administrativeArea", "");
					
//					obj.put("resultText", code);
				} catch (JSONException e0) {
					e0.printStackTrace();
				}
				
				JSONObject resultObj = new JSONObject();
				try {
					resultObj.put("resultCode", 1);
					resultObj.put("resultMsg", "");
					resultObj.put("object", obj);
				} catch (JSONException e0) {
					e0.printStackTrace();
				}
				callback(resultObj.toString(), mStrSuccessCallBack);
			}
		}
		super.onActivityResult(requestCode, resultCode, intent);
	}

	/**
	 * 开始轨迹记录
	 * 时间间隔60s，总时间15分钟，未到时间获取则以当前时间结束
	 */
	private void trailStart(final String success, final String fail){
		if(CirclePositionService.isSurvival){
			String info = CirclePositionService.isGetTrailInfo();
			if(info != null && info.length() > 0){
				JSONObject resultObj = new JSONObject();
				try {
					resultObj.put("resultCode", 0);
					resultObj.put("resultMsg", info);
				} catch (JSONException e) {
				// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
                callback(resultObj.toString(), fail);
				Toast.makeText(mPluginManager.getContext(), info, Toast.LENGTH_SHORT).show();
			}else{
				
//				new Thread(new Runnable() {
//					
//					@Override
//					public void run() {
//						try {
//							Thread.sleep(3000);
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						
//						JSONObject obj = new JSONObject();
//                        try {
//							obj.put("trailId", CirclePositionService.mList_info.get(0).time);
//							
//						} catch (JSONException e) {
//							// TODO Auto-generated catch block
//							callback("位置信息获取失败", fail);
//							e.printStackTrace();
//						}
//						
//						JSONObject resultObj = new JSONObject();
//						try {
//							resultObj.put("resultCode", 1);
//							resultObj.put("resultMsg", "成功");
//							resultObj.put("object", obj);
//						} catch (JSONException e) {
//						// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//		                
//		                callback(resultObj.toString(), success);
//					}
//				}).start();
			}
			
		}else{
			Intent intent = new Intent(mPluginManager.getContext(), CirclePositionService.class);
			intent.putExtra("success", success);
			intent.putExtra("fail", fail);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			CirclePositionService.setPlugin(this);
			mPluginManager.getContext().startService(intent);
//			mPluginManager.getContext().bindService(intent, conn, Context.BIND_AUTO_CREATE);
//			new Thread(new Runnable() {
//				
//				@Override
//				public void run() {
//					try {
//						Thread.sleep(5000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					JSONObject resultObj = new JSONObject();
//					try {
//						resultObj.put("resultCode", 1);
//						resultObj.put("resultMsg", "成功");
//						String str = CirclePositionService.getTrailId();
//						resultObj.put("object", str);
//					} catch (JSONException e) {
//					// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//	                
//	                callback(resultObj.toString(), success);
//				}
//			}).start();
		}
		
	}
	
	Gson gson = new Gson();
	/**
	 * 获取轨迹记录
	 * 时间间隔60s，总时间15分钟，未到时间获取则以当前时间结束
	 */
	private void getTrail(String success, String fail, String trailId, int coortype){
		if(CirclePositionService.isSurvival){
			List<SimpleLocationInfo> list_trail = CirclePositionService.getTrail(trailId, coortype);
			
			//坐标类型转换
			String coordinateType = "gcj02";
			if(coortype == 1){
				coordinateType = "gcj02";
			} else if(coortype == 2){
				coordinateType = "wgs84";
			} else if(coortype == 4){
				coordinateType = "bd09ll";
			}
			
			if(list_trail != null && list_trail.size() > 0){//成功获取轨迹
				
				JSONArray jsonarray = new JSONArray();
				for (SimpleLocationInfo simpleLocationInfo : list_trail) {
					if(coortype == 2){
						double[] result =  LocationUtil.gcj02towgs84(simpleLocationInfo.lon, simpleLocationInfo.lat);
						simpleLocationInfo.lon = result[0];
						simpleLocationInfo.lat = result[1];
					}else if(coortype == 4){
						double[] result =  LocationUtil.gcj02tobd09(simpleLocationInfo.lon, simpleLocationInfo.lat);
						simpleLocationInfo.lon = result[0];
						simpleLocationInfo.lat = result[1];
					}
					JSONObject jsonObj = null;
					try {
						jsonObj = new JSONObject(gson.toJson(simpleLocationInfo));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					jsonarray.put(jsonObj);
				}
				
				JSONObject obj = new JSONObject();
                try {
					obj.put("trailList", jsonarray);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					callback("位置信息获取失败", fail);
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
			}else{//获取轨迹失败
				JSONObject resultObj = new JSONObject();
				try {
					resultObj.put("resultCode", 0);
					resultObj.put("resultMsg", "获取轨迹失败");
				} catch (JSONException e) {
				// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
                callback(resultObj.toString(), fail);
			}
		}else{
			Toast.makeText(mPluginManager.getContext(), "还未开启轨迹记录", Toast.LENGTH_SHORT).show();
			JSONObject resultObj = new JSONObject();
			try {
				resultObj.put("resultCode", 0);
				resultObj.put("resultMsg", "还未开启轨迹记录");
			} catch (JSONException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            callback(resultObj.toString(), fail);
		}
		
	}
	
	MyBinder binder = null;
	private ServiceConnection conn = new ServiceConnection() {
        
        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
        	binder = null;
        }
        
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub
//            MyBinder binder = (MyBinder)service;
//            BindService bindService = binder.getService();
//            bindService.MyMethod();
//            flag = true;
//        	binder = (MyBinder) service;
        }
    };
	
	private void getLocation(final String success, final String fail, final int coortype){
		String coordinateType = "gcj02";
		if(coortype == 1){
			coordinateType = "gcj02";
		} else if(coortype == 2){
			coordinateType = "wgs84";
		} else if(coortype == 4){
			coordinateType = "bd09ll";
		}
		
		localManager = new BaiduLocationManager(
                mPluginManager.getContext(), new LocationCallBack() {
                    @Override
                    public void onLocationChange(BDLocation location) {
                    	
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        if(coortype == 2){
                        	double[] result =  LocationUtil.gcj02towgs84(longitude, latitude);
                        	longitude = result[0];
                        	latitude = result[1];
                        }
//                        Toast.makeText(mPluginManager.getContext(), "定位成功: " + latitude + " | " + longitude + " | ", Toast.LENGTH_SHORT).show();
                        JSONObject obj = new JSONObject();
                        try {
							obj.put("lon", longitude);
							obj.put("lat", latitude);
							obj.put("alt", location.getAltitude());
							obj.put("verAccuracy", location.getRadius());
							obj.put("horAccuracy", location.getRadius());
							obj.put("speed", location.getSpeed());
							obj.put("timeStamp", location.getTime());
							obj.put("country", location.getAddress().country);
							obj.put("name", location.getAddress().street + location.getAddress().streetNumber);
							obj.put("locality", location.getAddress().city);
							obj.put("subLocality", location.getAddress().district);
							obj.put("administrativeArea", location.getAddress().province);
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							callback("位置信息获取失败", fail);
							e.printStackTrace();
						}
                        
						/////////////////////////////////////////////
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

					@Override
					public void onLocationFail(String msg) {
						// TODO Auto-generated method stub
						JSONObject resultObj = new JSONObject();
						try {
							resultObj.put("resultCode", 0);
							resultObj.put("resultMsg", msg);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						callback(resultObj.toString(), fail);
					}
                }, coordinateType, 1);
        localManager.openLocationTask();
	}
	
	@Override
	public void callback(String result, String type) {
		// TODO Auto-generated method stub
		mPluginManager.callBack(result, type);
	}
	
}
