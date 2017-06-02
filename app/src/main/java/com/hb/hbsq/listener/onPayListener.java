package com.hb.hbsq.listener;

import com.kk.pay.IPayAbs;

/**
 * Created by wanglin  on 2017/3/30 10:27.
 */

public interface onPayListener {

    void onPayAli(IPayAbs iPayAbs,String payway, String nowway_type);

    void onPayWX(IPayAbs iPayAbs,String payway,String nowway_type);

}
