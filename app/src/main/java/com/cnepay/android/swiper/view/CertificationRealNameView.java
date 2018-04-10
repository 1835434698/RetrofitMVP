package com.cnepay.android.swiper.view;

import com.cnepay.android.swiper.bean.RealNameAuthBean;
import com.cnepay.android.swiper.core.view.MvpView;

/**
 * Created by Administrator on 2017/5/17.
 */

public interface CertificationRealNameView extends MvpView {
    void onOcrRevert();

    void onUploadComplete(String respMsg);

    void onEchoComplete(RealNameAuthBean bean);
}
