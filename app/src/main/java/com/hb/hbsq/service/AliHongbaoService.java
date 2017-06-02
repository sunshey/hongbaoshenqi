package com.hb.hbsq.service;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.app.PendingIntent;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.hb.hbsq.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import static android.R.string.no;

/**
 * Created by wanglin  on 2017/4/21 09:59.
 */

public class AliHongbaoService {

    private static final String ALI_VIEW_SELF_CH = "查看红包";
    private static final String ALI_VIEW_OTHERS_CH = "领取红包";

    private static final AliHongbaoService aliHongbaoService = new AliHongbaoService();
    private boolean isSelfPay;
    private boolean isDetail;

    private AliHongbaoService() {
    }

    public static AliHongbaoService getInstance() {

        return aliHongbaoService;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void setAliService(final AccessibilityService service, AccessibilityEvent event) {
        switch (event.getEventType()) {
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED://开关打开
                CharSequence classNameCs = event.getClassName();
                if (classNameCs == null) {
                    return;
                }
                String pubclassName = classNameCs.toString();
                LogUtil.msg("有2048事件   " + pubclassName);
                if (pubclassName.equals("android.widget.TextView") || pubclassName.equals("android.widget.ListView") || pubclassName.equals("android.widget.FrameLayout")) {
//                    openPackage(service);
                    getLastPacket(service);
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
//                                    if (getVipType(spSave4) || getVipType(spSave11)) {
//                                        WakeUtils wakeUtils = new WakeUtils(service);
//                                        wakeUtils.wakeAndUnlock(true);
//                                    }
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
                if ("com.alipay.mobile.chatapp.ui.GroupChatMsgActivity_".equals(cls)) {
                    getLastPacket(service);
//                    openPackage(service);
                } else if ("com.alipay.android.phone.discovery.envelope.get.SnsCouponDetailActivity".equals(cls)) {
                    openPackage(service);
                } else if ("com.alipay.android.phone.discovery.envelope.crowd.CrowdHostActivity".equals(cls)) {

                }
//                    try {
//                        Thread.sleep(2000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    checkDetailInfo(event);
//                    if (list.size() > 1) {
//                        String money = list.get(0);
//                        String nick = list.get(1);
//
//                        if (!TextUtils.isEmpty(money) && !TextUtils.isEmpty(nick)) {
//                            HongbaoAddEngin engin = new HongbaoAddEngin();
//                            try {
//                                engin.getInfo(UIUtils.getUid(), nick, Double.parseDouble(money), "qq", new Callback<HongbaoInfo>() {
//                                    @Override
//                                    public void onSuccess(ResultInfo<HongbaoInfo> resultInfo) {
//                                        LogUtil.msg(resultInfo.message);
//                                        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
//                                        list.clear();
//                                        isOpened = true;
//                                    }
//
//                                    @Override
//                                    public void onFailure(Response response) {
//
//                                    }
//                                });
//                            } catch (NumberFormatException e) {
//                            }
//                        }
//
//                    }
//                }
                break;
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void getLastPacket(AccessibilityService service) {
//        if (!isSelfPay) getChatList(service);
        AccessibilityNodeInfo rootNode = service.getRootInActiveWindow();
//        if (rootNode == null || rootNode.getChildCount() == 0) return;
        try {
            List<AccessibilityNodeInfo> nodeInfos = rootNode.findAccessibilityNodeInfosByViewId("com.alipay.mobile.chatapp:id/chat_msg_layout");

            if (nodeInfos == null || nodeInfos.size() == 0) return;
            for (AccessibilityNodeInfo nodeInfo : nodeInfos) {

                if (nodeInfo == null || nodeInfo.getParent() == null) continue;

                AccessibilityNodeInfo parent = nodeInfo.getParent().getParent();
//
//                for (int i = 0; i < parent.getChildCount(); i++) {

                    AccessibilityNodeInfo child = parent.getChild(0);
//                    if (child == null || child.getClassName() == null) continue;
                    if (child!= null && child.isClickable()) {
                        child.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
                    LogUtil.msg("rootNode  " + child.getClassName() + "----" + child.isClickable() + "----" + child.getContentDescription() + "----" + child.getText());
//                }
//
//                if (parent == null || parent.getChildCount() > 1) continue;
//                LogUtil.msg("rootNode  " + parent.getChildCount());


            }

        } catch (NoSuchMethodError error) {

        }


//        for (int i = 0; i < rootNode.getChildCount(); i++) {
//            AccessibilityNodeInfo child = rootNode.getChild(i);
//            if (child == null || child.getClassName() == null) continue;
//            if (child.getClassName().toString().equals("android.widget.ListView")) {
//                if (child.getChildCount() == 0) return;
//                for (int i1 = 0; i1 < child.getChildCount(); i1++) {
//                    AccessibilityNodeInfo child1 = child.getChild(i1);
//                    if (child1 == null || child1.getClassName() == null) return;
//
//                }
//
//
//            }
////            LogUtil.msg("rootNode  " + child.getClassName().toString());
//        }


        if (!isDetail) recycle(rootNode);


    }

    private Rect lastReceivieRect = new Rect();
    private Rect rect = new Rect();

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void recycle(AccessibilityNodeInfo info) {

        if (info == null) return;

        List<AccessibilityNodeInfo> receviedNodes = null;
        try {
            receviedNodes = info.findAccessibilityNodeInfosByViewId("com.alipay.mobile.chatapp:id/sys_msg_tv");
        } catch (NoSuchMethodError error) {
        }
        if (receviedNodes != null && receviedNodes.size() > 0) {
            AccessibilityNodeInfo lastReceivie = receviedNodes.get(receviedNodes.size() - 1);
            lastReceivie.getBoundsInScreen(lastReceivieRect);
        }
        try {
            List<AccessibilityNodeInfo> nodeInfos = findNodeInfosByTexts(info, new String[]{ALI_VIEW_OTHERS_CH, ALI_VIEW_SELF_CH});

            if (nodeInfos == null || nodeInfos.size() == 0) return;
            LogUtil.msg("className:  " + nodeInfos.size());

            nodeInfos.get(nodeInfos.size() - 1).getBoundsInScreen(rect);

            if (lastReceivieRect.top > rect.top) return;

            recycleList(nodeInfos.get(nodeInfos.size() - 1));
        } catch (Exception e) {

        } finally {
            info.recycle();
        }


    }

    /**
     * 是否在聊天列表界面
     */
    public void compare(AccessibilityService service) {
        AccessibilityNodeInfo info = service.getRootInActiveWindow();
        try {
            List<AccessibilityNodeInfo> selfInfos = info.findAccessibilityNodeInfosByText(ALI_VIEW_SELF_CH);
            info.recycle();
            isSelfPay = selfInfos != null && selfInfos.size() > 0;
        } catch (Exception e) {
            isSelfPay = false;
        }

    }


    private List<AccessibilityNodeInfo> findNodeInfosByTexts(AccessibilityNodeInfo nodeInfo, String[] texts) {

        for (String text : texts) {
            if (text == null) continue;
//            boolean isRobSelf = SPUtils.getBoolean1(this, SPConstant.ROB_SELF_KEY, true);//是否抢自己发的红包

//            if (text.equals(ALI_VIEW_SELF_CH)) continue;

            List<AccessibilityNodeInfo> nodes = nodeInfo.findAccessibilityNodeInfosByText(text);

            if (!nodes.isEmpty()) {

                return nodes;
            }
        }
        return new ArrayList<>();
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void getChatList(AccessibilityService service) {

        AccessibilityNodeInfo info = service.getRootInActiveWindow();

        if (info == null) return;
        try {
            List<AccessibilityNodeInfo> nodeInfos = info.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/afx");
            info.recycle();
            if (nodeInfos == null || nodeInfos.size() == 0) return;
//        LogUtil.msg("className: getChatList " + nodeInfos.size());
            for (AccessibilityNodeInfo nodeInfo : nodeInfos) {
                if (nodeInfo.getText() != null) {
                    String tip = nodeInfo.getText().toString();
//                    if (tip.contains(WECHAT_NOTIFICATION_TIP)) {
//                        recycleList(nodeInfo);
//                        break;
//                    }
                }
            }
        } catch (NoSuchMethodError error) {
        }

    }

    private void recycleList(AccessibilityNodeInfo info) {
        if (info == null) return;
        if (info.isClickable()) {
            info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            return;
        }
        info = info.getParent();
        recycleList(info);
    }

    private boolean openPackage(AccessibilityService service) {
        boolean isClick = false;
        AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();
        if (nodeInfo == null || nodeInfo.getChildCount() == 0) return false;
        for (int i = 0; i < nodeInfo.getChildCount(); i++) {
            if (nodeInfo.getChild(i) == null) continue;
            AccessibilityNodeInfo child = nodeInfo.getChild(i);
            CharSequence className = child.getClassName();
            if (className == null || child.getContentDescription() == null) continue;
            if (className.toString().equals("android.widget.ImageView") && child.getContentDescription().toString().equals("拆红包")) {
                isClick = true;
                nodeInfo.getChild(i).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                break;
            }
        }
        nodeInfo.recycle();
        return isClick;
    }
}
