package com.dd.ddsq.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.comm_recyclviewadapter.BaseAdapter;
import com.example.comm_recyclviewadapter.BaseViewHolder;
import com.dd.ddsq.R;
import com.dd.ddsq.bean.GoagalInfo;
import com.dd.ddsq.bean.LoginDataInfo;
import com.dd.ddsq.bean.UserVipInfo;
import com.dd.ddsq.bean.VipItemInfo;
import com.dd.ddsq.config.APPConfig;
import com.dd.ddsq.config.PayConfig;
import com.dd.ddsq.config.SPConstant;
import com.dd.ddsq.eventbus.EventBean;
import com.dd.ddsq.listener.onPayListener;
import com.dd.ddsq.ui.fragment.dialog.OpenVIPFragment;
import com.dd.ddsq.util.Encrypt;
import com.dd.ddsq.util.FPUitl;
import com.dd.ddsq.util.LogUtil;
import com.dd.ddsq.util.SPUtils;
import com.dd.ddsq.util.ToastUtil;
import com.dd.ddsq.util.UIUtils;
import com.kk.pay.IPayAbs;
import com.kk.pay.IPayCallback;
import com.kk.pay.OrderInfo;
import com.kk.pay.OrderParamsInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by admin on 2017/2/28.
 */

public class VIPItemAdapterNew extends BaseAdapter<VipItemInfo> implements View.OnClickListener {


    private static final int ITEM = 0;
    private static final int ITEM_FOOTER = 1;
    private static final int ITEM_HEADER = 2;

    private boolean isClick1, isClick2, isClick3, isClick5, isOPenSVIP, isClickWX, isClickQQ;

    private static final int superId = 4;
    private static final int smId = 16;
    private static final int fhId = 15;

    private String[] items;
    private String[] endItems;


    private OpenVIPFragment openVIPFragment;

    private ImageView ivAssistFunc1, ivAssistFunc2, ivAssistFunc3, ivAssistFunc5, ivQq, ivWx;


    public VIPItemAdapterNew(Context context, List<VipItemInfo> mList) {
        super(context, mList);
        items = mContext.getResources().getStringArray(R.array.lei);
        endItems = mContext.getResources().getStringArray(R.array.end);

    }



