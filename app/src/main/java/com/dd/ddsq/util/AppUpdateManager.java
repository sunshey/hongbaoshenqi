package com.dd.ddsq.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;

import com.dd.ddsq.listener.OnUpdateListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wanglin  on 2017/3/15 11:48.
 */

public class AppUpdateManager {

    private static final int DOWNLOAD_SUCCESS = 1;
    private static final int DOWNLOAD_ERROR = 2;
    private static final int DOWNLOAD_FAIL = 3;


    public static void downloadFile(final Context context, final String url, final File dir, final OnUpdateListener listener) {

//        final ProgressDialog progressDialog = new ProgressDialog(context);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        progressDialog.setTitle("提示信息");
//        progressDialog.setMessage("正在下载，请稍后...");
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.setCancelable(false);
//        progressDialog.setMax(100);
        LogUtil.msg("url:  " + url);
        final Handler mhandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case DOWNLOAD_SUCCESS:
                        File file = (File) msg.obj;

                        listener.onSuccess(file);
//                        progressDialog.dismiss();

                        break;
                    case DOWNLOAD_ERROR:
                    case DOWNLOAD_FAIL:
                        listener.onFailue();
                        break;
                }
            }
        };
//        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient mOkHttpClient = new OkHttpClient();
                final Request request = new Request.Builder()
                        .url(url)
                        .build();
                final Call call = mOkHttpClient.newCall(request);

                call.enqueue(new Callback() {

                    @Override
                    public void onFailure(Call call, IOException e) {
                        mhandler.sendEmptyMessage(DOWNLOAD_FAIL);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        InputStream is = null;
                        byte[] buf = new byte[1024];
                        int len = 0;
                        FileOutputStream fos = null;
                        int total_length = 0;
                        try {
                            is = response.body().byteStream();
                            long file_length = response.body().contentLength();
                            File file = new File(dir, getAPPName(url));
                            fos = new FileOutputStream(file);
                            while ((len = is.read(buf)) != -1) {
                                total_length += len;
                                int value = (int) ((total_length / (float) file_length) * 100);
//                                progressDialog.setProgress(value);
                                fos.write(buf, 0, len);
                            }
                            fos.flush();
                            mhandler.obtainMessage(DOWNLOAD_SUCCESS, file).sendToTarget();
                            //如果下载文件成功，第一个参数为文件的绝对路径
                        } catch (IOException e) {
                            mhandler.sendEmptyMessage(DOWNLOAD_ERROR);
                        } finally {
                            if (is != null) is.close();
                            if (fos != null) fos.close();

                        }
                    }
                });
            }
        }).start();


    }

    public static String getAPPName(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    /**
     * 显示提示框
     *
     * @param context
     * @param file
     */

    public static void showHintDialog(final Context context, final File file) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示");
        builder.setMessage("检测到有新版本");
        builder.setCancelable(true);
        builder.setPositiveButton("去更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                APPUtils.InstallAPK(context, file);
            }
        });

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (!activity.isFinishing()) {
                builder.show();
            }
        }
    }
}
