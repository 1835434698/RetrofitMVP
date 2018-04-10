package com.cnepay.android.swiper.presenter;

import com.cnepay.android.swiper.MainApp;
import com.cnepay.android.swiper.bean.SettleListBean;
import com.cnepay.android.swiper.core.model.RetrofitProvider;
import com.cnepay.android.swiper.core.model.network.AppException;
import com.cnepay.android.swiper.core.model.network.CusObserver;
import com.cnepay.android.swiper.core.presenter.MvpPresenterIml;
import com.cnepay.android.swiper.view.SettleQueryView;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * created by millerJK on time : 2017/5/11
 * description : 结算查询
 */

public class SettleQueryPresenter extends MvpPresenterIml<SettleQueryView> {

    /**
     * @param startTime
     * @param endTime
     * @param type      1-TN 2-D0
     */
    public void queryList(String startTime, String endTime, String type, String unique) {

        if (isViewAttached())
            getView().showLoading();
        else return;

        Map<String, String> params = new HashMap<>();
        params.put("appVersion", MainApp.APP_VERSION);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("settleType", type);
        if (unique != null)
            params.put("uniqueRecord", unique);
        RetrofitProvider.create()
                .settleQuery(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CusObserver<SettleListBean>() {

                    @Override
                    public void onNext(SettleListBean value) {
                        if (isViewAttached()) {
                            if (unique == null)
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
