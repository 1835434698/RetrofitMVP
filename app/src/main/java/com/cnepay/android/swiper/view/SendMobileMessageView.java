package com.cnepay.android.swiper.view;

import com.cnepay.android.swiper.core.view.MvpView;

/**
 * created by millerJK on time : 2017/5/11
 * description :
 */

public interface SendMobileMessageView extends MvpView {
    /**
     * 发送短信验证码--的回调，处理发送短信验证码的结果
     */
    void onSendMobileMessage(String cookie,String respMsg);
}
