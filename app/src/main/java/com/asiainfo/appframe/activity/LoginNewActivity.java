package com.asiainfo.appframe.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.asiainfo.appframe.CommonApplication;
import com.asiainfo.appframe.R;
import com.asiainfo.appframe.bean.AccessTokenResponse;
import com.asiainfo.appframe.bean.AreaCodeListResponse;
import com.asiainfo.appframe.bean.AreaCodes;
import com.asiainfo.appframe.bean.Cutscenes;
import com.asiainfo.appframe.bean.HomePageInfo;
import com.asiainfo.appframe.bean.LoginPage;
import com.asiainfo.appframe.bean.MsgPushAuthInfo;
import com.asiainfo.appframe.bean.MsgPushAuthResult;
import com.asiainfo.appframe.bean.SDKAccessTokenResponse;
import com.asiainfo.appframe.bean.UpgradeInfo;
import com.asiainfo.appframe.bean.UpgradeInfoBean;
import com.asiainfo.appframe.data.Constants;
import com.asiainfo.appframe.dialog.AreaChooseDialog;
import com.asiainfo.appframe.dialog.BottomAnimDialog;
import com.asiainfo.appframe.msgpush.Client;
import com.asiainfo.appframe.net.ApiClient;
import com.asiainfo.appframe.permission.AddPermission;
import com.asiainfo.appframe.utils.AutoUpdate;
import com.asiainfo.appframe.utils.CommonUtil;
import com.asiainfo.appframe.utils.DesUtil;
import com.asiainfo.appframe.utils.MD5Util;
import com.asiainfo.appframe.utils.SDKAuthCallBack;
import com.asiainfo.appframe.utils.SDKUtil;
import com.asiainfo.appframe.utils.StringUtil;
import com.asiainfo.appframe.utils.SystemPreference;
import com.asiainfo.appframe.v.V;
import com.github.mikephil.charting.charts.LineChart;
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

import java.util.ArrayList;
import java.util.List;

public class LoginNewActivity extends AppCompatActivity implements SDKAuthCallBack, V {

    private Context mContext;

//    private LoginPresenter mPresenter;
    public SDKUtil sdkUtil = null;

    //view
    ConstraintLayout parant;
    private EditText ed_login_account;
    private EditText ed_login_pwd;
    private EditText ed_login_authcode;
    private TextView tv_login_forget_pwd;
    private Button btn_login_get_authcode;
    private Button btn_login;
    private CheckBox cb_autologin;
    private TextView tv_reset_pwd;
    private ImageView iv_eye;

    private LineChart lineChart;

    //data
    private SharedPreferences sp;
    private boolean IS_AOTU_LOGIN = false;
    boolean showPwd = false;

    public CommonApplication application;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.appframe_activity_login_new);

