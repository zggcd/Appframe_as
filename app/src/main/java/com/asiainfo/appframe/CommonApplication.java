package com.asiainfo.appframe;

import com.asiainfo.appframe.data.Constants;
import com.asiainfo.appframe.exception.AndroidCrash;
import com.asiainfo.appframe.exception.httpreporter.CrashHttpReporter;
import com.asiainfo.appframe.exception.mailreporter.CrashEmailReporter;
import com.asiainfo.appframe.utils.SDKAuthCallBack;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;
public class CommonApplication extends Application{
	
	private static CommonApplication application;
	
//	//DATA
//	public static String app_id = "appf7ae0d9350c4a574";
//	public static String appSecret = "1c4ffe1ff01908430255fd6ead3364f9";
//	public static String teamKey = "";
//	
//	//data
//	public static String accessToken = "";
//	public static String expires_in;		//获取accesstaken的间隔时间
//	public static String phone_num, pwd, mac;
//	public static long start_time, end_time;
//	public static String staff_code = "";
	
	@Override
	public void onCreate() {
		super.onCreate();
		application = this;
		initEmailReporter();
		init();
	}
	
	private void init(){
		Constants.getInstance().getUiLayout = this.getResources().getString(R.string.getUiLayout);
		Constants.getInstance().getAccessToken = this.getResources().getString(R.string.getAccessToken);
		Constants.getInstance().refreshAccessToken = this.getResources().getString(R.string.refreshAccessToken);
		Constants.getInstance().preUiLayout = this.getResources().getString(R.string.preUiLayout);
		Constants.getInstance().getAreaCode = this.getResources().getString(R.string.getAreaCode);
		Constants.getInstance().getValidateCode = this.getResources().getString(R.string.getValidateCode);
		Constants.getInstance().getPostNum = this.getResources().getString(R.string.getPostNum);
		Constants.getInstance().getTeamKey = this.getResources().getString(R.string.getTeamKey);
	}
	
	/**
     * 使用EMAIL发送日志
     */
    private void initEmailReporter() {
        CrashEmailReporter reporter = new CrashEmailReporter(this);
        reporter.setReceiver("你的接收邮箱");
        reporter.setSender("你的发送邮箱");
        reporter.setSendPassword("xxxxxxxx");
        reporter.setSMTPHost("smtp.163.com");
        reporter.setPort("465");
        AndroidCrash.getInstance().setCrashReporter(reporter).init(this);
    }

    /**
     * 使用HTTP发送日志
     */
    private void initHttpReporter() {
        CrashHttpReporter reporter = new CrashHttpReporter(this) {
            /**
             * 重写此方法，可以弹出自定义的崩溃提示对话框，而不使用系统的崩溃处理。
             * @param thread
             * @param ex
             */
            @Override
            public void closeApp(Thread thread, Throwable ex) {
                final Activity activity = AppManager.currentActivity();
                Toast.makeText(activity, "发生异常，正在退出", Toast.LENGTH_SHORT).show();
                // 自定义弹出对话框
                new AlertDialog.Builder(activity).
                        setMessage("程序发生异常，现在退出").
                        setPositiveButton("确定", new DialogInterface.OnClickListener() {
		                    @Override
		                    public void onClick(DialogInterface dialog, int which) {
		                        AppManager.AppExit(activity);
		                    }
                        }).create().show();
                Log.d("MyApplication", "thead:" + Thread.currentThread().getName());
            }
        };
        reporter.setUrl("接收你请求的API").setFileParam("fileName")
                .setToParam("to").setTo("你的接收邮箱")
                .setTitleParam("subject").setBodyParam("message");
        reporter.setCallback(new CrashHttpReporter.HttpReportCallback() {
            @Override
            public boolean isSuccess(int i, String s) {
                return s.endsWith("ok");
            }
        });
        AndroidCrash.getInstance().setCrashReporter(reporter).init(this);
    }
    
    public static CommonApplication getInstance(){
    	return application;
    }
	
}