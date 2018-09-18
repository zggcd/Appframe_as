package com.asiainfo.appframe.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.asiainfo.appframe.CommonApplication;
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
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * 广告轮播视图
 * * ViewPager实现的轮播图广告自定义视图，如京东首页的广告轮播图效果； 
 * 既支持自动轮播页面也支持手势滑动切换页面 
 * @author Stiven
 *
 */
@SuppressLint("HandlerLeak")
public class PostFrameView_type1 extends RelativeLayout {

	// 使用universal-image-loader插件读取网络图片，需要工程导入universal-image-loader-1.8.6-with-sources.jar  
    private ImageLoader imageLoader = ImageLoader.getInstance();  
  
    //轮播图图片数量  
    private final static int IMAGE_COUNT = 5;  
    //自动轮播的时间间隔  
//    private final static int TIME_INTERVAL = 5;  
    //自动轮播启用开关  
    private final static boolean isAutoPlay = true;   
      
    //自定义轮播图的资源  
    private List<String> imageUrls = new ArrayList<String>();  
    //放轮播图片的ImageView 的list  
    private List<ImageView> imageViewsList;  
    //放圆点的View的list  
    private List<View> dotViewsList;  
      
    private ViewPager viewPager;  
    //当前轮播页  
    private int currentItem  = 0;  
    //定时任务  
    private ScheduledExecutorService scheduledExecutorService;
      
    private Context context;
    
    //DATA
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
				viewPager.setCurrentItem(currentItem);
				break;
			case 2:
				AccessTokenResponse response = (AccessTokenResponse)msg.obj;
				
				if(response.getCode() == 1000){
					Intent intent = new Intent(context, WelcomeActivity.class);
					context.startActivity(intent);
					((MainActivity)context).finish();
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
    
    public PostFrameView_type1(Context context) {  
        this(context, null);
        this.context = context;
        initImageLoader(context);
    }
    
    public PostFrameView_type1(Context context, AttributeSet attrs) {  
        this(context, attrs, 0);  
        // TODO Auto-generated constructor stub  
    }  
    public PostFrameView_type1(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle); 
//        this.context = context; 
//  
//        initImageLoader(context);
//          
//        initData();  
//        if(isAutoPlay){
//            startPlay();  
//        }  
          
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
        if(isAutoPlay){
            startPlay();
        }
    }
    
