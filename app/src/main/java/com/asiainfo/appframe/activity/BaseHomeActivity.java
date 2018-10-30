package com.asiainfo.appframe.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.asiainfo.appframe.CommonApplication;
import com.asiainfo.appframe.R;
import com.asiainfo.appframe.data.Constants;
import com.asiainfo.appframe.utils.CommonUtil;
import com.asiainfo.appframe.utils.Log;
import com.asiainfo.appframe.utils.ScreenListener;

import java.io.File;
import java.util.logging.Logger;

public abstract class BaseHomeActivity extends AppCompatActivity {
    // TAG
    public static final String TAG = "BaseHomeActivity";

    /**
     * 初始化组件
     */
    public abstract void initView();

    /**
     * 初始化数据
     */
    public abstract void initData();

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    public Context mContext;

    private FragmentManager mFragmentManager;
    private ScreenListener mScreenListener;

    public CommonApplication application;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        application = (CommonApplication) getApplication();
        this.mFragmentManager= getFragmentManager();
        this.mScreenListener = new ScreenListener(this);
        mScreenListener.setScreenStateListener(new ScreenListener.ScreenStateListener() {
            @Override
            public void onScreenOn() {
                Log.i("ScreenOn");
            }

            @Override
            public void onScreenOff() {
                Log.i("ScreenOff");
            }

            @Override
            public void onUserPresent() {
                Log.i("UserPresent");
            }
        });
        Constants.getInstance().activityList.add(this);

        // 初始化控件
        initView();
        // 初始化数据
        initData();
    }

    public int clearCacheFolder(File dir, long numDays) {
        int deletedFiles = 0;
        if (dir != null && dir.isDirectory()) {
            try {
                for (File child : dir.listFiles()) {
                    if (child.isDirectory()) {
                        deletedFiles += clearCacheFolder(child, numDays);
                    }
                    if (child.lastModified() < numDays) {
                        if (child.delete()) {
                            deletedFiles++;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return deletedFiles;
    }

    /**
     * 清除用户相关信息
     */
    public void releaseUserData() {
    }

    /**
     * 退出登陆，跳转到登陆页
     */
    public void logout() {
    }
}
