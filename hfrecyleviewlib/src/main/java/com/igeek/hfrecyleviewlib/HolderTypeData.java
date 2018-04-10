package com.igeek.hfrecyleviewlib;

import android.view.ViewGroup;

public interface  HolderTypeData<VH extends CommonViewHolder> {

    int getType();

    CommonViewHolder buildHolder(ViewGroup parent);

    void bindDatatoHolder(VH vh,int postion,int type);
}
