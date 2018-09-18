package com.asiainfo.appframe.net.logic;

import android.os.Handler;

import com.asiainfo.appframe.net.ClientRequest;
import com.loopj.android.http.RequestParams;

public class UploadException extends ClientRequest {

	public UploadException(Handler handler, int what) {
		super(handler, what);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected RequestParams appendMainBody() {
		// TODO Auto-generated method stub
		return null;
	}

}
