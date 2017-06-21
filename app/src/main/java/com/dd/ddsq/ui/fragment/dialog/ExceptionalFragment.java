package com.dd.ddsq.ui.fragment.dialog;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.dd.ddsq.R;
import com.dd.ddsq.bean.GoagalInfo;
import com.dd.ddsq.bean.LoginDataInfo;
import com.dd.ddsq.bean.PayWayInfo;
import com.dd.ddsq.common.BasePayDialogFragment;
import com.dd.ddsq.config.APPConfig;
import com.dd.ddsq.config.PayConfig;
import com.dd.ddsq.config.SPConstant;
import com.dd.ddsq.util.Encrypt;
import com.dd.ddsq.util.LogUtil;
import com.dd.ddsq.util.SPUtils;


import com.dd.ddsq.util.ToastUtil;
import com.dd.ddsq.util.UIUtils;
import com.kk.pay.I1PayAbs;
import com.kk.pay.IPayAbs;
import com.kk.pay.IPayCallback;
import com.kk.pay.IPayImpl;
import com.kk.pay.IXxPay2Impl;
import com.kk.pay.OrderInfo;
import com.kk.pay.OrderParamsInfo;
import com.kk.pay.PayImplFactory;

import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by admin on 2017/2/24.
 * 打赏页面
 */

public class ExceptionalFragment extends BasePayDialogFragment {

    @BindView(R.id.tv_money)
    TextView tvMoney;

    @BindView(R.id.et_exceptional_money)
    EditText etExceptionalMoney;
    @BindView(R.id.rl_exceptional)
    RelativeLayout rlExceptional;
    @BindView(R.id.btn_alipay)
    Button btnAlipay;
    @BindView(R.id.btn_wxpay)
    Button btnWxpay;

    private boolean isVisable = true;

    private double[] randoms = {6.66, 8.88, 16.66, 18.88, 26.66, 28.88, 36.66, 38.88, 46.66, 48.88, 56.66, 58.88, 66.66, 68.88, 76.66, 78.88, 86.66, 88.88, 96.66, 98.88};


    private String exceptionaMoney;
    private String wx_pay = PayConfig.PAY_WAY_NOWPAY_WX;
    private String ali_pay = PayConfig.PAY_WAY_ALIPAY;

    private IPayAbs iPayAbs;
    private IPayImpl payImpl;


    @Override
    protected int getLayoutId() {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setWindowAnimations(R.style.dialogFragment);
        return R.layout.dialog_exceptional;
    }

    @Override
    protected void initTitle(TextView tvTitle, ImageView ivClose) {

    }

    @Override
    protected boolean isNeedTitle() {
        return false;
    }

    @Override
    protected void initData() {
        super.initData();
        initListener();
    }

    @Override
    protected void setPaywayList(PayWayInfo payWayInfo) {
        String title = payWayInfo.getTitle();

        if ("支付宝支付".equals(title)) {//现在支付支付宝  支付宝
            btnAlipay.setVisibility(View.VISIBLE);
            ali_pay = payWayInfo.getName();
        } else if ("微信支付".equals(title)) {//现在支付微信 微信支付 h5微信支付 小小贝支付
            btnWxpay.setVisibility(View.VISIBLE);
            wx_pay = payWayInfo.getName();
        }

        payImpl = PayImplFactory
                .createPayImpl(getActivity(), wx_pay);
        iPayAbs = new I1PayAbs(getActivity(), PayImplFactory.createPayImpl(getActivity(), ali_pay), payImpl);

    }

    @Override
    public void initUI() {
        btnWxpay.setVisibility(View.VISIBLE);
        btnAlipay.setVisibility(View.VISIBLE);
    }

    private void initListener() {
        etExceptionalMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String lastMoney = s.toString().trim();
                if (!TextUtils.isEmpty(lastMoney)) {
                    tvMoney.setText(lastMoney + ".0");
                }
            }
        });
    }


    @OnClick({R.id.iv_close, R.id.tv_exceptional, R.id.btn_exceptional, R.id.tv_random_money, R.id.btn_alipay, R.id.btn_wxpay, R.id.btn_pay})
    public void onClick(View view) {
        exceptionaMoney = tvMoney.getText().toString().trim();
        OrderParamsInfo orderParamsInfo = getOrderParamsInfo(exceptionaMoney);
        switch (view.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.tv_exceptional://赏红包
                //// TODO: 2017/2/27
                ToastUtil.showToast(getContext(), "请稍等，红包马上就来", Toast.LENGTH_SHORT);
                break;
            case R.id.btn_exceptional:
                rlExceptional.setVisibility(isVisable ? View.VISIBLE : View.GONE);
                isVisable = !isVisable;
                break;
            case R.id.tv_random_money:
                if (rlExceptional.getVisibility() == View.VISIBLE) {
                    rlExceptional.setVisibility(View.GONE);
                }
                Random random = new Random();
                int i = random.nextInt(randoms.length);
                tvMoney.setText(String.valueOf(randoms[i]));
                break;
            case R.id.btn_alipay://接入支付宝//// TODO: 2017/2/27

                orderParamsInfo.setPayway_name(ali_pay);
                if (!TextUtils.isEmpty(exceptionaMoney) && iPayAbs != null) {
                    iPayAbs.alipay(orderParamsInfo, callback);
                }
                break;
            case R.id.btn_wxpay://接入微信 //// TODO: 2017/2/27
                orderParamsInfo.setPayway_name(wx_pay);
                if (!TextUtils.isEmpty(exceptionaMoney) && iPayAbs != null) {
                    iPayAbs.wxpay(orderParamsInfo, callback);
                    if (payImpl instanceof IXxPay2Impl) {
                        dismiss();
                    }

                }
                break;
            case R.id.btn_pay:

                break;

        }
    }

    private IPayCallback callback = new IPayCallback() {
        @Override
        public void onSuccess(final OrderInfo orderInfo) {

            if (isVisible()) dismiss();
            LogUtil.msg("onSuccess    " + orderInfo.toString());
        }

        @Override
        public void onFailure(OrderInfo orderInfo) {

            LogUtil.msg("onFailure    " + orderInfo.toString());
        }


    };


    private OrderParamsInfo getOrderParamsInfo(String money) {

        OrderParamsInfo orderParamsInfo = new OrderParamsInfo(APPConfig.PAY_URL, "0", PayConfig.PAY_TYPE_EXCEPTIONAL + "", Float.parseFloat(money));

        String json = SPUtils.getString(getActivity(), SPConstant.GOAGAL_INFO_KEY);
        LoginDataInfo info = JSON.parseObject(Encrypt.decode(json), LoginDataInfo.class);
        if (GoagalInfo.loginDataInfo == null) {
            GoagalInfo.loginDataInfo = info;
        }
        if (GoagalInfo.loginDataInfo != null && GoagalInfo.loginDataInfo.getQq() != null) {
            String qq = GoagalInfo.loginDataInfo.getQq();
            orderParamsInfo.setName(qq);
            orderParamsInfo.setUid(UIUtils.getUid(getContext()));
        }
        return orderParamsInfo;

    }

    @Override
    public void onPause() {
        super.onPause();
//        dismiss();
    }

}
