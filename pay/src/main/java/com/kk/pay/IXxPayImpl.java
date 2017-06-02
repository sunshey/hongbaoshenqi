package com.kk.pay;

import android.app.Activity;

import com.kk.pay.other.ToastUtil;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.securityhttp.security.Md5;
import com.payfeel.tokenapp.XxPayAPI;
import com.payfeel.tokenapp.XxPayAPIFactory;
import com.payfeel.tokenapp.XxPayResult;
import com.switfpass.pay.utils.MD5;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by zhangkai on 2017/5/4.
 */

public class IXxPayImpl extends IPayImpl {

    private XxPayAPI xxPayAPI;
    private final static String channelId = "10000786";
    private static String key = "vr0bdFxq2VZuW5B";
    private static final String API_APPSIGN = "http://api2.xiaoxiaopay.com:7500/appSign"; //获取App支付所需签名
    private static final String API_ORDER = "http://api2.xiaoxiaopay.com:7500/order";//下单获取支付参数

    public IXxPayImpl(Activity context) {
        super(context);

    }


    /**
     * 这部建议放置在服务器上进行操作，以保障数据安全
     */
    private String sign(String cporderid) {
        String content = "cporderid=" + cporderid + "&merchantID=" + channelId;
        return Md5.md5(content + "&key=" + key);
    }

    @Override
    public void pay(final OrderInfo orderInfo, final IPayCallback iPayCallback) {
        if (orderInfo == null || orderInfo.getPayInfo() == null) {
            ToastUtil.toast2(mContext, "支付失败");
            return;
        }
        PayInfo payInfo = orderInfo.getPayInfo();
        key = get(payInfo.getKey(), key);
        int merchantID = Integer.parseInt(get(payInfo.getMerchantID(), channelId));
        xxPayAPI = XxPayAPIFactory.createXxPayAPI(merchantID);
        BigDecimal price = new BigDecimal(orderInfo.getMoney() + "");

        final XxOrder order = new XxOrder(orderInfo.getOrder_sn(), payInfo.getIp(), merchantID, payInfo.getNotify_url(),
                10004,
                price,
                payInfo.getReturn_url(),
                orderInfo.getName());

        HashMap<String, String> params = new HashMap<>();
        params.put("transdata", new JSONObject(order.toMap()).toString());
        params.put("sign", MD5.md5s(order.toString() + "&key=" + key));

        HttpCoreEngin.get().rxpost(API_APPSIGN, SignInfo.class, params, false, false, false).flatMap(new
                                                                                                             Func1<SignInfo, Observable<HashMap>>() {
                                                                                                                 @Override
                                                                                                                 public Observable<HashMap> call(SignInfo signInfo) {
                                                                                                                     order.setIp(null);
                                                                                                                     if (signInfo == null || signInfo.getInfo() == null) {
                                                                                                                         orderInfo.setMessage("签名错误");
                                                                                                                         iPayCallback.onFailure(orderInfo);
                                                                                                                         return Observable.empty();
                                                                                                                     }
                                                                                                                     String sign = signInfo.getInfo().getSignValue().replace(" ", "+");
                                                                                                                     HashMap<String, String> params = new HashMap<>();
                                                                                                                     params.put("transdata", new JSONObject(order.toMap()).toString());
                                                                                                                     params.put("sign", sign);
                                                                                                                     params.put("signtype", "RSA");
                                                                                                                     return HttpCoreEngin.get().rxpost(API_ORDER, SignInfo.class, params, false, false, false);
                                                                                                                 }
                                                                                                             })
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<SignInfo>() {
            @Override
            public void call(SignInfo signInfo) {
                if (signInfo != null && signInfo.getResultCode() == 20000) {
                    xxSendPay(signInfo.getInfo().getPayurl(), orderInfo, iPayCallback);
                } else {
                    orderInfo.setMessage("支付失败");
                    iPayCallback.onFailure(orderInfo);
                }
            }
        });
    }

    private void xxSendPay(String payurl, final OrderInfo orderInfo, final IPayCallback iPayCallback) {
        xxPayAPI.sendPay(mContext, payurl, orderInfo.getOrder_sn(), sign(orderInfo.getOrder_sn()), new XxPayResult() {
            @Override
            public void result(int i, String s) {
                if (i == 20000) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        JSONObject info = jsonObject.optJSONObject("info");
                        int result = info.optInt("result");
                        if (result == 1) {
                            //TODO 已支付
                            orderInfo.setMessage("支付成功");
                            iPayCallback.onSuccess(orderInfo);
                            checkOrder(orderInfo);
                        } else {
                            //TODO 未支付
                            orderInfo.setMessage("支付取消");
                            iPayCallback.onFailure(orderInfo);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        orderInfo.setMessage("支付异常【" + e + "】");
                        iPayCallback.onFailure(orderInfo);
                    }
                } else {
                    //TODO 其他失败
                    orderInfo.setMessage("支付取消");
                    iPayCallback.onFailure(orderInfo);
                }
            }
        });
    }


    private void checkOrder(final OrderInfo orderInfo) {
        CheckOrderEngin checkOrderEngin = new CheckOrderEngin();
        checkOrderEngin.rxGetInfo(orderInfo).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<ResultInfo<ResultInfo>>() {
            @Override
            public void call(ResultInfo<ResultInfo> resultInfo) {
                if (resultInfo != null && resultInfo.code == HttpConfig.STATUS_OK) {
                    return;
                }
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkOrder(orderInfo);
                    }
                }, 5000);
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

}
