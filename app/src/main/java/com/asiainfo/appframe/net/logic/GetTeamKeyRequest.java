package com.asiainfo.appframe.net.logic;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;

import com.asiainfo.appframe.data.Constants;
import com.asiainfo.appframe.net.ClientRequest;
import com.loopj.android.http.RequestParams;

public class GetTeamKeyRequest extends ClientRequest{

	private String secret;
	
	public GetTeamKeyRequest(Handler handler, int wwhat, String secret) {
		super(handler, wwhat);
		this.secret = secret;
		formRequest(false, Constants.getInstance().getTeamKey);
	}

	@Override
	protected RequestParams appendMainBody() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.put("secret", secret);
		return params;
	}

	@Override
	public void onPostSuccess(String resultStr) {
		// TODO Auto-generated method stub
		JSONObject obj = null;
		try {
			obj = new JSONObject(resultStr);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Message msg = Message.obtain();
		msg.what = what;
		msg.obj = obj;
		handler.sendMessage(msg);
	}
	
	@Override
	public void onPostFail(Exception e) {
		// TODO Auto-generated method stub
		super.onPostFail(e);
		Message msg = Message.obtain();
		msg.what = -1;
		msg.obj = e.getMessage();
		handler.sendMessage(msg);
	}
	
}
