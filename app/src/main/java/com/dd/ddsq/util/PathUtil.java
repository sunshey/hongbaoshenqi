package com.dd.ddsq.util;

import com.dd.ddsq.config.APPConfig;

import java.io.File;

/**
 * Created by zhangkai on 2017/2/16.
 */

public class PathUtil {
    public static String getConfigPath() {
        makeBaseDir();
        File dir = new File(APPConfig.PATH + "/qhb_configs");
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir.getAbsolutePath();
    }

    private static void makeBaseDir() {
        File dir = new File(APPConfig.PATH);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }
}
