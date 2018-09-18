package com.asiainfo.appframe.webutil.activity;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLDecoder;

import com.asiainfo.appframe.R;
import com.asiainfo.appframe.utils.FileHelper;
import com.asiainfo.appframe.utils.ResourceUtil;
import com.asiainfo.appframe.webutil.AIWebChromeClient;
import com.asiainfo.appframe.webutil.ItemOverViewControler.JsInteration;
import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.exception.FileNotFoundException;
import com.joanzapata.pdfview.listener.OnDrawListener;
import com.joanzapata.pdfview.listener.OnLoadCompleteListener;
import com.joanzapata.pdfview.listener.OnPageChangeListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.http.util.EncodingUtils;

/**
 * 显示附件activity
 * @author Stiven
 *
 */
public class ShowPaperActivity extends Activity {

	private Context mContext;
	
	//附件类型	0:office文件；
	private int mAttachment_type = 0;
	
	private String mFilePath = "";
	
	//view
//	private WebView mWV;
	private PDFView mPdfView;
	private TextView mTV_title, mTV_txt;
	private LinearLayout mLL_back;

	private String mTitle = "";
	
	@SuppressLint("WrongCall")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(ResourceUtil.getLayoutId(mContext, "appframe_activity_showpaper"));
		
		Intent intent = getIntent();
		mFilePath = intent.getStringExtra("url");

		mTV_title = (TextView) findViewById(ResourceUtil.getId(mContext, "tv_title"));
		mLL_back = (LinearLayout) findViewById(ResourceUtil.getId(mContext, "ll_back"));
		mPdfView = (PDFView) findViewById(ResourceUtil.getId(mContext, "pdfview"));
		mTV_txt = (TextView) findViewById(ResourceUtil.getId(mContext, "tv_txt"));

		mTitle = mFilePath.substring(mFilePath.lastIndexOf("/") + 1, mFilePath.length());

		mTV_title.setText(mTitle);

		if(mFilePath.contains(".txt")){
			mPdfView.setVisibility(View.GONE);
			String content = read(mFilePath);
			mTV_txt.setText(content);

		}else{
			mTV_txt.setVisibility(View.GONE);
			mPdfView.fromFile(new File(mFilePath))
					.defaultPage(1)
					.showMinimap(false)
					.enableSwipe(true)
					.onDraw(new OnDrawListener() {
						@Override
						public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

						}
					})
					.onLoad(new OnLoadCompleteListener() {
						@Override
						public void loadComplete(int nbPages) {

						}
					})
					.onPageChange(new OnPageChangeListener() {
						@Override
						public void onPageChanged(int page, int pageCount) {
							mTV_title.setText(mTitle + " " + page + "/" + pageCount);
						}
					})
					.load();
		}

		mLL_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(null != mFilePath && mFilePath.length() > 0){
					FileHelper.deleteFile(mFilePath);
				}

				finish();
			}
		});

