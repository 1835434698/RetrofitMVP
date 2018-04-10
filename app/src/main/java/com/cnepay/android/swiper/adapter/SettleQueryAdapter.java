package com.cnepay.android.swiper.adapter;

import android.view.View;
import android.widget.TextView;

import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.bean.SettleListBean;
import com.igeek.hfrecyleviewlib.CommonViewHolder;
import com.igeek.hfrecyleviewlib.SingleTypeAdapter;

/**
 * created by millerJK on time : 2017/5/7
 * description : 结算查询ITEM
 */

public class SettleQueryAdapter extends SingleTypeAdapter<SettleListBean.SettleListEntity, SettleQueryAdapter.RecyclerViewHolder> {

    public SettleQueryAdapter(int resId) {
        super(resId);
    }

    @Override
    public RecyclerViewHolder createViewHolder(View itemView) {
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void bindViewHolder(RecyclerViewHolder holder, SettleListBean.SettleListEntity item, int position) {
        item.settleDate = item.settleDate.replace("-", ".");
        holder.mDateTv.setText(item.settleDate);
        holder.mStatusTv.setText(getStatus(item.settleStatus));
        holder.mSelectTv.setText(item.settleType);
        holder.mMoneyTv.setText("￥"+item.transAmount);
    }

    private String getStatus(int status){
        switch (status){
            case 1:
                return "成功";
            case 2:
                return "失败";
        }
        return "";
    }

    public static class RecyclerViewHolder extends CommonViewHolder {

        TextView mDateTv, mStatusTv, mSelectTv, mMoneyTv;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            mDateTv = (TextView) itemView.findViewById(R.id.query_item_date);
            mStatusTv = (TextView) itemView.findViewById(R.id.query_item_status);
            mSelectTv = (TextView) itemView.findViewById(R.id.query_item_select);
            mMoneyTv = (TextView) itemView.findViewById(R.id.query_item_money);
        }

    }
}
