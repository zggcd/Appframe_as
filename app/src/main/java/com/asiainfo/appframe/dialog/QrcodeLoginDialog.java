package com.asiainfo.appframe.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.asiainfo.appframe.R;
import com.asiainfo.appframe.utils.CommonUtil;

public class QrcodeLoginDialog extends Dialog {

    private Context mContext;


    //View
    private ImageView iv_login_sure, iv_login_cancel;

    public QrcodeLoginDialog(Context context) {
        super(context, R.style.dialog);
        this.mContext = context;
        initView();
    }

    private void initView(){

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.appframe_dialog_qrcodescan, null);

        Window window = this.getWindow();
        if (window != null) { //设置dialog的布局样式 让其位于底部
            window.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.y = CommonUtil.dip2px(mContext,10); //设置居于底部的距离
            window.setAttributes(lp);
        }
        setContentView(view);

        iv_login_sure = findViewById(R.id.iv_login_sure);
        iv_login_cancel = findViewById(R.id.iv_login_cancel);

        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.width = (int) CommonUtil.getWindowSize(mContext)[1] / 6 * 5;
//        lp.height = (int) CommonUtil.getWindowSize(mContext)[0] / 3 * 2;
        view.setLayoutParams(lp);

    }


    /**
     * 取消登录
     * @param listener
     * @return
     */
    public QrcodeLoginDialog setNegativeButton(View.OnClickListener listener){

        iv_login_cancel.setOnClickListener(listener);

        return this;
    }

    /**
     * 确认登录
     * @param listener
     * @return
     */
    public QrcodeLoginDialog setPositiveButton(View.OnClickListener listener){

        iv_login_sure.setOnClickListener(listener);

        return this;
    }

}