    @Override
    protected void convert(final BaseViewHolder holder, int position) {
        if (holder.getItemViewType() == ITEM) {
            if (position > 0) {
                final VipItemInfo vipItemInfo = mList.get(position - 1);
                if (position == 1 || position == mList.size()) {
                    holder.setTextColor(R.id.tv_function, mContext.getResources().getColor(R.color.green_34843a));
                } else {
                    holder.setTextColor(R.id.tv_function, mContext.getResources().getColor(R.color.black));
                }
                holder.setText(R.id.tv_function, vipItemInfo.getTitle());
                final String id = vipItemInfo.getId();

//                isNeedPay(vipItemInfo);

                isNeedpay2(vipItemInfo).observeOn(AndroidSchedulers.mainThread());


                String spSaveStateStr = FPUitl.get(mContext, SPConstant.VIP_OPEN_ITEM_KEY + id, "false");
                boolean spSaveState = false;
                if (spSaveStateStr.equals("true")) {
                    spSaveState = true;
                }
                if (isOPenSVIP) {
                    holder.setImageDrawable(R.id.iv_func, spSaveState ? mContext.getResources().getDrawable(R.drawable.icon_on) : mContext.getResources().getDrawable(R.drawable.icon_off));

                    if (id.equals(smId + "")) {
                        holder.setImageDrawable(R.id.iv_func, spSaveState ? mContext.getResources().getDrawable(R.drawable.icon_on) : mContext.getResources().getDrawable(R.drawable.icon_off));
                    }
                } else {
                    holder.setImageDrawable(R.id.iv_func, spSaveState ? mContext.getResources().getDrawable(R.drawable.icon_on) : mContext.getResources().getDrawable(R.drawable.icon_off));
                }
                holder.setTag(R.id.iv_func, spSaveState);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        isNeedpay2(vipItemInfo).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean aBoolean) {
                                LogUtil.msg("Boolean   ");
                                LogUtil.msg("Boolean   " + aBoolean.booleanValue());
                                if (aBoolean.booleanValue()) {
                                    pay(vipItemInfo);
                                } else {
                                    boolean isOpen = !(boolean) holder.getTag(R.id.iv_func);
                                    holder.setImageDrawable(R.id.iv_func, isOpen ? mContext.getResources().getDrawable(R.drawable.icon_on) : mContext.getResources().getDrawable(R.drawable.icon_off));
                                    ToastUtil.showToast(mContext, holder.getText(R.id.iv_func).toString() + (isOpen ? "已开启" : "已关闭"), Toast.LENGTH_SHORT);
                                    FPUitl.putString(mContext, SPConstant.VIP_OPEN_ITEM_KEY + id, isOpen + "");
                                    holder.setTag(R.id.iv_func, isOpen);
                                    if (vipItemInfo.getId().equals(superId + "")) {
                                        openSvip(isOpen);
                                    }
                                    if (!vipItemInfo.getId().equals(superId + "") && !vipItemInfo.getId().equals(smId + "") && !isOpen) {
                                        FPUitl.putString(mContext, SPConstant.VIP_OPEN_ITEM_KEY + superId, false + "");
                                        notifyDataSetChanged();
                                    }
                                }

                            }
                        });


//                        if (isNeedPay(vipItemInfo)) {
//                            pay(vipItemInfo);
//                        } else {
//                            boolean isOpen = !(boolean) holder.getTag(R.id.iv_func);
//                            holder.setImageDrawable(R.id.iv_func, isOpen ? mContext.getResources().getDrawable(R.drawable.icon_on) : mContext.getResources().getDrawable(R.drawable.icon_off));
//                            ToastUtil.showToast(mContext, holder.getText(R.id.iv_func).toString() + (isOpen ? "已开启" : "已关闭"), Toast.LENGTH_SHORT);
//                            FPUitl.putString(mContext, SPConstant.VIP_OPEN_ITEM_KEY + id, isOpen + "");
//                            holder.setTag(R.id.iv_func, isOpen);
//                            if (vipItemInfo.getId().equals(superId + "")) {
//                                openSvip(isOpen);
//                            }
//                            if (!vipItemInfo.getId().equals(superId + "") && !vipItemInfo.getId().equals(smId + "") && !isOpen) {
//                                FPUitl.putString(mContext, SPConstant.VIP_OPEN_ITEM_KEY + superId, false + "");
//                                notifyDataSetChanged();
//                            }
//                        }
                    }
                });

                if (position == 3) {
                    holder.setVisible(R.id.spinner, true);

                    // 建立Adapter并且绑定数据源
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, items);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        adapter.setDropDownViewResource(R.layout.myspinner);
                    ((Spinner) holder.getView(R.id.spinner)).setAdapter(adapter);
                    int idx = 0;
                    try {
                        idx = Integer.parseInt(FPUitl.get(mContext, SPConstant.LEI_KEY, "0"));
                    } catch (Exception e) {

                    }
                    ((Spinner) holder.getView(R.id.spinner)).setSelection(idx);
                    ((Spinner) holder.getView(R.id.spinner)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            TextView tv = (TextView) view;
                            tv.setTextColor(mContext.getResources().getColor(R.color.a7));
                            tv.setTextSize(14);
                            FPUitl.putString(mContext, SPConstant.LEI_KEY, position + "");
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (position == 4) {
                    holder.setVisible(R.id.spinner, true);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, endItems);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ((Spinner) holder.getView(R.id.spinner)).setAdapter(adapter);
                    int idx = 0;
                    try {
                        idx = Integer.parseInt(FPUitl.get(mContext, SPConstant.END_KEY, "0"));
                    } catch (Exception e) {

                    }
                    ((Spinner) holder.getView(R.id.spinner)).setSelection(idx);
                    ((Spinner) holder.getView(R.id.spinner)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            TextView tv = (TextView) view;
                            tv.setTextColor(mContext.getResources().getColor(R.color.a7));
                            tv.setTextSize(14);
                            FPUitl.putString(mContext, SPConstant.END_KEY, position + "");
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else {
                    holder.setVisible(R.id.spinner, false);
                }
            }
        } else if (holder.getItemViewType() == ITEM_FOOTER) {
            ivAssistFunc1 = holder.getView(R.id.iv_assist_func1);
            ivAssistFunc2 = holder.getView(R.id.iv_assist_func2);
            ivAssistFunc3 = holder.getView(R.id.iv_assist_func3);
            ivAssistFunc5 = holder.getView(R.id.iv_assist_func5);


            holder.setOnClickListener(R.id.rl_assist_func1, this);
            holder.setOnClickListener(R.id.rl_assist_func2, this);
            holder.setOnClickListener(R.id.rl_assist_func3, this);

            holder.setOnClickListener(R.id.rl_assist_func5, this);


            isClick1 = SPUtils.getBoolean1(mContext, SPConstant.OPEN_HONGBAO_KEY, true);
            isClick2 = SPUtils.getBoolean1(mContext, SPConstant.SELF_BACK_CHAT_KEY, true);
            isClick3 = SPUtils.getBoolean1(mContext, SPConstant.SONG_HINT_KEY, true);
            isClick5 = SPUtils.getBoolean1(mContext, SPConstant.ROB_SELF_KEY, true);

            setImageResource(ivAssistFunc1, isClick1);
            setImageResource(ivAssistFunc2, isClick2);
            setImageResource(ivAssistFunc3, isClick3);
            setImageResource(ivAssistFunc5, isClick5);


        } else if (holder.getItemViewType() == ITEM_HEADER) {


            isClickWX = SPUtils.getBoolean1(mContext, SPConstant.ROB_WX, true);
            isClickQQ = SPUtils.getBoolean1(mContext, SPConstant.ROB_QQ, true);

            ivQq = holder.getView(R.id.iv_qq);
            ivWx = holder.getView(R.id.iv_wx);
            setImageResource(ivQq, isClickQQ);
            setImageResource(ivWx, isClickWX);

            holder.setOnClickListener(R.id.rl_qq, this);
            holder.setOnClickListener(R.id.rl_wx, this);

        }


    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_HEADER;
        }
        if (position == getItemCount() - 1) {
            return ITEM_FOOTER;
        }
        return ITEM;
    }

    @Override
    public int getLayoutID(int viewType) {
        if (viewType == ITEM_FOOTER) {
            return R.layout.vip_item_footer;

        } else if (viewType == ITEM) {
            return R.layout.vip_item;

        } else if (viewType == ITEM_HEADER) {

            return R.layout.item_header;

        }
        return 0;
    }


    private void setImageResource(ImageView iv, boolean flag) {
        iv.setImageDrawable(flag ? mContext.getResources().getDrawable(R.drawable.icon_on) : mContext.getResources().getDrawable(R.drawable.icon_off));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_assist_func1:
                isClick1 = !isClick1;
                setImageResource(ivAssistFunc1, isClick1);
                ToastUtil.showToast(mContext, isClick1 ? "抢红包服务已开启" : "抢红包服务已关闭", Toast.LENGTH_SHORT);
                SPUtils.put1(mContext, SPConstant.OPEN_HONGBAO_KEY, isClick1);
                EventBus.getDefault().post(new EventBean(true, !isClick1));

                break;
            case R.id.rl_assist_func2:
                isClick2 = !isClick2;
                setImageResource(ivAssistFunc2, isClick2);
                SPUtils.put1(mContext, SPConstant.SELF_BACK_CHAT_KEY, isClick2);
                break;
            case R.id.rl_assist_func3:
                isClick3 = !isClick3;
                setImageResource(ivAssistFunc3, isClick3);
                SPUtils.put1(mContext, SPConstant.SONG_HINT_KEY, isClick3);
                break;
            case R.id.rl_assist_func5:
                isClick5 = !isClick5;
                setImageResource(ivAssistFunc5, isClick5);
                SPUtils.put1(mContext, SPConstant.ROB_SELF_KEY, isClick5);
                break;
            case R.id.rl_qq:
                isClickQQ = !isClickQQ;
                setImageResource(ivQq, isClickQQ);
                SPUtils.put1(mContext, SPConstant.ROB_QQ, isClickQQ);
                break;
            case R.id.rl_wx:
                isClickWX = !isClickWX;
                setImageResource(ivWx, isClickWX);
                SPUtils.put1(mContext, SPConstant.ROB_WX, isClickWX);
                break;

        }
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

    private boolean isNeedPay(VipItemInfo vipItemInfo) {
        boolean flag = true;

        if (isOPenSVIP && !vipItemInfo.getId().equals(smId + "")) {
            return false;
        }
        List<String> vipsInfoIds = synVipIds();
        for (String id : vipsInfoIds) {
            if (id.equals(vipItemInfo.getId())) {
                if (id.equals(superId + "")) {
                    isOPenSVIP = true;
                }
                flag = false;
                break;
            }
        }
        return flag;
    }


    private Observable isNeedpay2(final VipItemInfo vipItemInfo) {

        return Observable.just("").subscribeOn(Schedulers.io()).map(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String s) {
                return isNeedPay(vipItemInfo);
            }
        });
    }


    private void pay(final VipItemInfo vipItemInfo) {

        openVIPFragment = new OpenVIPFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("vipItemInfo", vipItemInfo);
        openVIPFragment.setArguments(bundle);
        if (mContext instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) mContext;
            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
            ft.add(openVIPFragment, null);
            ft.commitAllowingStateLoss();
        }
