package com.asiainfo.appframe.net.logic;

import java.util.Map;

import android.os.Handler;
import android.os.Message;

import com.asiainfo.appframe.bean.AccessTokenResponse;
import com.asiainfo.appframe.bean.JumpToWebWindowResponse;
import com.asiainfo.appframe.data.Constants;
import com.asiainfo.appframe.net.ClientRequest;
import com.asiainfo.appframe.utils.Log;
import com.loopj.android.http.RequestParams;

public class JumpToWebWindowRequest extends ClientRequest{

	private String storeName;
	private String signature;
	private Map<String, Object> params;
	
	public JumpToWebWindowRequest(String url, Handler handler, int what, String storeName, String signature, Map<String, Object> params) {
		super(handler, what);
		this.storeName = storeName;
		this.signature = signature;
		this.params = params;
		formRequest(false, url);
		
		Log.d("Appframe", url);
	}

	@Override
	protected RequestParams appendMainBody() {
		RequestParams params = new RequestParams();
		try {
			params.put("abilityalias", storeName);
			params.put("signature", signature);
			
			for (Map.Entry<String, Object> entry : this.params.entrySet()) {
				params.put(entry.getKey(), entry.getValue());
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
		
		Message msg = Message.obtain();
		msg.what = what;
		msg.obj = resultStr;
		handler.sendMessage(msg);
		
	}
	
	@Override
	public void onPostFail(Exception e) {
		// TODO Auto-generated method stub
		Log.d("Appframe", "== e ==> " + e.getMessage());
		super.onPostFail(e);
	}

}
