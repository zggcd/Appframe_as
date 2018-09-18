package com.asiainfo.appframe.webutil.plugins;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.webkit.WebView;
import android.widget.Toast;

import com.asiainfo.appframe.data.Constants;
import com.asiainfo.appframe.net.PortalClient;
import com.asiainfo.appframe.utils.StringUtil;
import com.asiainfo.appframe.webutil.BasePlugin;
import com.asiainfo.appframe.webutil.IPlugin;
import com.asiainfo.appframe.webutil.PluginManager;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MediaRecordPlugin extends BasePlugin {

	//control
	private static MediaPlayer mPlayer = null;
	private static MediaRecorder mRecorder = null;
	
	//data
	private static String BaseDir = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.MEDIA_DIR;
	private static String filename = null;
	private static boolean isRecord = false;  //是否在录制音频
	
	public MediaRecordPlugin(IPlugin ecInterface, PluginManager pm) {
		super(ecInterface, pm);
		// TODO Auto-generated constructor stub
		//初始化媒体资源
		initMedia();
	}
	
	private void initMedia(){
		File baseFile = new File(BaseDir);
		if(!baseFile.exists()){
			baseFile.mkdirs();
		}
	}
	
	/**
	 * 开始录制音频
	 */
	private void startRecord(String success, String fail){
		if(!isRecord){		//如果没有开始录制，则开始录制
			mRecorder = new MediaRecorder();
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			Date date = null;
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            date = new Date();
			filename = BaseDir + format.format(date) + ".3gp";
			mRecorder.setOutputFile(filename);
			try {
				
				mRecorder.prepare();
				mRecorder.start();
				isRecord = true;
				
				JSONObject resultObj = new JSONObject();
				try {
					resultObj.put("resultCode", 1);
					resultObj.put("resultMsg", "成功");
				} catch (JSONException e0) {
				// TODO Auto-generated catch block
					e0.printStackTrace();
				}
				callback(resultObj.toString(), success);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JSONObject resultObj = new JSONObject();
				try {
					resultObj.put("resultCode", 0);
					resultObj.put("resultMsg", e.getMessage());
				} catch (JSONException e0) {
				// TODO Auto-generated catch block
					e0.printStackTrace();
				}
				callback(resultObj.toString(), fail);
			}
		}else{
			Toast.makeText(mPluginManager.getContext(), "已经开始录音", 2000).show();
		}
	}
	
	/**
	 * 停止录音
	 */
	private void stopRecord(String success, String fail){
		if(isRecord){
			mRecorder.stop();
			mRecorder.release();
			mRecorder = null;
			isRecord = false;
			JSONObject resultObj = new JSONObject();
			try {
				resultObj.put("resultCode", 1);
				resultObj.put("resultMsg", "成功");
			} catch (JSONException e0) {
			// TODO Auto-generated catch block
				e0.printStackTrace();
			}
			callback(resultObj.toString(), success);
		}else{
			Toast.makeText(mPluginManager.getContext(), "当前没有开始录音", 2000).show();
		}
	}
	
	/**
	 * 开始播放录音
	 */
	private void startPlay(String success, String fail){
		// 多媒体播放器
		mPlayer = new MediaPlayer();
		mPlayer.reset();
		if(StringUtil.isEmpty(filename)){
			Toast.makeText(mPluginManager.getContext(), "目前尚没有录制完后的音频", Toast.LENGTH_SHORT).show();
			return;
		}
		try {
			mPlayer.setDataSource(filename);
			mPlayer.prepare();
			mPlayer.start();
			JSONObject resultObj = new JSONObject();
			try {
				resultObj.put("resultCode", 1);
				resultObj.put("resultMsg", "成功");
			} catch (JSONException e0) {
			// TODO Auto-generated catch block
				e0.printStackTrace();
			}
			callback(resultObj.toString(), success);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Toast.makeText(mPluginManager.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
			JSONObject resultObj = new JSONObject();
			try {
				resultObj.put("resultCode", 0);
				resultObj.put("resultMsg", e.getMessage());
			} catch (JSONException e0) {
			// TODO Auto-generated catch block
				e0.printStackTrace();
			}
			callback(resultObj.toString(), fail);
			e.printStackTrace();
		}
	}
	
	/**
	 * 暂停录音
	 */
	private void pausePlay(String success, String fail){
		if(mPlayer.isPlaying()){
			mPlayer.pause();
			JSONObject resultObj = new JSONObject();
			try {
				resultObj.put("resultCode", 1);
				resultObj.put("resultMsg", "成功");
			} catch (JSONException e0) {
			// TODO Auto-generated catch block
				e0.printStackTrace();
			}
			callback(resultObj.toString(), success);
		}
	}
	
	/**
	 * 继续播放
	 * @param success
	 * @param fail
	 */
	private void resumePlay(String success, String fail){
		if(!mPlayer.isPlaying()){
			mPlayer.start();
		}
	}
	
	/**
	 * 停止录音
	 */
	private void stopPlay(String success, String fail){
		if(mPlayer != null){
			if(mPlayer.isPlaying()){
				mPlayer.stop();
			}
			mPlayer.release();
			JSONObject resultObj = new JSONObject();
			try {
				resultObj.put("resultCode", 1);
				resultObj.put("resultMsg", "成功");
			} catch (JSONException e0) {
			// TODO Auto-generated catch block
				e0.printStackTrace();
			}
			callback(resultObj.toString(), success);
		}
		mPlayer = null;
	}
	
	/**
	 * 上传录音
	 */
	private void uploadAidio(final String success, final String fail, String uploadPath){
		if(StringUtil.isEmpty(uploadPath)){
			callback("无效的路径", fail);
			return;
		}
		
		if(isRecord){
			Toast.makeText(mPluginManager.getContext(), "正在录音，请停止录音重试", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(StringUtil.isEmpty(filename)){
			Toast.makeText(mPluginManager.getContext(), "目前尚没有录制完后的音频", Toast.LENGTH_SHORT).show();
			return;
		}
		
		//上传录音操作
		RequestParams requestParams = new RequestParams();
        File file = new File(filename);
        if (!file.exists()){
        	Toast.makeText(mPluginManager.getContext(), "音频不存在", Toast.LENGTH_SHORT).show();
			return;
        }

        try {
        	requestParams.put("uploadfile", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        final ProgressDialog pdDialog = ProgressDialog.show(mPluginManager.getContext(), "", "正在上传中，请稍后", true, true);
        pdDialog.setCanceledOnTouchOutside(false);

        // 设置返回，取消对话框监听，中断后台网络请求
        pdDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                PortalClient.client.cancelRequests(mPluginManager.getContext(),true);
                callback("音频上传取消", fail);
            }
        });
        
        try {
			uploadPath = URLDecoder.decode(uploadPath, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        PortalClient.post(mPluginManager.getContext(), uploadPath, requestParams, new AsyncHttpResponseHandler() {
//        	PortalClient.post(mPluginManager.getContext(), "http://zc.testpub.net/gateway/common/out/uploader", requestParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                    byte[] responseBody) {
                if (pdDialog != null && pdDialog.isShowing()) {
                    pdDialog.dismiss();
                }
                
                JSONObject object = new JSONObject();
                try {
                	object.put("name", filename);
                	object.put("localUrl", filename);
                	object.put("remoteUrl", filename);
    			} catch (JSONException e0) {
    				e0.printStackTrace();
    			}
                
                JSONObject resultObj = new JSONObject();
    			try {
    				resultObj.put("resultCode", 1);
    				resultObj.put("resultMsg", "");
    				resultObj.put("object", object);
    			} catch (JSONException e0) {
    				e0.printStackTrace();
    			}
    			callback(resultObj.toString(), success);
                
//                callback(new String(responseBody), success);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                    byte[] responseBody, Throwable error) {
                if (pdDialog != null && pdDialog.isShowing()) {
                    pdDialog.dismiss();
                }
                JSONObject resultObj = new JSONObject();
    			try {
    				resultObj.put("resultCode", 0);
    				resultObj.put("resultMsg", error.getMessage());
    			} catch (JSONException e0) {
    			// TODO Auto-generated catch block
    				e0.printStackTrace();
    			}
    			callback(resultObj.toString(), fail);
//                callback("音频上传失败", fail);
            }

        });
	}

	@Override
	public void execute(String action, JSONArray args) {
		// TODO Auto-generated method stub
		String success = args.optString(0);
        String fail = args.optString(1);
		if ("StartRecord".equals(action)) {//获取设备信息
			startRecord(success, fail);
        } else if("StopRecord".equals(action)){
        	stopRecord(success, fail);
        } else if("StartAudio".equals(action)){
        	startPlay(success, fail);
        } else if("PauseAudio".equals(action)){
        	pausePlay(success, fail);
        } else if("ContinueAudio".equals(action)){
        	resumePlay(success, fail);
        } else if("StopAudio".equals(action)){
        	stopPlay(success, fail);
        } else if("UploadAudio".equals(action)){
        	String uploadPath = args.optString(2);
        	uploadAidio(success, fail, uploadPath);
        }
	}
	
	@Override
	public void execute(String action, JSONArray args, String type) {
		String success = args.optString(0);
        String fail = args.optString(1);
        String param = "";
        
		if ("StartRecord".equals(action)) {//获取设备信息
			startRecord(success, fail);
        } else if("StopRecord".equals(action)){
        	stopRecord(success, fail);
        } else if("StartAudio".equals(action)){
        	startPlay(success, fail);
        } else if("PauseAudio".equals(action)){
        	pausePlay(success, fail);
        } else if("ContinueAudio".equals(action)){
        	resumePlay(success, fail);
        } else if("StopAudio".equals(action)){
        	stopPlay(success, fail);
        } else if("UploadAudio".equals(action)){
        	try {
				param = URLDecoder.decode(args.optString(2), "UTF-8");
				JSONObject jsonObject = new JSONObject(param);
				String uploadPath = jsonObject.getString("uploadPath");
	        	uploadAidio(success, fail, uploadPath);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        }
	}
	
	@Override
	public void callback(String result, String type) {
		// TODO Auto-generated method stub
		mPluginManager.callBack(result, type);
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
		if(mRecorder != null){
			mRecorder.stop();
			mRecorder.release();
			mRecorder = null;
		}
		if(mPlayer != null){
			if(mPlayer.isPlaying()){
				mPlayer.stop();
			}
			mPlayer.release();
			mPlayer = null;
		}
		filename = null;
		isRecord = false;
	}

}
