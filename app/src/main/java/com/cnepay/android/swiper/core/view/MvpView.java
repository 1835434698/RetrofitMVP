package com.cnepay.android.swiper.core.view;

import android.app.Activity;
import android.content.Context;

import com.cnepay.android.swiper.bean.LoginBean;
import com.cnepay.android.swiper.core.model.network.AppException;

/**
 * Created by Administrator on 2017/4/18.
 * 所有View基类
 */

public interface MvpView {

    Context obtainContext();

    Activity obtainActivity();

    /**
     * only show loading view
     */
    void showLoading();

    /**
     * connect to server get wrong such as connect timeout
     * <p>
     * close loading then show error by toast
     */
    void connectError(AppException error);

    /**
     * only close loading view
     *
     * @param content some time you should show this content by toast
     */
    void connectSuccess(Object content);

    LoginBean requestLoginBean();

    void requestSignOff();
}
