package com.asiainfo.appframe.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * 第三方开发者开启WEB穿入的type类参数
 * @author Stiven
 *
 */
public class SDKJSType {

	private String signature;
	private Map<String, Object> params = new HashMap<String, Object>();
	
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public Map<String, Object> getParams() {
		return params;
	}
	public void setParams(Map<String, Object> param) {
		this.params = param;
	}
	
}
