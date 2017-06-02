package com.hb.hbsq.adapter;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hb.hbsq.R;
import com.hb.hbsq.bean.GoagalInfo;
import com.hb.hbsq.bean.LoginDataInfo;
import com.hb.hbsq.bean.UserVipInfo;
import com.hb.hbsq.bean.VipItemInfo;
import com.hb.hbsq.config.APPConfig;
import com.hb.hbsq.config.PayConfig;
import com.hb.hbsq.config.SPConstant;
import com.hb.hbsq.eventbus.EventBean;
import com.hb.hbsq.listener.onPayListener;
import com.hb.hbsq.ui.fragment.dialog.OpenVIPFragment;
import com.hb.hbsq.util.Encrypt;
import com.hb.hbsq.util.FPUitl;
import com.hb.hbsq.util.LogUtil;
import com.hb.hbsq.util.SPUtils;
import com.hb.hbsq.util.ToastUtil;
import com.hb.hbsq.util.UIUtils;
import com.kk.pay.IPayAbs;
import com.kk.pay.IPayCallback;
import com.kk.pay.OrderInfo;
import com.kk.pay.OrderParamsInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by admin on 2017/2/28.
 */

public class VIPItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {


    private AppCompatActivity mContext;
    private List<VipItemInfo> vipInfoList;
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
    private HeadHolder headHolder;
    private FootHolder holder;


