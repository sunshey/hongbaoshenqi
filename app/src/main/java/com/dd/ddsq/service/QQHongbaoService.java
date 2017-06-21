package com.dd.ddsq.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.dd.ddsq.bean.HongbaoInfo;
import com.dd.ddsq.config.SPConstant;
import com.dd.ddsq.engine.HongbaoAddEngin;
import com.dd.ddsq.util.FPUitl;
import com.dd.ddsq.util.LogUtil;
import com.dd.ddsq.util.UIUtils;
import com.dd.ddsq.util.WakeUtils;
import com.kk.securityhttp.domain.ResultInfo;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2017/3/24 08:11.
 */

public class QQHongbaoService {


    /**
     * qq抢红包
     */
    private static final String WECHAT_OPEN_EN = "Open";
    private static final String WECHAT_OPENED_EN = "You've opened";
    private final static String QQ_DEFAULT_CLICK_OPEN = "点击拆开";
//    private final static String QQ_DEFAULT_HAVE_OPENED = "已拆开";

    private final static String QQ_HONG_BAO_PASSWORD = "口令红包";
    private final static String QQ_PACKAGE = "[QQ红包]";
    private final static String QQ_CLICK_TO_PASTE_PASSWORD = "点击输入口令";
    private final static String QQ_CHECK_DETAIL = "查看领取详情";
    private static final String QQ_RESIDUE_MONEY = "已存入余额";
    private static final String SPLASH_UI = "com.tencent.mobileqq.activity.SplashActivity";
    private static final String QWALLET_UI = "cooperation.qwallet.plugin.QWalletPluginProxyActivity";//开红包详情页
    private static final String PAYBRIDGE_UI = "com.tencent.mobileqq.activity.PayBridgeActivity";//低版本详情页

    private boolean mLuckyMoneyReceived;
    private String lastFetchedHongbaoId = null;
    private long lastFetchedTime = 0;
    private static final int MAX_CACHE_TOLERANCE = 5000;

    private AccessibilityNodeInfo rootNodeInfo;
    private List<AccessibilityNodeInfo> mReceiveNode;
    private List<String> list = new ArrayList<>();

    private boolean isOpened;

    private static QQHongbaoService qqHaobaoService = new QQHongbaoService();

    private QQHongbaoService() {

    }

    public static QQHongbaoService getInstance() {
        return qqHaobaoService;
    }

