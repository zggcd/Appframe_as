package com.asiainfo.appframe.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.asiainfo.appframe.CommonApplication;
import com.asiainfo.appframe.R;
import com.asiainfo.appframe.activity.MainActivity;
import com.asiainfo.appframe.activity.WebControlerActivity;
import com.asiainfo.appframe.activity.WebViewActivity;
import com.asiainfo.appframe.activity.WelcomeActivity;
import com.asiainfo.appframe.bean.AccessTokenResponse;
import com.asiainfo.appframe.bean.Footbars;
import com.asiainfo.appframe.bean.HomePageInfo;
import com.asiainfo.appframe.net.ApiClient;
import com.asiainfo.appframe.utils.CommonUtil;
import com.asiainfo.appframe.utils.SDKUtil;
import com.squareup.picasso.Picasso;

public class PostFrameView_type_bottom extends LinearLayout {

private Context mContext;
    
    private List<String> mList_imgUrl;	//图片地址list
    
    //data
    private int[] screenWH;				//屏幕高宽
    private List<Footbars> mList_floorBasicInfos = new ArrayList<Footbars>();
    
  //Handler  
    @SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){ 
  
        @Override  
        public void handleMessage(Message msg) {  
            // TODO Auto-generated method stub  
            super.handleMessage(msg);
            CommonUtil.closeCommonProgressDialog();
            switch (msg.what) {
            
			case 1:
				break;
			case 2:					//跳转web页面
				AccessTokenResponse response = (AccessTokenResponse)msg.obj;
				
				if(response.getCode() == 1000){
					Intent intent = new Intent(mContext, WelcomeActivity.class);
					mContext.startActivity(intent);
					((MainActivity)mContext).finish();
					break;
				}
				
				SDKUtil.accessToken = response.getAccess_token();
				SDKUtil.expires_in = response.getExpires_in();
				SDKUtil.start_time = System.currentTimeMillis();
				
				if(mList_floorBasicInfos.get(msg.arg1).getClickurl() != null && mList_floorBasicInfos.get(msg.arg1).getClickurl().length() > 0){
//					jumpToWeb(mList_floorBasicInfos.get(msg.arg1));
					ApiClient.getClickUrl(handler, 4, mList_floorBasicInfos.get(msg.arg1).getAbilityalias());
				}
				break;
			case 3:					//刷新主页除了底部状态栏之外的页面
				((MainActivity)mContext).clearUiWithoutFootbars();
				HomePageInfo homePageInfo = (HomePageInfo)msg.obj;
				((MainActivity)mContext).refreshUI(homePageInfo);
				break;
				
			case 4:
				//跳转URL
				String clickUrl = (String) msg.obj;
				if(clickUrl != null && clickUrl.length() > 0){
					jumpToWeb(clickUrl);
				}
				break;
			default:
				break;
			}
            
        }  
        
    };
    
	public PostFrameView_type_bottom(Context context) {
		super(context);
		this.mContext = context;
	}
	
	public void setFloorBasicInfos(List<Footbars> list){
    	this.mList_floorBasicInfos = list;
    	
    	Collections.sort(mList_floorBasicInfos, new Comparator<Footbars>() {

			@Override
			public int compare(Footbars lhs, Footbars rhs) {
				if(lhs.getSort() >= rhs.getSort() ){
					return 1;
				}else{
					return -1;
				}
			}
		});
    	
    	initData();
    }
	
	/** 
     * 初始化相关Data 
     */  
    private void initData(){  
        mList_imgUrl = new ArrayList<String>();
        screenWH = CommonUtil.getWindowSize(mContext);
        // 一步任务获取图片  
        if(mList_floorBasicInfos.size() == 0 || mList_floorBasicInfos == null){
        	Toast.makeText(mContext, "样式一中无数据", Toast.LENGTH_SHORT).show();
        	return;
        }
        new GetListTask().execute("");
    } 
    
    /** 
     * 初始化Views等UI 
     */  
    private void initUI(Context context){ 
        if(mList_imgUrl == null || mList_imgUrl.size() == 0) {
            return;
        }
          
        LayoutInflater.from(context).inflate(R.layout.appframe_linearlayout_frame, this, true);
        LinearLayout content = (LinearLayout)findViewById(R.id.ll_content);
        
        ((MainActivity) mContext).setUiTitle(mList_floorBasicInfos.get(0).getTitle());
        
        //底部样式个数
        int typeSize = mList_imgUrl.size();
        for (int i = 0; i < typeSize; i++) {
        	LinearLayout view = (LinearLayout)View.inflate(mContext, R.layout.appframe_layout_bottom, null);
        	LayoutParams lp = new LayoutParams(screenWH[1] / typeSize, LayoutParams.WRAP_CONTENT);
        	lp.setMargins(0, CommonUtil.dip2px(mContext, 3), 0, CommonUtil.dip2px(mContext, 3));
        	lp.gravity = 1;
        	view.setLayoutParams(lp);
        	
        	 final int j = i;
             view.setOnClickListener(new OnClickListener() {
 				
 				@Override
 				public void onClick(View v) {
 					// TODO Auto-generated method stub
 					long a = (System.currentTimeMillis() - SDKUtil.start_time) / 1000 - Long.parseLong(SDKUtil.expires_in);
 					if(a >= 0){
 						ApiClient.refreshAccessToken(handler, 2, j, SDKUtil.phone_num, SDKUtil.mac, SDKUtil.accessToken, SDKUtil.staff_code);
 						return;
 					}
 					// TODO Auto-generated method stub
 					if(mList_floorBasicInfos.get(j).getAbilityalias() != null && mList_floorBasicInfos.get(j).getAbilityalias().length() > 0){
 						//存在clickurl，跳转web页面
 						CommonUtil.showCommonProgressDialog(mContext, "加载中");
 						ApiClient.getClickUrl(handler, 4, mList_floorBasicInfos.get(j).getAbilityalias());
 					}else{
 						//不存在clickurl，刷新除底部导航栏外的页面
 						if(mList_floorBasicInfos.get(j).getTitle().equals(((MainActivity)mContext).getUiTitle())){
 							//若标题与导航栏标题一致，则不处理
 						}else{
 							CommonUtil.showCommonProgressDialog(mContext, "加载中");
 	 						ApiClient.getUiInfo(handler, 3, SDKUtil.app_id, mList_floorBasicInfos.get(j).getId() + "", SDKUtil.appSecret);
 	 						((MainActivity) mContext).setUiTitle(mList_floorBasicInfos.get(j).getTitle());
 						}
 					}
 				}
 			});
        	
        	ImageView iv_title = (ImageView) view.findViewById(R.id.iv_title);
        	TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        	
            view.setTag(mList_imgUrl.get(i));
            Picasso.with(mContext).load(mList_imgUrl.get(i)).into(iv_title);
            tv_title.setText(mList_floorBasicInfos.get(i).getTitle());
            content.addView(view);
        }
    } 
    
    /** 
     * 异步任务,获取数据 
     *  
     */  
    class GetListTask extends AsyncTask<String, Integer, Boolean> {  
  
        @Override  
        protected Boolean doInBackground(String... params) {  
            try {  
                // 这里一般调用服务端接口获取一组轮播图片，下面是从百度找的几个图片  
            	for (Footbars info : mList_floorBasicInfos) {
                	mList_imgUrl.add(info.getPicurl());
            	}
                return true;  
            } catch (Exception e) {
                e.printStackTrace();  
                return false;  
            }  
        }  
  
        @Override  
        protected void onPostExecute(Boolean result) { 
            super.onPostExecute(result);  
            if (result) {  
                initUI(mContext);  
            }  
        }  
    }
    
    private void jumpToWeb(String webUrl){
//    	Intent intent = new Intent(mContext, WebViewActivity.class);
    	Intent intent = new Intent(mContext, WebControlerActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("webUrl", webUrl);
		intent.putExtras(bundle);
		mContext.startActivity(intent);
    }
	
}
