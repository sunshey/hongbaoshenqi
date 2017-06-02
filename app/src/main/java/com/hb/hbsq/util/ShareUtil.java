package com.hb.hbsq.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.widget.Toast;

import com.hb.hbsq.bean.GameImage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangkai on 16/10/21.
 */
public class ShareUtil {
    public static void openWXShareWithImage(final Context ctx, final String content, final List<GameImage> images, final int
            type) {
        if (!APPUtils.isInstallAPP(ctx, "com.tencent.mm")) {
            ToastUtil.showToast(ctx, "请安装微信", Toast.LENGTH_SHORT);
            return;
        }

//        LoadingUtil.show(ctx, "分享初始化...");
        new AsyncTask<Void, Void, ArrayList<Uri>>() {
            @Override
            protected ArrayList<Uri> doInBackground(Void... arg0) {
                // TODO Auto-generated method stub
                ArrayList<Uri> list = new ArrayList<Uri>();
                if (images == null) {
                    return list;
                }
                for (GameImage gameImage : images) {
                    try {
                        Bitmap bitmap = Picasso.with(ctx).load(gameImage.getImgUri()).get();
                        list.add(getImageUri(ctx, bitmap));
                        LogUtil.msg("获取分享图片->" + gameImage.getImgUri());
                    } catch (Exception e) {
                    }
                }
                return list;
            }

            protected void onPostExecute(ArrayList<Uri> uris) {
                String activityName = "com.tencent.mm.ui.tools.ShareImgUI";
                if (type == 1) {
                    activityName = "com.tencent.mm.ui.tools.ShareToTimeLineUI";
                }
                Intent intent = new Intent(Intent.ACTION_SEND); // 地址
                ComponentName componentName = new ComponentName(
                        "com.tencent.mm", activityName
                );
                intent.setComponent(componentName);
                if (uris.size() > 0) {
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                    intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
                }
                intent.putExtra("Kdescription", content);
                ctx.startActivity(Intent.createChooser(intent, "分享"));
//                LoadingUtil.dismiss();
            }

        }.execute();
    }

    public static void openWXShareWithImage(final Context ctx, final String content, final String url, final int
            type) {
        if (!APPUtils.isInstallAPP(ctx, "com.tencent.mm")) {
            ToastUtil.showToast(ctx, "请安装微信", Toast.LENGTH_SHORT);
            return;
        }
//        LoadingUtil.show(ctx, "分享初始化...");
        new AsyncTask<Void, Void, Uri>() {
            @Override
            protected Uri doInBackground(Void... arg0) {
                // TODO Auto-generated method stub
                Uri uri = null;
                try {
                    try {
                        Bitmap bitmap = Picasso.with(ctx).load(url).transform(new Transformation() {
                            @Override
                            public Bitmap transform(Bitmap source) {
                                Bitmap inImage = BitMapUtil.zoomBitmap(source, UIUtils.dip2px(ctx, 30), UIUtils.dip2px(ctx, 30));
                                Bitmap circleBitmap = BitMapUtil.circleBitmap(inImage);
                                source.recycle();
                                return circleBitmap;
                            }

                            @Override
                            public String key() {
                                return "";
                            }
                        }).get();

                        uri = getImageUri(ctx, bitmap);
                        LogUtil.msg("获取分享图片->" + url);
                    } catch (Exception e) {
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return uri;
            }

            protected void onPostExecute(Uri uri) {

                if (uri == null) return;
                String activityName = "com.tencent.mm.ui.tools.ShareImgUI";
                if (type == 1) {
                    activityName = "com.tencent.mm.ui.tools.ShareToTimeLineUI";
                }
                Intent intent = new Intent(Intent.ACTION_SEND); // 地址
                ComponentName componentName = new ComponentName(
                        "com.tencent.mm", activityName
                );
                intent.setComponent(componentName);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.putExtra("Kdescription", content);
                intent.setType("image/*");
                ctx.startActivity(Intent.createChooser(intent, "分享"));
//                LoadingUtil.dismiss();
            }

        }.execute();
    }

    private static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static void OpenWxShareText(final Context ctx, String title, String content, int type) {
        if (!APPUtils.isInstallAPP(ctx, "com.tencent.mm")) {
            ToastUtil.showToast(ctx, "请安装微信", Toast.LENGTH_SHORT);
            return;
        }
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.putExtra(Intent.EXTRA_TITLE, title);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String activityName = "com.tencent.mm.ui.tools.ShareImgUI";
        if (type == 1) {
            activityName = "com.tencent.mm.ui.tools.ShareToTimeLineUI";
        }
        intent.setComponent(new ComponentName("com.tencent.mm", activityName));
        ctx.startActivity(intent);
    }

}
