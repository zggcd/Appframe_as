package com.asiainfo.appframe.service;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;

import com.asiainfo.appframe.webutil.activity.ShowPaperActivity;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.webkit.MimeTypeMap;

public class DownloadFileService extends Service {

	private Context mContext;
	
	private String success = "", fail = "", url = "";
	private String filename = "";
	
	private DownloadManager downloadManager;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mContext = this;
		downloadManager = (DownloadManager)this.getSystemService(Context.DOWNLOAD_SERVICE);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		this.success = intent.getStringExtra("success");
		this.fail = intent.getStringExtra("fail");
		this.url = intent.getStringExtra("url");
		URLDecoder decoder = new URLDecoder();
		filename = getFileName(decoder.decode(url));
		downloadFile(decoder.decode(url));
		return super.onStartCommand(intent, flags, startId);
	}
	
	//获取下载的文件名
	@SuppressWarnings("static-access")
	private String getFileName(String downloadUrl){
		String filename = "";
		
		if(downloadUrl.contains("/")){
			filename = downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1, downloadUrl.length());
		}
		
		return filename.replace(" ", "");
	}
	
	private void downloadFile(String downloadurl){
		DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadurl));
	    //漫游数据是否可以下载
		request.setAllowedOverRoaming(true);
		 
		//设置文件类型，可以在下载结束后自动打开文件
//		MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
//		String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(downloadurl));
//	    request.setMimeType(mimeString);
	    //在通知栏中显示，默认是显示的
	    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
	    request.setVisibleInDownloadsUi(true);
	    //sdcard的目录下的download文件夹
//	    File file = new File(Environment.getExternalStorageDirectory()
//                .getAbsolutePath() + "/Appframe/downloadFile/");
//	    if(!file.exists()){
//	    	file.mkdirs();
//	    }
	    //下载至data目录下
	    request.setDestinationInExternalFilesDir(mContext, "/Appframe/downloadFile/", filename);
	     
	    taskId = downloadManager.enqueue(request);
	     
	   //注册广播接收者，监听下载状态
        mContext.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
		 
	}
	long taskId;
	//广播接受者，接收下载状态
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkDownloadStatus();//检查下载状态
        }
    };
	
	//检查下载状态
    private void checkDownloadStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(taskId);//筛选下载任务，传入任务ID，可变参数
        Cursor c = downloadManager.query(query);
        if (c.moveToFirst()) {            
	        int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));            
	        switch (status) {                
		        case DownloadManager.STATUS_PAUSED:
		//                    MLog.i(">>>下载暂停");                
		        case DownloadManager.STATUS_PENDING:
		//                    MLog.i(">>>下载延迟");                
		        case DownloadManager.STATUS_RUNNING:
		//                    MLog.i(">>>正在下载");                    
		        break;                
		        case DownloadManager.STATUS_SUCCESSFUL:
		//                    MLog.i(">>>下载完成"); ''                   
		        //下载完成安装APK
		                    //downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + versionName;
		//                    installAPK(new File(downloadPath));
					File file = mContext.getExternalFilesDir(null);
					String filename = file.getAbsolutePath() + "/Appframe/downloadFile/" + this.filename;
		        	//展示文本
		        	Intent intent = new Intent(mContext, ShowPaperActivity.class);
		        	intent.putExtra("url", filename);
		        	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		        	startActivity(intent);
		        	break;                
		        case DownloadManager.STATUS_FAILED:
		//                    MLog.i(">>>下载失败");
		        	Log.d("", ">>>下载失败");
		         break;
	        }
        }
    }
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
