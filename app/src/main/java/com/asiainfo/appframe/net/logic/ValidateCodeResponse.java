package com.asiainfo.appframe.net.logic;

/**
 * 获取验证码返回的类
 * @author Stiven
 *
 */
public class ValidateCodeResponse {
	//{"code":0,"result":"0","timeleft":"60"}
	private int code;
	private String result;
	private String timeleft;
	private String msg = "";
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getTimeleft() {
		return timeleft;
	}
	public void setTimeleft(String timeleft) {
		this.timeleft = timeleft;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
}
