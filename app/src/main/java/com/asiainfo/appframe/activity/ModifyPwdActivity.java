package com.asiainfo.appframe.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.asiainfo.appframe.R;
import com.asiainfo.appframe.utils.SDKModifyPwdCallback;
import com.asiainfo.appframe.utils.SDKResetPwdCallback;
import com.asiainfo.appframe.utils.SDKUtil;
import com.asiainfo.appframe.utils.StringUtil;

public class ModifyPwdActivity extends AppCompatActivity {

    MySDKModifyPwdCallback modifyPwdCallback = new MySDKModifyPwdCallback();
    SDKUtil sdkUtil;

    private Context mContext;

    ConstraintLayout parent;
    private EditText ed_pwd_old, ed_pwd_new, ed_pwd_new2;
    private Button btn_sure;
    private ImageView iv_back, iv_eye, iv_eye_new, iv_eye_new2;

    boolean showPwdOld = false;
    boolean showPwdNew = false;
    boolean showPwdNew2 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.appframe_activity_modify_pwd);
        sdkUtil = SDKUtil.getInstance(this, null);
        sdkUtil.setSDKModifyPwdCallback(modifyPwdCallback);
        initView();
        initData();

    }

    public void initView() {
        parent = findViewById(R.id.parent);
        ed_pwd_old = findViewById(R.id.ed_pwd_old);
        ed_pwd_new = findViewById(R.id.ed_pwd_new);
        ed_pwd_new2 = findViewById(R.id.ed_pwd_new2);
        btn_sure = findViewById(R.id.btn_sure);
        iv_back = findViewById(R.id.iv_back);
    }

    public void initData() {

        parent.setOnClickListener(click -> {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(parent.getWindowToken(), 0);
        });

        btn_sure.setOnClickListener(click -> {
            String password = ed_pwd_old.getText().toString().trim();
            String nPassword = ed_pwd_new.getText().toString().trim();
            String nPassword2 = ed_pwd_new2.getText().toString().trim();

            if (StringUtil.isEmpty(password)){
                Toast.makeText(mContext, "请输入旧密码", Toast.LENGTH_SHORT).show();
                return;
            }else if (StringUtil.isEmpty(nPassword)){
                Toast.makeText(mContext, "请输入新密码", Toast.LENGTH_SHORT).show();
                return;
            }else if(StringUtil.isEmpty(nPassword2)){
                Toast.makeText(mContext, "请输入确认密码", Toast.LENGTH_SHORT).show();
                return;
            }

            if(nPassword.equals(nPassword2)){
                sdkUtil.modifyPwd(SDKUtil.accessToken, password, nPassword, 1);
            }else{
                Toast.makeText(mContext, "新密码不一致，请保持一致", Toast.LENGTH_SHORT).show();
            }

        });
        iv_back.setOnClickListener(click->{
            finish();
        });

        iv_eye.setOnClickListener(click->{
            if (!showPwdOld){
                showPwdOld = true;
                ed_pwd_old.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }else{
                showPwdOld = false;
                ed_pwd_old.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

        iv_eye_new.setOnClickListener(click->{
            if (!showPwdNew){
                showPwdNew = true;
                ed_pwd_new.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }else{
                showPwdNew = false;
                ed_pwd_new.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

        iv_eye_new2.setOnClickListener(click->{
            if (!showPwdNew2){
                showPwdNew2 = true;
                ed_pwd_new2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }else{
                showPwdNew2 = false;
                ed_pwd_new2.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

    }

    class MySDKModifyPwdCallback implements SDKModifyPwdCallback {
        /**
         * 修改密码返回
         *
         * @param result
         */
        @Override
        public void onModifyPwdCallback(String result) {

        }
    }
}