//        openVIPFragment.show(mContext.getSupportFragmentManager(), null);

        final OrderParamsInfo orderParamsInfo = getOrderParamsInfo(vipItemInfo);
        openVIPFragment.setListener(new onPayListener() {
            @Override
            public void onPayAli(IPayAbs iPayAbs, String payway, String nowway_type) {
                orderParamsInfo.setPayway_name(payway);
                iPayAbs.alipay(orderParamsInfo, new ICallBack(vipItemInfo));

            }

            @Override
            public void onPayWX(IPayAbs iPayAbs, String payway, String nowway_type) {
                orderParamsInfo.setPayway_name(payway);
                iPayAbs.wxpay(orderParamsInfo, new ICallBack(vipItemInfo));

            }

        });
    }

    private class ICallBack implements IPayCallback {
        private VipItemInfo vipItemInfo;

        private ICallBack(VipItemInfo vipItemInfo) {
            this.vipItemInfo = vipItemInfo;
        }

        @Override
        public void onSuccess(OrderInfo orderInfo) {
            saveData(vipItemInfo);
            LogUtil.msg("onSuccess    " + orderInfo.toString());

        }

        @Override
        public void onFailure(OrderInfo orderInfo) {

        }
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


    private void openSvip(boolean isOpen) {
        for (VipItemInfo vipItemInfo1 : mList) {
            String vip1Id = vipItemInfo1.getId();
            if (!vip1Id.equals("" + smId)) {
                FPUitl.putString(mContext, SPConstant.VIP_OPEN_ITEM_KEY + vip1Id, isOpen + "");
            }
        }
        if (mContext instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) mContext;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            });
        }
    }

    private void saveData(VipItemInfo vipItemInfo) {
        if (openVIPFragment.isVisible()) openVIPFragment.dismiss();
        String vipInfos = FPUitl.get(mContext, PayConfig.Prf_VipInfos, "");
        String temp_id = vipItemInfo.getId();

        FPUitl.putString(mContext, PayConfig.Prf_VipInfos, vipInfos + "-" + temp_id);
        if (temp_id.equals(superId + "")) {
            openSvip(true);
        } else {
            FPUitl.putString(mContext, SPConstant.VIP_OPEN_ITEM_KEY + temp_id, true + "");
            if (mContext instanceof AppCompatActivity) {
                AppCompatActivity activity = (AppCompatActivity) mContext;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        }
    }

}
