package com.dd.ddsq.ui.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.ddsq.R;
import com.dd.ddsq.adapter.HongbaoItemAdapter;
import com.dd.ddsq.bean.HongbaoItemInfo;
import com.dd.ddsq.bean.HongbaoListInfo;
import com.dd.ddsq.common.BaseActivity;
import com.dd.ddsq.engine.HongbaoListEngin;
import com.dd.ddsq.util.RecycleViewUtils;
import com.dd.ddsq.util.ToastUtil;
import com.dd.ddsq.util.UIUtils;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;

import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by admin on 2017/2/23.
 */

public class RecordActivty extends BaseActivity {

    @BindView(R.id.rcv)
    RecyclerView rcv;
    @BindView(R.id.srf)
    SwipeRefreshLayout srf;
    private HongbaoItemAdapter adapter;
    private int page = 1;
    private boolean isScroll = true;//设置recycleView是否滑动到底部
    private RecycleViewUtils recycleViewUtils;
    private List<HongbaoItemInfo> hongbaoInfoList;

    @Override
    protected boolean isNeedTitle() {
        return true;
    }

    @Override
    protected void initData() {
        srf.setRefreshing(true);
        srf.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        getRecord(false);
        initView();
    }

    private void initView() {
        recycleViewUtils = new RecycleViewUtils(rcv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcv.setLayoutManager(layoutManager);
//        rcv.setHasFixedSize(true);
        adapter = new HongbaoItemAdapter(this, null);
        adapter.setAnimationEnable(true);

//        rcv.addItemDecoration(new BaseItemDecoration(this, BaseItemDecoration.VERTICAL, new int[]{android.R.attr.listDivider}));
        rcv.setAdapter(adapter);
        initListener();

    }

    private void initListener() {
        srf.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                isScroll = true;
                getRecord(false);
                srf.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
            }
        });

        rcv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private LinearLayoutManager layoutManager;
            private int lastVisibleItemPosition;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (layoutManager != null) {
                    int itemCount = layoutManager.getItemCount();

                    if (newState == RecyclerView.SCROLL_STATE_IDLE && itemCount == lastVisibleItemPosition + 1 && recycleViewUtils.isFullScreen() && isScroll) {
                        getRecord(true);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();


            }
        });

    }

    @Override
    protected int getResourseLayout() {
        return R.layout.activity_record;
    }

    @Override
    public void initHeader(TextView tv_header_left) {
        tv_header_left.setText("红包记录");
    }

    private void getRecord(final boolean isLoadMore) {

        HongbaoListEngin hongbaoListEngin = new HongbaoListEngin();
        if (isLoadMore) {
            page++;
        }
        hongbaoListEngin.rxGetInfo(UIUtils.getUid(this), page).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ResultInfo<HongbaoListInfo>>() {
                               @Override
                               public void call(ResultInfo<HongbaoListInfo> resultInfo) {
                                   if (resultInfo != null && resultInfo.code == HttpConfig.STATUS_OK && resultInfo.data != null) {
                                       hongbaoInfoList = resultInfo.data.getHongbaoInfoList();
                                       if (isLoadMore) {

                                           isScroll = !(hongbaoInfoList == null || hongbaoInfoList.size() == 0);
                                           adapter.addData(hongbaoInfoList, isScroll, recycleViewUtils.isFullScreen());
                                       } else {
                                           adapter.setData(hongbaoInfoList, isScroll);
                                       }

                                   } else {
                                       ToastUtil.showToast(RecordActivty.this, HttpConfig.NET_ERROR, Toast.LENGTH_SHORT);
                                   }

                               }

                           }
                );


        if (srf.isRefreshing()) {

            srf.setRefreshing(false);
        }
    }


}
