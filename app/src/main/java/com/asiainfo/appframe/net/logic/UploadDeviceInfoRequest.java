package com.asiainfo.appframe.net.logic;

import android.os.Handler;

import com.asiainfo.appframe.net.ClientRequest;
import com.loopj.android.http.RequestParams;

public class UploadDeviceInfoRequest extends ClientRequest {

	private String deviceId;
	private String osName;
	private String osPlatform;
	private String osVersion;
	private String screenSize;
	private String deviceType;
	
	public UploadDeviceInfoRequest(String url, Handler handler, int what, String deviceId, String osName, String osPlatform, String osVersion, String screenSize, String deviceType) {
		super(handler, what);
		this.deviceId = deviceId;
		this.osName = osName;
		this.osPlatform = osPlatform;
		this.osVersion = osVersion;
		this.screenSize = screenSize;
		this.deviceType = deviceType;
		formRequest(false, url);
	}

	@Override
	protected RequestParams appendMainBody() {
		RequestParams params = new RequestParams();
		try {
			params.put("deviceId", deviceId);
			params.put("osName", osName);
			params.put("osPlatform", osPlatform);
			params.put("osVersion", osVersion);
			params.put("screenSize", screenSize);
			params.put("deviceType", deviceType);
			return params;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void onPostSuccess(String resultStr) {
		super.onPostSuccess(resultStr);
	}
	
	@Override
	public void onPostFail(Exception e) {
	}

}
