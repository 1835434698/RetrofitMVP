package com.igeek.hfrecyleviewlib;


import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.igeek.hfrecyleviewlib.CommonViewHolder.OnItemClickListener;
import com.igeek.hfrecyleviewlib.CommonViewHolder.OnItemLongClickListener;
import com.igeek.hfrecyleviewlib.swipe.OnSwipeMenuItemClickListener;
import com.igeek.hfrecyleviewlib.swipe.SwipeMenu;
import com.igeek.hfrecyleviewlib.swipe.SwipeMenuCreator;
import com.igeek.hfrecyleviewlib.swipe.SwipeMenuLayout;
import com.igeek.hfrecyleviewlib.swipe.SwipeMenuView;
import com.igeek.hfrecyleviewlib.utils.WrapperUtils;

import java.util.ArrayList;
import java.util.List;


public abstract class CommonAdapter<T> extends RecyclerView.Adapter<CommonViewHolder> {

    private static final String TAG = "CommonAdapter";

    private static final int BASE_ITEM_TYPE_HEADER = 100000;
    private static final int BASE_ITEM_TYPE_FOOTER = 200000;
    public static final int ITEM_TYPE_LOAD_MORE = Integer.MAX_VALUE - 2;
    public static final int ITEM_TYPE_NO_DATA = Integer.MAX_VALUE - 3;

    public OnItemClickListener clickListener;
    public OnItemLongClickListener longClickListener;

    private RecyclerView mRecyclerView;
    //数据集
    public List<T> datas = new ArrayList<T>();
    private SparseArray<View.OnClickListener> subViewListeners = new SparseArray<>(0);
    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    private SparseArrayCompat<View> mFootViews = new SparseArrayCompat<>();
    private View mLoadView, mNoDataView;
    //默认swipeMenu 关闭
    private SwipeMenuCreator mSwipeMenuCreator;

    private OnSwipeMenuItemClickListener mSwipeMenuItemClickListener;


    public boolean isNeedLoadMore = true;
    public boolean isLoadFinish = true;
    private boolean hasLoadMore;


    @Override
    public int getItemCount() {
        return datas.size() + getHeadCount() + getFootCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (position < getHeadCount()) {
            return mHeaderViews.keyAt(position);
        }
        if (mNoDataView != null && datas.size() == 1
                && datas.get(0) == null && position == getHeadCount()) {
            return ITEM_TYPE_NO_DATA;
        }
        if (hasLoadMore && datas.size() != 0 && position == getItemCount() - 1) {
            return ITEM_TYPE_LOAD_MORE;
        }
        if (position >= datas.size() + getHeadCount()) {
            return mFootViews.keyAt(position - datas.size() - getHeadCount());
        }
        return getDataItemViewType(position - getHeadCount());
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Log.e(TAG, "onCreateViewHolder");
        CommonViewHolder viewHolder;

        if (mHeaderViews.get(viewType) != null) {  //head
            LayoutParams lp = new LayoutParams(parent.getMeasuredWidth(), LayoutParams.WRAP_CONTENT);
            mHeaderViews.get(viewType).setLayoutParams(lp);
            viewHolder = createHeaderViewHolder(mHeaderViews.get(viewType));
        } else if (mFootViews.get(viewType) != null) { // foot
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            mFootViews.get(viewType).setLayoutParams(lp);
            viewHolder = createFooterViewHolder(mFootViews.get(viewType));
        } else if (viewType == ITEM_TYPE_LOAD_MORE) {//load more
            mLoadView = View.inflate(parent.getContext(), R.layout.layout_listbottom_loadingview, null);
            viewHolder = new CommonViewHolder(mLoadView);
        } else if (viewType == ITEM_TYPE_NO_DATA) {//no data view
            viewHolder = new CommonViewHolder(mNoDataView);
        } else { //data
            View contentView = onCreateNormalView(parent, viewType);
            if (mSwipeMenuCreator != null) {
                SwipeMenuLayout swipeMenuLayout = (SwipeMenuLayout) LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.swipe_item_default, parent, false);
                SwipeMenu swipeLeftMenu = new SwipeMenu(swipeMenuLayout, viewType);
                SwipeMenu swipeRightMenu = new SwipeMenu(swipeMenuLayout, viewType);

                mSwipeMenuCreator.onCreateMenu(swipeLeftMenu, swipeRightMenu, viewType);

                int leftMenuCount = swipeLeftMenu.getMenuItems().size();
                if (leftMenuCount > 0) {
                    SwipeMenuView swipeLeftMenuView = (SwipeMenuView) swipeMenuLayout.findViewById(R.id.swipe_left);
                    //Certainly is one of HORIZONTAL and VERTICAL, so do not care about the red line
                    swipeLeftMenuView.setOrientation(swipeLeftMenu.getOrientation());
                    swipeLeftMenuView.bindMenu(swipeLeftMenu, SwipeMenuView.LEFT_DIRECTION);
                    swipeLeftMenuView.bindMenuItemClickListener(mSwipeMenuItemClickListener, swipeMenuLayout);
                }

                int rightMenuCount = swipeRightMenu.getMenuItems().size();
                if (rightMenuCount > 0) {
                    SwipeMenuView swipeRightMenuView = (SwipeMenuView) swipeMenuLayout.findViewById(R.id.swipe_right);
                    swipeRightMenuView.setOrientation(swipeRightMenu.getOrientation());
                    swipeRightMenuView.bindMenu(swipeRightMenu, SwipeMenuView.RIGHT_DIRECTION);
                    swipeRightMenuView.bindMenuItemClickListener(mSwipeMenuItemClickListener, swipeMenuLayout);
                }

                if (leftMenuCount > 0 || rightMenuCount > 0) {
                    ViewGroup viewGroup = (ViewGroup) swipeMenuLayout.findViewById(R.id.swipe_content);
                    viewGroup.addView(contentView);
                    contentView = swipeMenuLayout;
                }
            }
            viewHolder = createNormalViewHolder(contentView, viewType);
        }

