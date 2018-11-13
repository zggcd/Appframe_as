package com.asiainfo.appframe.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.asiainfo.appframe.R;
import com.asiainfo.appframe.activity.HomeActivity;
import com.asiainfo.appframe.bean.BaseResult;
import com.asiainfo.appframe.bean.FootbarsBean;
import com.asiainfo.appframe.bean.HomePageDtoListBean;
import com.asiainfo.appframe.bean.HomepageInfo2;
import com.asiainfo.appframe.bean.uibean.AppFrontUIResponse;
import com.asiainfo.appframe.bean.uibean.ChildsBean;
import com.asiainfo.appframe.bean.uibean.ChildsBeanX;
import com.asiainfo.appframe.bean.uibean.ResultBean;
import com.asiainfo.appframe.dialog.QrcodeLoginDialog;
import com.asiainfo.appframe.net.ApiClient;
import com.asiainfo.appframe.net.logic.AppFrontUiRequest;
import com.asiainfo.appframe.permission.AddPermission;
import com.asiainfo.appframe.utils.CommonUtil;
import com.asiainfo.appframe.utils.SDKQrcodeCallback;
import com.asiainfo.appframe.utils.SDKUtil;
import com.asiainfo.appframe.utils.StringUtil;
import com.asiainfo.appframe.utils.WindowSizeUtil;
import com.asiainfo.appframe.view.Floor001View;
import com.asiainfo.appframe.view.FloorCarouselFigure1View;
import com.asiainfo.appframe.view.FloorEntry1View;
import com.asiainfo.appframe.view.FloorEntryBar1View;
import com.asiainfo.appframe.view.FloorEntryBar2View;
import com.asiainfo.appframe.view.FloorEntryMenuView;
import com.asiainfo.appframe.view.FloorMenuBar1View;
import com.asiainfo.appframe.view.FootBarView;
import com.asiainfo.appframe.zxing.CaptureActivity;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends BaseFragment {

    SDKUtil sdkUtil;

    EditText etSearch;
    Toolbar toolbar;
    AppBarLayout appbar;
    LinearLayout ll_content;
    CoordinatorLayout main_content;
    LinearLayout ll_bottom_view;	//底部导航栏
    ImageView iv_scancode, iv_msg;

    PopupWindow popupWindow;

    MyHandler handler = new MyHandler();

    public static final HomeFragment newInstance(Activity activity){
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    protected void onAttachToContext(Context context ) {
        super.onAttachToContext(context);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View commonLayout = inflater.inflate(R.layout.appframe_activity_home1, container, false);
        initView(commonLayout);
        initData();
        return commonLayout;
    }

    /**
     * 初始化view
     * @param view
     */
    private void initView(View view){

        etSearch = view.findViewById(R.id.et_search);
        toolbar = view.findViewById(R.id.toolbar);
        appbar = view.findViewById(R.id.appbar);
        ll_content = view.findViewById(R.id.ll_content);
        main_content = view.findViewById(R.id.main_content);
        ll_bottom_view = view.findViewById(R.id.ll_bottom_view);
        iv_scancode = view.findViewById(R.id.iv_scancode);
        iv_msg = view.findViewById(R.id.iv_msg);

        ll_bottom_view.setVisibility(View.GONE);
//        toolbar.inflateMenu(R.menu.menu_homepage);

        initPopupWindow();
    }

    private void initData(){

        sdkUtil = SDKUtil.getInstance(mContext, null);
        sdkUtil.setSDKQrcodeCallback(new SDKQrcodeCallback() {
            Gson gson = new Gson();

            @Override
            public void onQrcodeScanCallback(String result) {

                gson = new Gson();
                BaseResult baseResult = gson.fromJson(result, BaseResult.class);
                if (baseResult.getCode() == 1){

                    final QrcodeLoginDialog dialog = new QrcodeLoginDialog(mContext);
                    dialog.setNegativeButton(v -> {
                        sdkUtil.qrcodeCancel(SDKUtil.accessToken, qrcode);
                        dialog.dismiss();
                    });
                    dialog.setPositiveButton(v -> {
                        sdkUtil.qrcodeLogin(SDKUtil.accessToken, qrcode);
                        dialog.dismiss();
                    });
                    dialog.show();
                }else{
                    Toast.makeText(mContext, baseResult.getMsg(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onQrcodeLoginCallback(String result) {
                gson = new Gson();
                BaseResult baseResult = gson.fromJson(result, BaseResult.class);
                Toast.makeText(mContext, baseResult.getMsg(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onQrcodeCancelCallback(String result) {
                gson = new Gson();
                BaseResult baseResult = gson.fromJson(result, BaseResult.class);
                Toast.makeText(mContext, baseResult.getMsg(), Toast.LENGTH_SHORT).show();
            }
        });

        CommonUtil.showCommonProgressDialog(mContext, "加载中...");
        //获取所有页面信息
//        ApiClient.getUiInfo(handler, 1, SDKUtil.app_id, null, SDKUtil.appSecret);
        ApiClient.getAppFrontUI(SDKUtil.GetAPPFrontUI, handler, 2, SDKUtil.accessToken);

        iv_scancode.setOnClickListener(click -> {

            AddPermission addPermission = new AddPermission(getActivity());
            addPermission.addPermission(new AddPermission.PermissionsListener() {
                @Override
                public void onPermissionListener(boolean hasPermission, int code) {
                    Intent intent = new Intent(mContext, CaptureActivity.class);
                    startActivityForResult(intent, 100);
                }
            }, AddPermission.CODE_PERMISSIONS_CAMERA);
        });

        iv_msg.setOnClickListener(v ->{
            popupWindow.setWidth(CommonUtil.dip2px(mContext, 100));
            popupWindow.showAsDropDown(iv_msg, 1000, 0);
        });
    }

    String qrcode = "";//扫码返回

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        switch (requestCode){
            case 100:
                Bundle bundle = data.getExtras();
                qrcode = bundle.getString("result");
                if (!StringUtil.isEmpty(qrcode)){
                    sdkUtil.qrcodeScan(SDKUtil.accessToken, qrcode);
                }
                break;
            default:
                break;
        }
    }


    private void initPopupWindow(){
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.appframe_popup_right_top, null);
        LinearLayout ll_gohome = contentView.findViewById(R.id.ll_gohome);
        contentView.setFocusable(true);
        contentView.setFocusableInTouchMode(true);
        popupWindow = new PopupWindow(contentView, RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        contentView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode == KeyEvent.KEYCODE_BACK){
                    popupWindow.dismiss();
                }

                return false;
            }
        });

        ll_gohome.setVisibility(View.GONE);

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

        List<FootbarsBean> list_bar = homepageInfo2.getUiDto().getFootbars();
        if(list_bar != null && list_bar.size() > 0){
            FootBarView view_type_bottom = new FootBarView(mContext);
            view_type_bottom.setFloorBasicInfos(list_bar);
            ll_bottom_view.addView(view_type_bottom);
        }

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
                    floorEntry1View.setFloorEntry1ViewInfo(homePageDtoListBean);
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

    /**
     * 刷新页面新
     */
    public void refreshUI(List<ResultBean> list){
        if(list == null){
            return;
        }

        Collections.sort(list, new Comparator<ResultBean>() {
            @Override
            public int compare(ResultBean o1, ResultBean o2) {
                if (o1.getSort() >= o2.getSort())
                    return 1;
                else
                    return -1;
            }
        });
        List<ChildsBeanX> list_childs = list.get(0).getChilds();
        Collections.sort(list_childs, new Comparator<ChildsBeanX>() {
            @Override
            public int compare(ChildsBeanX o1, ChildsBeanX o2) {
                if (o1.getSort() >= o2.getSort())
                    return 1;
                else
                    return -1;
            }
        });

        for (int i = 0; i < list_childs.size(); i++) {
            ChildsBeanX childsBeanX = list_childs.get(i);
            List<ChildsBean> list_childsBean = childsBeanX.getChilds();
            switch (childsBeanX.getCode()){
                case "FLOOR-CAROUSEL-FIGURE-1": {//轮播图
                    FloorCarouselFigure1View floor001View = new FloorCarouselFigure1View(mContext);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, WindowSizeUtil.getWindowSize(mContext)[0] / 4);//CommonUtil.px2dip(mContext, 1600));
                    floor001View.setFloorBasicInfos(list_childsBean);
                    if(ll_content.getChildCount() != 0){
                        lp.setMargins(0, 20, 0, 0);
                    }
                    floor001View.setBackgroundColor(Color.parseColor("#ffffff"));
                    floor001View.setLayoutParams(lp);

                    ll_content.addView(floor001View);
                    break;
                }
                case "FLOOR-MENU-BAR-1": {//菜单
                    FloorMenuBar1View floorEntryMenuView = new FloorMenuBar1View(mContext);
                    floorEntryMenuView.setFloorEntryMenuViewInfo(list_childsBean);
                    LinearLayout.LayoutParams floorEntryMenuViewPl = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, WindowSizeUtil.getWindowSize(mContext)[1] / 2);//CommonUtil.px2dip(mContext, 1600));
//                    LinearLayout.LayoutParams floorEntryMenuViewPl = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, WindowSizeUtil.getWindowSize(mContext)[0] / 3);//CommonUtil.px2dip(mContext, 1600));
                    floorEntryMenuView.setBackgroundColor(Color.parseColor("#ffffff"));
                    floorEntryMenuView.setLayoutParams(floorEntryMenuViewPl);
                    floorEntryMenuViewPl.gravity = Gravity.CENTER;

                    ll_content.addView(floorEntryMenuView);
                    break;
                }
                case "FLOOR-ENTRY-BAR-1": {//便捷服务入口
                    FloorEntryBar2View floorEntry1View = new FloorEntryBar2View(mContext);
                    floorEntry1View.setFloorEntry1ViewInfo(childsBeanX);
                    LinearLayout.LayoutParams pl = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    floorEntry1View.setBackgroundColor(Color.parseColor("#fff000"));
                    floorEntry1View.setLayoutParams(pl);
                    if(ll_content.getChildCount() != 0){
                        pl.setMargins(0, 20, 0, 0);
                    }
//                    ll_content.addView(floorEntry1View);
                    break;
                }
                case "FLOOR-ENTRY-BAR-2": {//企业服务入口
                    FloorEntryBar1View floorEntryBar1View = new FloorEntryBar1View(mContext);
                    LinearLayout.LayoutParams pl = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    floorEntryBar1View.setLayoutParams(pl);
                    if(ll_content.getChildCount() != 0){
                        pl.setMargins(0, 20, 0, 0);
                    }
                    ll_content.addView(floorEntryBar1View);
                    break;
                }
                default:
                    break;
            }
        }
        FloorEntryBar1View floorEntryBar1View = new FloorEntryBar1View(mContext);
        LinearLayout.LayoutParams pl = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        floorEntryBar1View.setLayoutParams(pl);
        if(ll_content.getChildCount() != 0){
            pl.setMargins(0, 20, 0, 0);
        }
        ll_content.addView(floorEntryBar1View);

//        List<HomePageDtoListBean> list_floorBasicInfos = homepageInfo2.getUiDto().getHomePageDtoList();

    }

    private class MyHandler extends Handler {

        Gson gson = new Gson();

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

                case 2:
                    AppFrontUIResponse response = gson.fromJson((String)msg.obj, AppFrontUIResponse.class);
                    int code = response.getCode();
                    if(code == 1){
                        refreshUI(response.getResult());
                    }
                    break;
                default:
                    break;
            }
        }
    }

}
