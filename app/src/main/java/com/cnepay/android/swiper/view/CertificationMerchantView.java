package com.cnepay.android.swiper.view;

import com.cnepay.android.swiper.bean.BaseBean;
import com.cnepay.android.swiper.core.view.MvpView;

/**
 * Created by Administrator on 2017/5/18.
 */

public interface CertificationMerchantView extends MvpView {

    void onUploadComplete(String respMsg);

    void onEchoComplete(BaseBean bean);
}
