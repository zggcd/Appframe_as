package com.asiainfo.appframe.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.asiainfo.appframe.R;
import com.asiainfo.appframe.utils.SDKModifyPwdCallback;
import com.asiainfo.appframe.utils.SDKResetPwdCallback;
import com.asiainfo.appframe.utils.SDKUtil;

public class ModifyPwdActivity extends AppCompatActivity {

    MySDKModifyPwdCallback modifyPwdCallback = new MySDKModifyPwdCallback();
    SDKUtil sdkUtil;

    private Context mContext;

    private EditText ed_pwd_old, ed_pwd_new, ed_pwd_new2;
    private Button btn_sure;
    private ImageView iv_back;

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
        ed_pwd_old = findViewById(R.id.ed_pwd_old);
        ed_pwd_new = findViewById(R.id.ed_pwd_new);
        ed_pwd_new2 = findViewById(R.id.ed_pwd_new2);
        btn_sure = findViewById(R.id.btn_sure);
        iv_back = findViewById(R.id.iv_back);
    }

    public void initData() {

        btn_sure.setOnClickListener(click -> {
            String password = ed_pwd_old.getText().toString().trim();
            String nPassword = ed_pwd_new.getText().toString().trim();
            String nPassword2 = ed_pwd_new2.getText().toString().trim();
            if(nPassword.equals(nPassword2)){
                sdkUtil.modifyPwd(SDKUtil.accessToken, password, nPassword, 1);
            }else{
                Toast.makeText(mContext, "密码不一致，请保持一致", Toast.LENGTH_SHORT).show();
            }

        });
        iv_back.setOnClickListener(click->{
            finish();
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
