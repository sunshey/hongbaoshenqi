package com.dd.ddsq.listener;

import java.io.File;

/**
 * Created by wanglin  on 2017/3/15 11:46.
 */

public interface OnUpdateListener {
    void onSuccess(File file);

    void onFailue();
}
