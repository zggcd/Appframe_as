package com.asiainfo.appframe.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.asiainfo.appframe.R;
import com.asiainfo.appframe.activity.LoginNewActivity;
import com.asiainfo.appframe.bean.AuthInfoResult;
import com.asiainfo.appframe.utils.SDKUtil;
import com.asiainfo.appframe.utils.StringUtil;
import com.google.gson.Gson;

import java.io.Serializable;

public class MineFragment extends BaseFragment {

    private RelativeLayout rl_logout;

    //view
    private TextView tv_name, tv_job_num, tv_area;

    //data
    private SharedPreferences sp;
    private boolean IS_AOTU_LOGIN = false;
    Gson gson =new Gson();

    @Override
    protected void onAttachToContext(Context context) {
        super.onAttachToContext(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mineView = inflater.inflate(R.layout.appframe_fragment_mine, container, false);
        initView(mineView);
        initData();

        return mineView;
    }

    private void initView(View view){
        rl_logout = view.findViewById(R.id.rl_logout);
        tv_name = view.findViewById(R.id.tv_name);
        tv_job_num = view.findViewById(R.id.tv_job_num);
        tv_area = view.findViewById(R.id.tv_area);
    }

    private void initData(){
        sp = getActivity().getSharedPreferences("APPFRAME_SDK", Context.MODE_PRIVATE);
        String authInfo= SDKUtil.authInfoResult;
        AuthInfoResult authInfoResult = gson.fromJson(authInfo, AuthInfoResult.class);

        if (!StringUtil.isEmpty(authInfo)){
            tv_name.setText(authInfoResult.getStaffname());
            tv_job_num.setText("工号： " + authInfoResult.getStaffcode());
            tv_area.setText(authInfoResult.getAreaName());
        }

        IS_AOTU_LOGIN = false;
        rl_logout.setOnClickListener(click -> {
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("login_statue", IS_AOTU_LOGIN);
            editor.commit();
            Intent intent = new Intent(getActivity(), LoginNewActivity.class);
            startActivity(intent);
            getActivity().finish();
        });
    }
}
