package com.cnepay.android.swiper.presenter;

import com.cnepay.android.swiper.MainApp;
import com.cnepay.android.swiper.bean.MineMessageBean;
import com.cnepay.android.swiper.bean.ModifyMessageBean;
import com.cnepay.android.swiper.core.model.RetrofitProvider;
import com.cnepay.android.swiper.core.model.network.AppException;
import com.cnepay.android.swiper.core.model.network.CusObserver;
import com.cnepay.android.swiper.core.presenter.MvpPresenterIml;
import com.cnepay.android.swiper.utils.Logger;
import com.cnepay.android.swiper.view.MineMessageModifyView;
import com.cnepay.android.swiper.view.MineMessageQueryView;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * created by millerJK on time : 2017/5/11
 * description :
 */

public class ModifyMessagePresenter extends MvpPresenterIml<MineMessageModifyView> {
    private static final String TAG = "ModifyMessagePresenter";

    /**
     * 标记 未读消息位已读
     * @param messageId
     * @param detail  一般用false
     */
    public void modifyMessage( String messageId,boolean detail) {
        Logger.i("wjl","执行查询点击为已读");
        Map<String,Object> mapParams = new HashMap<>();
        mapParams.put("appVersion", MainApp.APP_VERSION);
        mapParams.put("messageId", messageId);
        mapParams.put("detail", detail);

        if (isViewAttached())
            getView().showLoading();
        else
            return;

        RetrofitProvider.create()
                .modifyMessage(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CusObserver<ModifyMessageBean>() {

                    @Override
                    public void onNext(ModifyMessageBean value) {
                        Logger.i("wjl", "修改消息onNext: ");
                        if (isViewAttached()) {
                            getView().connectSuccess(value);
                            getView().modifyMessage(value);
                        }

                    }

                    @Override
                    protected void onError(AppException ex) {
                        Logger.i("wjl", "修改消息onError: "+ex.toString());
                        if (isViewAttached()) {
                            getView().connectError(ex);
                        }
                    }
                });
    }
}
