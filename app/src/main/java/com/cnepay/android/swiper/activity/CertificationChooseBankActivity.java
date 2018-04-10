package com.cnepay.android.swiper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.EditText;

import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.adapter.BankAdapter;
import com.cnepay.android.swiper.bean.BankQueryBean;
import com.cnepay.android.swiper.core.MvpAppCompatActivity;
import com.cnepay.android.swiper.presenter.CertificationBankSearchPresenter;
import com.cnepay.android.swiper.view.CertificationBankView;
import com.igeek.hfrecyleviewlib.CommonViewHolder;
import com.igeek.hfrecyleviewlib.divider.LineVerComDecoration;
import com.igeek.hfrecyleviewlib.refresh.NestedRefreshLayout;
import com.igeek.hfrecyleviewlib.swipe.SwipeMenuRecyclerView;

/**
 * 资质认证-选择银行卡
 */
public class CertificationChooseBankActivity extends MvpAppCompatActivity implements View.OnClickListener, CertificationBankView {
    public static final String EXTRA_DATA = "_data";
    private EditText etBankAccount;
    private NestedRefreshLayout refresh_lay;
    private SwipeMenuRecyclerView recyclerView;
    private BankAdapter mBankAdapter;
    private CertificationBankSearchPresenter mSearchPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uc.setContentViewFrameWithBottomBtn(R.layout.activity_certification_choose_bank);
        uc.setTitle("选择开户行");
        refresh_lay = (NestedRefreshLayout) findViewById(R.id.refresh_lay);
        recyclerView = (SwipeMenuRecyclerView) findViewById(R.id.recyclerView);
        etBankAccount = (EditText) findViewById(R.id.etBankAccount);
        findViewById(R.id.tvSearch).setOnClickListener(this);

        refresh_lay.setForbidRefresh(true);

        recyclerView.addItemDecoration(new LineVerComDecoration(this, LineVerComDecoration.VERTICAL_LIST)); /***设置Item间分割线***/
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBankAdapter = new BankAdapter(R.layout.item_certification_choose_bank);
        recyclerView.setAdapter(mBankAdapter);

        mBankAdapter.setItemClickListener((v, adapterPosition) -> {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DATA, mBankAdapter.getData(adapterPosition));
            setResult(RESULT_OK, intent);
            finish();
        });

        mSearchPresenter = new CertificationBankSearchPresenter();
        mSearchPresenter.attachView(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSearchPresenter.detachView(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSearch:
                mSearchPresenter.bankQuery(etBankAccount.getText().toString().trim());
                break;
        }
    }

    @Override
    public void onFillRecycler(BankQueryBean bankQueryBean) {
        mBankAdapter.resetDatas(bankQueryBean.getBanks());
    }
}
