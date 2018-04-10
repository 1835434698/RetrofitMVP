package com.cnepay.android.swiper.presenter;

import android.net.Uri;

import com.cnepay.android.swiper.MainApp;
import com.cnepay.android.swiper.bean.AccountAuthBean;
import com.cnepay.android.swiper.bean.BankQueryBean;
import com.cnepay.android.swiper.bean.BaseBean;
import com.cnepay.android.swiper.bean.LoginBean;
import com.cnepay.android.swiper.core.model.RetrofitProvider;
import com.cnepay.android.swiper.core.model.network.AppException;
import com.cnepay.android.swiper.core.model.network.CusObserver;
import com.cnepay.android.swiper.core.presenter.MvpPresenterIml;
import com.cnepay.android.swiper.model.Dictionary;
import com.cnepay.android.swiper.utils.Convert;
import com.cnepay.android.swiper.view.CertificationAccountView;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/5/19.
 */

public class CertificationAccountPresenter extends MvpPresenterIml<CertificationAccountView> {
    private File file;
    private BankQueryBean.Bank bankInfo;

    public void addPicPath(Uri uri) {
        file = Convert.uri2File(getView().obtainContext(), uri);
    }

    public void setBankInfo(BankQueryBean.Bank bankInfo) {
        this.bankInfo = bankInfo;
    }

    public void initData() {
        LoginBean userBean = Dictionary.getLoginBean();
        if (userBean != null) {
            getView().setAuthInfo(userBean.getName(), userBean.getIdentityNumber());
        }
    }

    public void uploadAccountInfo(String accountNo, String mobile, String verificationCode) {
        if (null == file || null == bankInfo || null == Dictionary.getLoginBean()) return;
        getView().showLoading();
        MultipartBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("appVersion", MainApp.APP_VERSION)
                .addFormDataPart("name", Dictionary.getLoginBean().getName())
                .addFormDataPart("identityCard", Dictionary.getLoginBean().getIdentityNumber())
                .addFormDataPart("bankName", bankInfo.getBankDeposit())
                .addFormDataPart("unionBankNo", bankInfo.getUnionBankNo())
                .addFormDataPart("accountNo", accountNo)
                .addFormDataPart("mobile", mobile)
                .addFormDataPart("verificationCode", verificationCode)
                .addFormDataPart("card", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file))
                .build();
        RetrofitProvider.create()
                .accountAuth(requestBody)
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

    public void sendVerificationCode(String phone) {

    }

    public void downAccount() {
        getView().showLoading();
        RetrofitProvider.create().accountAuthStatus(MainApp.APP_VERSION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CusObserver<AccountAuthBean>() {
                    @Override
                    protected void onError(AppException ex) {
                        getView().connectError(ex);
                    }

                    @Override
                    public void onNext(AccountAuthBean value) {
                        getView().connectSuccess(value);
                        getView().onEchoComplete(value);
                    }
                });
    }
}
