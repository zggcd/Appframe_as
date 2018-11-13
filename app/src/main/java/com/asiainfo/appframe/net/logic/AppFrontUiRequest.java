package com.asiainfo.appframe.net.logic;

import android.os.Handler;
import android.os.Message;

import com.asiainfo.appframe.net.ClientRequest;
import com.asiainfo.appframe.net.logic.msgpush.MsgPushClientRequest;
import com.asiainfo.appframe.utils.HttpUtil;
import com.asiainfo.appframe.utils.RSAUtil;
import com.asiainfo.appframe.utils.SDKUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

public class AppFrontUiRequest extends ClientRequest {
    private String accesstoken;

    public AppFrontUiRequest(String url, Handler handler, int what, String accesstoken) {
        // TODO Auto-generated constructor stub
        super(handler, what);
        this.accesstoken = accesstoken;
        formRequest(false, url);
    }

    @Override
    protected void addHeader() {
        super.addHeader();
        AsyncHttpClient client= HttpUtil.getClient();
//        client.addHeader("Content-Type", "application/json");
    }

    @Override
    protected RequestParams appendMainBody() {

        RequestParams params = new RequestParams();
        try {
            params.put("access_token", accesstoken);
            return  params;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onPostSuccess(String resultStr) {
        // TODO Auto-generated method stub

        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = resultStr;
        handler.sendMessage(msg);
    }

    @Override
    public void onPostFail(Exception e) {
        // TODO Auto-generated method stub
        super.onPostFail(e);
    }
}
