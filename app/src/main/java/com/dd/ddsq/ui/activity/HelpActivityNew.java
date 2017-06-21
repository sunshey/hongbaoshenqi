package com.dd.ddsq.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.ddsq.R;
import com.dd.ddsq.adapter.HelpItemAdapter;
import com.dd.ddsq.bean.QAInfo;
import com.dd.ddsq.bean.QAListInfo;
import com.dd.ddsq.common.BaseActivity;
import com.dd.ddsq.engine.QaEngin;
import com.dd.ddsq.util.ToastUtil;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;

import java.util.List;

import butterknife.BindView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by admin on 2017/2/23.
 */
public class HelpActivityNew extends BaseActivity {


    @BindView(R.id.rcv)
    RecyclerView rcv;


    private HelpItemAdapter adapter;

    @Override
    protected boolean isNeedTitle() {
        return true;
    }

    @Override
    protected void initData() {

        getHelpList();
        initView();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcv.setLayoutManager(layoutManager);
        adapter = new HelpItemAdapter(this, null);

        rcv.setAdapter(adapter);
    }

    @Override
    protected int getResourseLayout() {
        return R.layout.activity_help_new;
    }

    @Override
    public void initHeader(TextView tv_header_left) {
    }

    private void getHelpList() {

        QaEngin qaEngin = new QaEngin();

        Observable<ResultInfo<QAListInfo>> observable = qaEngin.rxGetInfo();

        observable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<ResultInfo<QAListInfo>>() {
            @Override
            public void call(final ResultInfo<QAListInfo> resultInfo) {

                if (resultInfo != null && resultInfo.code == HttpConfig.STATUS_OK && resultInfo.data != null) {
                    List<QAInfo> qaInfoList = resultInfo.data.getQaInfoList();
                    adapter.setData(qaInfoList);

                } else {
                    ToastUtil.showToast(HelpActivityNew.this, HttpConfig.NET_ERROR, Toast.LENGTH_SHORT);
                }

            }
        });
    }


}
