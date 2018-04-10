package com.igeek.hfrecyleviewlib;

import android.view.View;
import android.view.ViewGroup;

public class HFMultiTypeRecyAdapter extends CommonAdapter<HolderTypeData> {

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
        return null;
    }

    @Override
    public CommonViewHolder createNormalViewHolder(View realContentView, int viewType) {
        return null;
    }

    @Override
    public int getDataItemViewType(int position) {
        return datas.get(position).getType();
    }

//    @Override
//    public CommonViewHolder createNormalViewHolder(ViewGroup parent, int viewType) {
//        for(HolderTypeData typeData:datas){
//            if(typeData.getType()==viewType){
//                return typeData.buildHolder(parent);
//            }
//        }
//        return null;
//    }

    @Override
    public void bindNormalViewHolder(CommonViewHolder holder, int position, int viewType) {
        datas.get(position).bindDatatoHolder(holder,position,viewType);
    }
}
