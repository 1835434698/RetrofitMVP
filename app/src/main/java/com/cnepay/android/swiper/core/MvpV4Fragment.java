package com.cnepay.android.swiper.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.cnepay.android.swiper.MainApp;
import com.cnepay.android.swiper.activity.HomePageActivity;
import com.cnepay.android.swiper.activity.LoginActivity;
import com.cnepay.android.swiper.bean.LoginBean;
import com.cnepay.android.swiper.core.model.network.AppException;
import com.cnepay.android.swiper.core.presenter.ActivityTransformationController;
import com.cnepay.android.swiper.core.presenter.SessionPresenter;
import com.cnepay.android.swiper.core.view.MvpView;
import com.cnepay.android.swiper.core.view.SessionView;
import com.cnepay.android.swiper.utils.Logger;
import com.cnepay.android.swiper.utils.Utils;

/**
 * Created by xugang on 2017/4/24.
 * Describe
 */

public class MvpV4Fragment extends Fragment implements MvpView, SessionView {
    protected ActivityTransformationController ac;
    protected SessionPresenter mSession;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ac = new ActivityTransformationController();
        mSession = new SessionPresenter();
        ac.attachView(this);
        mSession.attachView(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!ac.onActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ac.detachView(false);
        mSession.detachView(false);
    }

    @Override
    public Context obtainContext() {
        return getContext();
    }

    @Override
    public Activity obtainActivity() {
        return getActivity();
    }

    @Override
    public boolean signOff() {
        if (obtainActivity().isFinishing())
            return false;
        Intent i = new Intent(obtainActivity(), LoginActivity.class);
        if (obtainActivity().getClass() == HomePageActivity.class) {
            Logger.d(getClass().getSimpleName(), "当前activity为HomePageActivity");
            obtainActivity().finish();
            startActivity(i);
        } else if (Utils.isBaseActivityOfLauncher(HomePageActivity.class, obtainActivity())) {
            Logger.d(getClass().getSimpleName(), "当前activity所处的栈的栈底是HomePageActivity");
            ac.startActivityFromAssignedActivity(i, HomePageActivity.class, false);
        } else
            Logger.e(getClass().getSimpleName(), "当前activity所处的栈的栈底不是HomePageActivity(异常情况signOff)");
        return true;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void connectError(AppException error) {

    }

    @Override
    public void connectSuccess(Object content) {

    }

    @Override
    public LoginBean requestLoginBean() {
        return mSession.getLoginBean();
    }

    @Override
    public void requestSignOff() {
        mSession.signOff();
    }
}
