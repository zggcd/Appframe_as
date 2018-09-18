package com.asiainfo.appframe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.asiainfo.appframe.utils.ResourceUtil;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.utils.DistanceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by imahe001 on 2015/9/29.
 */
public class PoiSearchAdapter extends BaseAdapter {

    private Context context;
    private List<PoiInfo> poiInfos = new ArrayList<>();
    private final LatLng locationLatLng;

    public PoiSearchAdapter(Context context, List<PoiInfo> poiInfos, LatLng locationLatLng) {
        this.context = context;
        this.poiInfos = poiInfos;
        this.locationLatLng = locationLatLng;
    }

    @Override
    public int getCount() {
        return poiInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return poiInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(ResourceUtil.getLayoutId(context, "appframe_poisearch_item"), null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PoiInfo poiInfo = poiInfos.get(position);
        holder.poisearch_name.setText(poiInfo.name);
        holder.poisearch_address.setText(poiInfo.address);
        holder.poisearch_distance.setText((int) DistanceUtil.getDistance(locationLatLng, poiInfo.location)+"米");
        return convertView;
    }
    
    public void loadMoreView(List<PoiInfo> list){
    	poiInfos.addAll(list);
        this.notifyDataSetChanged();//强制动态刷新数据进而调用getView方法
   }

    class ViewHolder {
        TextView poisearch_name;
        TextView poisearch_address;
        TextView poisearch_distance;

        public ViewHolder(View view) {
            poisearch_name = (TextView) view.findViewById(ResourceUtil.getId(context, "poisearch_name"));
            poisearch_address = (TextView) view.findViewById(ResourceUtil.getId(context, "poisearch_address"));
            poisearch_distance = (TextView) view.findViewById(ResourceUtil.getId(context, "poisearch_distance"));
        }
    }
}
