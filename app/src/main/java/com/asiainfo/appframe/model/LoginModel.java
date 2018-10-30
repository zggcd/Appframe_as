package com.asiainfo.appframe.model;

import com.asiainfo.appframe.net.retrofit.NetworkManager;
import com.asiainfo.appframe.net.retrofit.response.Response;

import java.util.List;

import io.reactivex.Observable;

public class LoginModel {

    /**
     * 获取手机验证码
     * @param phone_num
     * @param app_id
     * @return
     */
    public Observable<Response<String>> getAuthCode(String phone_num, String app_id){
        return NetworkManager.getRequest().getAuthCode(phone_num, app_id);
    }

}
