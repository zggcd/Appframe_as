package com.asiainfo.appframe.net.logic;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import com.asiainfo.appframe.net.ApiClient;
import com.asiainfo.appframe.net.ClientRequest;
import com.asiainfo.appframe.utils.CommonUtil;
import com.asiainfo.appframe.utils.DesUtil;
import com.asiainfo.appframe.utils.SDKUtil;
import com.loopj.android.http.RequestParams;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;

public class GetClickUrlRequest  extends ClientRequest {

	private String abilityalias;
	
	private String signature;
	
	public GetClickUrlRequest(Handler handler, int what, String abilityalias) {
		super(handler, what);
		this.abilityalias = abilityalias;
		net.minidev.json.JSONObject userInfo = new net.minidev.json.JSONObject();
		userInfo.put("source", "");
		userInfo.put("source_type", 1);
		userInfo.put("ability", abilityalias);
		userInfo.put("createtime", System.currentTimeMillis());
		Payload payload= new Payload(userInfo);
		JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
		JWSObject jwsObject = new JWSObject(header, payload);
		String secret = SDKUtil.teamKey;
		if(secret == null || secret.length() <= 0){
			String s = "";
			net.minidev.json.JSONObject jsonObj = new net.minidev.json.JSONObject();
			userInfo.put("appsecret", SDKUtil.appSecret);
			Payload pl= new Payload(jsonObj);
			JWSHeader h = new JWSHeader(JWSAlgorithm.HS256);
			JWSObject obj = new JWSObject(h, pl);
//			String secret = "3d990d2276917dfac04467df11fff26d";
			try {
				JWSSigner signer = new MACSigner(SDKUtil.appSecret.getBytes());
				jwsObject.sign(signer);
				String token = obj.serialize();
				System.out.println("Serialised JWS object: " + token);
				s = token;
			} catch (JOSEException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			ApiClient.getTeamKey(myHandler, 2, s);
			return;
		}
		try {
			JWSSigner signer = new MACSigner(secret.getBytes());
			jwsObject.sign(signer);
			String token = jwsObject.serialize();
			System.out.println("Serialised JWS object: " + token);
			signature = token;
		} catch (JOSEException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		formRequest(false, com.asiainfo.appframe.data.Constants.getInstance().getPostNum + 
				"?access_token=" + SDKUtil.accessToken + 
				"&signature=" + signature);
	}

	@Override
	protected RequestParams appendMainBody() {
		// TODO Auto-generated method stub
		RequestParams requestParams = new RequestParams();
		requestParams.put("abilityalias", abilityalias);
		return requestParams;
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
		msg.obj = obj.opt("data");
		handler.sendMessage(msg);
	}
	
	@Override
	public void onPostFail(Exception e) {
		// TODO Auto-generated method stub
		super.onPostFail(e);
	}
	
	//Handler  
    @SuppressLint("HandlerLeak")
	private Handler myHandler = new Handler(){  
  
        @Override  
        public void handleMessage(Message msg) {  
            // TODO Auto-generated method stub  
            super.handleMessage(msg);
            switch (msg.what) {
			case 1:
				
				break;
			case 2:			//获取teamKey
				JSONObject result = (JSONObject) msg.obj;
				int code = (int) result.opt("code");
				if(code == 1){
					String teamKey = (String) result.opt("key");
					
					try {
						teamKey = DesUtil.ECBDecryptHex(teamKey, SDKUtil.appSecret);
						SDKUtil.teamKey = teamKey;
					} catch (Exception e) {
						
						e.printStackTrace();
					}
				}else{
					
				}
				break;
			default:
				break;
			}
            
        }  
        
    }; 

}
