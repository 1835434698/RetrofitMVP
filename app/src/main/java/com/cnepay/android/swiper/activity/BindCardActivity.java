package com.cnepay.android.swiper.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.adapter.BindCardAdapter;
import com.cnepay.android.swiper.bean.BankListBean;
import com.cnepay.android.swiper.bean.CardMangeBean;
import com.cnepay.android.swiper.core.MvpAppCompatActivity;
import com.cnepay.android.swiper.presenter.BankCardEditPresenter;
import com.cnepay.android.swiper.presenter.BankListPresenter;
import com.cnepay.android.swiper.view.BankListView;
import com.cnepay.android.swiper.view.CardManageView;
import com.igeek.hfrecyleviewlib.CommonViewHolder;
import com.igeek.hfrecyleviewlib.divider.LineVerComDecoration;
import com.igeek.hfrecyleviewlib.swipe.Closeable;
import com.igeek.hfrecyleviewlib.swipe.OnSwipeMenuItemClickListener;
import com.igeek.hfrecyleviewlib.swipe.SwipeMenuCreator;
import com.igeek.hfrecyleviewlib.swipe.SwipeMenuItem;
import com.igeek.hfrecyleviewlib.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * created by millerJK on time : 2017/5/16
 * description : 绑定信用卡
 */
public class BindCardActivity extends MvpAppCompatActivity implements CardManageView
        , BankListView,View.OnClickListener {

    private SwipeMenuRecyclerView mRecyclerView;
    private BindCardAdapter mAdapter;
    private BankCardEditPresenter mPresenter;
    private BankListPresenter mListPresenter;
    private List<BankListBean.ListEntity> mBeans;
    private int deleteIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uc.setContentViewFrameWithBottomBtn(R.layout.activity_bind_card);
        uc.setTitle("绑定信用卡");
        uc.getRightText().setText("RecyclerView");
        initView();
        initListener();
        mListPresenter.bankList();
    }

    private void initView() {
        mRecyclerView = (SwipeMenuRecyclerView) findViewById(R.id.bind_card_recycler);
        mRecyclerView.addItemDecoration(new LineVerComDecoration(this, LineVerComDecoration.VERTICAL_LIST));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new BindCardAdapter(R.layout.bind_card_item);
        mAdapter.setSwipeMenuCreator(swipeMenuCreator);
        View footView = View.inflate(this, R.layout.bind_card_foot, null);
        mAdapter.addFootView(footView);
        mRecyclerView.setAdapter(mAdapter);
        mPresenter = new BankCardEditPresenter();
        mPresenter.attachView(this);
        mListPresenter = new BankListPresenter();
        mListPresenter.attachView(this);
        mBeans = new ArrayList<>();
    }

    private void initListener() {
        mAdapter.setSwipeMenuItemClickListener(swipeClickListener);
        mAdapter.setItemClickListener(itemListener);
        uc.getRightText().setOnClickListener(this);
    }

    OnSwipeMenuItemClickListener swipeClickListener = new OnSwipeMenuItemClickListener() {
        @Override
        public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
            deleteIndex = adapterPosition;
            mPresenter.cardManage("true", null,null, null, String.valueOf(mBeans.get(adapterPosition).cardId));
        }
    };

    CommonViewHolder.OnItemClickListener itemListener = (v, adapterPosition) -> {
        if (adapterPosition == mAdapter.getItemCount() - 1) {
            ac.startActivityForResult(new Intent(BindCardActivity.this, AddBankCardActivity.class), 100);
        }
    };

    private SwipeMenuCreator swipeMenuCreator = (swipeLeftMenu, swipeRightMenu, viewType) -> {

        int width = getResources().getDimensionPixelSize(R.dimen.height_73dp);
        int height = getResources().getDimensionPixelSize(R.dimen.height_73dp);
        {
            SwipeMenuItem deleteItem = new SwipeMenuItem(BindCardActivity.this)
                    .setBackgroundDrawable(getResources().getDrawable(R.color.colorE44036))
                    .setText("删除")
                    .setTextColor(Color.WHITE)
                    .setWidth(width)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(deleteItem);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case AddBankCardActivity.BACK_BANK_LIST:
                mBeans.clear();
                mListPresenter.bankList();
                break;
            case AddBankCardActivity.BACK_HOME:
                finish();
                break;

        }
    }


    @Override
    public void showList(BankListBean bean) {
        mBeans.clear();
        mBeans.addAll(bean.list);
        mAdapter.resetDatas(bean.list);
    }

    @Override
    public void success(CardMangeBean value) {
        mBeans.remove(deleteIndex);
        mAdapter.removeData(deleteIndex);
    }

    @Override
    protected void onDestroy() {
        if (mListPresenter != null)
            mListPresenter.detachView(false);
        if (mPresenter != null)
            mPresenter.detachView(false);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        ac.startActivity(new Intent(BindCardActivity.this, TestRecyActivity.class));
    }
}
