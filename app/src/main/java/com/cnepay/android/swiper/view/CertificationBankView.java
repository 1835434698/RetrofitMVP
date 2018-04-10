package com.cnepay.android.swiper.view;

import com.cnepay.android.swiper.bean.BankQueryBean;
import com.cnepay.android.swiper.core.view.MvpView;

/**
 * Created by xugang on 2017/5/19.
 */

public interface CertificationBankView extends MvpView {
    void onFillRecycler(BankQueryBean bankQueryBean);
}
