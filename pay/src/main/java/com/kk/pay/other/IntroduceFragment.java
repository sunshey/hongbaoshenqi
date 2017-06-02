package com.kk.pay.other;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.kk.pay.R;

/**
 * Created by wanglin  on 2017/6/1 16:51.
 */

public class IntroduceFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        View view = View.inflate(getActivity(), R.layout.dialog_introduce, null);
        initView(view);
        return view;
    }

    private void initView(final View view) {
        final ViewPager viewpager = (ViewPager) view.findViewById(R.id.viewpager);
        IntroduceAdapter adapter = new IntroduceAdapter();
        viewpager.setAdapter(adapter);
        adapter.setListener(new IntroduceAdapter.onCloseListener() {
            @Override
            public void onClose() {
                dismiss();
            }

            @Override
            public void onNext(int position) {
                viewpager.setCurrentItem(position);
            }

        });
    }
}
