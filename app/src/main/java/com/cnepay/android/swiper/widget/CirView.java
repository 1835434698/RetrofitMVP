package com.cnepay.android.swiper.widget;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Scroller;

import com.cnepay.android.swiper.R;

/**
 * created by millerJK on time : 2017/4/22
 * description :
 */

public class CirView extends View {

    private static final String TAG = "CirView";

    private Context context;
    private Point centerPoint;
    private Point currentPoint;
    private boolean isScrolling;

    private GestureDetector mDetector;
    private int radius;
    private MyGestureListener gestureListener;
    private Scroller mScroller;

    public static final int LEFT_TOP = 0;
    public static final int RIGHT_TOP = 1;
    public static final int LEFT_BOTTOM = 2;
    public static final int RIGHT_BOTTOM = 3;

    public static final String NOT_OPEN = "待开通";
    public static final String ALREADY_OPEN = "已开通";
    public static final String NOT_BIND = "未绑定";
    public static final String ALREADY_BIND = "已绑定";

    private Paint mCirPaint;
    private Paint mMidCirPaint;
    private Paint mInsCirPaint;
    private Paint mTextPaint;

    private float dis;

    private String mLeftStatus;
    private String mRightStatus;
    private Bitmap mShadowBitmap;


    public CirView(Context context) {
        this(context, null);
    }

    public CirView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CirView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        gestureListener = new MyGestureListener();
        mDetector = new GestureDetector(context, gestureListener);
        mScroller = new Scroller(context);

        mCirPaint = new Paint();
        mCirPaint.setColor(Color.parseColor("#E44036"));
        mCirPaint.setAntiAlias(true);
        mCirPaint.setDither(true);
        mCirPaint.setStrokeWidth(4);
        mCirPaint.setStyle(Paint.Style.STROKE);

        mMidCirPaint = new Paint();
        mMidCirPaint.setAntiAlias(true);
        mMidCirPaint.setDither(true);
        mMidCirPaint.setColor(Color.WHITE);

