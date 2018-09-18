package com.asiainfo.appframe.activity;

import java.util.List;

import com.asiainfo.appframe.adapter.PoiAdapter;
import com.asiainfo.appframe.adapter.PoiSearchAdapter;
import com.asiainfo.appframe.utils.ResourceUtil;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.LogoPosition;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class LocationChooseActivity extends Activity implements BDLocationListener, OnGetGeoCoderResultListener, BaiduMap.OnMapStatusChangeListener, TextWatcher {
	
	private Context context;
	
	private MapView mMapView;
    private BaiduMap mBaiduMap;
    private ListView poisLL;
    private View loadMoreView;//附近listview加载更多
    private View loadMoreViewCity;//搜索listview加载更多
    
    private PoiAdapter poisLLAdapter = null;
    private PoiSearchAdapter poiSearchAdapter = null;
    
    //附近list
    public int last_index;  
    public int total_index;
    public boolean isLoading = false;
    
    //城市搜索list	
    public int last_index_city;  
    public int total_index_city;
    public boolean isLoading_city = false;
    
    //附近加载更多页数
    private int poi_page_num = 0;
    //搜索加载更多页数
    private int poi_search_page_num = 1;
    
    //附近搜索筛选
    private PoiNearbySearchOption poiNearbySearchOption = null;
    //城市内检索
    private PoiCitySearchOption poiCitySearchOption = null;
    private PoiSearch poiNearBySearch;
    private PoiSearch poiCitySearch;
    
    //当前手机所在poi信息,附近信息搜索key
    private PoiInfo currentPoiInfo = null;
    //搜索关键字
    private String searchStr = "";
    
    /**
     * 定位模式
     */
    private MyLocationConfiguration.LocationMode mCurrentMode;
    /**
     * 定位端
     */
    private LocationClient mLocClient;
    /**
     * 是否是第一次定位
     */
    private boolean isFirstLoc = true;
    /**
     * 定位坐标
     */
    private LatLng locationLatLng;
    /**
     * 定位城市
     */
    private String city;
    /**
     * 反地理编码
     */
    private GeoCoder geoCoder;
   
    /**
     * 界面上方布局
     */
    private RelativeLayout topRL;
    
    /**
     * 选中的地址坐标
     */
    private PoiInfo mChoosePoiInfo;
    
    /**
     * 搜索地址输入框
     */
    private EditText searchAddress;
    private ImageView mIV_search;
    private Button mBTN_location;
    private LinearLayout mLL_back;
    private LinearLayout mLL_sure;
    /**
     * 搜索输入框对应的ListView
     */
    private ListView searchPois;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(ResourceUtil.getLayoutId(this, "appframe_activity_location_baidu"));
        initView();
        initData();
    }

    private void initView() {
        mMapView = (MapView) findViewById(ResourceUtil.getId(context, "main_bdmap"));
        mBaiduMap = mMapView.getMap();

        poisLL = (ListView) findViewById(ResourceUtil.getId(context, "main_pois"));

        topRL = (RelativeLayout) findViewById(ResourceUtil.getId(context, "main_top_RL"));
        mBTN_location = (Button) findViewById(ResourceUtil.getId(context, "btn_location"));
        searchAddress = (EditText) findViewById(ResourceUtil.getId(context, "key_edit"));
        mIV_search = (ImageView) findViewById(ResourceUtil.getId(context, "iv_search"));
        searchPois = (ListView) findViewById(ResourceUtil.getId(context, "main_search_pois"));
        mLL_back = (LinearLayout) findViewById(ResourceUtil.getId(context, "ll_back"));
        mLL_sure = (LinearLayout) findViewById(ResourceUtil.getId(context, "ll_sure"));

        loadMoreView = getLayoutInflater().inflate(ResourceUtil.getLayoutId(context, "appframe_load_more"), null);
        loadMoreViewCity = getLayoutInflater().inflate(ResourceUtil.getLayoutId(context, "appframe_load_more"), null);
        loadMoreView.setVisibility(View.GONE);
        poisLL.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if(last_index == total_index && (scrollState == OnScrollListener.SCROLL_STATE_IDLE))
		        {
		            //表示此时需要显示刷新视图界面进行新数据的加载(要等滑动停止)
		            if(!isLoading)
		            {
		                //不处于加载状态的话对其进行加载  
		                isLoading = true;
		                //设置刷新界面可见 
		                loadMoreView.setVisibility(View.VISIBLE);
		                onLoad();
		            }
		        }
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				last_index = firstVisibleItem + visibleItemCount;  
		        total_index = totalItemCount;
			}
		});
        poisLL.addFooterView(loadMoreView, null, false);
        
        mLL_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        
        mLL_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	if(mChoosePoiInfo == null){
            		Toast.makeText(context, "正在定位", Toast.LENGTH_SHORT).show();
            		return;
            	}
            	Intent intent = new Intent();
            	intent.putExtra("result", mChoosePoiInfo);
            	setResult(RESULT_OK, intent);
                finish();
            }
        });
        
        searchPois.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if(last_index_city == total_index_city && (scrollState == OnScrollListener.SCROLL_STATE_IDLE))
		        {
		            //表示此时需要显示刷新视图界面进行新数据的加载(要等滑动停止)
		            if(!isLoading_city)
		            {
		                //不处于加载状态的话对其进行加载  
		                isLoading_city = true;
		                //设置刷新界面可见 
		                loadMoreViewCity.setVisibility(View.VISIBLE);
		                onLoadCity();
		            }
		        }
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				last_index_city = firstVisibleItem + visibleItemCount;  
		        total_index_city = totalItemCount;
			}
		});

        searchPois.addFooterView(loadMoreViewCity, null, false);
        
        searchPois.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            	
                searchAddress.setText("");
                poi_search_page_num = 1;
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(((PoiInfo)poiSearchAdapter.getItem(i)).location).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                currentPoiInfo = (PoiInfo)poiSearchAdapter.getItem(i);
                mChoosePoiInfo = (PoiInfo)poiSearchAdapter.getItem(i);
