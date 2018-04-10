package com.cnepay.android.swiper.activity;

import android.content.Intent;
import android.view.View;

import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.adapter.SettleQueryAdapter;
import com.cnepay.android.swiper.bean.SettleListBean;
import com.cnepay.android.swiper.presenter.SettleQueryPresenter;
import com.cnepay.android.swiper.view.SettleQueryView;
import com.igeek.hfrecyleviewlib.CommonViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * created by millerJK on time : 2017/5/7
 * description :结算查询
 */
public class SettleQueryActivity extends BaseQueryActivity
        implements SettleQueryView, CommonViewHolder.OnItemClickListener {

    private SettleQueryPresenter mPresenter;
    public SettleQueryAdapter mAdapter;
    public List<SettleListBean.SettleListEntity> mDatas;

    @Override
    public void init() {
        uc.setTitle(R.string.account_query);
        mQueryNumTipTv.setText(R.string.settle_num);
        mTotalMoneyTipTv.setText(R.string.settle_total_money);
        mAdapter = new SettleQueryAdapter(R.layout.account_query_item);
        mAdapter.addLoadMoreView();
        mAdapter.addNoDataView(noDataView);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setItemClickListener(this);
        mDatas = new ArrayList();
        mPresenter = new SettleQueryPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void resetNetting() {
        mPresenter.queryList(start, end, type, null);
    }

    @Override
    public void loadMoreNetting() {
        mPresenter.queryList(start, end, type, mDatas.get(mDatas.size() - 1).uniqueRecord);
    }


    @Override
    public void OnItemClick(View v, int adapterPosition) {
        Intent intent = new Intent(SettleQueryActivity.this, SettleDetailActivity.class);
        intent.putExtra("data", mDatas.get(adapterPosition));
        ac.startActivity(intent);
    }

    @Override
    public void resetData(SettleListBean value) {
        mDatas.clear();
        mDatas.addAll(value.settleList);
        mAdapter.resetDatas(value.settleList);
        totalNum = value.count;
        mQueryNumTv.setText(String.valueOf(value.count));
        mTotalMoneyTv.setText(String.valueOf(value.amount));
        mRefreshLayout.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mTimeoutView.setVisibility(View.GONE);
        loadDataFinish();
    }

    @Override
    public void appendData(SettleListBean value) {
        mDatas.addAll(value.settleList);
        mAdapter.appendDatas(value.settleList);
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
