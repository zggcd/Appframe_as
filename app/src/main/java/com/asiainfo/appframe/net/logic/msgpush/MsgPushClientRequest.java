package com.asiainfo.appframe.net.logic.msgpush;

import org.apache.http.Header;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.asiainfo.appframe.net.RequestException;
import com.asiainfo.appframe.net.RequestResultCallback;
import com.asiainfo.appframe.utils.HttpUtil;
import com.asiainfo.appframe.utils.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

public abstract class MsgPushClientRequest implements RequestResultCallback {

	protected Handler handler;
	protected int what;
	
	protected Gson gson;
	protected String url;
	
	public static final String TAG = "ClientRequest";
	
	public MsgPushClientRequest(Handler handler, int what){
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
		this.url = url;
		
		// 添加通信请求的头
        addHeader();
    }
	
    /**
     * 添加通信请求的头
     */
    protected void addHeader() {
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
				Log.d("Appframe", "== response ==>" +response );
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
        	@SuppressWarnings("unused")
			String params = appendMainBody();
        	Log.d("Appframe", "== url ==>" + url );
            Log.d("Appframe", "== params ==>" +params );
            HttpUtil.post(url, appendMainBody(), jsonHttpResponseHandler);

    }

    public MsgPushClientRequest(Handler handler, int wwhat, Context ctx) {
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
	protected abstract String appendMainBody();
}
