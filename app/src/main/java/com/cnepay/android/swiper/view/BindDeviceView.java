package com.cnepay.android.swiper.view;

import com.cnepay.android.swiper.core.view.MvpView;

/**
 * Created by Xushiwei on 2017/5/17.
 */

public interface BindDeviceView extends MvpView {
    void switchSearchView(boolean isOpen);

    void toast(int type);
}
