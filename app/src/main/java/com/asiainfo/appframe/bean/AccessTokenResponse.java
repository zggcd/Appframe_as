package com.asiainfo.appframe.bean;

public class AccessTokenResponse {
	
	/**
	 * 用户授权的唯一票据，用于调用微博的开放接口，
	 * 同时也是第三方应用验证微博用户登录的唯一票据，
	 * 第三方应用应该用该票据和自己应用内的用户建立唯一影射关系，
	 * 来识别登录状态，不能使用本返回值里的UID字段来做登录识别。
	 */
	private String access_token;
	
	/**
	 * access_token的生命周期，单位是秒数
	 */
	private String expires_in;
	
	/**
	 * 用Access_token失效以后，使用refreshToken进行刷新
	 */
	private String refreshtoken;
	
	/**
	 * 错误代码，未能获取access_token时才存在
	 */
	private int code;
	
	/**
	 * 错误描述，未能获取access_token时才存在
	 */
	private String msg;
	
	/**
	 * 用户信息，直接返回
	 */
	private AuthInfoResult authInfoResult;

	public AuthInfoResult getAuthInfoResult() {
		return authInfoResult;
	}

	public void setAuthInfoResult(AuthInfoResult authInfoResult) {
		this.authInfoResult = authInfoResult;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}

	public String getRefreshtoken() {
		return refreshtoken;
	}

	public void setRefreshtoken(String refreshtoken) {
		this.refreshtoken = refreshtoken;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
	
}
