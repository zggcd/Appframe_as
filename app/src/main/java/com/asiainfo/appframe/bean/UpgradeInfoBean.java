package com.asiainfo.appframe.bean;

public class UpgradeInfoBean {


    /**
     * errorMessage :
     * resultCode : 0000
     * resultObj : {"appName":"工作助手","isForce":true,"platformName":"Android","updateLink":"http://ztyx.telecomjs.com/ecs_appmanager/manage/version/requestVersionFile.do?versionId=6273","versionLog":"啊啊啊啊啊啊啊啊啊啊","versionName":"1.1","versionNum":"1.1","versionSize":"9283966"}
     */

    private String errorMessage;
    private String resultCode;
    private UpgradePackageInfoBean resultObj;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public UpgradePackageInfoBean getResultObj() {
        return resultObj;
    }

    public void setResultObj(UpgradePackageInfoBean resultObj) {
        this.resultObj = resultObj;
    }
}