//                mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng();
                searchPois.setVisibility(View.GONE);
                
                poi_page_num = 0;
                poisLLAdapter = null;
//                searchNeayBy(currentPoiInfo);
            }
        });

        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder().zoom(18).build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);

        //控制地图logo显示位置，不可阻挡不可移除
        mMapView.setLogoPosition(LogoPosition.logoPostionRightBottom);

        mMapView.showScaleControl(false);
        mMapView.showZoomControls(false);

        //地图状态改变相关监听
        mBaiduMap.setOnMapStatusChangeListener(this);
        //百度地图SDK为您提供了3种类型的地图资源（普通矢量地图、卫星图和空白地图）
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //开启定位图层
        mBaiduMap.setMyLocationEnabled(true);

        //定位图层显示方式
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;

        /**
         * 设置定位图层配置信息，只有先允许定位图层后设置定位图层配置信息才会生效
         * customMarker用户自定义定位图标
         * enableDirection是否允许显示方向信息
         * locationMode定位图层显示方式
         */
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, null));

        //初始化定位
        mLocClient = new LocationClient(this);
        //注册定位监听
        mLocClient.registerLocationListener(this);

        //定位选项
        LocationClientOption option = new LocationClientOption();
        /**
         * coorType - 取值有3个：
         * 返回国测局经纬度坐标系：gcj02
         * 返回百度墨卡托坐标系 ：bd09
         * 返回百度经纬度坐标系 ：bd09ll
         */
        option.setCoorType("bd09ll");
        //设置是否需要地址信息，默认为无地址
        option.setIsNeedAddress(true);
        //设置是否需要返回位置语义化信息，可以在BDLocation.getLocationDescribe()中得到数据，ex:"在天安门附近"， 可以用作地址信息的补充
        option.setIsNeedLocationDescribe(true);
        //设置是否需要返回位置POI信息，可以在BDLocation.getPoiList()中得到数据
        option.setIsNeedLocationPoiList(true);
        /**
         * 设置定位模式
         * Battery_Saving
         * 低功耗模式
         * Device_Sensors
         * 仅设备(Gps)模式
         * Hight_Accuracy
         * 高精度模式
         */
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //设置是否打开gps进行定位
        option.setOpenGps(true);
        //设置扫描间隔，单位是毫秒 当<1000(1s)时，定时定位无效
        option.setScanSpan(1000);
        option.setLocationNotify(true);

        //设置 LocationClientOption
        mLocClient.setLocOption(option);

        //开始定位
        mLocClient.start();
    }
    
    private void initData(){
    	poiNearbySearchOption = new PoiNearbySearchOption();
    	poiCitySearchOption = new PoiCitySearchOption();
    	  
    	poiNearBySearch = PoiSearch.newInstance();
    	//创建PoiSearch实例
        poiCitySearch = PoiSearch.newInstance();
    	// POI初始化搜索模块，注册搜索事件监听  
    	poiNearBySearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
			
    		 @Override
             public void onGetPoiResult(PoiResult poiResult) {
    			 
    			 if(poiResult == null || poiResult.error == PoiResult.ERRORNO.RESULT_NOT_FOUND){
    				 Toast.makeText(LocationChooseActivity.this, "未找到结果", Toast.LENGTH_LONG).show();  
    	                return;
    			 }
    			 
                 List<PoiInfo> poiInfos = poiResult.getAllPoi();
                 if(poiInfos == null){
                     return;
                 }
                 
                 if(poisLLAdapter == null){
                	 poisLLAdapter = new PoiAdapter(LocationChooseActivity.this, poiInfos);
                     poisLL.setAdapter(poisLLAdapter);
                    
                     poisLL.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                         @Override
                         public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                         	
                         	mChoosePoiInfo = (PoiInfo)poisLL.getAdapter().getItem(i);
                         	poisLLAdapter.changeChooseItem(i);
                            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(((PoiInfo)poisLLAdapter.getItem(i)).location));
                         }
                     });
                 }else{
                	 poisLLAdapter.loadMoreView(poiInfos);
                     loadComplete();//刷新结束
                 }
             }

             //poi 详情查询结果回调
             @Override
             public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
             }

             @Override
             public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

             }
		});
    	
    	//设置poi检索监听者
        poiCitySearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            //poi 查询结果回调
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
            	
                List<PoiInfo> poiInfos = poiResult.getAllPoi();
                
                if(poiInfos == null){
                    return;
                }
                poiSearchAdapter = new PoiSearchAdapter(LocationChooseActivity.this, poiInfos, locationLatLng);
                searchPois.setVisibility(View.VISIBLE);
                searchPois.setAdapter(poiSearchAdapter);
            }

            //poi 详情查询结果回调
            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        });
        
    }

    /**
     * 定位监听
     *
     * @param bdLocation
     */
    @Override
    public void onReceiveLocation(final BDLocation bdLocation) {

        //如果bdLocation为空或mapView销毁后不再处理新数据接收的位置
        if (bdLocation == null || mBaiduMap == null) {
            return;
        }

        //定位数据
        MyLocationData data = new MyLocationData.Builder()
                //定位精度bdLocation.getRadius()
                .accuracy(bdLocation.getRadius())
                //此处设置开发者获取到的方向信息，顺时针0-360
                .direction(bdLocation.getDirection())
                //经度
                .latitude(bdLocation.getLatitude())
                //纬度
                .longitude(bdLocation.getLongitude())
                //构建
                .build();

        //设置定位数据
        mBaiduMap.setMyLocationData(data);

        //是否是第一次定位
        if (isFirstLoc) {
            isFirstLoc = false;
            LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(ll, 18);
            mBaiduMap.animateMapStatus(msu);
        }

        mBTN_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(ll, 18);
                mBaiduMap.animateMapStatus(msu);
            }
        });

        //获取坐标，待会用于POI信息点与定位的距离
        locationLatLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
        //获取城市，待会用于POISearch
        city = bdLocation.getCity();

        //文本输入框改变监听，必须在定位完成之后
        searchAddress.removeTextChangedListener(this);
        searchAddress.addTextChangedListener(this);

        //创建GeoCoder实例对象
        geoCoder = GeoCoder.newInstance();
        //发起反地理编码请求(经纬度->地址信息)
        ReverseGeoCodeOption reverseGeoCodeOption = new ReverseGeoCodeOption();
        
        //设置反地理编码位置坐标
        reverseGeoCodeOption.location(new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude()));
        //设置查询结果监听者
        geoCoder.setOnGetGeoCodeResultListener(this);
