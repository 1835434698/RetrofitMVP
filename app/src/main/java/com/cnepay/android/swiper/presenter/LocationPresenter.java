package com.cnepay.android.swiper.presenter;

import android.location.Location;

import com.cnepay.android.swiper.core.presenter.MvpPresenterIml;
import com.cnepay.android.swiper.utils.LocationUtils;
import com.cnepay.android.swiper.view.LocationView;

/**
 * created by millerJK on time : 2017/5/22
 * description : 定位
 */

public class LocationPresenter extends MvpPresenterIml<LocationView> {

    public void location() {

        if (isViewAttached()) {
            getView().showLoading();
        } else return;

        Location location = LocationUtils.getInstance().fetchCurrLocation();
        if (location == null) {
//            if (!LocationUtils.getInstance().isEnable()) {
//                if (isViewAttached()) {
//                    getView().permissionDeny();
//                }
//            } else {
            LocationUtils.OnLocationListener listener = new MyLocationListener();
            LocationUtils.getInstance().startLocation(listener);
//            }
            return;
        } else
            getView().locationSuccess(location.getLongitude() + "," + location.getLatitude());
    }

    private class MyLocationListener implements LocationUtils.OnLocationListener {
        @Override
        public void onLocationSuccess(Location location) {
            if (isViewAttached()) {
                getView().locationSuccess(location.getLongitude() + "," + location.getLatitude());
            }
        }

        @Override
        public void onLocationFail(int code, String msg) {
            if (isViewAttached()) {
                getView().connectSuccess(null);
                getView().locationFailed(code);
            }
        }
    }
}
