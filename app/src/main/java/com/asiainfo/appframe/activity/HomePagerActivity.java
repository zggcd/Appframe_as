package com.asiainfo.appframe.activity;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.asiainfo.appframe.R;
import com.asiainfo.appframe.bean.AccessTokenResponse;
import com.asiainfo.appframe.bean.uibean.ChildsBean;
import com.asiainfo.appframe.data.Constants;
import com.asiainfo.appframe.fragment.HomeFragment;
import com.asiainfo.appframe.fragment.MineFragment;
import com.asiainfo.appframe.fragment.MsgFragment;
import com.asiainfo.appframe.fragment.TaskFragment;
import com.asiainfo.appframe.net.ApiClient;
import com.asiainfo.appframe.utils.CommonUtil;
import com.asiainfo.appframe.utils.SDKUtil;
import com.asiainfo.appframe.utils.StringUtil;
import com.asiainfo.appframe.view.TabControlView;

public class HomePagerActivity extends BaseHomeActivity {


    public FragmentManager fragmentManager = null;
    public FragmentTransaction fragmentTransaction = null;

    public HomeFragment homeFragment;
    public TaskFragment taskFragment;
    public MsgFragment MsgFragment;
    public MineFragment homeFragment4;

    //view
    TabControlView mTabControlView = null;

    //data
    public static String mCurrentFragTag = "";

    /**
     * 初始化组件
     */
    @Override
    public void initView() {
        setContentView(R.layout.appframe_activity_homepage_new);
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {

        SDKUtil sdkUtil = SDKUtil.getInstance(this, null);
        sdkUtil.change(this, null);

        fragmentManager = getFragmentManager();

        mTabControlView = (TabControlView) findViewById(R.id.bottom_layout);
        mTabControlView.initTabControl();
        mTabControlView.setTabCallback(new TabControlView.TabClickCallback() {
            @Override
            public void onTabClick(int itenId) {
                String tag = "";
                if((itenId & Constants.BTN_FLAG_HOME) != 0){
                    tag = Constants.FRAGMENT_FLAG_HOME;
                }else if((itenId & Constants.BTN_FLAG_TASK) != 0){

                    if((System.currentTimeMillis() - SDKUtil.start_time) / 1000 - Long.parseLong(SDKUtil.expires_in) >= 0){
                        ApiClient.refreshAccessToken(handler, 2, 0, SDKUtil.phone_num, SDKUtil.mac, SDKUtil.accessToken, SDKUtil.staff_code);
                        return;
                    }
                    ChildsBean childsBean = new ChildsBean();
                    childsBean.setAlias("todo");
                    itemClick(childsBean);
                    return;
//                    tag = Constants.FRAGMENT_FLAG_TASK;
                }else if((itenId & Constants.BTN_FLAG_MSG) != 0){
                    tag = Constants.FRAGMENT_FLAG_MSG;
                }else if((itenId & Constants.BTN_FLAG_MINE) != 0){
                    tag = Constants.FRAGMENT_FLAG_MINE;
                }
                setTabSelection(tag);
            }
        });
        mTabControlView.setDefaultCheck();
        initFragment();
    }

    public void initFragment(){
        setDefaultFirstFragment(Constants.FRAGMENT_FLAG_HOME);
    }

    private void setDefaultFirstFragment(String tag){
        setTabSelection(tag);
    }

    private void hideFragments(){
        if(fragmentTransaction != null){
            if(homeFragment != null){
                fragmentTransaction.hide(homeFragment);
            }
            if(taskFragment != null){
                fragmentTransaction.hide(taskFragment);
            }
            if(MsgFragment != null){
                fragmentTransaction.hide(MsgFragment);
            }
            if(homeFragment4 != null){
                fragmentTransaction.hide(homeFragment4);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setTabSelection(String tag){

        if(mCurrentFragTag.equals(tag)){
            return;
        }

        //开启fragment事物
        fragmentTransaction = fragmentManager.beginTransaction();
        hideFragments();
        switch (tag){
            case Constants.FRAGMENT_FLAG_HOME:
//            case Constants.FRAGMENT_FLAG_CLASSIFY:
                if(homeFragment != null){
                    fragmentTransaction.show(homeFragment);
                }else{
                    homeFragment = new HomeFragment();
                    fragmentTransaction.add(R.id.fragment_content, homeFragment, Constants.FRAGMENT_FLAG_HOME);
                }
                break;
            case Constants.FRAGMENT_FLAG_TASK:
                if(taskFragment != null){
                    fragmentTransaction.show(taskFragment);
                }else{
                    taskFragment = new TaskFragment();
                    fragmentTransaction.add(R.id.fragment_content, taskFragment, Constants.FRAGMENT_FLAG_TASK);
                }
                break;
            case Constants.FRAGMENT_FLAG_MSG:
                if(MsgFragment != null){
                    fragmentTransaction.show(MsgFragment);
                }else{
                    MsgFragment = new MsgFragment();
                    fragmentTransaction.add(R.id.fragment_content, MsgFragment, Constants.FRAGMENT_FLAG_MSG);
                }
                break;
            case Constants.FRAGMENT_FLAG_MINE:
                if(homeFragment4 != null){
                    fragmentTransaction.show(homeFragment4);
                }else{
                    homeFragment4 = new MineFragment();
                    fragmentTransaction.add(R.id.fragment_content, homeFragment4, Constants.FRAGMENT_FLAG_MINE);
                }
                break;
            default:
                break;
        }
        fragmentTransaction.commit();
        mCurrentFragTag = tag;

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
                        Intent intent = new Intent(mContext, WelcomeNewActivity.class);
                        mContext.startActivity(intent);
                        ((HomeActivity)mContext).finish();
                        break;
                    }

                    SDKUtil.accessToken = response.getAccess_token();
                    SDKUtil.expires_in = response.getExpires_in();
                    SDKUtil.start_time = System.currentTimeMillis();
                    ChildsBean childsBean = new ChildsBean();
                    childsBean.setAlias("todo");
                    itemClick(childsBean);
//                    ApiClient.getClickUrl(handler, 4, mList_FloorBasicInfos.get(msg.arg1).getAbilityalias());
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

    /**
     * 子元素点击事件
     * @param childsBean
     */
    private void itemClick(ChildsBean childsBean){
        String alias = childsBean.getAlias();
        String clickUrl = childsBean.getClickUrl();
        if (!StringUtil.isEmpty(alias)) {
            ApiClient.getClickUrl(handler, 4, alias);
        } else if (!StringUtil.isEmpty(clickUrl)) {
            jumpToWeb(clickUrl);
        }
    }

    private void jumpToWeb(String webUrl){
//    	Intent intent = new Intent(context, WebViewActivity.class);
        Intent intent = new Intent(mContext, WebControlerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("webUrl", webUrl);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

}
