package com.dd.ddsq.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.dd.ddsq.config.SPConstant;


public class SPUtils {


    public static SharedPreferences getSP(Context context) {
        return context.getSharedPreferences(SPConstant.SP_NAME, Context.MODE_PRIVATE);
    }

    public static SharedPreferences getSP(Context context, String name) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static void put(Context context, String key, Object value) {
        SharedPreferences sp = getSP(context);
        Editor editor = sp.edit();
        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        }
        editor.apply();
    }

    public static void put1(Context context, String key, Object value) {
        SharedPreferences sp = getSP(context, SPConstant.SP_NEW_NAME);
        Editor editor = sp.edit();
        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        }
        editor.apply();
    }

    public static boolean getBoolean1(Context context, String key) {
        return getSP(context, SPConstant.SP_NEW_NAME).getBoolean(key, false);
    }

    public static boolean getBoolean(Context context, String key) {
        return getSP(context).getBoolean(key, false);
    }

    public static boolean getBoolean(Context context, String key, boolean defalutValue) {
        return getSP(context).getBoolean(key, defalutValue);
    }

    public static boolean getBoolean1(Context context, String key, boolean defalutValue) {
        return getSP(context, SPConstant.SP_NEW_NAME).getBoolean(key, defalutValue);
    }

    public static String getString(Context context, String key) {
        return getSP(context).getString(key, null);
    }

    public static String getString1(Context context, String key) {
        return getSP(context, SPConstant.SP_NEW_NAME).getString(key, null);
    }

    public static int getInt(Context context, String key) {
        return getSP(context).getInt(key, SPConstant.INT_VALUE_INVAILD);
    }

    public static float getFloat(Context context, String key) {
        return getSP(context).getFloat(key, SPConstant.INT_VALUE_INVAILD);
    }


    public static int getInt(Context context, String key, int defaultValue) {
        return getSP(context).getInt(key, defaultValue);
    }

    public static long getLong(Context context, String key, long defaultValue) {
        return getSP(context).getLong(key, defaultValue);

    }

    public static void clearSP(Context context) {
        getSP(context).edit().clear().apply();
    }


    public static void remove(Context context, String key) {
        getSP(context).edit().remove(key).apply();
    }
}
