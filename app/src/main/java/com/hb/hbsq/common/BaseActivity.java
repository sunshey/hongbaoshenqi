package com.hb.hbsq.common;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;


import com.hb.hbsq.R;
import com.hb.hbsq.bean.GoagalInfo;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by admin on 2017/2/24.
 */

public abstract class BaseActivity extends AppCompatActivity {
    @BindView(R.id.tv_header_left)
    TextView tvHeaderLeft;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResourseLayout());
        ButterKnife.bind(this);
        init();
    }


    private void init() {
        initHeader(tvHeaderLeft);
        initData();

    }


    /**
     * 初始化数据
     */
    protected abstract void initData();

    protected abstract int getResourseLayout();

    /**
     * 初始化标题
     *
     * @param tv_header_left
     */
    protected abstract void initHeader(TextView tv_header_left);

    @OnClick(R.id.ll_back)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
