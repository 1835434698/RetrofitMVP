package com.cnepay.android.swiper;

import android.app.Application;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.multidex.MultiDex;

import com.cnepay.android.swiper.bean.DeviceBean;
import com.cnepay.android.swiper.bean.LoginBean;
import com.cnepay.android.swiper.core.model.RetrofitProvider;
import com.cnepay.android.swiper.model.Dictionary;
import com.cnepay.android.swiper.model.SDCard;
import com.cnepay.android.swiper.utils.LocationUtils;
import com.cnepay.android.swiper.utils.Logger;
import com.cnepay.android.swiper.utils.Utils;
import com.cnepay.config.DevConfig;
import com.cnepay.device.DevInfo;
import com.cnepay.device.DeviceType;
import com.cnepay.manager.DeviceManager;
import com.cnepay.manager.IDeviceManager;
import com.cnepay.manager.SimpleNotifyListener;

import java.net.MalformedURLException;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2017/4/25.
 */

public class MainApp extends Application {
    public static final String WEB_ENVIRONMENT_DEVELOP = "Develop";
    public static final String WEB_ENVIRONMENT_BRANCH = "Branch";
    public static final String WEB_ENVIRONMENT_TRUNK = "Trunk";//主干
    public static final String WEB_ENVIRONMENT_PRODUCT = "Product";
    public static final String WEB_ENVIRONMENT = WEB_ENVIRONMENT_DEVELOP;//此处切换环境
    public static final String APP_VERSION = "android.wealth.3.0.0";
    public static String SESSION_HEADER = null;//会话session
    public static int SESSION_LOGIN_EXPIRED_TIME;//登录session的默认时长
    public static final boolean PRODUCT_DEBUG = true;//为true表示在生产环境开启debug模式
    private static MainApp mainApp;
    private IDeviceManager deviceManager;
    private int SEARCH_DEVICE_TIMEOUT = -1;

    private String URL_BASE;
    private String URL_H5;
    private String URL_UPDATE;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        SESSION_HEADER = getString(R.string.session_header_name);
        SESSION_LOGIN_EXPIRED_TIME = getResources().getInteger(R.integer.session_expired_time_login);
        Dictionary.init(this);
        mainApp = this;
        LocationUtils.createInstance(this).startLocation(null);
        initJpush();
        initUrl();
        SDCard.init(this);
        RetrofitProvider.init(URL_BASE);

        deviceManager = DeviceManager.createInstance(this);
        deviceManager.register(deviceListener);
        initConfigs();
    }

    private void initJpush() {
        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush
    }

    private void initUrl() {
        String url_base_temp;
        String url_h5_temp;
        String url_update_temp;
        switch (WEB_ENVIRONMENT) {
            default:
            case WEB_ENVIRONMENT_DEVELOP:
                url_base_temp = getString(R.string.url_base_develop);
                url_h5_temp = getString(R.string.url_h5_develop);
                url_update_temp = getString(R.string.url_update_develop);
                break;
            case WEB_ENVIRONMENT_BRANCH:
                url_base_temp = getString(R.string.url_base_branch);
                url_h5_temp = getString(R.string.url_h5_branch);
                url_update_temp = getString(R.string.url_update_branch);
                break;
            case WEB_ENVIRONMENT_TRUNK:
                url_base_temp = getString(R.string.url_base_trunk);
                url_h5_temp = getString(R.string.url_h5_trunk);
                url_update_temp = getString(R.string.url_update_trunk);
                break;
            case WEB_ENVIRONMENT_PRODUCT:
                url_base_temp = getString(R.string.url_base_product);
                url_h5_temp = getString(R.string.url_h5_product);
                url_update_temp = getString(R.string.url_update_product);
                break;
        }
        try {
            URL_BASE = Utils.getStandardUrl(url_base_temp, "/");
            URL_H5 = Utils.getStandardUrl(url_h5_temp, "/");
            URL_UPDATE = Utils.getStandardUrl(url_update_temp, "/");
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("interface host url format is not correct");
        }
    }

    /**
     * 此处初始化各全局数据
     */
    private void initConfigs() {
        SEARCH_DEVICE_TIMEOUT = getResources().getInteger(R.integer.search_device_timeout);
    }

    private SimpleNotifyListener deviceListener = new SimpleNotifyListener() {
        @Override
        public void onConnectDevSuccess(DevInfo info) {
            Uri uri = Uri.parse("android.resource://" + getPackageName()
                    + "/" + R.raw.ring);
            Ringtone r = RingtoneManager.getRingtone(MainApp.this, uri);
            r.play();
        }

        @Override
        public void onDevPlugged() {
            Logger.i(MainApp.class.getSimpleName(), "onDevPlugged");
        }
    };

    @Override
    public void onTerminate() {
        super.onTerminate();
        mainApp = null;
        deviceManager.unRegister(deviceListener);
    }

    public static MainApp getMainApp() {
        return mainApp;
    }

    public static String getAppVersion() {
        return APP_VERSION;
    }

    public static IDeviceManager getDeviceManager() {
        return mainApp.deviceManager;
    }

    public int getSearchDeviceTimeout() {
        return SEARCH_DEVICE_TIMEOUT;
    }

    public void release() {
//        AnalyticsUtils.onProfileSignOff();友盟统计 注销
//        JPushInterface.setAlias(ui.activity, "", null);//取消极光推送的Alias值
//        UpdateModel.fileOfAPK = null;在线更新模块资源释放

        //对session释放
        LoginBean loginBean = Dictionary.getLoginBean();
        if (loginBean == null)
            return;
        Dictionary.deactivateBean(loginBean.getObjectName(), true);
//        Options options = new Options();
//        options.put(MainApp.NEED_CUSTOM_HEADERS, loginBean.getId());
        // TODO: 2017/5/19 通知服务器客户端下线

        //对设备释放
        deviceManager.interrupt();
        deviceManager.disconnect(); // FIXME cannot di sconnect during trading
        deviceManager.stopSearchDevice();
        DeviceBean deviceBean = loginBean.getDeviceBean();
        int devType = deviceBean == null ? DeviceType.DEV_TYPE_UNKNOWN : deviceBean.getDevType();
        if (devType != DeviceType.DEV_TYPE_UNKNOWN) {
            DevConfig config = DevConfig.getConfigByType(devType);
            config.clearTargetKsn();
            config.clearAid();
            config.clearRid();
            config.clearWorkKey();
            config.setNeedUpdateWorkKey(true);
            config.setNeedUpdateICKey(true);
        }
    }
}
