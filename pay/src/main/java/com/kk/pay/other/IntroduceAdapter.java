package com.kk.pay.other;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kk.pay.R;

/**
 * Created by wanglin  on 2017/6/1 16:55.
 */

public class IntroduceAdapter extends PagerAdapter {

    private String[] titles = new String[]{
            "点击下图所示【保存二维码到相册】按钮，跳转至微信",
            "进入微信首页，点击右上角+号，进入到微信扫一扫",
            "进入到扫码后点击右上角选项按钮，如图所示，找到【从相册选取二维码】，点击",
            "进入到手机本地相册，找到之前保存的二维码图片，点击就会自动识别图片进行支付",
            "识别二维码成功，如下图所示，发起微信二维码支付"
    };

    private int[] ivs = new int[]{
            R.drawable.t1, R.drawable.t2, R.drawable.t3, R.drawable.t4, R.drawable.t5};


    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = View.inflate(container.getContext(), R.layout.introduce_item, null);
        TextView tv = (TextView) view.findViewById(R.id.tv);
        ImageView iv = (ImageView) view.findViewById(R.id.iv);
        view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClose();
                }
            }
        });
        View nextView = view.findViewById(R.id.iv_next);
        nextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onNext(position + 1);
                }
            }
        });
        View leftView = view.findViewById(R.id.iv_left);
        leftView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onNext(position - 1);
                }
            }
        });
        if (position == 0) {
            leftView.setVisibility(View.GONE);
        } else {
            leftView.setVisibility(View.VISIBLE);
        }
        if (position == ivs.length - 1) {
            nextView.setVisibility(View.GONE);
        } else {
            nextView.setVisibility(View.VISIBLE);
        }
        tv.setText(titles[position]);
        iv.setImageResource(ivs[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    private onCloseListener listener;

    public onCloseListener getListener() {
        return listener;
    }

    public void setListener(onCloseListener listener) {
        this.listener = listener;
    }

    public interface onCloseListener {
        void onClose();

        void onNext(int position);


    }
}
