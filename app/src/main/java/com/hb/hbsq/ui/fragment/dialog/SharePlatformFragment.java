package com.hb.hbsq.ui.fragment.dialog;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hb.hbsq.R;
import com.hb.hbsq.common.BaseDialogFragment;

import butterknife.OnClick;

import static com.hb.hbsq.R.id.iv;

/**
 * Created by admin on 2017/3/1.
 */

public class SharePlatformFragment extends BaseDialogFragment {


    @Override
    protected void initData() {

    }

    @Override
    protected void initTitle(TextView tvTitle, ImageView ivClose) {
        tvTitle.setText("分享到");
        ivClose.setVisibility(View.GONE);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_share_platform;
    }

    @Override
    protected boolean isNeedTitle() {
        return true;
    }


    @OnClick({R.id.ll_wx_chat, R.id.ll_wx_circle})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_wx_chat:
                if (listener != null) {
                    listener.onShreWX();
                }
                break;
            case R.id.ll_wx_circle:
                if (listener != null) {
                    listener.onShareCircle();
                }
                break;
        }
        dismiss();

    }


    private onShareListener listener;

    public onShareListener getListener() {
        return listener;
    }

    public void setListener(onShareListener listener) {
        this.listener = listener;
    }

    public interface onShareListener {
        void onShreWX();

        void onShareCircle();
    }
}
