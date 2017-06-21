package com.dd.ddsq.common;

import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dd.ddsq.bean.PayWayInfo;
import com.dd.ddsq.bean.PaywayListInfo;
import com.dd.ddsq.config.SPConstant;
import com.dd.ddsq.engine.PaywayListEngin;
import com.dd.ddsq.util.Encrypt;
import com.dd.ddsq.util.SPUtils;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2017/4/28 10:45.
 */

public abstract class BasePayDialogFragment extends BaseDialogFragment {

    private List<PayWayInfo> listInfos = new ArrayList<>();
    private PaywayListEngin paywayListEngin;
    private List<PayWayInfo> lastPayInfoList = new ArrayList<>();

    @Override
    protected abstract int getLayoutId();

    @Override
    protected void initData() {
        paywayListEngin = new PaywayListEngin();
        getPaywayList();
        init();
    }


    @Override
    protected abstract void initTitle(TextView tvTitle, ImageView ivClose);


    @Override
    protected abstract boolean isNeedTitle();

    private void init() {
        String vipitem = Encrypt.decode(SPUtils.getString1(getActivity(), SPConstant.PAYWAY_LIST_KEY));
        List<PayWayInfo> payWayInfoList = JSONObject.parseArray(vipitem, PayWayInfo.class);
        if (payWayInfoList != null) {
            listInfos = payWayInfoList;
        }
        initView();
    }

    private void initView() {

        if (listInfos.size() > 0) {
            lastPayInfoList.clear();
            for (PayWayInfo listInfo : listInfos) {
                String title = listInfo.getTitle();
                if ("支付宝支付".equals(title) || "微信支付".equals(title)) {
                    lastPayInfoList.add(listInfo);
                }
            }
//            lastPayInfoList.clear();
//            lastPayInfoList.add(new PayWayInfo(PayConfig.PAY_WAY_WXPAY, "微信支付"));
//            lastPayInfoList.add(new PayWayInfo(PayConfig.PAY_WAY_WXPAY, "微信支付"));
//            lastPayInfoList.add(new PayWayInfo(PayConfig.PAY_WAY_XXPAY, "微信支付"));
//            lastPayInfoList.add(new PayWayInfo(PayConfig.PAY_WAY_NOWPAY_WX, "微信支付"));
//            lastPayInfoList.add(new PayWayInfo(PayConfig.PAY_WAY_NOWPAY_ALI, "支付宝支付"));
            filtrate(lastPayInfoList);
            if (lastPayInfoList.size() == 1) {
                PayWayInfo payWayInfo = lastPayInfoList.get(0);
                setPaywayList(payWayInfo);
            } else if (lastPayInfoList.size() >= 2) {
                for (int i = 0; i < 2; i++) {
                    setPaywayList(lastPayInfoList.get(i));
                }

            }
        }
    }


    protected abstract void setPaywayList(PayWayInfo payWayInfo);

    protected abstract void initUI();

    private void filtrate(List<PayWayInfo> paywayInfos) {
        boolean flag = false;
        if (paywayInfos.size() < 2) {
            return;
        }
        PayWayInfo info1 = paywayInfos.get(0);
        PayWayInfo info2 = paywayInfos.get(1);
        if (info1.getTitle().equals(info2.getTitle())) {
            flag = true;
        }
        if (flag) {
            paywayInfos.remove(info2);
            filtrate(paywayInfos);
        }
    }


    private void getPaywayList() {

        paywayListEngin.rxGetInfo().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<ResultInfo<PaywayListInfo>>() {
            @Override
            public void call(ResultInfo<PaywayListInfo> resultInfo) {
                if (resultInfo != null && resultInfo.code == HttpConfig.STATUS_OK && resultInfo.data != null) {
                    listInfos = resultInfo.data.getPayWayInfos();

                    String json = JSON.toJSONString(resultInfo.data.getPayWayInfos());
                    if (getContext() != null) {
                        SPUtils.put1(getContext(), SPConstant.PAYWAY_LIST_KEY, Encrypt.encode(json));
                    }
                    initView();
                } else {
                    initUI();
                }
            }
        });
    }

}
