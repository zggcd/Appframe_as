package com.asiainfo.appframe.zxing;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.asiainfo.appframe.R;
import com.asiainfo.appframe.utils.ImageUtil;
import com.asiainfo.appframe.utils.ResourceUtil;
import com.asiainfo.appframe.utils.StringUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;

/**
 * 扫码页面
 */
public class CaptureActivity extends Activity implements OnClickListener,
		Callback {

	private static final int REQUEST_CODE_CHOOSE_IMG = 8001;
	
	private String resultString;
	
	private Context mContext;

	private CaptureActivityHandler mHandler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;

	private ImageView mIvBack = null;
	private TextView mTvAlbum = null;
	private ImageView mImgFlashlight = null;
	
	SurfaceView surfaceView;
	
	@Override
	protected void onResume() {
		super.onResume();
//		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		surfaceView = (SurfaceView) findViewById(ResourceUtil.getId(mContext, "preview_view"));
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mHandler != null) {
			mHandler.quitSynchronously();
			mHandler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	/**
	 * Handler scan result
	 * 
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		resultString = result.getText();
		// FIXME
		if (resultString.equals("")) {
			Toast.makeText(this, "扫描失败!", Toast.LENGTH_SHORT).show();
			CaptureActivity.this.finish();
		} else {
			// 条码扫描结果返回
			Intent intent = new Intent();
			intent.putExtra("result", result.getText());
			setResult(RESULT_OK, intent);
			finish();
			
		}
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (mHandler == null) {
			mHandler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return mHandler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();
	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

//			AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.hybrid_sdk_beep);
			AssetFileDescriptor file = getResources().openRawResourceFd(ResourceUtil.getRawId(mContext, "hybrid_sdk_beep"));

			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.hybrid_sdk_layout_zxing);
//
//		CameraManager.init(getApplication());
//		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
//
//		hasSurface = false;
//		inactivityTimer = new InactivityTimer(this);
		
		mContext = this;
//		setContentView(R.layout.hybrid_sdk_layout_zxing);
		setContentView(ResourceUtil.getLayoutId(mContext, "appframe_layout_zxing"));

		CameraManager.init(getApplication());
//		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		viewfinderView = (ViewfinderView) findViewById(ResourceUtil.getId(mContext, "viewfinder_view"));

//		mTvBack = (TextView) findViewById(R.id.tv_back);
		mIvBack = (ImageView) findViewById(ResourceUtil.getId(mContext, "iv_back"));
		mIvBack.setOnClickListener(this);
//		mTvAlbum = (TextView) findViewById(R.id.tv_album);
		mTvAlbum = (TextView) findViewById(ResourceUtil.getId(mContext, "tv_album"));
		mTvAlbum.setOnClickListener(this);
//		mImgFlashlight = (ImageView) findViewById(R.id.img_flashlight);
		mImgFlashlight = (ImageView) findViewById(ResourceUtil.getId(mContext, "img_flashlight"));
		mImgFlashlight.setOnClickListener(this);

		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		
	}
	
	@Override
	public void onClick(View v) {
		if (v == mIvBack) {
			if (mHandler != null) {
				mHandler.quitSynchronously();
				mHandler = null;
			}
			CameraManager.get().closeDriver();
			inactivityTimer.shutdown();
			finish();

		} else if (v == mTvAlbum) {
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_PICK);
			intent.setType("image/*");
			startActivityForResult(intent, REQUEST_CODE_CHOOSE_IMG);

		} else if (v == mImgFlashlight) {
			if (CameraManager.get().isLightOn()) {
				CameraManager.get().turnLightOff();
//				mImgFlashlight.setImageResource(R.drawable.flashlight_off);
				mImgFlashlight.setImageResource(ResourceUtil.getDrawableId(mContext, "appframe_flashlight_off"));
			} else {
				CameraManager.get().turnLightOn();
//				mImgFlashlight.setImageResource(R.drawable.flashlight_on);
				mImgFlashlight.setImageResource(ResourceUtil.getDrawableId(mContext, "appframe_flashlight_on"));
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (resultCode == Activity.RESULT_OK) {
			// 选择本机图片
			if (requestCode == REQUEST_CODE_CHOOSE_IMG) {
				if (intent != null) {
					Uri uri = intent.getData();
					if (!StringUtil.isEmpty(uri.getAuthority())) {
						Cursor cursor = getContentResolver().query(uri,
								new String[] { MediaStore.Images.Media.DATA },
								null, null, null);
						if (null == cursor) {
							Toast.makeText(this, "图片没找到", Toast.LENGTH_SHORT).show();
							return;
						}
						cursor.moveToFirst();
						String path = cursor.getString(cursor
								.getColumnIndex(MediaStore.Images.Media.DATA));
						cursor.close();
						// LogUtil.d("原图路径=" + path);
						String picPath = ImageUtil.getPathWithCompress(this,
								path);
						decode(picPath);

					} else {
						// LogUtil.d("原图路径=" + uri.getPath());
						String picPath = ImageUtil.getPathWithCompress(this,
								uri.getPath());
						decode(picPath);
					}
				}
			}
		}

		super.onActivityResult(requestCode, resultCode, intent);
	}

	private void decode(String picPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// newOpts.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(picPath, newOpts);
		// int w = newOpts.outWidth;
		// int h = newOpts.outHeight;

		Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>(
				3);
		Vector<BarcodeFormat> formats = new Vector<BarcodeFormat>();
		formats.addAll(DecodeFormatManager.ONE_D_FORMATS);
		formats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
		formats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
		hints.put(DecodeHintType.POSSIBLE_FORMATS, formats);
		DecodeHandler handler = new DecodeHandler(this, hints);

		Result result = handler.decodeCapture(bitmap);
		handleDecode(result, bitmap);
		
		Intent intent = new Intent();
		intent.putExtra("result", result.getText());
		setResult(RESULT_OK, intent);
		finish();
	}
}