package com.cnepay.android.swiper.adapter;

import android.view.View;
import android.widget.TextView;

import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.bean.BankQueryBean;
import com.igeek.hfrecyleviewlib.CommonViewHolder;
import com.igeek.hfrecyleviewlib.SingleTypeAdapter;

/**
 * Created by Administrator on 2017/5/19.
 */

public class BankAdapter extends SingleTypeAdapter<BankQueryBean.Bank, BankAdapter.RecyclerViewHolder> {

    public BankAdapter(int resId) {
        super(resId);
    }

    @Override
    public RecyclerViewHolder createViewHolder(View itemView) {
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void bindViewHolder(RecyclerViewHolder holder, BankQueryBean.Bank bank, int position) {
        holder.bankName.setText(bank.getBankDeposit());
    }

    public static class RecyclerViewHolder extends CommonViewHolder {
        TextView bankName;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            bankName = (TextView) itemView.findViewById(R.id.tvBankName);
        }
    }
}
