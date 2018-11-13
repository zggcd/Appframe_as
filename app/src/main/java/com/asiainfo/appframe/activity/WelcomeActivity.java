package com.asiainfo.appframe.activity;

import java.util.List;

import org.json.JSONObject;

import com.asiainfo.appframe.CommonApplication;
import com.asiainfo.appframe.R;
import com.asiainfo.appframe.bean.AccessTokenResponse;
import com.asiainfo.appframe.bean.AreaCodes;
import com.asiainfo.appframe.bean.Cutscenes;
import com.asiainfo.appframe.bean.HomePageInfo;
import com.asiainfo.appframe.bean.LoginPage;
import com.asiainfo.appframe.bean.MsgPushAuthInfo;
import com.asiainfo.appframe.bean.MsgPushAuthResult;
import com.asiainfo.appframe.msgpush.Client;
import com.asiainfo.appframe.net.ApiClient;
import com.asiainfo.appframe.net.logic.ValidateCodeResponse;
import com.asiainfo.appframe.permission.AddPermission;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import com.asiainfo.appframe.utils.*;

import android.widget.Toast;

public class WelcomeActivity extends BaseActivity {
	
//	private static String TAG = "WelcomeActivity";
	private Context mContext;
	
	//view
	private ImageView mIV;
	
	//Handler  
    @SuppressLint("HandlerLeak")
	private Handler handler = new BaseHandler(){
  
        @SuppressLint("ShowToast")
		@Override  
        public void handleMessage(Message msg) {  
            // TODO Auto-generated method stub  
            super.handleMessage(msg);
            switch (msg.what) {
            case 0:
            	finish();
            	break;
			case 1:
				HomePageInfo info = (HomePageInfo) msg.obj;
				if(info.getCode() == 1){
				
					Cutscenes cutscenes = info.getUiDto().getCutscenes();
					final LoginPage loginPage = info.getUiDto().getLoginPage();
					
					if(cutscenes.getCssCode()!= null && cutscenes.getCssCode().length() != 0){
						Picasso.with(mContext).load(cutscenes.getPicurl()).error(R.drawable.appframe_welcome).into(mIV);
					}
					
					new Thread() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							Bundle bundle = new Bundle();
							bundle.putSerializable("loginPage", loginPage);
							
							jumpTo(LoginActivity.class, bundle);
						}
					}.start();
				}else{
//					Toast.makeText(mContext, info.getMsg(), 2000).show();
				}
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
						Toast.makeText(mContext, "系统异常，请检查网络", 2000).show();
						e.printStackTrace();
					}
				}else{
//					Toast.makeText(mContext, (String)result.opt("msg"), 2000).show();
				}
				break;
			case 3:
				MsgPushAuthResult msgPushAuthResult = (MsgPushAuthResult)msg.obj;
				MsgPushAuthInfo msgPushAuthInfo = (MsgPushAuthInfo)msgPushAuthResult.getObject();
				final String token = msgPushAuthInfo.getToken();
				//app_code +’_’ + area_code + ‘_’+staff_id
				final String[] topic = msgPushAuthInfo.getSub_topics().toArray(new String[msgPushAuthInfo.getSub_topics().size() - 1]);
				
				new Thread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Client client = new Client(app_code + "_" + area_code + "_" + staff_id, staff_id, token, topic);
						
						client.start();
					}
					
				}).start();
				
				break;
			default:
				break;
			}
            
        }  
        
    }; 
    
    String app_code = "bss";
	String area_code = "025";
	String staff_id = "KFNJ31288";
	String auth_secret = "09kjei387yfhn98#";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mContext = this;
		setContentView(R.layout.appframe_activity_welcome);
		SDKUtil.getInstance(mContext, null);
		
		super.onCreate(savedInstanceState);

		AddPermission addPermission = new AddPermission((Activity)mContext);
		addPermission.addPermission(new AddPermission.PermissionsListener() {
			@Override
			public void onPermissionListener(boolean hasPermission, int code) {

				String secret = "";
				net.minidev.json.JSONObject userInfo = new net.minidev.json.JSONObject();
				userInfo.put("appsecret", SDKUtil.appSecret);
				Payload payload= new Payload(userInfo);
				JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
				JWSObject jwsObject = new JWSObject(header, payload);
//		String secret = "3d990d2276917dfac04467df11fff26d";
				try {
					JWSSigner signer = new MACSigner(SDKUtil.appSecret.getBytes());
					jwsObject.sign(signer);
					String token = jwsObject.serialize();
					System.out.println("Serialised JWS object: " + token);
					secret = token;
				} catch (JOSEException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				ApiClient.getTeamKey(handler, 2, secret);
				String ip = CommonUtil.getLocalIpAddress();

				int timestamp = (int)System.currentTimeMillis();

				String signature = MD5Util.md5(app_code + area_code + timestamp + staff_id + auth_secret);
			}
		}, AddPermission.CODE_PERMISSIONS_STORAGE);

		
	}
	
	@Override
	public void initView() {
		mIV = (ImageView) findViewById(R.id.iv);
	}

	@Override
	public void initData() {

		AddPermission addPermission = new AddPermission((Activity)mContext);
		addPermission.addPermission(new AddPermission.PermissionsListener() {
			@Override
			public void onPermissionListener(boolean hasPermission, int code) {
				ApiClient.preUiLayout(handler, 1, SDKUtil.app_id);
			}
		}, AddPermission.CODE_PERMISSIONS_STORAGE);
		

	}
	
	public void jumpTo(Class<?> c, Bundle bundle){
		Intent intent = new Intent(WelcomeActivity.this, c);
		if(bundle != null){
			intent.putExtras(bundle);
		}
		startActivity(intent);
		finish();
	}

}
