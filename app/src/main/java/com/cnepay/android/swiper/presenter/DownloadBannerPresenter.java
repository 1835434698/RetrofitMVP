package com.cnepay.android.swiper.presenter;

import com.cnepay.android.swiper.MainApp;
import com.cnepay.android.swiper.bean.DownLoadBannerBean;
import com.cnepay.android.swiper.core.model.RetrofitProvider;
import com.cnepay.android.swiper.core.model.network.AppException;
import com.cnepay.android.swiper.core.model.network.CusObserver;
import com.cnepay.android.swiper.core.presenter.MvpPresenterIml;
import com.cnepay.android.swiper.view.DownloadBannerView;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wjl on 2017/5/20.
 */

public class DownloadBannerPresenter extends MvpPresenterIml<DownloadBannerView> {

    /**
     * 下载图片
     *
     * @param fileName
     * @param type
     */
    public void downloadBanner(String fileName, String type) {

        if (isViewAttached())
            getView().showLoading();
        else
            return;

        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("appVersion", MainApp.APP_VERSION);
        mapParams.put("fileName", fileName);
        mapParams.put("type", type);

        RetrofitProvider.create()
                .downloadBanner(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CusObserver<DownLoadBannerBean>() {
                    @Override
                    public void onNext(DownLoadBannerBean value) {
                        if (isViewAttached())
                            getView().connectSuccess(value);
                            getView().downloadbanner(value);
                    }

                    @Override
                    protected void onError(AppException ex) {
                        if (isViewAttached())
                            getView().connectError(ex);
                    }
                });
    }
}
