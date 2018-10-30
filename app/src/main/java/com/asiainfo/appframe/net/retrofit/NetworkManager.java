package com.asiainfo.appframe.net.retrofit;

import com.asiainfo.appframe.net.retrofit.request.Request;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络控制层
 */
public class NetworkManager {

    private static NetworkManager mInstance = null;

    public static NetworkManager getInstance() {
        if(mInstance == null){
            synchronized (NetworkManager.class){
                if (mInstance == null){
                    mInstance = new NetworkManager();
                }
            }
        }
        return mInstance;
    }

    Gson gson = null;
    private static Retrofit retrofit= null;
    private static OkHttpClient client = null;
    private static volatile Request request = null;
    LoginService loginService = null;

    private NetworkManager(){
        gson = new GsonBuilder()
                .setDateFormat("yy-MM-dd hh:mm:ss")
                .create();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        //初始化OkHttp
        client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(Request.HOST)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        loginService = retrofit.create(LoginService.class);
    }

    public static Request getRequest(){
        if(request == null){
            synchronized (Request.class){
                request = retrofit.create(Request.class);
            }
        }
        return request;
    }

//    public void getValidateCodeRequest(String phone_num, String app_id){
//        Call<ResponseBody> call = loginService.getValidateCode(phone_num, app_id);
//
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                response.body().toString();
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                t.printStackTrace();
//            }
//        });
//    }

}
