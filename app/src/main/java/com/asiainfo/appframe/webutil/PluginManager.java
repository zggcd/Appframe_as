package com.asiainfo.appframe.webutil;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import com.asiainfo.appframe.utils.ResourceUtil;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Log;
import android.webkit.WebView;

public class PluginManager implements CallbackContext {

	private Context mContext;
    private Map<String, String> pluginMap = new HashMap<String, String>();
    private WebView mWV = null;

    /**
     * 插件管理类构造
     * 
     * @param ctx
     * @param webView
     */
    public PluginManager(Context ctx, WebView webview) {
        mContext = ctx;
        this.mWV = webview;
        initPluginMap();
    }
    
    /**
     * 插件管理类构造
     * 
     * @param ctx
     */
    public PluginManager(Context ctx){
    	this.mContext = ctx;
    	initPluginMap();
    }

    /**
     * 初始化插件
     */
    public void initPluginMap() {
        XmlResourceParser xrp = mContext.getResources().getXml(ResourceUtil.getXmlId(mContext, "plugin"));
        try {
            while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {
                if (xrp.getEventType() == XmlResourceParser.START_TAG) {
                    String name = xrp.getName();
                    if (name.equals("plugin")) {
                        String pluginName = xrp.getAttributeValue(0);
                        String pluginPath = xrp.getAttributeValue(1);
                        pluginMap.put(pluginName, pluginPath);
                    }
                }
                xrp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    BasePlugin plugin;
    /**
     * 执行插件调用
     * 
     * @param service
     * @param action
     * @param rawArgs
     */
    public void execute(String service, String action, String rawArgs) {
    	AndroidResult = null;
    	Log.d("PM", "===PM===> service: " + service);
    	Log.d("PM", "===PM===> action: " + action);
    	Log.d("PM", "===PM===> rawArgs" + rawArgs);
    	
    	JSONObject jsonObj = new JSONObject(pluginMap);
    	Log.d("PM", "===PM===> pluginMap:" + jsonObj.toString());
    	
        String pluginPath = pluginMap.get(service);
        try {
            plugin = (BasePlugin) Class
                    .forName(pluginPath)
                    .getConstructor(IPlugin.class,
                            PluginManager.class)
                    .newInstance(mContext, this);
            // 执行插件
            plugin.exec(action, rawArgs);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public BasePlugin getPlugin(){
    	return plugin;
    }

    private String toCallbackString(String statement, String callbackId) {
        return "EC_Bridge_JS.invokeCallback('" + callbackId + "'," + true
                + ",'" + statement + "');";
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    private String AndroidResult = null;
    
    /**
     * 返回原生能力调用的结果
     * @return
     */
    public String getAndroidResult(){
    	return AndroidResult;
    }
    
    @Override
    public void callBack(String statement, String callbackId) {
    	String a = toCallbackString(statement, callbackId);
    	Log.d("PM", a);
    	if(mWV != null){
        	mWV.loadUrl("javascript:" + toCallbackString(statement, callbackId));
    	}else{
    		AndroidResult = statement;
    	}
//        ((WebViewActivity) mContext).sendJavascript(toCallbackString(statement, callbackId));
    }
	
}
