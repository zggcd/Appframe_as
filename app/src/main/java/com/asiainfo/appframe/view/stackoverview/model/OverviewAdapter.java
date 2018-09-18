package com.asiainfo.appframe.view.stackoverview.model;


import android.content.Context;
import android.view.ViewGroup;

import com.asiainfo.appframe.view.stackoverview.misc.OverviewConfiguration;
import com.asiainfo.appframe.view.stackoverview.views.OverviewCard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stiven on 2017/5/18 0018.
 */

public abstract class OverviewAdapter<VH extends ViewHolder, Model extends Object> {

    /** Task stack callback **/
    public interface Callbacks{
        public void onCardAdded(OverviewAdapter adapter, int position);
        public void onCardRemoved(OverviewAdapter adapter, int position);
    }

    Callbacks mCallbacks;

    //这个只是单纯用来计数的
    List<Model> mItems = new ArrayList<>();

    public OverviewAdapter(List<Model> models){
        if (models != null) {
            mItems = models;
        }
    }

    /** Sets the callbacks for this task stack */
    public void setCallbacks(Callbacks cb) {
        mCallbacks = cb;
    }

    /**
     * 插入元素
     * @param model 元素
     * @param position 位置
     */
    public void notifyDataSetInserted(Model model, int position)
    {
        if (position < 0 || position > mItems.size()) {
            throw new IllegalArgumentException("Position is out of bounds.");
        }

        mItems.add(position, model);

        if (mCallbacks != null) {
            mCallbacks.onCardAdded(this, position);
        }
    }

    /** Removes a task */
    public void notifyDataSetRemoved(int position)
    {
        if (position < 0 || position > mItems.size()) {
            throw new IllegalArgumentException("Position is out of bounds.");
        }

        if (mCallbacks != null) {
            // Notify that a task has been removed
            mCallbacks.onCardRemoved(this, position);
        }
        
        mItems.remove(position);
    }

    /**
     * 只不过是删除然后再重新添加罢了，来实现改变某项内容
     * @param newItems
     */
    public void notifyDataSetChanged(List<Model> newItems)
    {
        if (newItems == null) {
            newItems = new ArrayList<>();
        }

        for(int i = 0; i < mItems.size(); ++i)
        {
            if (mCallbacks != null) {
                mCallbacks.onCardRemoved(this, i);
            }
        }

        mItems = newItems;
        for(int i =0; i < newItems.size(); ++i) {
            if (mCallbacks != null) {
                mCallbacks.onCardAdded(this, i);
            }
        }
    }

    public List<Model> getData() { return mItems;}

    public abstract VH onCreateViewHolder(Context context, ViewGroup parent);

    /**
     * This method is expected to populate the view in vh with the model in vh.
     */
    public abstract void onBindViewHolder(VH vh);

    public final int getNumberOfItems() {
        return mItems.size();
    }

    public final VH createViewHolder(Context context, OverviewConfiguration config) {

        OverviewCard container = new OverviewCard(context);
        container.setConfig(config);
        VH vh = onCreateViewHolder(context, container);
        vh.setContainer(container);
        return vh;
    }

    public final void bindViewHolder(VH vh, int position) {
        vh.model = mItems.get(position);
        onBindViewHolder(vh);
    }

    public void removeItemView(){

    }

}
