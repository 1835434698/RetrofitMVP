package com.cnepay.android.swiper.presenter;

import com.cnepay.android.swiper.MainApp;
import com.cnepay.android.swiper.bean.AccountDetailBean;
import com.cnepay.android.swiper.core.model.RetrofitProvider;
import com.cnepay.android.swiper.core.model.network.CusObserver;
import com.cnepay.android.swiper.core.model.network.AppException;
import com.cnepay.android.swiper.core.presenter.MvpPresenterIml;
import com.cnepay.android.swiper.view.SettleDetailView;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * created by millerJK on time : 2017/5/15
 * description :
 */

public class SettleDetailPresenter extends MvpPresenterIml<SettleDetailView> {

    public void query(String id) {

        if (isViewAttached()) {
            getView().showLoading();
        } else return;

        Map<String, String> params = new HashMap<>();
        params.put("appVersion", MainApp.APP_VERSION);
        params.put("transId", id);
        RetrofitProvider.create()
                .tranInfo(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CusObserver<AccountDetailBean>() {

                    @Override
                    public void onNext(AccountDetailBean value) {
                        if (isViewAttached()) {
                            getView().updateUI(value.tranInfo);
                            getView().connectSuccess(value);
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
