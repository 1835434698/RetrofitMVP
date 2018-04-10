package com.cnepay.android.swiper.adapter;

import android.view.View;
import android.widget.TextView;

import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.bean.BankListBean;
import com.igeek.hfrecyleviewlib.CommonViewHolder;
import com.igeek.hfrecyleviewlib.SingleTypeAdapter;

/**
 * created by millerJK on time : 2017/5/7
 * description : 银行卡列表
 */

public class BindCardAdapter extends SingleTypeAdapter<BankListBean.ListEntity, BindCardAdapter.RecyclerViewHolder> {

    public BindCardAdapter(int resId) {
        super(resId);
    }

    @Override
    public RecyclerViewHolder createViewHolder(View itemView) {
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void bindViewHolder(RecyclerViewHolder holder, BankListBean.ListEntity item, int position) {
        holder.mBankTv.setText(item.bankName);
        holder.mUserTv.setText(item.accountName);
        holder.mNumTv.setText(item.accountNo);
    }

    public static class RecyclerViewHolder extends CommonViewHolder {

        TextView mBankTv, mUserTv, mNumTv;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            mBankTv = (TextView) itemView.findViewById(R.id.bind_card_bank);
            mUserTv = (TextView) itemView.findViewById(R.id.bind_card_user);
            mNumTv = (TextView) itemView.findViewById(R.id.bind_card_number);
        }

    }
}
