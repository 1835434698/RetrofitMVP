package com.cnepay.android.swiper.view;

import com.cnepay.android.swiper.activity.SettleQueryActivity;
import com.cnepay.android.swiper.bean.SettleListBean;
import com.cnepay.android.swiper.core.view.MvpView;

/**
 * created by millerJK on time : 2017/5/11
 * description : 结算查询
 * {@link SettleQueryActivity}
 */

public interface SettleQueryView extends MvpView {

    void resetData(SettleListBean value);

    void appendData(SettleListBean value);

    void connectTimeout();

}
