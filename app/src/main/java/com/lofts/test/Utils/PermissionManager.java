package com.lofts.test.Utils;

import android.app.Activity;
import android.os.Build;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;

import java.util.ArrayList;
import java.util.List;


public class PermissionManager {

    private Activity mActivity;
    private static XXPermissions xPermissions;

    private PermissionManager(Activity activity) {
        mActivity = activity;
    }

    public static PermissionManager with(Activity activity) {
        xPermissions = XXPermissions.with(activity);
        return new PermissionManager(activity);
    }

    public PermissionManager permission(String... permissions) {
        xPermissions.permission(permissions);
        return this;
    }


    public PermissionManager permission(String[]... permissions) {
        xPermissions.permission(permissions);
        return this;
    }


    public PermissionManager permission(List<String> permissions) {
        xPermissions.permission(permissions);
        return this;
    }

    public void request(OnRequestPermission call) {
        //targetSdkVersion小于23不需要处理权限问题
        if (mActivity.getApplicationInfo().targetSdkVersion < Build.VERSION_CODES.M) {
            call.requestSuccess(new ArrayList<>(), false);
        } else {
            xPermissions.request(new OnPermission() {
                @Override
                public void hasPermission(List<String> granted, boolean isAll) {
                    call.requestSuccess(granted, isAll);
                }
                @Override
                public void noPermission(List<String> denied, boolean quick) {
                    call.requestSuccess(denied, quick);
                }
            });
        }
    }


    public interface OnRequestPermission {

        //权限请求成功
        void requestSuccess(List<String> granted, boolean isAll);

        //权限请求失败
        void resuestFailure(List<String> granted, boolean isAll);
    }

}
