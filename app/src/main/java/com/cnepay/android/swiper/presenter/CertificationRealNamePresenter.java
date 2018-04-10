package com.cnepay.android.swiper.presenter;

import android.net.Uri;
import android.text.TextUtils;

import com.cnepay.android.swiper.MainApp;
import com.cnepay.android.swiper.bean.BaseBean;
import com.cnepay.android.swiper.bean.RealNameAuthBean;
import com.cnepay.android.swiper.core.model.RetrofitProvider;
import com.cnepay.android.swiper.core.model.network.AppException;
import com.cnepay.android.swiper.core.model.network.CusObserver;
import com.cnepay.android.swiper.core.presenter.MvpPresenterIml;
import com.cnepay.android.swiper.utils.Convert;
import com.cnepay.android.swiper.view.CertificationRealNameView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/5/17.
 */

public class CertificationRealNamePresenter extends MvpPresenterIml<CertificationRealNameView> {
    private Map<String, File> mMap = new HashMap<>();

    public void addPicPath(int code, Uri uri) {
        String key;
        switch (code) {
            case 1001:
                key = "personal";
                break;
            case 1002:
                key = "personalBack";
                break;
            case 1003:
                key = "hPersonal";
                break;
            default:
                return;
        }
        File file = Convert.uri2File(getView().obtainContext(), uri);
        mMap.put(key, file);
    }

    public void scanOcr() {
    }

    public void uploadRealNameInfo(String name, String idNumber) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(idNumber) || mMap.size() != 3) return;
        getView().showLoading();
        MultipartBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("appVersion", MainApp.APP_VERSION)
                .addFormDataPart("name", name)
                .addFormDataPart("idNumber", idNumber)
                .addFormDataPart("personal", mMap.get("personal").getName(), RequestBody.create(MediaType.parse("multipart/form-data"), mMap.get("personal")))
                .addFormDataPart("personalBack", mMap.get("personalBack").getName(), RequestBody.create(MediaType.parse("multipart/form-data"), mMap.get("personalBack")))
                .addFormDataPart("hPersonal", mMap.get("hPersonal").getName(), RequestBody.create(MediaType.parse("multipart/form-data"), mMap.get("hPersonal")))
                .build();

        RetrofitProvider.create()
                .realNameAuth(requestBody)
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

    public void downRealNameInfo() {
        getView().showLoading();
        RetrofitProvider.create()
                .realNameAuthStatus(MainApp.APP_VERSION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CusObserver<RealNameAuthBean>() {
                    @Override
                    protected void onError(AppException ex) {
                        getView().connectError(ex);
                    }

                    @Override
                    public void onNext(RealNameAuthBean value) {
                        getView().connectSuccess(value);
                        getView().onEchoComplete(value);
                    }
                });
    }
}
