package com.asiainfo.appframe.net.logic;

import java.util.Date;

import android.os.Handler;

import com.asiainfo.appframe.net.ClientRequest;
import com.asiainfo.appframe.utils.HttpUtil;
import com.asiainfo.appframe.utils.SDKUtil;
import com.asiainfo.appframe.utils.StringUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

public class RecordH5InvokeRequest extends ClientRequest {

	String requestUrl;
	Date requestTime;
	Date responseTime;
	String responseText;
	String responseStatus;
	String app_id;
	
	String RequestSeq;
	
	public RecordH5InvokeRequest(String url, Handler handler, int what, String requestUrl, Date requestTime, Date responseTime, String responseText, String responseStatus, String app_id) {
		super(handler, what);
		this.requestUrl = requestUrl;
		this.requestTime = requestTime;
		this.responseTime = responseTime;
		this.responseText = responseText;
		this.responseStatus = responseStatus;
		this.app_id = app_id;
		
		formRequest(false, url);
	}
	
	@Override
	protected void addHeader() {
		super.addHeader();
	}

	@Override
	protected RequestParams appendMainBody() {
		RequestParams params = new RequestParams();
		try {
			params.put("requestUrl", requestUrl);
			params.put("requestTime", requestTime);
			params.put("responseTime", responseTime);
			params.put("responseText", responseText);
			params.put("responseStatus", responseStatus);
			if(H5Seq != null && H5Seq.length() > 0){
				params.put("parentReqSeq", H5Seq);
			}
			params.put("appid", app_id);
			return params;
		} catch (Exception e) {
			
		}
		return null;
	}
	
	@Override
	public void onPostSuccess(String resultStr) {
		// TODO Auto-generated method stub
		super.onPostSuccess(resultStr);
	}
	
	@Override
	public void onPostFail(Exception e) {
		// TODO Auto-generated method stub
		super.onPostFail(e);
	}

}
