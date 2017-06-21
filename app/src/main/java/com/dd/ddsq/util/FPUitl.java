package com.dd.ddsq.util;

import android.content.Context;


/**
 * Created by zhangkai on 2017/2/20.
 */

public class FPUitl {
    public static String get(Context context, String key, String value) {
        String result = "";
        String fStr = FileUtil.readInfoInSDCard(context, PathUtil.getConfigPath(), key);
        if (fStr == null) {
            fStr = SPUtils.getSP(context).getString(key, value);
        }
        result = fStr;
        return result;
    }

    public static void putString(Context context, String key, String value) {
        SPUtils.put(context, key, value);
        FileUtil.writeInfoInSDCard(context, value, PathUtil.getConfigPath(), key);
    }
}
