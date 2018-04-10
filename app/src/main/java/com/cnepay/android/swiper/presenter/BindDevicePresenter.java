package com.cnepay.android.swiper.presenter;

import com.cnepay.android.swiper.MainApp;
import com.cnepay.android.swiper.core.presenter.MvpPresenterIml;
import com.cnepay.android.swiper.utils.Logger;
import com.cnepay.android.swiper.view.BindDeviceView;
import com.cnepay.device.DevInfo;
import com.cnepay.manager.IDeviceManager;
import com.cnepay.manager.NotifyListener;
import com.cnepay.manager.SimpleNotifyListener;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Xushiwei on 2017/5/17.
 */

public class BindDevicePresenter extends MvpPresenterIml<BindDeviceView> {
    public static final int DEVICE_BUSY=1;

    private IDeviceManager deviceManager;

    @Override
    public void attachView(BindDeviceView view) {
        super.attachView(view);
        deviceManager = MainApp.getDeviceManager();
        deviceManager.register(listener);
    }

    @Override
    public void detachView(boolean saveInstance) {
        super.detachView(saveInstance);
        deviceManager.unRegister(listener);
    }

    public void searchDevice(){
        Logger.e(getClass().getSimpleName(), "searchDevice");
        getView().switchSearchView(true);
        if (deviceManager.isCurrentStateOf(NotifyListener.State.UNCONNECT)) {
            deviceManager.searchDevice(MainApp.getMainApp().getSearchDeviceTimeout());
        }else
            getView().toast(DEVICE_BUSY);
    }

    private SimpleNotifyListener listener = new SimpleNotifyListener(){
        @Override
        public void onFindDevice(Map<String, DevInfo> devInfos) {
            ArrayList<DevInfo> scanResults = new ArrayList<>(devInfos.values());
            Logger.e(BindDevicePresenter.this.getClass().getSimpleName(), "onFindDevice scanResults:"+scanResults);
            // TODO: 2017/5/20 整合 过滤 显示在RecyclerView中
        }
    };

    public void cancel() {
        // TODO: 2017/5/20 区分当前状态
        if (deviceManager.isCurrentStateOf(NotifyListener.State.SEARCHING)) {//搜索中     取消搜索
            deviceManager.stopSearchDevice();
        }else if (deviceManager.isCurrentStateOf(NotifyListener.State.CONNECTING)) {//连接中     取消连接
            deviceManager.interrupt();
        }else if (deviceManager.isCurrentStateOf(NotifyListener.State.UNCONNECT)){//未连接状态      普通取消
            getView().switchSearchView(false);
        }else {//其他      设备忙
            getView().toast(DEVICE_BUSY);
        }
    }
}
