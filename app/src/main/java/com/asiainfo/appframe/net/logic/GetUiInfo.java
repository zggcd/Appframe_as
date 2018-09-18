package com.asiainfo.appframe.net.logic;

import android.os.Handler;
import android.os.Message;

import com.asiainfo.appframe.CommonApplication;
import com.asiainfo.appframe.bean.HomePageInfo;
import com.asiainfo.appframe.bean.HomepageInfo2;
import com.asiainfo.appframe.net.ClientRequest;
import com.asiainfo.appframe.utils.SDKUtil;
import com.loopj.android.http.RequestParams;

/**
 * 获取首页、底部导航栏信息
 * @author Stiffen
 *
 */
public class GetUiInfo extends ClientRequest {
	
	private String app_id;
	private String au_id;
	@SuppressWarnings("unused")
	private String appsecret;

	/**
	 * @param handler
	 * @param what
	 * @param app_id
	 * @param au_id					有au_id表示只获取页面信息，不获取底部导航栏，不存au_id则表示获取全部页面信息
	 * @param appsecret
	 */
	public GetUiInfo(Handler handler, int what, String app_id, String au_id, String appsecret) {
		super(handler, what);
		this.app_id = app_id;
		this.au_id = au_id;
		this.appsecret = appsecret;
		formRequest(false, com.asiainfo.appframe.data.Constants.getInstance().getUiLayout + "?access_token=" + SDKUtil.accessToken);
	}

	@Override
	protected RequestParams appendMainBody() {
		RequestParams params = new RequestParams();
		try {
			params.put("app_id", app_id);
			if(au_id != null){
				params.put("au_id", au_id);
			}
			return  params;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void onPostSuccess(String resultStr) {
		// TODO Auto-generated method stub
		HomepageInfo2 homePageInfo = gson.fromJson(resultStr, HomepageInfo2.class);
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
