/**
 * 
 */
package com.asiainfo.appframe.net;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.content.Context;
import android.util.Log;

import com.asiainfo.appframe.CommonApplication;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 亚信联创 电信EC产品研发
 * 
 * @author 李国荣
 * @Description:
 * @version V1.0
 * @date 2013-11-8
 * 
 * 
 *       门户请求基类 提供：get，post，上传，下载等请求
 */

public class PortalClient {
	private static final String BASE_URL = "";
	private static String CONTENTTYPE = "application/x-www-form-urlencoded;charset=UTF-8";
	private static String ENCODING = "UTF-8";

	public static AsyncHttpClient client = new AsyncHttpClient();

	public static Context mContext = null;
	
	public static void get(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		mContext = CommonApplication.getInstance();
		client.get(getAbsoluteUrl(url), params, responseHandler);
		
	}

	public static void post(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		mContext = CommonApplication.getInstance();
		client.post(getAbsoluteUrl(url), params, responseHandler);

	}
	
	public static void post(Context context, String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		mContext = context;
		client.setURLEncodingEnabled(true);
		client.post(getAbsoluteUrl(url), params, responseHandler);

	}

	// 门户层post请求，返回出来在responseHandler接口的实现类中进�?
	public static void post(String url, String entity,
			AsyncHttpResponseHandler responseHandler) {
		try {
			Log.d("PortalClient", entity);
			client.post(mContext, getAbsoluteUrl(url),
					new StringEntity(entity, ENCODING), CONTENTTYPE,
					responseHandler);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static String getAbsoluteUrl(String relativeUrl) {
		return BASE_URL + relativeUrl;
	}

	private static void cancle() {
		client.cancelRequests(mContext, true);
	}

	public static void shutdown() {
		cancle();
	}

}
