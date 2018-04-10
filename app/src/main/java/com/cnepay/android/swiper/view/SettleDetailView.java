package com.cnepay.android.swiper.view;

import com.cnepay.android.swiper.bean.AccountDetailBean;
import com.cnepay.android.swiper.core.view.MvpView;

/**
 * created by millerJK on time : 2017/5/15
 * description :
 */

public interface SettleDetailView extends MvpView {

    void updateUI(AccountDetailBean.TranInfoEntity entity);
}