        if (viewHolder != null && viewType != ITEM_TYPE_NO_DATA) {
            viewHolder.setOnItemClickListener(clickListener);
            viewHolder.setOnItemLongClickListener(longClickListener);
            updateSubViewClickEvent(viewHolder, subViewListeners);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {
//        Log.e(TAG, "onBindViewHolder");
        final int type = getItemViewType(position);

        if (mHeaderViews.get(type) != null) {
            bindHeadViewHolder(holder);
        } else if (mFootViews.get(type) != null) {
            bindFootViewHolder(holder);
        } else if (type == ITEM_TYPE_LOAD_MORE) {

        } else if (type == ITEM_TYPE_NO_DATA) {

        } else {
            View itemView = holder.itemView;
            if (itemView instanceof SwipeMenuLayout) {
                SwipeMenuLayout swipeMenuLayout = (SwipeMenuLayout) itemView;
                int childCount = swipeMenuLayout.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View childView = swipeMenuLayout.getChildAt(i);
                    if (childView instanceof SwipeMenuView) {
                        ((SwipeMenuView) childView).bindAdapterViewHolder(holder);
                    }
                }
            }
            bindNormalViewHolder(holder, position - getHeadCount(), type);
        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        // 最先被调用，在getItemCount之前
        mRecyclerView = recyclerView;

        WrapperUtils.onAttachedToRecyclerView(recyclerView, new WrapperUtils.SpanSizeCallback() {
            @Override
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
                if (isInHeadRange(position) || isInFootRange(position)) {
                    return layoutManager.getSpanCount();
                }
                if (oldLookup != null) {
                    return oldLookup.getSpanSize(position);
                }
                return 1;
            }
        });
    }

