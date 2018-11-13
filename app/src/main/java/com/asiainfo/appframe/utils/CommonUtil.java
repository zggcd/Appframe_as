package com.asiainfo.appframe.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import com.asiainfo.appframe.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class CommonUtil {
	
	/**
	 * 获取屏幕宽高
	 * @param context
	 * @return
	 */
	public static int[] getWindowSize(Context context){
		
//		WindowManager wm = (WindowManager)((Activity) context).getWindowManager();
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		int width = outMetrics.widthPixels;
		int height = outMetrics.heightPixels;
		int[] wh = {height, width};
		return wh;
	}
	
	public static float[] getWindowDensity(Context context){
		
		//
		
		
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		float[] result ={metrics.xdpi, metrics.xdpi};
		return result;
	}
	
	/** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  
    
    /**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 sp
	 */
	public static int px2sp(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	/** 
     * 将sp值转换为px值，保证文字大小不变 
     *  
     * @param spValue 
     * @param fontScale 
     *            （DisplayMetrics类中属性scaledDensity） 
     * @return 
     */  
    public static int sp2px(Context context, float spValue) {  
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
        return (int) (spValue * fontScale + 0.5f);  
    }
	
	private static ConnectivityManager connectivityManager;
	/**
	 * 检测网络是否连接
	 */
	public static boolean checkNetworkState(Context ctx){
		boolean flag = false;
		//得到网络连接信息
		connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		//去进行判断网络是否连接
		if(connectivityManager.getActiveNetworkInfo() != null){
			flag = connectivityManager.getActiveNetworkInfo().isAvailable();
		}
		if(!flag){
			setNetwork(ctx);
		} else {
			isNetworkAvailable(ctx);
		}
		
		return flag;
	}
	
	/**
	 * 网络未连接时，调用设置方法
	 */
	private static void setNetwork(final Context ctx){
//		Toast.makeText(ctx, "wifi is closed!", Toast.LENGTH_SHORT).show();  
        
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);  
        builder.setIcon(R.drawable.appframe_logo);  
        builder.setTitle("网络提示信息");  
        builder.setMessage("网络不可用，如果继续，请先设置网络！");  
        builder.setPositiveButton("设置", new OnClickListener() {  
            @Override  
            public void onClick(DialogInterface dialog, int which) {  
                Intent intent = null;  
                /** 
                 * 判断手机系统的版本！如果API大于10 就是3.0+ 
                 * 因为3.0以上的版本的设置和3.0以下的设置不一样，调用的方法不同 
                 */  
                if (android.os.Build.VERSION.SDK_INT > 10) {  
                    intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);  
                } else {  
                    intent = new Intent();  
                    ComponentName component = new ComponentName(  
                            "com.android.settings",  
                            "com.android.settings.WirelessSettings");  
                    intent.setComponent(component);  
                    intent.setAction("android.intent.action.VIEW");  
                }  
                ((Activity)ctx).startActivity(intent);  
            }  
        });  
  
        builder.setNegativeButton("取消", new OnClickListener() {  
            @Override  
            public void onClick(DialogInterface dialog, int which) {  
  
            }  
        });  
        builder.create();  
        builder.show();
	}
	
	/*
	 * 网络已连接，然后去判断wifi连接还是gprs连接
	 */
	public static boolean isNetworkAvailable(Context ctx){
		@SuppressWarnings("deprecation")
		State gprs = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();  
        @SuppressWarnings("deprecation")
		State wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();  
        if(gprs == State.CONNECTED || gprs == State.CONNECTING){  
//            Toast.makeText(ctx, "wifi is open! gprs", Toast.LENGTH_SHORT).show(); 
        }  
        //判断为wifi状态下才加载广告，如果是GPRS手机网络则不加载！  
        if(wifi == State.CONNECTED || wifi == State.CONNECTING){  
//            Toast.makeText(ctx, "wifi is open! wifi", Toast.LENGTH_SHORT).show();
//            loadAdmob();  
        }
		return false;
	}

	/**
	 * 判断网络是否链接
	 *
	 * @param ctx
	 * @return boolean
	 */
	public static boolean isNetworkAvailableNew(Context ctx) {
		Context context = ctx;
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			Log.i("CommonUtil", "无网络连接");
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		Log.i("CommonUtil", "无网络连接");
		return false;
	}
	
	public static ProgressDialog commonProgressDialog = null;
	
	/***
	 * 展示共通进图对话框 showMyProgressDialog("同步数据")
	 */
	public static void showCommonProgressDialog(Context context, String msg){
		if (commonProgressDialog != null) {
			commonProgressDialog.dismiss();
			commonProgressDialog = null;
		}
		
		commonProgressDialog = ProgressDialog.show(context, "", msg, true, true);
		commonProgressDialog.setContentView(R.layout.appframe_loading_process_dialog_withmsg);
		commonProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		WindowManager.LayoutParams params = commonProgressDialog.getWindow().getAttributes();//一定要用mProgressDialog得到当前界面的参数对象
		params.width = 60;
		commonProgressDialog.getWindow().setAttributes(params);
		TextView tvMsg = (TextView)commonProgressDialog.findViewById(R.id.tv_msg);
		tvMsg.setText(msg);
		commonProgressDialog.setCanceledOnTouchOutside(false);
		
	}
	
	/***
	 * 关闭共通进度条 closeMyProgressDialog()
	 */
	public static void closeCommonProgressDialog() {
		if (commonProgressDialog != null) {
			commonProgressDialog.dismiss();
			commonProgressDialog = null;
		}
	}
	/***
	 * 返回当前共通进度条是否还在展示
	 * @return
	 */
	public static boolean isShowingCommonProgressDialog(){
		if(commonProgressDialog!=null&&
		   commonProgressDialog.isShowing()){
			return true;
		}
		return false;
	}
	

	public static String getLocalIpAddress() { 
		try { 
		    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) { 
		      NetworkInterface intf = en.nextElement(); 
		      for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) { 
		        InetAddress inetAddress = enumIpAddr.nextElement();
		        if (!inetAddress.isLoopbackAddress()) { 
		        return inetAddress.getHostAddress().toString(); 
		      } 
		    } 
		  } 
		  } catch (SocketException ex) { 
		      Log.e("", ex.toString()); 
		  } 
	  return null; 
	}

	/**
	 * 关闭程序
	 *
	 * @param act
	 */
	public static void systemExit(Activity act) {
		// System.exit(1);
		act.finish();
		killProcess();

	}

	/**
	 * 杀掉应用进程
	 */
	public static void killProcess() {
		try {
			Runtime.getRuntime().exec("kill -9 " + android.os.Process.myPid());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 自定义布局弹出框
	 *
	 * @param activity
	 * @param view
	 * @param widthPx
	 * @param heightPx
	 * @return
	 */
	public static Dialog showDialog(Activity activity, View view, int widthPx,
									int heightPx) {

		final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		Dialog dialog = builder.show();
		dialog.getWindow().setContentView(view);
		WindowManager.LayoutParams layoutParams = dialog.getWindow()
				.getAttributes();
		layoutParams.width = widthPx;
		layoutParams.height = heightPx;
		dialog.getWindow().setAttributes(layoutParams);
		dialog.setCanceledOnTouchOutside(false);
		return dialog;
	}

	/**
	 * 获取本地软件版本号
	 */
	public static PackageInfo getLocalVersion(Context ctx) {
		int localVersion = 0;
		try {
			PackageInfo packageInfo = ctx.getApplicationContext()
					.getPackageManager()
					.getPackageInfo(ctx.getPackageName(), 0);
//			localVersion = packageInfo.versionCode;
			return packageInfo;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}


}
