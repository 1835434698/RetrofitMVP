package com.cnepay.android.swiper.presenter;

import com.cnepay.android.swiper.MainApp;
import com.cnepay.android.swiper.bean.BaseBean;
import com.cnepay.android.swiper.core.model.RetrofitProvider;
import com.cnepay.android.swiper.core.model.network.AppException;
import com.cnepay.android.swiper.core.model.network.CusObserver;
import com.cnepay.android.swiper.core.presenter.MvpPresenterIml;
import com.cnepay.android.swiper.model.Dictionary;
import com.cnepay.android.swiper.view.ValidatePasswordView;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wjl on 2017/5/19.
 */

public class ValidatePasswordPresenter extends MvpPresenterIml<ValidatePasswordView> {
    /**
     * 验证用户密码 是否正确
     *
     * 密码
     *   TODO: 2017/5/19  需要加密参数??
     */

    public void validatePassword(String password) {
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("appVersion", MainApp.APP_VERSION);
        mapParams.put("password", password);


        if (isViewAttached()) {
            getView().showLoading();
        }else{
            return;
        }
        RetrofitProvider.create()
                .validatePassword(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CusObserver<BaseBean>() {

                    @Override
                    public void onNext(BaseBean value) {
                        if (isViewAttached()) {
                            getView().connectSuccess(value);
                            getView().validatePasswordView(value);
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
