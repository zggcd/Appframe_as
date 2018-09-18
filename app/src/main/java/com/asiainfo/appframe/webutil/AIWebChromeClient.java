package com.asiainfo.appframe.webutil;

import com.asiainfo.appframe.utils.StringUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.EditText;

public class AIWebChromeClient extends WebChromeClient {

	private Activity mActivity;
	private PluginManager mPluginManager;
	
	AlertDialog mDialog = null;
	
	public AIWebChromeClient(Activity activity, WebView webview){
		super();
		this.mActivity = activity;
		this.mPluginManager = new PluginManager(mActivity, webview);
	}
	
	@Override
	public void onReceivedTitle(WebView view, String title) {
		// TODO Auto-generated method stub
		super.onReceivedTitle(view, title);
	}
	
	@Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
    }

    /**
     * 播放网络视频时全屏会被调用的方法
     */
    @Override
    public void onShowCustomView(View view,
           CustomViewCallback callback) {
    }

    /**
     * 视频播放退出全屏会被调用的
     */
    @Override
    public void onHideCustomView() {
    }

    /**
     * Tell the client to display a javascript alert dialog.
     * 
     * @param view
     * @param url
     * @param message
     * @param result
     */
    @Override
    public boolean onJsAlert(WebView view, String url, String message,
            final JsResult result) {
        if (mActivity.isFinishing()) {
            return true;
        }
        AlertDialog.Builder dlg = new AlertDialog.Builder(mActivity);
        dlg.setMessage(message);
        dlg.setTitle("Alert");
        // Don't let alerts break the back button
        dlg.setCancelable(true);
        dlg.setPositiveButton(android.R.string.ok,
                new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
        dlg.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                result.cancel();
            }
        });
        dlg.setOnKeyListener(new DialogInterface.OnKeyListener() {
            // DO NOTHING
            public boolean onKey(DialogInterface dialog, int keyCode,
                    KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    result.confirm();
                    return false;
                } else
                    return true;
            }
        });
        mDialog = dlg.create();
        dlg.show();
        return true;
    }

    /**
     * Tell the client to display a confirm dialog to the user.
     * 
     * @param view
     * @param url
     * @param message
     * @param result
     */
    @Override
    public boolean onJsConfirm(WebView view, String url, String message,
            final JsResult result) {
        if (mActivity.isFinishing()) {
            return true;
        }
        AlertDialog.Builder dlg = new AlertDialog.Builder(mActivity);
        dlg.setMessage(message);
        dlg.setTitle("Confirm");
        dlg.setCancelable(true);
        dlg.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
        dlg.setNegativeButton(android.R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        result.cancel();
                    }
                });
        dlg.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                result.cancel();
            }
        });
        dlg.setOnKeyListener(new DialogInterface.OnKeyListener() {
            // DO NOTHING
            public boolean onKey(DialogInterface dialog, int keyCode,
                    KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    result.cancel();
                    return false;
                } else
                    return true;
            }
        });
        mDialog = dlg.create();
        dlg.show();
        return true;
    }

    /**
     * Tell the client to display a prompt dialog to the user. If the client
     * returns true, WebView will assume that the client will handle the
     * prompt dialog and call the appropriate JsPromptResult method.
     * 
     * Since we are hacking prompts for our own purposes, we should not be
     * using them for this purpose, perhaps we should hack console.log to do
     * this instead!
     * 
     * @param view
     * @param url
     * @param message
     * @param defaultValue
     * @param result
     */
    @Override
    public boolean onJsPrompt(WebView view, String url, String message,
            String defaultValue, JsPromptResult result) {

        if (mActivity.isFinishing()) {
            // result.confirm("");
            result.cancel();
        } else if (!StringUtil.isEmpty(defaultValue)&& defaultValue.startsWith("ec-bridge-js:")) {
            String[] args = defaultValue.split(":", -1);
            String service = "";
            String action = "";
            String params = "";
            if (args.length < 3) {
                return true;
            }
            service = args[1];
            action = args[2];
            if (args.length > 3) {
                params = args[3];
            }
            mPluginManager.execute(service, action, params);
            // result.confirm("");
            result.cancel();
        } else {
            final JsPromptResult res = result;
            AlertDialog.Builder dlg = new AlertDialog.Builder(mActivity);
            dlg.setMessage(message);
            final EditText input = new EditText(mActivity);
            if (defaultValue != null) {
                input.setText(defaultValue);
            }
            dlg.setView(input);
            dlg.setCancelable(false);
            dlg.setPositiveButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                int which) {
                            String usertext = input.getText().toString();
                            res.confirm(usertext);
                        }
                    });
            dlg.setNegativeButton(android.R.string.cancel,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                int which) {
                            res.cancel();
                        }
                    });
            mDialog = dlg.create();
            dlg.show();
        }
        return true;
    }
    
    /**
     * 释放plugin中必须释放的资源
     */
    public void release(){
    	mPluginManager.getPlugin().destroy();
    	if(mDialog != null){
    		mDialog.dismiss();
    	}
    }
    
    public BasePlugin getPlugin(){
    	return mPluginManager.getPlugin();
    }
	
}
