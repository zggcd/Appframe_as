package com.asiainfo.appframe.webutil.plugins;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.asiainfo.appframe.permission.AddPermission;
import com.asiainfo.appframe.webutil.BasePlugin;
import com.asiainfo.appframe.webutil.IPlugin;
import com.asiainfo.appframe.webutil.PluginManager;

public class Scancode extends BasePlugin {

	/** REQUEST_CODE：扫码 */
	private static final int REQUEST_CODE_SCAN_CODE = 8000;

	private String mStrSuccessCallBack = "";
	
	public Scancode(IPlugin ecInterface, PluginManager pm) {
		super(ecInterface, pm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(final String action, JSONArray args) {
		// TODO Auto-generated method stub
		final String success = args.optString(0);
        final String fail = args.optString(1);
        mStrSuccessCallBack = success;
        
        AddPermission addPermission = new AddPermission((Activity)mECInterface);
        addPermission.addPermission(new AddPermission.PermissionsListener() {
            @Override
            public void onPermissionListener(boolean hasPermission, int code) {
            	if ("Scanning".equals(action)) {//获取设备信息
        			scanning(success, fail);
                }
            }
        }, AddPermission.CODE_PERMISSIONS_CAMERA);
        
		
	}
	
	@Override
	public void execute(final String action, JSONArray args, String type) {
		// TODO Auto-generated method stub
		final String success = args.optString(0);
        final String fail = args.optString(1);
        mStrSuccessCallBack = success;
        
        
        AddPermission addPermission = new AddPermission((Activity)mECInterface);
        addPermission.addPermission(new AddPermission.PermissionsListener() {
            @Override
            public void onPermissionListener(boolean hasPermission, int code) {
            	if ("Scanning".equals(action)) {//获取设备信息
        			scanning(success, fail);
                }
            }
        }, AddPermission.CODE_PERMISSIONS_CAMERA);
//		if ("Scanning".equals(action)) {//获取设备信息
//			(success, fail);
//        }
	}

	@Override
	public void callback(String result, String type) {
		// TODO Auto-generated method stub
		mPluginManager.callBack(result, type);
	}
	
	/**
	 * 扫码
	 */
	public void scanning(String success, String fail) {
		mStrSuccessCallBack = success;
		try {
//			Intent intent = new Intent((Context) mECInterface, CaptureActivity.class);
			Intent intent = new Intent((Context) mECInterface, com.asiainfo.appframe.zxing.CaptureActivity.class);
			
			mECInterface.startActivityForResult(this, intent, REQUEST_CODE_SCAN_CODE);
		} catch (Exception e) {
//			resultText
			JSONObject resultObj = new JSONObject();
			try {
				resultObj.put("resultCode", 0);
				resultObj.put("resultMsg", e.getMessage());
			} catch (JSONException e0) {
				e0.printStackTrace();
			}
			callback(resultObj.toString(), fail);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == REQUEST_CODE_SCAN_CODE) {
				String code = intent.getStringExtra("result");
				
				JSONObject obj = new JSONObject();
				try {
					obj.put("resultText", code);
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

}
