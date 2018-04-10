package com.cnepay.android.swiper.presenter;

import com.cnepay.android.swiper.MainApp;
import com.cnepay.android.swiper.bean.BaseBean;
import com.cnepay.android.swiper.core.model.RetrofitProvider;
import com.cnepay.android.swiper.core.model.network.AppException;
import com.cnepay.android.swiper.core.model.network.CusObserver;
import com.cnepay.android.swiper.core.presenter.MvpPresenterIml;
import com.cnepay.android.swiper.model.Dictionary;
import com.cnepay.android.swiper.utils.Logger;
import com.cnepay.android.swiper.view.RegisterView;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * created by wjl on time : 2017/5/12
 * description :
 */

public class RegisterPresenter extends MvpPresenterIml<RegisterView> {
    private static final String TAG = "RegisterPresenter";

    /**
     * 注册
     *
     * @param mobile
     * @param password
     * @param idCode   验证码
     *                 TODO: 2017/5/12  需要加密参数
     */

    public void register(String mobile, String password, String idCode,String cookie) {
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("appVersion", MainApp.APP_VERSION);
        mapParams.put("mobile", mobile);
        mapParams.put("password", password);
        mapParams.put("idCode", idCode);

        Map<String, Object> mapHerads = new HashMap<>();
        mapHerads.put("Cookie", cookie);

        if (isViewAttached()) {
            getView().showLoading();
        }else{
            return;
        }
        RetrofitProvider.create()
                .register(mapHerads,mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CusObserver<BaseBean>() {

                    @Override
                    public void onNext(BaseBean value) {
                        Logger.i(TAG, "onNext: ");
                        if (isViewAttached()) {
                            getView().connectSuccess(value);
                            getView().onRegister(value);
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
