package com.asiainfo.appframe.bean;

import java.util.List;

public class UiDtoBean {
    private List<FootbarsBean> footbars;
    private List<HomePageDtoListBean> homePageDtoList;

    public List<FootbarsBean> getFootbars() {
        return footbars;
    }

    public void setFootbars(List<FootbarsBean> footbars) {
        this.footbars = footbars;
    }

    public List<HomePageDtoListBean> getHomePageDtoList() {
        return homePageDtoList;
    }

    public void setHomePageDtoList(List<HomePageDtoListBean> homePageDtoList) {
        this.homePageDtoList = homePageDtoList;
    }
}
