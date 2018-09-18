package com.asiainfo.appframe.net;

public interface RequestResultCallback {

//	public void onPostSuccess(JSONObject response);

	public void onPostFail(Exception e);

	public void onPostSuccess(String resultStr);
	
}
