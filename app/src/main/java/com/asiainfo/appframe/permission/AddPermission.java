package com.asiainfo.appframe.permission;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by stiven on 2017/10/25 0025.
 * 添加动态权限申请
 * 需要运行时申请的权限
 */

public class AddPermission implements ActivityCompat.OnRequestPermissionsResultCallback, PermissionActivity.PermissionListener {

    Activity context;

    public final static int CODE_PERMISSIONS_STORAGE = 800;
    public final static int CODE_PERMISSIONS_PHONE = 801;
    public final static int CODE_PERMISSIONS_BLUTTOTH = 802;
    public final static int CODE_PERMISSIONS_LOCATION= 803;
    public final static int CODE_PERMISSIONS_RECODE = 804;
    public final static int CODE_PERMISSIONS_CAMERA= 805;

    /**
     * 内存权限
     * Manifest.permission_group.STORAGE
     */
    String[] PERMISSIONS_STORAGE = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE

    };

    /**
     * 获取手机状态等权限
     * Manifest.permission_group.PHONE
     */
    String[] PERMISSIONS_PHONE = new String[]{
            Manifest.permission.READ_PHONE_STATE
    };

    /**
     * 蓝牙扫描权限
     */
    String[] PERMISSIONS_BLUETOOTCH = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    /**
     * 位置权限
     */
    String[] PERMISSIONS_LOCATION = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * 录音权限
     */
    String[] PERMISSIONS_RECODE = new String[]{
            Manifest.permission.RECORD_AUDIO
    };

    /**
     * 相册、拍照权限
     */
    String[] PERMISSIONS_CAMERA = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    PermissionsListener listener;

    public PermissionsListener getListener() {
        return listener;
    }

    public AddPermission(Activity context){
        this.context = context;
    }

    public void addPermission(PermissionsListener listener, int code){
        this.listener = listener;

        Map<Integer, String[]> permissionMap = new HashMap<>();
        permissionMap.put(CODE_PERMISSIONS_STORAGE, PERMISSIONS_STORAGE);
        permissionMap.put(CODE_PERMISSIONS_PHONE, PERMISSIONS_PHONE);
        permissionMap.put(CODE_PERMISSIONS_BLUTTOTH, PERMISSIONS_BLUETOOTCH);
        permissionMap.put(CODE_PERMISSIONS_LOCATION, PERMISSIONS_LOCATION);
        permissionMap.put(CODE_PERMISSIONS_RECODE, PERMISSIONS_RECODE);
        permissionMap.put(CODE_PERMISSIONS_CAMERA, PERMISSIONS_CAMERA);
        if(Build.VERSION.SDK_INT >= 23){
            checkPermissions(permissionMap.get(code), code);
        }else{
            listener.onPermissionListener(true, code);
        }
    }

    public void checkPermissions(String[] permissions, int code){
        boolean CHECK_YES = true;
        for (String permission:permissions) {
            if(ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED){
                CHECK_YES = false;
            }
        }

        if (!CHECK_YES){
            requestPermissions(permissions, code);
        }else{
            listener.onPermissionListener(true, code);
        }

    }

    public void requestPermissions(final String[] permissions, final int code){

        boolean SHOULD_YES = true;

        for (String permission:permissions) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(context, permission)){
                SHOULD_YES = false;
            }
        }

        if(!SHOULD_YES){
            AlertDialog.Builder build = new AlertDialog.Builder(context);
            build.setTitle("提示");

            switch (code){
                case CODE_PERMISSIONS_STORAGE:
                    build.setMessage("请开启存储权限");
                    break;
                case CODE_PERMISSIONS_PHONE:
                    build.setMessage("请开启手机状态权限");
                    break;
                case CODE_PERMISSIONS_BLUTTOTH:
                    build.setMessage("请开启蓝牙扫描权限");
                    break;
                case CODE_PERMISSIONS_LOCATION:
                    build.setMessage("请开启位置权限");
                    break;
                case CODE_PERMISSIONS_RECODE:
                    build.setMessage("请开启录音权限");
                    break;
                case CODE_PERMISSIONS_CAMERA:
                    build.setMessage("请开启摄像头权限");
                    break;
            }

            build.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).setPositiveButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    PermissionActivity.setPermissionListener(AddPermission.this);
                    Intent intent = new Intent(context, PermissionActivity.class);
                    intent.putExtra(PermissionActivity.KEY_INPUT_PERMISSIONS, permissions);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(PermissionActivity.KEY_PERMISSION_CODE, code);
                    context.startActivity(intent);
//                    ActivityCompat.requestPermissions(context, permissions, code);
                }
            });
            build.create().show();
        }else{
            PermissionActivity.setPermissionListener(AddPermission.this);
            Intent intent = new Intent(context, PermissionActivity.class);
            intent.putExtra(PermissionActivity.KEY_INPUT_PERMISSIONS, permissions);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(PermissionActivity.KEY_PERMISSION_CODE, code);
            context.startActivity(intent);
//            ActivityCompat.requestPermissions(context, permissions, code);
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean GRANTED = PermissionUtil.verifyPermissions(grantResults);
        switch (requestCode){
            case CODE_PERMISSIONS_STORAGE:
                if(GRANTED){
                    listener.onPermissionListener(true, requestCode);
                }else{
                    AlertDialog.Builder build = new AlertDialog.Builder(context);
                    build.setTitle("获取存储空间");
                    build.setMessage("我们需要获取存储空间，为你存储个人信息；否则，你将无法正常使用直通一线\n设置路径：设置->应用->直通一线->权限");
                    build.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            listener.onPermissionListener(false, requestCode);
                            context.finish();
                        }
                    }).setPositiveButton("去设置", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent =  new Intent(Settings.ACTION_APPLICATION_SETTINGS);
                            context.startActivity(intent);
                        }
                    });
                    build.create().show();
                }
                break;
            case CODE_PERMISSIONS_PHONE:
            case CODE_PERMISSIONS_BLUTTOTH:
            case CODE_PERMISSIONS_LOCATION:
            case CODE_PERMISSIONS_RECODE:
            case CODE_PERMISSIONS_CAMERA:
            	if(GRANTED){
                    listener.onPermissionListener(true, requestCode);
                }else{
                	AlertDialog.Builder build = new AlertDialog.Builder(context);
                    build.setMessage("相关权限被关闭，请开启权限后重试");
                    build.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            listener.onPermissionListener(false, requestCode);
                            context.finish();
                        }
                    }).setPositiveButton("去设置", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent =  new Intent(Settings.ACTION_APPLICATION_SETTINGS);
                            context.startActivity(intent);
                        }
                    });
                    build.create().show();
                }
//                AlertDialog.Builder build = new AlertDialog.Builder(context);
//                build.setMessage("相关权限被关闭，请开启权限后重试");
//                build.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        listener.onPermissionListener(false, requestCode);
//                        context.finish();
//                    }
//                });
//                build.create().show();
                break;
        }
    }

    public abstract static class PermissionUtil {
        public static boolean verifyPermissions(int[] grantResults) {
            // At least one result must be checked.
            if (grantResults.length < 1) {
                return false;
            }

            // Verify that each required permission has been granted, otherwise return false.
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
            return true;
        }

    }

    public interface PermissionsListener{
        public void onPermissionListener(boolean hasPermission, int code);
    }

}
