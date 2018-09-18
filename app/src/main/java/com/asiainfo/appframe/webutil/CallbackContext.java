package com.asiainfo.appframe.webutil;

import android.content.Context;

public interface CallbackContext {
	public Context getContext();

	public void callBack(String statement, String callbackId);
}
