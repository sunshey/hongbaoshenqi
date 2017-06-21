package com.dd.ddsq.config;

import android.os.Environment;

/**
 * Created by admin on 2017/2/28.
 * <p>
 * app配置文件
 */

public class APPConfig {
    private static final boolean DEBUG = false;

    public static final String PATH = Environment.getExternalStorageDirectory().getPath();

    private static final String baseUrl = "http://u.wk990.com/api/";

    private static final String debugBaseUrl = "http://120.76.202.236:1980/api/";


    /**
     * 初始化接口
     */
    public static final String INIT_URL = getBaseUrl() + "index/init?app_id=2&agent_id=1";

    /**
     * 问答列表接口
     *
     * @return
     */
    public static final String QA_LIST_URL = getBaseUrl() + "index/qa_list?app_id=2";
    /**
     * 会员列表
     */
    public static final String VIP_LIST_URL = getBaseUrl() + "index/vip_list?app_id=2&agent_id=1";
    /**
     * 订单列表
     */
    public static final String ORDER_LIST_URL = getBaseUrl() + "index/order_list?app_id=2";
    /**
     * 支付接口
     */
    public static final String PAY_URL = getBaseUrl() + "index/pay?app_id=2";
    /**
     * 充值回调地址
     */
    public static final String NOTIFY_URL_URL = getBaseUrl() + "index/notify_url";
    /**
     * 红包添加
     */
    public static final String HONGBAO_ADD_URL = getBaseUrl() + "index/hongbao_add?app_id=2";
    /**
     * 红包列表
     */
    public static final String HONGBAO_LIS_URL = getBaseUrl() + "index/hongbao_list?app_id=2";
    /**
     * 退货，取消订单
     */
    public static final String ORDERS_CANCEL_URL = getBaseUrl() + "index/orders_cancel?app_id=2";
    /**
     * 订单验证，支付接口页面返回成功时调用此接口
     */
    public static final String ORDERS_CHECK_URL = getBaseUrl() + "index/orders_check?app_id=2";

    public static final String SHARE_URL = getBaseUrl() + "index/share?app_id=2";
    /**
     * 获取支付方式列表
     */
    public static final String PAYWAY_LIST_URL = getBaseUrl() + "index/payway_list?app_id=2";


    private static String getBaseUrl() {
        return (DEBUG ? debugBaseUrl : baseUrl);
    }
}
