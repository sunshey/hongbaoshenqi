package com.hb.hbsq.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hb.hbsq.R;
import com.hb.hbsq.bean.GoagalInfo;
import com.hb.hbsq.bean.LoginDataInfo;
import com.hb.hbsq.bean.VipInfo;
import com.hb.hbsq.config.SPConstant;
import com.hb.hbsq.engine.LoginEngin;

import com.hb.hbsq.util.Encrypt;
import com.hb.hbsq.util.SPUtils;
import com.hb.hbsq.util.ToastUtil;
import com.hb.hbsq.util.UIUtils;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;


/**
 * Created by admin on 2017/2/28.
 * 启动页
 */

public class LaunchActivity extends AppCompatActivity {
    @BindView(R.id.iv)
    ImageView iv;

    private Handler handler = new Handler();
    private VipInfo vipInfo;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        ButterKnife.bind(this);

        initData();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                switchActivity();
            }
        }, 2000);
    }

    private void initData() {
        LoginEngin loginEngin = new LoginEngin();

        loginEngin.rxGetInfo().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<ResultInfo<LoginDataInfo>>() {


            @Override
            public void call(ResultInfo<LoginDataInfo> resultInfo) {
                if (resultInfo != null && resultInfo.code == HttpConfig.STATUS_OK && resultInfo.data != null) {
                    GoagalInfo.loginDataInfo = resultInfo.data;
                    vipInfo = GoagalInfo.loginDataInfo.getVipInfo();

                    vipInfo.addObserver(new UIUtils());
                    vipInfo.setDataChanged();
                    vipInfo.notifyObservers(vipInfo.getUid());

                    String jsonString = JSON.toJSONString(GoagalInfo.loginDataInfo);
                    SPUtils.put(LaunchActivity.this, SPConstant.GOAGAL_INFO_KEY, Encrypt.encode(jsonString));
                } else {
                    ToastUtil.showToast(LaunchActivity.this, HttpConfig.NET_ERROR, Toast.LENGTH_SHORT);
                }
            }
        });

    }

    private void switchActivity() {
        startActivity(new Intent(LaunchActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (vipInfo != null)
            vipInfo.deleteObservers();
    }
}
