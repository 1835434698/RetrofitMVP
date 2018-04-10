package com.cnepay.android.swiper.presenter;

import com.cnepay.android.swiper.MainApp;
import com.cnepay.android.swiper.bean.BankQueryBean;
import com.cnepay.android.swiper.core.model.RetrofitProvider;
import com.cnepay.android.swiper.core.model.network.AppException;
import com.cnepay.android.swiper.core.model.network.CusObserver;
import com.cnepay.android.swiper.core.presenter.MvpPresenterIml;
import com.cnepay.android.swiper.view.CertificationBankView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/5/19.
 */

public class CertificationBankSearchPresenter extends MvpPresenterIml<CertificationBankView> {

    public void bankQuery(String keyword) {
        getView().showLoading();
        RetrofitProvider.create()
                .bankQuery(keyword, "111", "150", "1", String.valueOf(System.currentTimeMillis()), MainApp.APP_VERSION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CusObserver<BankQueryBean>() {
                    @Override
                    protected void onError(AppException ex) {
                        getView().connectError(ex);
                    }

                    @Override
                    public void onNext(BankQueryBean value) {
                        getView().connectSuccess(value);
                        getView().onFillRecycler(value);
                    }
                });
    }

}
