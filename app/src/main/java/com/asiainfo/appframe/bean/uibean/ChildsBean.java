package com.asiainfo.appframe.bean.uibean;

import java.util.List;

public class ChildsBean {
    /**
     * "alias": "todo",
     * attrVals : [{"alias":"subHead","definitionId":110,"eleId":853,"frozen":0,"id":1088,"name":"副标题","roleIndivid":0,"stype":1,"type":1,"typeId":108,"userIndivid":0,"val":"详细的订购数据"},{"alias":"InterfaceUrl","definitionId":121,"eleId":853,"frozen":0,"id":1089,"name":"取数接口地址","roleIndivid":0,"stype":1,"type":2,"typeId":108,"userIndivid":0,"val":"http://jiaofeng.tunnel.testpub.net/gateway/statistic/ablityCensus/year"},{"alias":"period","definitionId":122,"eleId":853,"frozen":0,"id":1090,"name":"取数周期","roleIndivid":0,"stype":1,"type":1,"typeId":108,"userIndivid":0,"val":"900"},{"alias":"chartType","definitionId":123,"eleId":853,"frozen":0,"id":1091,"name":"图表类型","roleIndivid":0,"stype":1,"type":1,"typeId":108,"userIndivid":0,"val":"2"}]
     * clickUrl : http://zc.testpub.net/files/afportal/6220bbefb941400db566242b22ca4ee0.png
     * code : FLOOR-ENTRY-ITEM-1
     * cssId : 108
     * id : 853
     * imgUrl : http://zc.testpub.net/files/afportal/6220bbefb941400db566242b22ca4ee0.png
     * name : 订购战报
     * parentId : 852
     * sort : 1
     * type : 3
     */
    private String alias;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    private String clickUrl;
    private String code;
    private int cssId;
    private int id;
    private String imgUrl;
    private String name;
    private int parentId;
    private int sort;
    private int type;
    private List<AttrValsBeanX> attrVals;

    public String getClickUrl() {
        return clickUrl;
    }

    public void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCssId() {
        return cssId;
    }

    public void setCssId(int cssId) {
        this.cssId = cssId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<AttrValsBeanX> getAttrVals() {
        return attrVals;
    }

    public void setAttrVals(List<AttrValsBeanX> attrVals) {
        this.attrVals = attrVals;
    }


}