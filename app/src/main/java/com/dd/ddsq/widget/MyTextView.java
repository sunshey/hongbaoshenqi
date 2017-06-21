package com.dd.ddsq.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.dd.ddsq.R;
import com.dd.ddsq.util.UIUtils;

/**
 * Created by wanglin  on 2017/6/16 14:19.
 */

public class MyTextView extends AppCompatTextView {
    public MyTextView(Context context) {
        super(context);
    }


    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = getPaint();

        paint.setColor(getResources().getColor(R.color.a74));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrikeThruText(true);
        float width = getWidth();

        for (float i = 0; i < width; i++) {

            paint.setStrokeWidth(UIUtils.dip2px(getContext(), 2));
        }

        paint.setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

        float heigh = getHeight();
        canvas.drawLine(UIUtils.dip2px(getContext(), -5), heigh / 2, width + UIUtils.dip2px(getContext(), 5), heigh / 2, paint);
    }
}
