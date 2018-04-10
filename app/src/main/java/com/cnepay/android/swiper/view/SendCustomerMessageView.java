package com.cnepay.android.swiper.view;

import com.cnepay.android.swiper.bean.BaseBean;
import com.cnepay.android.swiper.core.view.MvpView;

/**
 * created by millerJK on time : 2017/5/17
 * description :
 */

public interface SendCustomerMessageView extends MvpView {

    void sendMessageSuccess(BaseBean value);

    void sendMessageError();

}