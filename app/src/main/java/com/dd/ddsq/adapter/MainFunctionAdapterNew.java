package com.dd.ddsq.adapter;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dd.ddsq.R;
import com.dd.ddsq.bean.GoagalInfo;
import com.dd.ddsq.bean.LoginDataInfo;
import com.dd.ddsq.bean.UserVipInfo;
import com.dd.ddsq.bean.VipItemInfo;
import com.dd.ddsq.bean.VipItemListInfo;
import com.dd.ddsq.config.APPConfig;
import com.dd.ddsq.config.PayConfig;
import com.dd.ddsq.config.SPConstant;
import com.dd.ddsq.engine.VipEngin;
import com.dd.ddsq.listener.onPayListener;
import com.dd.ddsq.ui.activity.VIPActivity;
import com.dd.ddsq.ui.fragment.dialog.DetailFunctionFragment;
import com.dd.ddsq.util.Encrypt;
import com.dd.ddsq.util.FPUitl;
import com.dd.ddsq.util.LogUtil;
import com.dd.ddsq.util.SPUtils;
import com.dd.ddsq.util.ToastUtil;
import com.dd.ddsq.util.UIUtils;
import com.example.comm_recyclviewadapter.BaseAdapter;
import com.example.comm_recyclviewadapter.BaseViewHolder;
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

public class MainFunctionAdapterNew extends BaseAdapter<String> {

    private DetailFunctionFragment detailFunctionFragment;

    private VipEngin vipEngin;

    private int[] iconList = {R.drawable.icon_speed, R.drawable.icon_control, R.drawable.icon_scan, R.drawable.icon_svip,
            R.drawable.icon_best, R.drawable.icon_bi, R.drawable.icon_big, R.drawable.icon_more};

    public MainFunctionAdapterNew(Context context, List<String> mList) {
        super(context, mList);

        if (vipEngin == null) {
            vipEngin = new VipEngin();
        }
    }

    @Override
    public int getLayoutID(int viewType) {
        return R.layout.item_main_function_new;
    }


    @Override
    protected void convert(BaseViewHolder holder, int position) {

        holder.setText(R.id.tv_function_name, mList.get(position));
        holder.setImageResource(R.id.iv_function_icon, iconList[position]);
        onClick(holder, position);
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

    private void onClick(final BaseViewHolder holder, final int position) {

        holder.setOnClickListener(R.id.ll_function, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (position) {
                    case 0://加速抢
                        if (isOpenVip("5")) {
                            mContext.startActivity(new Intent(mContext, VIPActivity.class));
                        } else {
                            openSVIP(position);
                        }
                        break;
                    case 1://尾数控制
                        if (isOpenVip("7")) {
                            mContext.startActivity(new Intent(mContext, VIPActivity.class));
                        } else {
                            openSVIP(position);
                        }
                        break;
                    case 2://扫雷
                        if (isOpenVip("6")) {
                            mContext.startActivity(new Intent(mContext, VIPActivity.class));
                        } else {
                            openSVIP(position);
                        }
                        break;
                    case 3:// 永久SVIP
                        if (isOpenVip("4")) {
                            mContext.startActivity(new Intent(mContext, VIPActivity.class));
                        } else {
                            openSVIP(position);
                        }
                        break;
                    case 4://手气最佳
                        if (isOpenVip("8")) {
                            mContext.startActivity(new Intent(mContext, VIPActivity.class));
                        } else {
                            openSVIP(position);
                        }
                        break;
                    case 5://躲避小包
                        if (isOpenVip("9")) {
                            mContext.startActivity(new Intent(mContext, VIPActivity.class));
                        } else {
                            openSVIP(position);
                        }
                        break;
                    case 6:// 抢大包
                        if (isOpenVip("16")) {
                            mContext.startActivity(new Intent(mContext, VIPActivity.class));
                        } else {
                            openSVIP(position);
                        }
                        break;
                    case 7://更多vip
                        mContext.startActivity(new Intent(mContext, VIPActivity.class));
                        break;

                }
            }
        });

    }

    private boolean isOpenVip(String id) {
        boolean isOpenSVIP = false;
        if (synVipIds() != null && synVipIds().size() > 0) {
            for (String s : synVipIds()) {
                if ("4".equals(s) || id.equals(s)) {
                    isOpenSVIP = true;
                    break;
                }
            }
        }
        return isOpenSVIP;
    }


    private void openSVIP(int postion) {

        String vipitem = Encrypt.decode(SPUtils.getString(mContext, SPConstant.VIP_ITEM_KEY));
        List<VipItemInfo> vipItemInfos = JSONObject.parseArray(vipitem, VipItemInfo.class);
        if (vipItemInfos == null) {
            getData(postion);
        } else {
            show(vipItemInfos, postion);
        }

    }

    //加速抢 尾数控制  扫雷  永久SVIP
    //手气最佳  躲避小宝  抢大包  更多vip
    private void show(List<VipItemInfo> vipItemInfos, int postion) {
        if (vipItemInfos != null && vipItemInfos.size() > 0) {
            VipItemInfo vipItemInfo = null;
            String functionIntroduce = "";
            switch (postion) {
                case 0:
                    vipItemInfo = vipItemInfos.get(1);
                    functionIntroduce = "系统自动优化内存\n可以以最快的速度抢到红包";
                    break;
                case 1:
                    vipItemInfo = vipItemInfos.get(3);
                    functionIntroduce = "系统通过计算分析\n可以很好的提高尾号控制";
                    break;
                case 2:
                    vipItemInfo = vipItemInfos.get(2);
                    functionIntroduce = "系统通过分析\n可以避免遇到雷区";
                    break;
                case 3:
                    vipItemInfo = vipItemInfos.get(0);
                    functionIntroduce = "将为您开通所有VIP功能\n并且享有永久使用权";
                    break;
                case 4:
                    vipItemInfo = vipItemInfos.get(4);
                    functionIntroduce = "系统将通过数据运算和分析\n帮助您提高抢到手气最佳包的概率";
                    break;
                case 5:
                    vipItemInfo = vipItemInfos.get(5);
                    functionIntroduce = "系统自动通过分析运算\n智能躲避最小红包";
                    break;
                case 6:
                    vipItemInfo = vipItemInfos.get(11);
                    functionIntroduce = "系统自动锁定大包出现时段\n立即提高抢大包的概率";
                    break;
            }

            detailFunctionFragment = new DetailFunctionFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("vipItemInfo", vipItemInfo);
            bundle.putString("function", functionIntroduce);
            detailFunctionFragment.setArguments(bundle);

            if (mContext instanceof AppCompatActivity) {
                AppCompatActivity activity = (AppCompatActivity) mContext;

                if (!activity.isDestroyed()) {

                    FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                    ft.add(detailFunctionFragment, null);
                    ft.commitAllowingStateLoss();
                }
            }

            final OrderParamsInfo orderParamsInfo = getOrderParamsInfo(vipItemInfo);
            detailFunctionFragment.setListener(new onPayListener() {
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

    private void getData(final int postion) {
        vipEngin.rxGetInfo().observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ResultInfo<VipItemListInfo>>() {
                               @Override
                               public void call(ResultInfo<VipItemListInfo> resultInfo) {
                                   if (resultInfo != null && resultInfo.code == 1 && resultInfo.data != null) {
                                       List<VipItemInfo> vipInfoList = resultInfo.data.getVipInfoList();
                                       show(vipInfoList, postion);
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
