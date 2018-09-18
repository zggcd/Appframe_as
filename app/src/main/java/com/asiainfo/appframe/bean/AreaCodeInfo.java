package com.asiainfo.appframe.bean;

import java.util.List;

public class AreaCodeInfo {

	private List<AreaCodes> authCodes ;

	private int code;

	public void setAuthCodes(List<AreaCodes> authCodes){
	this.authCodes = authCodes;
	}
	public List<AreaCodes> getAuthCodes(){
	return this.authCodes;
	}
	public void setCode(int code){
	this.code = code;
	}
	public int getCode(){
	return this.code;
	}
	
}
