package com.dd.ddsq.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.example.comm_recyclviewadapter.BaseAdapter;
import com.example.comm_recyclviewadapter.BaseViewHolder;
import com.dd.ddsq.R;
import com.dd.ddsq.bean.HongbaoItemInfo;
import com.kk.pay.other.AnimationUtil;
import com.kk.pay.other.LogUtil;

import java.util.List;

/**
 * Created by wanglin  on 2017/3/4 09:18.
 */

public class HongbaoItemAdapter extends BaseAdapter<HongbaoItemInfo> {

    public HongbaoItemAdapter(Context context, List<HongbaoItemInfo> mList) {
        super(context, mList);
    }

    @Override
    public int getLayoutID(int viewType) {

        return R.layout.detail_hongbao_item;

    }


    @Override
    protected void convert(BaseViewHolder holder, int position) {

        HongbaoItemInfo hongbaoInfo = mList.get(position);

        holder.setText(R.id.tv_from, hongbaoInfo.getFrom().equals("wx") ? "微信" : hongbaoInfo.getFrom().equals("qq") ? "QQ" : hongbaoInfo.getFrom());

        holder.setText(R.id.tv_money, String.valueOf(hongbaoInfo.getMoney()));

        holder.setText(R.id.tv_nickname, hongbaoInfo.getPayer());
        String add_time = hongbaoInfo.getAdd_time();
        if (!TextUtils.isEmpty(add_time)) {
            String[] split = add_time.split(" ");

            holder.setText(R.id.tv_time, split[0] + "\n" + split[1]);
        }

    }

}
