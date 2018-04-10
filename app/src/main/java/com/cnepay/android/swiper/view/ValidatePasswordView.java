package com.cnepay.android.swiper.view;

import com.cnepay.android.swiper.core.view.MvpView;

/**
 * Created by wjl on 2017/5/19.
 * 我的页面中 进入修改密码，检查原密码是否正确
 */

public interface ValidatePasswordView extends MvpView {
    void validatePasswordView(Object content);
}
