package com.cnepay.android.swiper.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.util.List;

public class LocationUtils {

    AMapLocationClient mAMapLocationClient;
    AMapLocationClientOption mLocationOption;
    private OnLocationListener listener;
    private String TAG = "LocationUtils";
    private Location currLocation;
    private Context context;
    private static LocationUtils location;
    private LocationManager locationManager;
    public static final int ERROR_RADIUS = 1000;
    private long locationTime;
    private List<String> intervalProviders;
    // 设置发送定位请求的时间间隔，单位毫秒（最小值为1000，如果小于1000，按照1000算）
    private static long INTERVAL = 3 * 60 * 1000L; //5分钟定位一次
    //这个时间之内，最后一次获取的地理坐标有效，超过了需要重新获取最新的。
    public static final long POSITION_VALIDITY_TIME = 90 * 1000l;
    //获取定位的超时时长，超过该时间后，发出失败回调，但会继续进行地理信息获取。
    public static long TIMEOUT_LONG = 15000L;


    private LocationUtils(Context ctx) {
        this.context = ctx;
        locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        intervalProviders = locationManager.getAllProviders();
    }

    public static LocationUtils createInstance(Context ctx) {
        if (location == null) {
            location = new LocationUtils(ctx);
        }
        return location;
    }

    public static LocationUtils getInstance() {
        return location;
    }

    public boolean isEnable() {
        for (String provider : intervalProviders) {
            if (locationManager.isProviderEnabled(provider)) return true;
        }
        return false;
    }


    public void startLocation(OnLocationListener listener) {
        startLocation(listener, TIMEOUT_LONG);
    }

    public void startLocation(OnLocationListener listener, long timeout) {
        TIMEOUT_LONG = timeout;
        this.listener = listener;

        destroy();
        mAMapLocationClient = new AMapLocationClient(context);
        mLocationOption = getDefaultOption();
        mAMapLocationClient.setLocationOption(mLocationOption);
        mAMapLocationClient.setLocationListener(amapListener);
        mAMapLocationClient.startLocation();

    }

    public AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(TIMEOUT_LONG);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(INTERVAL);//可选，设置定位间隔。默认为3分钟
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    private AMapLocationListener amapListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {

            int errorCode = aMapLocation.getErrorCode();
            String errorInfo = aMapLocation.getErrorInfo();

            Logger.i(TAG, "amapListener onLocationChanged  aMapLocation:" + aMapLocation + "   errorCode:" + errorCode);
            if (0 == errorCode) {
                if (aMapLocation.getAccuracy() <= ERROR_RADIUS) {
                    currLocation = aMapLocation;
                    locationTime = System.currentTimeMillis();
                }
                if (listener != null) {
                    listener.onLocationSuccess(aMapLocation);
                }
            } else {
                Logger.i(TAG, "errorCode:" + errorCode + "   errorInfo: " + errorInfo + "   locationDetail" + aMapLocation.getLocationDetail());
                if (listener != null) {
                    listener.onLocationFail(errorCode, errorInfo);
                }
            }
        }
    };


    public Location fetchCurrLocation() {
        if (System.currentTimeMillis() - locationTime > POSITION_VALIDITY_TIME
                || !isEnable()) {
            return null;
        }
        return currLocation;
    }

    private void stopLocation() {
        if (mAMapLocationClient != null && mAMapLocationClient.isStarted()) {
            mAMapLocationClient.stopLocation();
        }

    }

    public void destroy() {
        if (mAMapLocationClient != null) {
            stopLocation();
            mAMapLocationClient.onDestroy();
        }
        mAMapLocationClient = null;
        mLocationOption = null;
    }

    public interface OnLocationListener {
        void onLocationSuccess(Location location);

        void onLocationFail(int code, String msg);
    }

}
