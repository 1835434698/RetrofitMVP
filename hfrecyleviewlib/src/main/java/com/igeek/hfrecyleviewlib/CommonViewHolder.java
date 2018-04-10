package com.igeek.hfrecyleviewlib;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

import com.igeek.hfrecyleviewlib.swipe.SwipeMenuLayout;

public class CommonViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener, View.OnLongClickListener {

    public static final int ITEM_TYPE_DATA = 1;


    private OnItemClickListener clickListener;
    private OnItemLongClickListener longClickListener;

    private SparseArray<View.OnClickListener> subClickListeners = new SparseArray(0);
    private SparseArray<View.OnLongClickListener> subLongClickListeners = new SparseArray(0);

    public CommonViewHolder(View itemView) {
        super(itemView);
    }

    public CommonViewHolder(View itemView, OnItemClickListener clickListener
            , OnItemLongClickListener longClickListener) {
        super(itemView);

        this.clickListener = clickListener;
        this.longClickListener = longClickListener;

        if (this.clickListener != null)
            itemView.setOnClickListener(this);
        if (this.longClickListener != null) {
            itemView.setOnLongClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {

        View.OnClickListener subClickListener = subClickListeners.get(v.getId());

        if (subClickListener != null) {
            subClickListener.onClick(v);
        } else if (clickListener != null) {
            clickListener.OnItemClick(v, getLayoutPosition());
        }

    }

    @Override
    public boolean onLongClick(View v) {

        View.OnLongClickListener sublongClickListener = subLongClickListeners.get(v.getId());

        if (sublongClickListener != null) {
            sublongClickListener.onLongClick(v);
        } else if (longClickListener != null) {
            if (itemView instanceof SwipeMenuLayout) {
                if (itemView.getScrollX() > 5)
                    return false;
            }
            longClickListener.OnItemLongClick(v, getLayoutPosition());
        }

        return longClickListener != null || sublongClickListener != null;
    }


    public interface OnItemClickListener {
        void OnItemClick(View v, int adapterPosition);
    }

    public interface OnItemLongClickListener {
        void OnItemLongClick(View v, int adapterPosition);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
        itemView.setOnClickListener(this);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
        itemView.setOnLongClickListener(this);
    }

    /**
     * 为itemView 的子控件添加点击事件的处理
     *
     * @param resId itemView 子视图的 android:id
     */
    public void setSubViewClickListener(int resId, View.OnClickListener listener) {
        View view = itemView.findViewById(resId);
        if (view != null && subClickListeners.get(resId) == null) {
            view.setOnClickListener(this);
            subClickListeners.put(resId, listener);
        }
    }

    /**
     * 为itemView 的子控件添加长按事件的处理
     *
     * @param resId itemView 子视图的 android:id
     */
    public void setSubViewLongClickListener(int resId, View.OnLongClickListener listener) {
        View view = itemView.findViewById(resId);
        if (view != null && subClickListeners.get(resId) != null) {
            view.setOnLongClickListener(this);
            subLongClickListeners.put(resId, listener);
        }
    }

    /**
     * 如果发生引用导致内存泄露，那就在宿主销毁之前重置下吧
     */
    public void resetAllListeners() {
        this.clickListener = null;
        this.longClickListener = null;
        this.subClickListeners.clear();
    }

}
