package com.asiainfo.appframe.activity;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Secure;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.asiainfo.appframe.CommonApplication;
import com.asiainfo.appframe.R;
import com.asiainfo.appframe.bean.AccessTokenResponse;
import com.asiainfo.appframe.bean.AreaCodeInfo;
import com.asiainfo.appframe.bean.AreaCodeListResponse;
import com.asiainfo.appframe.bean.AreaCodes;
import com.asiainfo.appframe.bean.LoginPage;
import com.asiainfo.appframe.bean.SDKAccessTokenResponse;
import com.asiainfo.appframe.dialog.BottomAnimDialog;
import com.asiainfo.appframe.net.ApiClient;
import com.asiainfo.appframe.net.logic.ValidateCodeResponse;
import com.asiainfo.appframe.utils.CommonUtil;
import com.asiainfo.appframe.utils.SDKAuthCallBack;
import com.asiainfo.appframe.utils.SDKUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

public class LoginActivity extends BaseActivity implements SDKAuthCallBack{

//	private static String TAG = "LoginActivity";
	private Context mContext;
	
	private int TYPE_LAYOUT = 0;
	
	//Data
	int[] wh;
	private int top_width;
	private int top_height;
	private boolean IS_AOTU_LOGIN = false;
	SharedPreferences sp;
	private String phone_num = "";
	private String pwd;
	private String base_code = "";
	private String mac;
	private String areaCode;			//地区码
	private String staff_id;
	private String staff_code;
	private String accessToken;
	private LoginPage loginPage;		//登录页图片信息（加载页获取）
	
	private MyHandler handler;
	
	//View
	private View view;
	private ImageView iv_top_bg;
	private ImageView iv_logo_bg;
	private ImageView iv_logo;
	private LinearLayout ll_login;
	private EditText mET_phone_num;		//手机号
	private EditText mET_pwd;			//验证码
	private EditText mET_base_code;		//密码
	private ImageView mIV_autologin;		//自动登录按钮
	private Button mBTN_get_validate_code;	//获取验证码按钮
	
	public SDKUtil sdkUtil = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mContext = this;
		sdkUtil = SDKUtil.getInstance(this, this);
		sdkUtil.change(this, this);
		view = View.inflate(mContext, R.layout.appframe_activity_login, null);
		setContentView(view);
//		sdkUtil.getUAccessToken("18800100101", "1001", "zhang", "0255");
		super.onCreate(savedInstanceState);
	}
	
	public int initContentView() {
		// TODO Auto-generated method stub
		
		switch (TYPE_LAYOUT) {
		case 0:
			return R.layout.appframe_activity_login;
		case 1:
			return R.layout.appframe_activity_login;
		default:
			return R.layout.appframe_activity_login;
		}
	}

	@Override
	public void initView() {
		iv_top_bg = (ImageView) findViewById(R.id.iv_top_bg);
		ll_login = (LinearLayout) findViewById(R.id.ll_login);
		mET_phone_num = (EditText) findViewById(R.id.et_phone_num);
		mET_pwd = (EditText) findViewById(R.id.et_pwd);
		mET_base_code = (EditText) findViewById(R.id.et_basecode);
		mIV_autologin = (ImageView) findViewById(R.id.iv_autologin);
		mBTN_get_validate_code = (Button)findViewById(R.id.btn_get_validate_code);
	}

	public static String xml2JSON(String xml){
		try {
			JSONObject obj = XML.toJSONObject(xml);
			String str = ((JSONObject)obj.get("certificate")).getString("certNumber");
			((JSONObject)obj.get("certificate")).remove("certNumber");
			((JSONObject)obj.get("certificate")).put("certNumber", str);
			return obj.toString();
		} catch (JSONException e) {
			System.err.println("xml->json失败" + e.getLocalizedMessage());
			return "";
		}
	}
	
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
//		String IdCodeContent_decryp = "<certificate><partyName>张建</partyName><gender>1</gender><nation>汉</nation><bornDay>19911217</bornDay><certAddress>安徽省来安县独山乡王巷村张郢组２２号</certAddress><certNumber>430321199507184128</certNumber><certOrg>来安县公安局</certOrg><effDate>20110408</effDate><expDate>20210408</expDate><identityPic>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</identityPic></certificate>";
//		String jsonString = xml2JSON(IdCodeContent_decryp);
//		System.out.println(jsonString);
		
		handler = new MyHandler();
		wh = CommonUtil.getWindowSize(mContext);
		sp = getSharedPreferences("APPFRAME_SDK", Context.MODE_PRIVATE);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		loginPage = (LoginPage)bundle.getSerializable("loginPage");
		
		ViewTreeObserver vto  = iv_top_bg.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() { 
            @SuppressWarnings("deprecation")
			@Override  
            public void onGlobalLayout() {
            	iv_top_bg.getViewTreeObserver().removeGlobalOnLayoutListener(this); 
            	top_height = iv_top_bg.getHeight();
            	top_width = iv_top_bg.getWidth();
            	addLogoBg();
            }   
        });
		
        ll_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//登录
				login(true);
			}
		});
        
        mIV_autologin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(IS_AOTU_LOGIN){
					IS_AOTU_LOGIN = false;
					mIV_autologin.setBackgroundResource(R.drawable.appframe_choose_common);
				}else{
					IS_AOTU_LOGIN = true;
					mIV_autologin.setBackgroundResource(R.drawable.appframe_choose_down);
				}
			}
		});
        
        mBTN_get_validate_code.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startVerifyCodeTimer();
				
				sdkUtil.getValidateCode(mET_phone_num.getText().toString().trim());
				
