package com.igeek.hfrecyleviewlib.refresh;

public class PullViewHelper {

    //下拉视图的高度
    private int pullViewHeight;
    //下拉的最大高度
    private int pullMaxHeight;
    //下拉视图的最小高度
    private int pullMinHeight;
    //刷新的临界高度
    private int pullRefreshHeight;
    //阻尼因子
    private float SCROLL_RATIO = 0.5f;
    private boolean mIsInTouch = false;
    private float mScroll = 0;
    private int mMaxScroll = 0;
    private int mMinScroll = 0;
    private float refreshPercentage;

    public PullViewHelper(int height, int maxHeight, int minHeight, int refreshHeight) {
        pullViewHeight = Math.max(0, height);
        pullMinHeight = Math.max(0, minHeight);
        pullMaxHeight = Math.max(pullViewHeight, maxHeight);
        pullRefreshHeight = Math.max(pullViewHeight, refreshHeight);

        mScroll = 0;
        mMaxScroll = 0;
        mMinScroll = -pullMaxHeight;

        refreshPercentage = pullRefreshHeight * (1.0F) / pullMaxHeight;
    }

    public int getMaxHeight() {
        return pullMaxHeight;
    }

    public int getMinHeight() {
        return pullMinHeight;
    }

    public int getHeight() {
        return pullViewHeight;
    }

    public int getScroll() {
        return (int) mScroll;
    }

    public int getMaxScroll() {
        return mMaxScroll;
    }

    public int getMinScroll() {
        return mMinScroll;
    }

    public boolean isInTouch() {
        return mIsInTouch;
    }

    public boolean canScrollDown() {
        return mScroll > mMinScroll;
    }

    public boolean canScrollUp() {
        return mScroll < mMaxScroll;
    }

    public int checkUpdateScroll(int deltaY) {

        float willTo;

        float consumed = deltaY;

        if (deltaY > 0) {
            willTo = mScroll + deltaY;
            if (willTo > mMaxScroll) {
                willTo = mMaxScroll;
                consumed = willTo - mMaxScroll;
            }
        } else {
            willTo = mScroll + deltaY * SCROLL_RATIO;

//            final float pullHeight=Math.abs(willTo);
//            float dragPercent = Math.min(1f, pullHeight / pullViewHeight);
//            float tensionSlingshotPercent = Math.max(0, Math.min(pullHeight - pullViewHeight,pullMaxHeight-pullViewHeight) / pullViewHeight);
//            float tensionPercent = (float) ((tensionSlingshotPercent / 4) -Math.pow((tensionSlingshotPercent / 4), 2)) * 2f;
//            float extraMove = (pullViewHeight) * tensionPercent * 2;
//            willTo = -((pullViewHeight * dragPercent) + extraMove);

            if (willTo < mMinScroll) willTo = mMinScroll;

            consumed = willTo - mScroll;
        }
        mScroll = willTo;
        return (int) consumed;
    }

    public float getScrollPercent() {
        return  -mScroll / pullRefreshHeight;
    }

    public boolean canTouchUpToRefresh() {
        return -mScroll / pullMaxHeight >= refreshPercentage;
    }

    public int getPullRefreshHeight() {
        return pullRefreshHeight;
    }

    public void setScroll(float mScroll) {
        this.mScroll = mScroll;
    }
}
