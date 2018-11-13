package com.asiainfo.appframe.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.asiainfo.appframe.R;
import com.asiainfo.appframe.data.Constants;

/**
 * Created by stiven on 2017/8/9 0009.
 * 首页底部tab
 */


public class ButtomTabView extends RelativeLayout {
    private Context mContext;

    private TextView mTV_text;
    private ImageView mIV_image;
    private ImageView mIV_new_tag;

    private final static int DEFAULT_IMAGE_WIDTH = 64;
    private final static int DEFAULT_IMAGE_HEIGHT = 64;
    private int CHECKED_COLOR = Color.rgb(1, 109, 255);//蓝色
    private int UNCHECKED_COLOR = Color.rgb(102, 102, 102);//灰色
    private int BG_CHECKED_COLOR = Color.rgb(239, 239, 239);//背景色
    private int BG_UNCHECKED_COLOR = Color.rgb(250, 250, 250);//背景色


    public ButtomTabView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View parentView = inflater.inflate(R.layout.view_bottom_tab, this, true);
        mTV_text = (TextView) parentView.findViewById(R.id.tv_text);
        mIV_image = (ImageView)parentView.findViewById(R.id.iv_iamge);
//        mIV_new_tag = (ImageView) parentView.findViewById(R.id.iv_new_tag);
    }

    public void setImage(int imageId){
        if(mIV_image != null){
            mIV_image.setImageResource(imageId);
            setImageSize(DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);
        }
    }

    public void setText(String s){
        if(mTV_text != null){
            mTV_text.setText(s);
            mTV_text.setTextColor(UNCHECKED_COLOR);
        }
    }

    public void setTextColor(int colorId){
        if(mTV_text != null){
            mTV_text.setTextColor(colorId);
        }
    }

    public void showNewTag(){
//        mIV_new_tag.setVisibility(View.VISIBLE);
    }

    public void dismissNewTag(){
//        mIV_new_tag.setVisibility(View.GONE);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    private void setImageSize(int w, int h){
        if(mIV_image != null){
            ViewGroup.LayoutParams lp = mIV_image.getLayoutParams();
            lp.width = w;
            lp.height = h;
            mIV_image.setLayoutParams(lp);
        }
    }

    public void setCheck(int itemId){

        int checkDrawableId = -1;
        switch (itemId){
            case Constants.BTN_FLAG_HOME:
                checkDrawableId = R.mipmap.ic_home_light;
                break;
            case Constants.BTN_FLAG_TASK:
                checkDrawableId = R.mipmap.ic_task_light;
                break;
            case Constants.BTN_FLAG_MSG:
                checkDrawableId = R.mipmap.ic_msg_light;
                break;
            case Constants.BTN_FLAG_MINE:
                checkDrawableId = R.mipmap.ic_mine_light;
                break;
            default:
                break;
        }

        if(mTV_text != null){
            mTV_text.setTextColor(CHECKED_COLOR);
        }

        if(mIV_image != null){
            mIV_image.setImageResource(checkDrawableId);
        }

        setBackgroundColor(BG_CHECKED_COLOR);
    }

    public void setUnCheck(int itemId){

        int uncheckDrawableId = -1;
        switch (itemId){
            case Constants.BTN_FLAG_HOME:
                uncheckDrawableId = R.mipmap.ic_home_nomal;
                break;
            case Constants.BTN_FLAG_TASK:
                uncheckDrawableId = R.mipmap.ic_task_nomal;
                break;
            case Constants.BTN_FLAG_MSG:
                uncheckDrawableId = R.mipmap.ic_msg_nomal;
                break;
            case Constants.BTN_FLAG_MINE:
                uncheckDrawableId = R.mipmap.ic_mine_nomal;
                break;
            default:
                break;
        }

        if(mTV_text != null){
            mTV_text.setTextColor(UNCHECKED_COLOR);
        }

        if(mIV_image != null){
            mIV_image.setImageResource(uncheckDrawableId);
        }

        setBackgroundColor(BG_UNCHECKED_COLOR);
    }
}
