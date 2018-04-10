package com.cnepay.android.swiper.view;

import com.cnepay.android.swiper.bean.AccountAuthBean;
import com.cnepay.android.swiper.bean.BaseBean;
import com.cnepay.android.swiper.core.view.MvpView;

/**
 * Created by Administrator on 2017/5/19.
 */

public interface CertificationAccountView extends MvpView {

    void onOcrRevert();

    void setAuthInfo(String name, String identityCard);

    void onSendVCode(BaseBean baseBean);

    void onUploadComplete(String respMsg);

    void onEchoComplete(AccountAuthBean bean);
}
