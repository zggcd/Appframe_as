package com.asiainfo.appframe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.asiainfo.appframe.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stiven on 2017/10/30 0030.
 */

public class TabControlView extends LinearLayout implements View.OnClickListener {
    public interface TabClickCallback {
        public void onTabClick(int itenId);
    }

    private Context mContext;

    private ButtomTabView mBTN_common, mBTN_discover, mBTN_classify, mBTN_mine;

    private List<ButtomTabView> mList_view = new ArrayList<ButtomTabView>();

    private TabClickCallback mTabClickCallback = null;

    public TabControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mBTN_common = (ButtomTabView) findViewById(R.id.btn_common);
        mBTN_discover = (ButtomTabView) findViewById(R.id.btn_discover);
        mBTN_classify = (ButtomTabView) findViewById(R.id.btn_classify);
        mBTN_mine = (ButtomTabView) findViewById(R.id.btn_mine);
        mList_view.clear();
        mList_view.add(mBTN_common);
        mList_view.add(mBTN_discover);
        mList_view.add(mBTN_classify);
        mList_view.add(mBTN_mine);
    }

    public void setDefaultCheck() {
        if (mBTN_common != null) {
//            mBTN_common.setCheck(Constants.BTN_FLAG_COMMON);
        }
    }

    public void setTabCallback(TabClickCallback tabCallback) {
        mTabClickCallback = tabCallback;
    }

    public void showNewTag(){
        if(mBTN_discover!=null){
            mBTN_discover.showNewTag();
        }
    }

    public void dismissNewTag(){
        if(mBTN_discover!=null){
            mBTN_discover.dismissNewTag();
        }
    }

    public void initTabControl() {
        int index = -1;

        if (mBTN_common != null) {
            index = Constants.BTN_FLAG_COMMON;
            mBTN_common.setUnCheck(index);
            mBTN_common.setText("常用");
//            mBTN_common.setImage(R.mipmap.common_uncheck);
        }
        if (mBTN_discover != null) {
            index = Constants.BTN_FLAG_DISCOVER;
            mBTN_discover.setUnCheck(index);
            mBTN_discover.setText("发现");
//            mBTN_discover.setImage(R.mipmap.discover_uncheck);
        }
        if (mBTN_classify != null) {
            index = Constants.BTN_FLAG_CLASSIFY;
            mBTN_classify.setUnCheck(index);
            mBTN_classify.setText("分类");
//            mBTN_classify.setImage(R.mipmap.classify_uncheck);
        }
        if (mBTN_mine != null) {
            index = Constants.BTN_FLAG_MINE;
            mBTN_mine.setUnCheck(index);
            mBTN_mine.setText("我的");
//            mBTN_mine.setImage(R.mipmap.mine_uncheck);
        }
        setBtnListener();

    }

    private void setBtnListener() {
        int num = this.getChildCount();
        for (int i = 0; i < num; i++) {
            View v = getChildAt(i);
            if (v != null) {
                v.setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View v) {

        initTabControl();

        int index = -1;

        switch (v.getId()) {
            case R.id.btn_common:
                index = Constants.BTN_FLAG_COMMON;
                mBTN_common.setCheck(index);
                break;
            case R.id.btn_discover:
                index = Constants.BTN_FLAG_DISCOVER;
                mBTN_discover.setCheck(index);
                break;
            case R.id.btn_classify:
                index = Constants.BTN_FLAG_CLASSIFY;
                mBTN_classify.setCheck(index);
                break;
            case R.id.btn_mine:
                index = Constants.BTN_FLAG_MINE;
                mBTN_mine.setCheck(index);
                break;
            default:
                break;
        }
        if (mTabClickCallback != null) {
            mTabClickCallback.onTabClick(index);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
//        layoutItems(l, t, r, b);
    }

    /**
     * 最左边和最右边的view由母布局的padding进行控制位置。这里需对第2、3个view的位置重新设置
     *
     * @param l
     * @param t
     * @param r
     * @param b
     */
    private void layoutItems(int l, int t, int r, int b) {
        int n = getChildCount();
        if (n == 0) {
            return;
        }
        int paddingleft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int width = r - l;
        int height = b - t;
        int allViewWidth = 0;
        for (int i = 0; i < n; i++) {
            View v = getChildAt(i);
            allViewWidth += v.getWidth();
        }
        int blankWidth = (width - allViewWidth - paddingleft - paddingRight) / (n - 1);

        LayoutParams params1 = (LayoutParams) mList_view.get(1).getLayoutParams();
        params1.leftMargin = blankWidth;
        mList_view.get(1).setLayoutParams(params1);

        LayoutParams params2 = (LayoutParams) mList_view.get(2).getLayoutParams();
        params2.leftMargin = blankWidth;
        mList_view.get(2).setLayoutParams(params2);

    }
}
