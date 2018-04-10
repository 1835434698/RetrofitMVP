package com.cnepay.android.swiper.presenter;

import com.cnepay.android.swiper.MainApp;
import com.cnepay.android.swiper.bean.BaseBean;
import com.cnepay.android.swiper.bean.MineMessageBean;
import com.cnepay.android.swiper.bean.ModifyMessageBean;
import com.cnepay.android.swiper.core.model.RetrofitProvider;
import com.cnepay.android.swiper.core.model.network.AppException;
import com.cnepay.android.swiper.core.model.network.CusObserver;
import com.cnepay.android.swiper.core.presenter.MvpPresenterIml;
import com.cnepay.android.swiper.utils.Logger;
import com.cnepay.android.swiper.view.MineMessageQueryView;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * created by millerJK on time : 2017/5/11
 * description :
 */

public class MineMessagePresenter extends MvpPresenterIml<MineMessageQueryView> {
    private static final String TAG = "MineMessagePresenter";

    /**
     * 查询消息
     *
     * @param detail
     */
    public void queryMineMessage( boolean detail) {
        Logger.i("wjl","执行查询消息");
        Map<String,Object> mapParams = new HashMap<>();
        mapParams.put("appVersion", MainApp.APP_VERSION);
//        mapParams.put("messageId", messageId);
        mapParams.put("detail", detail);

        if (isViewAttached())
            getView().showLoading();
        else
            return;

        RetrofitProvider.create()
                .queryMineMessage(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CusObserver<MineMessageBean>() {

                    @Override
                    public void onNext(MineMessageBean value) {
                        Logger.i("wjl", "onNext: ");
                        if (isViewAttached()) {
                            getView().connectSuccess(value);
                            getView().queryMineMessage(value);
                        }

                    }

                    @Override
                    protected void onError(AppException ex) {
                        Logger.i("wjl", "onError: "+ex.toString());
                        if (isViewAttached()) {
                            getView().connectError(ex);
                        }
                    }
                });
    }

}
