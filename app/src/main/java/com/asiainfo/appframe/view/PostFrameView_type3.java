package com.asiainfo.appframe.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

import com.asiainfo.appframe.R;
import com.asiainfo.appframe.activity.MainActivity;
import com.asiainfo.appframe.activity.WebControlerActivity;
import com.asiainfo.appframe.activity.WebViewActivity;
import com.asiainfo.appframe.activity.WelcomeActivity;
import com.asiainfo.appframe.bean.AccessTokenResponse;
import com.asiainfo.appframe.bean.FloorBasicInfos;
import com.asiainfo.appframe.net.ApiClient;
import com.asiainfo.appframe.utils.CommonUtil;
import com.asiainfo.appframe.utils.SDKUtil;
import com.squareup.picasso.Picasso;

public class PostFrameView_type3 extends RelativeLayout {
	private Context mContext;
    
    private List<String> mList_imgUrl;	//图片地址list
    
    //data
    private int[] screenWH;				//屏幕高宽
    private List<FloorBasicInfos> mList_floorBasicInfos = new ArrayList<FloorBasicInfos>();

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
			case 2:
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
				
				ApiClient.getClickUrl(handler, 4, mList_floorBasicInfos.get(msg.arg1).getAbilityalias());
				break;
			case 3:
				//取数
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
    
	public PostFrameView_type3(Context context) {
		super(context);
		this.mContext = context;
	}
	
	public void setFloorBasicInfos(List<FloorBasicInfos> list){
    	this.mList_floorBasicInfos = list;
    	
    	Collections.sort(mList_floorBasicInfos, new Comparator<FloorBasicInfos>() {

			@Override
			public int compare(FloorBasicInfos lhs, FloorBasicInfos rhs) {
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
        FrameLayout fl_content = (FrameLayout) findViewById(R.id.fl_content);
        LinearLayout content = (LinearLayout)findViewById(R.id.ll_content);
        
        for (int i = 0; i < mList_imgUrl.size(); i++) {
            
        	String extendsabilityalias = mList_floorBasicInfos.get(i).getExtendsabilityalias();
        	final String abilityalias = mList_floorBasicInfos.get(i).getAbilityalias();
        	if(extendsabilityalias != null && extendsabilityalias != ""){
        		TextView tv_num = new TextView(mContext);
        		FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(60, 60);
        		flp.setMargins((screenWH[1] / 4 - 25 + 20) * (i + 1) - 60 + 20, 0, 0, 0);
        		tv_num.setLayoutParams(flp);
        		tv_num.setText("12");
        		tv_num.setTextColor(Color.WHITE);
        		tv_num.setTextSize(CommonUtil.sp2px(mContext, 3));
        		tv_num.setGravity(Gravity.CENTER);
        		tv_num.setBackgroundResource(R.drawable.appframe_bg_num);
        		fl_content.addView(tv_num);
        		ApiClient.getPostNum(handler, 3, extendsabilityalias, tv_num);
        	}
        	
        	View space_view = new View(mContext);
        	LinearLayout.LayoutParams space_params = new LinearLayout.LayoutParams(20,screenWH[1] / 4 - 5);
        	content.addView(space_view, space_params);
        	
        	LinearLayout ll = new LinearLayout(mContext);
        	LinearLayout.LayoutParams ll_layoutparams = new LinearLayout.LayoutParams(screenWH[1] / 4 - 25, screenWH[1] / 4 - 25 + 60);
        	ll_layoutparams.setMargins(0, 20, 0, 0);
        	ll.setOrientation(LinearLayout.VERTICAL);
        	ll.setLayoutParams(ll_layoutparams);
        	
        	//图片
        	ImageView view =  new ImageView(context);
            view.setTag(mList_imgUrl.get(i));
            view.setScaleType(ScaleType.FIT_XY);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWH[1] / 4 - 25,screenWH[1] / 4 - 25);
            Picasso.with(mContext).load(mList_imgUrl.get(i)).into(view);
            final int j = i;
            view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					CommonUtil.showCommonProgressDialog(mContext, "加载中");
					if((System.currentTimeMillis() - SDKUtil.start_time) / 1000 - Long.parseLong(SDKUtil.expires_in) >= 0){
						ApiClient.refreshAccessToken(handler, 2, j, SDKUtil.phone_num, SDKUtil.mac, SDKUtil.accessToken, SDKUtil.staff_code);
						return;
					}
					// TODO Auto-generated method stub
					ApiClient.getClickUrl(handler, 4, abilityalias);
				}
			});
            view.setLayoutParams(params);
            ll.addView(view);
            
            //文字
            TextView tv = new TextView(mContext);
            tv.setText(mList_floorBasicInfos.get(i).getTitle());
            tv.setTextColor(Color.BLACK);
            tv.setTextSize(16);
            tv.setGravity(Gravity.CENTER);
            @SuppressWarnings("unused")
			LayoutParams tv_lp = new LayoutParams(screenWH[1] / 4 - 25, 60);
            ll.addView(tv);
            
            content.addView(ll);
//            content.addView(view, params);
        }
        View space_view = new View(mContext);
    	LinearLayout.LayoutParams space_params = new LinearLayout.LayoutParams(20,screenWH[1] / 4 - 5);
    	content.addView(space_view, space_params);
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
            	for (FloorBasicInfos info : mList_floorBasicInfos) {
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
