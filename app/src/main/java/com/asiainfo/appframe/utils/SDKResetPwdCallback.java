package com.asiainfo.appframe.utils;

/**
 * 重置密码
 */
public interface SDKResetPwdCallback {

    /**
     * 获取重置密码的验证码
     * @param result
     */
    void onAuthCodeSuccess(String result);

    /**
     * 重置密码返回
     * @param result
     */
    void onResetPwdSuccess(String result);

}
