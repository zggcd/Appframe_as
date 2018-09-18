package com.asiainfo.appframe.webutil.plugins;

import org.json.JSONArray;

import android.content.Intent;

import com.asiainfo.appframe.service.CirclePositionService;
import com.asiainfo.appframe.service.DownloadFileService;
import com.asiainfo.appframe.webutil.BasePlugin;
import com.asiainfo.appframe.webutil.IPlugin;
import com.asiainfo.appframe.webutil.PluginManager;

/**
 * 文件下载并展示插件
 * @author Stiven
 *
 */
public class FilePlugin extends BasePlugin {

	String success;
	String fail;
	
	public FilePlugin(IPlugin ecInterface, PluginManager pm) {
		super(ecInterface, pm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(String action, JSONArray args) {
		this.success = args.optString(0);
        this.fail = args.optString(1);

		if ("downloadFile".equals(action)) {//下载文件并展示
			String url = args.optString(2);
			downloadFile(url);
        }
		
	}
	
	public void downloadFile(String fileurl){
		Intent intent = new Intent(mPluginManager.getContext(), DownloadFileService.class);
		intent.putExtra("success", success);
		intent.putExtra("fail", fail);
		intent.putExtra("url", fileurl);
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mPluginManager.getContext().startService(intent);
	}

	@Override
	public void callback(String result, String type) {
		mPluginManager.callBack(result, type);
	}

}
