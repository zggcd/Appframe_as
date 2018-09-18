package com.asiainfo.appframe.bean;

public class MsgPushAuthResult {
	
	private MsgPushAuthInfo object;

	private String resultCode;

	public void setObject(MsgPushAuthInfo object){
		this.object = object;
	}
	public MsgPushAuthInfo getObject(){
		return this.object;
	}
	public void setResultCode(String resultCode){
		this.resultCode = resultCode;
	}
	public String getResultCode(){
		return this.resultCode;
	}
}
