package com.asiainfo.appframe.net.logic;

import java.io.File;

import android.os.Handler;

import com.asiainfo.appframe.net.ClientRequest;
import com.asiainfo.appframe.utils.SDKUtil;
import com.loopj.android.http.RequestParams;

public class UploadImageRequest extends ClientRequest {

	private int sourceType;
	private File uploadfile;
	private String resourceSeq;
	
	public UploadImageRequest(String url, Handler handler, int what,int sourceType, File uploadfile, String resourceSeq) {
		super(handler, what);
		this.sourceType = sourceType;
		this.uploadfile = uploadfile;
		this.resourceSeq = resourceSeq;
		formRequest(false, url + SDKUtil.accessToken);
	}

	@Override
	protected RequestParams appendMainBody() {
		RequestParams params = new RequestParams();
		try {
			params.put("sourceType", sourceType);
			params.put("uploadfile", uploadfile);
			params.put("resourceSeq", resourceSeq);
			return params;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void onPostSuccess(String resultStr) {
		super.onPostSuccess(resultStr);
	}
	
	@Override
	public void onPostFail(Exception e) {
	}

}
