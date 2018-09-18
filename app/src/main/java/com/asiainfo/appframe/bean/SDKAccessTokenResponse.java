package com.asiainfo.appframe.bean;

/**
 * SDK返回的获取accessToken回调的解析类
 * @author Stiven
 *
 */
public class SDKAccessTokenResponse {
	
	private int code;
	private String msg;
	private String authInfoResult;
	private String expires_in;
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
	public String getAuthInfoResult() {
		return authInfoResult;
	}
	public void setAuthInfoResult(String authInfoResult) {
		this.authInfoResult = authInfoResult;
	}
	public String getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}
	
	
	
}
