package com.cnepay.android.swiper.presenter;

import com.cnepay.android.swiper.MainApp;
import com.cnepay.android.swiper.bean.BannerBean;
import com.cnepay.android.swiper.bean.ModifyMessageBean;
import com.cnepay.android.swiper.core.model.RetrofitProvider;
import com.cnepay.android.swiper.core.model.network.AppException;
import com.cnepay.android.swiper.core.model.network.CusObserver;
import com.cnepay.android.swiper.core.presenter.MvpPresenterIml;
import com.cnepay.android.swiper.core.view.MvpView;
import com.cnepay.android.swiper.utils.Logger;
import com.cnepay.android.swiper.view.BannerView;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wjl on 2017/5/17.
 */

public class BannerPresenter extends MvpPresenterIml<BannerView> {

    /**
     * 获取广告位资源
     */
    public void getBannerList() {
        Map<String, Object> mapParams = new HashMap<>();
        mapParams.put("appVersion", MainApp.APP_VERSION);

        if (isViewAttached())
            getView().showLoading();
        else
            return;

        RetrofitProvider.create()
                .getBannerList(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CusObserver<BannerBean>() {

                    @Override
                    public void onNext(BannerBean value) {
                        Logger.i("wjl", "获取banner   onNext: ");
                        if (isViewAttached()) {
                            getView().connectSuccess(value);
                            getView().getBanner(value);
                        }

                    }

                    @Override
                    protected void onError(AppException ex) {
                        Logger.i("wjl", "获取banner   onError: " + ex.toString());
                        if (isViewAttached()) {
                            getView().connectError(ex);
                        }
                    }
                });
    }
}
