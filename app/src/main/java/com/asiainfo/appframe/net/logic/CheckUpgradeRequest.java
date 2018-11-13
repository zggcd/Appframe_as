package com.asiainfo.appframe.net.logic;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.asiainfo.appframe.net.ClientRequest;
import com.asiainfo.appframe.utils.SDKUtil;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

public class CheckUpgradeRequest extends ClientRequest {


    public CheckUpgradeRequest(Handler handler, int what, String curVersionNum) {
        super(handler, what);
        formRequest(false, SDKUtil.queryVersionUpdateInfoUrl
                + "appKey=u09a18aq&platformCode=Android" + "&curVersionNum=" + curVersionNum);
    }

    /**
     * 拼装请求体
     */
    @Override
    protected RequestParams appendMainBody() {
        return null;
    }

    @Override
    public void onPostSuccess(String resultStr) {
        // TODO Auto-generated method stub
        JSONObject obj = null;
        try {
            obj = new JSONObject(resultStr);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = obj;
        handler.sendMessage(msg);
    }

    @Override
    public void onPostFail(Exception e) {
        // TODO Auto-generated method stub
        super.onPostFail(e);
        Message msg = Message.obtain();
        msg.what = -1;
        msg.obj = e.getMessage();
        handler.sendMessage(msg);
    }

}
