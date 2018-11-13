package com.asiainfo.appframe.utils;

public interface SDKQrcodeCallback {

    /**
     * 扫码返回
     * @param result
     */
    void onQrcodeScanCallback(String result);

    /**
     * 扫码登录确认返回
     * @param result
     */
    void onQrcodeLoginCallback(String result);

    /**
     * 扫码登录取消返回
     * @param result
     */
    void onQrcodeCancelCallback(String result);

}
