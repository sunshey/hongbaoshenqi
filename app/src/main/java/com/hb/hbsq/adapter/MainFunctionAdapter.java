package com.hb.hbsq.adapter;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.comm_recyclviewadapter.BaseAdapter;
import com.example.comm_recyclviewadapter.BaseViewHolder;
import com.hb.hbsq.R;
import com.hb.hbsq.bean.GoagalInfo;
import com.hb.hbsq.bean.LoginDataInfo;
import com.hb.hbsq.bean.UserVipInfo;
import com.hb.hbsq.bean.VipItemInfo;
import com.hb.hbsq.bean.VipItemListInfo;
import com.hb.hbsq.config.APPConfig;
import com.hb.hbsq.config.PayConfig;
import com.hb.hbsq.config.SPConstant;
import com.hb.hbsq.engine.VipEngin;
import com.hb.hbsq.listener.onPayListener;
import com.hb.hbsq.ui.activity.HelpActivity;
import com.hb.hbsq.ui.activity.HelpActivityNew;
import com.hb.hbsq.ui.activity.RecordActivty;
import com.hb.hbsq.ui.activity.VIPActivity;
import com.hb.hbsq.ui.fragment.dialog.OpenVIPFragment;
import com.hb.hbsq.util.Encrypt;
import com.hb.hbsq.util.FPUitl;
import com.hb.hbsq.util.LogUtil;
import com.hb.hbsq.util.NetUtils;
import com.hb.hbsq.util.SPUtils;
import com.hb.hbsq.util.ToastUtil;
import com.hb.hbsq.util.UIUtils;
import com.kk.pay.IPayAbs;
import com.kk.pay.IPayCallback;
import com.kk.pay.OrderInfo;
import com.kk.pay.OrderParamsInfo;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2017/3/15 17:52.
 */

public class MainFunctionAdapter extends BaseAdapter<String> {

    private boolean isOpenSVIP;

    private OpenVIPFragment openVIPFragment;

    private VipEngin vipEngin;

    public MainFunctionAdapter(Context context, List<String> mList) {
        super(context, mList);
        if (vipEngin == null) {
            vipEngin = new VipEngin();
        }
    }

    @Override
    public int getLayoutID(int viewType) {
        return R.layout.item_main_function;
    }


    @Override
    protected int getTotalCount() {
        return mList.size();
    }

    @Override
    protected void convert(BaseViewHolder holder, int position) {
        setDynamicParams(holder);
        holder.setText(R.id.btn_function, mList.get(position));
        onClick(holder, position);
    }


    private void setDynamicParams(BaseViewHolder holder) {

        Display display = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int height = display.getHeight();
        int width = display.getWidth();
        LogUtil.msg("height:  " + height + "   width:  " + width);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.getView(R.id.btn_function).getLayoutParams();
        layoutParams.width = width / 2;
        holder.getView(R.id.btn_function).setLayoutParams(layoutParams);

    }

