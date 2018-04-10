package com.cnepay.android.swiper.presenter;

import com.cnepay.android.swiper.MainApp;
import com.cnepay.android.swiper.bean.BankListBean;
import com.cnepay.android.swiper.core.model.RetrofitProvider;
import com.cnepay.android.swiper.core.model.network.AppException;
import com.cnepay.android.swiper.core.model.network.CusObserver;
import com.cnepay.android.swiper.core.presenter.MvpPresenterIml;
import com.cnepay.android.swiper.view.BankListView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * created by millerJK on time : 2017/5/17
 * description : 绑定银行卡/列表
 */

public class BankListPresenter extends MvpPresenterIml<BankListView> {

    public void bankList() {

        if (isViewAttached()) {
            getView().showLoading();
        } else return;

        RetrofitProvider.create()
                .bandList(MainApp.APP_VERSION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CusObserver<BankListBean>() {
                    @Override
                    public void onNext(BankListBean value) {
                        if (isViewAttached()) {
                            getView().connectSuccess(value);
                            getView().showList(value);
                        }
                    }

                    @Override
                    protected void onError(AppException ex) {
                        if (isViewAttached()) {
                            getView().connectError(ex);
                        }
                    }
                });
    }
}
