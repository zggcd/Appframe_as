package com.asiainfo.appframe.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.asiainfo.appframe.R;
import com.asiainfo.appframe.utils.Log;
import com.asiainfo.appframe.webutil.AIWebChromeClient;
import com.asiainfo.appframe.webutil.BasePlugin;
import com.asiainfo.appframe.webutil.IPlugin;
import com.asiainfo.appframe.webutil.ItemOverViewControler;
import com.asiainfo.appframe.webutil.PluginManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.browse.MediaBrowser.ItemCallback;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class WebViewActivity extends BaseActivity implements IPlugin {

//	private Context mContext;

	//view
    private TextView mTV_title;
    private WebView mWV;
    
    private Activity mActivity;
    
    //data
    String webUrl = "";
    
    /** 插件管理 */
    private PluginManager mPluginManager = null;
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	// TODO Auto-generated method stub
    	super.onSaveInstanceState(outState);
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mActivity = this;
		setContentView(R.layout.appframe_activity_webview);
		super.onCreate(savedInstanceState);
		// 启动硬加速
        if (getPhoneAndroidSDK() >= 11) {
            getWindow().setFlags(0x1000000, 0x1000000);
        }
        
        initWebView();
		
	}
	
	@Override
	public void initView() {
		mTV_title = (TextView) findViewById(R.id.tv_title);
		mWV = (WebView) findViewById(R.id.wv);
	}

	@Override
	public void initData() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		webUrl = bundle.getString("webUrl", "");
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	public void initWebView(){
		
//		mPluginManager = new PluginManager(this, mWV);
		if(mWV == null){
			return;
		}
		if(webUrl == null || webUrl.length() <= 0){
			Toast.makeText(this, "无效的链接", Toast.LENGTH_SHORT).show();
			return;
		}
		mWV.loadUrl(webUrl);
//		mWV.loadUrl("http://zc.testpub.net/files/nativeJsBridgeDemo/demo.html");
//		mWV.loadUrl("http://61.160.128.138:9512/files/nativeJsBridgeDemo/demo.html");
		mWV.setWebViewClient(new WebViewClient());
        mWV.getSettings().setAllowFileAccess(true);             // 允许访问文件
        mWV.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWV.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
        mWV.getSettings().setSupportZoom(true);                 // 支持缩放
        mWV.getSettings().setUseWideViewPort(true);             //关键点
        mWV.getSettings().setSaveFormData(false);
        mWV.getSettings().setGeolocationEnabled(true);
        mWV.getSettings().setLoadWithOverviewMode(false);
//        mWV.getSettings().setBuiltInZoomControls(true);         // 设置显示缩放按钮
        mWV.getSettings().setDisplayZoomControls(false);			// 设置影藏缩放按钮
        mWV.getSettings().setDomStorageEnabled(true);
        mWV.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWV.addJavascriptInterface(new JsInteration(), "control");
        mWV.getSettings().setJavaScriptEnabled(true);
        mWV.addJavascriptInterface(new ProxyBridge(), "ProxyBridge");
        client = new AIWebChromeClient(WebViewActivity.this, mWV);
        mWV.setWebChromeClient(client);
        mWV.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);
                Log.i("info_out","pagefinished");
                mTV_title.setText(mWV.getTitle());
                if (mWV != null && mWV.canGoBack()){
//                    btnBack.setVisibility(View.VISIBLE);
                }else{
//                    btnBack.setVisibility(View.GONE);
                }

            }
        });
	}
	
	AIWebChromeClient client;
	
	/**
	 * 返回
	 * @param v
	 */
	public void goBack(View v){
		if(mWV.canGoBack()){
			mWV.goBack();
        }else{
            finish();
        }
	}
	
	/**
	 * 关闭
	 * @param v
	 */
	public void goFinish(View v){
		finish();
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if(mWV.canGoBack()){
			mWV.goBack();
        }else{
            finish();
        }
	}
	
	/**
     * 向页面返回回调
     * 
     * @param statement
     */
    public void sendJavascript(final String statement) {
        if (statement != null) {
            Thread t = new Thread() {
                public void run() {
                    mWV.loadUrl("javascript:" + statement);
                }
            };
            runOnUiThread(t);
        }
    }
    
	public class JsInteration {
        Handler handler = new Handler();
        @JavascriptInterface
        public void toastMessage(String message) {
        		
        }

    }

    class ProxyBridge {
        String date;
        String json_date;
        DatePickerDialog pickerDialog;
        Handler handler = new Handler();

        @JavascriptInterface
        public void loadVINInputViewWithVINNO(final String vin) {
            Log.i("info_out", "vin:" + vin);
            handler.post(new Runnable() {
                @Override
                public void run() {
                }
            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mWV.onPause();
        mWV.pauseTimers();
    }

    @Override
    public void onResume() {
        super.onResume();
//        client.release();
        mWV.onResume();
        mWV.resumeTimers();
    }
    @Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
//		client.release();
	}

    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        if(mWV == null){
        	return;
        }
        mWV.stopLoading();
        mWV.setWebViewClient(null);
        mWV.setWebChromeClient(null);
        mWV.removeAllViews();
        mWV.destroy();
        if(client.getPlugin() != null){
        	client.release();
        }
//        mPluginManager.getPlugin().destroy();
    }
    
    /**
     * 获取终端的系统版本号
     * 
     * @return
     */
    private int getPhoneAndroidSDK() {
        int version = 0;
        try {
            version = Build.VERSION.SDK_INT;
        } catch (NumberFormatException e) {
        }
        return version;

    }
    
    public static boolean isEmpty(String str)
   	{
   		if (str == null || str.trim().equals(""))
   			return true;
   		else
   			return false;
   	}

    /** 插件类 */
    private BasePlugin mECPlugin = null;
    
	@Override
	public void startActivityForResult(BasePlugin plugin, Intent intent,
			int requestCode) {
		// TODO Auto-generated method stub
		/**
	     * 启动另一个页面并等待返回结果
	     */
	        mECPlugin = plugin;
	        startActivityForResult(intent, requestCode);
	}
	
	@Override
	public void onActivityResult(int requestCode,int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		BasePlugin callback = mECPlugin;
        if (callback != null) {
            callback.onActivityResult(requestCode, resultCode, data);
            mECPlugin = null;
        }
	}

	public Map<String, String> mMap_content = new HashMap<String, String>();
	@Override
	public ItemOverViewControler addNewWebView(int type, String url) {
		return null;
	}
	
	@Override
	public boolean setWindowSession(String content) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getWindowSession(String windowId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void recordH5Invoke(String RequestSep) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onAbilityResult(String abilityResult) {
		// TODO Auto-generated method stub
		
	}

}
