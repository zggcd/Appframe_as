package com.asiainfo.appframe.net.logic.msgpush;

import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;

public class MsgPushRefreshRequest  extends MsgPushClientRequest {
	
	String version;
	String token;
	
	public MsgPushRefreshRequest(String url, Handler handler, int what, String version, String token) {
		super(handler, what);
		this.version = version;
		this.token = token;
		formRequest(false, "http://10.20.16.170:8101/mqtt-web/staff/refreshToken");
	}

	@Override
	protected String appendMainBody() {
		JSONObject jo = new JSONObject();
		try {
			jo.put("version", version);
			jo.put("token", token);
			return  jo.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void onPostSuccess(String resultStr) {
		Message msg = Message.obtain();
		msg.what = what;
		msg.obj = resultStr;
		handler.sendMessage(msg);
	}
	
	@Override
	public void onPostFail(Exception e) {
		super.onPostFail(e);
	}
}