//				//获取验证码接口
//				ApiClient.getValidateCode(handler, 3, mET_phone_num.getText().toString().trim());
			}
		});
		
        IS_AOTU_LOGIN = sp.getBoolean("login_statue", false);
        phone_num = sp.getString("phone_num", "");
        
        mET_phone_num.setText(phone_num);
        mET_pwd.setText(pwd);
        mET_base_code.setText(base_code);
        
        if(IS_AOTU_LOGIN){
        	login(false);
			mIV_autologin.setBackgroundResource(R.drawable.appframe_choose_down);
        }else{
			mIV_autologin.setBackgroundResource(R.drawable.appframe_choose_common);
        }
	}
	
	/**
	 * 是否需要调地区码接口
	 * @param bool
	 */
	private void login(boolean bool){
		phone_num = mET_phone_num.getText().toString().trim();
		pwd = mET_pwd.getText().toString().trim();
		base_code = mET_base_code.getText().toString().trim();
		mac = Secure.getString(getBaseContext().getContentResolver(), Secure.ANDROID_ID);
		
		if(!bool){
			CommonUtil.showCommonProgressDialog(mContext, "登陆中");
			
			sdkUtil.refreshAccessToken();
			
//			ApiClient.refreshAccessToken(handler, 4, 0, phone_num, mac, accessToken, staff_code);
			return;
		}
		
//		if(phone_num == null || phone_num.equals("") || phone_num.length() < 11){
//			Toast.makeText(mContext, "手机号填写错误", Toast.LENGTH_SHORT).show();
//		}else 
		if(phone_num == null || phone_num.equals("")){
			Toast.makeText(mContext, "账号/手机号不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		if(base_code == null || base_code.equals("")){
			Toast.makeText(mContext, "密码不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		if(pwd == null || pwd.equals("") || pwd.length() < 6){
			Toast.makeText(mContext, "验证码填写错误", Toast.LENGTH_SHORT).show();
		}else{
			CommonUtil.showCommonProgressDialog(mContext, "登陆中");			//非自动登录时，要掉获取地区码接口
			if(bool){
				sdkUtil.getAreaCode(phone_num);
//				ApiClient.getAreaCode(handler, 2, phone_num);
			}else{
				//换成refresh_accesstoken
//				ApiClient.getAccessToken(handler, 1, phone_num, pwd, CommonApplication.app_id, areaCode, mac, "sdzfg", CommonApplication.appSecret);
				ApiClient.refreshAccessToken(handler, 4, 0, SDKUtil.phone_num, SDKUtil.mac, SDKUtil.accessToken, staff_code);
			}
			
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	private View addLogoBg(){
		View view = View.inflate(mContext, R.layout.appframe_logo_frame, null);
		
		iv_logo = (ImageView) view.findViewById(R.id.iv_logo);
		
		LayoutParams lp = new LayoutParams(wh[1] / 5 * 2, wh[1] / 5 * 2);
		lp.leftMargin = top_width / 2 - wh[1] / 5;
		lp.topMargin = top_height - wh[1] / 5;
		view.setLayoutParams(lp);
		((RelativeLayout)this.view).addView(view);
		
		Picasso.with(mContext).load(loginPage.getPicurl()).error(R.drawable.appframe_logo_login).into(iv_logo);
		
		return iv_logo_bg;
	}
	
	public void jumpTo(Class<?> c, Bundle bundle){
		Intent intent = new Intent(LoginActivity.this, c);
		if(bundle != null){
			intent.putExtras(bundle);
		}
		startActivity(intent);
		finish();
	}
	
	@SuppressLint("HandlerLeak")
	private class MyHandler extends BaseHandler{
		public MyHandler() {
			
		}
		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			CommonUtil.closeCommonProgressDialog();
			switch (msg.what) {
			case 1:		//登录
				AccessTokenResponse response = (AccessTokenResponse)msg.obj;
				if(response.getMsg() == null){
					
					Editor editor = sp.edit();
					editor.putString("phone_num", phone_num);
					editor.putBoolean("login_statue", IS_AOTU_LOGIN);
					editor.putString("areaCode", areaCode);
					editor.putString("staff_id", staff_id);
					editor.putString("staff_code", staff_code);
					editor.putString("accessToken", response.getAccess_token());
					editor.commit();
					
					SDKUtil.accessToken = response.getAccess_token();
					SDKUtil.expires_in = response.getExpires_in();
					SDKUtil.phone_num = phone_num;
					SDKUtil.mac = mac;
					SDKUtil.start_time = System.currentTimeMillis();
					
//					jumpTo(MainActivity.class, null);
					jumpTo(HomeActivity.class, null);
				}else{
					Toast.makeText(mContext, response.getMsg(), Toast.LENGTH_SHORT).show();
				}
				
				break;
				
			case 2:		//获取地区码
				
				List<AreaCodes> areaCodeInfo = (List<AreaCodes>) msg.obj;
				
				if(areaCodeInfo == null){
					Toast.makeText(mContext,"服务器异常", Toast.LENGTH_SHORT).show();
					return;
				}
				
				final BottomAnimDialog bottom_dialog = new BottomAnimDialog(mContext, areaCodeInfo);
				bottom_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
					
					@Override
					public void onDismiss(DialogInterface dialog) {
						
						AreaCodes authCodes = bottom_dialog.getAreaCodes();
						if(authCodes != null){
							areaCode = authCodes.getAreaCode();
							staff_id = authCodes.getStaffId();
							staff_code = authCodes.getStaffId();
							ApiClient.getAccessToken(handler, 1, phone_num, pwd, base_code, SDKUtil.app_id, areaCode, mac, staff_code, SDKUtil.appSecret, staff_id);
							
						}else{
							//do nothing
						}
					}
				});
				bottom_dialog.show();
				break;
			case 3:		//获取验证码
				ValidateCodeResponse validateCode = (ValidateCodeResponse) msg.obj;
				if(validateCode.getCode() == 1){
					Toast.makeText(mContext, "验证码获取成功", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(mContext, validateCode.getResult(), Toast.LENGTH_SHORT).show();
				}
				break;
				
			case 4:		//刷新accesstoken
				AccessTokenResponse accessTokenResponse = (AccessTokenResponse)msg.obj;
				if(accessTokenResponse.getMsg() == null){
					
					Editor editor = sp.edit();
					editor.putString("phone_num", phone_num);
					editor.putBoolean("login_statue", IS_AOTU_LOGIN);
					editor.putString("areaCode", areaCode);
					editor.putString("staff_code", staff_code);
					editor.putString("accessToken", accessToken);
					editor.commit();
					
					SDKUtil.accessToken = accessTokenResponse.getAccess_token();
					SDKUtil.expires_in = accessTokenResponse.getExpires_in();
					SDKUtil.phone_num = phone_num;
					SDKUtil.mac = mac;
					SDKUtil.start_time = System.currentTimeMillis();
					SDKUtil.staff_code = staff_code;
					
//					jumpTo(MainActivity.class, null);
					jumpTo(HomeActivity.class, null);
				}else{
					Toast.makeText(mContext, accessTokenResponse.getMsg(), Toast.LENGTH_SHORT).show();
				}
			default:
				break;
			}
		}
	}

	/**发送验证码，改变文字时间的计时器*/
	private CountDownTimer timer;
	/**
	 * 发送验证码按钮文字倒计时功能
	 */
	private void startVerifyCodeTimer(){
		mBTN_get_validate_code.setClickable(false);
		mBTN_get_validate_code.setTextColor(Color.parseColor("#C1C1C1"));
		
		mBTN_get_validate_code.setBackgroundResource(R.drawable.appframe_get_validate_code_pressed);
		if(timer == null){
			timer = new CountDownTimer(60*1000, 1000) {
				@Override
				public void onTick(long millisUntilFinished) {
					mBTN_get_validate_code.setText("已发送("+(millisUntilFinished/1000)+")");
				}
				@Override
				public void onFinish() {
					mBTN_get_validate_code.setClickable(true);
					mBTN_get_validate_code.setText("发送验证码");
					mBTN_get_validate_code.setTextColor(Color.parseColor("#ffffff"));
					
                    // 将发送验证码的式样改为圆角
                    // buttonSendCaptcha.setBackgroundColor(Color.parseColor("#42abea"));
					mBTN_get_validate_code.setBackgroundResource(R.drawable.appframe_get_validate_code_nomal);
				}
			};
		}
		timer.cancel();
		timer.start();
	}

	@Override
	public void onValidateCodeCallback(String result) {
		// TODO Auto-generated method stub
		Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onAreaCodeCallback(String result) {
		// TODO Auto-generated method stub
		CommonUtil.closeCommonProgressDialog();
		
		Gson gson = new Gson();
		AreaCodeListResponse areaCodeListResponse = gson.fromJson(result, AreaCodeListResponse.class);
		
		if(areaCodeListResponse.getCode() == 1){
			
			List<AreaCodes> areaCodeInfo = areaCodeListResponse.getAreaCodes();
		
			if(areaCodeInfo == null){
				Toast.makeText(mContext,"服务器异常", Toast.LENGTH_SHORT).show();
				return;
			}
			
			final BottomAnimDialog bottom_dialog = new BottomAnimDialog(mContext, areaCodeInfo);
			bottom_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
				
				@Override
				public void onDismiss(DialogInterface dialog) {
					
					AreaCodes authCodes = bottom_dialog.getAreaCodes();
					if(authCodes != null){
						sdkUtil.getAccessToken(phone_num, pwd, base_code, authCodes.getAreaCode());
						
					}else{
					}
					
//					AreaCodes authCodes = bottom_dialog.getAreaCodes();
//					if(authCodes != null){
//						areaCode = authCodes.getAreaCode();
//						staff_id = authCodes.getStaffId();
//						staff_code = authCodes.getStaffId();
//						ApiClient.getAccessToken(handler, 1, phone_num, pwd, CommonApplication.app_id, areaCode, mac, staff_code, CommonApplication.appSecret, staff_id);
//						
//					}else{
//						//do nothing
//					}
				}
			});
			bottom_dialog.show();
		}else{
			Toast.makeText(mContext, areaCodeListResponse.getMsg(), Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onAccessTokenCallback(String result) {
		Gson gson = new Gson();
		SDKAccessTokenResponse response = gson.fromJson(result, SDKAccessTokenResponse.class);
		if(response.getCode() == 1){
			Editor editor = sp.edit();
			editor.putBoolean("login_statue", IS_AOTU_LOGIN);
			editor.commit();
			SDKUtil.start_time = System.currentTimeMillis();
			SDKUtil.expires_in = response.getExpires_in();
//			sdkUtil.jumpToWebWindow("http://zc.testpub.net/files/nativeJsBridgeDemo/demo.html");
//			sdkUtil.jumpToWebWindow("http://61.160.128.138:9512/files/nativeJsBridgeDemo/1.0.0/demo.html");
//			jumpTo(MainActivity.class, null);
			jumpTo(HomeActivity.class, null);
		} else{
			Toast.makeText(mContext, response.getMsg(), Toast.LENGTH_SHORT).show();
		}
		
	}

	@Override
	public void onRefreshAccessTokenCallback(String result) {
		// TODO Auto-generated method stub
		CommonUtil.closeCommonProgressDialog();
		Gson gson = new Gson();
		AccessTokenResponse accessTokenResponse = gson.fromJson(result, AccessTokenResponse.class);
		if(accessTokenResponse.getCode() == 1){
			SDKUtil.start_time = System.currentTimeMillis();
			SDKUtil.expires_in = accessTokenResponse.getExpires_in();
//			jumpTo(MainActivity.class, null);
			jumpTo(HomeActivity.class, null);
		}else{
			Toast.makeText(mContext, accessTokenResponse.getMsg(), Toast.LENGTH_SHORT).show();
		}
		
	}

	@Override
	public void onError(String errMessage) {
		// TODO Auto-generated method stub
		CommonUtil.closeCommonProgressDialog();
		Toast.makeText(mContext, errMessage, Toast.LENGTH_SHORT).show();
	}
	
}
