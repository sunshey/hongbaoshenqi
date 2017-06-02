package com.hb.hbsq.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.hb.hbsq.R;

import butterknife.ButterKnife;

/**
 * Created by admin on 2017/2/27.
 */

public abstract class BaseDialogFragment extends DialogFragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        View view = inflater.inflate(getLayoutId(), null);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View v) {
        if (isNeedTitle()) {
            TextView tvTitle = (TextView) v.findViewById(R.id.tv_title);
            ImageView ivClose = (ImageView) v.findViewById(R.id.iv_close);
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isVisible())
                        dismiss();
                }
            });

            initTitle(tvTitle, ivClose);
        }
        initData();
    }

    protected abstract void initData();


    protected abstract void initTitle(TextView tvTitle, ImageView ivClose);

    protected abstract int getLayoutId();

    protected abstract boolean isNeedTitle();

}
