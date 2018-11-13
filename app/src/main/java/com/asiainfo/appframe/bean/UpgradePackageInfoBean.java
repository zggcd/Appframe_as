package com.asiainfo.appframe.bean;

public class UpgradePackageInfoBean {

    /**
     * appName : 工作助手
     * isForce : true
     * platformName : Android
     * updateLink : http://ztyx.telecomjs.com/ecs_appmanager/manage/version/requestVersionFile.do?versionId=6273
     * versionLog : 啊啊啊啊啊啊啊啊啊啊
     * versionName : 1.1
     * versionNum : 1.1
     * versionSize : 9283966
     */

    private String appName;
    private boolean isForce;
    private String platformName;
    private String updateLink;
    private String versionLog;
    private String versionName;
    private String versionNum;
    private String versionSize;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public boolean isIsForce() {
        return isForce;
    }

    public void setIsForce(boolean isForce) {
        this.isForce = isForce;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getUpdateLink() {
        return updateLink;
    }

    public void setUpdateLink(String updateLink) {
        this.updateLink = updateLink;
    }

    public String getVersionLog() {
        return versionLog;
    }

    public void setVersionLog(String versionLog) {
        this.versionLog = versionLog;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionNum() {
        return versionNum;
    }

    public void setVersionNum(String versionNum) {
        this.versionNum = versionNum;
    }

    public String getVersionSize() {
        return versionSize;
    }

    public void setVersionSize(String versionSize) {
        this.versionSize = versionSize;
    }

}