//        geoCoder.reverseGeoCode(reverseGeoCodeOption);
    }

    //地理编码查询结果回调函数
    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
    	
    }

    //反地理编码查询结果回调函数
    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
    	poi_page_num = 0;
        List<PoiInfo> poiInfos = reverseGeoCodeResult.getPoiList();
        if( null == poiInfos){
        	return;
        }
        
        
        if(poiInfos.size() > 0){
        	currentPoiInfo = poiInfos.get(0);
        	mChoosePoiInfo = poiInfos.get(0);
        }
        
        poisLLAdapter = null;
        poisLLAdapter = new PoiAdapter(LocationChooseActivity.this, poiInfos);
        poisLL.setAdapter(poisLLAdapter);
       
        poisLL.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            	
            	mChoosePoiInfo = (PoiInfo)poisLL.getAdapter().getItem(i);
            	poisLLAdapter.changeChooseItem(i);
            	mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(((PoiInfo)poisLLAdapter.getItem(i)).location));
            }
        });
        
//        poisLLAdapter = null;
//        searchNeayBy(currentPoiInfo);
//        poiNearbySearchOption.keyword(mChoosePoiInfo.address);
//        poiNearbySearchOption.location(new LatLng(currentPoiInfo.location.latitude, currentPoiInfo.location.longitude));
//        poiNearbySearchOption.radius(1000);  // 检索半径，单位是米
//        poiNearbySearchOption.pageCapacity(10);  // 默认每页10条
//        poiNearbySearchOption.pageNum(poi_page_num++);
//        poiNearBySearch.searchNearby(poiNearbySearchOption);  // 发起附近检索请求
    }


    /**
     * 手势操作地图，设置地图状态等操作导致地图状态开始改变
     *
     * @param mapStatus 地图状态改变开始时的地图状态
     */
    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {
    }

    /** 因某种操作导致地图状态开始改变。
     * @param mapStatus 地图状态改变开始时的地图状态
     * @param i ，取值有：
     * 1：用户手势触发导致的地图状态改变,比如双击、拖拽、滑动底图
     * 2：SDK导致的地图状态改变, 比如点击缩放控件、指南针图标
     * 3：开发者调用,导致的地图状态改变
     */

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus, int i) {
    }

    /**
     * 地图状态变化中
     *
     * @param mapStatus 当前地图状态
     */
    @Override
    public void onMapStatusChange(MapStatus mapStatus) {
    }

    /**
     * 地图状态改变结束
     *
     * @param mapStatus 地图状态改变结束后的地图状态
     */
    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {
        //地图操作的中心点
        LatLng cenpt = mapStatus.target;
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(cenpt));
    }

    /**
     * 输入框监听---输入之前
     *
     * @param s     字符序列
     * @param start 开始
     * @param count 总计
     * @param after 之后
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    /**
     * 输入框监听---正在输入
     *
     * @param s      字符序列
     * @param start  开始
     * @param before 之前
     * @param count  总计
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    /**
     * 输入框监听---输入完毕
     *
     * @param s
     */
    @Override
    public void afterTextChanged(Editable s) {
    	poi_search_page_num = 1;
    	searchStr = s.toString();
        if (s.length() == 0 || "".equals(s.toString())) {
            searchPois.setVisibility(View.GONE);
        } else {
        	searchAddressOfCity(s.toString());
            //关键字
//            poiCitySearchOption.keyword(s.toString());
//            //城市
//            poiCitySearchOption.city(city);
//            //设置每页容量，默认为每页10条
//            poiCitySearchOption.pageCapacity(20);
//            //分页编号
//            poiCitySearchOption.pageNum(poi_search_page_num);
//            poiCitySearch.searchInCity(poiCitySearchOption);

        }
    }

    /** 
     * 刷新加载 
     */  
    public void onLoad()
    {
    	searchNeayBy(currentPoiInfo); 
    }
    
    /**
     * 搜索加载
     */
    public void onLoadCity(){
    	searchAddressOfCity(searchStr);
    }
    
    /**
     * 搜索附近位置
     * @param latlng
     */
    private void searchNeayBy(PoiInfo poiInfo){
        poiNearbySearchOption.keyword(poiInfo.address);
        poiNearbySearchOption.location(new LatLng(poiInfo.location.latitude, poiInfo.location.longitude));
        poiNearbySearchOption.radius(3000);  // 检索半径，单位是米
        poiNearbySearchOption.pageCapacity(10);  // 默认每页10条
        poiNearbySearchOption.pageNum(poi_page_num++);
        poiNearBySearch.searchNearby(poiNearbySearchOption);  // 发起附近检索请求
    }
    
    private void searchAddressOfCity(String s){
    	//关键字
        poiCitySearchOption.keyword(s.toString());
        //城市
        poiCitySearchOption.city(city);
        //设置每页容量，默认为每页10条
        poiCitySearchOption.pageCapacity(20);
        //分页编号
        poiCitySearchOption.pageNum(poi_search_page_num++);
        
        poiCitySearch.searchInCity(poiCitySearchOption);
    }
      
    /** 
     * 加载完成 
     */  
    public void loadComplete() 
    {  
        loadMoreView.setVisibility(View.GONE);//设置刷新界面不可见 
        isLoading = false;//设置正在刷新标志位false
        this.invalidateOptionsMenu();  
//        poisLL.removeFooterView(loadMoreView);//如果是最后一页的话，则将其从ListView中移出  
    }  

    //回退键
    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // activity 恢复时同时恢复地图控件
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // activity 暂停时同时暂停地图控件
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //退出时停止定位
        mLocClient.stop();
        //退出时关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);

        // activity 销毁时同时销毁地图控件
        mMapView.onDestroy();

        //释放资源
        if (geoCoder != null) {
            geoCoder.destroy();
        }

        mMapView = null;
    }
}
