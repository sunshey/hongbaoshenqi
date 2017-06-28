package com.dd.ddsq.ui.fragment.dialog;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dd.ddsq.R;
import com.dd.ddsq.bean.GoagalInfo;
import com.dd.ddsq.bean.PayWayInfo;
import com.dd.ddsq.bean.VipItemInfo;
import com.dd.ddsq.common.BasePayDialogFragment;
import com.dd.ddsq.config.PayConfig;
import com.dd.ddsq.listener.onPayListener;
import com.kk.pay.I1PayAbs;
import com.kk.pay.IPayAbs;
import com.kk.pay.PayImplFactory;

import java.math.BigDecimal;

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
    @BindView(R.id.tv_discount_money)
    TextView tvDiscountMoney;
    @BindView(R.id.tv_discount)
    TextView tvDiscount;
    @BindView(R.id.rl_discount)
    RelativeLayout rlDiscount;


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

            String realPrice = vipItemInfo.getReal_price();
            String price = vipItemInfo.getPrice();
            String pjName = vipItemInfo.getTitle() + GoagalInfo.agent_id;

            tvVipFunction.setText(pjName);
            tvVipMoney.setText(String.format(getResources().getString(R.string.money), realPrice));
            tvDiscountMoney.setText(String.format(getResources().getString(R.string.discount_money), Float.parseFloat(price)));

//            tvDiscountMoney.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);//中划线
            BigDecimal bigDecimal = new BigDecimal(realPrice);
            BigDecimal bigDecimal1 = new BigDecimal(price);
            float discount = bigDecimal.divide(bigDecimal1, 2, BigDecimal.ROUND_HALF_UP).floatValue() * 10;
            tvDiscount.setText(String.format(getResources().getString(R.string.discount), discount));


        }
    }

    @Override
    protected void setPaywayList(PayWayInfo payWayInfo) {
        String name = payWayInfo.getName();
        String title = payWayInfo.getTitle();

        if ("支付宝支付".equals(title)) {//支付宝
            rlAlipay.setVisibility(View.VISIBLE);
            viewAli.setVisibility(View.VISIBLE);
            ali_pay = payWayInfo.getName();
            pay_way = ALI_PAY;
            isAliVisable = true;
            if (!name.equals(PayConfig.PAY_WAY_NOWPAY_ALI)) {//现在支付支付宝
                ali_nowway_type = "";
            }
        } else if ("微信支付".equals(title)) {//微信
            rlWxpay.setVisibility(View.VISIBLE);
            viewWx.setVisibility(View.VISIBLE);
            wx_pay = payWayInfo.getName();
            if (!name.equals(PayConfig.PAY_WAY_NOWPAY_WX)) {//现在支付微信
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
                setDiscount(true);
                break;
            case R.id.rl_wxpay:
                ivWxpaySelector.setImageDrawable(getResources().getDrawable(R.drawable.pay_select_press));
                ivAlipaySelector.setImageDrawable(getResources().getDrawable(R.drawable.pay_select_normal));
                pay_way = WX_PAY;
                setDiscount(false);
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


    public void setDiscount(boolean isAli) {
        if (isAli) {
            rlDiscount.setVisibility(View.VISIBLE);
            tvVipMoney.setText(String.format(getResources().getString(R.string.money), vipItemInfo.getReal_price()));
            tvDiscountMoney.setText(String.format(getResources().getString(R.string.discount_money), Float.parseFloat(vipItemInfo.getPrice())));
        } else {
            rlDiscount.setVisibility(View.GONE);
            tvVipMoney.setText(String.format(getResources().getString(R.string.money), vipItemInfo.getPrice()));
        }
    }

}
