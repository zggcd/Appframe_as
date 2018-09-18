package com.asiainfo.appframe.activity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.asiainfo.appframe.CommonApplication;
import com.asiainfo.appframe.R;
import com.asiainfo.appframe.bean.Footbars;
import com.asiainfo.appframe.bean.HomePageDtoList;
import com.asiainfo.appframe.bean.HomePageInfo;
import com.asiainfo.appframe.bean.SDKJSType;
import com.asiainfo.appframe.net.ApiClient;
import com.asiainfo.appframe.service.CirclePositionService;
import com.asiainfo.appframe.utils.CommonUtil;
import com.asiainfo.appframe.utils.SDKUtil;
import com.asiainfo.appframe.view.MenuItemClickListener;
import com.asiainfo.appframe.view.PostFrameView_type1;
import com.asiainfo.appframe.view.PostFrameView_type2;
import com.asiainfo.appframe.view.PostFrameView_type3;
import com.asiainfo.appframe.view.PostFrameView_type_bottom;
import com.asiainfo.appframe.view.RightMenuView;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends BaseActivity implements MenuItemClickListener {

//	private static String TAG = "MainActivity";
	
	private Context mContext;
	
	//View
	private View mContentView;
	/**右上角弹出的popupWindow**/
	private RightMenuView rightMenuView;
	private TextView mTV_title;				//标题
	private RelativeLayout rl_top_title;	//顶部
	private LinearLayout ll_content;		//中部内容
	private LinearLayout ll_bottom_view;	//底部导航栏
	
	//data
	private int[] sceenWH;
	private final String UI_STYPE_1 = "FLOOR-001";			//样式一
	private final String UI_STYPE_2 = "FLOOR-002";			//样式二
	private final String UI_STYPE_3 = "FLOOR-003";			//样式三
	private final String UI_STYPE_4 = "FLOOR-004";			//样式四
	
	SharedPreferences sp;
	
	MyHandler handler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mContext = this;
		handler = new MyHandler();
		mContentView = View.inflate(mContext, R.layout.appframe_activity_main, null);
		setContentView(mContentView);
		super.onCreate(savedInstanceState);
//		
//		SDKUtil sdkUtil = SDKUtil.getInstance(mContext, null);
//		SDKJSType type = new SDKJSType();
//		type.setSignature("eyJhbGciOiJIUzI1NiJ9.eyJzb3VyY2UiOiJhcHBmN2FlMGQ5MzUwYzRhNTc0Iiwic291cmNlX3R5cGUiOjMsImFiaWxpdHkiOiJvVGVzdDEiLCJjcmVhdGV0aW1lIjoxNDk1MDA3ODkzfQ.FeA7EciifD4PkQrxjFPNW0oh2SB7IZ1ercoBciIUmVs");
//		sdkUtil.jumpToWebWindow("oTest1", null, type);
		
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		rl_top_title = (RelativeLayout) findViewById(R.id.rl_top_title);
		mTV_title = (TextView) findViewById(R.id.tv_title);
		ll_content = (LinearLayout) findViewById(R.id.ll_content);
		ll_bottom_view = (LinearLayout) findViewById(R.id.ll_bottom_view);
	}

	private ServiceConnection conn = new ServiceConnection() {
        
        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
            
        }
        
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub
//            MyBinder binder = (MyBinder)service;
//            BindService bindService = binder.getService();
//            bindService.MyMethod();
//            flag = true;
        }
    };
	
	@Override
	public void initData() {
		
		/*Intent intent = new Intent(this, CirclePositionService.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startService(intent);
		bindService(intent, conn, Context.BIND_AUTO_CREATE);*/
		
		// TODO Auto-generated method stub
		sp = getSharedPreferences("APPFRAME_SDK", Context.MODE_PRIVATE);
		CommonUtil.showCommonProgressDialog(mContext, "加载中...");
		//获取所有页面信息
		ApiClient.getUiInfo(handler, 1, SDKUtil.app_id, null, SDKUtil.appSecret);
		rightMenuView = new RightMenuView(mContext);
		sceenWH = CommonUtil.getWindowSize(mContext);
	}
	
	/**
	 * 这是标题栏标题
	 * @param title
	 */
	public void setUiTitle(String title){
		if(title != null){
			mTV_title.setText(title);
		}
	}
	
	/**
	 * 
	 */
	public String getUiTitle(){
		return mTV_title.getText().toString();
	}
	
	/**
	 * 呼出右上角菜单
	 * 
	 * @param view
	 */
	public void showRightPopMenu(View view) {
		if (rightMenuView.getPopupWindow() == null || !rightMenuView.getPopupWindow().isShowing()) {
			rightMenuView.initPopupWindow();
			rightMenuView.setMenuItemClickListener(this);
			rightMenuView.getPopupWindow().showAsDropDown(rl_top_title,
					sceenWH[1] - CommonUtil.dip2px(mContext, 183),
					- CommonUtil.dip2px(mContext, 8));
		} else {
			rightMenuView.dismissPopupWindow();
		}
	}

	@Override
	public void itemClick(View view, Object object) {
		
		if(view.getId() == R.id.rl_version){
			rightMenuView.dismissPopupWindow();
		} else if(view.getId() == R.id.rl_log_off){
			rightMenuView.dismissPopupWindow();
			
			Editor editor = sp.edit();
			editor.putBoolean("login_statue", false);
			editor.commit();
			
			jumpTo(WelcomeActivity.class, null);
		}
		
//		switch (view.getId()) {
//		case R.id.rl_version:
//			rightMenuView.dismissPopupWindow();
//			break;
//		case R.id.rl_log_off:
//			rightMenuView.dismissPopupWindow();
//				
//			Editor editor = sp.edit();
//			editor.putBoolean("login_statue", false);
//			editor.commit();
//			
//			Intent intent = new Intent(mContext, WelcomeActivity.class);
//			startActivity(intent);
//			finish();
//				
//			break;
//		}
	}
	
	public void jumpTo(Class<?> c, Bundle bundle){
		Intent intent = new Intent(MainActivity.this, c);
		if(bundle != null){
			intent.putExtras(bundle);
		}
		startActivity(intent);
		finish();
	}
	
	public void clearUiWithoutFootbars(){
		ll_content.removeAllViews();
	}
	
	/**
	 * 刷新页面
	 */
	public void refreshUI(HomePageInfo homePageInfo){
		if(homePageInfo == null){
			return;
		}
		List<HomePageDtoList> list_ui = homePageInfo.getUiDto().getHomePageDtoList();
		
		if(list_ui == null){
			return;
		}
		List<Footbars> list_bar = homePageInfo.getUiDto().getFootbars();
		
		Collections.sort(list_ui, new Comparator<HomePageDtoList>() {

			@Override
			public int compare(HomePageDtoList lhs, HomePageDtoList rhs) {
				if(lhs.getSort() >= rhs.getSort() ){
					return 1;
				}else{
					return -1;
				}
			}
		});
		if(list_bar != null && list_bar.size() > 0){
			PostFrameView_type_bottom view_type_bottom = new PostFrameView_type_bottom(mContext);
			view_type_bottom.setFloorBasicInfos(list_bar);
			ll_bottom_view.addView(view_type_bottom);
		}
		
		for (HomePageDtoList homePageDtoList : list_ui) {
			if(homePageDtoList.getFloorBasicInfos() != null){
			
				switch (homePageDtoList.getCssCode()) {
				case UI_STYPE_1:
					PostFrameView_type1 view_type1 = new PostFrameView_type1(mContext);
					view_type1.setFloorBasicInfos(homePageDtoList.getFloorBasicInfos());
					LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, screenWH[0] / 4);//CommonUtil.px2dip(mContext, 1600));
					
					if(ll_content.getChildCount() != 0){
						lp.setMargins(0, 20, 0, 0);
					}
					view_type1.setLayoutParams(lp);
					ll_content.addView(view_type1);
					break;
					
				case UI_STYPE_2:
					PostFrameView_type2 view_type2 = new PostFrameView_type2(mContext);
					view_type2.setFloorBasicInfos(homePageDtoList.getFloorBasicInfos());
					RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
					view_type2.setLayoutParams(lp2);
					ll_content.addView(view_type2);
					break;
					
				case UI_STYPE_3:
					PostFrameView_type3 view_type3 = new PostFrameView_type3(mContext);
					view_type3.setFloorBasicInfos(homePageDtoList.getFloorBasicInfos());
					RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
					view_type3.setLayoutParams(lp3);
					ll_content.addView(view_type3);
					break;
					
				case UI_STYPE_4:
					
					break;
	
				default:
					break;
				}
			}
		}
		
		View space_view = new View(mContext);
    	LayoutParams space_params = new LayoutParams(20,LayoutParams.MATCH_PARENT);
    	ll_content.addView(space_view, space_params);
	}

	@SuppressLint("HandlerLeak")
	private class MyHandler extends BaseHandler{
		@SuppressLint("HandlerLeak")
		public MyHandler() {
			
		}
		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			CommonUtil.closeCommonProgressDialog();
			switch (msg.what) {
			case 1:
				HomePageInfo homePageInfo = (HomePageInfo)msg.obj;
				if(homePageInfo.getCode() == 1){
					refreshUI(homePageInfo);
				}else {
					Toast.makeText(mContext, homePageInfo.getMsg(), Toast.LENGTH_SHORT).show();
				}
				
				break;

			default:
				break;
			}
		}
	}
}
