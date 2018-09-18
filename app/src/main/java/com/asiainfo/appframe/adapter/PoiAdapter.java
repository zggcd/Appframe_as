package com.asiainfo.appframe.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.asiainfo.appframe.utils.ResourceUtil;
import com.baidu.mapapi.search.core.PoiInfo;

import java.util.List;

/**
 * poi适配器
 */
public class PoiAdapter extends BaseAdapter {
    private Context context;
    private List<PoiInfo> pois;
    
    private int chooseItem = 0;

    public PoiAdapter(Context context, List<PoiInfo> pois) {
        this.context = context;
        this.pois = pois;
    }

    @Override
    public int getCount() {
        return pois.size();
    }

    @Override
    public Object getItem(int position) {
        return pois.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(ResourceUtil.getLayoutId(context, "appframe_locationpois_item"), null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == chooseItem) {
            holder.iv_location.setVisibility(View.VISIBLE);
            holder.locationpoi_name.setTextColor(Color.parseColor("#FF5722"));
        }else{
            holder.iv_location.setVisibility(View.GONE);
            holder.locationpoi_name.setTextColor(Color.parseColor("#000000"));
        }
        PoiInfo poiInfo = pois.get(position);
        holder.locationpoi_name.setText(poiInfo.name);
        holder.locationpoi_address.setText(poiInfo.address);
        return convertView;
    }
    
    public void changeChooseItem(int position){
    	chooseItem = position;
    	this.notifyDataSetChanged();
    }
    
    public void loadMoreView(List<PoiInfo> list){
    	 pois.addAll(list);
         this.notifyDataSetChanged();//强制动态刷新数据进而调用getView方法
    }
    
    public void setList(List<PoiInfo> list){
    	pois = list;
    	this.notifyDataSetChanged();//强制动态刷新数据进而调用getView方法
    	
    }

    class ViewHolder {
        TextView locationpoi_name;
        TextView locationpoi_address;
        ImageView iv_location;

        ViewHolder(View view) {
            locationpoi_name = (TextView) view.findViewById(ResourceUtil.getId(context, "locationpois_name"));
            locationpoi_address = (TextView) view.findViewById(ResourceUtil.getId(context, "locationpois_address"));
            iv_location = (ImageView) view.findViewById(ResourceUtil.getId(context, "iv_location"));
        }
    }
}
