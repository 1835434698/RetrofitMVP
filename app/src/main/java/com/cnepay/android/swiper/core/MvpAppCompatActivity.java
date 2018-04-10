package com.cnepay.android.swiper.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.activity.HomePageActivity;
import com.cnepay.android.swiper.activity.LoginActivity;
import com.cnepay.android.swiper.bean.LoginBean;
import com.cnepay.android.swiper.core.model.network.AppException;
import com.cnepay.android.swiper.core.presenter.ActivityInflaterController;
import com.cnepay.android.swiper.core.presenter.ActivityTransformationController;
import com.cnepay.android.swiper.core.presenter.SessionPresenter;
import com.cnepay.android.swiper.core.view.MvpView;
import com.cnepay.android.swiper.core.view.SessionView;
import com.cnepay.android.swiper.utils.Logger;
import com.cnepay.android.swiper.utils.Utils;
import com.cnepay.android.swiper.widget.LoadingDialog;


/**
 * Created by xugang on 2017/4/24.
 * Describe
 */

public class MvpAppCompatActivity extends AppCompatActivity implements MvpView, SessionView {
    protected ActivityTransformationController ac;
    protected ActivityInflaterController uc;
    protected SessionPresenter mSession;
    protected LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ac = new ActivityTransformationController();
        uc = new ActivityInflaterController();

        mSession = new SessionPresenter();
        ac.attachView(this);
        mSession.attachView(this);
        uc.attachView(this);
        mLoadingDialog = new LoadingDialog(this, R.style.LoadingDialog);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (uc.getBackIcon() != null)
            uc.getBackIcon().setOnClickListener(v -> onBackPressed());
    }

    public boolean declarationPermissions(String permission, int code) {
        if (!(PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, permission)) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, code);
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!ac.onActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ac.quitAnimation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ac.detachView(false);
        mSession.detachView(false);
        uc.detachView(false);
    }

    @Override
    public Context obtainContext() {
        return this;
    }

    @Override
    public Activity obtainActivity() {
        return this;
    }

    @Override
    public boolean signOff() {
        if (isFinishing())
            return false;
        Intent i = new Intent(this, LoginActivity.class);
        if (getClass() == HomePageActivity.class) {
            Logger.d(getClass().getSimpleName(), "当前activity为HomePageActivity");
            finish();
            startActivity(i);
        } else if (Utils.isBaseActivityOfLauncher(HomePageActivity.class, this)) {
            Logger.d(getClass().getSimpleName(), "当前activity所处的栈的栈底是HomePageActivity");
            ac.startActivityFromAssignedActivity(i, HomePageActivity.class, false);
        } else
            Logger.e(getClass().getSimpleName(), "当前activity所处的栈的栈底不是HomePageActivity(异常情况signOff)");
        return true;
    }

    @Override
    public void showLoading() {
        mLoadingDialog.show();
    }

    @Override
    public void connectError(AppException error) {
        if (mLoadingDialog != null)
            mLoadingDialog.dismiss();
        uc.toast(error.getDisplayMessage());
    }

    @Override
    public void connectSuccess(Object content) {
        if (mLoadingDialog != null)
            mLoadingDialog.dismiss();
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
