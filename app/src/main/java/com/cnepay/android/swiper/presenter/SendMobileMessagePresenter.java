package com.cnepay.android.swiper.presenter;

import com.cnepay.android.swiper.MainApp;
import com.cnepay.android.swiper.bean.BaseBean;
import com.cnepay.android.swiper.core.model.RetrofitProvider;
import com.cnepay.android.swiper.core.model.network.AppException;
import com.cnepay.android.swiper.core.model.network.CusObserver;
import com.cnepay.android.swiper.core.presenter.MvpPresenterIml;
import com.cnepay.android.swiper.view.SendMobileMessageView;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Created by wjl on 2017/5/12.
 */

public class SendMobileMessagePresenter extends MvpPresenterIml<SendMobileMessageView> {
    private static final String TAG = "SendMobileMessagePresen";

    /**
     * 发送验证码
     *
     * @param mobile
     * @param mobile
     * @param type   注册、忘记密码必传(registe/forget)  (非必传项)
     *               TODO: 2017/5/12  需要加密参数
     */
    public void sendMobileMessage(String mobile, String type) {

        if (isViewAttached()) {
            getView().showLoading();
        } else return;

        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("mobile", mobile);
        mapParams.put("type", type);
        mapParams.put("appVersion", MainApp.APP_VERSION);

        RetrofitProvider.create()
                .sendMobileMessage(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CusObserver<Response<BaseBean>>() {

                    @Override
                    public void onNext(Response<BaseBean> value) {
                        if (isViewAttached()) {
                            getView().connectSuccess(null);
                            getView().onSendMobileMessage(value.headers().get("Set-Cookie"),value.body().getRespMsg());
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

