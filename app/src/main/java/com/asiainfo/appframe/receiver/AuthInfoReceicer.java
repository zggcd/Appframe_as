package com.asiainfo.appframe.receiver;

import com.asiainfo.appframe.utils.StringUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public abstract class AuthInfoReceicer extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if("com.asiainfo.appframe.955".equals(intent.getAction())){
	            String data=intent.getStringExtra("data");
//	            Log.i("test","02号接收了"+daa);
	            if(!StringUtil.isEmpty(data)){
	            	 onResult(data);
	            }
	    }
	}
	
	public abstract void onResult(String result);

}
