package com.cnepay.android.swiper.presenter;

import android.net.Uri;

import com.cnepay.android.swiper.MainApp;
import com.cnepay.android.swiper.bean.BaseBean;
import com.cnepay.android.swiper.bean.MerchantAuthBean;
import com.cnepay.android.swiper.core.model.RetrofitProvider;
import com.cnepay.android.swiper.core.model.network.AppException;
import com.cnepay.android.swiper.core.model.network.CusObserver;
import com.cnepay.android.swiper.core.presenter.MvpPresenterIml;
import com.cnepay.android.swiper.utils.Convert;
import com.cnepay.android.swiper.view.CertificationMerchantView;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/5/18.
 */

public class CertificationMerchantPresenter extends MvpPresenterIml<CertificationMerchantView> {
    private File file;

    public void addPicPath(Uri uri) {
        file = Convert.uri2File(getView().obtainContext(), uri);
    }

    public void uploadMerchantInfo(String companyName, String regPlace, String businessLicense, String isCheckMerchantInfo) {
        getView().showLoading();
        MultipartBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("appVersion", MainApp.APP_VERSION)
                .addFormDataPart("companyName", companyName)
                .addFormDataPart("regPlace", regPlace)
                .addFormDataPart("businessLicense", businessLicense)
                .addFormDataPart("isCheckMerchantInfo", isCheckMerchantInfo)
                .addFormDataPart("business", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file))
                .build();
        RetrofitProvider.create()
                .merchantAuth(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CusObserver<BaseBean>() {
                    @Override
                    protected void onError(AppException ex) {
                        getView().connectSuccess(null);
                        getView().onUploadComplete(ex.toString());
                    }

                    @Override
                    public void onNext(BaseBean value) {
                        getView().connectSuccess(value);
                        getView().onUploadComplete(value.getRespMsg());
                    }
                });
    }

    public void downMerchant() {
        getView().showLoading();
        RetrofitProvider.create().merchantAuthStatus(MainApp.APP_VERSION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CusObserver<MerchantAuthBean>() {
                    @Override
                    protected void onError(AppException ex) {
                        getView().connectError(ex);
                    }

                    @Override
                    public void onNext(MerchantAuthBean value) {
                        getView().connectSuccess(value);
                        getView().onEchoComplete(value);
                    }
                });
    }
}