    /** 
     * 开始轮播图切换 
     */  
    private void startPlay(){  
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, 4, TimeUnit.SECONDS);  
    }  
    /** 
     * 停止轮播图切换 
     */  
    @SuppressWarnings("unused")
	private void stopPlay(){  
        scheduledExecutorService.shutdown();
    }  
    /** 
     * 初始化相关Data 
     */  
    private void initData(){  
        imageViewsList = new ArrayList<ImageView>();
        dotViewsList = new ArrayList<View>();
  
        if(mList_floorBasicInfos.size() == 0 || mList_floorBasicInfos == null){
        	Toast.makeText(context, "样式一中无数据", Toast.LENGTH_SHORT).show();
        	return;
        }
        
        // 一步任务获取图片  
        new GetListTask().execute("");
    }  
    /** 
     * 初始化Views等UI 
     */  
    private void initUI(final Context context){ 
        if(imageUrls == null || imageUrls.size() == 0)  
            return;  
          
        LayoutInflater.from(context).inflate(R.layout.appframe_layout_slideshow, this, true);
          
        LinearLayout dotLayout = (LinearLayout)findViewById(R.id.dotLayout);  
        dotLayout.removeAllViews();  
          
        // 热点个数与图片特殊相等  
        for (int i = 0; i < imageUrls.size(); i++) {  
            ImageView view =  new ImageView(context);  
            view.setTag(imageUrls.get(i));  
//            if(i==0)//给一个默认图  
//                view.setBackgroundResource(R.drawable.login_top_bg);  
            view.setScaleType(ScaleType.FIT_XY);
            imageViewsList.add(view);
              
            ImageView dotView =  new ImageView(context);  
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);  
            params.leftMargin = 4;  
            params.rightMargin = 4;  
            
            final int j = i;
            view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					CommonUtil.showCommonProgressDialog(context, "加载中");
					if((System.currentTimeMillis() - SDKUtil.start_time) / 1000 - Long.parseLong(SDKUtil.expires_in) >= 0){
						ApiClient.refreshAccessToken(handler, 2, j, SDKUtil.phone_num, SDKUtil.mac, SDKUtil.accessToken, SDKUtil.staff_code);
						return;
					}
					// TODO Auto-generated method stub
					String abilityalias = mList_floorBasicInfos.get(j).getAbilityalias();
					ApiClient.getClickUrl(handler, 4, abilityalias);
				}
			});
            
            dotLayout.addView(dotView, params);
            dotViewsList.add(dotView);
        }  
          
        viewPager = (ViewPager) findViewById(R.id.viewPager);  
        
        viewPager.setFocusable(true);  
          
        viewPager.setAdapter(new MyPagerAdapter());
        viewPager.setOnPageChangeListener(new MyPageChangeListener());  
    }
    
    private void jumpToWeb(String webUrl){
//    	Intent intent = new Intent(context, WebViewActivity.class);
    	Intent intent = new Intent(context, WebControlerActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("webUrl", webUrl);
		intent.putExtras(bundle);
		context.startActivity(intent);
    }
      
    /** 
     * 填充ViewPager的页面适配器 
     *  
     */  
    private class MyPagerAdapter  extends PagerAdapter{  
  
        @Override  
        public void destroyItem(View container, int position, Object object) {  
            // TODO Auto-generated method stub  
            //((ViewPag.er)container).removeView((View)object);  
            ((ViewPager)container).removeView(imageViewsList.get(position));  
        }  
  
        @Override
        public Object instantiateItem(View container, int position) {  
            ImageView imageView = imageViewsList.get(position);
  
            imageLoader.displayImage(imageView.getTag() + "", imageView);  
              
            ((ViewPager)container).addView(imageViewsList.get(position));  
            return imageViewsList.get(position);  
        }  
  
        @Override  
        public int getCount() {  
            // TODO Auto-generated method stub  
            return imageViewsList.size();  
        }  
  
        @Override  
        public boolean isViewFromObject(View arg0, Object arg1) {  
            // TODO Auto-generated method stub  
            return arg0 == arg1;  
        }  
        @Override  
        public void restoreState(Parcelable arg0, ClassLoader arg1) {  
            // TODO Auto-generated method stub  
  
        }  
  
        @Override  
        public Parcelable saveState() {  
            // TODO Auto-generated method stub  
            return null;  
        }  
  
        @Override  
        public void startUpdate(View arg0) {  
            // TODO Auto-generated method stub  
  
        }  
  
        @Override  
        public void finishUpdate(View arg0) {  
            // TODO Auto-generated method stub  
              
        }  
          
    }  
    /** 
     * ViewPager的监听器 
     * 当ViewPager中页面的状态发生改变时调用 
     *  
     */  
    private class MyPageChangeListener implements OnPageChangeListener{  
  
        boolean isAutoPlay = false;  
  
        @Override  
        public void onPageScrollStateChanged(int arg0) { 
            // TODO Auto-generated method stub  
            switch (arg0) {  
            case 1:// 手势滑动，空闲中  
                isAutoPlay = false;  
                break;  
            case 2:// 界面切换中  
                isAutoPlay = true;  
                break;  
            case 0:// 滑动结束，即切换完毕或者加载完毕 
//                 当前为最后一张，此时从右向左滑，则切换到第一张
                if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 && !isAutoPlay) {  
                    viewPager.setCurrentItem(0);  
                }  
                // 当前为第一张，此时从左向右滑，则切换到最后一张
                else if (viewPager.getCurrentItem() == 0 && !isAutoPlay) {  
                    viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 1);  
                }  
                break;  
        }  
        }  
  
        @Override  
        public void onPageScrolled(int arg0, float arg1, int arg2) {  
            // TODO Auto-generated method stub  
              
        }  
  
        @Override  
        public void onPageSelected(int pos) {  
            // TODO Auto-generated method stub  
              
            currentItem = pos; 
            for(int i=0;i < dotViewsList.size();i++){  
                if(i == pos){  
                    ((View)dotViewsList.get(pos)).setBackgroundResource(R.drawable.appframe_dot_focus);  
                }else {  
                    ((View)dotViewsList.get(i)).setBackgroundResource(R.drawable.appframe_dot_blur);  
                }  
            }  
        }  
          
    }  
      
    /** 
     *执行轮播图切换任务 
     * 
     */  
    private class SlideShowTask implements Runnable{
  
        @Override  
        public void run() {  
            // TODO Auto-generated method stub  
            synchronized (viewPager) {  
                currentItem = (currentItem+1)%imageViewsList.size();
                @SuppressWarnings("unused")
				Message msg = Message.obtain();
                handler.sendEmptyMessage(1);
//                handler.obtainMessage().sendToTarget();
            }  
        }  
          
    }  
      
    /** 
     * 销毁ImageView资源，回收内存 
     *  
     */  
    @SuppressWarnings("unused")
	private void destoryBitmaps() {  
  
        for (int i = 0; i < IMAGE_COUNT; i++) {  
            ImageView imageView = imageViewsList.get(i);
            Drawable drawable = imageView.getDrawable();
            if (drawable != null) {  
                //解除drawable对view的引用  
                drawable.setCallback(null);  
            }  
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
                  
            	for (FloorBasicInfos info : mList_floorBasicInfos) {
            		imageUrls.add(info.getPicurl().trim());
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
                initUI(context);  
            }  
        }  
    }  
      
    /** 
     * ImageLoader 图片组件初始化 
     *  
     * @param context 
     */  
    public static void initImageLoader(Context context) {  
        // This configuration tuning is custom. You can tune every option, you  
        // may tune some of them,  
        // or you can create default configuration by  
        // ImageLoaderConfiguration.createDefault(this);  
        // method.  
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory().discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).writeDebugLogs() // Remove  
                                                                                                                                                                                                                                                                                                // for  
                                                                                                                                                                                                                                                                                                // release  
                                                                                                                                                                                                                                                                                                // app  
                .build();  
        // Initialize ImageLoader with configuration.  
        ImageLoader.getInstance().init(config);  
    }  

}