//		mWV = (WebView) findViewById(R.id.wv);
//
//		mWV.getSettings().setAllowFileAccess(true);             // 允许访问文件
//        mWV.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//        mWV.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
//        mWV.getSettings().setSupportZoom(true);                 // 支持缩放
//        mWV.getSettings().setUseWideViewPort(true);             //关键点
//        mWV.getSettings().setSaveFormData(false);
//        mWV.getSettings().setGeolocationEnabled(true);
//        mWV.getSettings().setLoadWithOverviewMode(false);
////        mWV.getSettings().setBuiltInZoomControls(true);         // 设置显示缩放按钮
//        mWV.getSettings().setDisplayZoomControls(false);			// 设置影藏缩放按钮
//        mWV.getSettings().setDomStorageEnabled(true);
//        mWV.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//        mWV.addJavascriptInterface(new JsInteration(), "control");
//        mWV.getSettings().setJavaScriptEnabled(true);
//        mWV.addJavascriptInterface(new ProxyBridge(), "ProxyBridge");
//        AIWebChromeClient client = new AIWebChromeClient((Activity)mContext, mWV);
//        mWV.setWebChromeClient(client);
//        mWV.setWebViewClient(new WebViewClient(){
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                // TODO Auto-generated method stub
//                super.onPageFinished(view, url);
//                Log.i("info_out","pagefinished");
////                callback.pageFinish(mWV.getTitle());
//            }
//        });
//
//		if(mFilePath.contains(".xlsx") || mFilePath.contains(".doc") || mFilePath.contains(".docx")){
//			mWV.loadUrl("https://view.officeapps.live.com/op/view.aspx?src=" + mFilePath);
//		}else{
//			URLDecoder decoder = new URLDecoder();
//			mWV.loadUrl(decoder.decode(mFilePath));
//		}
		
	}

	@Override
	public void onBackPressed() {
		if(null != mFilePath && mFilePath.length() > 0){
			FileHelper.deleteFile(mFilePath);
		}

		finish();
	}

	public class JsInteration {
	        Handler handler = new Handler();
	        @JavascriptInterface
	        public void toastMessage(String message) {

	        }

	    }

	class ProxyBridge {
		String date;
		String json_date;
		DatePickerDialog pickerDialog;
		Handler handler = new Handler();

		@JavascriptInterface
		public void loadVINInputViewWithVINNO(final String vin) {
			Log.i("info_out", "vin:" + vin);
			handler.post(new Runnable() {
				@Override
				public void run() {
				}
			});
		}
	}


	private String read(String fileName) {
		StringBuffer buffer = new StringBuffer();
		try {
			FileInputStream fis = new FileInputStream(fileName);
			InputStreamReader isr = new InputStreamReader(fis,"GB2312");//文件编码Unicode,UTF-8,ASCII,GB2312,Big5
			Reader in = new BufferedReader(isr);
			int ch;
			while ((ch = in.read()) > -1) {
				buffer.append((char)ch);
			}
			in.close();
			return buffer.toString();
		} catch (IOException e) {
			return "";
		}

	}

	/**
	 * 一、私有文件夹下的文件存取（/data/data/包名/files）
	 *
	 * @param fileName
	 * @param message
	 */
	public void writeFileData(String fileName, String message) {
		try {
			FileOutputStream fout = openFileOutput(fileName, MODE_PRIVATE);
			byte[] bytes = message.getBytes();
			fout.write(bytes);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * //读文件在./data/data/包名/files/下面
	 *
	 * @param fileName
	 * @return
	 */
	public String readFileData(String fileName) {
		String res = "";
		try {
			FileInputStream fin = openFileInput(fileName);
			int length = fin.available();
			byte[] buffer = new byte[length];
			fin.read(buffer);
			res = EncodingUtils.getString(buffer, "UTF-8");
			fin.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 写， 读sdcard目录上的文件，要用FileOutputStream， 不能用openFileOutput
	 * 不同点：openFileOutput是在raw里编译过的，FileOutputStream是任何文件都可以
	 * @param fileName
	 * @param message
	 */
	// 写在/mnt/sdcard/目录下面的文件
	public void writeFileSdcard(String fileName, String message) {

		try {

			// FileOutputStream fout = openFileOutput(fileName, MODE_PRIVATE);

			FileOutputStream fout = new FileOutputStream(fileName);

			byte[] bytes = message.getBytes();

			fout.write(bytes);

			fout.close();

		}

		catch (Exception e) {

			e.printStackTrace();

		}

	}

	// 读在/mnt/sdcard/目录下面的文件

	public String readFileSdcard(String fileName) {

		String res = "";

		try {

			FileInputStream fin = new FileInputStream(fileName);

			int length = fin.available();

			byte[] buffer = new byte[length];

			fin.read(buffer);

			res = EncodingUtils.getString(buffer, "UTF-8");

			fin.close();

		}

		catch (Exception e) {

			e.printStackTrace();

		}

		return res;

	}


	/**
	 * 二、从resource中的raw文件夹中获取文件并读取数据（资源文件只能读不能写）
	 *
	 * @param fileInRaw
	 * @return
	 */
	public String readFromRaw(int fileInRaw) {
		String res = "";
		try {
			InputStream in = getResources().openRawResource(fileInRaw);
			int length = in.available();
			byte[] buffer = new byte[length];
			in.read(buffer);
			res = EncodingUtils.getString(buffer, "GBK");
			// res = new String(buffer,"GBK");
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 三、从asset中获取文件并读取数据（资源文件只能读不能写）
	 *
	 * @param fileName
	 * @return
	 */
	public String readFromAsset(String fileName) {
		String res = "";
		try {
			InputStream in = getResources().getAssets().open(fileName);
			int length = in.available();
			byte[] buffer = new byte[length];
			in.read(buffer);
			res = EncodingUtils.getString(buffer, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

}
