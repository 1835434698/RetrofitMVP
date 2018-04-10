package com.cnepay.android.swiper.widget;

import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.OverScroller;

import com.cnepay.android.swiper.R;

/**
 * created by millerJK on time : 2017/4/28
 * description :
 */
public class StickyNavLayout extends LinearLayout implements NestedScrollingParent {

    private static final String TAG = "StickyNavLayout";
    private static final int DEFAULT_DURATION = 300;
    private boolean isDown;

    private View mGridView;
    private View mScrollView;
    private CirView mTopView;
    private int mTopViewHeight;

    private OverScroller mScroller;
    private VelocityTracker mVelocityTracker;


    public StickyNavLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);

        mScroller = new OverScroller(context);
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTopView = (CirView) findViewById(R.id.home_page_cirView);
        View gridView = findViewById(R.id.home_page_gridView);
        View scrollView = findViewById(R.id.home_page_scroll);
        mScrollView = scrollView;
        mGridView = gridView;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewGroup.LayoutParams params = mScrollView.getLayoutParams();
        params.height = getMeasuredHeight() - mGridView.getHeight();
        mScrollView.setLayoutParams(params);
//        Log.e(TAG, "onMeasure: " + mTopView.getMeasuredHeight() + "    " + mTopView.getMeasuredHeight());
        setMeasuredDimension(getMeasuredWidth(), mTopView.getMeasuredHeight()
                + mGridView.getMeasuredHeight() + mScrollView.getHeight());
//        Log.e(TAG, "onMeasure: StickyNavLayout   " + getMeasuredHeight());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTopViewHeight = mTopView.getMeasuredHeight();
    }


    public void fling(int velocityY) {
        mScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, 0);
        invalidate();
    }

    /**
     * update cirView
     *
     * @see CirView
     */
    private void updateCirView() {
        float percentage = 1 - 1.0f * getScrollY() / mTopViewHeight;
        mTopView.setScaleX(percentage);
        mTopView.setScaleY(percentage);
        mTopView.setTranslationY(getScrollY() / 2);
        mTopView.setAlpha(percentage);
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > mTopViewHeight) {
            y = mTopViewHeight;
        }
        if (y != getScrollY()) {
            super.scrollTo(x, y);
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            updateCirView();
            if (onScrollListener != null) {
                onScrollListener.onScrollListener(getScrollY());
            }
            invalidate();
        }
    }

    private void smoothToScroll(boolean isDown) {

        if (isDown && getScrollY() <= mTopViewHeight / 3 * 2) {
            mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), DEFAULT_DURATION);
        } else if (isDown && getScrollY() > mTopViewHeight / 3 * 2) {
            mScroller.startScroll(0, getScrollY(), 0, mTopViewHeight - getScrollY(), DEFAULT_DURATION);
        } else if (!isDown && getScrollY() < mTopViewHeight / 3) {
            mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), DEFAULT_DURATION);
        } else if (!isDown && getScrollY() >= mTopViewHeight / 3) {
            mScroller.startScroll(0, getScrollY(), 0, mTopViewHeight - getScrollY(), DEFAULT_DURATION);
        }
        invalidate();
    }


    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {

        boolean hiddenTop = dy > 0 && getScrollY() < mTopViewHeight;
        boolean showTop = dy < 0 && getScrollY() >= 0 && !ViewCompat.canScrollVertically(target, -1);

        if (Math.abs(dy) >= 5 && dy > 0) {
            isDown = false;
        } else if (Math.abs(dy) >= 5 && dy < 0) {
            isDown = true;
        }
        if (onScrollListener != null) {
            onScrollListener.onScrollListener(getScrollY());
        }
        if (hiddenTop || showTop) {
            scrollBy(0, dy);
            if (getScrollY() > 0) {
                updateCirView();
            }
            consumed[1] = dy;
        }
    }


    @Override
    public void onStopNestedScroll(View target) {
        smoothToScroll(isDown);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return true;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        if (getScrollY() >= mTopViewHeight) return false;
        fling((int) velocityY);
        return true;
    }


    @Override
    public int getNestedScrollAxes() {
        return 0;
    }

    OnScrollListener onScrollListener;

    public interface OnScrollListener {
        void onScrollListener(float distance);
    }

    public void setOnScrollListener(OnScrollListener listener) {
        this.onScrollListener = listener;
    }

    /**
     * scroll up to top
     */
    public void pushUp() {

        if (getScrollY() != 0)
            return;

        mScroller.startScroll(0, 0, 0, mTopViewHeight, DEFAULT_DURATION);
        invalidate();
    }

    /**
     * scroll down to bottom
     */

    public void pullDown() {

        if (getScrollY() != mTopViewHeight)
            return;

        mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), DEFAULT_DURATION);
        invalidate();
    }


}
