package com.cnepay.android.swiper.view;

import com.cnepay.android.swiper.bean.LoginBean;
import com.cnepay.android.swiper.core.view.MvpView;

/**
 * created by millerJK on time : 2017/5/22
 * description : 获取商户资质
 */

public interface MerchantQualifyView extends MvpView {

    void updateUI(LoginBean.MerchantQualify entity);
}
