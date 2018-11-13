package com.asiainfo.appframe.net;

import org.apache.http.Header;
import org.json.JSONObject;

import com.asiainfo.appframe.CommonApplication;
import com.asiainfo.appframe.utils.CommonUtil;
import com.asiainfo.appframe.utils.HttpUtil;
import com.asiainfo.appframe.utils.Log;
import com.asiainfo.appframe.utils.MD5Util;
import com.asiainfo.appframe.utils.SDKUtil;
import com.asiainfo.appframe.utils.StringUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * 请求基类
 * @author Stiven
 *
 */
@SuppressWarnings("deprecation")
public abstract class ClientRequest implements RequestResultCallback {

	protected Handler handler;
	protected int what;
	
	protected Gson gson;
	protected String url;
	
	public static final String TAG = "ClientRequest";
	
	public ClientRequest(Handler handler, int what){
		this.handler = handler;
		this.what = what;
		gson = new GsonBuilder().create();
	}
	
	protected void formGetRequest(String url) {
		formRequest(true, url);
	}

	protected void formRequest() {
		formRequest(false, "");
	}

	protected void formRequest(boolean isGet, String url) {
//		String urlStr = Constants.getInstance().url;
//		if (isGet) {
//			urlStr = url;
//		} else {
//			if (url != null && url.length() > 0) {
//				urlStr = url;
//			}
//		}
		this.url = url;
		
		// 添加通信请求的头
        addHeader();
    }
	//请求流水
	protected static String Seq = "";
	//H5能力调用流水：能力调用接口产生的流水号作为同步H5响应信息的父级请求流水在params中上传，
	protected static String H5Seq = "";
	
    /**
     * 添加通信请求的头
     */
    protected void addHeader() {
    	
    	AsyncHttpClient client = HttpUtil.getClient();
        client.removeAllHeaders();
    	client.addHeader("AF-SDKVersion", "Android_V1.2");//SDK版本
    	SDKUtil sdkUtil = SDKUtil.getInstance(null, null);
    	if(sdkUtil != null){
    		String md5 = MD5Util.md5(sdkUtil.uuid);
    		Seq = "01_" + md5.substring(8, 15) + "_" + System.currentTimeMillis() + "_" + StringUtil.getRandomString(4);
    		client.addHeader("AF-RequestSeq", Seq);
    	}

    }
	
    /**
     * 通信请求开始
     */
    public void start() {

        // 通信结果处理
    	TextHttpResponseHandler  jsonHttpResponseHandler = new TextHttpResponseHandler () {

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				Log.d("Appframe", "== response ==>" + arg0 + arg2 );
				RequestException ept = new RequestException();
                ept.setMsg("服务器异常，请稍后再试  " + arg3.getMessage());
                onPostFail(ept);
				
			}

			@Override
			public void onSuccess(int statusCode, Header[] arg1, String response) {
				Log.d("Appframe", "== response ==>" + response );
				switch (statusCode) {
                case 200:
                    onPostSuccess(response.toString());
                    break;

                default:
                    RequestException ept = new RequestException();
                    ept.setMsg("网络连接失败，请检查网络  " + statusCode);
                    onPostFail(ept);
                    break;
                }
				
			}

        };

        // 判断是否有网络连接
//        if (CommonUtil.checkNetworkState(CommonApplication.getInstance())) {
            // 进行请求的通信
        	@SuppressWarnings("unused")
			RequestParams params = appendMainBody();
        	Log.d("Appframe", "== url ==>" + url );
            Log.d("Appframe", "== params ==>" +params );
            HttpUtil.post(url, appendMainBody(), jsonHttpResponseHandler);
           
//        } else {
//            RequestException ept = new RequestException();
//            ept.setMsg("网络未连接，请检查网络");
//            onPostFail(ept);
//        }

    }

    public ClientRequest(Handler handler, int wwhat, Context ctx) {
        this.handler = handler;
        this.what = wwhat;
        gson = new GsonBuilder().create();
    }

    @Override
    public void onPostSuccess(String resultStr) {
//        reusltStrObj = gson.fromJson(resultStr, ResultStr.class);
    }

    @Override
    public void onPostFail(Exception e) {
        String errorMsg = ((RequestException) e).getMsg();
        Bundle data = new Bundle();
        data.putString("errorMsg", errorMsg);
        // Message msg = handler.obtainMessage();
        Message msg = new Message();
        msg.arg1 = -1;
        msg.what = 0;
        msg.obj = errorMsg;
        handler.sendMessage(msg);
        
    }
    
    /**
	 * 拼装请求体
	 */
	protected abstract RequestParams appendMainBody();
    
}
