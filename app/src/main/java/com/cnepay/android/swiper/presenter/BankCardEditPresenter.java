package com.cnepay.android.swiper.presenter;

import com.cnepay.android.swiper.MainApp;
import com.cnepay.android.swiper.bean.CardMangeBean;
import com.cnepay.android.swiper.core.model.RetrofitProvider;
import com.cnepay.android.swiper.core.model.network.AppException;
import com.cnepay.android.swiper.core.model.network.CusObserver;
import com.cnepay.android.swiper.core.presenter.MvpPresenterIml;
import com.cnepay.android.swiper.model.Dictionary;
import com.cnepay.android.swiper.view.CardManageView;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * created by millerJK on time : 2017/5/17
 * description : 添加/删除 信用卡
 */

public class BankCardEditPresenter extends MvpPresenterIml<CardManageView> {


    public void cardManage(String isDelete, String bankCard, String mobile, String idCode, String cardIds) {

        if (isViewAttached()) {
            getView().showLoading();
        } else return;

        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("appVersion", MainApp.APP_VERSION);
        mapParams.put("isDelete", isDelete);
        if (isDelete.equals("true")) { //delete card
            mapParams.put("cardIds", cardIds);
        } else {
            mapParams.put("mobile", mobile);
            mapParams.put("accountName", Dictionary.getLoginBean().getLoginName());
            mapParams.put("isSelf", "true");
            mapParams.put("bankCard", bankCard);
            mapParams.put("idCode", idCode);
            mapParams.put("idNumber","111111111111111");//Dictionary.getLoginSession().getIdentityNumber()
            mapParams.put("reqTime",String.valueOf(System.currentTimeMillis()));
        }

        RetrofitProvider.create()
                .cardManage(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CusObserver<CardMangeBean>() {
                    @Override
                    public void onNext(CardMangeBean value) {
                        if (isViewAttached()) {
                            getView().connectSuccess(null);
                            getView().success(value);
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
