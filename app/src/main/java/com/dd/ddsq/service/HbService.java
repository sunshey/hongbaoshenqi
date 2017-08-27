package com.dd.ddsq.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.accessibilityservice.GestureDescription;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.dd.ddsq.bean.HongbaoInfo;
import com.dd.ddsq.config.SPConstant;
import com.dd.ddsq.engine.HongbaoAddEngin;
import com.dd.ddsq.ui.activity.MainActivity;
import com.dd.ddsq.util.FPUitl;
import com.dd.ddsq.util.LogUtil;
import com.dd.ddsq.util.SPUtils;
import com.dd.ddsq.util.ToastUtil;
import com.dd.ddsq.util.UIUtils;
import com.dd.ddsq.util.WakeUtils;
import com.kk.securityhttp.domain.ResultInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by admin on 2017/2/23.
 */

public class HbService extends AccessibilityService {


    private String lastMAIN;
    private boolean isBack = false;//抢完红包后是否回到主聊天界面
    private String replay = "谢谢您的大红包";//自动回复内容

    private boolean isOpen;//是否打开过开红包的界面

    private Handler mHandler = new Handler();
    private long delayTime;//延迟时间

    private String wechatNickname;//个人微信昵称

    private boolean isWX = false;


    private boolean isDetail = false;

    private static HbService service;

    private static final String WECHAT_VIEW_SELF_CH = "查看红包";
    private static final String WECHAT_VIEW_OTHERS_CH = "领取红包";
    private static final String WECHAT_NOTIFICATION_TIP = "[微信红包]";
    private static final String RECEIVE_UI = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI";
    private static final String DETAIL_UI = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI";
    private static final String LAUNCHER_UI = "com.tencent.mm.ui.LauncherUI";
    private boolean isSelfPay;

