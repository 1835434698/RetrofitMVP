package com.cnepay.android.swiper.adapter;

import android.view.View;
import android.widget.TextView;

import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.bean.TransactionListBean;
import com.igeek.hfrecyleviewlib.CommonViewHolder;
import com.igeek.hfrecyleviewlib.SingleTypeAdapter;

/**
 * created by millerJK on time : 2017/5/7
 * description : 交易查询
 */

public class TransactionQueryAdapter extends SingleTypeAdapter<TransactionListBean.TransListEntity, TransactionQueryAdapter.RecyclerViewHolder> {

    public TransactionQueryAdapter(int resId) {
        super(resId);
    }

    @Override
    public RecyclerViewHolder createViewHolder(View itemView) {
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void bindViewHolder(RecyclerViewHolder holder, TransactionListBean.TransListEntity item, int position) {
        item.transTime = item.transTime.split(" ")[0].replace("-", ".");
        holder.mDateTv.setText(item.transTime);
        holder.mStatusTv.setText(item.transType);
        holder.mSelectTv.setText(item.settleType);
        holder.mMoneyTv.setText("￥" + item.amount);
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
