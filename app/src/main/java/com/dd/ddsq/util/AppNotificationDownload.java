package com.dd.ddsq.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.dd.ddsq.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by wanglin  on 2017/6/21 16:19.
 * 通过notification下载更新
 */

public class AppNotificationDownload {

    private static OkHttpClient mOkHttpClient = new OkHttpClient();

    public static void checkUpdate(final Context context, final String url, final File dir) {
        final NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context).setSmallIcon(R.mipmap.icon)
                .setContentTitle("发现新版本")
                .setContentText("正在下载，请稍候...");
        Observable.just(url).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                final Request request = new Request.Builder()
                        .url(s)
                        .build();
                Call call = mOkHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

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
                                builder.setProgress(100, value, false);
                                nm.notify(1, builder.build());
                                fos.write(buf, 0, len);
                            }

                            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            builder.setProgress(0, 0, false)
                                    .setContentText("下载完成")
                                    .setContentIntent(pendingIntent);
                            nm.notify(1, builder.build());
                            context.startActivity(intent);

                            fos.flush();

                            //如果下载文件成功，第一个参数为文件的绝对路径
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (is != null) is.close();
                            if (fos != null) fos.close();

                        }
                    }
                });
            }
        });

    }

    private static String getAPPName(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }
}
