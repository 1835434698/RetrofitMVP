package com.cnepay.android.swiper.presenter;

import com.cnepay.android.swiper.MainApp;
import com.cnepay.android.swiper.bean.SignBean;
import com.cnepay.android.swiper.core.model.RetrofitProvider;
import com.cnepay.android.swiper.core.model.network.AppException;
import com.cnepay.android.swiper.core.model.network.CusObserver;
import com.cnepay.android.swiper.core.presenter.MvpPresenterIml;
import com.cnepay.android.swiper.view.SignView;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * created by millerJK on time : 2017/5/22
 * description : 签到
 * 使用流式布局
 */

public class SignPresenter extends MvpPresenterIml<SignView> {

    public void signin(String position) {

        if (isViewAttached()) {
            getView().showLoading();
        } else return;

        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("appVersion", MainApp.getAppVersion());
        mapParams.put("position", position);
        RetrofitProvider.create()
                .signin(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CusObserver<SignBean>() {
                    @Override
                    public void onNext(SignBean value) {
                        if (isViewAttached()) {
                            getView().signSuccess();
                            getView().connectSuccess(null);
                        }
                    }

                    @Override
                    protected void onError(AppException ex) {
                        if (isViewAttached()) {
                            getView().signFailed();
                            getView().connectError(ex);
                        }
                    }
                });
    }


}
