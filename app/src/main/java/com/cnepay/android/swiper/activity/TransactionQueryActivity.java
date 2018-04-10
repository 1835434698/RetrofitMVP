package com.cnepay.android.swiper.activity;

import android.content.Intent;
import android.view.View;

import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.adapter.TransactionQueryAdapter;
import com.cnepay.android.swiper.bean.TransactionListBean;
import com.cnepay.android.swiper.presenter.TransactionQueryPresenter;
import com.cnepay.android.swiper.view.TransactionQueryView;
import com.igeek.hfrecyleviewlib.CommonViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * created by millerJK on time : 2017/5/7
 * description :结算查询
 */
public class TransactionQueryActivity extends BaseQueryActivity
        implements TransactionQueryView, CommonViewHolder.OnItemClickListener {

    private TransactionQueryPresenter mPresenter;
    public TransactionQueryAdapter mAdapter;
    public List<TransactionListBean.TransListEntity> mDatas;

    @Override
    public void init() {
        uc.setTitle(R.string.transaction_query);
        mQueryNumTipTv.setText(R.string.transaction_count);
        mTotalMoneyTipTv.setText(R.string.transaction_total_money);
        mAdapter = new TransactionQueryAdapter(R.layout.account_query_item);
        mAdapter.addLoadMoreView();
        mAdapter.addNoDataView(noDataView);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setItemClickListener(this);
        mDatas = new ArrayList();
        mPresenter = new TransactionQueryPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void resetNetting() {
        mPresenter.queryList(start, end, type, null);
    }

    @Override
    public void loadMoreNetting() {
        mPresenter.queryList(start, end, type, String.valueOf(mDatas.get(mDatas.size() - 1).tranid));
    }


    @Override
    public void OnItemClick(View v, int adapterPosition) {
        Intent intent = new Intent(TransactionQueryActivity.this, TransactionDetailActivity.class);
        intent.putExtra("tranid", String.valueOf(mDatas.get(adapterPosition).tranid));
        ac.startActivity(intent);
    }


    @Override
    public void resetData(TransactionListBean value) {
        mDatas.clear();
        mDatas.addAll(value.transList);
        mAdapter.resetDatas(mDatas);
        totalNum = value.count;
        mQueryNumTv.setText(String.valueOf(value.count));
        mTotalMoneyTv.setText(String.valueOf(value.amount));
        mRefreshLayout.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mTimeoutView.setVisibility(View.GONE);
        loadDataFinish();
    }

    @Override
    public void appendData(TransactionListBean value) {
        mDatas.addAll(value.transList);
        mAdapter.appendDatas(value.transList);
        loadDataFinish();
    }

    @Override
    public void connectTimeout() {
        if (mDatas.size() == 0) {
            mRefreshLayout.setVisibility(View.GONE);
            mTimeoutView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            mTimeoutImg.setVisibility(View.VISIBLE);
        } else
            loadDataFinish();
    }

    public void loadDataFinish() {
        mAdapter.onLoadComplete();
        if (mDatas.size() == totalNum) {
            mAdapter.isNeedLoadMore(false);
        } else {
            mAdapter.isNeedLoadMore(true);
        }
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null)
            mPresenter.detachView(false);
        super.onDestroy();
    }
}
