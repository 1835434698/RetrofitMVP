package com.cnepay.android.swiper.presenter;

import com.cnepay.android.swiper.MainApp;
import com.cnepay.android.swiper.bean.MerchantQualifyBean;
import com.cnepay.android.swiper.core.model.RetrofitProvider;
import com.cnepay.android.swiper.core.model.network.AppException;
import com.cnepay.android.swiper.core.model.network.CusObserver;
import com.cnepay.android.swiper.core.presenter.MvpPresenterIml;
import com.cnepay.android.swiper.view.MerchantQualifyView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * created by millerJK on time : 2017/5/22
 * description : 获取商户资质
 */

public class MerchantQualifyPresenter extends MvpPresenterIml<MerchantQualifyView> {

    public void merchantQualify() {

        if (isViewAttached()) {
            getView().showLoading();
        } else return;

        RetrofitProvider.create()
                .merchantQualify(MainApp.getAppVersion())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CusObserver<MerchantQualifyBean>() {
                    @Override
                    public void onNext(MerchantQualifyBean value) {
                        if (isViewAttached()) {
                            getView().updateUI(value.merchantQualify);
                            getView().connectSuccess(null);
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
