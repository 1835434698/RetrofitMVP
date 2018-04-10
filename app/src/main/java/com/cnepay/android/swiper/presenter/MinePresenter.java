package com.cnepay.android.swiper.presenter;

import com.cnepay.android.swiper.MainApp;
import com.cnepay.android.swiper.bean.BaseBean;
import com.cnepay.android.swiper.bean.LoginBean;
import com.cnepay.android.swiper.core.model.RetrofitProvider;
import com.cnepay.android.swiper.core.model.network.AppException;
import com.cnepay.android.swiper.core.model.network.CusObserver;
import com.cnepay.android.swiper.core.presenter.MvpPresenterIml;
import com.cnepay.android.swiper.core.view.MvpView;
import com.cnepay.android.swiper.core.view.SessionView;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wjl on 2017/5/15.
 */

public class MinePresenter extends MvpPresenterIml<SessionView> {

    public void logout() {
        getView().requestSignOff();

        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("appVersion", MainApp.APP_VERSION);
        if (isViewAttached())
            getView().showLoading();
        else
            return;
        RetrofitProvider.create()
                .logout(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CusObserver<BaseBean>() {

                    @Override
                    protected void onError(AppException ex) {
                        if (isViewAttached())
                            getView().connectError(ex);
                    }

                    @Override
                    public void onNext(BaseBean value) {
                        if (isViewAttached()) {
                            getView().connectSuccess(value);
                        }
                    }

                });
    }
}
