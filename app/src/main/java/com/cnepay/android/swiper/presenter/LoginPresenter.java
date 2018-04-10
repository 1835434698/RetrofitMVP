package com.cnepay.android.swiper.presenter;

import com.cnepay.android.swiper.MainApp;
import com.cnepay.android.swiper.bean.LoginBean;
import com.cnepay.android.swiper.core.model.RetrofitProvider;
import com.cnepay.android.swiper.core.model.network.AppException;
import com.cnepay.android.swiper.core.model.network.CusObserver;
import com.cnepay.android.swiper.core.presenter.MvpPresenterIml;
import com.cnepay.android.swiper.model.Dictionary;
import com.cnepay.android.swiper.utils.Logger;
import com.cnepay.android.swiper.utils.Utils;
import com.cnepay.android.swiper.view.LoginView;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;


/**
 * created by millerJK on time : 2017/5/11
 * description :
 */

public class LoginPresenter extends MvpPresenterIml<LoginView> {

    /**
     * 登录
     *
     * @param loginName
     * @param password
     * @param position
     * @param registId
     * @param reqTime
     */
    public void login(String loginName, String password, String position, String registId, String reqTime, boolean needsSaveData) {

        if (isViewAttached())
            getView().showLoading();
        else
            return;

        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("appVersion", MainApp.APP_VERSION);
        mapParams.put("loginName", loginName);
        mapParams.put("password", password);
        mapParams.put("position", position);
        mapParams.put("registId", registId);
        mapParams.put("reqTime", reqTime);

        RetrofitProvider.create()
                .login(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CusObserver<Response<LoginBean>>() {
                    @Override
                    public void onNext(Response<LoginBean> value) {
                        Logger.d(LoginPresenter.class.getSimpleName(),"onNext");
                        Dictionary.replaceLoginBean(value.body());
                        if (isViewAttached()) {
                            Dictionary.getLoginBean().setLoginName(loginName);
                            if (needsSaveData) {
                                Utils.savePhone(getView().obtainContext(), loginName);
                            } else {
                                Utils.cleanPhone(getView().obtainContext());
                            }
                            getView().connectSuccess(null);
                            getView().login(value.body());
                        }
                    }

                    @Override
                    protected void onError(AppException ex) {
                        if (isViewAttached())
                            getView().connectError(ex);
                    }
                });
    }

}