    public VIPItemAdapter(AppCompatActivity context, List<VipItemInfo> vipInfoList) {
        this.mContext = context;
        this.vipInfoList = vipInfoList;
        items = mContext.getResources().getStringArray(R.array.lei);
        endItems = mContext.getResources().getStringArray(R.array.end);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == ITEM_FOOTER) {
            view = View.inflate(parent.getContext(), R.layout.vip_item_footer, null);
            return new FootHolder(view);

        } else if (viewType == ITEM) {
            view = View.inflate(parent.getContext(), R.layout.vip_item, null);
            return new MyHolder(view);

        } else if (viewType == ITEM_HEADER) {
            view = View.inflate(parent.getContext(), R.layout.item_header, null);
            return new HeadHolder(view);

        }
        return null;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder hd, final int position) {
        if (hd instanceof MyHolder) {
            final MyHolder holder = (MyHolder) hd;
            if (position > 0) {
                final VipItemInfo vipItemInfo = vipInfoList.get(position - 1);
                if (position == 1 || position == vipInfoList.size()) {
                    holder.tvFunction.setTextColor(mContext.getResources().getColor(R.color.green_34843a));
                } else {
                    holder.tvFunction.setTextColor(mContext.getResources().getColor(R.color.black));
                }
                holder.tvFunction.setText(vipItemInfo.getTitle());
                final String id = vipItemInfo.getId();

                isNeedpay2(vipItemInfo).observeOn(AndroidSchedulers.mainThread()).subscribe();

                String spSaveStateStr = FPUitl.get(mContext, SPConstant.VIP_OPEN_ITEM_KEY + id, "false");
                boolean spSaveState = false;
                if (spSaveStateStr.equals("true")) {
                    spSaveState = true;
                }
                if (isOPenSVIP) {
                    holder.ivFunc.setImageDrawable(spSaveState ? mContext.getResources().getDrawable(R.drawable.icon_on) : mContext.getResources().getDrawable(R.drawable.icon_off));
                    if (id.equals(smId + "")) {
                        holder.ivFunc.setImageDrawable(spSaveState ? mContext.getResources().getDrawable(R.drawable.icon_on) : mContext.getResources().getDrawable(R.drawable.icon_off));
                    }
                } else {
                    holder.ivFunc.setImageDrawable(spSaveState ? mContext.getResources().getDrawable(R.drawable.icon_on) : mContext.getResources().getDrawable(R.drawable.icon_off));
                }
                holder.ivFunc.setTag(spSaveState);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        isNeedpay2(vipItemInfo).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean aBoolean) {
                                LogUtil.msg("thred 1 " + Thread.currentThread() + "----" + aBoolean);
                                if (aBoolean) {
                                    pay(vipItemInfo);
                                } else {
                                    boolean isOpen = !(boolean) holder.ivFunc.getTag();
                                    holder.ivFunc.setImageDrawable(isOpen ? mContext.getResources().getDrawable(R.drawable.icon_on) : mContext.getResources().getDrawable(R.drawable.icon_off));
                                    ToastUtil.showToast(mContext, holder.tvFunction.getText().toString() + (isOpen ? "已开启" : "已关闭"), Toast.LENGTH_SHORT);
                                    FPUitl.putString(mContext, SPConstant.VIP_OPEN_ITEM_KEY + id, isOpen + "");
                                    holder.ivFunc.setTag(isOpen);
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
                    }
                });

                if (position == 3) {
                    holder.spinner.setVisibility(View.VISIBLE);
                    // 建立Adapter并且绑定数据源
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, items);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        adapter.setDropDownViewResource(R.layout.myspinner);
                    holder.spinner.setAdapter(adapter);
                    int idx = 0;
                    try {
                        idx = Integer.parseInt(FPUitl.get(mContext, SPConstant.LEI_KEY, "0"));
                    } catch (Exception e) {

                    }
                    holder.spinner.setSelection(idx);
                    holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                    holder.spinner.setVisibility(View.VISIBLE);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, endItems);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    holder.spinner.setAdapter(adapter);
                    int idx = 0;
                    try {
                        idx = Integer.parseInt(FPUitl.get(mContext, SPConstant.END_KEY, "0"));
                    } catch (Exception e) {

                    }
                    holder.spinner.setSelection(idx);
                    holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                    holder.spinner.setVisibility(View.GONE);
                }
            }
        } else if (hd instanceof FootHolder) {
            FootHolder holder = (FootHolder) hd;
            this.holder = holder;
            holder.rlAssistFunc1.setOnClickListener(this);
            holder.rlAssistFunc2.setOnClickListener(this);
            holder.rlAssistFunc3.setOnClickListener(this);

            holder.rlAssistFunc5.setOnClickListener(this);

            isClick1 = SPUtils.getBoolean1(mContext, SPConstant.OPEN_HONGBAO_KEY, true);
            isClick2 = SPUtils.getBoolean1(mContext, SPConstant.SELF_BACK_CHAT_KEY, true);
            isClick3 = SPUtils.getBoolean1(mContext, SPConstant.SONG_HINT_KEY, true);
            isClick5 = SPUtils.getBoolean1(mContext, SPConstant.ROB_SELF_KEY, true);
            setImageResource(holder.ivAssistFunc1, isClick1);
            setImageResource(holder.ivAssistFunc2, isClick2);
            setImageResource(holder.ivAssistFunc3, isClick3);
            setImageResource(holder.ivAssistFunc5, isClick5);

        } else if (hd instanceof HeadHolder) {
            HeadHolder headHolder = (HeadHolder) hd;
            this.headHolder = headHolder;
            isClickWX = SPUtils.getBoolean1(mContext, SPConstant.ROB_WX, true);
            isClickQQ = SPUtils.getBoolean1(mContext, SPConstant.ROB_QQ, true);
            setImageResource(headHolder.ivQq, isClickQQ);
            setImageResource(headHolder.ivWx, isClickWX);
            headHolder.rlQq.setOnClickListener(this);
            headHolder.rlWx.setOnClickListener(this);

        }

    }

    @Override
    public int getItemCount() {
        return vipInfoList == null ? 0 : vipInfoList.size() + 2;
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


    private void setImageResource(ImageView iv, boolean flag) {
        iv.setImageDrawable(flag ? mContext.getResources().getDrawable(R.drawable.icon_on) : mContext.getResources().getDrawable(R.drawable.icon_off));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_assist_func1:
                isClick1 = !isClick1;
                holder.ivAssistFunc1.setImageDrawable(isClick1 ? mContext.getResources().getDrawable(R.drawable.icon_on) : mContext.getResources().getDrawable(R.drawable.icon_off));
                ToastUtil.showToast(mContext, isClick1 ? "抢红包服务已开启" : "抢红包服务已关闭", Toast.LENGTH_SHORT);
                SPUtils.put1(mContext, SPConstant.OPEN_HONGBAO_KEY, isClick1);
                EventBus.getDefault().post(new EventBean(true, !isClick1));

                break;
            case R.id.rl_assist_func2:
                isClick2 = !isClick2;
                holder.ivAssistFunc2.setImageDrawable(isClick2 ? mContext.getResources().getDrawable(R.drawable.icon_on) : mContext.getResources().getDrawable(R.drawable.icon_off));
                SPUtils.put1(mContext, SPConstant.SELF_BACK_CHAT_KEY, isClick2);
                break;
            case R.id.rl_assist_func3:
                isClick3 = !isClick3;
                holder.ivAssistFunc3.setImageDrawable(isClick3 ? mContext.getResources().getDrawable(R.drawable.icon_on) : mContext.getResources().getDrawable(R.drawable.icon_off));
                SPUtils.put1(mContext, SPConstant.SONG_HINT_KEY, isClick3);
                break;
            case R.id.rl_assist_func5:
                isClick5 = !isClick5;
                holder.ivAssistFunc5.setImageDrawable(isClick5 ? mContext.getResources().getDrawable(R.drawable.icon_on) : mContext.getResources().getDrawable(R.drawable.icon_off));
                SPUtils.put1(mContext, SPConstant.ROB_SELF_KEY, isClick5);
                break;
            case R.id.rl_qq:
                isClickQQ = !isClickQQ;
                setImageResource(headHolder.ivQq, isClickQQ);
                SPUtils.put1(mContext, SPConstant.ROB_QQ, isClickQQ);
                break;
            case R.id.rl_wx:
                isClickWX = !isClickWX;
                setImageResource(headHolder.ivWx, isClickWX);
                SPUtils.put1(mContext, SPConstant.ROB_WX, isClickWX);
                break;

        }
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_function)
        TextView tvFunction;
        @BindView(R.id.tv_function_extra)
        TextView tvFunctionExtra;
        @BindView(R.id.iv_func)
        ImageView ivFunc;
        @BindView(R.id.rl_function)
        RelativeLayout rlFunction;

        @BindView(R.id.spinner)
        Spinner spinner;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public class FootHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_assist_func1)
        ImageView ivAssistFunc1;
        @BindView(R.id.rl_assist_func1)
        RelativeLayout rlAssistFunc1;
        @BindView(R.id.iv_assist_func2)
        ImageView ivAssistFunc2;
        @BindView(R.id.rl_assist_func2)
        RelativeLayout rlAssistFunc2;
        @BindView(R.id.iv_assist_func3)
        ImageView ivAssistFunc3;
        @BindView(R.id.rl_assist_func3)
        RelativeLayout rlAssistFunc3;

        @BindView(R.id.iv_assist_func5)
        ImageView ivAssistFunc5;
        @BindView(R.id.rl_assist_func5)
        RelativeLayout rlAssistFunc5;

        public FootHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class HeadHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_wx)
        ImageView ivWx;
        @BindView(R.id.rl_wx)
        RelativeLayout rlWx;
        @BindView(R.id.iv_qq)
        ImageView ivQq;
        @BindView(R.id.rl_qq)
        RelativeLayout rlQq;

        public HeadHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setData(List<VipItemInfo> vipInfoList) {
        this.vipInfoList = vipInfoList;
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

    private Observable<Boolean> isNeedpay2(final VipItemInfo vipItemInfo) {

        return Observable.just(vipItemInfo).subscribeOn(Schedulers.io()).map(new Func1<VipItemInfo, Boolean>() {
            @Override
            public Boolean call(VipItemInfo vipItemInfo) {
                LogUtil.msg("thred 2 " + Thread.currentThread());
                return isNeedPay(vipItemInfo);
            }
        });
    }


    private void pay(final VipItemInfo vipItemInfo) {

        openVIPFragment = new OpenVIPFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("vipItemInfo", vipItemInfo);
        openVIPFragment.setArguments(bundle);
        FragmentTransaction ft = mContext.getSupportFragmentManager().beginTransaction();
        if (!openVIPFragment.isAdded()) {
            ft.add(openVIPFragment, null);
        }
        ft.commitAllowingStateLoss();
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
        for (VipItemInfo vipItemInfo1 : vipInfoList) {
            String vip1Id = vipItemInfo1.getId();
            if (!vip1Id.equals("" + smId)) {
                FPUitl.putString(mContext, SPConstant.VIP_OPEN_ITEM_KEY + vip1Id, isOpen + "");
            }
        }
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
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
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            });
        }
    }

}
