package com.dd.ddsq.ui.fragment.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.ddsq.R;
import com.dd.ddsq.bean.GoagalInfo;
import com.dd.ddsq.bean.PayWayInfo;
import com.dd.ddsq.bean.VipItemInfo;
import com.dd.ddsq.common.BasePayDialogFragment;
import com.dd.ddsq.config.PayConfig;
import com.dd.ddsq.listener.onPayListener;
import com.dd.ddsq.util.ToastUtil;
import com.kk.pay.I1PayAbs;
import com.kk.pay.IPayAbs;
import com.kk.pay.PayImplFactory;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by wanglin  on 2017/6/19 11:06.
 */

public class DetailFunctionFragment extends BasePayDialogFragment {

    @BindView(R.id.tv_function)
    TextView tvFunction;
    @BindView(R.id.iv_close_function)
    ImageView ivCloseFunction;
    @BindView(R.id.btn_alipay)
    Button btnAlipay;
    @BindView(R.id.tv_detatil_introduce)
    TextView tvDetatilIntroduce;
    @BindView(R.id.tv_detail_price)
    TextView tvDetailPrice;


    private VipItemInfo vipItemInfo;

    private int pay_way = 1;//选择支付方式
    private static final int ALI_PAY = 1;//支付宝支付
    private static final int WX_PAY = 2;//微信支付


    private String wx_pay = PayConfig.PAY_WAY_NOWPAY_WX;
    private String ali_pay = PayConfig.PAY_WAY_ALIPAY;

    private String wx_nowway_type = PayConfig.NOWPAY_WX;
    private String ali_nowway_type = PayConfig.NOWPAY_ALI;

    private IPayAbs iPayAbs;

    @Override
    protected int getLayoutId() {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return R.layout.fragment_detail_function;
    }

    @Override
    protected void initTitle(TextView tvTitle, ImageView ivClose) {

    }

    @Override
    protected boolean isNeedTitle() {
        return false;
    }

    @Override
    protected void setPaywayList(PayWayInfo payWayInfo) {
        String name = payWayInfo.getName();
        String title = payWayInfo.getTitle();

        if ("支付宝支付".equals(title)) {////现在支付支付宝

            ali_pay = payWayInfo.getName();
            pay_way = ALI_PAY;

            if (name.equals(PayConfig.PAY_WAY_ALIPAY)) {//支付宝
                ali_nowway_type = "";
            }
        }

        iPayAbs = new I1PayAbs(getActivity(), PayImplFactory.createPayImpl(getActivity(), ali_pay),
                PayImplFactory.createPayImpl(getActivity(), wx_pay));

    }

    @Override
    protected void initUI() {

    }

    @Override
    protected void initData() {
        super.initData();
        if (getArguments() != null) {
            vipItemInfo = (VipItemInfo) getArguments().getSerializable("vipItemInfo");
            String function = getArguments().getString("function");

            String realPrice = vipItemInfo.getReal_price();
            String price = vipItemInfo.getPrice();
            final String pjName = vipItemInfo.getTitle() + GoagalInfo.channelInfo.agent_id;

            tvFunction.setText(pjName);
            tvDetailPrice.setText(price);
            tvDetatilIntroduce.setText(function);

//            tvDiscountMoney.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);//中划线
            BigDecimal bigDecimal = new BigDecimal(realPrice);
            BigDecimal bigDecimal1 = new BigDecimal(price);
            float discount = bigDecimal.divide(bigDecimal1, 2, BigDecimal.ROUND_HALF_UP).floatValue() * 10;
            btnAlipay.setText(String.format(getResources().getString(R.string.alipay), discount, realPrice));


        }

    }

    @OnClick({R.id.iv_close_function, R.id.btn_alipay, R.id.btn_wxpay})
    public void onCLick(View view) {
        switch (view.getId()) {
            case R.id.iv_close_function:
                dismiss();
                break;
            case R.id.btn_alipay:
                if (listener != null && iPayAbs != null) {
                    listener.onPayAli(iPayAbs, ali_pay, ali_nowway_type);
                }
                break;
            case R.id.btn_wxpay:
                ToastUtil.showToast(getContext(), "微信支付暂不支持，请选择支付宝", Toast.LENGTH_SHORT, Gravity.CENTER);
                break;
        }

    }


    private onPayListener listener;

    public onPayListener getListener() {
        return listener;
    }

    public void setListener(onPayListener listener) {
        this.listener = listener;
    }
}
