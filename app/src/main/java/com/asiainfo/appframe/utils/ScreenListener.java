package com.asiainfo.appframe.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by stiven on 2017/8/8 0008.
 * 屏幕状态监听
 */

public class ScreenListener {

    private Context mContext;
    private ScreenBroadcastReceiver mScreenReceiver;
    private ScreenStateListener mScreenStateListener;

    public ScreenListener(Context context){
        this.mContext = context;
        this.mScreenReceiver = new ScreenBroadcastReceiver();

    }

    public void setScreenStateListener(ScreenStateListener listener){
        mScreenStateListener = listener;
        registerListener();
    }

    private void registerListener(){
        IntentFilter filer = new IntentFilter();
        filer.addAction(Intent.ACTION_SCREEN_ON);
        filer.addAction(Intent.ACTION_SCREEN_OFF);
        filer.addAction(Intent.ACTION_USER_PRESENT);
        mContext.registerReceiver(mScreenReceiver, filer);
    }

    private class ScreenBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(Intent.ACTION_SCREEN_ON.equals(action)){
                mScreenStateListener.onScreenOn();
            }else if(Intent.ACTION_SCREEN_OFF.equals(action)){
                mScreenStateListener.onScreenOff();
            }else if(Intent.ACTION_USER_PRESENT.equals(action)){
                mScreenStateListener.onUserPresent();
            }
        }
    }

    public interface ScreenStateListener{
        public void onScreenOn();//开屏
        public void onScreenOff();//锁屏
        public void onUserPresent();//解锁
    }

}
