package com.hb.hbsq.util;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import com.hb.hbsq.R;

/**
 * Created by wanglin  on 2017/3/20 15:21.
 */
public class LoadingUtil {

    public static void show(Context context, String mess) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(R.layout.dialog_loading);
    }

}
