package com.asiainfo.appframe.utils;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.apache.http.entity.StringEntity;

import net.minidev.json.JSONObject;

import com.asiainfo.appframe.CommonApplication;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import android.content.Context;
import android.telephony.TelephonyManager;

public class HttpUtil {
	
	/**
     * 实例化对象
     */
    private static AsyncHttpClient client = new AsyncHttpClient();

    /**
     * 设置链接超时，如果不设置，默认为10s
     */
    static {
        client.setTimeout(15000);
    }

    /**
     * 获取cient对象
     * 
     * @return
     */
    public static AsyncHttpClient getClient() {
        return client;
    }

    /**
     * 取消所有的通信请求
     */
    public static void cancelAllRequests(boolean mayInterruptIfRunning) {
        client.cancelAllRequests(mayInterruptIfRunning);
    }

    /**
     * 取消指定的通信请求
     * 
     * @param context
     */
    public static void cancleRequests(Context context) {
        client.cancelRequests(context, true);
    }

    /**
     * 参数类型为JSONObject的通信请求
     * 
     * @param url
     * @param jsonRequest
     * @param jsonHttpResponseHandler
     */
    public static void post(String url, RequestParams jsonRequest,
            TextHttpResponseHandler jsonHttpResponseHandler) {

//    	 RequestParams requestParams = new RequestParams();
//         if (jsonRequest != null) {
//             for (Entry<String, String> entry : jsonRequest.entrySet()) {
//                 requestParams.put(entry.getKey(), entry.getValue());
//             }
//         }
    	
        client.post(CommonApplication.getInstance(), url, jsonRequest,
                jsonHttpResponseHandler);
    }
    
    /**
     * 参数类型为String的通信请求
     * 
     * @param url
     * @param json
     * @param jsonHttpResponseHandler
     */
    public static void post(String url, String json,
            TextHttpResponseHandler jsonHttpResponseHandler) {

        StringEntity stringEntity = null;
        if (json != null && json.length() > 0) {
            try {
                stringEntity = new StringEntity(json);
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
        }

        client.post(CommonApplication.getInstance(), url, stringEntity,
                "application/json", jsonHttpResponseHandler);

    }

    /**
     * 参数类型为RequestParams的通信请求
     * 
     * @param url
     * @param requestParam
     * @param jsonHttpResponseHandler
     */
//    public static void post(String url, RequestParam requestParam,
//            JsonHttpResponseHandler jsonHttpResponseHandler) {
//
//        RequestParams requestParams = new RequestParams();
//        if (requestParam != null) {
//            Map<String, String> param = requestParam.getParam();
//            for (Entry<String, String> entry : param.entrySet()) {
//                requestParams.put(entry.getKey(), entry.getValue());
//            }
//        }
//
//        client.post(CommonApplication.getInstance(), url, requestParams,
//                jsonHttpResponseHandler);
//    }

    /**
     * 无参数的通信请求
     * 
     * @param url
     * @param jsonHttpResponseHandler
     */
    public static void post(String url,
            JsonHttpResponseHandler jsonHttpResponseHandler) {

        client.post(CommonApplication.getInstance(), url, null,
                "application/json", jsonHttpResponseHandler);
    }
}
