package com.cnepay.android.swiper.view;

import com.cnepay.android.swiper.bean.LoginBean;
import com.cnepay.android.swiper.core.view.MvpView;

/**
 * created by millerJK on time : 2017/5/11
 * description :
 */

public interface LoginView extends MvpView {
    void login(LoginBean content);
}
