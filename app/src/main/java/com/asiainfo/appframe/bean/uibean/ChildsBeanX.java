package com.asiainfo.appframe.bean.uibean;

import java.util.List;

public class ChildsBeanX {
    /**
     * childs : [{"attrVals":[{"alias":"subHead","definitionId":110,"eleId":853,"frozen":0,"id":1088,"name":"副标题","roleIndivid":0,"stype":1,"type":1,"typeId":108,"userIndivid":0,"val":"详细的订购数据"},{"alias":"InterfaceUrl","definitionId":121,"eleId":853,"frozen":0,"id":1089,"name":"取数接口地址","roleIndivid":0,"stype":1,"type":2,"typeId":108,"userIndivid":0,"val":"http://jiaofeng.tunnel.testpub.net/gateway/statistic/ablityCensus/year"},{"alias":"period","definitionId":122,"eleId":853,"frozen":0,"id":1090,"name":"取数周期","roleIndivid":0,"stype":1,"type":1,"typeId":108,"userIndivid":0,"val":"900"},{"alias":"chartType","definitionId":123,"eleId":853,"frozen":0,"id":1091,"name":"图表类型","roleIndivid":0,"stype":1,"type":1,"typeId":108,"userIndivid":0,"val":"2"}],"clickUrl":"http://zc.testpub.net/files/afportal/6220bbefb941400db566242b22ca4ee0.png","code":"FLOOR-ENTRY-ITEM-1","cssId":108,"id":853,"imgUrl":"http://zc.testpub.net/files/afportal/6220bbefb941400db566242b22ca4ee0.png","name":"订购战报","parentId":852,"sort":1,"type":3},{"attrVals":[{"alias":"subHead","definitionId":110,"eleId":854,"frozen":0,"id":1092,"name":"副标题","roleIndivid":0,"stype":1,"type":1,"typeId":108,"userIndivid":0,"val":"收益明细 早知道"},{"alias":"InterfaceUrl","definitionId":121,"eleId":854,"frozen":0,"id":1093,"name":"取数接口地址","roleIndivid":0,"stype":1,"type":2,"typeId":108,"userIndivid":0,"val":"http://jiaofeng.tunnel.testpub.net/gateway/statistic/ablityCensus/year"},{"alias":"period","definitionId":122,"eleId":854,"frozen":0,"id":1094,"name":"取数周期","roleIndivid":0,"stype":1,"type":1,"typeId":108,"userIndivid":0,"val":"900"},{"alias":"chartType","definitionId":123,"eleId":854,"frozen":0,"id":1095,"name":"图表类型","roleIndivid":0,"stype":1,"type":1,"typeId":108,"userIndivid":0,"val":"2"}],"clickUrl":"http://zc.testpub.net/files/afportal/13aa883ceb364911958c9b36b1eec026.png","code":"FLOOR-ENTRY-ITEM-1","cssId":108,"id":854,"imgUrl":"http://zc.testpub.net/files/afportal/13aa883ceb364911958c9b36b1eec026.png","name":"营销收益","parentId":852,"sort":2,"type":3}]
     * code : FLOOR-ENTRY-BAR-1
     * cssId : 107
     * id : 852
     * name : 便捷服务入口
     * parentId : 851
     * sort : 3
     * type : 2
     */

    private String code;
    private int cssId;
    private int id;
    private String name;
    private int parentId;
    private int sort;
    private int type;
    private List<ChildsBean> childs;

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

    public List<ChildsBean> getChilds() {
        return childs;
    }

    public void setChilds(List<ChildsBean> childs) {
        this.childs = childs;
    }


}