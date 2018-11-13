package com.asiainfo.appframe.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.asiainfo.appframe.R;
import com.asiainfo.appframe.activity.BaseActivity;
import com.asiainfo.appframe.activity.LoginNewActivity;
import com.asiainfo.appframe.activity.WelcomeActivity;
import com.asiainfo.appframe.bean.UpgradeInfo;
import com.asiainfo.appframe.bean.UpgradeInfoBean;
import com.asiainfo.appframe.data.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/* =========================================================
 * 亚信联创 电信CRM研发部
 * @author 郑晖
 * @LoginActivity 2011-05-19 14:55
 * @Description: 程序升级
 * @version V1.0
 * =========================================================
 * upLoginActivity LoginActivity                upLoginActivity author     Description
 * 2011-05-19                                                   郑晖                            创建文件
 * 2011-06-02                                                   翁德辉                       doDownloadTheFile里面的对于流的操作需进行异常保护 fos,is
 * 2011-06-06                                                   郑晖                            修改AlertDialog外部可做判断
 */
public class AutoUpdate {
	private static final int SHORT_LINK = 20002;
	public Activity activity = null;
	public int versionCode = 0;
	public String versionName = "";
	private String currentFilePath = "";
	private String currentTempFilePath = "";
	private String fileEx = "";
	private String fileNa = "";
	private ProgressDialog dialog;
	private Handler handler;
	private Intent intent;

	/** 获取的版本更新内容 **/
	private List<Map<String, Object>> valueList = null;

	public AutoUpdate(Activity activity, Handler handler, Intent intent) {
		this.activity = activity;
		this.handler = handler;
		this.intent = intent;
	}

