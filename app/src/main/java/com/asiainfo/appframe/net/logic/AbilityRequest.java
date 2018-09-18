package com.asiainfo.appframe.net.logic;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;

import com.asiainfo.appframe.net.ClientRequest;
import com.asiainfo.appframe.utils.Log;
import com.loopj.android.http.RequestParams;

public class AbilityRequest extends ClientRequest {

	String abilityalias = "";
	String signature = "";
	String userparam = "";
	
	public AbilityRequest(String url, Handler handler, int what, String abilityalias, String signature, String userParam) {
		super(handler, what);
		this.abilityalias = abilityalias;
		this.signature = signature;
		this.userparam = userParam; 
		formRequest(false, url);
		Log.d("Appframe", url);
	}

	@Override
	protected RequestParams appendMainBody() {
		// TODO Auto-generated method stub
		H5Seq = Seq;
		RequestParams params = new RequestParams();
		try {
			params.put("abilityalias", abilityalias);
			params.put("signature", signature);
			
			JSONObject json = new JSONObject(userparam);
			JSONArray names = json.names();
			if(names != null){
				for (int i = 0; i < names.length(); i++) {
					String key = names.getString(i);
					String value = json.optString(key, "");
					params.add(key, value);
				}
			}
			
			Log.d("Appframe", "== params ==> " + params);
			return  params;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void onPostSuccess(String resultStr) {
		// TODO Auto-generated method stub
		Log.d("Appframe", "== resultStr ==> " +resultStr);
//		JumpToWebWindowResponse response = gson.fromJson(resultStr, JumpToWebWindowResponse.class);
//		if(response.code == 1){
			Message msg = Message.obtain();
			msg.what = what;
			msg.obj = resultStr;
			handler.sendMessage(msg);
//		}else{
//			
//		}
		
	}
	
	@Override
	public void onPostFail(Exception e) {
		// TODO Auto-generated method stub
		Log.d("Appframe", "== e ==> " + e.getMessage());
		super.onPostFail(e);
	}

}
