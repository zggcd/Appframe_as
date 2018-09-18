package com.asiainfo.appframe.utils;

/**
 * 统一认证的sdk返回
 * @author Stiven
 *
 */
public interface SDKAuthCallBack {
	/**
	 * 获取验证码的返回
	 * @return
	 */
//	public void onValidateCodeCallback(ValidateCodeResponse validateCode);
	public void onValidateCodeCallback(String result);
	/**
	 * 获取地区码的返回
	 * @return
	 */
//	public void onAreaCodeCallback(List<AuthCodes> areaCodeInfo);
	public void onAreaCodeCallback(String result);
	/**
	 * 获取accesstoken的返回
	 * @return
	 */
//	public void onAccessTokenCallback(AccessTokenResponse response);
	public void onAccessTokenCallback(String result);
	
	/**
	 * 刷新accesstoken的返回
	 * @return
	 */
//	public void onRefreshAccessTokenCallback(AccessTokenResponse accessTokenResponse);
	public void onRefreshAccessTokenCallback(String result);
	/**
	 * 失败回调
	 */
	public void onError(String errMessage);
}
