package com.asiainfo.appframe.activity;

import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.asiainfo.appframe.R;
import com.asiainfo.appframe.net.ApiClient;
import com.asiainfo.appframe.receiver.AuthInfoReceicer;
import com.asiainfo.appframe.utils.ResourceUtil;
import com.asiainfo.appframe.utils.SDKUtil;
import com.asiainfo.appframe.utils.StringUtil;
import com.asiainfo.appframe.view.stackoverview.misc.Utilities;
import com.asiainfo.appframe.view.stackoverview.model.OverviewAdapter;
import com.asiainfo.appframe.view.stackoverview.model.ViewHolder;
import com.asiainfo.appframe.view.stackoverview.views.OverView;
import com.asiainfo.appframe.view.stackoverview.views.OverviewCard;
import com.asiainfo.appframe.webutil.BasePlugin;
import com.asiainfo.appframe.webutil.IPlugin;
import com.asiainfo.appframe.webutil.ItemOverViewControler;
import com.asiainfo.appframe.webutil.ItemOverViewControler.WebPageLoadCallBack;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * web页及多窗口控制
 * @author Stiven
 *
 */
public class WebControlerActivity extends BaseActivity implements OverView.RecentsViewCallbacks, IPlugin, WebPageLoadCallBack {
	
	//Top level views
	private OverView mRecentsView;
	private RelativeLayout mRL_webcontent, mRL_top;
	private LinearLayout mLL_edit_windows, mLL_back, mLL_finish;
	private TextView mTV_title;
	
	//data
    private List<String> mList_webinfo = new ArrayList<String>();
    private List<ViewHolder> mList_viewholder = new ArrayList<ViewHolder>();
    private List<ItemOverViewControler> mList_item_overview = new ArrayList<ItemOverViewControler>();
    //多窗口之间传递的数据
    public Map<String, String> mMap_content = new HashMap<String, String>();
    
    OverviewAdapter<ViewHolder<View, ItemOverViewControler>, ItemOverViewControler> stack = null;
    
    private String webUrl = "";
    
    AuthInfoReceicer authInfoReceicer;
    IntentFilter authInfoIntentFilter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	// 启动硬加速
        if (getPhoneAndroidSDK() >= 11) {
            getWindow().setFlags(0x1000000, 0x1000000);
        }
    	super.onCreate(savedInstanceState);
//    	authInfoReceicer = new AuthInfoReceicer();
//    	authInfoIntentFilter = new IntentFilter();
//    	authInfoIntentFilter.addAction("com.asiainfo.appframe.955");
    }
    
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setContentView(ResourceUtil.getLayoutId(mContext, "appframe_activity_webcontroler"));
		mRL_webcontent = (RelativeLayout) findViewById(ResourceUtil.getId(mContext, "rl_webcontent"));
		mRL_top = (RelativeLayout) findViewById(ResourceUtil.getId(mContext, "rl_top"));
		mLL_edit_windows = (LinearLayout) findViewById(ResourceUtil.getId(mContext, "ll_edit_windows"));
		mLL_back = (LinearLayout) findViewById(ResourceUtil.getId(mContext, "ll_back"));
		mLL_finish = (LinearLayout) findViewById(ResourceUtil.getId(mContext, "ll_finish"));
		mTV_title = (TextView) findViewById(ResourceUtil.getId(mContext, "tv_title"));
		
		mRecentsView = (OverView) findViewById(ResourceUtil.getId(mContext, "recents_view"));
		mRecentsView.setCallbacks(this);
		
		// Register the broadcast receiver to handle messages when the screen is turned off
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(SearchManager.INTENT_GLOBAL_SEARCH_ACTIVITY_CHANGED);
	
        // Private API calls to make the shadows look better
        try {
            Utilities.setShadowProperty("ambientRatio", String.valueOf(1.5f));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
	}
	
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		webUrl = bundle.getString("webUrl", "");
		
		mList_webinfo.add(webUrl);
//		mList_webinfo.add("http://zc.testpub.net/files/nativeJsBridgeDemo/demo.html");
//		mList_webinfo.add("http://132.228.187.252:9512/af-portal/sandbox/redirectUrl?phone=" + SDKUtil.phone_num);
		
		if(StringUtil.isEmpty(webUrl)){
			return;
		}

        final ItemOverViewControler itemOverViewControler = new ItemOverViewControler(mContext);
