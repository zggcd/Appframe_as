package com.asiainfo.appframe.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.asiainfo.appframe.R;
import com.asiainfo.appframe.bean.AreaCodes;
import com.asiainfo.appframe.utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class AreaChooseDialog extends Dialog {

    private Context mContext;

    //data
    private List<AreaCodes> mList_AuthCodes = new ArrayList<AreaCodes>();
    private AreaCodes mCurrent_authcodes = null;

    //View
    private Button btn_area_choose;
    private RecyclerView rv_area_list;

    public AreaChooseDialog(Context context, List<AreaCodes> list) {
        super(context, R.style.dialog);
        this.mContext = context;
        this.mList_AuthCodes = list;
        initView();
    }

    private void initView(){

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.appframe_dialog_area_choose, null);

        Window window = this.getWindow();
        if (window != null) { //设置dialog的布局样式 让其位于底部
            window.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.y = CommonUtil.dip2px(mContext,10); //设置居于底部的距离
            window.setAttributes(lp);
        }
        setContentView(view);

        btn_area_choose = findViewById(R.id.btn_area_choose);
        rv_area_list = findViewById(R.id.rv_area_list);

        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.width = (int) CommonUtil.getWindowSize(mContext)[1] / 6 * 5;
        lp.height = (int) CommonUtil.getWindowSize(mContext)[0] / 3 * 2;
        view.setLayoutParams(lp);

        //设置布局管理器
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        //设置布局管理一条数据占用几行，如果是头布局则头布局自己占用一行
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });

        rv_area_list.setLayoutManager(gridLayoutManager);

        if(mList_AuthCodes != null && mList_AuthCodes.size() > 0){
            mCurrent_authcodes = mList_AuthCodes.get(0);
        }

        AreaListAdapter adapter = new AreaListAdapter(mContext, mList_AuthCodes);
        rv_area_list.setAdapter(adapter);

    }

    View.OnClickListener chooseListener = null;
    public void setOnChooseListener(View.OnClickListener listener){
        chooseListener = listener;
        btn_area_choose.setOnClickListener(chooseListener);
    }

    public AreaCodes getmCurrent_authcodes() {
        return mCurrent_authcodes;
    }

    class AreaListAdapter extends RecyclerView.Adapter<AreaAdapterHolder>{

        private Context context;
        private List<AreaCodes> list = new ArrayList<>();
        int choose = 0;

        public AreaListAdapter(Context context, List<AreaCodes> list){
            this.context = context;
            this.list = list;
        }

        @NonNull
        @Override
        public AreaAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View root = LayoutInflater.from(context).inflate(R.layout.appframe_listitem_area, parent, false);
            return new AreaAdapterHolder(root);
        }

        @Override
        public void onBindViewHolder(@NonNull AreaAdapterHolder holder, int position) {
            AreaCodes areaCodes = list.get(position);
            holder.tv_area_name.setText(areaCodes.getAreaName());
            if (holder.tv_area_name.getTag() != null){
                holder.tv_area_name.setTag(position);
            }

            if(choose == position){
                holder.tv_area_name.setBackgroundResource(R.drawable.appframe_shape_area_bg);
            }else{
                holder.tv_area_name.setBackgroundResource(android.R.color.white);
            }

            holder.tv_area_name.setOnClickListener(click->{
                selectChange(position);
                mCurrent_authcodes = list.get(position);
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        /**
         * 改变选择的地区
         * @param position
         */
        private void selectChange(int position){
            choose = position;
            notifyDataSetChanged();
        }

    }

    class AreaAdapterHolder extends RecyclerView.ViewHolder{

        TextView tv_area_name;
        int bg_state = 0;

        public AreaAdapterHolder(View itemView) {
            super(itemView);
            tv_area_name = itemView.findViewById(R.id.tv_area_name);
        }
    }

}
