package com.cnepay.android.swiper.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.bean.AccountDetailBean;
import com.cnepay.android.swiper.core.MvpAppCompatActivity;
import com.cnepay.android.swiper.presenter.SettleDetailPresenter;
import com.cnepay.android.swiper.utils.SecurityUtils;
import com.cnepay.android.swiper.view.SettleDetailView;

/**
 * created by millerJK on time : 2017/5/20
 * description :交易详情
 */
public class TransactionDetailActivity extends MvpAppCompatActivity implements SettleDetailView {

    private TextView mMerchantIdTv, mMerchantNameTv;
    private TextView mSettleTimeTv, mSettleTypeTv;
    private TextView mCardNumTv, mTransactionOrderTv;
    private TextView mTransactionWaterTv, mTransactionPortTv;
    private TextView mTransTypeTv;
    private TextView mTransactionMoney, mTransactionStatusTv;
    private SettleDetailPresenter mPresenter;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uc.setContentViewScrollWithFollowBottomBtn(R.layout.activity_transaction_detail);
        uc.setTitle("交易详情");
        initView();
        initData();
        mPresenter = new SettleDetailPresenter();
        mPresenter.attachView(this);
        mPresenter.query(id);
    }

    private void initView() {
        mMerchantIdTv = (TextView) findViewById(R.id.detail_merchant_id);
        mMerchantNameTv = (TextView) findViewById(R.id.detail_merchant_name);
        mSettleTimeTv = (TextView) findViewById(R.id.detail_settlement_time);
        mSettleTypeTv = (TextView) findViewById(R.id.detail_settlement_type);
        mTransactionMoney = (TextView) findViewById(R.id.detail_transaction_money);
        mTransactionStatusTv = (TextView) findViewById(R.id.detail_status);
        mTransactionOrderTv = (TextView) findViewById(R.id.detail_transaction_num);
        mTransactionWaterTv = (TextView) findViewById(R.id.detail_transaction_water);
        mTransactionPortTv = (TextView) findViewById(R.id.detail_transaction_port);
        mCardNumTv = (TextView) findViewById(R.id.detail_transaction_card_num);
        mTransTypeTv = (TextView) findViewById(R.id.detail_card_num);
    }

    private void initData() {
        id = getIntent().getStringExtra("tranid");
    }

    @Override
    public void updateUI(AccountDetailBean.TranInfoEntity entity) {
        mMerchantIdTv.setText(entity.merchantNo);
        mMerchantNameTv.setText(entity.merchantName);
        String date = entity.transTime.split(" ")[0].replace("-", ".");
        mSettleTimeTv.setText(date);
        mSettleTypeTv.setText(entity.settleType);
        mTransactionOrderTv.setText(entity.batchNo);
        mTransactionWaterTv.setText(entity.voucherNo);
        mTransactionPortTv.setText(entity.terminalNo);
        mCardNumTv.setText(SecurityUtils.replace4BankCard(entity.cardNoWipe));
        mTransTypeTv.setText(getTransType(entity.transType));
        mTransactionStatusTv.setText(getTransStatus(entity.transStatus));
        mTransactionMoney.setText("￥" + String.valueOf(entity.amount));

    }

    private String getTransType(String type) {
        switch (type) {
            case "sale":
                return "消费";
            case "sale_void":
                return "撤销";
            case "auth_comp":
                return "预授权完成";
            case "auth_comp_cancel":
                return "预授权完成撤销";
            case "refund":
                return "退货";
        }
        return null;
    }

    private String getTransStatus(int transType) {
        switch (transType) {
            case 0:
                return "失败";
            case 1:
                return "成功";
            case 2:
                return "已冲正";
            case 3:
                return "已撤销";
            case 4:
                return "已退款";
        }
        return null;
    }
}
