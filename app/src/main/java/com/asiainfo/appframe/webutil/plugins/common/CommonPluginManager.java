package com.asiainfo.appframe.webutil.plugins.common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Xml;

import com.asiainfo.appframe.R;
import com.asiainfo.appframe.bean.XMLCommon;
import com.asiainfo.appframe.utils.ResourceUtil;
import com.asiainfo.appframe.webutil.BasePlugin;
import com.asiainfo.appframe.webutil.CallbackContext;
import com.asiainfo.appframe.webutil.IPlugin;
import com.asiainfo.appframe.webutil.PluginManager;

public class CommonPluginManager implements CallbackContext {
	private Context mContext;
	private Map<String, Object> mMap_content = null;
	
	/**
	 * 统一插件管理类构造
	 * @param context
	 */
	public CommonPluginManager(Context context){
		this.mContext = context;
		if(mMap_content == null){
			initXML();
		}
	}
	
	/**
	 * 解析publicplugin.xml文件
	 */
	private void initXML(){
		mMap_content = new HashMap<String, Object>();
		XmlResourceParser xrp = mContext.getResources().getXml(ResourceUtil.getXmlId(mContext, "publicplugin"));
		try {
			int event = xrp.getEventType();				//先获取当前解析器光标在哪
			while(event != XmlPullParser.END_DOCUMENT){	//如果还没到文档结束标志，那么久继续往下处理
				XMLCommon xmlCommon = null;
				switch(event){
					case XmlPullParser.START_DOCUMENT:	//解析开始
						break;
					case XmlPullParser.START_TAG:		//获取需要的数据
						if(xrp.getName().equals("plugin")){
							xmlCommon = new XMLCommon();
							xmlCommon.setJS_API(xrp.getAttributeValue(null, "JS_API"));
							xmlCommon.setAlias(xrp.getAttributeValue(null, "alias"));
							xmlCommon.setCategory(xrp.getAttributeValue(null, "category"));
							xmlCommon.setMethod(xrp.getAttributeValue(null, "method"));
							mMap_content.put(xmlCommon.getJS_API(), xmlCommon);
						}
						break;
					case XmlPullParser.END_TAG:
						break;
					case XmlPullParser.TEXT:			//标签中间内容
						break;
					default:
						break;
				}
				event = xrp.next();						//将当前解析器移向下一步
			}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	BasePlugin plugin;
	
	/**
	 * 
	 * @param pluginManager
	 * @param successCallback
	 * @param failCallback
	 * @param alias
	 * @param signature
	 * @param userParam
	 */
	public void execute(PluginManager pluginManager, String successCallback, String failCallback, String JS_API, String signature, String userParam){
		
		String pluginPath = ((XMLCommon)mMap_content.get(JS_API)).getCategory();
		try {
			plugin = (BasePlugin)Class
					.forName(pluginPath)
					.getConstructor(IPlugin.class, 
							PluginManager.class)
					.newInstance(mContext, pluginManager);
			String rawArgs = 
					"f:"
					+ successCallback
					+ ":f:"
					+ failCallback
					+ ":s:"
					+ URLEncoder.encode(userParam, "UTF-8");
			rawArgs = URLEncoder.encode(rawArgs, "UTF-8");
			plugin.exec(((XMLCommon)mMap_content.get(JS_API)).getMethod(), rawArgs, "1");
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return mContext;
	}

	@Override
	public void callBack(String statement, String callbackId) {
		// TODO Auto-generated method stub
		
	}

}
