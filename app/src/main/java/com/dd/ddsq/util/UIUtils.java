package com.dd.ddsq.util;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.dd.ddsq.bean.GoagalInfo;
import com.dd.ddsq.bean.LoginDataInfo;
import com.dd.ddsq.bean.UserVipInfo;
import com.dd.ddsq.bean.VipInfo;
import com.dd.ddsq.config.PayConfig;
import com.dd.ddsq.config.SPConstant;
import com.kk.securityhttp.net.contains.HttpConfig;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by admin on 2017/2/23.
 */

public class UIUtils implements Observer {

    /**
     * 判断是否是合法的手机号
     *
     * @param number
     * @return
     */
    public static boolean isMatchPhone(String number) {

        return number.matches("((13[0-9])|(15[0-9])|(14[0-9])|(16[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}");
    }

    /**
     * 判断抢红包服务是否打开
     *
     * @param context
     * @return
     */
    public static boolean isOpenService(Context context) {
        AccessibilityManager am = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> serviceList = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        for (AccessibilityServiceInfo info : serviceList) {
            LogUtil.msg(info.getId());
            if (info.getId().equals(context.getPackageName() + "/.service.HbService")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    /**
     * 将当天日期格式化后转换成字符串
     *
     * @return
     */
    public static String date2str() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    public static String getKey(String key) {
        return key.replace("-----BEGIN RSA PRIVATE KEY-----", "").replace("-----END RSA PRIVATE KEY-----", "")
                .replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "")
                .replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "").replace("\r", "")
                .replace("\n", "");
    }

    /**
     * 获得当前时间
     *
     * @param
     * @return
     */
    public static String getCurrentTime() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return df.format(new Date());
    }

    /**
     * 将日期转换成string类型
     *
     * @param date
     * @return
     */
    public static String long2Str(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return df.format(date);
    }

    /**
     * 同步数据
     *
     * @return
     */
    public static List<String> synVipIds(Context context) {
        List<String> idList = new ArrayList<>();
        if (GoagalInfo.loginDataInfo != null && GoagalInfo.loginDataInfo.getVipListInfo() != null) {
            List<UserVipInfo> vipListInfo = GoagalInfo.loginDataInfo.getVipListInfo();
            if (vipListInfo != null) {
                for (UserVipInfo vipInfo : vipListInfo) {
                    idList.add(vipInfo.getVip_id());
                }
            }
        }

        String vipsInfos = FPUitl.get(context, PayConfig.Prf_VipInfos, "");
        String[] vipsInfoIds = vipsInfos.split("-");
        for (String id : vipsInfoIds) {
            boolean flag = false;
            for (String _id : idList) {
                if (_id.equals(id)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                idList.add(id);
            }
        }
        return idList;
    }

    /**
     * 获取用户ID
     *
     * @return
     */
    public static String getUid(Context mContext) {
        String uid = "";
        String json = SPUtils.getString(mContext, SPConstant.GOAGAL_INFO_KEY);
        LoginDataInfo info = JSON.parseObject(Encrypt.decode(json), LoginDataInfo.class);
        if (GoagalInfo.loginDataInfo == null) {
            GoagalInfo.loginDataInfo = info;
        }
        if (GoagalInfo.loginDataInfo != null && GoagalInfo.loginDataInfo.getVipInfo() != null) {
            VipInfo vipInfo = GoagalInfo.loginDataInfo.getVipInfo();
            uid = vipInfo.getUid();
        } else {
            ToastUtil.showToast(mContext, HttpConfig.NET_ERROR, Toast.LENGTH_SHORT);
        }
        return uid;
    }

    @Override
    public void update(Observable o, Object arg) {

        LogUtil.msg("data  " + arg.toString());
    }
}
