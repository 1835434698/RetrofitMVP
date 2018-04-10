package com.cnepay.android.swiper.view;

import com.cnepay.android.swiper.core.view.MvpView;

/**
 * Created by wjl on 2017/5/19.
 * 验证验证码是否正确
 */

public interface CheckMobileMessageView extends MvpView {

    void checkMobileMessage(Object content);
}
