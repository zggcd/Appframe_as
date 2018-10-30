package com.asiainfo.appframe.net.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface LoginService {

    @POST("auth/getAuthCode")
    Call<ResponseBody> getValidateCode(@Path("phone_num") String phone_num, @Path("app_id") String app_id);

}
