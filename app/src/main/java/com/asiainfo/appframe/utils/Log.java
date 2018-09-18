package com.asiainfo.appframe.utils;

public class Log {
	private static final String TAG = "AppFrame";

	// 系统日志的控制开关
	public static final boolean DEBUG = true;

	public static void v(String tag, String msg) {
		if (DEBUG) {
			android.util.Log.v(tag, msg);
		}
	}

	public static void i(String msg) {
		if (DEBUG) {
			android.util.Log.i(TAG, msg);
		}
	}

	public static void i(String tag, String msg) {
		if (DEBUG) {
			android.util.Log.i(tag, msg);
		}
	}

	public static void d(String msg) {
		if (DEBUG) {
			android.util.Log.d(TAG, msg);
		}
	}

	public static void d(String tag, String msg) {
		if (DEBUG) {
			android.util.Log.d(tag, msg);
		}
	}

	public static void d(String tag, String msg, Throwable tr) {
		if (DEBUG) {
			android.util.Log.d(tag, msg, tr);
		}
	}

	public static void w(String tag, String msg, Throwable tr) {
		if (DEBUG) {
			android.util.Log.w(tag, msg, tr);
		}
	}

	public static void e(String msg) {
		if (DEBUG) {
			android.util.Log.e(TAG, msg);
		}
	}

	public static void e(String msg, Throwable e) {
		if (DEBUG) {
			android.util.Log.e(TAG, msg, e);
		}
	}

	public static void e(String tag, String msg) {
		if (DEBUG) {
			android.util.Log.e(tag, msg);
		}
	}

	public static void e(String tag, String msg, Throwable e) {
		if (DEBUG) {
			android.util.Log.e(tag, msg, e);
		}
	}

	public static void trace(String tag, String msg) {
		if (DEBUG) {
			android.util.Log.i(tag, msg);
		}
	}
}
