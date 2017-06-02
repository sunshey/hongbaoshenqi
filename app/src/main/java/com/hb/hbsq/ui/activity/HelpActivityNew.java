package com.hb.hbsq.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.hb.hbsq.R;
import com.hb.hbsq.adapter.HelpItemAdapter;
import com.hb.hbsq.bean.QAInfo;
import com.hb.hbsq.bean.QAListInfo;
import com.hb.hbsq.common.BaseActivity;
import com.hb.hbsq.engine.QaEngin;
import com.hb.hbsq.util.ToastUtil;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.hb.hbsq.R.id.srf;

/**
 * Created by admin on 2017/2/23.
 */
public class HelpActivityNew extends BaseActivity {


    @BindView(R.id.rcv)
    RecyclerView rcv;


    private HelpItemAdapter adapter;

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