    @Override
    public void onViewAttachedToWindow(CommonViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        //每一个Item添加到RecyclerView上将会调用一次该方法
        LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            boolean needFull = isInHeadRange(holder.getLayoutPosition()) || isInFootRange(holder.getLayoutPosition());
            ((StaggeredGridLayoutManager.LayoutParams) lp).setFullSpan(needFull);
        }

    }

    public void updateSubViewClickEvent(CommonViewHolder viewHolder, SparseArray<View.OnClickListener> subViewListeners) {
        for (int index = 0; index < subViewListeners.size(); index++) {
            int key_subResId = subViewListeners.keyAt(index);
            viewHolder.setSubViewClickListener(key_subResId, subViewListeners.get(key_subResId));
        }
    }

    /**
     * 获取每种数据结构的视图type
     *
     * @param position 这里的位置相对于headView的位置减少1(如果headView不为空的话)
     * @return 返回给定数据结构所需展现视图的类型
     */
    public abstract int getDataItemViewType(int position);

    public CommonViewHolder createHeaderViewHolder(View headView) {
        return null;
    }

    public abstract View onCreateNormalView(ViewGroup parent, int viewType);

    public abstract CommonViewHolder createNormalViewHolder(View realContentView, int viewType);

    public CommonViewHolder createFooterViewHolder(View footView) {
        return null;
    }


    public void bindHeadViewHolder(CommonViewHolder holder) {
    }

    public abstract void bindNormalViewHolder(CommonViewHolder holder, int position, int viewType);

    public void bindFootViewHolder(CommonViewHolder holder) {
    }


    /**
     * 获取所有的数据集合
     *
     * @return
     */
    public List<T> getDatas() {
        return datas;
    }

    /**
     * 获取指定位置的数据
     */
    public T getData(int position) {
        if (position < getHeadCount() || position > getItemCount() - getFootCount())
            return null;
        else
            return datas.get(position);
    }

    /**
     * get item position except head and foot
     *
     * @param adapterPosition
     * @return
     */
    public int getPosition(int adapterPosition) {
        return adapterPosition - getHeadCount();
    }

    /**
     * 清除所有的数据
     */
    public void clear() {
        datas.clear();
        notifyItemRangeRemoved(0 + getHeadCount(), datas.size());
    }

    /**
     * reset data
     *
     * @param list
     */
    public void resetDatas(List<T> list) {
        if (list != null) {
            datas.clear();
            datas.addAll(list);
            if (mLoadView != null) {
                onLoadComplete();
            }
            if (mLoadView != null && !hasLoadMore) {
                hasLoadMore = true;
            }
            if (mNoDataView != null && datas.size() == 0) {
                datas.add(null);
            }
            notifyDataSetChanged();
        }

    }


    /**
     * @param position index position
     * @param data
     */
    public void insertData(int position, T data) {
        if (position >= 0 && position < getItemCount() - getFootCount() && data != null) {
            this.datas.add(position, data);
            notifyItemInserted(position + getHeadCount());
        } else
            throw new IllegalArgumentException();
    }

    /**
     * @param position index position
     * @param list
     */
    public void insertDatas(int position, List<T> list) {
        if (position >= 0 && position < getItemCount() - getFootCount() && list != null) {
            datas.addAll(position, list);
            notifyItemRangeInserted(position + getHeadCount(), list.size());
        } else
            throw new IllegalArgumentException();
    }

    /**
     * append single data
     *
     * @param data
     */
    public void appendData(T data) {
        if (data != null) {
            final int oldSize = datas.size();
            datas.add(data);
            notifyItemInserted(oldSize + getHeadCount());
        }
    }

    /**
     * append array datas
     *
     * @param list
     */
    public void appendDatas(List<T> list) {

        if (list != null && list.size() > 0) {
            datas.addAll(list);
            int offset = getHeadCount();
            notifyItemRangeInserted(datas.size() - list.size() + offset, list.size());
        }
    }

    /**
     * @param position
     */
    public void removeData(int position) {
        if (position >= 0 && position < getItemCount() - getFootCount()) {
            final int offest = getHeadCount();
            datas.remove(position);
            if (datas.size() == 0 && mNoDataView != null) {
                datas.add(null);
                if (hasLoadMore)
                    hasLoadMore = false;
            }
            notifyItemRemoved(position + offest);
            Log.e(TAG, "removeData: ");
        }
    }

    /**
     * @param position   index of headView
     * @param visibility {@link View#VISIBLE} {@link View#INVISIBLE}  {@link View#GONE}
     */
    public void setHeadVisibility(int position, int visibility) {
        View headView = mHeaderViews.get(position);
        if (headView != null && headView.getVisibility() != visibility) {
            headView.setVisibility(visibility);
            notifyItemChanged(position);
        }
    }

    /**
     * @param position   index of footView
     * @param visibility {@link View#VISIBLE} {@link View#INVISIBLE}  {@link View#GONE}
     */
    public void setFootVisibility(int position, int visibility) {
        View footView = mFootViews.get(position);
        if (footView != null && footView.getVisibility() != visibility) {
            footView.setVisibility(visibility);
            notifyItemChanged(datas.size() + getHeadCount());
        }
    }

    /**
     * @param position the position need to be changed in the collection  {@link #mHeaderViews}
     * @param newView  new view
     */
    public void updateHeadView(int position, View newView) {

        View headView = mHeaderViews.get(position);

        if (headView == null)
            throw new NullPointerException("the view which need updated is not exist !");

        if (newView == null || newView == headView)
            return;

        mHeaderViews.setValueAt(position, newView);
        notifyItemChanged(position);
    }

    /**
     * @param position the position need to be changed in the collection  {@link #mFootViews}
     * @param newView  new view
     */
    public void updateFootView(int position, View newView) {

        View footView = mFootViews.get(position);

        if (footView == null)
            throw new NullPointerException("the view which need updated is not exist !");

        if (newView == null || newView == footView)
            return;

        mFootViews.setValueAt(position, newView);
        notifyItemChanged(datas.size() + getHeadCount() + position);
    }

    /**
     * do not use this method
     */
    public void resetMoreView(boolean flag) {

        isLoadFinish = false;

        if (flag) {
            mLoadView.findViewById(R.id.loading_progress).setVisibility(View.VISIBLE);
            mLoadView.findViewById(R.id.loading_more).setVisibility(View.GONE);
            mLoadView.findViewById(R.id.loading_no_data).setVisibility(View.GONE);
        } else {
            mLoadView.findViewById(R.id.loading_progress).setVisibility(View.GONE);
            mLoadView.findViewById(R.id.loading_more).setVisibility(View.GONE);
            mLoadView.findViewById(R.id.loading_no_data).setVisibility(View.VISIBLE);
        }


    }

    public void onLoadComplete() {
        isLoadFinish = true;
        if (mLoadView == null)
            return;
        mLoadView.findViewById(R.id.loading_progress).setVisibility(View.GONE);
        mLoadView.findViewById(R.id.loading_more).setVisibility(View.VISIBLE);
        mLoadView.findViewById(R.id.loading_no_data).setVisibility(View.GONE);

    }

    public boolean needMatchParentWidth(int position) {

        if (getHeadCount() != 0 && position < getHeadCount()) return true;

        if (getFootCount() != 0 && position >= getRealDataCount() + getHeadCount()) return true;

        return false;
    }

    public void addHeadView(View headView) {
        mHeaderViews.put(getHeadCount() + BASE_ITEM_TYPE_HEADER, headView);
    }

    public void addFootView(View footView) {
        mFootViews.put(mFootViews.size() + BASE_ITEM_TYPE_FOOTER, footView);
    }

    public void addLoadMoreView() {
        hasLoadMore = true;
    }

    /**
     * must invoke  before {@link #addFootView(View)}
     *
     * @param view
     */
    public void addNoDataView(View view) {
        if (view == null) {
            throw new NullPointerException("noDataView can not be null");
        }
        datas.add(null);
        mNoDataView = view;
    }

    public int getHeadCount() {
        return mHeaderViews.size();
    }

    public int getFootCount() {
        return mFootViews.size() + getLoadMoreCount();
    }

    public int getRealDataCount() {
        return datas.size();
    }

    public int getLoadMoreCount() {
        return hasLoadMore && !(datas.size() == 1 && datas.get(0) == null) ? 1 : 0;
    }

    public int getNoDataCount() {
        return mNoDataView == null ? 0 : 1;
    }

    public void isNeedLoadMore(boolean flag) {
        this.isNeedLoadMore = flag;
    }


    public boolean isInFootRange(int index) {
        return getFootCount() != 0 && index >= 0 && index >= getRealDataCount() + getHeadCount()
                && index < getRealDataCount() + getHeadCount() + getFootCount();
    }

    public boolean isInHeadRange(int index) {
        return getHeadCount() != 0 && index >= 0 && index < getHeadCount();
    }


    /**
     * Set to create menu listener.
     *
     * @param swipeMenuCreator listener.
     */
    public void setSwipeMenuCreator(SwipeMenuCreator swipeMenuCreator) {
        mSwipeMenuCreator = swipeMenuCreator;

    }

    /**
     * Set to click menu listener.
     *
     * @param swipeMenuItemClickListener listener.
     */
    public void setSwipeMenuItemClickListener(OnSwipeMenuItemClickListener swipeMenuItemClickListener) {
        this.mSwipeMenuItemClickListener = swipeMenuItemClickListener;
    }

    //添加数据子视图的点击事件
    public void addSubViewListener(int resId, View.OnClickListener listener) {
        subViewListeners.put(resId, listener);
    }

    public void setItemLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    public void setItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

}
