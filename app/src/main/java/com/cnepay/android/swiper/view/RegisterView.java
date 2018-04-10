package com.cnepay.android.swiper.view;

import com.cnepay.android.swiper.core.view.MvpView;

/**
 * created by millerJK on time : 2017/5/11
 * description :
 */

public interface RegisterView extends MvpView {
    /**
     * 注册--的回调  处理注册的结果
     */
    void onRegister(Object content);
}
