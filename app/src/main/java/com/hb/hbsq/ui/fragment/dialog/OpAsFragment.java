package com.hb.hbsq.ui.fragment.dialog;


import android.content.Intent;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hb.hbsq.R;
import com.hb.hbsq.common.BaseDialogFragment;
import com.hb.hbsq.util.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by admin on 2017/2/23.
 * 打开服务弹窗fragment
 */

public class OpAsFragment extends BaseDialogFragment {

    @Override
    protected int getLayoutId() {
        getDialog().setCanceledOnTouchOutside(true);
        return R.layout.fragment_opas;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initTitle(TextView tvTitle, ImageView ivClose) {
        tvTitle.setText("请打开辅助服务");
    }


    @Override
    protected boolean isNeedTitle() {
        return true;
    }


    @OnClick({R.id.iv_close, R.id.btn_open_as, R.id.tv_content})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.btn_open_as:
            case R.id.tv_content:
                try {
                    startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));

                } catch (Exception e) {
                }

                ToastUtil.showToast(getActivity(), "点击[乐乐红包神器 ❤❤开启有效❤❤]开启服务", Toast.LENGTH_SHORT);
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isVisible())
            dismiss();
    }
}
