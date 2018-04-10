package com.cnepay.android.swiper.core.presenter;

import com.cnepay.android.swiper.MainApp;
import com.cnepay.android.swiper.bean.DeviceBean;
import com.cnepay.android.swiper.bean.LoginBean;
import com.cnepay.android.swiper.core.model.SessionProvider;
import com.cnepay.android.swiper.core.view.SessionView;
import com.cnepay.android.swiper.model.Dictionary;
import com.cnepay.config.DevConfig;
import com.cnepay.device.DeviceType;

/**
 * Created by Administrator on 2017/4/25.
 */

public class SessionPresenter extends MvpPresenterIml<SessionView> {

    protected SessionProvider mSessionProvider;

    public LoginBean getLoginBean() {
        LoginBean loginBean = Dictionary.getLoginBean();
        if (loginBean==null)
            signOff();
        return loginBean;
    }

    public void signOff(){
        if (getView().signOff()){
//        AnalyticsUtils.onProfileSignOff();友盟统计 注销
//        JPushInterface.setAlias(ui.activity, "", null);//取消极光推送的Alias值
//        UpdateModel.fileOfAPK = null;在线更新模块资源释放

            //对session释放
            LoginBean loginBean = Dictionary.getLoginBean();
            Dictionary.deactivateBean((loginBean==null?null:loginBean.getObjectName()),true);
//        Options options = new Options();
//        options.put(MainApp.NEED_CUSTOM_HEADERS, loginBean.getId());
            // TODO: 2017/5/19 是否需要通知服务器客户端下线

            //对设备释放
            MainApp.getDeviceManager().interrupt();
            MainApp.getDeviceManager().disconnect(); // FIXME cannot di sconnect during trading
            MainApp.getDeviceManager().stopSearchDevice();
            DeviceBean deviceBean = loginBean==null?null:loginBean.getDeviceBean();
            int devType = deviceBean==null? DeviceType.DEV_TYPE_UNKNOWN: deviceBean.getDevType();
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
}
