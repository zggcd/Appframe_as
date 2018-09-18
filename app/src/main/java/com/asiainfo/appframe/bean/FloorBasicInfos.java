package com.asiainfo.appframe.bean;

import java.io.Serializable;

public class FloorBasicInfos implements Serializable{
	
//	{
//        "clickurl": "http://zc.testpub.net/files/appframework/banner@3x.png",
//        "createtime": {
//            "date": 20,
//            "day": 1,
//            "hours": 0,
//            "minutes": 0,
//            "month": 1,
//            "seconds": 0,
//            "time": 1487520000000,
//            "timezoneOffset": -480,
//            "year": 117
//        },
//        "homeid": 1,
//        "id": 1,
//        "picurl": "http://zc.testpub.net/files/appframework/banner@3x.png",
//        "showhome": 1,
//        "sort": 1
//    }
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String clickurl;

	private Createtime createtime;

	private int homeid;

	private int id;

	private String picurl;

	private int showhome;

	private int sort;
	
	private String title;
	
	private String abilityalias;
	
	private String extendsabilityalias;		//取数

	public String getExtendsabilityalias() {
		return extendsabilityalias;
	}
	public void setExtendsabilityalias(String extendsabilityalias) {
		this.extendsabilityalias = extendsabilityalias;
	}
	public String getAbilityalias() {
		return abilityalias;
	}
	public void setAbilityalias(String abilityalias) {
		this.abilityalias = abilityalias;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setClickurl(String clickurl){
	this.clickurl = clickurl;
	}
	public String getClickurl(){
	return this.clickurl;
	}
	public void setCreatetime(Createtime createtime){
	this.createtime = createtime;
	}
	public Createtime getCreatetime(){
	return this.createtime;
	}
	public void setHomeid(int homeid){
	this.homeid = homeid;
	}
	public int getHomeid(){
	return this.homeid;
	}
	public void setId(int id){
	this.id = id;
	}
	public int getId(){
	return this.id;
	}
	public void setPicurl(String picurl){
	this.picurl = picurl;
	}
	public String getPicurl(){
	return this.picurl;
	}
	public void setShowhome(int showhome){
	this.showhome = showhome;
	}
	public int getShowhome(){
	return this.showhome;
	}
	public void setSort(int sort){
	this.sort = sort;
	}
	public int getSort(){
	return this.sort;
	}
}
