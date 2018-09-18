package com.asiainfo.appframe.webutil;

import java.net.URLDecoder;

import org.json.JSONArray;

import com.asiainfo.appframe.utils.Log;

import android.content.Intent;
import android.view.View;
import android.webkit.WebView;

/**
 * 插件基类
 * @author Stiven
 *
 */
public abstract class BasePlugin {
	public IPlugin mECInterface;
    public PluginManager mPluginManager;
    
    /**
     * 插件类
     * 
     * @param ecInterface
     *            实现ECInterface的activity
     * @param webView
     *            webView
     * @param pm
     *            插件管理类
     */
    public BasePlugin(IPlugin ecInterface, PluginManager pm) {
        this.mECInterface = ecInterface;
        this.mPluginManager = pm;
    }
    
    /**
     * 插件执行(参数处理)
     * 
     * @param action
     * @param args
     */
    public void exec(String action, String args) {
        try {
            String pluginAction = URLDecoder.decode(action, "UTF-8");
            String pluginParams = URLDecoder.decode(args, "UTF-8");
            JSONArray pluginParamsJson = new JSONArray();
            if (!isEmpty(pluginParams)) {
                String[] pluginParamsArr = pluginParams.split(":", -1);
                for (int i = 0, j = 0, l = pluginParamsArr.length; i < l; i += 2, j++) {
                    pluginParamsJson.put(j, pluginParamsArr[i + 1]);
                }

                pluginAction = pluginAction.substring(0, pluginAction.length()
                        - pluginParamsJson.length());
            }
            execute(pluginAction, pluginParamsJson);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    /**
     * 插件执行(参数处理),客户端自己拼接的字符串
     * 
     * @param action
     * @param args
     */
    public void exec(String action, String args, String type) {
        try {
            String pluginAction = URLDecoder.decode(action, "UTF-8");
            String pluginParams = URLDecoder.decode(args, "UTF-8");
            JSONArray pluginParamsJson = new JSONArray();
            if (!isEmpty(pluginParams)) {
                String[] pluginParamsArr = pluginParams.split(":", -1);
                for (int i = 0, j = 0, l = pluginParamsArr.length; i < l; i += 2, j++) {
                    pluginParamsJson.put(j, pluginParamsArr[i + 1]);
                }

//                pluginAction = pluginAction.substring(0, pluginAction.length() - pluginParamsJson.length());
            }
            execute(pluginAction, pluginParamsJson, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public static boolean isEmpty(String str)
	{
		if (str == null || str.trim().equals(""))
			return true;
		else
			return false;
	}

    /**
     * 插件执行
     * 
     * @param action
     * @param args
     */
    public abstract void execute(String action, JSONArray args);
    /**
     * 插件执行，调用统一插件时，客户端手动拼接字符串
     * @param action
     * @param args
     * @param type	不做任何操作
     */
    public void execute(String action, JSONArray args, String type){
    	
    }
    /**
     * 执行插件后返回
     * @param result
     * @param type
     */
    public abstract void callback(String result, String type);
    
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {}
    
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){}
    
    public void destroy(){}
}
