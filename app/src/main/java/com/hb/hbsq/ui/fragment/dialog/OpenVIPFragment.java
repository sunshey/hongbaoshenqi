package com.hb.hbsq.ui.fragment.dialog;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hb.hbsq.R;
import com.hb.hbsq.bean.GoagalInfo;
import com.hb.hbsq.bean.PayWayInfo;
import com.hb.hbsq.bean.VipItemInfo;
import com.hb.hbsq.common.BasePayDialogFragment;
import com.hb.hbsq.config.PayConfig;
import com.hb.hbsq.listener.onPayListener;
import com.hb.hbsq.util.LogUtil;
import com.kk.pay.I1PayAbs;
import com.kk.pay.IPayAbs;
import com.kk.pay.PayImplFactory;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by admin on 2017/2/27.
 */

public class OpenVIPFragment extends BasePayDialogFragment {

    @BindView(R.id.tv_vip_function)
    TextView tvVipFunction;
    @BindView(R.id.tv_vip_money)
    TextView tvVipMoney;
    @BindView(R.id.iv_alipay_selector)
    ImageView ivAlipaySelector;
    @BindView(R.id.iv_wxpay_selector)
    ImageView ivWxpaySelector;
    @BindView(R.id.rl_alipay)
    RelativeLayout rlAlipay;
    @BindView(R.id.rl_wxpay)
    RelativeLayout rlWxpay;
    @BindView(R.id.view_ali)
    View viewAli;
    @BindView(R.id.view_wx)
    View viewWx;

    private int pay_way = 1;//选择支付方式
    private static final int ALI_PAY = 1;//支付宝支付
    private static final int WX_PAY = 2;//微信支付
    private VipItemInfo vipItemInfo;

    private String wx_pay = PayConfig.PAY_WAY_NOWPAY_WX;
    private String ali_pay = PayConfig.PAY_WAY_ALIPAY;

    private String wx_nowway_type = PayConfig.NOWPAY_WX;
    private String ali_nowway_type = PayConfig.NOWPAY_ALI;

    private IPayAbs iPayAbs;

    private boolean isAliVisable;

    @Override
    public int getLayoutId() {
        return R.layout.dialog_open_vip;
    }

    @Override
    protected boolean isNeedTitle() {
        return true;
    }

    @Override
    protected void initTitle(TextView tvTitle, ImageView imageView) {
        tvTitle.setText("开通VIP");
    }

    @Override
    protected void initData() {
        super.initData();
        if (getArguments() != null) {
            vipItemInfo = (VipItemInfo) getArguments().getSerializable("vipItemInfo");
            LogUtil.msg(vipItemInfo.toString());

            final String price = vipItemInfo.getReal_price();
            final String pjName = vipItemInfo.getTitle() + GoagalInfo.agent_id;

            tvVipFunction.setText(pjName);
            tvVipMoney.setText(String.format(getResources().getString(R.string.money), price));
        }
    }

    @Override
    protected void setPaywayList(PayWayInfo payWayInfo) {
        String name = payWayInfo.getName();
        String title = payWayInfo.getTitle();

        if ("支付宝支付".equals(title)) {////现在支付支付宝
            rlAlipay.setVisibility(View.VISIBLE);
            viewAli.setVisibility(View.VISIBLE);
            ali_pay = payWayInfo.getName();
            pay_way = ALI_PAY;
            isAliVisable = true;
            if (name.equals(PayConfig.PAY_WAY_ALIPAY)) {//支付宝
                ali_nowway_type = "";
            }
        } else if ("微信支付".equals(title)) {//现在支付微信
            rlWxpay.setVisibility(View.VISIBLE);
            viewWx.setVisibility(View.VISIBLE);
            wx_pay = payWayInfo.getName();
            if (name.equals(PayConfig.PAY_WAY_WXPAY) ||//微信
                    name.equals(PayConfig.PAY_WAY_H5WXPAY) ||//h5微信
                    name.equals(PayConfig.PAY_WAY_XXPAY)) {//小小贝微信
                wx_nowway_type = "";
            }

        }

        iPayAbs = new I1PayAbs(getActivity(), PayImplFactory.createPayImpl(getActivity(), ali_pay),
                PayImplFactory.createPayImpl(getActivity(), wx_pay));
        if (isAdded()) {
            if (!isAliVisable) {
                pay_way = WX_PAY;
                ivWxpaySelector.setImageDrawable(getResources().getDrawable(R.drawable.pay_select_press));
            } else {
                ivWxpaySelector.setImageDrawable(getResources().getDrawable(R.drawable.pay_select_normal));

            }
        }
    }

    @Override
    public void initUI() {
        rlWxpay.setVisibility(View.VISIBLE);
        rlAlipay.setVisibility(View.VISIBLE);
        viewWx.setVisibility(View.VISIBLE);
        viewAli.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.rl_alipay, R.id.rl_wxpay, R.id.btn_confirm_pay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_alipay:
                ivAlipaySelector.setImageDrawable(getResources().getDrawable(R.drawable.pay_select_press));
                ivWxpaySelector.setImageDrawable(getResources().getDrawable(R.drawable.pay_select_normal));
                pay_way = ALI_PAY;
                break;
            case R.id.rl_wxpay:
                ivWxpaySelector.setImageDrawable(getResources().getDrawable(R.drawable.pay_select_press));
                ivAlipaySelector.setImageDrawable(getResources().getDrawable(R.drawable.pay_select_normal));
                pay_way = WX_PAY;
                break;
            case R.id.btn_confirm_pay:

                switch (pay_way) {
                    case ALI_PAY:
                        if (listener != null && iPayAbs != null) {
                            listener.onPayAli(iPayAbs, ali_pay, ali_nowway_type);
                        }
                        break;
                    case WX_PAY:
                        if (listener != null && iPayAbs != null) {
                            listener.onPayWX(iPayAbs, wx_pay, wx_nowway_type);
                        }
                        break;

                }
        }
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        dismiss();
//    }

    private onPayListener listener;

    public onPayListener getListener() {
        return listener;
    }

    public void setListener(onPayListener listener) {
        this.listener = listener;
    }


}
