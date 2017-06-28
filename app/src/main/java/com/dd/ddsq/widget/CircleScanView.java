package com.dd.ddsq.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;

import com.dd.ddsq.util.LogUtil;

/**
 * Created by admin on 2017/2/24.
 */

public class CircleScanView extends View {
    private float mWidth;

    private float mHeight;

    private Paint mPaint, mPaint2;

    private RectF mRectF;

    private float startAngle = 0;

    private float radius;
    private Path path;
    private static final int START = 1;

    public CircleScanView(Context context) {
        this(context, null);
    }

    public CircleScanView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleScanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();

    }

    private void initData() {
        mPaint = new Paint();

        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor("#99ffffff"));

        mPaint2 = new Paint();
        mPaint2.setStyle(Paint.Style.FILL);
        mPaint2.setColor(Color.parseColor("#00ff00"));

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getWidth();

        mHeight = getHeight();
        LogUtil.msg("mWidth  " + mWidth + "  mHeight" + mHeight);
        mRectF = new RectF((float) (mWidth * 0.1), (float) (mWidth * 0.1),
                (float) (mWidth * 0.9), (float) (mWidth * 0.9));
        // 绘制渐变效果

        LinearGradient gradient = new LinearGradient((float) (mWidth * 0.3),
                (float) (mWidth * 0.9), (float) (mWidth * 0.1),
                (float) (mWidth * 0.5), new int[]{
                Color.parseColor("#FC7558"), Color.parseColor("#FC7A5F"),
                Color.parseColor("#FC836A"), Color.parseColor("#FD907B"),
                Color.parseColor("#FDA596"), Color.parseColor("#F9BAA4"), Color.parseColor("#EDBBB5")}, null,
                Shader.TileMode.CLAMP);

        mPaint2.setShader(gradient);


        // 圆的半径
        radius = (float) (mWidth * 0.4);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvasCircle(canvas);
        canvasArc(canvas);

    }

    // 绘制圆
    private void canvasCircle(Canvas canvas) {


        canvas.drawCircle(mWidth / 2, mHeight / 2, radius, mPaint);

    }


    // 绘制旋转的扇形
    private void canvasArc(Canvas canvas) {
        canvas.drawArc(mRectF, startAngle, 60, true, mPaint2);

    }

    /**
     * 开启一个线程
     *
     * @author Administrator
     */
    private class MyThread extends Thread {

        @Override
        public void run() {

            while (true) {
                if (running) {
                    SystemClock.sleep(10);
                    handler.sendEmptyMessage(START);
                } else {
                    break;
                }
            }

        }
    }

    private boolean running = true;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            synchronized (this) {
                switch (msg.what) {
                    case START:
                        startAngle++;
                        if (startAngle == 360) {
                            startAngle = 0;
                        }
                        invalidate();
                        break;
                }

            }
        }
    };

    private MyThread thread;

    // 开启动画
    public void setStartAngle() {
        thread = new MyThread();
        thread.start();

    }

    // 重新开启动画
    public void startAnge() {
        running = true;
    }

    // 暂停动画
    public void stopAnge() {
        running = false;

    }

    // 是否在运动
    public boolean isRunning() {
        return running;
    }

}
