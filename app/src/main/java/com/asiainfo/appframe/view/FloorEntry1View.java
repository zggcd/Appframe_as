package com.asiainfo.appframe.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.asiainfo.appframe.R;
import com.asiainfo.appframe.activity.HomeActivity;
import com.asiainfo.appframe.activity.MainActivity;
import com.asiainfo.appframe.activity.WebControlerActivity;
import com.asiainfo.appframe.activity.WelcomeActivity;
import com.asiainfo.appframe.bean.AccessTokenResponse;
import com.asiainfo.appframe.bean.FloorBasicInfosBean;
import com.asiainfo.appframe.bean.HomePageDtoListBean;
import com.asiainfo.appframe.net.ApiClient;
import com.asiainfo.appframe.utils.CommonUtil;
import com.asiainfo.appframe.utils.SDKUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FloorEntry1View extends LinearLayout {
    private Context context;

    //view
    ViewPager viewPager;

    //DATA
    List<FloorBasicInfosBean> mList_FloorBasicInfos = new ArrayList<>();

    HomePageDtoListBean mParentInfo;

    public FloorEntry1View(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public void setFloorEntry1ViewInfo(HomePageDtoListBean info){
        this.mParentInfo = info;
        this.mList_FloorBasicInfos = info.getFloorBasicInfos();
        Collections.sort(mList_FloorBasicInfos, new Comparator<FloorBasicInfosBean>() {

            @Override
            public int compare(FloorBasicInfosBean lhs, FloorBasicInfosBean rhs) {
                if(lhs.getSort() >= rhs.getSort() ){
                    return 1;
                }else{
                    return -1;
                }
            }
        });

        initData();
    }

    public FloorEntry1View(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FloorEntry1View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(){
        LayoutInflater.from(context).inflate(R.layout.appframe_floor_entry1, this, true);
    }

    @SuppressLint("SetTextI18n")
    private void initData(){

        if(mList_FloorBasicInfos.size() > 0){

            TextView tv_entry1_name = (TextView) findViewById(R.id.tv_entry1_name);

            RelativeLayout rl_entry1_1 = findViewById(R.id.rl_entry1_1);
            TextView tv_entry1_menu_name1 = (TextView) findViewById(R.id.tv_entry1_menu_name1);
            TextView tv_entry1_menu_describe1 = (TextView) findViewById(R.id.tv_entry1_menu_describe1);
            final ImageView iv_entey1_1 = (ImageView) findViewById(R.id.iv_entey1_1);

            RelativeLayout rl_entry1_2 = findViewById(R.id.rl_entry1_2);
            TextView tv_entry1_menu_name2 = (TextView) findViewById(R.id.tv_entry1_menu_name2);
            TextView tv_entry1_menu_describe2 = (TextView) findViewById(R.id.tv_entry1_menu_describe2);
            final ImageView iv_entey1_2 = (ImageView) findViewById(R.id.iv_entey1_2);

            String entry1_menu_name = mList_FloorBasicInfos.get(0).getTitle();
            String entry1_menu_describe1 = mList_FloorBasicInfos.get(0).getSubhead();
            tv_entry1_name.setText(mParentInfo.getTitle());
            tv_entry1_menu_name1.setText(entry1_menu_name);
            tv_entry1_menu_describe1.setText(entry1_menu_describe1);
            String iconUrl1 = mList_FloorBasicInfos.get(0).getPicurl();
            Picasso.with(context).load(iconUrl1).into(iv_entey1_1, new Callback() {
                @Override
                public void onSuccess() {
                    iv_entey1_1.setBackground(null);
                }

                @Override
                public void onError() {

                }
            });
            rl_entry1_1.setOnClickListener( click -> {
                if((System.currentTimeMillis() - SDKUtil.start_time) / 1000 - Long.parseLong(SDKUtil.expires_in) >= 0){
                    ApiClient.refreshAccessToken(handler, 2, 0, SDKUtil.phone_num, SDKUtil.mac, SDKUtil.accessToken, SDKUtil.staff_code);
                    return;
                }
                // TODO Auto-generated method stub
                ApiClient.getClickUrl(handler, 4, mList_FloorBasicInfos.get(0).getAbilityalias());
            });

            String entry1_menu_name2 = mList_FloorBasicInfos.get(1).getTitle();
            String entry1_menu_describe2 = mList_FloorBasicInfos.get(1).getSubhead();
            tv_entry1_menu_name2.setText(entry1_menu_name2);
            tv_entry1_menu_describe2.setText(entry1_menu_describe2);
            String iconUrl2 = mList_FloorBasicInfos.get(1).getPicurl();
            Picasso.with(context).load(iconUrl2).into(iv_entey1_2, new Callback() {
                @Override
                public void onSuccess() {
                    iv_entey1_2.setBackground(null);
                }

                @Override
                public void onError() {

                }
            });
            rl_entry1_2.setOnClickListener( click -> {
                if((System.currentTimeMillis() - SDKUtil.start_time) / 1000 - Long.parseLong(SDKUtil.expires_in) >= 0){
                    ApiClient.refreshAccessToken(handler, 2, 0, SDKUtil.phone_num, SDKUtil.mac, SDKUtil.accessToken, SDKUtil.staff_code);
                    return;
                }
                // TODO Auto-generated method stub
                ApiClient.getClickUrl(handler, 4, mList_FloorBasicInfos.get(1).getAbilityalias());
            });

        }
        invalidate();

    }

    //Handler
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            CommonUtil.closeCommonProgressDialog();
            switch (msg.what) {
                case 1:
                    break;
                case 2:
                    AccessTokenResponse response = (AccessTokenResponse)msg.obj;

                    if(response.getCode() == 1000){
                        Intent intent = new Intent(context, WelcomeActivity.class);
                        context.startActivity(intent);
                        ((HomeActivity)context).finish();
                        break;
                    }

                    SDKUtil.accessToken = response.getAccess_token();
                    SDKUtil.expires_in = response.getExpires_in();
                    SDKUtil.start_time = System.currentTimeMillis();
                    ApiClient.getClickUrl(handler, 4, mList_FloorBasicInfos.get(msg.arg1).getAbilityalias());
                    break;
                case 3:
                    //取数
                    break;
                case 4:
                    //跳转URL
                    String clickUrl = (String) msg.obj;
                    if(clickUrl != null && clickUrl.length() > 0){
                        jumpToWeb(clickUrl);
                    }
                    break;
                default:
                    break;
            }

        }

    };

    private void jumpToWeb(String webUrl){
//    	Intent intent = new Intent(mContext, WebViewActivity.class);
        Intent intent = new Intent(context, WebControlerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("webUrl", webUrl);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

}
