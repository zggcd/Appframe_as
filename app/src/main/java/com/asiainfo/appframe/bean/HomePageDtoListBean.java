package com.asiainfo.appframe.bean;

import java.util.List;

public class HomePageDtoListBean {
    /**
     * cssCode : FLOOR-ENTRY-1
     * cssid : 24
     * floorBasicInfos : [{"createtime":{"date":7,"day":5,"hours":11,"minutes":26,"month":8,"seconds":49,"time":1536290809000,"timezoneOffset":-480,"year":118},"floorExtends":{"id":40},"homeid":42,"id":40,"picurl":"http://jiaofeng.tunnel.testpub.net/gateway/img/app1/%E8%AE%A2%E8%B4%AD%E6%88%98%E6%8A%A5.png","showhome":1,"sort":1,"subhead":"详细的订购数据","title":"订购战报"},{"createtime":{"date":7,"day":5,"hours":11,"minutes":28,"month":8,"seconds":4,"time":1536290884000,"timezoneOffset":-480,"year":118},"floorExtends":{"id":41},"homeid":42,"id":41,"picurl":"http://jiaofeng.tunnel.testpub.net/gateway/img/app1/%E8%90%A5%E9%94%80%E6%94%B6%E7%9B%8A.png","showhome":1,"sort":2,"subhead":"收益明细 早知道","title":"营销收益"}]
     * id : 42
     * sort : 3
     * status : 1
     * title : 便捷服务入口
     * type : 1
     * uiid : 82
     */

    private String cssCode;
    private int cssid;
    private int id;
    private int sort;
    private int status;
    private String title;
    private int type;
    private int uiid;
    private List<FloorBasicInfosBean> floorBasicInfos;

    public String getCssCode() {
        return cssCode;
    }

    public void setCssCode(String cssCode) {
        this.cssCode = cssCode;
    }

    public int getCssid() {
        return cssid;
    }

    public void setCssid(int cssid) {
        this.cssid = cssid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUiid() {
        return uiid;
    }

    public void setUiid(int uiid) {
        this.uiid = uiid;
    }

    public List<FloorBasicInfosBean> getFloorBasicInfos() {
        return floorBasicInfos;
    }

    public void setFloorBasicInfos(List<FloorBasicInfosBean> floorBasicInfos) {
        this.floorBasicInfos = floorBasicInfos;
    }
}
