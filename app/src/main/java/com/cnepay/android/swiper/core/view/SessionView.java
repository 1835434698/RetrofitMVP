package com.cnepay.android.swiper.core.view;

/**
 * Created by Administrator on 2017/4/25.
 */

public interface SessionView extends MvpView {
    /**
     * 下线逻辑不能直接在Activity或Fragment中调用，必须交由{@link com.cnepay.android.swiper.core.presenter.SessionPresenter}统一处理。通过{@link #requestSignOff()}调用
     * @return 登出界面操作正在处理中时返回 {@code false}
     * otherwise {@code true}
     */
    @Deprecated
    boolean signOff();
}
