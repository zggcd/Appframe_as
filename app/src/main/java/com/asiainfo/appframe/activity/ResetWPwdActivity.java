package com.asiainfo.appframe.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.asiainfo.appframe.R;
import com.asiainfo.appframe.utils.SDKRandomPasswordCallback;
import com.asiainfo.appframe.utils.SDKResetPwdCallback;
import com.asiainfo.appframe.utils.SDKUtil;
import com.asiainfo.appframe.utils.StringUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class ResetWPwdActivity extends AppCompatActivity {

    MySDKResetPwdCallback resetPwdCallback = new MySDKResetPwdCallback();
    MySDKRandomPasswordCallback randomPasswordCallback = new MySDKRandomPasswordCallback();
    SDKUtil sdkUtil;

    private Context mContext;

    ConstraintLayout parent;
    private EditText ed_reset_phone_num, ed_reset_authcode, ed_reset_new_pwd, ed_reset_auto_pwd;
    private Button btn_sure, btn_reset_get_authcode;
    private ImageView iv_back, iv_eye;
    private Button btn_auto_pwd;
    private TextView tv_reset_copy;

    boolean showPwd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.appframe_activity_reset_pwd);
        sdkUtil = SDKUtil.getInstance(this, null);
        sdkUtil.setSDKResetPwdCallback(resetPwdCallback);
        sdkUtil.setSDKRandomPasswordCallback(randomPasswordCallback);
        initView();
        initData();

    }

    public void initView(){
        parent =findViewById(R.id.parent);
        ed_reset_phone_num = findViewById(R.id.ed_reset_phone_num);
        ed_reset_authcode = findViewById(R.id.ed_reset_authcode);
        ed_reset_new_pwd = findViewById(R.id.ed_reset_new_pwd);
        btn_sure = findViewById(R.id.btn_sure);
        btn_reset_get_authcode = findViewById(R.id.btn_reset_get_authcode);
        iv_back = findViewById(R.id.iv_back);
        iv_eye = findViewById(R.id.iv_eye);
        btn_auto_pwd = findViewById(R.id.btn_auto_pwd);
        ed_reset_auto_pwd = findViewById(R.id.ed_reset_auto_pwd);
        tv_reset_copy = findViewById(R.id.tv_reset_copy);
    }

    public void initData(){

        Intent parentIntent = getIntent();
        Bundle bundle = parentIntent.getExtras();
        if (bundle != null){
            String phone_num = bundle.getString("phone_num");
            ed_reset_phone_num.setText(phone_num);
        }

        parent.setOnClickListener(click -> {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(parent.getWindowToken(), 0);
        });


        btn_reset_get_authcode.setOnClickListener(click ->{

            String phone_num = ed_reset_phone_num.getText().toString().trim();
            if (StringUtil.isEmpty(phone_num)){
                Toast.makeText(mContext, "请输入账号或手机号", Toast.LENGTH_SHORT).show();
                return;
            }
            startVerifyCodeTimer();
            sdkUtil.getResetPwdAuthcode(phone_num);
        });

        btn_sure.setOnClickListener(click ->{
            String phone_num = ed_reset_phone_num.getText().toString().trim();
            String smscode = ed_reset_authcode.getText().toString().trim();
            String nPassword = ed_reset_new_pwd.getText().toString().trim();

            if (StringUtil.isEmpty(phone_num)){
                Toast.makeText(mContext, "请输入账号或手机号", Toast.LENGTH_SHORT).show();
                return;
            }else if (StringUtil.isEmpty(smscode)){
                Toast.makeText(mContext, "请输入验证码", Toast.LENGTH_SHORT).show();
                return;
            }else if(StringUtil.isEmpty(nPassword)){
                Toast.makeText(mContext, "请输入新密码", Toast.LENGTH_SHORT).show();
                return;
            }

            sdkUtil.resetPwd(phone_num, smscode, nPassword);
        });

        iv_eye.setOnClickListener(click->{
            if (!showPwd){
                showPwd = true;
                ed_reset_new_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }else{
                showPwd = false;
                ed_reset_new_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

        btn_auto_pwd.setOnClickListener(click -> {
            sdkUtil.getAutoPwd();
        });

        tv_reset_copy.setOnClickListener(click ->{
            String randomPwd = ed_reset_auto_pwd.getText().toString().trim();
            if (!StringUtil.isEmpty(randomPwd)){
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setText(randomPwd);
                Toast.makeText(mContext, "已拷贝", Toast.LENGTH_SHORT).show();
            }
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

    class MySDKRandomPasswordCallback implements SDKRandomPasswordCallback{

        /**
         * 自动生成密码返回
         *
         * @param result
         */
        @Override
        public void onRandomPasswordSuccess(String result) {
//            Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
            JSONObject jsonObject = null;
            try {
                if (!StringUtil.isEmpty(result)){
                    jsonObject = new JSONObject(result);
                    String randomPwd =jsonObject.getString("result");
                    if (tv_reset_copy.getVisibility()== View.GONE){
                        tv_reset_copy.setVisibility(View.VISIBLE);
                    }
                    ed_reset_auto_pwd.setText(randomPwd);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
            timer = new CountDownTimer(60*1000, 1000) {
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
