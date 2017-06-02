package com.kk.pay.other;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by zhangkai on 16/9/19.
 */
public class ToastUtil {
    public static void toast(Context context, String msg) {
        if (msg == null || msg.trim().isEmpty()) {
            msg = "火☆秂攻入哋球，请重试";
        }
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }

    public static void toast2(Context context, String msg) {
        if (msg == null || msg.trim().isEmpty()) {
            msg = "火☆秂攻入哋球，请重试";
        }
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
    public static void toast3(Context context, String msg) {
        if (msg == null || msg.trim().isEmpty()) {
            msg = "火☆秂攻入哋球，请重试";
        }
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }
}
