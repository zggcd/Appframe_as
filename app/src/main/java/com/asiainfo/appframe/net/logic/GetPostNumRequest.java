package com.asiainfo.appframe.net.logic;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.asiainfo.appframe.CommonApplication;
import com.asiainfo.appframe.net.ClientRequest;
import com.asiainfo.appframe.utils.SDKUtil;
import com.loopj.android.http.RequestParams;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;

public class GetPostNumRequest extends ClientRequest {

	private String abilityalias;
	private TextView tv;
	
	private String signature;
	
	public GetPostNumRequest(Handler handler, int what, String abilityalias, TextView tv) {
		super(handler, what);
		this.abilityalias = abilityalias;
		this.tv = tv;
		
		net.minidev.json.JSONObject userInfo = new net.minidev.json.JSONObject();
		userInfo.put("source", "");
		userInfo.put("source_type", 1);
		userInfo.put("ability", abilityalias);
		userInfo.put("createtime", 1490232247);
		Payload payload= new Payload(userInfo);
		JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
		JWSObject jwsObject = new JWSObject(header, payload);
		String secret = "3d990d2276917dfac04467df11fff26d";
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
		
		formRequest(false, SDKUtil.GetPostNum +
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
		try {
			JSONObject obj = new JSONObject(resultStr);
			if(resultStr.contains("number")){
			
				final int num = ((JSONObject)obj.opt("data")).getInt("number");
				tv.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						tv.setText(num + "");
					}
				});
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Message msg = Message.obtain();
		msg.what = what;
		handler.sendMessage(msg);
	}
	
	@Override
	public void onPostFail(Exception e) {
		// TODO Auto-generated method stub
		super.onPostFail(e);
	}

}
