package com.cnepay.android.swiper.view;

import com.cnepay.android.swiper.bean.CardMangeBean;
import com.cnepay.android.swiper.core.view.MvpView;

/**
 * created by millerJK on time : 2017/5/17
 * description : 添加/删除 信用卡
 */

public interface CardManageView extends MvpView {

    void success(CardMangeBean value);
}
