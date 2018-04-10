package com.cnepay.android.swiper.view;

import com.cnepay.android.swiper.bean.BankListBean;
import com.cnepay.android.swiper.core.view.MvpView;

/**
 * created by millerJK on time : 2017/5/17
 * description :
 */

public interface BankListView extends MvpView {

    void showList(BankListBean bean);
}
