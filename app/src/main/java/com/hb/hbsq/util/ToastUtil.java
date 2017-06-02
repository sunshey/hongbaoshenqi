package com.hb.hbsq.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.hb.hbsq.R;

/**
 * Created by admin on 2017/2/28.
 */

public class ToastUtil {

    private static Toast toast;
    public static void showToast(Context context, String message, int duration) {

        if (toast == null) {
            toast = Toast.makeText(context, message, duration);

        }else {
            toast.setText(message);
        }
        toast.show();
    }


    public static void showPrettyToast(Context context, String message) {

        TextView tv = new TextView(context);
        tv.setText(message);
        tv.setTextColor(context.getResources().getColor(R.color.orange));
        tv.setTextSize(18);
        tv.setBackgroundResource(R.drawable.toast_bg);
        int leftRightPx = UIUtils.dip2px(context, 12);
        int topBottomPx = UIUtils.dip2px(context, 8);
        tv.setPadding(leftRightPx, topBottomPx, leftRightPx, topBottomPx);
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(tv);
        toast.show();
    }
}