    private String[] nameList = new String[]{"com.tencent.mm:id/bfs", "com.tencent.mm:id/beg", "com.tencent.mm:id/bie"};
    private String[] moneyList = new String[]{"com.tencent.mm:id/bfw", "com.tencent.mm:id/bek", "com.tencent.mm:id/bii"};
    private String[] wechatList = new String[]{"com.tencent.mm:id/buq", "com.tencent.mm:id/bt5", "com.tencent.mm:id/by2"};

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getPackageName() == null) return;
        String packageName = event.getPackageName().toString();

        boolean isOpenRob = SPUtils.getBoolean1(this, SPConstant.OPEN_HONGBAO_KEY, true);//是否开启抢红包
        if (!isOpenRob) return;

        if (SPUtils.getBoolean1(this, SPConstant.ROB_WX, true) && packageName.equals("com.tencent.mm")) {
            if (thread == null) {
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                Thread.sleep(8 * 1000);

                                isDetail = false;

                            } catch (InterruptedException e) {

                            }
                        }
                    }
                });
                thread.start();
            }
            setService(event);
        }
        if (SPUtils.getBoolean1(this, SPConstant.ROB_QQ, true) && packageName.equals("com.tencent.mobileqq")) {
            QQHongbaoService service = QQHongbaoService.getInstance();
            service.setQqService(this, event);
        }
        if (packageName.equals("com.eg.android.AlipayGphone")) {//支付宝红包
            AliHongbaoService service = AliHongbaoService.getInstance();
//            service.setAliService(this, event);
        }
    }


    private boolean getVipType(String str) {
        boolean spSaveState = false;
        if (str.equals("true")) {
            spSaveState = true;
        }
        return spSaveState;
    }

    @Override
    public void onInterrupt() {
        ToastUtil.showToast(this, "中断抢红包服务", Toast.LENGTH_SHORT);
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        service = this;
        //发送广播，已经连接上了
        Intent intent = new Intent(Config.ACTION_QIANGHONGBAO_SERVICE_CONNECT);
        sendBroadcast(intent);
        Toast.makeText(this, "已连接抢红包服务", Toast.LENGTH_SHORT).show();
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void setService(AccessibilityEvent event) {
        int eventType = event.getEventType();

        String spSave15 = FPUitl.get(this, SPConstant.VIP_OPEN_ITEM_KEY + 15, "false");//防封号
        String spSave14 = FPUitl.get(this, SPConstant.VIP_OPEN_ITEM_KEY + 14, "false");//自动回复
        String spSave11 = FPUitl.get(this, SPConstant.VIP_OPEN_ITEM_KEY + 11, "false");//息屏
        String spSave4 = FPUitl.get(this, SPConstant.VIP_OPEN_ITEM_KEY + 4, "false");//SVIP
        String spSave5 = FPUitl.get(this, SPConstant.VIP_OPEN_ITEM_KEY + 5, "false");//抢加速
        String spSave8 = FPUitl.get(this, SPConstant.VIP_OPEN_ITEM_KEY + 8, "false");//提高概率


        boolean isBackChat = SPUtils.getBoolean1(this, SPConstant.SELF_BACK_CHAT_KEY, true);//是否自动返回聊天页
        CharSequence classNameCs = event.getClassName();
        if (classNameCs == null) {
            return;
        }
        String pubclassName = classNameCs.toString();
        LogUtil.msg("有2048事件   " + pubclassName);

        switch (eventType) {

            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED://开关打开

                if (pubclassName.equals("android.widget.TextView") || pubclassName.equals("android.widget.ListView")) {
                    compare(event);

                    getLastPacket(event);

                } else if (pubclassName.equals("com.tencent.mm.ui.mogic.WxViewPager")) {
                    isDetail = false;
                }
                break;
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED://当通知栏发生改变时
                List<CharSequence> texts = event.getText();
                LogUtil.msg("通知栏事件:   " + texts);
                if (!texts.isEmpty()) {
                    for (CharSequence text : texts) {
                        String content = text.toString();
                        if (content.contains(WECHAT_NOTIFICATION_TIP)) {
                            if (event.getParcelableData() != null &&
                                    event.getParcelableData() instanceof Notification) {
                                Notification notification = (Notification) event.getParcelableData();
                                PendingIntent pendingIntent = notification.contentIntent;
                                try {
                                    if (getVipType(spSave4) || getVipType(spSave11)) {
                                        WakeUtils wakeUtils = new WakeUtils(this);
                                        wakeUtils.wakeAndUnlock(true);
                                    }
                                    pendingIntent.send();
                                    isWX = true;
                                    isDetail = false;

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
                break;
            //当窗口的状态发生改变时
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:

                if (pubclassName.equals(LAUNCHER_UI)) {
                    compare(event);
                    //点击最后一个红包
                    getLastPacket(event);

                    if (isBack && isOpen) {
                        LogUtil.msg("回到聊天界面");
                        if (getVipType(spSave4) || getVipType(spSave14)) {
                            if (fill()) {
                                isDetail = true;
                                send();
                            }
                        }
                        isBack = false;
                        isOpen = false;
                    }
                    if (TextUtils.isEmpty(wechatNickname)) {
                        wechatNickname = getNameOrMoney(event, wechatList);
//                            LogUtil.msg("name:  " + wechatNickname);
                    }

                } else if (pubclassName.equals(RECEIVE_UI) || pubclassName.equals("com.tencent.mm.plugin.luckymoney.ui.En_fba4b94f")) {
                    isDetail = true;
                    //开红包
                    LogUtil.msg("开红包");
                    if (!getVipType(spSave15)) {
                        if (getVipType(spSave5) || getVipType(spSave4)) {
                            delayTime = 0;
                        } else {
                            delayTime = 2000;

                        }
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (android.os.Build.VERSION.SDK_INT <= 23) {

                                    if (openPackage()) {
                                        isOpen = true;
                                    }
                                } else {
                                    if (specialHandle()) {
                                        isOpen = true;
                                    }
                                }
                            }
                        }, delayTime);
                    }
                } else if (pubclassName.equals(DETAIL_UI)) {
                    //红包详情
                    isDetail = true;
//                    LogUtil.msg("红包详情" + "   " + isOpen);
                    String name = getNameOrMoney(event, nameList);//姓名

                    String money = getNameOrMoney(event, moneyList);//金额

//                    LogUtil.msg("name:  " + name + "  money:   " + money);
                    if (isOpen) {
                        performGlobalAction(GLOBAL_ACTION_BACK);
                        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(money)) {
                            HongbaoAddEngin engin = new HongbaoAddEngin();
                            try {
                                engin.rxGetInfo(UIUtils.getUid(this), name, Double.parseDouble(money), "wx")
                                        .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<ResultInfo<HongbaoInfo>>() {
                                    @Override
                                    public void call(ResultInfo<HongbaoInfo> resultInfo) {
                                        if (resultInfo != null && resultInfo.message != null)
                                            LogUtil.msg("resultInfo  " + resultInfo.message);

                                    }
                                });
                            } catch (NumberFormatException e) {
                            }

                        }

                        if (isWX && !isBackChat) {
                            Intent intent = new Intent(this, MainActivity.class);
                            if (getVipType(spSave8) || getVipType(spSave4)) {
                                if (!TextUtils.isEmpty(wechatNickname)) {
                                    if (wechatNickname.equals(getRecieveMostName())) {//自己获得人气王
                                        ToastUtil.showPrettyToast(this, "恭喜您通过本软件成为抢到最大红包，成为运气王");
                                        intent.putExtra("success", "success");
                                    }
                                }
                            }
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            isOpen = false;
                            isWX = false;
                        } else {
                            if (getVipType(spSave8) || getVipType(spSave4)) {
                                if (!TextUtils.isEmpty(wechatNickname)) {
                                    if (wechatNickname.equals(getRecieveMostName())) {//自己获得人气王
                                        ToastUtil.showPrettyToast(this, "恭喜您通过本软件成为抢到最大红包，成为运气王");
                                    }
                                }
                            }
                        }
                        isBack = true;
                    }
                } else {
                    lastMAIN = pubclassName;
                }
                break;
        }

    }


    /**
     * 获取发红包人的微信昵称和金额
     *
     * @param idList
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private String getNameOrMoney(AccessibilityEvent event, String[] idList) {

        AccessibilityNodeInfo nodeInfo = event.getSource();
        if (nodeInfo != null) {
            try {
                List<AccessibilityNodeInfo> textList = null;
                for (String s : idList) {
                    textList = nodeInfo.findAccessibilityNodeInfosByViewId(s);
                    if (textList != null && textList.size() > 0) {
                        break;
                    }
                }
                nodeInfo.recycle();
                if (textList == null || textList.size() == 0) return "";
                for (AccessibilityNodeInfo info : textList) {
                    if (info.getText() != null)
                        return info.getText().toString();
                }

            } catch (NoSuchMethodError e) {

            }

        }
        return null;
    }

    /**
     * 获取群发红包的人气王
     *
     * @param
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private String getRecieveMostName() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {

            try {
                List<AccessibilityNodeInfo> textIsBestList = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/bjt");
                if (textIsBestList == null) {
                    textIsBestList = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/bic");
                }
                nodeInfo.recycle();
                if (textIsBestList != null && textIsBestList.size() > 0) {
                    AccessibilityNodeInfo parent = textIsBestList.get(0).getParent();
                    if (parent != null) {
                        List<AccessibilityNodeInfo> textNameList = parent.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/bjn");
                        if (textNameList == null) {
                            textNameList = parent.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/bi7");
                        }
                        for (AccessibilityNodeInfo info : textNameList) {
                            if (info.getText() != null)
                                return info.getText().toString();
                        }
                    }
                }
            } catch (NoSuchMethodError e) {

            }

        }
        return null;
    }


    private boolean openPackage() {
        boolean isClick = false;
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo == null || nodeInfo.getChildCount() == 0) return false;
        for (int i = 0; i < nodeInfo.getChildCount(); i++) {
            if (nodeInfo.getChild(i) == null) continue;

            CharSequence className = nodeInfo.getChild(i).getClassName();
            if (className == null) continue;
            if (className.toString().equals("android.widget.Button")) {
                isClick = true;
                nodeInfo.getChild(i).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                break;
            }
        }
        nodeInfo.recycle();
        return isClick;
    }

    /**
     * 寻找窗体中的“发送”按钮，并且点击。
     */
    @SuppressLint("NewApi")
    public void send() {
        isDetail = true;
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            List<AccessibilityNodeInfo> list = nodeInfo
                    .findAccessibilityNodeInfosByText("发送");
            if (list != null && list.size() > 0) {
                for (AccessibilityNodeInfo n : list) {
                    if (n.getClassName().equals("android.widget.Button") && n.isEnabled()) {
                        n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
                }

            } else {
                List<AccessibilityNodeInfo> liste = nodeInfo
                        .findAccessibilityNodeInfosByText("Send");
                if (liste != null && liste.size() > 0) {
                    for (AccessibilityNodeInfo n : liste) {
                        if (n.getClassName().equals("android.widget.Button") && n.isEnabled()) {
                            n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("NewApi")
    public boolean fill() {
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        if (rootNode != null) {
            return findEditText(rootNode, replay);
        }
        return false;
    }

    //查找输入框
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private boolean findEditText(AccessibilityNodeInfo rootNode, String content) {
        int count = rootNode.getChildCount();
        for (int i = 0; i < count; i++) {
            AccessibilityNodeInfo nodeInfo = rootNode.getChild(i);
            if (nodeInfo == null) {
                continue;
            }
            if ("android.widget.EditText".equals(nodeInfo.getClassName())) {
                Bundle arguments = new Bundle();
                arguments.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT,
                        AccessibilityNodeInfo.MOVEMENT_GRANULARITY_WORD);
                arguments.putBoolean(AccessibilityNodeInfo.ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN, true);
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY, arguments);
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
                ClipData clip = ClipData.newPlainText("label", content);
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setPrimaryClip(clip);
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_PASTE);
                return true;
            }
            if (findEditText(nodeInfo, content)) {
                return true;
            }
        }
        return false;
    }

    private static Thread thread;


    /**
     * 是否在聊天列表界面
     */
    private void compare(AccessibilityEvent event) {
//        AccessibilityNodeInfo info = getRootInActiveWindow();
        AccessibilityNodeInfo info = event.getSource();
        try {
            List<AccessibilityNodeInfo> selfInfos = info.findAccessibilityNodeInfosByText(WECHAT_VIEW_SELF_CH);
            info.recycle();
            isSelfPay = selfInfos != null && selfInfos.size() > 0;

        } catch (Exception e) {
            isSelfPay = false;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void getLastPacket(AccessibilityEvent event) {

        AccessibilityNodeInfo rootNode = getRootInActiveWindow();

        if (!isSelfPay) getChatList();

//        if (!isDetail)
        recycle(rootNode);

    }

    private Rect lastReceivieRect = new Rect();
    private Rect rect = new Rect();
    private Rect currentRect = new Rect();


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void recycle(AccessibilityNodeInfo info) {

        if (info == null) return;

        List<AccessibilityNodeInfo> receviedNodes = null;
        try {
            receviedNodes = info.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/if");
            if (receviedNodes == null || receviedNodes.size() == 0) {
                receviedNodes = info.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/im");
            }
        } catch (NoSuchMethodError error) {
        }
        if (receviedNodes != null && receviedNodes.size() > 0) {
            AccessibilityNodeInfo lastReceivie = receviedNodes.get(receviedNodes.size() - 1);
            lastReceivie.getBoundsInScreen(lastReceivieRect);
        }

        try {
            List<AccessibilityNodeInfo> nodeInfos = findNodeInfosByTexts(info, new String[]{WECHAT_VIEW_OTHERS_CH, WECHAT_VIEW_SELF_CH});

            if (nodeInfos == null || nodeInfos.size() == 0) return;

            currentRect.set(rect);
            nodeInfos.get(nodeInfos.size() - 1).getBoundsInScreen(rect);


            if (lastReceivieRect.top > rect.top || currentRect.top == rect.top) return;

            recycleList(nodeInfos.get(nodeInfos.size() - 1));
        } catch (Exception e) {

        } finally {
            info.recycle();
        }


    }


    private List<AccessibilityNodeInfo> findNodeInfosByTexts(AccessibilityNodeInfo nodeInfo, String[] texts) {

        for (String text : texts) {
            if (text == null) continue;
            boolean isRobSelf = SPUtils.getBoolean1(this, SPConstant.ROB_SELF_KEY, true);//是否抢自己发的红包

            if ((text.equals(WECHAT_VIEW_SELF_CH) && !isRobSelf)) continue;

            List<AccessibilityNodeInfo> nodes = nodeInfo.findAccessibilityNodeInfosByText(text);

            if (!nodes.isEmpty()) {

                return nodes;
            }
        }
        return new ArrayList<>();
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void getChatList() {

        AccessibilityNodeInfo info = getRootInActiveWindow();

        if (info == null) return;
        try {
            List<AccessibilityNodeInfo> nodeInfos = info.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/afx");
            if (nodeInfos == null || nodeInfos.size() == 0) {
                nodeInfos = info.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/agy");
            }

            info.recycle();
            if (nodeInfos == null || nodeInfos.size() == 0) return;

            for (AccessibilityNodeInfo nodeInfo : nodeInfos) {
                if (nodeInfo != null && nodeInfo.getText() != null) {
                    String tip = nodeInfo.getText().toString();
                    if (tip.contains(WECHAT_NOTIFICATION_TIP) && !isDetail) {
                        recycleList(nodeInfo);
                        isDetail = true;
                        break;
                    }
                }
            }
        } catch (NoSuchMethodError error) {
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void recycleList(AccessibilityNodeInfo info) {
        if (info == null) return;
        if (info.isClickable()) {

            info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            return;
        }
        info = info.getParent();
        recycleList(info);


    }

    /**
     * 判断当前服务是否正在运行
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean isRunning() {
        if (service == null) {
            return false;
        }
        AccessibilityManager accessibilityManager = (AccessibilityManager) service.getSystemService(Context.ACCESSIBILITY_SERVICE);
        AccessibilityServiceInfo info = service.getServiceInfo();

        if (info == null) {
            return false;
        }

        List<AccessibilityServiceInfo> list = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        Iterator<AccessibilityServiceInfo> iterator = list.iterator();

        boolean isConnect = false;
        while (iterator.hasNext()) {
            AccessibilityServiceInfo i = iterator.next();
            if (i.getId().equals(info.getId())) {
                isConnect = true;
                break;
            }
        }
        return isConnect;
    }

    private boolean isNOpen = false;

    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean specialHandle() {

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float dpi = metrics.density;

        Path path = new Path();
        if (640 == dpi) {
            path.moveTo(720, 1575);
        } else {
            path.moveTo(540, 1060);
        }

        GestureDescription.Builder builder = new GestureDescription.Builder();
        GestureDescription gestureDescription = builder.addStroke(new GestureDescription.StrokeDescription(path, 450, 50)).build();
        if (gestureDescription == null) return false;
        dispatchGesture(gestureDescription, new GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                Log.d("preferences", "onCompleted");
                isNOpen = true;
                super.onCompleted(gestureDescription);

            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                Log.d("preferences", "onCancelled");
                isNOpen = false;
                super.onCancelled(gestureDescription);
            }
        }, null);

        return isNOpen;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //发送广播，已经断开辅助服务
        Intent intent = new Intent(Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT);
        sendBroadcast(intent);

    }


    public static void startForeground(Notification notification) {
        service.startForeground(1, notification);
    }
}
