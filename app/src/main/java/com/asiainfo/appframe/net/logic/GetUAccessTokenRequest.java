package com.asiainfo.appframe.net.logic;

import android.os.Handler;
import android.os.Message;

import com.asiainfo.appframe.net.ClientRequest;
import com.asiainfo.appframe.utils.Des3Util;
import com.asiainfo.appframe.utils.SDKUtil;
import com.loopj.android.http.RequestParams;

/**
 * 获取Uname相关的accessToken
 * @author Stiven
 *
 */
public class GetUAccessTokenRequest extends ClientRequest {

	String phone_num;
	String staff_code;
	String user_name;
	String area_code;
	String app_id;
	String mac;
	String signer;
	
	public GetUAccessTokenRequest(String url, Handler handler, int what, String phone_num, String staff_code, String user_name, String area_code, String app_id, String mac) {
		super(handler, what);
		this.phone_num = phone_num;
		this.staff_code = staff_code;
		this.user_name = user_name;
		this.area_code = area_code;
		this.app_id = app_id;
		this.mac = mac;
		String decode = "";
		Des3Util.secretKey = SDKUtil.appSecret;
		String preDecode = "app_id=" + app_id + "&" +
						   "area_code=" + area_code + "&" +
						   "phone_num=" + phone_num + "&" +
						   "staff_code=" + staff_code + "&" +
						   "user_name=" + user_name	 + "&";
		try {
			this.signer = Des3Util.encode(preDecode);
			decode = Des3Util.decode(signer);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		formRequest(false, url);
		
	}

	@Override
	protected RequestParams appendMainBody() {
		RequestParams params = new RequestParams();
		try {
			params.put("phone_num", phone_num); 
			params.put("staff_code", staff_code);
			params.put("user_name", user_name);
			params.put("area_code", area_code);
			params.put("app_id", app_id);
			params.put("mac", mac);
			params.put("signer", signer);
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
	}
	
	@Override
	public void onPostFail(Exception e) {
		// TODO Auto-generated method stub
		super.onPostFail(e);
	}

}
