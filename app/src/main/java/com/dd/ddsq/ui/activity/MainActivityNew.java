package com.dd.ddsq.ui.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.dd.ddsq.R;
import com.dd.ddsq.adapter.MainFunctionAdapterNew;
import com.dd.ddsq.bean.GoagalInfo;
import com.dd.ddsq.bean.LoginDataInfo;
import com.dd.ddsq.common.BaseActivity;
import com.dd.ddsq.config.SPConstant;
import com.dd.ddsq.engine.LoginEngin;
import com.dd.ddsq.eventbus.EventBean;
import com.dd.ddsq.listener.OnUpdateListener;
import com.dd.ddsq.service.HbService;
import com.dd.ddsq.ui.fragment.dialog.ExceptionalFragment;
import com.dd.ddsq.ui.fragment.dialog.OpAsFragment;
import com.dd.ddsq.ui.fragment.dialog.TwoButtonDialog;
import com.dd.ddsq.util.AppNotificationDownload;
import com.dd.ddsq.util.AppUpdateManager;
import com.dd.ddsq.util.Encrypt;
import com.dd.ddsq.util.LogUtil;
import com.dd.ddsq.util.NetUtils;
import com.dd.ddsq.util.SPUtils;
import com.dd.ddsq.util.ToastUtil;
import com.dd.ddsq.util.UIUtils;
import com.dd.ddsq.widget.AutoRollTextView;
import com.dd.ddsq.widget.CircleScanView;
import com.dd.ddsq.widget.FllowerAnimation;
import com.example.comm_recyclviewadapter.BaseItemDecoration;
import com.kk.pay.IPayImpl;
import com.kk.securityhttp.domain.ResultInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;


public class MainActivityNew extends BaseActivity {


