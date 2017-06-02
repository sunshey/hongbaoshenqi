package com.hb.hbsq.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hb.hbsq.R;
import com.hb.hbsq.adapter.VIPItemAdapter;
import com.hb.hbsq.bean.VipItemInfo;
import com.hb.hbsq.bean.VipItemListInfo;
import com.hb.hbsq.common.BaseActivity;
import com.hb.hbsq.config.SPConstant;
import com.hb.hbsq.engine.VipEngin;
import com.hb.hbsq.util.Encrypt;
import com.hb.hbsq.util.SPUtils;
import com.kk.pay.IPayImpl;
import com.kk.pay.IWXH5PayImpl;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by admin on 2017/2/27.
 * 设置VIP的页面
 */

public class VIPActivity extends BaseActivity {


    @BindView(R.id.rcv)
    RecyclerView rcv;
    private VIPItemAdapter adapter;
    private List<VipItemInfo> vipInfoList = new ArrayList<>();

    @Override
    protected int getResourseLayout() {
        return R.layout.activity_vip;
    }

    @Override
    public void initHeader(TextView tv_header_left) {
        tv_header_left.setText("高级设置");
    }


    @Override
    protected void initData() {

        String vipitem = Encrypt.decode(SPUtils.getString(this, SPConstant.VIP_ITEM_KEY));
        List<VipItemInfo> vipItemInfos = JSONObject.parseArray(vipitem, VipItemInfo.class);
        if (vipItemInfos != null) {
            vipInfoList = vipItemInfos;
        }
        getVipList();
        initView();

    }

    private void initView() {
        LinearLayoutManager manager = new LinearLayoutManager(VIPActivity.this);
        rcv.setLayoutManager(manager);
        adapter = new VIPItemAdapter(VIPActivity.this, vipInfoList);
        rcv.setAdapter(adapter);
    }

    /**
     * 获取VIP项目列表
     */
    private void getVipList() {
        VipEngin vipEngin = new VipEngin();

        vipEngin.rxGetInfo().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<ResultInfo<VipItemListInfo>>() {
            @Override
            public void call(final ResultInfo<VipItemListInfo> resultInfo) {

                if (resultInfo != null && resultInfo.code == HttpConfig.STATUS_OK && resultInfo.data != null) {
                    vipInfoList.clear();
                    vipInfoList.addAll(resultInfo.data.getVipInfoList());
                    String json = JSON.toJSONString(vipInfoList);
                    SPUtils.put(VIPActivity.this, SPConstant.VIP_ITEM_KEY, Encrypt.encode(json));
                    adapter.notifyDataSetChanged();
                }

            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (IPayImpl.uiPayCallback != null && IPayImpl.uOrderInfo != null && IWXH5PayImpl.isGen()) {
            IWXH5PayImpl.checkOrder(IPayImpl.uOrderInfo, IPayImpl.uiPayCallback);

        }
    }
}
