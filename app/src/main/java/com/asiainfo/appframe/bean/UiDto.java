package com.asiainfo.appframe.bean;

import java.util.List;

public class UiDto {
	
//	"homePageDtoList": []
	
	private List<Footbars> footbars ;
	
	//首页信息
	private List<HomePageDtoList> homePageDtoList ;
	
	//过场动画信息
	private Cutscenes cutscenes;
	
	//登录页信息
	private LoginPage loginPage;

	public List<Footbars> getFootbars() {
		return footbars;
	}
	public void setFootbars(List<Footbars> footbars) {
		this.footbars = footbars;
	}
	public Cutscenes getCutscenes() {
		return cutscenes;
	}
	public void setCutscenes(Cutscenes cutscenes) {
		this.cutscenes = cutscenes;
	}
	public LoginPage getLoginPage() {
		return loginPage;
	}
	public void setLoginPage(LoginPage loginPage) {
		this.loginPage = loginPage;
	}
	public void setHomePageDtoList(List<HomePageDtoList> homePageDtoList){
	this.homePageDtoList = homePageDtoList;
	}
	public List<HomePageDtoList> getHomePageDtoList(){
	return this.homePageDtoList;
	}
}
