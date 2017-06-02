package com.kk.pay;

import android.app.Activity;

/**
 * Created by zhangkai on 2017/3/17.
 */

public abstract class IPayAbs {
    public static final boolean Debug = false;
    public static final float Debug_Price = 0.01f;

    protected IPayImpl aliPayImpl;
    protected IPayImpl wxiPayImpl;
    protected Activity mContext;


    public IPayAbs(Activity context, IPayImpl aliPayImpl, IPayImpl wxiPayImpl) {
        this.mContext = context;
        this.aliPayImpl = aliPayImpl;
        this.wxiPayImpl = wxiPayImpl;
    }

    public IPayAbs(Activity context, IPayImpl aliPayImpl) {
        this(context, aliPayImpl, null);
    }


    public abstract void wxpay(OrderParamsInfo orderParamsInfo,
                               IPayCallback
                                       callback);

    public abstract void alipay(OrderParamsInfo orderParamsInfo,
                                IPayCallback
                                        callback);

    public float debug(float price) {
        return Debug ? Debug_Price : price;
    }
}