    private boolean getVipType(String str) {
        boolean spSaveState = false;
        if (str.equals("true")) {
            spSaveState = true;
        }
        return spSaveState;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    protected void setQqService(final AccessibilityService service, AccessibilityEvent event) {
        this.rootNodeInfo = service.getRootInActiveWindow();

        mReceiveNode = null;
        String spSave14 = FPUitl.get(service, SPConstant.VIP_OPEN_ITEM_KEY + 14, "false");//自动回复
        String spSave11 = FPUitl.get(service, SPConstant.VIP_OPEN_ITEM_KEY + 11, "false");//息屏
        String spSave4 = FPUitl.get(service, SPConstant.VIP_OPEN_ITEM_KEY + 4, "false");//SVIP

        switch (event.getEventType()) {
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED://开关打开
                CharSequence classNameCs = event.getClassName();
                if (classNameCs == null) {
                    return;
                }
                String pubclassName = classNameCs.toString();
                LogUtil.msg("有2048事件   " + pubclassName);
                if (pubclassName.equals("android.widget.TextView") || pubclassName.equals("android.widget.AbsListView") || pubclassName.equals("android.widget.FrameLayout")) {
                    openPackage(service);

                }
                break;
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED://当通知栏发生改变时
                List<CharSequence> texts = event.getText();
                LogUtil.msg("通知栏事件:   " + texts);
                if (!texts.isEmpty()) {
                    for (CharSequence text : texts) {
                        String content = text.toString();
                        if (content.contains("[QQ红包]")) {
                            if (event.getParcelableData() != null &&
                                    event.getParcelableData() instanceof Notification) {
                                Notification notification = (Notification) event.getParcelableData();
                                PendingIntent pendingIntent = notification.contentIntent;
                                try {
                                    if (getVipType(spSave4) || getVipType(spSave11)) {
                                        WakeUtils wakeUtils = new WakeUtils(service);
                                        wakeUtils.wakeAndUnlock(true);
                                    }
                                    pendingIntent.send();
                                    LogUtil.msg("进入QQ" + event.getClassName().toString());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                CharSequence className = event.getClassName();
                if (className == null) {
                    return;
                }
                String cls = className.toString();
                LogUtil.msg("className:   " + cls);
                if (SPLASH_UI.equals(cls)) {
                    if (isOpened) {
                        if (getVipType(spSave4) || getVipType(spSave14)) {
                            if (service instanceof HbService) {
                                HbService hbService = (HbService) service;
                                if (hbService.fill()) {
                                    hbService.send();
                                }
                            }
                        }
                        isOpened = false;
                    }
                    openPackage(service);
                } else if (QWALLET_UI.equals(cls) || PAYBRIDGE_UI.equals(cls)) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    checkDetailInfo(event);
                    if (list.size() > 1) {
                        String money = list.get(0);
                        String nick = list.get(1);

                        if (!TextUtils.isEmpty(money) && !TextUtils.isEmpty(nick)) {
                            HongbaoAddEngin engin = new HongbaoAddEngin();
                            try {
                                engin.rxGetInfo(UIUtils.getUid(service), nick, Double.parseDouble(money), "qq").observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<ResultInfo<HongbaoInfo>>() {
                                    @Override
                                    public void call(ResultInfo<HongbaoInfo> resultInfo) {
                                        if (resultInfo != null && resultInfo.message != null) LogUtil.msg("resultInfo   " + resultInfo.message);
                                        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                                        list.clear();
                                        isOpened = true;
                                    }
                                });

                            } catch (NumberFormatException e) {
                            }
                        }

                    }
                }
                break;
        }
    }

    private void openPackage(AccessibilityService service) {
        checkNodeInfo();
        /* 如果已经接收到红包并且还没有戳开 */
        if (mLuckyMoneyReceived && (mReceiveNode != null)) {
            int size = mReceiveNode.size();
            if (size > 0) {

                String id = getHongbaoText(mReceiveNode.get(size - 1));

                long now = System.currentTimeMillis();

                if (this.shouldReturn(id, now - lastFetchedTime))
                    return;

                lastFetchedHongbaoId = id;
                lastFetchedTime = now;

                AccessibilityNodeInfo cellNode = mReceiveNode.get(size - 1);
                if (cellNode == null || cellNode.getText() == null) return;


                if (cellNode.getText().toString().equals("口令红包已拆开")) {
                    return;
                }

                cellNode.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);


                if (cellNode.getText().toString().equals(QQ_HONG_BAO_PASSWORD)) {

                    AccessibilityNodeInfo rowNode = service.getRootInActiveWindow();
                    if (rowNode == null) {
                        LogUtil.msg("noteInfo is　null");
                        return;
                    } else {
                        recycleQQ(rowNode);
                    }
                }

                mLuckyMoneyReceived = false;

            }
        }
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void recycleQQ(AccessibilityNodeInfo info) {
        if (info.getChildCount() == 0) {

            if (info.getText() != null && info.getText().toString().equals(QQ_CLICK_TO_PASTE_PASSWORD)) {

                info.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
            if (info.getClassName() == null || info.getText() == null) return;

            if (info.getClassName().toString().equals("android.widget.Button") && info.getText().toString().equals("发送")) {
                info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }

        } else {
            for (int i = 0; i < info.getChildCount(); i++) {
                if (info.getChild(i) != null) {
                    recycleQQ(info.getChild(i));
                }
            }
        }
    }

    /**
     * 检查聊天窗口节点信息
     */
    private void checkNodeInfo() {
        if (rootNodeInfo == null) {
            return;
        }
         /* 聊天会话窗口，遍历节点匹配“点击拆开”，“口令红包”，“点击输入口令” */
        List<AccessibilityNodeInfo> nodes1 = this.findAccessibilityNodeInfosByTexts(this.rootNodeInfo, new String[]{
                QQ_DEFAULT_CLICK_OPEN, QQ_HONG_BAO_PASSWORD, QQ_CLICK_TO_PASTE_PASSWORD, "发送"});

        if (!nodes1.isEmpty()) {
            String nodeId = Integer.toHexString(System.identityHashCode(this.rootNodeInfo));

            if (!nodeId.equals(lastFetchedHongbaoId)) {
                mLuckyMoneyReceived = true;
                mReceiveNode = nodes1;
            }
        }

    }


    private void checkDetailInfo(AccessibilityEvent event) {
        try {
            AccessibilityNodeInfo info = event.getSource();
            if (info == null) return;

            List<AccessibilityNodeInfo> nodeInfos = info.findAccessibilityNodeInfosByText(QQ_CHECK_DETAIL);
            if (nodeInfos.isEmpty()) return;

            AccessibilityNodeInfo info1 = nodeInfos.get(0).getParent();
            if (info1 == null) return;

            AccessibilityNodeInfo rootInfo = info1.getParent();
            if (rootInfo == null) return;

            List<AccessibilityNodeInfo> nodeInfos1 = rootInfo.findAccessibilityNodeInfosByText(QQ_RESIDUE_MONEY);
            if (nodeInfos1.isEmpty()) return;
            AccessibilityNodeInfo info2 = nodeInfos1.get(0).getParent();//余额
            if (info2 == null) return;
            for (int i = 0; i < info2.getChildCount(); i++) {
                AccessibilityNodeInfo childMoney = info2.getChild(2);
                AccessibilityNodeInfo childNick = info2.getChild(5);
                if (childMoney != null && childMoney.getClassName() != null && childMoney.getClassName().toString().equals("android.widget.TextView")) {
                    String money = childMoney.getText().toString();
                    list.add(money);
                }
                if (childNick != null && childNick.getClassName() != null && childNick.getClassName().toString().equals("android.widget.TextView")) {
                    String nick = childNick.getText().toString().replaceAll("的红包", "");
                    list.add(nick);
                }
                if (list.size() >= 2) {
                    break;
                }
            }

        } catch (Exception e) {
        }


    }

    /**
     * 将节点对象的id和红包上的内容合并
     * 用于表示一个唯一的红包
     *
     * @param node 任意对象
     * @return 红包标识字符串
     */
    private String getHongbaoText(AccessibilityNodeInfo node) {

        /* 获取红包上的文本 */
        String content;
        try {

            AccessibilityNodeInfo i = node.getParent().getChild(0);
            content = i.getText().toString();
            LogUtil.msg("content:   " + content + "   className  " + i.getClassName().toString());
        } catch (Exception npe) {
            return null;
        }

        return content;
    }


    /**
     * 判断是否返回,减少点击次数
     * 现在的策略是当红包文本和缓存不一致时,戳
     * 文本一致且间隔大于MAX_CACHE_TOLERANCE时,戳
     *
     * @param id       红包id
     * @param duration 红包到达与缓存的间隔
     * @return 是否应该返回
     */
    private boolean shouldReturn(String id, long duration) {
        // ID为空
        if (id == null) return true;

        // 名称和缓存不一致
        return duration < MAX_CACHE_TOLERANCE && id.equals(lastFetchedHongbaoId);

    }

    /**
     * 批量化执行AccessibilityNodeInfo.findAccessibilityNodeInfosByText(text).
     * 由于这个操作影响性能,将所有需要匹配的文字一起处理,尽早返回
     *
     * @param nodeInfo 窗口根节点
     * @param texts    需要匹配的字符串们
     * @return 匹配到的节点数组
     */
    private List<AccessibilityNodeInfo> findAccessibilityNodeInfosByTexts(AccessibilityNodeInfo nodeInfo, String[] texts) {
        for (String text : texts) {
            if (text == null) continue;

            List<AccessibilityNodeInfo> nodes = nodeInfo.findAccessibilityNodeInfosByText(text);

            if (!nodes.isEmpty()) {
                if (text.equals(WECHAT_OPEN_EN) && !nodeInfo.findAccessibilityNodeInfosByText(WECHAT_OPENED_EN).isEmpty()) {
                    continue;
                }
                return nodes;
            }
        }
        return new ArrayList<>();
    }
}
