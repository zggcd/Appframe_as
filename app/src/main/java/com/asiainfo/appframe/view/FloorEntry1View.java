package com.asiainfo.appframe.view;

import android.annotation.SuppressLint;
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
import com.asiainfo.appframe.bean.HomePageDtoListBean;
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

    HomePageDtoListBean mParentInfo;

    public FloorEntry1View(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public void setFloorEntry1ViewInfo(HomePageDtoListBean info){
        this.mParentInfo = info;
        this.mList_FloorBasicInfos = info.getFloorBasicInfos();
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

    @SuppressLint("SetTextI18n")
    private void initData(){

        if(mList_FloorBasicInfos.size() > 0){

            TextView tv_entry1_name = (TextView) findViewById(R.id.tv_entry1_name);

            TextView tv_entry1_menu_name1 = (TextView) findViewById(R.id.tv_entry1_menu_name1);
            TextView tv_entry1_menu_describe1 = (TextView) findViewById(R.id.tv_entry1_menu_describe1);
            final ImageView iv_entey1_1 = (ImageView) findViewById(R.id.iv_entey1_1);

            TextView tv_entry1_menu_name2 = (TextView) findViewById(R.id.tv_entry1_menu_name2);
            TextView tv_entry1_menu_describe2 = (TextView) findViewById(R.id.tv_entry1_menu_describe2);
            final ImageView iv_entey1_2 = (ImageView) findViewById(R.id.iv_entey1_2);

            String entry1_menu_name = mList_FloorBasicInfos.get(0).getTitle();
            String entry1_menu_describe1 = mList_FloorBasicInfos.get(0).getSubhead();
            tv_entry1_name.setText(mParentInfo.getTitle());
            tv_entry1_menu_name1.setText(entry1_menu_name);
            tv_entry1_menu_describe1.setText(entry1_menu_describe1);
            String iconUrl1 = mList_FloorBasicInfos.get(0).getPicurl();
            Picasso.with(context).load(iconUrl1).into(iv_entey1_1, new Callback() {
                @Override
                public void onSuccess() {
                    iv_entey1_1.setBackground(null);
                }

                @Override
                public void onError() {

                }
            });

            String entry1_menu_name2 = mList_FloorBasicInfos.get(1).getTitle();
            String entry1_menu_describe2 = mList_FloorBasicInfos.get(1).getSubhead();
            tv_entry1_menu_name2.setText(entry1_menu_name2);
            tv_entry1_menu_describe2.setText(entry1_menu_describe2);
            String iconUrl2 = mList_FloorBasicInfos.get(1).getPicurl();
            Picasso.with(context).load(iconUrl2).into(iv_entey1_2, new Callback() {
                @Override
                public void onSuccess() {
                    iv_entey1_2.setBackground(null);
                }

                @Override
                public void onError() {

                }
            });

        }
        invalidate();

    }

}
