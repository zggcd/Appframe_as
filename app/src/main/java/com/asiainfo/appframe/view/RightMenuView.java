package com.asiainfo.appframe.view;

import com.asiainfo.appframe.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

public class RightMenuView implements View.OnClickListener {
	
	private Context mContext;
	
	
	private MenuItemClickListener menuItemClickListener;
	
	//View
	private PopupWindow mPopupWindow;
	private RelativeLayout rl_version;
	private RelativeLayout rl_log_off;
	
	public RightMenuView(Context context) {
		this.mContext = context;
	}
	
	@SuppressLint("InflateParams")
	private View initPopupView(){
		View menuView = LayoutInflater.from(mContext).inflate(R.layout.appframe_right_top_menu, null);
		rl_version = (RelativeLayout)menuView.findViewById(R.id.rl_version);
		rl_log_off = (RelativeLayout)menuView.findViewById(R.id.rl_log_off);
		
		return menuView;
		
	}
	
	@SuppressWarnings("deprecation")
	public void initPopupWindow(){
		if(mPopupWindow == null){
			mPopupWindow = new PopupWindow(initPopupView(), LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
			mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
			mPopupWindow.setTouchable(true);
			rl_version.setOnClickListener(this);
			rl_log_off.setOnClickListener(this);
		}
	}
	
	/**
	 * 根据是否有新模板磁贴返回PopupWindow
	 * @return
	 */
	public PopupWindow getPopupWindow(){
		return mPopupWindow;
	}
	
	public void dismissPopupWindow(){
		if(mPopupWindow!=null && mPopupWindow.isShowing()){
			mPopupWindow.dismiss();
		}
	}
	
	public void setMenuItemClickListener(MenuItemClickListener menuItemClickListener){
		this.menuItemClickListener = menuItemClickListener;
	}
	
	@Override
	public void onClick(View view) {
		menuItemClickListener.itemClick(view,null);
	}
	
}
