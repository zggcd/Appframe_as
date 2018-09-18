package com.asiainfo.appframe.view.stackoverview.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.asiainfo.appframe.view.stackoverview.misc.OverviewConfiguration;
import com.asiainfo.appframe.view.stackoverview.model.OverviewAdapter;

/**
 * Created by stiven on 2017/5/18 0018.
 */

public class OverView  extends FrameLayout implements OverviewStackView.Callbacks  {

    public interface RecentsViewCallbacks {
        public void onCardDismissed(int position);
        public void onAllCardsDismissed();
    }

    OverviewStackView mStackView;
    OverviewConfiguration mConfig;
    OverviewAdapter mAdapter;
    RecentsViewCallbacks mCallbacks;

    public OverView(Context context) {
        super(context);
        init(context);
    }

    public OverView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public OverView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(21)
    public OverView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context)
    {
        mConfig = new OverviewConfiguration(context);
    }

    /** Sets the callbacks */
    public void setCallbacks(RecentsViewCallbacks cb) {
        mCallbacks = cb;
    }

    /** Set/get the bsp root node */
    public void setTaskStack(OverviewAdapter adapter) {

        if (mStackView != null) {
            removeView(mStackView);
        }

        mAdapter = adapter;
        mStackView = new OverviewStackView(getContext(), adapter, mConfig);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mStackView.setLayoutParams(params);

        mStackView.setCallbacks(this);
//        mStackView.setAlpha(0);
//        mStackView.animate().alpha(1.f).setStartDelay(2000).setDuration(3500).start();

        //所以说 OverviewStackView 才是重点
        addView(mStackView);
    }

    /**
     * This is called with the full size of the window since we are handling our own insets.
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (mStackView != null) {
            Rect stackBounds = new Rect();
            mConfig.getOverviewStackBounds(width, height, stackBounds);
            mStackView.setStackInsetRect(stackBounds);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onCardDismissed(int position) {
        if (mCallbacks != null) {
            mCallbacks.onCardDismissed(position);
        }
    }

    @Override
    public void onAllCardsDismissed() {
        if (mCallbacks != null) {
            mCallbacks.onAllCardsDismissed();
        }
    }
    
    //获取当前点击到的Overviewcard
    public OverviewCard getChildView(int x, int y){
    	
    	return mStackView.getChildView(x, y);
    }
    
    public void setCanDelete(boolean candelete){
    	mStackView.setCanDelete(candelete);
    }

}
