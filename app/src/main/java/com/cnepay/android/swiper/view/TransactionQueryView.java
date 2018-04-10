package com.cnepay.android.swiper.view;

import com.cnepay.android.swiper.bean.TransactionListBean;
import com.cnepay.android.swiper.core.view.MvpView;

/**
 * created by millerJK on time : 2017/5/11
 * description : 交易查询
 */

public interface TransactionQueryView extends MvpView {

    void resetData(TransactionListBean value);

    void appendData(TransactionListBean value);

    void connectTimeout();

}
