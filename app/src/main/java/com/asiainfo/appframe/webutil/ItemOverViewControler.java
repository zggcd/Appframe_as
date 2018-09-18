package com.asiainfo.appframe.webutil;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import com.asiainfo.appframe.service.DownloadFileService;
import com.asiainfo.appframe.utils.StringUtil;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ItemOverViewControler {
	
	/**
	 * web页加载完回调
	 * @author Stiven
	 *
	 */
	public interface WebPageLoadCallBack{
		public void pageStart(String url);
		public void pageFinish(String title);
		public void receivedError(String errorCode, String errorMsg);
	}
	
	private Context mContext;

    public WebView webview;
    

    AIWebChromeClient client;
    
    WebPageLoadCallBack callback;
    //父windowId
    public ItemOverViewControler parentItem = null;
    //本windowId
    public String windowID = "";
    //关联的windowID
    public List<ItemOverViewControler> list_ChildItem = new ArrayList<ItemOverViewControler>();
    
    public ItemOverViewControler(Context context){
        this.mContext = context;
        init();
    }

    /**
     * 初始化
     */
    private void init(){
    	
        webview = new WebView(mContext);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        webview.setLayoutParams(lp);
        webview.getSettings().setAllowFileAccess(true);             // 允许访问文件
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        webview.getSettings().setSupportZoom(true);                 // 支持缩放
        webview.getSettings().setUseWideViewPort(true);             //关键点
        webview.getSettings().setSaveFormData(false);
        webview.getSettings().setGeolocationEnabled(true);
        webview.getSettings().setLoadWithOverviewMode(false);
//        mWV.getSettings().setBuiltInZoomControls(true);         // 设置显示缩放按钮
        webview.getSettings().setDisplayZoomControls(false);			// 设置影藏缩放按钮
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webview.addJavascriptInterface(new JsInteration(), "control");
        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new ProxyBridge(), "ProxyBridge");
        client = new AIWebChromeClient((Activity)mContext, webview);
        webview.setWebChromeClient(client);
     // 添加下载的监听
        webview.setDownloadListener(new MyWebViewDownLoadListener());
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.endsWith(".txt")){
                    Intent intent = new Intent(mContext, DownloadFileService.class);
                    intent.putExtra("success", "");
                    intent.putExtra("fail", "");
                    intent.putExtra("url", url);
                    mContext.startService(intent);
                    return true;

                }
                return super.shouldOverrideUrlLoading(view, url);
            }
            
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
            	// TODO Auto-generated method stub
            	super.onPageStarted(view, url, favicon);
            	callback.pageStart(url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);
                Log.i("info_out","pagefinished");
                callback.pageFinish(webview.getTitle());
            }
            
            @Override
            public void onReceivedError(WebView view,
            		WebResourceRequest request, WebResourceError error) {
            	// TODO Auto-generated method stub
            	super.onReceivedError(view, request, error);
            	callback.receivedError(error.getErrorCode() + "", error.getDescription() +"");
            }
        });
        
        windowID = "window_" + System.currentTimeMillis();
    }
    
    /**
     * 下载监听事件
     * 
     * @author stiven
     * 
     */
    private class MyWebViewDownLoadListener implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent,
                String contentDisposition, String mimetype, long contentLength) {
//            Uri uri = Uri.parse(url);
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//            startActivity(intent);
        	if(url.contains(".xls") || url.contains(".doc")|| url.contains(".ppt") || url.contains(".txt")){
        		webview.loadUrl("https://view.officeapps.live.com/op/view.aspx?src=" + url);
        	} else if(url.endsWith(".jpg") || url.endsWith(".jpeg") || url.endsWith(".png")){
        		webview.loadDataWithBaseURL(null, "<img  src=" + url + ">", "text/html", "charset=UTF-8", null);
        	}else if(url.endsWith(".pdf")){
                Intent intent = new Intent(mContext, DownloadFileService.class);
                intent.putExtra("success", "");
                intent.putExtra("fail", "");
                intent.putExtra("url", url);
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startService(intent);
            }else{
        		webview.loadUrl(url);
        	}
        	
        }
    }

    public void setCallBack(WebPageLoadCallBack cb){
    	this.callback = cb;
    }
    
    /**
     * webview lodurl
     * @param url
     */
    public void loadUrl(String url){
        if(url == null && url.length() <= 0){
            Toast.makeText(mContext, "无效的连接", Toast.LENGTH_SHORT).show();
            return;
        }
        webview.loadUrl(url);
    }

    /**
     * 返回
     * @param v
     */
    public void goBack(){
        if(webview.canGoBack()){
            webview.goBack();
        }else{
//            finish();
        }
    }

    /**
     * activity onPause
     */
    public void pause(){
        webview.onPause();
        webview.pauseTimers();
    }

    /**
     * activity onResume
     */
    public void resume(){
        webview.onResume();
        webview.resumeTimers();
    }

    /**
     * activity onDestroy
     */
    public void destroy(){
        if(webview == null){
            return;
        }
        webview.stopLoading();
        webview.setWebViewClient(null);
        webview.setWebChromeClient(null);
        webview.destroy();
        if(client.getPlugin() != null){
            client.release();
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
    
    //添加父windowId
    public void setParentItem(ItemOverViewControler parentItem){
    	if(parentItem != null){
    		this.parentItem = parentItem;
    	}
    }
    
    public ItemOverViewControler getParentItem(){
    	return this.parentItem;
    }
    
    //添加关联ID
    public void addChildItem(ItemOverViewControler childItem){
    	if(childItem != null){
    		list_ChildItem.add(childItem);
    	}
    }
    
    //删除windowId
    public void deleteChildItem(ItemOverViewControler childItem){
    	if(list_ChildItem.contains(childItem)){
        	list_ChildItem.remove(childItem);
    	}
    }
    
    //获取关联windowId列表
    public List<ItemOverViewControler> getChildItemList(){
    	return list_ChildItem;
    }
    
}
