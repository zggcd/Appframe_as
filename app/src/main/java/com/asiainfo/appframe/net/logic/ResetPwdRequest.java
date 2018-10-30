package com.asiainfo.appframe.net.logic;

import android.os.Handler;
import android.os.Message;

import com.asiainfo.appframe.data.Constants;
import com.asiainfo.appframe.net.ClientRequest;
import com.asiainfo.appframe.net.logic.msgpush.MsgPushClientRequest;
import com.asiainfo.appframe.utils.HttpUtil;
import com.asiainfo.appframe.utils.RSAUtil;
import com.asiainfo.appframe.utils.SDKUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

public class ResetPwdRequest extends MsgPushClientRequest {
    private String username;		//用户手机号
    private String smscode;				//机器码作为唯一标识
    private String nPassword;	//accesstoken

    public ResetPwdRequest(Handler handler, int what, String username, String smscode, String nPassword) {
        // TODO Auto-generated constructor stub
        super(handler, what);
        this.username = username;
        this.smscode = smscode;
        this.nPassword = nPassword;
        formRequest(false, Constants.getInstance().refreshAccessToken);
    }

    public ResetPwdRequest(String url, Handler handler, int what, String username, String smscode, String nPassword) {
        // TODO Auto-generated constructor stub
        super(handler, what);
        this.username = username;
        this.smscode = smscode;
        this.nPassword = nPassword;
        formRequest(false, url);
    }

    @Override
    protected void addHeader() {
        super.addHeader();
        AsyncHttpClient client= HttpUtil.getClient();
        client.addHeader("Content-Type", "application/json");
    }

    public ResetPwdRequest(Handler handler, int what) {
        super(handler, what);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected String appendMainBody() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", RSAUtil.getEnCodeString(username, SDKUtil.key_public));
            jsonObject.put("smscode", RSAUtil.getEnCodeString(smscode, SDKUtil.key_public));
            jsonObject.put("nPassword", RSAUtil.getEnCodeString(nPassword, SDKUtil.key_public));
            return  jsonObject.toString();
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
