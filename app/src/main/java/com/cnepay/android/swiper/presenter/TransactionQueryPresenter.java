package com.cnepay.android.swiper.presenter;

import com.cnepay.android.swiper.MainApp;
import com.cnepay.android.swiper.bean.TransactionListBean;
import com.cnepay.android.swiper.core.model.RetrofitProvider;
import com.cnepay.android.swiper.core.model.network.AppException;
import com.cnepay.android.swiper.core.model.network.CusObserver;
import com.cnepay.android.swiper.core.presenter.MvpPresenterIml;
import com.cnepay.android.swiper.view.TransactionQueryView;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * created by millerJK on time : 2017/5/11
 * description : 交易查询
 */

public class TransactionQueryPresenter extends MvpPresenterIml<TransactionQueryView> {

    /**
     * @param startTime
     * @param endTime
     * @param type      1-TN 2-D0
     */
    public void queryList(String startTime, String endTime, String type, String lastId) {

        if (isViewAttached())
            getView().showLoading();
        else return;

        Map<String, String> params = new HashMap<>();
        params.put("appVersion", MainApp.APP_VERSION);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("settleType", type);
        if (lastId != null)
            params.put("lastID", lastId);
        RetrofitProvider.create()
                .transactionQuery(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CusObserver<TransactionListBean>() {

                    @Override
                    public void onNext(TransactionListBean value) {
                        if (isViewAttached()) {
                            if (lastId == null)
                                getView().resetData(value);
                            else
                                getView().appendData(value);

                            getView().connectSuccess(null);
                        }
                    }

                    @Override
                    protected void onError(AppException ex) {
                        if (isViewAttached()) {
                            getView().connectError(ex);
                            if (ex.getCode() == 1003)
                                getView().connectTimeout();
                        }
                    }
                });
    }

}
