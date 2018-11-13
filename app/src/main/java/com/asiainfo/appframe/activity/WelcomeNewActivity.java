package com.asiainfo.appframe.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.asiainfo.appframe.CommonApplication;
import com.asiainfo.appframe.R;
import com.asiainfo.appframe.bean.Cutscenes;
import com.asiainfo.appframe.bean.HomePageInfo;
import com.asiainfo.appframe.bean.LoginPage;
import com.asiainfo.appframe.bean.MsgPushAuthInfo;
import com.asiainfo.appframe.bean.MsgPushAuthResult;
import com.asiainfo.appframe.bean.PreUpgradeInfoBean;
import com.asiainfo.appframe.bean.UpgradeInfoBean;
import com.asiainfo.appframe.data.Constants;
import com.asiainfo.appframe.msgpush.Client;
import com.asiainfo.appframe.net.ApiClient;
import com.asiainfo.appframe.permission.AddPermission;
import com.asiainfo.appframe.utils.AutoUpdate;
import com.asiainfo.appframe.utils.CommonUtil;
import com.asiainfo.appframe.utils.DesUtil;
import com.asiainfo.appframe.utils.MD5Util;
import com.asiainfo.appframe.utils.SDKUtil;
import com.asiainfo.appframe.utils.SystemPreference;
import com.google.gson.Gson;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class WelcomeNewActivity extends BaseActivity {
    private static String TAG = "WelcomeActivity";
    private Context mContext;

    //view
    private ImageView mIV;
    CommonApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        mContext = this;
        setContentView(R.layout.appframe_activity_welcome);
        SDKUtil.getInstance(mContext, null);
        application = (CommonApplication) getApplication();
        super.onCreate(savedInstanceState);
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

                if (CommonUtil.isNetworkAvailableNew(WelcomeNewActivity.this)){
                    // 检查软件更新
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            autoUpdate = new AutoUpdate(WelcomeNewActivity.this, handler, getIntent());
                            WelcomeNewActivity.this.startUpdateThread();
                            application.isVersionChecked = true;
                        }
                    }, 500);

                } else {
//            CommonUtil.showTipDialog(WelcomeActivity.this
//                    ,getString(R.string.netLinkErrorText));
                }

//                jumpTo(LoginNewActivity.class, null);
            }
        }, AddPermission.CODE_PERMISSIONS_STORAGE);


    }

    public void jumpTo(Class<?> c, Bundle bundle){
        Intent intent = new Intent(WelcomeNewActivity.this, c);
        if(bundle != null){
            intent.putExtras(bundle);
        }
        startActivity(intent);
        finish();
    }

    AutoUpdate autoUpdate;

    /**
     * 版本校验线程
     */
    private void startUpdateThread() {
        PackageInfo packInfo = autoUpdate.getCurrentVersion();
        application.versionName = packInfo.versionName;
        application.versionCode = String.valueOf(packInfo.versionCode);


        ApiClient.UpgradeVersion(handler, 10002, SDKUtil.packageVersionName);

		/*ApiClient.upgrade(handler, CODE_UPGRADE, null,
				Constants.getInstance().packageCd, application.versionCode,
				Constants.DEVICE_CODE, null, android.os.Build.VERSION.RELEASE,
				getString(R.string.UNIDESK_ACTION_UPGRADE));*/
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){

        Gson gson = new Gson();

        @SuppressLint("ShowToast")
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
//                    finish();
                    break;
                case 10002:
                    PreUpgradeInfoBean preInfo = gson.fromJson(msg.obj.toString(), PreUpgradeInfoBean.class);
                    if (preInfo.getResultCode().equals("0000")){
                        UpgradeInfoBean upgradeInfo = gson.fromJson(msg.obj.toString(), UpgradeInfoBean.class);
                        if (upgradeInfo != null) {
                            Constants.getInstance().desktopApkUrl = upgradeInfo.getResultObj().getUpdateLink();
                            if (upgradeInfo.getResultObj().isIsForce()) {
                                Log.d("", "发现新版本(" + upgradeInfo.getResultObj().getVersionName() + ")[强制更新]");
                                autoUpdate.update(true, true, upgradeInfo);
                            } else {
                                Log.d("", "发现新版本(" + upgradeInfo.getResultObj().getVersionName() + ")[非强制更新]");
                                String ignoreVcode = SystemPreference.getString(mContext, Constants.IGNORE);
                                autoUpdate.update(true, false, upgradeInfo);
                            }
                        } else {
                            autoUpdate.update(false, false, (UpgradeInfoBean) null);
                        }
                    }else{
                        jumpTo(LoginNewActivity.class, null);
                    }
                    break;
                default:
                    break;
            }

        }

    };

}
