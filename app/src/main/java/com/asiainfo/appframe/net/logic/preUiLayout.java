package com.asiainfo.appframe.net.logic;

import android.os.Handler;
import android.os.Message;

import com.asiainfo.appframe.bean.HomePageInfo;
import com.asiainfo.appframe.net.ClientRequest;
import com.loopj.android.http.RequestParams;

/**
 * 获取登陆及过场动画配置
 * @author Stiven
 *
 */
public class preUiLayout extends ClientRequest  {
	
	private String app_id;
	
	public preUiLayout(Handler handler, int what, String app_id) {
		super(handler, what);
		this.app_id = app_id;
		formRequest(false, com.asiainfo.appframe.data.Constants.getInstance().preUiLayout);
		// TODO Auto-generated constructor stub
	}

	

	@Override
	protected RequestParams appendMainBody() {
		RequestParams params = new RequestParams();
		try {
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
		HomePageInfo homePageInfo = gson.fromJson(resultStr, HomePageInfo.class);
		Message msg = Message.obtain();
		msg.what = what;
		msg.obj = homePageInfo;
		handler.sendMessage(msg);
	}
	
	@Override
	public void onPostFail(Exception e) {
		// TODO Auto-generated method stub
		super.onPostFail(e);
	}
	
}
