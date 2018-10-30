package com.asiainfo.appframe.dialog;

import java.util.ArrayList;
import java.util.List;

import com.asiainfo.appframe.R;
import com.asiainfo.appframe.bean.AreaCodes;
import com.asiainfo.appframe.utils.CommonUtil;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * 底部地区选择弹出框
 * @author Stiffen
 *
 */
public class BottomAnimDialog extends Dialog {

	private final Context mContext;
	
	//data
	private List<AreaCodes> mList_AuthCodes = new ArrayList<AreaCodes>();
	private AreaCodes mCurrent_authcodes = null;
	
	//View
	private TextView mTV_cancel;
	
	public BottomAnimDialog(Context context, List<AreaCodes> list) {  
		  
        super(context, R.style.dialog);  
        this.mContext = context;  
        this.mList_AuthCodes = list;
        initView();  
  
    }
	
	@SuppressLint("InflateParams")
	private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
        View view = inflater.inflate(R.layout.appframe_bottom_anim_dialog_layout, null);  
        
        Window window = this.getWindow();  
        if (window != null) { //设置dialog的布局样式 让其位于底部   
            window.setGravity(Gravity.BOTTOM);


            WindowManager.LayoutParams lp = window.getAttributes();
            int width = lp.width;
            lp.width = (int)CommonUtil.getWindowDensity(mContext)[1];
            lp.y = CommonUtil.dip2px(mContext,10); //设置居于底部的距离 
            window.setAttributes(lp);
        } 
        
        mTV_cancel = view.findViewById(R.id.tv_cancel);
        mTV_cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
        
        LinearLayout ll_dialog_content = (LinearLayout) view.findViewById(R.id.ll_dialog_content);
        for (int i = 0; i < mList_AuthCodes.size(); i++) {
        	AreaCodes authCode = mList_AuthCodes.get(i);
        	
        	TextView tv = new TextView(mContext);
			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, CommonUtil.dip2px(mContext, 50));
//			lp.gravity = Gravity.CENTER;
			tv.setGravity(Gravity.CENTER);
			tv.setTextColor(Color.BLACK);
			tv.setTextSize(CommonUtil.sp2px(mContext, 8));
			tv.setLayoutParams(lp);
			tv.setText(authCode.getAreaName());
			ll_dialog_content.addView(tv);
			
//			if(i < mList_AuthCodes.size() - 1){
				View v_line = new View(mContext);
				LayoutParams v_lp = new LayoutParams(LayoutParams.MATCH_PARENT, CommonUtil.dip2px(mContext, 1));
				v_line.setBackgroundColor(Color.TRANSPARENT);
				v_line.setLayoutParams(v_lp);
				ll_dialog_content.addView(v_line);
//			}
			
			final int j = i;
			tv.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mCurrent_authcodes = mList_AuthCodes.get(j);
					dismiss();
				}
			});
			
        }
  
        setContentView(view);  
  
//        setData();  
    }
	
	//返回点击的areacode
	public AreaCodes getAreaCodes(){
		return mCurrent_authcodes;
	}
	
}
