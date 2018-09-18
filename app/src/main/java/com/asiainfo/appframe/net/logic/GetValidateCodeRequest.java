package com.asiainfo.appframe.net.logic;

import android.os.Handler;
import android.os.Message;

import com.asiainfo.appframe.data.Constants;
import com.asiainfo.appframe.net.ClientRequest;
import com.asiainfo.appframe.utils.HttpUtil;
import com.asiainfo.appframe.utils.RSAHelper;
import com.asiainfo.appframe.utils.RSAUtil;
import com.asiainfo.appframe.utils.SDKUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

/**
 * 根据手机号获取验证码
 * @author Stiven
 *
 */
public class GetValidateCodeRequest extends ClientRequest {
	
	private String phone_num;
	private String app_id;
	
	public GetValidateCodeRequest(Handler handler, int what, String phone_num, String app_id) {
		super(handler, what);
		formRequest(false, Constants.getInstance().getValidateCode + "?phone_num=" + phone_num + "&app_id=" + app_id);
	}
	
	public GetValidateCodeRequest(String url, Handler handler, int what, String phone_num, String app_id) {
		super(handler, what);
		this.phone_num = RSAUtil.getEnCodeString(phone_num, SDKUtil.key_public);
		this.app_id = app_id;
//			String str1 = new String(RSAHelper.encryptData(phone_num.getBytes(), RSAHelper.getPublicKey(SDKUtil.key_public)));
		formRequest(false, url);
		
	}
	
	@Override
	protected void addHeader() {
		// TODO Auto-generated method stub
		super.addHeader();
		AsyncHttpClient client = HttpUtil.getClient();
		client.addHeader("AF-Version", "1.0");
	}

	@Override
	protected RequestParams appendMainBody() {
		RequestParams params = new RequestParams();
		try {
			params.put("phone_num", phone_num);
			params.put("app_id", app_id);
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
		
//		ValidateCodeResponse response = gson.fromJson(resultStr, ValidateCodeResponse.class);
//		if(response.getCode() == 1){
//			Message msg = Message.obtain();
//			msg.what = what;
//			msg.obj = response;
//			handler.sendMessage(msg);
//		}else{
//			Message msg = Message.obtain();
//			msg.what = 0;
//			msg.obj = resultStr;
//			handler.sendMessage(msg);
//		}
		
	}
	
	@Override
	public void onPostFail(Exception e) {
		// TODO Auto-generated method stub
		super.onPostFail(e);
	}

}
