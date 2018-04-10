package com.cnepay.android.swiper.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.bean.SettleListBean;
import com.cnepay.android.swiper.core.MvpAppCompatActivity;
import com.cnepay.android.swiper.utils.SecurityUtils;

/**
 * created by millerJK on time : 2017/5/15
 * description :结算详情
 */
public class SettleDetailActivity extends MvpAppCompatActivity {

    private TextView mMerchantIdTv, mMerchantNameTv;
    private TextView mSettleTimeTv, mSettleTypeTv;
    private TextView mSettleMoneyTv, mCardNumTv;
    private TextView mTransactionMoney, mTransactionStatusTv;
    private SettleListBean.SettleListEntity entity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uc.setContentViewScrollWithFollowBottomBtn(R.layout.activity_account_detail);
        uc.setTitle("结算详情");
        initView();
        initData();
    }

    private void initView() {
        mMerchantIdTv = (TextView) findViewById(R.id.detail_merchant_id);
        mMerchantNameTv = (TextView) findViewById(R.id.detail_merchant_name);
        mSettleTimeTv = (TextView) findViewById(R.id.detail_settlement_time);
        mSettleTypeTv = (TextView) findViewById(R.id.detail_settlement_type);
        mSettleMoneyTv = (TextView) findViewById(R.id.detail_account_money);
        mCardNumTv = (TextView) findViewById(R.id.detail_card_num);
        mTransactionMoney = (TextView) findViewById(R.id.detail_transaction_money);
        mTransactionStatusTv = (TextView) findViewById(R.id.detail_status);
    }

    private void initData() {
        entity = getIntent().getParcelableExtra("data");
        mMerchantIdTv.setText(entity.merchantNo);
        mMerchantNameTv.setText(entity.merchantName);
        String date = entity.settleDate.split(" ")[0].replace("-", ".");
        mSettleTimeTv.setText(date);
        mSettleTypeTv.setText(entity.settleType);
        mSettleMoneyTv.setText("￥" + String.valueOf(entity.settleMoney));
        mCardNumTv.setText(SecurityUtils.replace4BankCard(entity.accountNum));
        mTransactionMoney.setText("￥" + String.valueOf(entity.transAmount));
        mTransactionStatusTv.setText(entity.settleStatus == 1 ? "已付款" : "未付款");
    }


}
