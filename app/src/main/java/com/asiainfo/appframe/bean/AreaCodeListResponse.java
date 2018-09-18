package com.asiainfo.appframe.bean;

import java.util.List;

public class AreaCodeListResponse {
//	{"authCodes":[{"areaCode":"0000","areaName":"省公司","staffId":"43806"},{"areaCode":"0510","areaName":"无锡","staffId":"43806"}],
//		"code":1,
//		"error":"",
//		"errorDescription":"",
//		"msg":"",
//		"params":"",
//		"result":"0"
//	}
	
	private List<AreaCodes> areaCodes;
	private int code;
	private String error;
	private String errorDescription;
	private String msg;
	private String params;
	private String result;
	
	public List<AreaCodes> getAreaCodes() {
		return areaCodes;
	}
	public void setAreaCodes(List<AreaCodes> areaCodes) {
		this.areaCodes = areaCodes;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getErrorDescription() {
		return errorDescription;
	}
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
}
