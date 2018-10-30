package com.asiainfo.appframe.net.logic;

import android.os.Handler;
import android.os.Message;

import com.asiainfo.appframe.data.Constants;
import com.asiainfo.appframe.net.logic.msgpush.MsgPushClientRequest;
import com.asiainfo.appframe.utils.HttpUtil;
import com.asiainfo.appframe.utils.RSAUtil;
import com.asiainfo.appframe.utils.SDKUtil;
import com.loopj.android.http.AsyncHttpClient;

import org.json.JSONObject;

public class ModifyPwdRequest extends MsgPushClientRequest {
    private String accesstoken;		//accesstoken
    private String password;				//旧密码
    private String nPassword;	//新密码
    private int First;	//新密码

    public ModifyPwdRequest(String url, Handler handler, int what, String accesstoken, String password, String nPassword, int First) {
        // TODO Auto-generated constructor stub
        super(handler, what);
        this.accesstoken = accesstoken;
        this.password = password;
        this.nPassword = nPassword;
        this.First = First;
        formRequest(false, url);
    }

    @Override
    protected void addHeader() {
        super.addHeader();
        AsyncHttpClient client= HttpUtil.getClient();
        client.addHeader("Content-Type", "application/json");
    }

    @Override
    protected String appendMainBody() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("accesstoken", accesstoken);
            jsonObject.put("password", RSAUtil.getEnCodeString(password, SDKUtil.key_public));
            jsonObject.put("nPassword", RSAUtil.getEnCodeString(nPassword, SDKUtil.key_public));
            jsonObject.put("First", First);
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
