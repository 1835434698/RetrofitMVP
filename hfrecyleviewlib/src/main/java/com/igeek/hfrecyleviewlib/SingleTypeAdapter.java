package com.igeek.hfrecyleviewlib;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * single type adapter
 */
public abstract class SingleTypeAdapter<T, H extends CommonViewHolder> extends CommonAdapter<T> {

    private int resId;

    public SingleTypeAdapter(int resId) {
        this.resId = resId;
    }

    @Override
    public CommonViewHolder createHeaderViewHolder(View headView) {
        return new CommonViewHolder(headView);
    }

    @Override
    public CommonViewHolder createFooterViewHolder(View footView) {
        return new CommonViewHolder(footView);
    }

    @Override
    public View onCreateNormalView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).inflate(resId, parent, false);
    }

    @Override
    public int getDataItemViewType(int position) {
        return CommonViewHolder.ITEM_TYPE_DATA;
    }

    @Override
    public CommonViewHolder createNormalViewHolder(View realContentView, int viewType) {
        return createViewHolder(realContentView);
    }

    @Override
    public void bindNormalViewHolder(CommonViewHolder holder, int position, int viewType) {
        bindViewHolder((H) holder, datas.get(position), position);
    }

    public abstract H createViewHolder(View itemView);

    public abstract void bindViewHolder(H holder, T t, int position);


}
