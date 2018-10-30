package com.asiainfo.appframe.net.retrofit.request;

import com.asiainfo.appframe.net.retrofit.response.Response;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Request {

    public static String HOST = "http://61.160.128.138:9512/gateway/";

    @POST("auth/getAuthCode")
    Observable<Response<String>> getAuthCode(@Query("phone_num") String phone_num, @Query("app_id") String app_id);

}
