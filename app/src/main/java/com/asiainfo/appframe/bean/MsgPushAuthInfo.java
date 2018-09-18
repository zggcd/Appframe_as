package com.asiainfo.appframe.bean;

import java.util.List;

public class MsgPushAuthInfo {

	private String app_code;

	private String area_code;

	private int expire_time;

	private List<String> pub_topics ;

	private String staff_id;

	private List<String> sub_topics ;

	private String token;

	public void setApp_code(String app_code){
	this.app_code = app_code;
	}
	public String getApp_code(){
	return this.app_code;
	}
	public void setArea_code(String area_code){
	this.area_code = area_code;
	}
	public String getArea_code(){
	return this.area_code;
	}
	public void setExpire_time(int expire_time){
	this.expire_time = expire_time;
	}
	public int getExpire_time(){
	return this.expire_time;
	}
	public void setPub_topics(List<String> pub_topics){
	this.pub_topics = pub_topics;
	}
	public List<String> getPub_topics(){
	return this.pub_topics;
	}
	public void setStaff_id(String staff_id){
	this.staff_id = staff_id;
	}
	public String getStaff_id(){
	return this.staff_id;
	}
	public void setSub_topics(List<String> sub_topics){
	this.sub_topics = sub_topics;
	}
	public List<String> getSub_topics(){
	return this.sub_topics;
	}
	public void setToken(String token){
	this.token = token;
	}
	public String getToken(){
	return this.token;
	}
	
}