	// 软件更新
	public void update(boolean isVersion, final boolean isForce,
			final UpgradeInfo upgrade) {

		if (isVersion) {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					showUpdateDialog(upgrade, isForce);
				}
			}, 300);
		} else {
			// 版本不需要更新自定义的逻辑
			if (activity instanceof LoginNewActivity) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						jumpToLogin();
					}
				});
			}
		}
	}

	// 软件更新
	public void update(boolean isVersion, final boolean isForce,
					   final UpgradeInfoBean upgrade) {

		if (isVersion) {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					showUpdateDialog(upgrade, isForce);
				}
			}, 300);
		} else {
			// 版本不需要更新自定义的逻辑
			if (activity instanceof LoginNewActivity) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						jumpToLogin();
					}
				});
			}
		}
	}


	/***
	 * 跳转到登录页
	 */
	public void jumpToLogin(){

		SharedPreferences preferences = activity
				.getApplicationContext()
				.getSharedPreferences(
						Constants.getInstance().SHAREDPREFERENCES_NAME,
						activity.MODE_PRIVATE);

		boolean isFirstIn = preferences.getBoolean("isFirstIn", true);
		Bundle bundle = new Bundle();
		bundle.putString("shortUrl", intent.getDataString());
		((BaseActivity) activity).jumpTo(LoginNewActivity.class, bundle);
	}

	// 判断是否更新
	@SuppressWarnings("rawtypes")
	public boolean isVersion() throws Exception {
		getCurrentVersion();
		try {
			// 获取资源文件信息 (XML)
			URL url = new URL(Constants.getInstance().desktopUrl
					+ activity.getString(R.string.versionXmlUrl));
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			int code = conn.getResponseCode();
			if (HttpURLConnection.HTTP_OK == code) {

				valueList = XMLReader.getXMLList(url.openStream());

				if (valueList == null) {
					return false;
				}

				for (Map map : valueList) {
					String mapKey = map.get("xmlParserName").toString();
					if (mapKey.equals("version")) {
						int latestVersionCode = Integer.parseInt(map
								.get("code").toString()); // code
						String latestVersionName = map.get("name").toString(); // name
						Log.i("版本对比", versionCode + "_" + latestVersionCode);
						// 根据versionCode校验版本
						if (latestVersionCode > versionCode) {
							return true;
						} else {
							return false;
						}
					}
				}
			}
		} catch (SocketTimeoutException e) {
			Log.e("校验版本连接超时", e);
		}

		return false;
	}

	// 初始化获取当前版本信息
	public PackageInfo getCurrentVersion() {
		PackageInfo info = null;
		try {
			info = activity.getPackageManager().getPackageInfo(
					activity.getPackageName(), 0);
			this.versionCode = info.versionCode;
			this.versionName = info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return info;
	}

	// 升级弹出提示框
	public void showUpdateDialog(UpgradeInfo upgrade, boolean isForce) {

		if (isForce) {
			showForceUpdateDialog(upgrade);
		} else {
			showUnForceUpdateDialog(upgrade);
		}
	}

	// 升级弹出提示框
	public void showUpdateDialog(UpgradeInfoBean upgrade, boolean isForce) {

		if (isForce) {
			showForceUpdateDialog(upgrade);
		} else {
			showUnForceUpdateDialog(upgrade);
		}
	}

	/**
	 * 强制更新
	 *
	 * @param upgrade
	 */
	private void showForceUpdateDialog(UpgradeInfo upgrade) {
		LayoutInflater inflater = LayoutInflater.from(activity);
		View textEntryView = inflater.inflate(R.layout.force_update, null);

		TextView tvVId = (TextView) textEntryView.findViewById(R.id.tvVId);
		LinearLayout llContent = (LinearLayout) textEntryView.findViewById(R.id.llContent);
		tvVId.setText("(" + upgrade.softVersion + ")");
		TextView textView = new TextView(activity);
		textView.setText(upgrade.contentDesc);
		textView.setTextColor(Color.parseColor("#585858"));
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
		llContent.addView(textView);
		final Dialog dialog = CommonUtil.showDialog(activity, textEntryView, (int) (SystemPreference.getInt(activity, "screenWidth") * 0.8),
						(int) (SystemPreference
								.getInt(activity, "screenHeight") * 0.4));

		Button updateBtn = (Button) textEntryView.findViewById(R.id.update_btn);
		updateBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AutoUpdateAsyncTask auat = new AutoUpdateAsyncTask(activity);
				auat.execute(Constants.getInstance().desktopApkUrl);
			}
		});

		dialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				if (activity instanceof WelcomeActivity) {
					CommonUtil.systemExit(activity);
				}
			}
		});
	}

	/**
	 * 强制更新
	 *
	 * @param upgrade
	 */
	private void showForceUpdateDialog(UpgradeInfoBean upgrade) {
		LayoutInflater inflater = LayoutInflater.from(activity);
		View textEntryView = inflater.inflate(R.layout.force_update, null);

		TextView tvVId = (TextView) textEntryView.findViewById(R.id.tvVId);
		LinearLayout llContent = (LinearLayout) textEntryView.findViewById(R.id.llContent);
		tvVId.setText("(" + upgrade.getResultObj().getVersionName() + ")");
		TextView textView = new TextView(activity);
		textView.setText(upgrade.getResultObj().getVersionLog());
		textView.setTextColor(Color.parseColor("#585858"));
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
		llContent.addView(textView);
		final Dialog dialog = CommonUtil.showDialog(activity, textEntryView, (int)(CommonUtil.getWindowSize(activity)[1] * 0.8),
				(int) (CommonUtil.getWindowSize(activity)[0] * 0.4));

		Button updateBtn = (Button) textEntryView.findViewById(R.id.update_btn);
		updateBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AutoUpdateAsyncTask auat = new AutoUpdateAsyncTask(activity);
				auat.execute(Constants.getInstance().desktopApkUrl, upgrade.getResultObj().getVersionSize());
			}
		});

		dialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				if (activity instanceof WelcomeActivity) {
					CommonUtil.systemExit(activity);
				}
			}
		});
	}

	class MyOnClickListener implements OnClickListener {
		boolean isIgnore;

		public MyOnClickListener(boolean isIgnore) {
			this.isIgnore = isIgnore;
		}

		@Override
		public void onClick(View v) {

		}
	}

	private void showUnForceUpdateDialog(final UpgradeInfo upgrade) {
		LayoutInflater inflater = LayoutInflater.from(activity);
		View textEntryView = inflater.inflate(R.layout.unforce_update, null);

		TextView tvVId = (TextView) textEntryView.findViewById(R.id.tvVId);
		LinearLayout llContent = (LinearLayout) textEntryView
				.findViewById(R.id.llContent);
		tvVId.setText("(" + upgrade.softVersion + ")");
		TextView textView = new TextView(activity);
		textView.setText(upgrade.contentDesc);
		textView.setTextColor(Color.parseColor("#585858"));
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
		llContent.addView(textView);
		View ignoreView = inflater.inflate(R.layout.ignore_update_item, null);
		final ImageView ignoreImg = (ImageView) ignoreView
				.findViewById(R.id.ignore_img);
		llContent.addView(ignoreView);
		String ignoreVcode = SystemPreference.getString(
				activity.getApplicationContext(), Constants.IGNORE);
		if (upgrade.softVersoinCode.equals(ignoreVcode)) {
			ignoreImg.setBackgroundResource(R.drawable.select);
		}
		ignoreView.setOnClickListener(new MyOnClickListener(
				upgrade.softVersoinCode.equals(ignoreVcode)) {

			@Override
			public void onClick(View v) {
				if (isIgnore) {
					ignoreImg.setBackgroundResource(R.drawable.unselect);
					SystemPreference.remove(activity.getApplicationContext(),
							Constants.IGNORE);
				} else {
					ignoreImg.setBackgroundResource(R.drawable.select);
					SystemPreference.setString(
							activity.getApplicationContext(), Constants.IGNORE,
							upgrade.softVersoinCode);
				}
				isIgnore = !isIgnore;
			}
		});

		final Dialog dialog = CommonUtil.showDialog(activity, textEntryView, (int) (SystemPreference
						.getInt(activity, "screenWidth") * 0.8),
						(int) (SystemPreference
								.getInt(activity, "screenHeight") * 0.4));

		Button cancelBtn = (Button) textEntryView.findViewById(R.id.cancelBtn);
		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (activity instanceof WelcomeActivity) {
					handler.post(new Runnable() {

						@Override
						public void run() {
							jumpToLogin();
						}
					});
				} else {
					if (dialog != null) {
						dialog.dismiss();
					}
				}
			}
		});
		Button confirmBtn = (Button) textEntryView
				.findViewById(R.id.confirmBtn);
		confirmBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AutoUpdateAsyncTask auat = new AutoUpdateAsyncTask(activity);
				auat.execute(Constants.getInstance().desktopApkUrl);
			}
		});

		dialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				if (activity instanceof WelcomeActivity) {
					CommonUtil.systemExit(activity);
				}
			}
		});
	}

	private void showUnForceUpdateDialog(final UpgradeInfoBean upgrade) {
		LayoutInflater inflater = LayoutInflater.from(activity);
		View textEntryView = inflater.inflate(R.layout.unforce_update, null);

		TextView tvVId = (TextView) textEntryView.findViewById(R.id.tvVId);
		LinearLayout llContent = (LinearLayout) textEntryView
				.findViewById(R.id.llContent);
		tvVId.setText("(" + upgrade.getResultObj().getVersionName() + ")");
		TextView textView = new TextView(activity);
		textView.setText(upgrade.getResultObj().getVersionLog());
		textView.setTextColor(Color.parseColor("#585858"));
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
		llContent.addView(textView);
		View ignoreView = inflater.inflate(R.layout.ignore_update_item, null);
		final ImageView ignoreImg = (ImageView) ignoreView
				.findViewById(R.id.ignore_img);
		llContent.addView(ignoreView);
		String ignoreVcode = SystemPreference.getString(
				activity.getApplicationContext(), Constants.IGNORE);
		if (upgrade.getResultObj().getVersionName().equals(ignoreVcode)) {
			ignoreImg.setBackgroundResource(R.drawable.select);
		}
		ignoreView.setOnClickListener(new MyOnClickListener(upgrade.getResultObj().getVersionName().equals(ignoreVcode)) {

			@Override
			public void onClick(View v) {
				if (isIgnore) {
					ignoreImg.setBackgroundResource(R.drawable.unselect);
					SystemPreference.remove(activity.getApplicationContext(),
							Constants.IGNORE);
				} else {
					ignoreImg.setBackgroundResource(R.drawable.select);
					SystemPreference.setString(
							activity.getApplicationContext(), Constants.IGNORE,
							upgrade.getResultObj().getVersionName());
				}
				isIgnore = !isIgnore;
			}
		});

		final Dialog dialog = CommonUtil.showDialog(activity, textEntryView, (int) (SystemPreference
						.getInt(activity, "screenWidth") * 0.8),
				(int) (SystemPreference
						.getInt(activity, "screenHeight") * 0.4));

		Button cancelBtn = (Button) textEntryView.findViewById(R.id.cancelBtn);
		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (activity instanceof WelcomeActivity) {
					handler.post(new Runnable() {

						@Override
						public void run() {
							jumpToLogin();
						}
					});
				} else {
					if (dialog != null) {
						dialog.dismiss();
					}
				}
			}
		});
		Button confirmBtn = (Button) textEntryView
				.findViewById(R.id.confirmBtn);
		confirmBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AutoUpdateAsyncTask auat = new AutoUpdateAsyncTask(activity);
				auat.execute(Constants.getInstance().desktopApkUrl);
			}
		});

		dialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				if (activity instanceof WelcomeActivity) {
					CommonUtil.systemExit(activity);
				}
			}
		});
	}

	// 等待提示框
	public void showWaitDialog() {
		dialog = new ProgressDialog(activity);
		dialog.setMessage("数据载入中，请稍候！");
		dialog.setIndeterminate(true);
		dialog.setCancelable(true);
		dialog.show();
	}

	// 文件下载
	private void downloadTheFile(final String strPath) {

		String apk_url = Constants.getInstance().desktopApkUrl;
		fileEx = apk_url.substring(apk_url.lastIndexOf(".") + 1,
				apk_url.length()).toLowerCase();
		fileNa = apk_url.substring(apk_url.lastIndexOf("/") + 1,
				apk_url.lastIndexOf("."));
		try {
			if (strPath.equals(currentFilePath)) {
				doDownloadTheFile(strPath);
			}
			currentFilePath = strPath;
			Runnable r = new Runnable() {
				public void run() {
					try {
						doDownloadTheFile(strPath);
					} catch (Exception e) {
						Log.e(this.getClass().getName(), e.getMessage(), e);
					}
				}
			};
			new Thread(r).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void doDownloadTheFile(String strPath) throws Exception {
		Log.i(this.getClass().getName(), "getDataSource()");
		if (!URLUtil.isNetworkUrl(strPath)) {
			Log.i(this.getClass().getName(),
					"getDataSource() It's a wrong URL!");
		} else {
			URL myURL = new URL(strPath);
			URLConnection conn = myURL.openConnection();
			conn.connect();
			InputStream is = conn.getInputStream();
			if (is == null) {
				throw new RuntimeException("stream is null");
			}
			FileOutputStream fos = null;
			try {

				File myTempFile = File.createTempFile(fileNa, "." + fileEx);
				currentTempFilePath = myTempFile.getAbsolutePath();
				fos = new FileOutputStream(myTempFile);
				byte buf[] = new byte[128];
				do {
					int numread = is.read(buf);
					if (numread <= 0) {
						break;
					}
					fos.write(buf, 0, numread);
				} while (true);
				Log.i(this.getClass().getName(),
						"getDataSource() Download  ok...");
				dialog.cancel();
				dialog.dismiss();
				openFile(myTempFile);

			} catch (Exception ex) {
				Log.e(this.getClass().getName(),
						"getDataSource() error: " + ex.getMessage(), ex);
			} finally {
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void openFile(File f) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		String type = getMIMEType(f);
		intent.setDataAndType(Uri.fromFile(f), type);
		activity.startActivity(intent);
	}

	public void delFile() {
		Log.i(this.getClass().getName(), "The TempFile(" + currentTempFilePath
				+ ") was deleted.");
		File myFile = new File(currentTempFilePath);
		if (myFile.exists()) {
			myFile.delete();
		}
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