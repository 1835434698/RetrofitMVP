package com.cnepay.android.swiper.adapter;

import android.view.View;
import android.widget.TextView;

import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.bean.AccountQueryItem;
import com.igeek.hfrecyleviewlib.CommonViewHolder;
import com.igeek.hfrecyleviewlib.SingleTypeAdapter;

/**
 * created by millerJK on time : 2017/5/7
 * description :
 */

public class AccountQueryAdapter extends SingleTypeAdapter<AccountQueryItem, AccountQueryAdapter.RecyclerViewHolder> {

    public AccountQueryAdapter(int resId) {
        super(resId);
    }

    @Override
    public RecyclerViewHolder createViewHolder(View itemView) {
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void bindViewHolder(RecyclerViewHolder holder, AccountQueryItem item, int position) {
        holder.mDateTv.setText(item.time);
        holder.mStatusTv.setText(item.status);
        holder.mSelectTv.setText(item.selector);
        holder.mMoneyTv.setText(item.money);
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