//        itemOverViewControler.loadUrl("http://zc.testpub.net/files/nativeJsBridgeDemo/demo.html");
        itemOverViewControler.loadUrl(mList_webinfo.get(mList_webinfo.size() - 1));
        itemOverViewControler.setCallBack(this);
        mList_item_overview.add(itemOverViewControler);

        mRL_webcontent.addView(itemOverViewControler.webview);
        mRL_webcontent.setTag(itemOverViewControler);
	
        mLL_edit_windows.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mRL_webcontent.getChildCount() > 0){
					
					mRL_top.setVisibility(View.GONE);
					
                    mRL_webcontent.removeAllViews();
                    mRL_webcontent.setTag(null);

                    for (ViewHolder<View, ItemOverViewControler> viewholder:mList_viewholder) {
                        ((RelativeLayout) viewholder.itemView.findViewById(ResourceUtil.getId(mContext, "rl_content"))).removeAllViews();
                    }

                    mList_viewholder.clear();

                    stack = new OverviewAdapter<ViewHolder<View, ItemOverViewControler>, ItemOverViewControler>(mList_item_overview) {
                        @Override
                        public ViewHolder<View, ItemOverViewControler> onCreateViewHolder(Context context, ViewGroup parent) {
                            
                            final View v = View.inflate(context, ResourceUtil.getLayoutId(context, "item_windows"), null);
                            return new ViewHolder<View, ItemOverViewControler>(v);
                        }

                        @Override
                        public void onBindViewHolder(final ViewHolder<View, ItemOverViewControler> viewHolder) {
                            mList_viewholder.add(viewHolder);
                            final RelativeLayout rl_content = (RelativeLayout) viewHolder.itemView.findViewById(ResourceUtil.getId(mContext, "rl_content"));
                            if(rl_content.getChildCount() > 1){
                                rl_content.removeViewAt(0);
                            }
                            rl_content.addView(viewHolder.model.webview, 0);

                            ((View) viewHolder.itemView.findViewById(ResourceUtil.getId(mContext, "v_cover"))).setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                	mRL_top.setVisibility(View.VISIBLE);
                                    rl_content.removeViewAt(0);
                                    mRL_webcontent.addView(viewHolder.model.webview);
                                    mRL_webcontent.setTag(viewHolder.model);
                                }
                            });
                            if(mRL_webcontent.getChildCount() > 0){
                                rl_content.removeViewAt(0);
                                mRL_webcontent.addView(viewHolder.model.webview);
                                mRL_webcontent.setTag(viewHolder.model);
                            }
                        }
                    };
                    mRecentsView.removeAllViews();
                    mRecentsView.setTaskStack(stack);

                }else{

                }
			}
		});
        mLL_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mRL_webcontent.getChildCount() > 0){
					ItemOverViewControler item = (ItemOverViewControler)mRL_webcontent.getTag();
					if(item.webview.canGoBack()){
						item.webview.goBack();
					}else{
						
					}
				}
			}
		});
        mLL_finish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		int action = ev.getAction();
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if(mRL_webcontent.getChildCount() > 0){
				break;
			}
			OverviewCard card = mRecentsView.getChildView((int)ev.getX(), (int)ev.getY());
			if(null == card){
				break;
			}
			RelativeLayout rl_content = (RelativeLayout)card.findViewById(ResourceUtil.getId(mContext, "rl_content"));
			WebView wv = (WebView)rl_content.getChildAt(0);
			if(rl_content.getChildCount() > 1){
				for (ItemOverViewControler item : mList_item_overview) {
					if(wv.equals(item.webview)){
						
						if(item.getChildItemList().size() > 0){//当前window存在关联ID
							mRecentsView.setCanDelete(false);
						}else{//当前窗口不存在关联ID
							//该页面是否能够删除判断
							if(mList_item_overview.size() == 1){
								Toast.makeText(mContext, "唯一页面，无法删除", Toast.LENGTH_SHORT).show();
								mRecentsView.setCanDelete(false);
							}else{
								mRecentsView.setCanDelete(true);
							}
						}
						
						break;
					}
				}
			}
			break;

		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		super.onBackPressed();
		if(mRL_webcontent.getChildCount() >0) {
			WebView wv = ((ItemOverViewControler)mRL_webcontent.getTag()).webview;
			if(wv.canGoBack()){
				wv.goBack();
			}else{
				finish();
			}
		}
	}
	
	@Override
    public void onActivityResult(int requestCode,int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        BasePlugin callback = mECPlugin;
        if (callback != null) {
            callback.onActivityResult(requestCode, resultCode, data);
            mECPlugin = null;
        }
    }
	
	/** 插件类 */
    private BasePlugin mECPlugin = null;
	
	@Override
	public void startActivityForResult(BasePlugin plugin, Intent intent,
			int requestCode) {
		// TODO Auto-generated method stub
		/**
         * 启动另一个页面并等待返回结果
         */
        mECPlugin = plugin;
        startActivityForResult(intent, requestCode);
	}

	@Override
	public ItemOverViewControler addNewWebView(int type, String url) {

		URLDecoder decoder = new URLDecoder();
		url = decoder.decode(url);

		if(mList_item_overview.size() >= 10){
			return null;
		}
		if(StringUtil.isEmpty(url)){
			return null;
		}
		mList_webinfo.add(url);
//		mList_webinfo.add("http://zc.testpub.net/files/nativeJsBridgeDemo/js-api-demo.html");

        ItemOverViewControler nextItem = new ItemOverViewControler(mContext);
        String url1 = mList_webinfo.get(mList_webinfo.size() - 1);
        nextItem.loadUrl(url1);
        nextItem.setCallBack(this);
        mList_item_overview.add(nextItem);
        
        if(type == 1){
        	ItemOverViewControler currentItem = (ItemOverViewControler)mRL_webcontent.getTag();
        	currentItem.addChildItem(nextItem);
        	nextItem.setParentItem(currentItem);
        }

        mRL_webcontent.removeAllViews();
        mRL_webcontent.setTag(null);
        mRL_webcontent.addView(nextItem.webview);
        mRL_webcontent.setTag(nextItem);

        return nextItem;
	}

	@Override
	public String getWindowSession(String windowId) {
		if(mRL_webcontent.getChildCount() <= 0){
            return null;
        }
        if(mMap_content.containsKey(windowId)){
            return mMap_content.get(windowId);
        }else{
            return null;
        }
	}

	@Override
	public boolean setWindowSession(String content) {
		if(mRL_webcontent.getChildCount() <= 0){
            return false;
        }
        if(mMap_content.containsKey(((ItemOverViewControler)mRL_webcontent.getTag()).windowID)){
            mMap_content.remove(((ItemOverViewControler)mRL_webcontent.getTag()).windowID);
        }
        mMap_content.put(((ItemOverViewControler)mRL_webcontent.getTag()).windowID, content);

		JSONObject obj = new JSONObject();
		try {
			obj.put("content", content);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JSONObject resultObj = new JSONObject();
		try {
			resultObj.put("resultCode", 1);
			resultObj.put("resultMsg", "成功");
			resultObj.put("object", obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ItemOverViewControler itemCurrent = (ItemOverViewControler)mRL_webcontent.getTag();
		ItemOverViewControler itemParent = itemCurrent.getParentItem();
		if(itemParent != null){
			//子页面传值后，想父页面的js注入塞入的值
			itemParent.webview.loadUrl("javascript:" + "NativeJsAPI.newWindowCallback['" + itemCurrent.windowID + "']('" + resultObj + "');");
		}

        return true;
	}
	
	@Override
	public void recordH5Invoke(String RequestSep) {
		
	}
	
	@Override
	public void onAbilityResult(String abilityResult) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCardDismissed(int position) {
		// TODO Auto-generated method stub
		if(position > 0){
			ItemOverViewControler itemCurrent = mList_item_overview.get(position);
			ItemOverViewControler itemParent = itemCurrent.getParentItem();
			if(itemParent != null){
				itemParent.deleteChildItem(itemCurrent);
				//在app中删除window时，同事删除js端的windowID
				itemParent.webview.loadUrl("javascript:" + "clearInterval(NativeJsAPI.getNewWindowSessionJobs['" + itemCurrent.windowID + "']);");
			}
			
		}
	}

	@Override
	public void onAllCardsDismissed() {
		// TODO Auto-generated method stub
		
	}
	
	boolean needWebPageLoad = false;
	String currentUrl = "";
	String responseCode = "";
	Date startTime;
	Date finishTime;
	String responseText = "";
	
	@Override
	public void pageStart(String url) {
		currentUrl = url;
		responseCode = "200";
		startTime = new Date(System.currentTimeMillis());
	}
	
	@Override
	public void pageFinish(String title) {
		
		if(title != null && title.length() > 0 && title.length() < 20){
			mTV_title.setText(title);
		}
		finishTime = new Date(System.currentTimeMillis());
		
		if(needWebPageLoad){//当能力调用的type为H5时，上传相应信息
			ApiClient.recordH5Invoke(SDKUtil.RecordH5Invoke, handler, 1, currentUrl, startTime, finishTime, title, responseCode, SDKUtil.app_id);
		}
		
		needWebPageLoad = false;
		
		
	}
	@Override
	public void receivedError(String errorCode, String errorMsg) {
		responseCode = errorCode;
		responseText = errorMsg;
	}
	
	/**
     * 获取终端的系统版本号
     * 
     * @return
     */
    private int getPhoneAndroidSDK() {
        int version = 0;
        try {
            version = Build.VERSION.SDK_INT;
        } catch (NumberFormatException e) {
        }
        return version;

    }

    @Override
    protected void onResume() {
        super.onResume();
        for(ItemOverViewControler item:mList_item_overview){
            item.resume();
        }
//        registerReceiver(authInfoReceicer, authInfoIntentFilter);

    }
    
    @Override
    protected void onPause() {
        super.onPause();
        for(ItemOverViewControler item:mList_item_overview){
            item.pause();
        }
//        unregisterReceiver(authInfoReceicer);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        for(ItemOverViewControler item:mList_item_overview){
            item.destroy();
        }
    }
    
    Handler handler = new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		switch (msg.what) {
			case 0:
				
				break;

			default:
				break;
			}
    	}
    };

}
