package com.cnepay.android.swiper.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.cnepay.android.swiper.R;

/**
 * created by millerJK on time : 2017/5/8
 * description :热门活动
 */

public class HotEventView extends View {

    private Bitmap mBitmap;
    private Paint mPaint, mTextPaint;
    private boolean hasEvent;
    private String text = "热门活动";
    private Rect mRect;
    private int picWidth, picHeight;
    private int distance;

    public HotEventView(Context context) {
        this(context, null);
    }

    public HotEventView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HotEventView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.hot);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mRect = new Rect();
        mTextPaint = new Paint();
        mTextPaint.setTextSize(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setAntiAlias(true);
        mTextPaint.getTextBounds(text, 0, text.length(), mRect);

        picWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 13
                , getResources().getDisplayMetrics());
        picHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8
                , getResources().getDisplayMetrics());

        distance = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3
                , getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = mRect.width() + picWidth + distance * 2;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawText(canvas);
        if (!hasEvent) {
            drawPic(canvas);
        }

    }

    private void drawText(Canvas canvas) {
        canvas.drawText(text, 0, getMeasuredHeight() / 2 + mRect.height() / 2, mTextPaint);
    }

    private void drawPic(Canvas canvas) {
        canvas.drawBitmap(mBitmap, null, new RectF(mRect.width() + distance
                , getMeasuredHeight() / 2 - mRect.height() / 2, mRect.width() + picWidth + distance
                , getMeasuredHeight() / 2 - mRect.height() / 2 + picHeight), mPaint);
    }

    public void hasNewMessage(boolean isNew) {
        hasEvent = isNew;
        invalidate();
    }

}
