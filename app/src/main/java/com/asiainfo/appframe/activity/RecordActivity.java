package com.asiainfo.appframe.activity;

import java.io.File;
import java.io.IOException;

import com.asiainfo.appframe.R;
import com.asiainfo.appframe.data.Constants;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class RecordActivity extends Activity {

	private Context mContext;
	
	//View
	private Button mBTN_startrecord, mBTN_stoprecord, mBTN_startplay, mBTN_stopplay;
	
	//control
	private MediaPlayer mPlayer;
	private MediaRecorder mRecorder;
	
	//data
	private String Filename = null; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.appframe_activity_record);
		
		mBTN_startrecord = (Button) findViewById(R.id.startRecord);
		mBTN_stoprecord = (Button) findViewById(R.id.stopRecord);
		mBTN_startplay = (Button) findViewById(R.id.startPlay);
		mBTN_stopplay = (Button) findViewById(R.id.stopPlay);
		
		/**
		 * 开始录音
		 */
		mBTN_startrecord.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mRecorder = new MediaRecorder();
				mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
				mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
				mRecorder.setOutputFile(Filename);
				mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
				try {
					mRecorder.prepare();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mRecorder.start();
			}
		});
		
		/**
		 * 停止录音
		 */
		mBTN_stoprecord.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mRecorder.stop();  
	             mRecorder.release();  
	             mRecorder = null;
			}
		});
		
		/**
		 * 开始 播放
		 */
		mBTN_startplay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 mPlayer = new MediaPlayer();  
		            try{  
		                mPlayer.setDataSource(Filename);  
		                mPlayer.prepare();  
		                mPlayer.start();  
		            }catch(IOException e){
		            }  
			}
		});
		
		/**
		 * 停止播放
		 */
		mBTN_stopplay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mPlayer.release();  
	            mPlayer = null;  
			}
		});
		
		Filename = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.MEDIA_DIR;
		File baseFile = new File(Filename);
		if(!baseFile.exists()){
			baseFile.mkdirs();
		}
		Filename += "/" + System.currentTimeMillis() + ".3gp";
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
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
	}
	
}
