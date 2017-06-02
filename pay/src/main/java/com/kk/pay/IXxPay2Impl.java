package com.kk.pay;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.google.zxing.WriterException;
import com.kk.pay.other.LoadingDialog;
import com.kk.pay.other.PayCodeFragment;
import com.kk.pay.other.QRCodeEncoder;
import com.kk.pay.other.ScreenUtil;
import com.kk.pay.other.ToastUtil;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.switfpass.pay.utils.MD5;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by zhangkai on 2017/5/31.
 */

public class IXxPay2Impl extends IPayImpl {
    private static final String API_ORDER = "http://api2.xiaoxiaopay.com:7500/order";//下单获取支付参数


    private final static String channelId = "10000786";

    private static String key = "vr0bdFxq2VZuW5B";

    public IXxPay2Impl(Activity context) {
        super(context);
        loadingDialog = new LoadingDialog(context);
        isGen = true;
    }

    @Override
    public void pay(final OrderInfo orderInfo, final IPayCallback iPayCallback) {
        if (orderInfo == null || orderInfo.getPayInfo() == null) {
            ToastUtil.toast2(mContext, "支付失败");
            return;
        }
        PayInfo payInfo = orderInfo.getPayInfo();
        key = get(payInfo.getKey(), key);
        final int merchantID = Integer.parseInt(get(payInfo.getMerchantID(), channelId));
        BigDecimal price = new BigDecimal(orderInfo.getMoney() + "");
        final XxOrder order = new XxOrder(orderInfo.getOrder_sn(), payInfo.getIp(), merchantID, payInfo.getNotify_url(),
                10001,
                price,
                payInfo.getReturn_url(),
                orderInfo.getName());

        final HashMap<String, String> params = new HashMap<>();
        String json = new JSONObject(order.toMap()).toString();
        params.put("transdata", URLEncoder.encode(json));
        params.put("sign", MD5.md5s(order.toString() + "&key=" + key));
        params.put("signtype", "MD5");
        HttpCoreEngin.get().rxpost(API_ORDER, SignInfo.class, params, false, false, false)
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<SignInfo>() {
            @Override
            public void call(SignInfo signInfo) {
                if (signInfo != null && signInfo.getResultCode() == 20000) {

                    try {
                        Bitmap bitmap = QRCodeEncoder.encodeAsBitmap(signInfo.getInfo().getPayurl(), ScreenUtil.dip2px(mContext, 250), ScreenUtil.dip2px(mContext, 250));

                        PayCodeFragment payCodeFragment = new PayCodeFragment();
                        payCodeFragment.setBitmap(bitmap);
                        payCodeFragment.setOderInfo(orderInfo);
                        payCodeFragment.setIpaycallBack(iPayCallback);
                        payCodeFragment.show(mContext.getFragmentManager(), null);

                    } catch (WriterException e) {
                        e.printStackTrace();
                    }

                } else {
                    orderInfo.setMessage("支付失败");
                }
            }
        });

    }

    class XxOrder {
        private String cporderid;
        private String ip;
        private int merchantID;
        private String notifyurl;
        private int paytype;
        private BigDecimal price;
        private String returnurl;
        private String waresname;

        public XxOrder() {
        }

        /**
         * @param cporderid
         * @param ip
         * @param merchantID
         * @param notifyurl
         * @param paytype
         * @param price
         * @param returnurl
         * @param waresname
         */
        public XxOrder(String cporderid, String ip, int merchantID, String notifyurl, int paytype, BigDecimal price,
                       String returnurl, String waresname) {
            this.cporderid = cporderid;
            this.ip = ip;
            this.merchantID = merchantID;
            this.notifyurl = notifyurl;
            this.paytype = paytype;
            this.price = price.setScale(2, BigDecimal.ROUND_HALF_UP);
            this.returnurl = returnurl;
            this.waresname = waresname;
        }

        public XxOrder(String cporderid, String ip, int merchantID, String notifyurl, int paytype, String price,
                       String returnurl, String waresname) {
            this(cporderid, ip, merchantID, notifyurl, paytype, new BigDecimal(price), returnurl, waresname);
        }

        /**
         * 由于Java float的特性，并不建议使用该构造方法
         * {@link #XxOrder(String, String, int, String, int, BigDecimal, String, String)}
         */
        @Deprecated
        public XxOrder(String cporderid, String ip, int merchantID, String notifyurl, int paytype, float price,
                       String returnurl, String waresname) {
            this(cporderid, ip, merchantID, notifyurl, paytype, new BigDecimal(price), returnurl, waresname);
        }

        /**
         * 由于Java double的特性，并不建议使用该构造方法
         * {@link #XxOrder(String, String, int, String, int, BigDecimal, String, String)}
         */
        @Deprecated
        public XxOrder(String cporderid, String ip, int merchantID, String notifyurl, int paytype, double price,
                       String returnurl, String waresname) {
            this(cporderid, ip, merchantID, notifyurl, paytype, new BigDecimal(price), returnurl, waresname);
        }

        /**
         * @return the cporderid
         */
        public String getCporderid() {
            return cporderid;
        }

        /**
         * @param cporderid the cporderid to set
         */
        public void setCporderid(String cporderid) {
            this.cporderid = cporderid;
        }

        /**
         * @return the ip
         */
        public String getIp() {
            return ip;
        }

        /**
         * @param ip the ip to set
         */
        public void setIp(String ip) {
            this.ip = ip;
        }

        /**
         * @return the merchantID
         */
        public int getMerchantID() {
            return merchantID;
        }

        /**
         * @param merchantID the merchantID to set
         */
        public void setMerchantID(int merchantID) {
            this.merchantID = merchantID;
        }

        /**
         * @return the notifyurl
         */
        public String getNotifyurl() {
            return notifyurl;
        }

        /**
         * @param notifyurl the notifyurl to set
         */
        public void setNotifyurl(String notifyurl) {
            this.notifyurl = notifyurl;
        }

        /**
         * @return the paytype
         */
        public int getPaytype() {
            return paytype;
        }

        /**
         * @param paytype the paytype to set
         */
        public void setPaytype(int paytype) {
            this.paytype = paytype;
        }

        /**
         * @return the price
         */
        public BigDecimal getPrice() {
            return price;
        }

        /**
         * @param price the price to set
         */
        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        /**
         * @return the returnurl
         */
        public String getReturnurl() {
            return returnurl;
        }

        /**
         * @param returnurl the returnurl to set
         */
        public void setReturnurl(String returnurl) {
            this.returnurl = returnurl;
        }

        /**
         * @return the waresname
         */
        public String getWaresname() {
            return waresname;
        }

        /**
         * @param waresname the waresname to set
         */
        public void setWaresname(String waresname) {
            this.waresname = waresname;
        }


        public TreeMap<String, Object> toMap() {
            TreeMap<String, Object> treeMap = new TreeMap<>();
            treeMap.put("merchantID", merchantID);
            treeMap.put("waresname", waresname);
            treeMap.put("cporderid", cporderid);
            treeMap.put("price", price);
            treeMap.put("returnurl", returnurl);
            treeMap.put("notifyurl", notifyurl);
            treeMap.put("paytype", paytype);
            treeMap.put("ip", ip);
            return treeMap;
        }

        @Override
        public final String toString() {
            return createParams(toMap());
        }

        private String createParams(TreeMap<String, Object> params) {
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                Object value = entry.getValue();
                if (value instanceof BigDecimal)
                    builder.append(entry.getKey()).append("=")
                            .append(((BigDecimal) value).setScale(2, BigDecimal.ROUND_HALF_UP).toString()).append("&");
                else if (!isEmpty(value))
                    builder.append(entry.getKey()).append("=").append(value.toString()).append("&");
            }
            return builder.deleteCharAt(builder.length() - 1).toString();
        }

        private boolean isEmpty(Object object) {
            return object == null || object.equals("");
        }
    }

    private onPayCodeListener listener;

    public onPayCodeListener getListener() {
        return listener;
    }

    public void setListener(onPayCodeListener listener) {
        this.listener = listener;
    }

    public interface onPayCodeListener {
        void onPaycode(SignInfo signInfo);
    }

}
