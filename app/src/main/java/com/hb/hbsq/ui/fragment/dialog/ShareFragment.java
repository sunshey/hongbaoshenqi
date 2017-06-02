package com.hb.hbsq.ui.fragment.dialog;

import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hb.hbsq.R;
import com.hb.hbsq.common.BaseDialogFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by admin on 2017/2/24.
 */

public class ShareFragment extends BaseDialogFragment {


    @BindView(R.id.tv_share_link)
    TextView tvShareLink;


    @Override
    protected void initData() {

    }

    @Override
    protected void initTitle(TextView tvTitle, ImageView ivClose) {
        tvTitle.setText("分享好友");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_share;
    }

    @Override
    protected boolean isNeedTitle() {
        return true;
    }

    @OnClick({R.id.iv_close, R.id.tv_share_link, R.id.btn_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.tv_share_link://复制到剪贴板
                ClipboardManager manager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                manager.setText(tvShareLink.getText());
                Toast.makeText(getContext(), "分享链接已复制", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_share://分享
//                Intent shareInt = new Intent(Intent.ACTION_SEND);
//                shareInt.setType("text/plain");
//                shareInt.putExtra(Intent.EXTRA_SUBJECT, "分享到");
//                shareInt.putExtra(Intent.EXTRA_TITLE, "分享到");
//
//                shareInt.putExtra(Intent.EXTRA_TEXT, "【最专业的抢红包神器】\n" +
//                        "http://t.iqr.cc/uMb2Uj\n" +
//                        "点击链接，选择在浏览器打开");
//                shareInt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(shareInt);

                SharePlatformFragment platformFragment = new SharePlatformFragment();
                platformFragment.show(getFragmentManager(), null);
                dismiss();
                break;
        }
    }


}
