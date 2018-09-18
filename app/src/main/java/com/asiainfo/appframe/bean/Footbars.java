package com.asiainfo.appframe.bean;

import java.io.Serializable;

public class Footbars implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String cssCode;

	private int cssid;

	private int id;

	private int isdefault;

	private String picurl;

	private int sort;

	private int status;

	private String title;

	private int type;

	private int urid;

	private int version;
	
	private String clickurl;
	
	private String abilityalias;

	public String getAbilityalias() {
		return abilityalias;
	}
	public void setAbilityalias(String abilityalias) {
		this.abilityalias = abilityalias;
	}
	public String getClickurl() {
		return clickurl;
	}
	public void setClickurl(String clickurl) {
		this.clickurl = clickurl;
	}
	public void setCssCode(String cssCode){
	this.cssCode = cssCode;
	}
	public String getCssCode(){
	return this.cssCode;
	}
	public void setCssid(int cssid){
	this.cssid = cssid;
	}
	public int getCssid(){
	return this.cssid;
	}
	public void setId(int id){
	this.id = id;
	}
	public int getId(){
	return this.id;
	}
	public void setIsdefault(int isdefault){
	this.isdefault = isdefault;
	}
	public int getIsdefault(){
	return this.isdefault;
	}
	public void setPicurl(String picurl){
	this.picurl = picurl;
	}
	public String getPicurl(){
	return this.picurl;
	}
	public void setSort(int sort){
	this.sort = sort;
	}
	public int getSort(){
	return this.sort;
	}
	public void setStatus(int status){
	this.status = status;
	}
	public int getStatus(){
	return this.status;
	}
	public void setTitle(String title){
	this.title = title;
	}
	public String getTitle(){
	return this.title;
	}
	public void setType(int type){
	this.type = type;
	}
	public int getType(){
	return this.type;
	}
	public void setUrid(int urid){
	this.urid = urid;
	}
	public int getUrid(){
	return this.urid;
	}
	public void setVersion(int version){
	this.version = version;
	}
	public int getVersion(){
	return this.version;
	}
}