//        mPresenter = new LoginPresenter(new LoginModel(), this, SchedulerProvider.getInstance());
        initView();
        AddPermission addPermission = new AddPermission((Activity)mContext);
        addPermission.addPermission(new AddPermission.PermissionsListener() {
            @Override
            public void onPermissionListener(boolean hasPermission, int code) {
                initData();
            }
        }, AddPermission.CODE_PERMISSIONS_STORAGE);
    }

    @SuppressLint("CutPasteId")
    private void initView(){
        parant = findViewById(R.id.parant);
        ed_login_account = findViewById(R.id.ed_login_account);
        ed_login_pwd = findViewById(R.id.ed_login_pwd);
        ed_login_authcode = findViewById(R.id.ed_login_authcode);
        tv_login_forget_pwd = findViewById(R.id.tv_login_forget_pwd);
        btn_login_get_authcode = findViewById(R.id.btn_login_get_authcode);
        btn_login = findViewById(R.id.btn_login);
        cb_autologin = findViewById(R.id.cb_autologin);
        tv_reset_pwd = findViewById(R.id.tv_reset_pwd);
        iv_eye = findViewById(R.id.iv_eye);

//        lineChart = findViewById(R.id.lc);

//        List<String> xDataList = new ArrayList<>();// x轴数据源
//        List<Entry> yDataList = new ArrayList<>();// y轴数据数据源
//        //给上面的X、Y轴数据源做假数据测试
//        for (int i = 0; i < 24; i++) {
//            // x轴显示的数据
//            xDataList.add(i + ":00");
//            //y轴生成float类型的随机数
//            float value = (float) (Math.random() * 10) + 3;
//            yDataList.add(new Entry(value, i));
//        }
//
//        //显示图表,参数（ 上下文，图表对象， X轴数据，Y轴数据，图表标题，曲线图例名称，坐标点击弹出提示框中数字单位）
//        ChartsUtil.showChart(this, lineChart, xDataList, yDataList, "供热趋势图", "供热量/时间","kw/h");
    }

    private void initData(){
        sdkUtil = SDKUtil.getInstance(this, this);
        sdkUtil.change(this, this);
        application = (CommonApplication) getApplication();

        String secret = "";
        try {
            net.minidev.json.JSONObject userInfo = new net.minidev.json.JSONObject();
            userInfo.put("appsecret", SDKUtil.appSecret);
            Payload payload= new Payload(userInfo);
            JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
            JWSObject jwsObject = new JWSObject(header, payload);
    //		String secret = "3d990d2276917dfac04467df11fff26d";

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

        parant.setOnClickListener(click -> {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(parant.getWindowToken(), 0);
        });

        sp = getSharedPreferences("APPFRAME_SDK", Context.MODE_PRIVATE);
        IS_AOTU_LOGIN = sp.getBoolean("login_statue", false);

        btn_login_get_authcode.setOnClickListener(click ->{
            String phone_num = ed_login_account.getText().toString().trim();
            if (StringUtil.isEmpty(phone_num)){
                Toast.makeText(mContext, "请输入账号或手机号", Toast.LENGTH_SHORT).show();
                return;
            }
            startVerifyCodeTimer();

            sdkUtil.getValidateCode(phone_num);
        });

        btn_login.setOnClickListener(click ->{
            String phone_num = ed_login_account.getText().toString().trim();

            if (StringUtil.isEmpty(phone_num)){
                Toast.makeText(mContext, "请输入账号或手机号", Toast.LENGTH_SHORT).show();
                return;
            }else if (StringUtil.isEmpty(ed_login_authcode.getText().toString().trim())){
                Toast.makeText(mContext, "请输入验证码", Toast.LENGTH_SHORT).show();
                return;
            }else if(StringUtil.isEmpty(ed_login_pwd.getText().toString().trim())){
                Toast.makeText(mContext, "请输入新密码", Toast.LENGTH_SHORT).show();
                return;
            }

            sdkUtil.getAreaCode(phone_num);
        });

        tv_reset_pwd.setOnClickListener(click->{
            String phone_num = ed_login_account.getText().toString().trim();
            Bundle bundle = new Bundle();
            if(!StringUtil.isEmpty(phone_num)){
                bundle.putString("phone_num", phone_num);
            }
            jumpTo(ResetWPwdActivity.class, bundle);
        });

        tv_login_forget_pwd.setOnClickListener(click->{
            String phone_num = ed_login_account.getText().toString().trim();
            Bundle bundle = new Bundle();
            if(!StringUtil.isEmpty(phone_num)){
                bundle.putString("phone_num", phone_num);
            }
            jumpTo(ResetWPwdActivity.class, bundle);
        });

        cb_autologin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    IS_AOTU_LOGIN = true;
                }else{
                    IS_AOTU_LOGIN = false;
                }
            }
        });

        iv_eye.setOnClickListener( clicl -> {
            if (!showPwd){
                showPwd = true;
                ed_login_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }else{
                showPwd = false;
                ed_login_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

        if (IS_AOTU_LOGIN){
            sdkUtil.refreshAccessToken();
        }
    }


    @Override
    public void getDataSuccess() {

    }

    @Override
    public void getDataFail() {

    }

    /**发送验证码，改变文字时间的计时器*/
    private CountDownTimer timer;
    /**
     * 发送验证码按钮文字倒计时功能
     */
    private void startVerifyCodeTimer(){
        btn_login_get_authcode.setClickable(false);
        if(timer == null){
            timer = new CountDownTimer(60*1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    btn_login_get_authcode.setText(""+(millisUntilFinished/1000)+"S后重新获取");
                }
                @Override
                public void onFinish() {
                    btn_login_get_authcode.setClickable(true);
                    btn_login_get_authcode.setText("获取验证码");
//                    btn_login_get_authcode.setTextColor(Color.parseColor("#ffffff"));
                }
            };
        }
        timer.cancel();
        timer.start();
    }

    /**
     * Called when pointer capture is enabled or disabled for the current window.
     *
     * @param hasCapture True if the window has pointer capture.
     */
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    /**
     * 获取验证码的返回
     *
     * @param result
     * @return
     */
    @Override
    public void onValidateCodeCallback(String result) {
        Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
    }

    /**
     * 获取地区码的返回
     *
     * @param result
     * @return
     */
    @Override
    public void onAreaCodeCallback(String result) {
        Gson gson = new Gson();
        AreaCodeListResponse areaCodeListResponse = gson.fromJson(result, AreaCodeListResponse.class);

        if(areaCodeListResponse.getCode() == 1){
            List<AreaCodes> areaCodeInfo = areaCodeListResponse.getAreaCodes();
            if(areaCodeInfo == null){
                Toast.makeText(mContext,areaCodeListResponse.getMsg(), Toast.LENGTH_SHORT).show();
                return;
            }
            final AreaChooseDialog bottom_dialog = new AreaChooseDialog(mContext, areaCodeInfo);
            bottom_dialog.setOnChooseListener(click->{
                AreaCodes authCodes = bottom_dialog.getmCurrent_authcodes();
                    if(authCodes != null){
                        String phone_num = ed_login_account.getText().toString().trim();
                        String pwd = ed_login_authcode.getText().toString().trim();
                        String base_code = ed_login_pwd.getText().toString().trim();
                        sdkUtil.getAccessToken(phone_num, pwd, base_code, authCodes.getAreaCode());
                    }else{

                    }
                bottom_dialog.dismiss();
            });
            bottom_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {

                }
            });
            bottom_dialog.show();
        }else{
            Toast.makeText(mContext, areaCodeListResponse.getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取accesstoken的返回
     *
     * @param result
     * @return
     */
    @Override
    public void onAccessTokenCallback(String result) {
        Gson gson = new Gson();
        SDKAccessTokenResponse response = gson.fromJson(result, SDKAccessTokenResponse.class);
        if(response.getCode() == 1){
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("login_statue", IS_AOTU_LOGIN);
            editor.commit();
            SDKUtil.start_time = System.currentTimeMillis();
            SDKUtil.expires_in = response.getExpires_in();
            jumpTo(HomePagerActivity.class, null);
            finish();
        }else if (response.getCode() == 1010){
            if(!response.getStaff_state().equals("1") && !response.getStaff_state().equals("2")){  //1、初始密码 2、密码过期
                String phone_num = ed_login_account.getText().toString().trim();
                Bundle bundle = new Bundle();
                if(!StringUtil.isEmpty(phone_num)){
                    bundle.putString("phone_num", phone_num);
                }
                jumpTo(ModifyPwdActivity.class, bundle);
            }
        }else{
            Toast.makeText(mContext, response.getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 刷新accesstoken的返回
     *
     * @param result
     * @return
     */
    @Override
    public void onRefreshAccessTokenCallback(String result) {
        CommonUtil.closeCommonProgressDialog();
        Gson gson = new Gson();
        AccessTokenResponse accessTokenResponse = gson.fromJson(result, AccessTokenResponse.class);
        if(accessTokenResponse.getCode() == 1){
            SDKUtil.start_time = System.currentTimeMillis();
            SDKUtil.expires_in = accessTokenResponse.getExpires_in();
//			jumpTo(MainActivity.class, null);
            jumpTo(HomePagerActivity.class, null);
            finish();
        }else{
            Toast.makeText(mContext, accessTokenResponse.getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /**
     * 失败回调
     *
     * @param errMessage
     */
    @Override
    public void onError(String errMessage) {
        Toast.makeText(mContext, errMessage, Toast.LENGTH_SHORT).show();
    }

    public void jumpTo(Class<?> c, Bundle bundle){
        Intent intent = new Intent(LoginNewActivity.this, c);
        if(bundle != null){
            intent.putExtras(bundle);
        }
        startActivity(intent);
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
                case 2:			//获取teamKey
                    JSONObject result = (JSONObject) msg.obj;
                    int code = (int) result.opt("code");
                    if(code == 1){
                        String teamKey = (String) result.opt("key");
                        try {
                            teamKey = DesUtil.ECBDecryptHex(teamKey, SDKUtil.appSecret);
                            SDKUtil.teamKey = teamKey;
                        } catch (Exception e) {
                            Toast.makeText(mContext, "系统异常，请检查网络", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }else{
//					Toast.makeText(mContext, (String)result.opt("msg"), 2000).show();
                    }
                    break;

                case 10002:
//                    UpgradeInfoBean upgradeInfo = gson.fromJson(msg.obj.toString(), UpgradeInfoBean.class);
//                    if (upgradeInfo != null) {
//                        Constants.getInstance().desktopApkUrl = upgradeInfo.getResultObj().getUpdateLink();
//                        if (upgradeInfo.getResultObj().isIsForce()) {
//                            Log.d("", "发现新版本(" + upgradeInfo.getResultObj().getVersionName() + ")[强制更新]");
//                            autoUpdate.update(true, true, upgradeInfo);
//                        } else {
//                            Log.d("", "发现新版本(" + upgradeInfo.getResultObj().getVersionName() + ")[非强制更新]");
//                            String ignoreVcode = SystemPreference.getString(mContext, Constants.IGNORE);
//                            autoUpdate.update(true, false, upgradeInfo);
//                        }
//                    } else {
//                        autoUpdate.update(false, false, (UpgradeInfoBean) null);
//                    }
//
//                    break;
                default:
                    break;
            }

        }

    };
}
