package com.asiainfo.appframe.net.logic;

import android.os.Handler;
import android.os.Message;

import com.asiainfo.appframe.bean.AccessTokenResponse;
import com.asiainfo.appframe.data.Constants;
import com.asiainfo.appframe.net.ClientRequest;
import com.asiainfo.appframe.utils.RSAUtil;
import com.asiainfo.appframe.utils.SDKUtil;
import com.loopj.android.http.RequestParams;

public class RefreshAccessTokenRequest extends ClientRequest {
	private String phone_num;		//用户手机号
	private String mac;				//机器码作为唯一标识
	private String refresh_token;	//accesstoken
	private int location;			//当前数据位置
	
	public RefreshAccessTokenRequest(Handler handler, int what, int location, String phone_num, String mac,String refresh_token) {
		// TODO Auto-generated constructor stub
		super(handler, what);
		this.phone_num = phone_num;
		this.refresh_token = refresh_token;
		this.mac = mac;
		this.location = location;
		formRequest(false, Constants.getInstance().refreshAccessToken);
	}
	
	public RefreshAccessTokenRequest(String url, Handler handler, int what, int location, String phone_num, String mac,String refresh_token) {
		// TODO Auto-generated constructor stub
		super(handler, what);
		this.phone_num = phone_num;
		this.refresh_token = refresh_token;
		this.mac = mac;
		this.location = location;
		formRequest(false, url);
	}
	
	public RefreshAccessTokenRequest(Handler handler, int what) {
		super(handler, what);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected RequestParams appendMainBody() {
		RequestParams params = new RequestParams();
		try {
			params.put("phone_num", RSAUtil.getEnCodeString(phone_num, SDKUtil.key_public));
			params.put("mac", mac);
			params.put("refresh_token", refresh_token);
			return  params;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void onPostSuccess(String resultStr) {
		// TODO Auto-generated method stub
		
		Message msg = Message.obtain();
		msg.what = what;
		msg.obj = resultStr;
		handler.sendMessage(msg);
		
//		AccessTokenResponse response = gson.fromJson(resultStr, AccessTokenResponse.class);
//		Message msg = Message.obtain();
//		msg.what = what;
//		msg.arg1 = location;
//		msg.obj = response;
//		handler.sendMessage(msg);
	}
	
	@Override
	public void onPostFail(Exception e) {
		// TODO Auto-generated method stub
		super.onPostFail(e);
	}
}
