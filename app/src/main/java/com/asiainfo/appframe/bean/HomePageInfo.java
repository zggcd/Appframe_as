package com.asiainfo.appframe.bean;

/**
 * 首页样式信息类
 * @author Stiven
 *
 */
public class HomePageInfo {
//	{
//	    "code": 0,
//	    "msg": "success",
//	    "uiDto": {}
//	}
	
	private int code;

	private String msg;

	private UiDto uiDto;

	public void setCode(int code){
	this.code = code;
	}
	public int getCode(){
	return this.code;
	}
	public void setMsg(String msg){
	this.msg = msg;
	}
	public String getMsg(){
	return this.msg;
	}
	public void setUiDto(UiDto uiDto){
	this.uiDto = uiDto;
	}
	public UiDto getUiDto(){
	return this.uiDto;
	}
}
