package com.asiainfo.appframe.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.asiainfo.appframe.R;
import com.asiainfo.appframe.activity.LoginNewActivity;
import com.asiainfo.appframe.bean.AuthInfoResult;
import com.asiainfo.appframe.utils.SDKUtil;
import com.asiainfo.appframe.utils.StringUtil;
import com.google.gson.Gson;

public class MsgFragment extends BaseFragment {

    private RelativeLayout rl_logout;

    //view
    private TextView tv_name, tv_job_num;

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
        View mineView = inflater.inflate(R.layout.appframe_fragment_msg, container, false);
        initView(mineView);
        initData();

        return mineView;
    }

    private void initView(View view){
    }

    private void initData(){
    }
}
