package com.asiainfo.appframe.webutil;

import android.content.Intent;

/**
 * 插件接口，
 * @author Stiven
 *
 */
public interface IPlugin {
	/**
	 * 开启activity并返回
	 * @param plugin
	 * @param intent
	 * @param requestCode
	 */
	public void startActivityForResult(BasePlugin plugin, Intent intent, int requestCode);

	public ItemOverViewControler addNewWebView(int type, String url);
	
	public String getWindowSession(String windowId);
	
	public boolean setWindowSession(String content);
	
	/**
	 * 当能力调用为H5时，需要上传web页的开始及结束时间
	 * @param 当前能力调用的请求流水
	 */
	public void recordH5Invoke(String RequestSep);
	
	public void onAbilityResult(String abilityResult);
	
}
