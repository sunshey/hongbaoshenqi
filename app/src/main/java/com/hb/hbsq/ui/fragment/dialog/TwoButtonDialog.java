package com.hb.hbsq.ui.fragment.dialog;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hb.hbsq.R;
import com.hb.hbsq.bean.PayWayInfo;
import com.hb.hbsq.common.BaseDialogFragment;
import com.hb.hbsq.ui.activity.VIPActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wanglin  on 2017/3/9 14:38.
 */

public class TwoButtonDialog extends BaseDialogFragment {

    @BindView(R.id.tv_tint)
    TextView tvTint;
    private int count;

    @Override
    protected void initData() {
        if (getArguments() != null)
            count = getArguments().getInt("count");
    }

    @Override
    protected void initTitle(TextView tvTitle, ImageView imageView) {
        tvTitle.setText("重要提示");

        if (count > 10) {
            tvTint.setText("您的免费使用次数已用完\n请及时充值或分享来增加您的使用次数\n以免影响您的使用！");
            imageView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_two_button;
    }

    @Override
    protected boolean isNeedTitle() {
        return true;
    }

    @OnClick({R.id.btn_open_vip, R.id.btn_share})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_open_vip:
                startActivity(new Intent(getActivity(), VIPActivity.class));
                break;
            case R.id.btn_share:
                SharePlatformFragment sharePlatformFragment = new SharePlatformFragment();
                sharePlatformFragment.show(getFragmentManager(), null);
                break;
        }
        dismiss();
    }

    @Override
    public void onPause() {
        super.onPause();
        dismiss();
    }
}
