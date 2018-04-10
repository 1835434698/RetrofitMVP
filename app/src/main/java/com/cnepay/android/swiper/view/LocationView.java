package com.cnepay.android.swiper.view;

import com.cnepay.android.swiper.core.view.MvpView;

/**
 * created by millerJK on time : 2017/5/22
 * description : 定位
 */

public interface LocationView extends MvpView {

    void permissionDeny();

    void locationSuccess(String position);

    void locationFailed(int errorCode);
}
