package com.asiainfo.appframe.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.asiainfo.appframe.R;
import com.asiainfo.appframe.bean.FloorBasicInfosBean;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FloorEntry1View extends LinearLayout {
    private Context context;

    //view
    ViewPager viewPager;

    //DATA
    List<FloorBasicInfosBean> mList_FloorBasicInfos = new ArrayList<>();

    public FloorEntry1View(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public void setFloorEntry1ViewInfo(List<FloorBasicInfosBean> list){
        this.mList_FloorBasicInfos = list;
        Collections.sort(mList_FloorBasicInfos, new Comparator<FloorBasicInfosBean>() {

            @Override
            public int compare(FloorBasicInfosBean lhs, FloorBasicInfosBean rhs) {
                if(lhs.getSort() >= rhs.getSort() ){
                    return 1;
                }else{
                    return -1;
                }
            }
        });

        initData();
    }

    public FloorEntry1View(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FloorEntry1View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(){
        LayoutInflater.from(context).inflate(R.layout.appframe_floor_entry1, this, true);
    }

    private void initData(){
//        viewPager = findViewById(R.id.viewPager);
//        viewPager.setVisibility(View.GONE);
//
//        if(mList_FloorBasicInfos.size() > 0){
//            View view = View.inflate(context, R.layout.appframe_floor_entry1, null);
//            addView(view);
//        }
//        invalidate();

    }

}
