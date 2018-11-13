package com.asiainfo.appframe.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.asiainfo.appframe.R;
import com.asiainfo.appframe.data.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.NumberFormat;


/**
 * 亚信联创 电信CRM研发部
 * 
 * @author 黄斌
 * @Description: 使用异步进行版本更新
 * @version V1.0
 * @date 2011-9-15
 */
public class AutoUpdateAsyncTask extends AsyncTask<String, Integer, String> {

	private boolean isRun = true;

	public Activity activity = null;

	// 要下载的apk大小
	public Float apkSize = null;

	// 已下载的apk大小
	public Float alreadySize = (float) 0;

	// 将要下载的apk转为的流
	private InputStream is = null;

	// 保存apk的本地流
	private FileOutputStream fos = null;

	// 保存的apk文件
	private File myTempFile = null;
	// 进度条弹窗
	private Dialog updateDialog = null;

	// 进度框布局
	private View textEntryView = null;

	// 进度条
	private ProgressBar pbSchedule = null;

	// 进度值
	private TextView tvSchedule = null;

	private String runException = "init";

	private String updateType = "all";// smart

	public AutoUpdateAsyncTask(Activity activity) {
		this.activity = activity;
	}

	/**
	 * 线程运行前准备工作 方法
	 * **/
	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		String apk_url = Constants.getInstance().desktopApkUrl;
		apk_url.substring(apk_url.lastIndexOf(".") + 1,
				apk_url.length()).toLowerCase();
		apk_url.substring(apk_url.lastIndexOf("/") + 1,
				apk_url.lastIndexOf("."));

		// 弹出进度窗
		LayoutInflater inflater = LayoutInflater.from(activity);
		textEntryView = inflater.inflate(R.layout.updateprogressbar, null);
		pbSchedule = (ProgressBar) textEntryView.findViewById(R.id.pbSchedule);
		pbSchedule.setMax(100);
		pbSchedule.setProgress(0);

		tvSchedule = (TextView) textEntryView.findViewById(R.id.tvSchedule);

		updateDialog = CommonUtil.showDialog(activity, textEntryView,
				(int) (CommonUtil.getWindowSize(activity)[1] * 0.8),
				LayoutParams.WRAP_CONTENT);
		Button cancleBtn = (Button) textEntryView
				.findViewById(R.id.cancle_update_btn);
		cancleBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isRun = false;
				updateDialog.dismiss();
				activity.finish();
			}
		});

	}

	/**
	 * 线程方法 相当于run
	 * **/
	@Override
	protected String doInBackground(String... params) {

		String strPath = params[0];

		if (!URLUtil.isNetworkUrl(strPath)) {
			Log.i(this.getClass().getName(),
					"getDataSource() It's a wrong URL!");
			runException = "error";
			return "error";
		}

		URL myURL;
		try {
			String fileEx = strPath.substring(strPath.lastIndexOf(".") + 1,
					strPath.length()).toLowerCase();// 下载文件后缀
			String fileNa = strPath.substring(strPath.lastIndexOf("/") + 1,
					strPath.lastIndexOf("."));// 下载文件名
			if (fileNa.length() < 3) {
				fileNa = fileNa + System.currentTimeMillis();
			}
			String sdPath = Environment.getExternalStorageDirectory() + "/Appframe";// 或得sd卡路径
			Log.v("sdPath", sdPath);
			File dir = new File(sdPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			myTempFile = new File(sdPath + "/" + "appframe" + "." + "apk");
			updateType = "all";
			myURL = new URL(strPath);
			HttpURLConnection conn = (HttpURLConnection) myURL.openConnection();
			conn.setReadTimeout(6000);
			conn.setConnectTimeout(3000);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept-Encoding", "identity");
			conn.connect();
			is = conn.getInputStream();
			if (is == null) {
				throw new RuntimeException("stream is null");
			}

//			apkSize = (float) conn.getContentLength();
			apkSize = Float.parseFloat(params[1]);
			myTempFile.createNewFile();
			fos = new FileOutputStream(myTempFile);

			byte buf[] = new byte[1024];
			do {
				int numread = is.read(buf);
				if (numread <= 0) {
					break;
				}
				fos.write(buf, 0, numread);
				alreadySize = alreadySize + buf.length;
				NumberFormat nf = NumberFormat.getPercentInstance();
				String proportion = nf.format(alreadySize / apkSize);

				// 往UI线程传进度值
				publishProgress(Integer.valueOf(proportion.substring(0,
						proportion.length() - 1)));

			} while (isRun);
		} catch (Exception e) {
			e.printStackTrace();
			runException = "error";
			return "error";
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}

	/**
	 * 线程进度百分比
	 * **/
	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);

		pbSchedule.setProgress(values[0]);

		int val = values[0];
		if (val >= 100) {
			val = 99;
		}
		tvSchedule.setText(val + "%");
	}

	/**
	 * 线程运行结束方法
	 * **/
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);

		if (runException.equals("error")) {

			AlertDialog.Builder aBuilder = new AlertDialog.Builder(activity)
					.setTitle("版本更新")
					.setIcon(R.drawable.imsgico)
					.setMessage("网络中断,请保持网络稳定后再重新升级");
			aBuilder.setPositiveButton("重新升级",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							AutoUpdateAsyncTask auat = new AutoUpdateAsyncTask(
									activity);
							auat.execute(Constants.getInstance().desktopApkUrl);
						}

					});
			aBuilder.setNegativeButton("放弃升级",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							if (updateDialog != null) {
								updateDialog.dismiss();
							}
							activity.finish();
						}

					});

			aBuilder.show();

			return;
		}

		pbSchedule.setProgress(100);
		tvSchedule.setText("100%");

		if (isRun && myTempFile != null) {
			if (updateType.equals("all")) {
				Intent intent = new Intent();
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				intent.setAction(Intent.ACTION_VIEW);
				String type = getMIMEType(myTempFile);
				Uri contentUri = null;
				if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N){
					intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
					contentUri = FileProvider.getUriForFile(activity, "com.asiainfo.appframe", myTempFile);
				}else{
					contentUri = Uri.fromFile(myTempFile);
				}

				intent.setDataAndType(contentUri, type);
				activity.startActivityForResult(intent, 666);
//				if (activity instanceof WelcomeActivity) {
					activity.finish();
//				} else if (activity instanceof AboutActivity) {
//					activity.finish();
//					ActivityTaskManager.getInstance().closeAllActivity();
//					// 清空活动activity列表
//					for (Activity act : Constants.getInstance().activityList) {
//						act.finish();
//					}
//				}
			}
		}
	}

	/**
	 * 取消线程时调用的方法
	 * **/
	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	private String getMIMEType(File f) {
		String type = "";
		String fName = f.getName();
		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length())
				.toLowerCase();
		if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
				|| end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
			type = "audio";
		} else if (end.equals("3gp") || end.equals("mp4")) {
			type = "video";
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			type = "image";
		} else if (end.equals("apk")) {
			type = "application/vnd.android.package-archive";
		} else {
			type = "*";
		}
		if (end.equals("apk")) {
		} else {
			type += "/*";
		}
		return type;
	}

}
