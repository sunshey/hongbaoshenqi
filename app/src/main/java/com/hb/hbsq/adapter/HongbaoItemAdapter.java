package com.hb.hbsq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.comm_recyclviewadapter.BaseAdapter;
import com.example.comm_recyclviewadapter.BaseViewHolder;
import com.hb.hbsq.R;
import com.hb.hbsq.bean.HongbaoItemInfo;
import com.kk.pay.other.AnimationUtil;
import com.kk.pay.other.LogUtil;
import com.nineoldandroids.view.ViewHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wanglin  on 2017/3/4 09:18.
 */

public class HongbaoItemAdapter extends BaseAdapter<HongbaoItemInfo> {

    public HongbaoItemAdapter(Context context, List<HongbaoItemInfo> mList) {
        super(context, mList);
    }

    @Override
    public int getLayoutID(int viewType) {
        if (viewType == ITEM) {
            return R.layout.detail_hongbao_item;
        } else if (viewType == ITEM_FOOTER) {
            return R.layout.hongbao_item_footer;
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount() && mIsFullScreen) {
            return ITEM_FOOTER;
        }
        return ITEM;
    }


    @Override
    protected int getTotalCount() {
        return mIsFullScreen ? mList.size() + 1 : mList.size();
    }

    @Override
    protected void convert(BaseViewHolder holder, int position) {
        if (holder.getItemViewType() == ITEM) {

            HongbaoItemInfo hongbaoInfo = mList.get(position);

            holder.setText(R.id.tv_from, hongbaoInfo.getFrom().equals("wx") ? "微信" : hongbaoInfo.getFrom().equals("qq") ? "QQ" : hongbaoInfo.getFrom());

            holder.setText(R.id.tv_money, String.valueOf(hongbaoInfo.getMoney()));

            holder.setText(R.id.tv_nickname, hongbaoInfo.getPayer());
            String add_time = hongbaoInfo.getAdd_time();
            if (!TextUtils.isEmpty(add_time)) {
                String[] split = add_time.split(" ");

                holder.setText(R.id.tv_time, split[0] + "\n" + split[1]);
            }
        } else if (holder.getItemViewType() == ITEM_FOOTER) {
            LogUtil.msg("mIsScroll   " + mIsScroll);
            if (!mIsScroll) {

                holder.setVisible(R.id.iv_circle, false);

                holder.setText(R.id.tv_msg, "没有更多红包记录啦");
            } else {

                holder.setVisible(R.id.iv_circle, true);
                holder.getView(R.id.iv_circle).startAnimation(AnimationUtil.rotaAnimation());
                holder.setText(R.id.tv_msg, "加载中...");

            }
        }
    }

}
