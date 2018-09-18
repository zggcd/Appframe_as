package com.asiainfo.appframe.bean;

import java.util.List;

public class HomePageDtoList {
	
//	{
//        "cssCode": "FLOOR-001",
//        "cssid": 4,
//        "floorBasicInfos": []
//        "id": 1,
//        "sort": 1,
//        "status": 1,
//        "uiid": 3
//    }
	
	private String cssCode;

	private int cssid;			//样式ID 1：广告轮播；2,：一行两个大图；3:一行四个图；4：底部导航栏

	private List<FloorBasicInfos> floorBasicInfos ;

	private int id;

	private int sort;

	private int status;

	private int uiid;

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
	public void setFloorBasicInfos(List<FloorBasicInfos> floorBasicInfos){
	this.floorBasicInfos = floorBasicInfos;
	}
	public List<FloorBasicInfos> getFloorBasicInfos(){
	return this.floorBasicInfos;
	}
	public void setId(int id){
	this.id = id;
	}
	public int getId(){
	return this.id;
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
	public void setUiid(int uiid){
	this.uiid = uiid;
	}
	public int getUiid(){
	return this.uiid;
	}
}
