package com.asiainfo.appframe.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.asiainfo.appframe.R;
import com.asiainfo.appframe.bean.Footbars;
import com.asiainfo.appframe.bean.HomePageDtoList;
import com.asiainfo.appframe.bean.HomePageDtoListBean;
import com.asiainfo.appframe.bean.HomePageInfo;
import com.asiainfo.appframe.bean.HomepageInfo2;
import com.asiainfo.appframe.net.ApiClient;
import com.asiainfo.appframe.utils.AssetsUtils;
import com.asiainfo.appframe.utils.CommonUtil;
import com.asiainfo.appframe.utils.SDKUtil;
import com.asiainfo.appframe.utils.WindowSizeUtil;
import com.asiainfo.appframe.view.Floor001View;
import com.asiainfo.appframe.view.FloorEntry1View;
import com.asiainfo.appframe.view.FloorEntryMenuView;
import com.asiainfo.appframe.view.PostFrameView_type1;
import com.asiainfo.appframe.view.PostFrameView_type2;
import com.asiainfo.appframe.view.PostFrameView_type3;
import com.asiainfo.appframe.view.PostFrameView_type_bottom;
import com.asiainfo.appframe.view.RightMenuView;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.asiainfo.appframe.activity.BaseActivity.*;

public class HomeActivity extends AppCompatActivity {


    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.main_content)
    CoordinatorLayout mainContent;


    LinearLayout ll_content;
    SharedPreferences sp;

    MyHandler handler = new MyHandler();

    private Context mContext;
    private CoordinatorLayout.LayoutParams params;

    //data
    Gson gson = new Gson();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.appframe_activity_home1);
        //初始化绑定ButterKnife
        ButterKnife.bind(this);
        toolbar.inflateMenu(R.menu.menu_homepage);
        initView();
        initData();

    }

    private void initView() {
        View view = findViewById(R.id.content);
        ll_content = view.findViewById(R.id.ll_content);

    }

    private void initData() {
//        String filename1 = "app60b44a0844a9c0c4.json";
//        String filename2 = "app837e40094ee52e6c.json";
//        String json1 = AssetsUtils.getJsonFromAsset(this, filename1);
//        final String json2 = AssetsUtils.getJsonFromAsset(this, filename2);
//        try {
//            json1 = URLDecoder.decode(json1, "utf-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        HomepageInfo2 info = gson.fromJson(json1, HomepageInfo2.class);
        sp = getSharedPreferences("APPFRAME_SDK", Context.MODE_PRIVATE);
        CommonUtil.showCommonProgressDialog(mContext, "加载中...");
        //获取所有页面信息
        ApiClient.getUiInfo(handler, 1, SDKUtil.app_id, null, SDKUtil.appSecret);
    }

    public void jumpTo(Class<?> c, Bundle bundle){
        Intent intent = new Intent(HomeActivity.this, c);
        if(bundle != null){
            intent.putExtras(bundle);
        }
        startActivity(intent);
        finish();
    }

    /**
     * 刷新页面
     */
    public void refreshUI(HomepageInfo2 homepageInfo2){
        if(homepageInfo2 == null){
            return;
        }

        List<HomePageDtoListBean> list_floorBasicInfos = homepageInfo2.getUiDto().getHomePageDtoList();

        /**
         * 排序
         */
        Collections.sort(list_floorBasicInfos, new Comparator<HomePageDtoListBean>() {

            @Override
            public int compare(HomePageDtoListBean lhs, HomePageDtoListBean rhs) {
                if(lhs.getSort() >= rhs.getSort() ){
                    return 1;
                }else{
                    return -1;
                }
            }
        });

        for (HomePageDtoListBean homePageDtoListBean:
                list_floorBasicInfos) {
            switch (homePageDtoListBean.getCssCode()){
                case "FLOOR-001"://轮播图
                    Floor001View floor001View = new Floor001View(mContext);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, WindowSizeUtil.getWindowSize(mContext)[0] / 4);//CommonUtil.px2dip(mContext, 1600));
                    floor001View.setFloorBasicInfos(homePageDtoListBean.getFloorBasicInfos());
                    if(ll_content.getChildCount() != 0){
                        lp.setMargins(0, 20, 0, 0);
                    }
                    floor001View.setBackgroundColor(Color.parseColor("#ffffff"));
                    floor001View.setLayoutParams(lp);

                    ll_content.addView(floor001View);
                    break;
                case "FLOOR-ENTRY-1"://入口
                    FloorEntry1View floorEntry1View = new FloorEntry1View(mContext);
                    floorEntry1View.setFloorEntry1ViewInfo(homePageDtoListBean.getFloorBasicInfos());
                    LinearLayout.LayoutParams pl = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    floorEntry1View.setBackgroundColor(Color.parseColor("#fff000"));
                    floorEntry1View.setLayoutParams(pl);
                    if(ll_content.getChildCount() != 0){
                        pl.setMargins(0, 20, 0, 0);
                    }
                    ll_content.addView(floorEntry1View);
                    break;
                case "FLOOR-MENU-FLOW"://菜单
                    FloorEntryMenuView floorEntryMenuView = new FloorEntryMenuView(mContext);
                    floorEntryMenuView.setFloorEntryMenuViewInfo(homePageDtoListBean.getFloorBasicInfos());
                    LinearLayout.LayoutParams floorEntryMenuViewPl = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, WindowSizeUtil.getWindowSize(mContext)[0] / 4);//CommonUtil.px2dip(mContext, 1600));
                    floorEntryMenuView.setBackgroundColor(Color.parseColor("#ffffff"));
                    floorEntryMenuView.setLayoutParams(floorEntryMenuViewPl);

                    ll_content.addView(floorEntryMenuView);
                    break;

                case "FLOOR-ENTRY-2"://入口二
                    break;
                default:
                    break;
            }
        }
    }

    private class MyHandler extends Handler {
        @SuppressLint("HandlerLeak")
        public MyHandler() {

        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            CommonUtil.closeCommonProgressDialog();
            switch (msg.what) {
                case 1:
                    HomepageInfo2 info2 = (HomepageInfo2)msg.obj;
                    if(info2.getCode() == 1){
                        refreshUI(info2);
                    }else {
                        Toast.makeText(mContext, info2.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                    break;

                default:
                    break;
            }
        }
    }

}