        mInsCirPaint = new Paint();
        mInsCirPaint.setAntiAlias(true);
        mInsCirPaint.setDither(true);
        mInsCirPaint.setColor(Color.parseColor("#E44036"));

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);

        mLeftStatus = NOT_OPEN;
        mRightStatus = NOT_BIND;
        mShadowBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.circle_shadow);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = 0;
        int height = 0;

        width = getScreenWidth();
        height = width;

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        centerPoint = new Point(w / 2, w / 2);
        currentPoint = new Point(w / 2, w / 2);
        radius = 2 * w / 5;
        dis = radius * 0.1f;

    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        drawOutCircle(canvas);
        drawInsCircle(canvas);
        drawText(canvas);
        drawMidCircle(canvas);

    }

    private void drawOutCircle(Canvas canvas) {
        if (isScrolling) {
            currentPoint.x = centerPoint.x + gestureListener.xDis;
            currentPoint.y = centerPoint.y + gestureListener.yDis;
        }
        canvas.drawCircle(currentPoint.x, currentPoint.y, radius, mCirPaint);
    }

    private void drawMidCircle(Canvas canvas) {
//        canvas.drawCircle(currentPoint.x, currentPoint.y, radius - dis, mMidCirPaint);

        float midRadius = radius - dis;
        RectF rectF = new RectF(currentPoint.x - midRadius, currentPoint.y
                - midRadius, currentPoint.x + midRadius, currentPoint.y + midRadius);
        canvas.drawBitmap(mShadowBitmap, null,rectF, mMidCirPaint);
    }

    private void drawInsCircle(Canvas canvas) {

        mInsCirPaint.setColor(Color.parseColor("#E44036"));
        RectF rectF = new RectF(currentPoint.x - 0.87f * radius, currentPoint.y - 0.87f * radius
                , currentPoint.x + 0.87f * radius, currentPoint.y + 0.87f * radius);
        mInsCirPaint.setMaskFilter(new BlurMaskFilter(30, BlurMaskFilter.Blur.INNER));
        canvas.drawArc(rectF, 0, -180, true, mInsCirPaint);

        mInsCirPaint.setMaskFilter(null);

        if (mLeftStatus.equals(NOT_OPEN)) {
            mInsCirPaint.setColor(Color.parseColor("#F6F6F6"));
            canvas.drawArc(rectF, 90, 90, true, mInsCirPaint);
        } else {
            mInsCirPaint.setColor(Color.WHITE);
            canvas.drawArc(rectF, 90, 90, true, mInsCirPaint);
        }

        if (mRightStatus.equals(NOT_BIND)) {
            mInsCirPaint.setColor(Color.parseColor("#F6F6F6"));
            canvas.drawArc(rectF, 0, 90, true, mInsCirPaint);
        } else {
            mInsCirPaint.setColor(Color.WHITE);
            canvas.drawArc(rectF, 0, 90, true, mInsCirPaint);
        }
    }

    private void drawText(Canvas canvas) {
        String title, status;
        Rect titleRect = new Rect();
        Rect statusRect = new Rect();

        float mTitleTextSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 25, getResources().getDisplayMetrics());

        //收款
        title = "收款";
        mTextPaint.setTextSize(mTitleTextSize);
        mTextPaint.setStrokeWidth(0);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.getTextBounds(title, 0, title.length(), titleRect);
        canvas.drawText(title, currentPoint.x - titleRect.width() / 2,
                currentPoint.y - radius / 2 + titleRect.height() / 2, mTextPaint);

        /*------------start--------*/
        mTitleTextSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics());
        mTextPaint.setTextSize(mTitleTextSize);
        mTextPaint.setColor(Color.BLACK);


        title = "收款状态";
        mTextPaint.getTextBounds(title, 0, title.length(), titleRect);
        canvas.drawText(title, currentPoint.x - 0.7f * radius / 2 - titleRect.width() / 2,
                currentPoint.y + 0.2f * radius + titleRect.height() / 2, mTextPaint);
        title = "收款设备";
        mTextPaint.getTextBounds(title, 0, title.length(), titleRect);
        canvas.drawText(title, currentPoint.x + 0.7f * radius / 2 - titleRect.width() / 2,
                currentPoint.y + 0.2f * radius + titleRect.height() / 2, mTextPaint);

        if (mLeftStatus.equals(ALREADY_OPEN)) {
            mTextPaint.setColor(Color.parseColor("#FF9346"));
            mTextPaint.setStyle(Paint.Style.FILL);
            mTextPaint.setStrokeWidth(1);
        } else {
            mTextPaint.setColor(Color.parseColor("#E0E0E0"));
            mTextPaint.setStyle(Paint.Style.STROKE);
            mTextPaint.setStrokeWidth(1);
        }

        RectF rectF = new RectF(currentPoint.x - 0.7f * radius / 2 - titleRect.width() / 2,
                currentPoint.y + 0.3f * radius,
                currentPoint.x - 0.7f * radius / 2 + titleRect.width() / 2,
                currentPoint.y + 0.32f * radius + titleRect.height());
        canvas.drawRoundRect(rectF, 30, 30, mTextPaint);

        if (mRightStatus.equals(ALREADY_BIND)) {
            mTextPaint.setColor(Color.parseColor("#FF9346"));
            mTextPaint.setStyle(Paint.Style.FILL);
        } else {
            mTextPaint.setColor(Color.parseColor("#E0E0E0"));
            mTextPaint.setStyle(Paint.Style.STROKE);
        }
        mTextPaint.setStrokeWidth(1);

        RectF rectF1 = new RectF(currentPoint.x + 0.7f * radius / 2 - titleRect.width() / 2,
                currentPoint.y + 0.3f * radius,
                currentPoint.x + 0.7f * radius / 2 + titleRect.width() / 2,
                currentPoint.y + 0.32f * radius + titleRect.height());
        canvas.drawRoundRect(rectF1, 30, 30, mTextPaint);


        mTitleTextSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics());

        status = mLeftStatus;

        if (mLeftStatus.equals(ALREADY_OPEN)) {
            mTextPaint.setColor(Color.WHITE);
        } else {
            mTextPaint.setColor(Color.parseColor("#E0E0E0"));
        }
        mTextPaint.setStrokeWidth(0);
        mTextPaint.setTextSize(mTitleTextSize);
        mTextPaint.getTextBounds(status, 0, status.length(), statusRect);
        canvas.drawText(status, currentPoint.x - 0.7f * radius / 2 - statusRect.width() / 2,
                currentPoint.y + 0.3f * radius + titleRect.height() / 2 + statusRect.height() / 2,
                mTextPaint);

        status = mRightStatus;
        if (mRightStatus.equals(ALREADY_BIND)) {
            mTextPaint.setColor(Color.WHITE);
        } else {
            mTextPaint.setColor(Color.parseColor("#E0E0E0"));
        }
        mTextPaint.getTextBounds(status, 0, status.length(), statusRect);
        canvas.drawText(status, currentPoint.x + 0.7f * radius / 2 - statusRect.width() / 2,
                currentPoint.y + 0.3f * radius + titleRect.height() / 2 + statusRect.height() / 2,
                mTextPaint);

        /*------------end--------*/


        if (mLeftStatus.equals(NOT_OPEN) && mRightStatus.equals(ALREADY_BIND)
                || mLeftStatus.equals(ALREADY_OPEN) && mRightStatus.equals(NOT_BIND))
            return;
        else {
            mTextPaint.setStrokeWidth(2);
            mTextPaint.setColor(Color.parseColor("#EAEAEA"));
            canvas.drawLine(currentPoint.x, currentPoint.y + 0.12f * radius,
                    currentPoint.x, currentPoint.y + 0.5f * radius, mTextPaint);
        }


    }


    public int getScreenWidth() {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                onActionUp();
                break;
        }
        return mDetector.onTouchEvent(event);
    }

    private void onActionUp() {
        if (isScrolling) {
            isScrolling = false;
            smoothScrollTo();
        }
    }


    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClickListener(int type);
    }


    OnScrollListener onScrollListener;

    public void setOnScrollListener(OnScrollListener listener) {
        onScrollListener = listener;
    }


    public interface OnScrollListener {
        void onScrollListener(float x, float y);
    }


    public void smoothScrollTo() {

        mScroller.startScroll(currentPoint.x, currentPoint.y,
                -gestureListener.xDis, -gestureListener.yDis, 1000);
        invalidate();
    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller != null) {
            if (mScroller.computeScrollOffset()) {
                currentPoint.x = mScroller.getCurrX();
                currentPoint.y = mScroller.getCurrY();
                postInvalidate();
            }
        }
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        float xDiff, yDiff;
        int xDis, yDis;
        int type;


        public double getDistance(MotionEvent e) {

            xDiff = Math.abs(e.getX() - centerPoint.x);
            yDiff = Math.abs(e.getY() - centerPoint.y);
            return Math.sqrt(xDiff * xDiff + yDiff * yDiff);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {

            if (getDistance(e) <= radius) {
                if (onItemClickListener != null) {
                    if (e.getY() <= centerPoint.y) {
                        if (e.getX() <= centerPoint.x)
                            type = LEFT_TOP;
                        else
                            type = RIGHT_TOP;
                    } else {
                        if (e.getX() <= centerPoint.x)
                            type = LEFT_BOTTOM;
                        else
                            type = RIGHT_BOTTOM;
                    }

                    if (!isScrolling) {
                        onItemClickListener.onItemClickListener(type);
                    }

                }
            }
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onDown(MotionEvent ev) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//            if (getDistance(e1) <= radius) {
//                isScrolling = false;
//                smoothScrollTo();
//            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        /**
         * @param e1        The user first presses the point coordinates(坐标)
         * @param e2        The coordinates of the points moved each time
         * @param distanceX Relative to the last point of movement
         * @param distanceY
         * @return
         */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            //首次按压点在圆以内才会触发滑动监听
            if (getDistance(e1) <= radius) {
                isScrolling = true;
//                Log.e(TAG, "onScroll: " + e1.getX() + "  " + e1.getY() + "   " + e2.getX()
//                        + "  " + e2.getY() + "    " + distanceX + "  " + distanceY);
                xDis = (int) (e2.getX() - e1.getX());
                yDis = (int) (e2.getY() - e1.getY());
                if (Math.abs(yDis) >= getHeight() / 2 - radius) {
                    if (yDis > 0)
                        yDis = getHeight() / 2 - radius;
                    else
                        yDis = radius - getHeight() / 2;
                }

                if (Math.abs(xDis) >= getWidth() / 2 - radius) {
                    if (xDis > 0)
                        xDis = getWidth() / 2 - radius;
                    else
                        xDis = radius - getHeight() / 2;
                }
                if (onScrollListener != null) {
                    onScrollListener.onScrollListener(e2.getX(), e2.getY());
                }
                invalidate();
            }
            return true;
        }
    }

    /**
     * @param position only can {@link #LEFT_BOTTOM} {@link #RIGHT_BOTTOM}
     * @param status   content {@link #NOT_OPEN} {@link #ALREADY_OPEN}{@link #NOT_BIND}{@link #ALREADY_BIND}
     */
    public void updateStatus(int position, String status) {

        if (position != LEFT_BOTTOM && position != RIGHT_BOTTOM)
            throw new IllegalArgumentException("parameter position must be LEFT_BOTTOM " +
                    "or RIGHT_BOTTOM");

        if (status != NOT_OPEN && status != NOT_BIND
                && status != ALREADY_OPEN && status != ALREADY_BIND)
            throw new IllegalArgumentException("parameter position must be NOT_OPEN " +
                    "or ALREADY_OPEN or NOT_BIND or ALREADY_BIND");

        if (position == LEFT_BOTTOM) {
            mLeftStatus = status;
        } else {
            mRightStatus = status;
        }
        invalidate();

    }

    public float getRadius(){
        return radius;
    }


}
