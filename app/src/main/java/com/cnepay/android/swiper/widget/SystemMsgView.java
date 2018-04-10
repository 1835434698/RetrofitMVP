package com.cnepay.android.swiper.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.cnepay.android.swiper.R;

/**
 * created by millerJK on time : 2017/5/9
 * description :  消息
 */

public class SystemMsgView extends View {

    private Bitmap msgBitmap, pointBitmap;
    private Paint mPaint;
    private boolean hasMsg;

    public SystemMsgView(Context context) {
        this(context, null);
    }

    public SystemMsgView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SystemMsgView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        msgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sys_msg);
        pointBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sys_msg_point);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 26
                , getResources().getDisplayMetrics());
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25
                , getResources().getDisplayMetrics());

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawMsg(canvas);
        if (!hasMsg) {
            drawPoint(canvas);
        }
    }

    private void drawMsg(Canvas canvas) {

        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 23
                , getResources().getDisplayMetrics());
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 22
                , getResources().getDisplayMetrics());

        canvas.drawBitmap(msgBitmap, null, new RectF(0, 0, width, height), mPaint);
    }

    private void drawPoint(Canvas canvas) {

        int pointSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 9
                , getResources().getDisplayMetrics());

        canvas.drawBitmap(pointBitmap, null, new RectF(getMeasuredWidth() - pointSize
                , 0, getMeasuredWidth(), pointSize), mPaint);
    }

    public void hasNewMessage(boolean isNew) {
        hasMsg = isNew;
        invalidate();
    }


}
