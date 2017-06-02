package com.hb.hbsq.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hb.hbsq.R;
import com.hb.hbsq.common.BaseActivity;
import com.hb.hbsq.util.APPUtils;
import com.hb.hbsq.util.ToastUtil;

import butterknife.OnClick;

/**
 * Created by wanglin  on 2017/3/9 09:41.
 */

public class HelpActivity extends BaseActivity {


    @Override
    protected void initData() {

    }

    @Override
    protected int getResourseLayout() {
        return R.layout.activity_help;
    }

    @Override
    public void initHeader(TextView tv_header_left) {

    }
@OnClick({R.id.btn1,R.id.btn2,R.id.btn3,R.id.btn4})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn1:
               startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                break;
            case R.id.btn2:
                startActivity(new Intent(this, VIPActivity.class));
                break;
            case R.id.btn3:
                if (APPUtils.isInstallAPP(this, "com.tencent.mm")) {
                    Intent intent = new Intent();
                    ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");// 报名该有activity
                    intent.setAction(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setComponent(cmp);
                    startActivity(intent);
                } else {
                    ToastUtil.showToast(this, "您还没有安装微信，请先安装", Toast.LENGTH_SHORT);
                }
                break;
            case R.id.btn4:
                startActivity(new Intent(Settings.ACTION_SECURITY_SETTINGS));
                break;
        }
    }
}
