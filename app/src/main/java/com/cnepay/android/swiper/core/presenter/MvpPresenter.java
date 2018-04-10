package com.cnepay.android.swiper.core.presenter;


import com.cnepay.android.swiper.core.view.MvpView;

/**
 * Created by Administrator on 2017/4/18.
 */

public interface MvpPresenter<V extends MvpView> {
    void attachView(V view);

    void detachView(boolean saveInstance);
}
