package com.cnepay.android.swiper.presenter;

import com.cnepay.android.swiper.MainApp;
import com.cnepay.android.swiper.bean.BaseBean;
import com.cnepay.android.swiper.core.model.RetrofitProvider;
import com.cnepay.android.swiper.core.model.network.AppException;
import com.cnepay.android.swiper.core.model.network.CusObserver;
import com.cnepay.android.swiper.core.presenter.MvpPresenterIml;
import com.cnepay.android.swiper.view.ResetPasswordView;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wjl on 2017/5/15.
 */

public class ResetPasswordPresenter extends MvpPresenterIml<ResetPasswordView> {

    private static final String TAG = "ModifyPasswordPresenter";



    /**
     * 修改密码
     *
     * 如果是忘记密码找回密码islogin=0
     * 如果是已经登录变更密码 islogin=1
     */
    public void resetPassword(Map<String, String> mapParams) {
        if (isViewAttached()) {
            getView().showLoading();
        } else {
            return;
        }
        RetrofitProvider.create()
                .resetPassword(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CusObserver<BaseBean>() {


                    @Override
                    public void onNext(BaseBean value) {
                        if (isViewAttached()) {
                            getView().connectSuccess(value);
                            getView().resetPassword(value);
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
