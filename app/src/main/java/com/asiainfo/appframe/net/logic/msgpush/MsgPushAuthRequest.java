package com.asiainfo.appframe.net.logic.msgpush;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;

import com.asiainfo.appframe.bean.MsgPushAuthResult;
import com.asiainfo.appframe.net.ClientRequest;
import com.asiainfo.appframe.utils.RSAUtil;
import com.asiainfo.appframe.utils.SDKUtil;
import com.loopj.android.http.RequestParams;

public class MsgPushAuthRequest extends MsgPushClientRequest {
	
	String version;
	String staff_id;
	String app_code;
	String area_code;
	int timestamp;
	String ip;
	String signature;
	
	public MsgPushAuthRequest(String url, Handler handler, int what, String version, String staff_id, String app_code, String area_code, int timestamp, String ip, String signature) {
		super(handler, what);
		this.version = version;
		this.staff_id = staff_id;
		this.app_code = app_code;
		this.area_code = area_code;
		this.timestamp = timestamp;
		this.ip = ip;
		this.signature = signature;
		formRequest(false, "http://10.20.16.170:8101/mqtt-web/staff");
	}

	@Override
	protected String appendMainBody() {
		JSONObject jo = new JSONObject();
		try {
			jo.put("version", version);
			jo.put("staff_id", staff_id);
			jo.put("app_code", app_code);
			jo.put("area_code", area_code);
			jo.put("timestamp", timestamp);
			jo.put("ip", ip);
			jo.put("signature", signature);
			return  jo.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void onPostSuccess(String resultStr) {
		
		// TODO Auto-generated method stub
		MsgPushAuthResult obj = null;
		obj = gson.fromJson(resultStr, MsgPushAuthResult.class);
		Message msg = Message.obtain();
		msg.what = what;
		msg.obj = obj;
		handler.sendMessage(msg);
	}
	
	@Override
	public void onPostFail(Exception e) {
		super.onPostFail(e);
	}

}
