package com.asiainfo.appframe.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class WindowSizeUtil {

    /**
     * 获取屏幕宽高
     * @param context
     * @return
     */
    public static int[] getWindowSize(Context context){

//		WindowManager wm = (WindowManager)((Activity) context).getWindowManager();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;
        int[] wh = {height, width};
        return wh;
    }

    /**
     * 获取状态栏高度
     * 状态栏高度=取大于其的最小整数（25*上下文_获取应用包的资源实例_获取当前屏幕尺寸_屏幕密度比例）
     * @param context
     * @return
     */
//    public static double getStatusBarHeight(Context context){
//        double statusBarHeight = Math.ceil(25 * context.getResources().getDisplayMetrics().density);
//        return statusBarHeight;
//    }

    /**
     * 获取状态栏高度
     * @param context
     * @return
     */
//    public static int getStatusBarHeight(Context context) {
//        int result = 0;
//        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
//        if (resourceId > 0) {
//            result = context.getResources().getDimensionPixelSize(resourceId);
//        }
//        return result;
//    }
}
