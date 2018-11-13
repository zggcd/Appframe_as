package com.asiainfo.appframe.activity;

import com.asiainfo.appframe.AppManager;
import com.asiainfo.appframe.utils.CommonUtil;
import com.asiainfo.appframe.utils.SDKUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public abstract class BaseActivity extends Activity {
	
//	private static final String TAG = "BaseActivity";
	
	protected static Context mContext;
	
	protected int[] screenWH;				//屏幕高宽
	
	//定时任务
//	private static ScheduledExecutorService scheduledExecutorService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mContext = this;
		screenWH = CommonUtil.getWindowSize(mContext);
		AppManager.addActivity(this);
		initView();
		initData();
	}
	
	public abstract void initView();
	
	public abstract void initData();
	
	
//	public void startTimeTask() {
//		
//		
//		final MyHandler handler = new MyHandler();
//		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
//		scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
//			
//			@Override
//			public void run() {
//				ApiClient.refreshAccessToken(handler, 1, phone_num, mac, accessToken, "sdhflad");
//			}
//		}, Integer.parseInt("10"), Integer.parseInt("10"), TimeUnit.SECONDS);
//	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		AppManager.finishActivity(this);
	}
	
	public class BaseHandler extends Handler{
		@SuppressLint("HandlerLeak")
		public BaseHandler() {
			
		}
		
		@SuppressLint("ShowToast")
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			CommonUtil.closeCommonProgressDialog();
			switch (msg.what) {
			case 0:
				Toast.makeText(mContext, (String)msg.obj, Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		}
	}

	public void jumpTo(Class<?> c, Bundle bundle){
		Intent intent = new Intent(BaseActivity.this, c);
		if(bundle != null){
			intent.putExtras(bundle);
		}
		startActivity(intent);
		finish();
	}

}
