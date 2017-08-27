package com.kk.pay.other;

import android.app.DialogFragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.kk.pay.IPayCallback;
import com.kk.pay.IPayImpl;
import com.kk.pay.OrderInfo;
import com.kk.pay.R;
import com.kk.pay.XjInfo;
import com.squareup.picasso.Picasso;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by wanglin  on 2017/6/1 11:51.
 */

public class PayCodeFragmentNew extends DialogFragment {

    private Bitmap bitmap;
    private OrderInfo oderInfo;
    private IPayCallback ipaycallBack;
    private boolean ispay;
    private TextView tvLink;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        View view = inflater.inflate(R.layout.dialog_wxpay_new, null);
        initView(view);

        return view;

    }


    private void initView(final View view) {
        ImageView iv = (ImageView) view.findViewById(R.id.iv_pay_code);
        tvLink = (TextView) view.findViewById(R.id.tv_link);
        if (getArguments() != null) {
            XjInfo xjInfo = (XjInfo) getArguments().getSerializable("xjInfo");
            String code_img_url = xjInfo.getCode_img_url();
            Picasso.with(getActivity()).load(code_img_url).into(iv);
            tvLink.setText(xjInfo.getCode_url());
            try {
                Bitmap bitmap = QRCodeEncoder.encodeAsBitmap(xjInfo.getCode_url(), ScreenUtil.dip2px(getActivity(), 250), ScreenUtil.dip2px(getActivity(), 250));
                setBitmap(bitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            }

        }

        view.findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


//        iv.setImageBitmap(bitmap);
        showSave();
//        MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "", "");

        iv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                pay();
                return true;
            }
        });
        tvLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyLink();

            }

        });

        view.findViewById(R.id.btn_save_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyLink();
                pay();
            }
        });

    }


    public void copyLink() {

        ClipboardManager manager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
//        manager.setText(tvLink.getText());
        ClipData clipData = ClipData.newPlainText(tvLink.getText(), tvLink.getText());
        manager.setPrimaryClip(clipData);
        Toast.makeText(getActivity(), "支付链接已复制", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        IPayImpl.checkOrder(getOderInfo(), getIpaycallBack());
    }

    private void pay() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI"));
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
//        ToastUtil.toast3(getActivity(), "打开微信扫一扫，在页面右上角选择相册中的二维码");
        ispay = true;

    }


    @Override
    public void onResume() {
        super.onResume();
        if (ispay) {
            dismiss();
        }
    }

    public OrderInfo getOderInfo() {
        return oderInfo;
    }

    public void setOderInfo(OrderInfo oderInfo) {
        this.oderInfo = oderInfo;
    }

    public IPayCallback getIpaycallBack() {
        return ipaycallBack;
    }

    public void setIpaycallBack(IPayCallback ipaycallBack) {
        this.ipaycallBack = ipaycallBack;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    private void showSave() {

        new Thread(saveFileRunnable).start();
    }

    /**
     * 保存照片
     *
     * @param bm
     * @param fileName
     * @throws IOException
     */
    private final static String ALBUM_PATH = Environment.getExternalStorageDirectory() + "/hbsq/";
    private String mSaveMessage;
    private String fileName = "android.jpg";

    public void saveFile(Bitmap bm) throws IOException {
        File dirFile = new File(ALBUM_PATH);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(ALBUM_PATH + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);

        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(myCaptureFile);
        intent.setData(uri);
        getActivity().sendBroadcast(intent);
        bos.flush();
        bos.close();
    }

    private Runnable saveFileRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                saveFile(getBitmap());
                mSaveMessage = "图片保存成功！";
            } catch (Exception e) {
                mSaveMessage = "图片保存失败！";
                e.printStackTrace();
            }
        }

    };


}
