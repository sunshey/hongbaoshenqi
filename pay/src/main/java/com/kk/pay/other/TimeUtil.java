package com.kk.pay.other;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhangkai on 2017/3/18.
 */

public class TimeUtil {
    //< 获取当前时间yyyy-MM-dd格式字符串
    public static String getTimeStr() {
        Date cDate = new Date();
        String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        return fDate;
    }

    //< 获取当前时间yyyy-MM-dd格式字符串2
    public static String getTimeStr2() {
        Date cDate = new Date();
        String fDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cDate);
        return fDate;
    }

}
