package com.igeek.hfrecyleviewlib;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.igeek.hfrecyleviewlib.swipe.SwipeMenuRecyclerView;

/**
 * recycleView滚动添加刷新和加载
 */
public abstract class RecycleScrollListener extends RecyclerView.OnScrollListener {

    private static final String TAG = "RecyclerScrollListener";

    private int firstVisibleItemPosition;
    private int lastVisibleItemPosition;
    private int visitCount;
    private int itemCount;
    private boolean isRefresh;
    private boolean isLoading;


    public RecycleScrollListener() {
        super();
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        itemCount = recyclerView.getLayoutManager().getItemCount();
        visitCount = recyclerView.getLayoutManager().getChildCount();
        CommonAdapter adapter = (CommonAdapter) recyclerView.getAdapter();

        if (newState == RecyclerView.SCROLL_STATE_IDLE) {

            if (!isRefresh && firstVisibleItemPosition == 0 && visitCount > 0
                    && lastVisibleItemPosition != itemCount - 1) {
                isRefresh = true;
                refresh();
            }

            if (visitCount > 0 && lastVisibleItemPosition == itemCount - 1
                    && adapter.getLoadMoreCount() != 0 && adapter.isLoadFinish) {
                if (adapter.isNeedLoadMore && recyclerView instanceof SwipeMenuRecyclerView) {
                    if (((SwipeMenuRecyclerView) recyclerView).isCanLoadMore()) {
                        adapter.resetMoreView(true);
                        isLoading = true;
                        loadMore();
                    }

                } else {
                    adapter.resetMoreView(false);
                }
            }
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
            firstVisibleItemPosition = manager.findFirstCompletelyVisibleItemPosition();
            lastVisibleItemPosition = manager.findLastVisibleItemPosition();
        } else if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager manager = (GridLayoutManager) layoutManager;
            firstVisibleItemPosition = manager.findFirstCompletelyVisibleItemPosition();
            lastVisibleItemPosition = manager.findLastVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) layoutManager;
            int[] positions = new int[manager.getSpanCount()];
            positions = manager.findFirstCompletelyVisibleItemPositions(positions);
//            String fristPosition=BuildIntArraysToStr(positions);
            firstVisibleItemPosition = findMinValue(positions);
            positions = manager.findLastVisibleItemPositions(positions);
//            String lastPosition=BuildIntArraysToStr(positions);
            lastVisibleItemPosition = findMaxValue(positions);
        }

    }

    public int findMinValue(int[] values) {

        int minVal = values[0];
        for (Integer value : values) {
            if (minVal > value)
                minVal = value;
        }

        return minVal;
    }

    public int findMaxValue(int[] values) {

        int maxVal = values[0];
        for (Integer value : values) {
            if (maxVal <= value)
                maxVal = value;
        }

        return maxVal;
    }


    public abstract void loadMore();

    /**
     * maybe use sometimes
     */
    public void refresh() {

    }

}
