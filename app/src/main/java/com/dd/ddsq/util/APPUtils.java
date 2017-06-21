package com.dd.ddsq.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.io.File;
import java.util.List;

/**
 * Created by admin on 2017/2/27.
 */

public class APPUtils {
    /**
     * 判断手机是否安装有某个指定的应用
     *
     * @param context
     * @param packageName
     * @return
     */

    public static boolean isInstallAPP(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        boolean isInstalled = false;
        try {
            PackageInfo packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            if (packageInfo != null) {
                isInstalled = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            isInstalled = false;
        }

        return isInstalled;
    }

    /**
     * 跳转到某个指定的应用
     *
     * @param activity
     * @param packageName
     * @param launcher
     */
    public static void switchAPP(Activity activity, String packageName, String launcher) {
        Intent intent = new Intent();
        ComponentName cmp = new ComponentName(packageName, launcher);// 报名该有activity

        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(cmp);
        activity.startActivityForResult(intent, 0);
    }

    public static PackageInfo getPackageInfo(Context context) {
        PackageManager pm = context.getPackageManager();
        //通过当前的包名获取包的信息
        try {
            return pm.getPackageInfo(context.getPackageName(), 0);//获取包对象信息;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void InstallAPK(Context context, File file) {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            LogUtil.msg(e.getMessage());
        }
    }

    /**
     * 判断应用是否前台应用
     *
     * @return
     */
    public static boolean isForeGround(Context context, String packageName) {
        boolean flag = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningAppProcess : runningAppProcesses) {

            LogUtil.msg("iswx::" + runningAppProcess.processName);
            if (runningAppProcess.processName.equals(packageName)
                    && runningAppProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * 判断应用程序处于前台还是后台
     */
    public static boolean isMyApp(Context context, String packageName) {

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {

            ComponentName topActivity = tasks.get(0).topActivity;

            if (topActivity.getPackageName().equals(packageName)) {
                return true;
            }
        }
        return false;
    }



}
