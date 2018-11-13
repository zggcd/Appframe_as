package com.asiainfo.appframe.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.asiainfo.appframe.R;
import com.asiainfo.appframe.activity.HomeActivity;
import com.asiainfo.appframe.activity.WebControlerActivity;
import com.asiainfo.appframe.activity.WebViewActivity;
import com.asiainfo.appframe.activity.WelcomeActivity;
import com.asiainfo.appframe.bean.AccessTokenResponse;
import com.asiainfo.appframe.bean.uibean.ChildsBean;
import com.asiainfo.appframe.net.ApiClient;
import com.asiainfo.appframe.utils.CommonUtil;
import com.asiainfo.appframe.utils.Log;

public class TaskFragment extends BaseFragment {

    WebView wv_task;

    @Override
    protected void onAttachToContext(Context context) {
        super.onAttachToContext(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mineView = inflater.inflate(R.layout.appframe_fragment_task, container, false);
        initView(mineView);
        initData();

        return mineView;
    }

    private void initView(View view) {
        wv_task = view.findViewById(R.id.wv_task);
    }

    private void initData(){
        wv_task.setWebViewClient(new WebViewClient());
        wv_task.getSettings().setAllowFileAccess(true);             // 允许访问文件
        wv_task.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv_task.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        wv_task.getSettings().setSupportZoom(true);                 // 支持缩放
        wv_task.getSettings().setUseWideViewPort(true);             //关键点
        wv_task.getSettings().setSaveFormData(false);
        wv_task.getSettings().setGeolocationEnabled(true);
        wv_task.getSettings().setLoadWithOverviewMode(false);
//        wv_task.getSettings().setBuiltInZoomControls(true);         // 设置显示缩放按钮
        wv_task.getSettings().setDisplayZoomControls(false);			// 设置影藏缩放按钮
        wv_task.getSettings().setDomStorageEnabled(true);
        wv_task.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        wv_task.getSettings().setJavaScriptEnabled(true);
        wv_task.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);
                if (wv_task != null && wv_task.canGoBack()){
//                    btnBack.setVisibility(View.VISIBLE);
                }else{
//                    btnBack.setVisibility(View.GONE);
                }

            }
        });
//        ApiClient.getClickUrl(handler, 4, "todo");
    }

    //Handler
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            CommonUtil.closeCommonProgressDialog();
            switch (msg.what) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    //取数
                    break;
                case 4:
                    //跳转URL
                    String clickUrl = (String) msg.obj;
                    if (clickUrl != null && clickUrl.length() > 0) {
                        wv_task.loadUrl(clickUrl);
                    }
                    break;
                default:
                    break;
            }

        }

    };
}