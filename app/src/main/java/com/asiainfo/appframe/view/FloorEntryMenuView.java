package com.asiainfo.appframe.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.asiainfo.appframe.R;
import com.asiainfo.appframe.activity.HomeActivity;
import com.asiainfo.appframe.activity.WebControlerActivity;
import com.asiainfo.appframe.activity.WelcomeActivity;
import com.asiainfo.appframe.bean.AccessTokenResponse;
import com.asiainfo.appframe.bean.FloorBasicInfosBean;
import com.asiainfo.appframe.net.ApiClient;
import com.asiainfo.appframe.utils.CommonUtil;
import com.asiainfo.appframe.utils.SDKUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FloorEntryMenuView extends LinearLayout {

    private Context context;

    //view
    ViewPager viewPager;

    //DATA
    List<FloorBasicInfosBean> mList_FloorBasicInfos = new ArrayList<>();

    public FloorEntryMenuView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public void setFloorEntryMenuViewInfo(List<FloorBasicInfosBean> list){
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

    public FloorEntryMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FloorEntryMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(){
        LayoutInflater.from(context).inflate(R.layout.appframe_layout_slideshow, this, true);
    }

    private void initData(){
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        if(mList_FloorBasicInfos.size() > 0){
            List<View> list_view = new ArrayList<>();
            int pageSize = mList_FloorBasicInfos.size() / 8;            //viewpager总页数
            int endPageMenuSize = mList_FloorBasicInfos.size() % 8;     //最后一页的菜单数量
            if(endPageMenuSize == 0 && pageSize > 0){
                pageSize--;
            }

            for (int i = 0; i <= pageSize; i++){
                View view = View.inflate(context, R.layout.appframe_layout_menu, null);

                //第一个菜单
                LinearLayout ll_menu_00 = (LinearLayout) view.findViewById(R.id.ll_menu_00);
                if( i * 8 < mList_FloorBasicInfos.size() ){
                    final ImageView iv_00 = (ImageView) ll_menu_00.getChildAt(0);
//                    String imageUrl = mList_FloorBasicInfos.get(i * 8).getPicurl();
                    String imageUrl = mList_FloorBasicInfos.get(i * 8).getPicurl();
                    Picasso.with(context).load(imageUrl).into(iv_00, new Callback() {
                        @Override
                        public void onSuccess() {
                            iv_00.setBackground(null);
                        }

						@Override
						public void onError() {
							// TODO Auto-generated method stub
							
						}
                    });
//                    picasso.load(R.mipmap.task).into(iv_00);
                    TextView tv_00 = (TextView) ll_menu_00.getChildAt(1);
                    tv_00.setText(mList_FloorBasicInfos.get(i * 8).getTitle());
                    int finalI = i;
                    ll_menu_00.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            jumpToWeb("http://zc.testpub.net/files/schedule/index.html");

                            if((System.currentTimeMillis() - SDKUtil.start_time) / 1000 - Long.parseLong(SDKUtil.expires_in) >= 0){
                                ApiClient.refreshAccessToken(handler, 2, 0, SDKUtil.phone_num, SDKUtil.mac, SDKUtil.accessToken, SDKUtil.staff_code);
                                return;
                            }
                            // TODO Auto-generated method stub
                            ApiClient.getClickUrl(handler, 4, mList_FloorBasicInfos.get(finalI * 8).getAbilityalias());
                        }
                    });
                }else{
                    ll_menu_00.setVisibility(View.INVISIBLE);
                }

                //第二个菜单
                LinearLayout ll_menu_01 = (LinearLayout) view.findViewById(R.id.ll_menu_01);
                if( i * 8 + 1 < mList_FloorBasicInfos.size() ){
                    final ImageView iv_00 = (ImageView) ll_menu_01.getChildAt(0);
                    String imageUrl = mList_FloorBasicInfos.get(i * 8 + 1).getPicurl();

                    Picasso.with(context).load(imageUrl).into(iv_00, new Callback() {
                        @Override
                        public void onSuccess() {
                            iv_00.setBackground(null);
                        }

                        @Override
                        public void onError() {

                        }
                    });
                    TextView tv_00 = (TextView) ll_menu_01.getChildAt(1);
                    tv_00.setText(mList_FloorBasicInfos.get(i * 8 + 1).getTitle());
                    int finalI = i;
                    ll_menu_01.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            jumpToWeb("http://zc.testpub.net/files/schedule/index.html");

                            if((System.currentTimeMillis() - SDKUtil.start_time) / 1000 - Long.parseLong(SDKUtil.expires_in) >= 0){
                                ApiClient.refreshAccessToken(handler, 2, 0, SDKUtil.phone_num, SDKUtil.mac, SDKUtil.accessToken, SDKUtil.staff_code);
                                return;
                            }
                            // TODO Auto-generated method stub
                            ApiClient.getClickUrl(handler, 4, mList_FloorBasicInfos.get(finalI * 8 + 1).getAbilityalias());
                        }
                    });
                }else{
                    ll_menu_01.setVisibility(View.INVISIBLE);
                }

                //第三个菜单
                LinearLayout ll_menu_02 = (LinearLayout) view.findViewById(R.id.ll_menu_02);
                if( i * 8 + 2 < mList_FloorBasicInfos.size() ){
                    final ImageView iv_00 = (ImageView) ll_menu_02.getChildAt(0);
                    String imgUrl = mList_FloorBasicInfos.get(i * 8 + 2).getPicurl();
                    Picasso.with(context).load(imgUrl).into(iv_00, new Callback() {
                        @Override
                        public void onSuccess() {
                            iv_00.setBackground(null);
                        }

                        @Override
                        public void onError() {

                        }
                    });
                    TextView tv_00 = (TextView) ll_menu_02.getChildAt(1);
                    tv_00.setText(mList_FloorBasicInfos.get(i * 8 + 2).getTitle());
                    int finalI = i;
                    ll_menu_02.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            jumpToWeb("http://zc.testpub.net/files/schedule/index.html");

                            if((System.currentTimeMillis() - SDKUtil.start_time) / 1000 - Long.parseLong(SDKUtil.expires_in) >= 0){
                                ApiClient.refreshAccessToken(handler, 2, 0, SDKUtil.phone_num, SDKUtil.mac, SDKUtil.accessToken, SDKUtil.staff_code);
                                return;
                            }
                            // TODO Auto-generated method stub
                            ApiClient.getClickUrl(handler, 4, mList_FloorBasicInfos.get(finalI * 8 + 2).getAbilityalias());
                        }
                    });
                }else{
                    ll_menu_02.setVisibility(View.INVISIBLE);
                }

                //第四个菜单
                LinearLayout ll_menu_03 = (LinearLayout) view.findViewById(R.id.ll_menu_03);
                if( i * 8 + 3 < mList_FloorBasicInfos.size() ){
                    final ImageView iv_00 = (ImageView) ll_menu_03.getChildAt(0);
                    Picasso.with(context).load(mList_FloorBasicInfos.get(i * 8 + 3).getPicurl()).into(iv_00, new Callback() {
                        @Override
                        public void onSuccess() {
                            iv_00.setBackground(null);
                        }

                        @Override
                        public void onError() {

                        }
                    });
                    TextView tv_00 = (TextView) ll_menu_03.getChildAt(1);
                    tv_00.setText(mList_FloorBasicInfos.get(i * 8 + 3).getTitle());
                    int finalI = i;
                    ll_menu_03.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            jumpToWeb("http://zc.testpub.net/files/schedule/index.html");

                            if((System.currentTimeMillis() - SDKUtil.start_time) / 1000 - Long.parseLong(SDKUtil.expires_in) >= 0){
                                ApiClient.refreshAccessToken(handler, 2, 0, SDKUtil.phone_num, SDKUtil.mac, SDKUtil.accessToken, SDKUtil.staff_code);
                                return;
                            }
                            // TODO Auto-generated method stub
                            ApiClient.getClickUrl(handler, 4, mList_FloorBasicInfos.get(finalI * 8 + 3).getAbilityalias());
                        }
                    });
                }else{
                    ll_menu_03.setVisibility(View.INVISIBLE);
                }

                //第五个菜单
                LinearLayout ll_menu_10 = (LinearLayout) view.findViewById(R.id.ll_menu_10);
                if( i * 8 + 4 < mList_FloorBasicInfos.size() ){
                    final ImageView iv_00 = (ImageView) ll_menu_10.getChildAt(0);
                    Picasso.with(context).load(mList_FloorBasicInfos.get(i * 8 + 4).getPicurl()).into(iv_00, new Callback() {
                        @Override
                        public void onSuccess() {
                            iv_00.setBackground(null);
                        }

                        @Override
                        public void onError() {

                        }
                    });
                    TextView tv_00 = (TextView) ll_menu_10.getChildAt(1);
                    tv_00.setText(mList_FloorBasicInfos.get(i * 8 + 4).getTitle());
                    int finalI = i;
                    ll_menu_10.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            jumpToWeb("http://zc.testpub.net/files/schedule/index.html");

                            if((System.currentTimeMillis() - SDKUtil.start_time) / 1000 - Long.parseLong(SDKUtil.expires_in) >= 0){
                                ApiClient.refreshAccessToken(handler, 2, 0, SDKUtil.phone_num, SDKUtil.mac, SDKUtil.accessToken, SDKUtil.staff_code);
                                return;
                            }
                            // TODO Auto-generated method stub
                            ApiClient.getClickUrl(handler, 4, mList_FloorBasicInfos.get(finalI * 8 + 4).getAbilityalias());
                        }
                    });
                }else{
                    ll_menu_10.setVisibility(View.INVISIBLE);
                }

                //第六个菜单
                LinearLayout ll_menu_11 = (LinearLayout) view.findViewById(R.id.ll_menu_11);
                if( i * 8 + 5 < mList_FloorBasicInfos.size() ){
                    final ImageView iv_00 = (ImageView) ll_menu_11.getChildAt(0);
                    String imgUrl = mList_FloorBasicInfos.get(i * 8 + 5).getPicurl();
//                    picasso.load(R.mipmap.task).into(iv_00);
                    Picasso.with(context).load(imgUrl).into(iv_00, new Callback() {
                        @Override
                        public void onSuccess() {
                            iv_00.setBackground(null);
                        }

                        @Override
                        public void onError() {

                        }
                    });
                    TextView tv_00 = (TextView) ll_menu_11.getChildAt(1);
                    tv_00.setText(mList_FloorBasicInfos.get(i * 8 + 5).getTitle());
                    int finalI = i;
                    ll_menu_11.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            jumpToWeb("http://zc.testpub.net/files/schedule/index.html");

                            if((System.currentTimeMillis() - SDKUtil.start_time) / 1000 - Long.parseLong(SDKUtil.expires_in) >= 0){
                                ApiClient.refreshAccessToken(handler, 2, 0, SDKUtil.phone_num, SDKUtil.mac, SDKUtil.accessToken, SDKUtil.staff_code);
                                return;
                            }
                            // TODO Auto-generated method stub
                            ApiClient.getClickUrl(handler, 4, mList_FloorBasicInfos.get(finalI * 8 + 5).getAbilityalias());
                        }
                    });
                }else{
                    ll_menu_11.setVisibility(View.INVISIBLE);
                }

                //第七个菜单
                LinearLayout ll_menu_12 = (LinearLayout) view.findViewById(R.id.ll_menu_12);
                if( i * 8 + 6 < mList_FloorBasicInfos.size() ){
                    final ImageView iv_00 = (ImageView) ll_menu_12.getChildAt(0);
                    String imgUrl = mList_FloorBasicInfos.get(i * 8 + 6).getPicurl();
                    Picasso.with(context).load(imgUrl).into(iv_00, new Callback() {
                        @Override
                        public void onSuccess() {
                            iv_00.setBackground(null);
                        }

                        @Override
                        public void onError() {

                        }
                    });
                    TextView tv_00 = (TextView) ll_menu_12.getChildAt(1);
                    tv_00.setText(mList_FloorBasicInfos.get(i * 8 + 6).getTitle());
                    int finalI = i;
                    ll_menu_12.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            jumpToWeb("http://zc.testpub.net/files/schedule/index.html");

                            if((System.currentTimeMillis() - SDKUtil.start_time) / 1000 - Long.parseLong(SDKUtil.expires_in) >= 0){
                                ApiClient.refreshAccessToken(handler, 2, 0, SDKUtil.phone_num, SDKUtil.mac, SDKUtil.accessToken, SDKUtil.staff_code);
                                return;
                            }
                            // TODO Auto-generated method stub
                            ApiClient.getClickUrl(handler, 4, mList_FloorBasicInfos.get(finalI * 8 + 6).getAbilityalias());
                        }
                    });
                }else{
                    ll_menu_12.setVisibility(View.INVISIBLE);
                }

                //第八个菜单
                LinearLayout ll_menu_13 = (LinearLayout) view.findViewById(R.id.ll_menu_13);
                if( i * 8 + 7 < mList_FloorBasicInfos.size() ){
                    final ImageView iv_00 = (ImageView) ll_menu_13.getChildAt(0);
                    String imgUrl = mList_FloorBasicInfos.get(i * 8 + 7).getPicurl();
                    Picasso.with(context).load(imgUrl).into(iv_00, new Callback() {
                        @Override
                        public void onSuccess() {
                            iv_00.setBackground(null);
                        }

                        @Override
                        public void onError() {

                        }
                    });
                    TextView tv_00 = (TextView) ll_menu_13.getChildAt(1);
                    tv_00.setText(mList_FloorBasicInfos.get(i * 8 + 7).getTitle());
                    int finalI = i;
                    ll_menu_13.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            jumpToWeb("http://zc.testpub.net/files/schedule/index.html");

                            if((System.currentTimeMillis() - SDKUtil.start_time) / 1000 - Long.parseLong(SDKUtil.expires_in) >= 0){
                                ApiClient.refreshAccessToken(handler, 2, 0, SDKUtil.phone_num, SDKUtil.mac, SDKUtil.accessToken, SDKUtil.staff_code);
                                return;
                            }
                            // TODO Auto-generated method stub
                            ApiClient.getClickUrl(handler, 4, mList_FloorBasicInfos.get(finalI * 8 + 7).getAbilityalias());
                        }
                    });
                }else{
                    ll_menu_13.setVisibility(View.INVISIBLE);
                }

                list_view.add(view);

            }
            viewPager.setAdapter(new MenuViewpagerAdapter(context,list_view));
        }
        invalidate();

    }

    class MenuViewpagerAdapter extends PagerAdapter{

        List<View> aList_view = new ArrayList<>();

        public MenuViewpagerAdapter(Context context, List<View> list){
            aList_view = list;
        }

        /**
         * Return the number of views available.
         */
        @Override
        public int getCount() {
            return aList_view.size();
        }

        //view是否由对象产生，官方写arg0==arg1即可
        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == (View) object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(aList_view.get(position));
        }

        //对应页卡添加上数据
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
//            return super.instantiateItem(container, position);

            container.addView(aList_view.get(position));
            return aList_view.get(position);

        }
    }

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
                    break;
                case 2:
                    AccessTokenResponse response = (AccessTokenResponse)msg.obj;

                    if(response.getCode() == 1000){
                        Intent intent = new Intent(context, WelcomeActivity.class);
                        context.startActivity(intent);
                        ((HomeActivity)context).finish();
                        break;
                    }

                    SDKUtil.accessToken = response.getAccess_token();
                    SDKUtil.expires_in = response.getExpires_in();
                    SDKUtil.start_time = System.currentTimeMillis();
                    ApiClient.getClickUrl(handler, 4, mList_FloorBasicInfos.get(msg.arg1).getAbilityalias());
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

    private void jumpToWeb(String webUrl){
//    	Intent intent = new Intent(context, WebViewActivity.class);
        Intent intent = new Intent(context, WebControlerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("webUrl", webUrl);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

}
