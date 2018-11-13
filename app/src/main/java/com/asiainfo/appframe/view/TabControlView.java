package com.asiainfo.appframe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.asiainfo.appframe.R;
import com.asiainfo.appframe.data.Constants;

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
            mBTN_common.setCheck(Constants.BTN_FLAG_HOME);
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
            index = Constants.BTN_FLAG_HOME;
            mBTN_common.setUnCheck(index);
            mBTN_common.setText("首页");
            mBTN_common.setImage(R.mipmap.ic_home_nomal);
        }
        if (mBTN_discover != null) {
            index = Constants.BTN_FLAG_TASK;
            mBTN_discover.setUnCheck(index);
            mBTN_discover.setText("任务");
            mBTN_discover.setImage(R.mipmap.ic_task_nomal);
        }
        if (mBTN_classify != null) {
            index = Constants.BTN_FLAG_MSG;
            mBTN_classify.setUnCheck(index);
            mBTN_classify.setText("消息");
            mBTN_classify.setImage(R.mipmap.ic_msg_nomal);
        }
        if (mBTN_mine != null) {
            index = Constants.BTN_FLAG_MINE;
            mBTN_mine.setUnCheck(index);
            mBTN_mine.setText("我的");
            mBTN_mine.setImage(R.mipmap.ic_mine_nomal);
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

        int index = -1;

        switch (v.getId()) {
            case R.id.btn_common:
                index = Constants.BTN_FLAG_HOME;
                initTabControl();
                mBTN_common.setCheck(index);
                break;
            case R.id.btn_discover:
                index = Constants.BTN_FLAG_TASK;
//                mBTN_discover.setCheck(index);
                break;
            case R.id.btn_classify:
                initTabControl();
                index = Constants.BTN_FLAG_MSG;
                mBTN_classify.setCheck(index);
                break;
            case R.id.btn_mine:
                initTabControl();
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
