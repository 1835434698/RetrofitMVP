package com.cnepay.android.swiper.view;

import com.cnepay.android.swiper.core.view.MvpView;

/**
 * Created by Administrator on 2017/5/16.
 */

public interface CertificationTakePhoto extends MvpView {


    void requestNecessaryPermission();

    void showPhotoPopupWindows();

    void takePhoto();

}
