package com.hb.hbsq.util;

import android.util.Log;

/**
 * 日志输出类
 *
 * @author zhangkai
 */
public class LogUtil {
    public static boolean DEBUG = true;

    private static final int LEVEL = 2;// 日志输出级别
    private static final int V = 0;
    private static final int D = 1;
    private static final int I = 2;
    private static final int W = 3;
    private static final int E = 4;

    private static final String TAG = "微信抢红包神器";


    public static void msg(String msg) {
        switch (LEVEL) {
            case V:
                if (DEBUG)
                    Log.v(TAG, msg);
                break;
            case D:
                if (DEBUG)
                    Log.d(TAG, msg);
                break;
            case I:
                if (DEBUG)
                    Log.i(TAG, msg);
                break;
            case W:
                if (DEBUG)
                    Log.w(TAG, msg);
                break;
            case E:
                if (DEBUG)
                    Log.e(TAG, msg);
                break;
            default:
                break;
        }
    }
}