    private List<String> synVipIds() {
        List<String> idList = new ArrayList<>();
        if (GoagalInfo.loginDataInfo != null && GoagalInfo.loginDataInfo.getVipListInfo() != null) {
            List<UserVipInfo> vipListInfo = GoagalInfo.loginDataInfo.getVipListInfo();
            if (vipListInfo != null) {
                for (UserVipInfo vipInfo : vipListInfo) {
                    idList.add(vipInfo.getVip_id());
                }
            }
        }

        String vipsInfos = FPUitl.get(mContext, PayConfig.Prf_VipInfos, "");
        String[] vipsInfoIds = vipsInfos.split("-");
        for (String id : vipsInfoIds) {
            boolean flag = false;
            for (String _id : idList) {
                if (_id.equals(id)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                idList.add(id);
            }
        }
        return idList;
    }

    private void onClick(final BaseViewHolder holder, final int postion) {
        holder.setOnClickListener(R.id.btn_function, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (postion) {
                    case 0:
                        mContext.startActivity(new Intent(mContext, VIPActivity.class));
                        break;
                    case 1:
                        if (synVipIds() != null && synVipIds().size() > 0) {
                            for (String s : synVipIds()) {
                                if ("4".equals(s)) {
                                    isOpenSVIP = true;
                                    break;
                                }
                            }
                        }
                        if (isOpenSVIP) {
                            mContext.startActivity(new Intent(mContext, VIPActivity.class));
                        } else {
                            openSVIP();
                        }
                        break;

                    case 2:
                        mContext.startActivity(new Intent(mContext, RecordActivty.class));
                        break;
                    case 3:
                        if (NetUtils.checekConnection(mContext)) {
                            mContext.startActivity(new Intent(mContext, HelpActivityNew.class));
                        } else {
                            mContext.startActivity(new Intent(mContext, HelpActivity.class));
                        }
                        break;

                }
            }
        });

    }


    private void openSVIP() {

        String vipitem = Encrypt.decode(SPUtils.getString(mContext, SPConstant.VIP_ITEM_KEY));
        List<VipItemInfo> vipItemInfos = JSONObject.parseArray(vipitem, VipItemInfo.class);
        if (vipItemInfos == null) {
            getData();
        } else {
            show(vipItemInfos);
        }

    }

    private void show(List<VipItemInfo> vipItemInfos) {
        if (vipItemInfos != null && vipItemInfos.size() > 0) {
            final VipItemInfo vipItemInfo = vipItemInfos.get(0);
            openVIPFragment = new OpenVIPFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("vipItemInfo", vipItemInfo);
            openVIPFragment.setArguments(bundle);
            if (mContext instanceof AppCompatActivity) {
                AppCompatActivity activity = (AppCompatActivity) mContext;

                if (!activity.isDestroyed()) {

                    FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                    ft.add(openVIPFragment, null);
                    ft.commitAllowingStateLoss();
                }
            }
//            openVIPFragment.show(mContext.getSupportFragmentManager(), null);
            final OrderParamsInfo orderParamsInfo = getOrderParamsInfo(vipItemInfo);
            openVIPFragment.setListener(new onPayListener() {
                @Override
                public void onPayAli(IPayAbs iPayAbs, String payway, String nowway_type) {
                    orderParamsInfo.setPayway_name(payway);
                    iPayAbs.alipay(orderParamsInfo, new ICallBack());
                }

                @Override
                public void onPayWX(IPayAbs iPayAbs, String payway, String nowway_type) {
                    orderParamsInfo.setPayway_name(payway);
                    iPayAbs.wxpay(orderParamsInfo, new ICallBack());
                }
            });
        }
    }

    private class ICallBack implements IPayCallback {


        @Override
        public void onSuccess(OrderInfo orderInfo) {

            String vipInfos = FPUitl.get(mContext, PayConfig.Prf_VipInfos, "");

            FPUitl.putString(mContext, PayConfig.Prf_VipInfos, vipInfos + "-" + orderInfo.getGood_id());
            try {
                mContext.startActivity(new Intent(mContext, VIPActivity.class));
            } catch (ActivityNotFoundException e) {

            }

            LogUtil.msg("onSuccess    " + orderInfo.toString());
        }

        @Override
        public void onFailure(OrderInfo orderInfo) {

        }
    }

    private void getData() {
        vipEngin.rxGetInfo().observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ResultInfo<VipItemListInfo>>() {
                               @Override
                               public void call(ResultInfo<VipItemListInfo> resultInfo) {
                                   if (resultInfo != null && resultInfo.code == 1 && resultInfo.data != null) {
                                       List<VipItemInfo> vipInfoList = resultInfo.data.getVipInfoList();
                                       show(vipInfoList);
                                       String json = JSON.toJSONString(vipInfoList);
                                       SPUtils.put(mContext, SPConstant.VIP_ITEM_KEY, Encrypt.encode(json));
                                   } else {
                                       ToastUtil.showToast(mContext, HttpConfig.NET_ERROR, Toast.LENGTH_SHORT);
                                   }
                               }
                           }

                );

    }

    private OrderParamsInfo getOrderParamsInfo(VipItemInfo vipItemInfo) {
        OrderParamsInfo orderParamsInfo = new OrderParamsInfo(APPConfig.PAY_URL, vipItemInfo.getId(), PayConfig.PAY_TYPE_CONSUME + "", Float.parseFloat(vipItemInfo.getReal_price()));
        String json = SPUtils.getString(mContext, SPConstant.GOAGAL_INFO_KEY);
        LoginDataInfo info = JSON.parseObject(Encrypt.decode(json), LoginDataInfo.class);
        if (GoagalInfo.loginDataInfo == null) {
            GoagalInfo.loginDataInfo = info;
        }
        if (GoagalInfo.loginDataInfo != null && GoagalInfo.loginDataInfo.getQq() != null) {
            String qq = GoagalInfo.loginDataInfo.getQq();
            orderParamsInfo.setName(qq);
            orderParamsInfo.setUid(UIUtils.getUid(mContext));
        }

        return orderParamsInfo;

    }

}
