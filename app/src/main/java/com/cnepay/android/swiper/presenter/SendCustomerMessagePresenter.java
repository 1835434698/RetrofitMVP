package com.cnepay.android.swiper.presenter;

import com.cnepay.android.swiper.MainApp;
import com.cnepay.android.swiper.bean.BaseBean;
import com.cnepay.android.swiper.core.model.RetrofitProvider;
import com.cnepay.android.swiper.core.model.network.AppException;
import com.cnepay.android.swiper.core.model.network.CusObserver;
import com.cnepay.android.swiper.core.presenter.MvpPresenterIml;
import com.cnepay.android.swiper.view.SendCustomerMessageView;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * created by millerJK on time : 2017/5/17
 * description : 发送验证码
 */

public class SendCustomerMessagePresenter extends MvpPresenterIml<SendCustomerMessageView> {

    public void sendMobileMessage(String mobileNum, String cardNum) {

        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("appVersion", MainApp.APP_VERSION);
       mapParams.put("accountName", "王");//Dictionary.getLoginSession().getLoginName()
        mapParams.put("idNumber","111111111111111");//Dictionary.getLoginSession().getIdentityNumber()
        mapParams.put("mobile", mobileNum);
        mapParams.put("bankCard", cardNum);
        mapParams.put("reqTime",String.valueOf(System.currentTimeMillis()));

        RetrofitProvider.create()
                .sendCustomerMessage(mapParams)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CusObserver<BaseBean>() {
                    @Override
                    public void onNext(BaseBean value) {
                        if (isViewAttached()) {
                            getView().sendMessageSuccess(value);
                        }
                    }

                    @Override
                    protected void onError(AppException ex) {
                        if (isViewAttached()) {
                            getView().sendMessageError();
                            getView().connectError(ex);
                        }
                    }

                });
    }
}
