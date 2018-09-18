package com.asiainfo.appframe.net.logic;

import android.os.Handler;
import android.os.Message;

import com.asiainfo.appframe.net.ClientRequest;
import com.asiainfo.appframe.utils.RSAUtil;
import com.asiainfo.appframe.utils.SDKUtil;
import com.loopj.android.http.RequestParams;

/**
 * 获取地区码
 * @author Stiffen
 *
 */
public class GetAreaCodeRequest extends ClientRequest {

	private String username;
	
	public GetAreaCodeRequest(Handler handler, int what, String username) {
		super(handler, what);
		this.username = username;
		formRequest(false, com.asiainfo.appframe.data.Constants.getInstance().getAreaCode);
	}
	
	public GetAreaCodeRequest(String url, Handler handler, int what, String username) {
		super(handler, what);
		this.username = username;
		formRequest(false, url);
	}

	@Override
	protected RequestParams appendMainBody() {
		RequestParams params = new RequestParams();
		try {
			params.put("username", RSAUtil.getEnCodeString(username, SDKUtil.key_public));
			params.put("app_id", username);
			return  params;
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
		
//		if(resultStr.contains("`")){
//			AreaCodeListResponse areaCodeListResponse = gson.fromJson(resultStr, AreaCodeListResponse.class);
//			
//			Message msg = Message.obtain();
//			msg.what = 0;
//			msg.obj = resultStr;
//			handler.sendMessage(msg);
//			
//		}else{
//			List<AuthCodes> areaCodeInfo = gson.fromJson(resultStr, new TypeToken<List<AuthCodes>>() {}.getType());
//			Message msg = Message.obtain();
//			msg.what = what;
//			msg.obj = areaCodeInfo;
//			handler.sendMessage(msg);
//		}
	}
	
	@Override
	public void onPostFail(Exception e) {
		// TODO Auto-generated method stub
		super.onPostFail(e);
	}
	
}
