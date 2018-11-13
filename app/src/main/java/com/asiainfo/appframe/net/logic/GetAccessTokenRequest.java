package com.asiainfo.appframe.net.logic;

import android.os.Handler;
import android.os.Message;

import com.asiainfo.appframe.bean.AccessTokenResponse;
import com.asiainfo.appframe.data.Constants;
import com.asiainfo.appframe.net.ClientRequest;
import com.asiainfo.appframe.utils.RSAUtil;
import com.asiainfo.appframe.utils.SDKUtil;
import com.loopj.android.http.RequestParams;

public class GetAccessTokenRequest extends ClientRequest  {

	private String phone_num;		//用户手机号
	private String mac;				//机器码作为唯一标识
	private String app_id;			//应用ID
	private String area_code;		//地区码
	private String password;		//密码
	private String staff_code;		//用户的staff_id
	private String app_secret;		//用户的app_secret
	private String staff_id;
	private String basecode;		//密码，password为验证码
	
	public GetAccessTokenRequest(Handler handler, int what, String phone_num, String password,String basecode, String app_id, String area_code, String mac, String staff_code, String app_secret, String staff_id) {
		// TODO Auto-generated constructor stub
		super(handler, what);
		this.phone_num = phone_num;
		this.password = password;
		this.app_id = app_id;
		this.area_code = area_code;
		this.mac = mac;
		this.staff_code = staff_code;
		this.app_secret = app_secret;
		this.staff_id = staff_id;
		this.basecode = basecode;
		formRequest(false, SDKUtil.GetAccessToken);
	}
	
	public GetAccessTokenRequest(String url, Handler handler, int what, String phone_num, String password, String basecode, String app_id, String area_code, String mac, String staff_code, String app_secret, String staff_id) {
		// TODO Auto-generated constructor stub
		super(handler, what);
		this.phone_num = phone_num;
		this.password = password;
		this.basecode = basecode;
		this.app_id = app_id;
		this.area_code = area_code;
		this.mac = mac;
		this.staff_code = staff_code;
		this.app_secret = app_secret;
		this.staff_id = staff_id;
		formRequest(false, url);
	}
	
	public GetAccessTokenRequest(Handler handler, int what) {
		super(handler, what);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected RequestParams appendMainBody() {
		RequestParams params = new RequestParams();
		try {
			params.put("phone_num", RSAUtil.getEnCodeString(phone_num, SDKUtil.key_public));
			params.put("mac", mac);
			params.put("app_id", app_id);
			params.put("area_code", area_code);
			params.put("password", password);
			params.put("basecode", RSAUtil.getEnCodeString(basecode, SDKUtil.key_public));
			params.put("staff_code", staff_code);
			params.put("app_secret", app_secret);
			params.put("staff_id", staff_id);
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
//		msg.obj = response;
//		handler.sendMessage(msg);
	}
	
	@Override
	public void onPostFail(Exception e) {
		// TODO Auto-generated method stub
		super.onPostFail(e);
	}

}
