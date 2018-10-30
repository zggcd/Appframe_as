package com.asiainfo.appframe.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.asiainfo.appframe.R;
import com.asiainfo.appframe.utils.SDKResetPwdCallback;
import com.asiainfo.appframe.utils.SDKUtil;

public class ResetWPwdActivity extends AppCompatActivity {

    MySDKResetPwdCallback resetPwdCallback = new MySDKResetPwdCallback();
    SDKUtil sdkUtil;

    private Context mContext;

    private EditText ed_reset_phone_num, ed_reset_authcode, ed_reset_new_pwd;
    private Button btn_sure, btn_reset_get_authcode;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.appframe_activity_reset_pwd);
        sdkUtil = SDKUtil.getInstance(this, null);
        sdkUtil.setSDKResetPwdCallback(resetPwdCallback);
        initView();
        initData();

    }

    public void initView(){
        ed_reset_phone_num = findViewById(R.id.ed_reset_phone_num);
        ed_reset_authcode = findViewById(R.id.ed_reset_authcode);
        ed_reset_new_pwd = findViewById(R.id.ed_reset_new_pwd);
        btn_sure = findViewById(R.id.btn_sure);
        btn_reset_get_authcode = findViewById(R.id.btn_reset_get_authcode);
        iv_back = findViewById(R.id.iv_back);
    }

    public void initData(){

        Intent parentIntent = getIntent();
        Bundle bundle = parentIntent.getExtras();
        if (bundle != null){
            String phone_num = bundle.getString("phone_num");
            ed_reset_phone_num.setText(phone_num);
        }

        btn_reset_get_authcode.setOnClickListener(click ->{
            startVerifyCodeTimer();
            String phone_num = ed_reset_phone_num.getText().toString().trim();
            sdkUtil.getResetPwdAuthcode(phone_num);
        });

        btn_sure.setOnClickListener(click ->{
            String phone_num = ed_reset_phone_num.getText().toString().trim();
            String smscode = ed_reset_authcode.getText().toString().trim();
            String nPassword = ed_reset_new_pwd.getText().toString().trim();
            sdkUtil.resetPwd(phone_num, smscode, nPassword);
        });

        iv_back.setOnClickListener(click->{
            finish();
        });
    }

    class MySDKResetPwdCallback implements SDKResetPwdCallback{

        /**
         * 获取重置密码的验证码
         *
         * @param result
         */
        @Override
        public void onAuthCodeSuccess(String result) {
            Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
        }

        /**
         * 重置密码返回
         *
         * @param result
         */
        @Override
        public void onResetPwdSuccess(String result) {
            Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    /**发送验证码，改变文字时间的计时器*/
    private CountDownTimer timer;
    /**
     * 发送验证码按钮文字倒计时功能
     */
    private void startVerifyCodeTimer(){
        btn_reset_get_authcode.setClickable(false);
        if(timer == null){
            timer = new CountDownTimer(30*1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    btn_reset_get_authcode.setText(""+(millisUntilFinished/1000)+"S后重新获取");
                }
                @Override
                public void onFinish() {
                    btn_reset_get_authcode.setClickable(true);
                    btn_reset_get_authcode.setText("获取验证码");
//                    btn_login_get_authcode.setTextColor(Color.parseColor("#ffffff"));
                }
            };
        }
        timer.cancel();
        timer.start();
    }

}
