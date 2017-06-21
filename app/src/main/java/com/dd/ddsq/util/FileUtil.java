package com.dd.ddsq.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;


import com.dd.ddsq.bean.GoagalInfo;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;


public class FileUtil {

    private static String getFileName(String name){
        return Md5.md5(GoagalInfo.uuid + name);
    }

    ///< 读取输入流
    public static String readString(InputStream in) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line;
        StringBuffer result = new StringBuffer();
        while ((line = br.readLine()) != null) {
            result.append(line + "\n");
        }
        return result.toString();
    }

    ///< 写入字符到sdcard
    public static void writeInfoInSDCard(Context context, String result, String dir, String name) {
        String tmpName = getFileName(name);
        String tmpResult = Base64.encode(Encrypt.encode(result).getBytes());
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdir();
        }
        File gpxfile = new File(dir, tmpName);
        try {
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(tmpResult);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            LogUtil.msg("w->" + e.getMessage());
        }
        LogUtil.msg("file w ->" + tmpName + "->" + result);
    }

    ///< 写入图片到sdcard
    public static void writeImageInSDCard(Context context, Bitmap bitmap, String dir, String name) {
        String tmpName = getFileName(name);
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdir();
        }
        File logoFile = new File(dir, tmpName);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(logoFile);
            fos.write(bitmapdata);
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LogUtil.msg(tmpName + "->" + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.msg(tmpName + "->" + e.getMessage());
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        LogUtil.msg("w icon->" + logoFile.getAbsolutePath());
    }

    ///< 从sdcard获取图片
    public static Bitmap getImageFromSDCard(Context context, String dir, String name) {
        String tmpName = getFileName(name);
        String filePath = dir + "/" + tmpName;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        LogUtil.msg("r icon->" + filePath);
        return bitmap;
    }

    ///< 从sdcard获取字符
    public static String readInfoInSDCard(Context context, String dir, String name) {
        String tmpName = getFileName(name);
        File file = new File(dir, tmpName);
        if (file.exists()) {
            //Read text from file
            StringBuilder text = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    text.append(line);
                }
                br.close();
                String reuslt = Encrypt.decode(new String(Base64.decode(text.toString())));
                LogUtil.msg("r ->" + tmpName + "->" + reuslt);
                return reuslt;
            } catch (IOException e) {
                //You'll need to add proper error handling here
                e.printStackTrace();
                LogUtil.msg("r->" + e.getMessage());
            }
        }
        return null;
    }


    public static void writeInputStreamToFile(InputStream in, String path) {
        OutputStream output = null;
        try {
            File file = new File(path);
            output = new FileOutputStream(file);

            byte[] buffer = new byte[4 * 1024]; // or other buffer size
            int read;
            while ((read = in.read(buffer)) != -1) {
                output.write(buffer, 0, read);
            }
            output.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                output.close();
            } catch (Exception e) {
            }
        }
    }
}