    @BindView(R.id.iv_scan)
    CircleScanView ivScan;
    @BindView(R.id.iv_caishen)
    ImageView ivCaishen;
    @BindView(R.id.iv_vip)
    ImageView ivVip;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_notification)
    AutoRollTextView tvNotification;
    @BindView(R.id.iv_sound)
    ImageView ivSound;
    @BindView(R.id.rl_scan)
    RelativeLayout rlScan;
    @BindView(R.id.rl_main)
    RelativeLayout rlMain;
    @BindView(R.id.tv_state)
    TextView tvState;

    @BindView(R.id.rcv_main_function)
    RecyclerView rcvMainFunction;
    private OpAsFragment opAsFragment;
    private ExceptionalFragment exceptionalFragment;
    private StringBuilder sb = new StringBuilder();
    private boolean isVisable = true;
    private boolean isFirst = true;
    private boolean isFormVIPAd = false;
    private boolean isNotify;


    private void init() {
        rcvMainFunction.setLayoutManager(new GridLayoutManager(this, 4));
        String[] items = getResources().getStringArray(R.array.detail_function);
        List<String> mList = new ArrayList<>(Arrays.asList(items));
        MainFunctionAdapterNew adapter = new MainFunctionAdapterNew(this, mList);
        BaseItemDecoration decoration = new BaseItemDecoration(this);
        rcvMainFunction.setAdapter(adapter);
        rcvMainFunction.addItemDecoration(decoration);
    }

    @Override
    protected boolean isNeedTitle() {
        return false;
    }

    @Override
    protected void initData() {
        checkNet();
        checkVersionUpdate();

        setDynamicParams();
        init();
        EventBus.getDefault().register(this);
    }

    @Override
    protected int getResourseLayout() {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        return R.layout.activity_main_new;
    }

    @Override
    protected void initHeader(TextView tv_header_left) {

    }

    @OnClick({R.id.iv_vip, R.id.iv_caishen, R.id.tv_help, R.id.tv_record})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_vip://跳到打赏界面
                if (exceptionalFragment == null)
                    exceptionalFragment = new ExceptionalFragment();
                if (!exceptionalFragment.isVisible()) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.add(exceptionalFragment, null);
                    ft.commitAllowingStateLoss();
                }
                break;
            case R.id.iv_caishen://点击财神，扫描扇区出现
                if (HbService.isRunning()) {
                    open(isVisable);
                    isFormVIPAd = false;
                } else {
                    try {
                        startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                    } catch (ActivityNotFoundException e) {
                    }

                    ToastUtil.showToast(this, "点击[微信抢红包神器 ❤❤开启有效❤❤]开启服务", Toast.LENGTH_SHORT);
                }
                break;

            case R.id.tv_help:
                if (NetUtils.checekConnection(this)) {
                    startActivity(new Intent(this, HelpActivityNew.class));
                } else {
                    startActivity(new Intent(this, HelpActivity.class));
                }
                break;

            case R.id.tv_record:
                startActivity(new Intent(this, RecordActivty.class));
                break;
        }
    }

    private void open(boolean _isVisable) {
        ivScan.setVisibility(_isVisable ? View.GONE : View.VISIBLE);
        if (_isVisable) {
            ivScan.stopAnge();
        } else {
            ivScan.startAnge();
            ivScan.setStartAngle();
        }
        ToastUtil.showToast(this, _isVisable ? "抢红包服务已关闭!" : "抢红包服务已打开", Toast.LENGTH_SHORT);
        tvState.setText(_isVisable ? "抢红包服务已暂停" : "正在抢红包中....");
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = createNotification(_isVisable ? "抢红包服务已关闭" : "正在抢红包中", _isVisable ? "<<<点击打开>>>" : "<<<点击关闭>>>", !_isVisable);
        nm.notify(1, notification);
        HbService.startForeground(notification);
        isVisable = !_isVisable;
        SPUtils.put1(this, SPConstant.OPEN_HONGBAO_KEY, isVisable);//false
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (!TextUtils.isEmpty(intent.getStringExtra("success"))) {
            startFlower();
        }
        getNoty(intent);

    }

    private void getNoty(Intent intent) {
        if (intent.hasExtra("nft")) {
            isNotify = intent.getBooleanExtra("nft", false);
            isFormVIPAd = false;
        }
    }

    private void checkNet() {
        if (!NetUtils.checekConnection(this)) {
            ToastUtil.showToast(this, "网络没有连接，请设置网络，要不然可能错过抢红包哦！", Toast.LENGTH_SHORT);
        }
        String json = SPUtils.getString(this, SPConstant.GOAGAL_INFO_KEY);
        LoginDataInfo info = JSON.parseObject(Encrypt.decode(json), LoginDataInfo.class);
        if (info != null) {
            initData(info);
            isFirst = false;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateStatus();
        if (isFirst)
            getData();

        if (IPayImpl.uiPayCallback != null && IPayImpl.uOrderInfo != null && IPayImpl.isGen()) {
            IPayImpl.checkOrder(IPayImpl.uOrderInfo, IPayImpl.uiPayCallback);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (opAsFragment != null) {
            opAsFragment.dismiss();
        }
        ivScan.stopAnge();
        isFirst = true;
        isNotify = !SPUtils.getBoolean1(this, SPConstant.OPEN_HONGBAO_KEY, true);
        isFormVIPAd = false;
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        ivVip.setImageResource(R.drawable.money_drawable);

        AnimationDrawable ad = (AnimationDrawable) ivVip.getDrawable();

//        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//            ad.setOneShot(true);
//        }

        ivCaishen.setImageResource(R.drawable.caishen_drawable);
        AnimationDrawable caiShenAd = (AnimationDrawable) ivCaishen.getDrawable();

        if (hasFocus) {
            ad.start();
            caiShenAd.start();
        } else {
            ad.stop();
            caiShenAd.stop();
        }


    }


    private void updateStatus() {
        try {
            if (HbService.isRunning()) {
                if (!isFormVIPAd) {
                    open(isNotify);
                }
                if (opAsFragment != null) {
                    opAsFragment.dismiss();
                }
            } else {

                if (opAsFragment == null) {
                    opAsFragment = new OpAsFragment();
                }
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                if (!opAsFragment.isAdded()) {
                    ft.add(opAsFragment, null);
                }
                ft.commitAllowingStateLoss();
                ivScan.setVisibility(View.GONE);
                tvState.setText("抢红包服务已暂停");
            }
        } catch (Exception e) {
        }

    }


    private Notification createNotification(CharSequence tickerText, CharSequence contentText, boolean isclick) {
        int icon = R.mipmap.icon;

        Intent notificationIntent = new Intent(this, MainActivityNew.class); //点击该通知后要跳转的Activity
        notificationIntent.putExtra("nft", isclick);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(icon)
                .setContentTitle(tickerText)
                .setContentText(contentText)
                .setContentIntent(contentIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.flags |= Notification.FLAG_ONGOING_EVENT; //将此通知放到通知栏的"Ongoing"即"正在运行"组中
        notification.flags |= Notification.FLAG_NO_CLEAR; //表明在点击了通知栏中的"清除通知"后，此通知不清除，

        return notification;
    }


    private void playAnimation(int count) {
        ivSound.setVisibility(View.VISIBLE);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1.0f, 0, 1.0f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(1000);
        scaleAnimation.setRepeatMode(Animation.INFINITE);
        scaleAnimation.setRepeatCount(count);
        ivSound.setAnimation(scaleAnimation);
    }


    private void setNotice(List<String> noticeInfos) {
        if (noticeInfos != null && noticeInfos.size() > 0) {
            for (String noticeInfo : noticeInfos) {
                sb.append(noticeInfo + "     ");
            }
            tvNotification.setText(sb.toString());
            tvNotification.init(getWindowManager());
            tvNotification.startScroll();
            playAnimation(Integer.MAX_VALUE);
        }
    }

    private LoginEngin loginEngin;

    private void getData() {
        if (loginEngin == null) loginEngin = new LoginEngin();
        loginEngin.rxGetInfo().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<ResultInfo<LoginDataInfo>>() {
            @Override
            public void call(ResultInfo<LoginDataInfo> loginDataInfoResultInfo) {
                if (loginDataInfoResultInfo != null && loginDataInfoResultInfo.code == 1 && loginDataInfoResultInfo.data != null) {
                    GoagalInfo.loginDataInfo = loginDataInfoResultInfo.data;

                    if (GoagalInfo.loginDataInfo != null) {
                        initData(GoagalInfo.loginDataInfo);
                    }

                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(final Throwable throwable) {

                ToastUtil.showToast(MainActivityNew.this, throwable.getMessage(), Toast.LENGTH_SHORT);

            }
        });
    }


    /**
     * 提醒
     */
    private void tint(String countStr, String shareCounStr) {
        int count = 0, shareCount = 0;
        if (!TextUtils.isEmpty(countStr)) {
            count = Integer.parseInt(countStr);
        }
        if (!TextUtils.isEmpty(shareCounStr)) {
            shareCount = Integer.parseInt(shareCounStr);
        }

        if (UIUtils.synVipIds(this).isEmpty()) {
            TwoButtonDialog twoButtonDialog = new TwoButtonDialog();
            Bundle bundle = new Bundle();
            bundle.putInt("count", count);
            twoButtonDialog.setArguments(bundle);
            if (count > 10 && shareCount <= 0) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(twoButtonDialog, null);
                ft.commitAllowingStateLoss();
            }
        }

    }


    private void startFlower() {
        FllowerAnimation fllowerAnimation = new FllowerAnimation(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        fllowerAnimation.setLayoutParams(params);
        rlMain.addView(fllowerAnimation);
        fllowerAnimation.startAnimation();
    }

    private void initData(LoginDataInfo info) {
        if (info.getCountInfo() != null) {
            String count = info.getCountInfo().getCount();
            String vip_test_num = info.getVipInfo().getVip_test_num();
            tvCount.setText(count);
            tint(count, vip_test_num);
            if (!TextUtils.isEmpty(info.getCountInfo().getSum()))
                tvMoney.setText(info.getCountInfo().getSum());
        }
        setNotice(info.getHongbaoNoticeInfos());


    }


    private void checkVersionUpdate() {

        if (GoagalInfo.loginDataInfo != null && GoagalInfo.loginDataInfo.getUpdateInfo() != null) {
            File dir;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                dir = Environment.getExternalStorageDirectory();
            } else {
                dir = getFilesDir();
            }
            File file = new File(dir, AppUpdateManager.getAPPName(GoagalInfo.loginDataInfo.getUpdateInfo().getUrl()));
            if (file.exists()) {
                PackageManager packageManager = getPackageManager();
                PackageInfo packageInfo = packageManager.getPackageArchiveInfo(file.getAbsolutePath(), PackageManager.GET_ACTIVITIES);

                if (packageInfo != null && packageInfo.versionCode < GoagalInfo.loginDataInfo.getUpdateInfo().getCode()) {
                    update(dir);
                } else {
                    if (isNewVersion(file)) {
                        AppUpdateManager.showHintDialog(this, file);
                    }
                }

            } else {
                update(dir);
//                AppNotificationDownload.checkUpdate(this, GoagalInfo.loginDataInfo.getUpdateInfo().getUrl(), dir);
            }
        }
    }

    private void update(File dir) {
        AppUpdateManager.downloadFile(this, GoagalInfo.loginDataInfo.getUpdateInfo().getUrl(), dir, new OnUpdateListener() {
            @Override
            public void onSuccess(final File file) {
                if (isNewVersion(file)) {
                    AppUpdateManager.showHintDialog(MainActivityNew.this, file);
                }
            }

            @Override
            public void onFailure() {

            }
        });
    }

    private boolean isNewVersion(File file) {
        PackageManager packageManager = getPackageManager();

        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(file.getAbsolutePath(), PackageManager.GET_ACTIVITIES);

        if (packageInfo == null || GoagalInfo.packageInfo == null) {
            return false;
        }

        return packageInfo.versionCode > GoagalInfo.packageInfo.versionCode;
    }

    private void setDynamicParams() {
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int height = display.getHeight();
        int width = display.getWidth();
        LogUtil.msg("height:  " + height + "   width:  " + width);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rlScan.getLayoutParams();
        if (height == 1920) {
            layoutParams.topMargin = height / 20;
            rlScan.setLayoutParams(layoutParams);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStateEvent(EventBean bean) {
        isFormVIPAd = bean.isFormVIPAd();
        open(bean.isOpen());

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
