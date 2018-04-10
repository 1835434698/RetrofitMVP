package com.cnepay.android.swiper.core.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.AnimRes;
import android.text.TextUtils;

import com.cnepay.android.swiper.MainApp;
import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.activity.HomePageActivity;
import com.cnepay.android.swiper.activity.LoginActivity;
import com.cnepay.android.swiper.activity.WebViewActivity;
import com.cnepay.android.swiper.core.model.RetrofitProvider;
import com.cnepay.android.swiper.core.view.MvpView;
import com.cnepay.android.swiper.utils.Logger;
import com.cnepay.android.swiper.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2017/4/20.
 */

public final class ActivityTransformationController extends MvpPresenterIml<MvpView> {

    private
    @AnimRes
    int enterAnim = -1;
    private
    @AnimRes
    int exitAnim = -1;
    private static final int REQUEST_CODE_CALLBACK = 0x1000 >> 2;
    private static final int RESULT_CODE_RESPONSE = 0x1001 >> 2;
    private static final String EXTRA_ACTIVITY_NAME = "_extra_activity_name";
    private static final String EXTRA_IS_FROM_ASSIGNED = "isFromAssigned";

    public void setEnterAnim(int enterAnim) {
        this.enterAnim = enterAnim;
    }

    public void setExitAnim(int exitAnim) {
        this.exitAnim = exitAnim;
    }

    public void enterAnimation() {
        getView().obtainActivity().overridePendingTransition(-1 == enterAnim ? R.anim.slide_in_right : enterAnim, -1 == exitAnim ? R.anim.slide_out_left : exitAnim);
    }

    public void quitAnimation() {
        getView().obtainActivity().overridePendingTransition(-1 == enterAnim ? R.anim.slide_in_left : enterAnim, -1 == exitAnim ? R.anim.slide_out_right : exitAnim);
    }

    public void startWebActivity(Context context, int titleType, String prameter, String title) {
        startWebActivity(context, titleType, prameter, title, false);
    }
    public void startWebActivity(Context context, int titleType, String prameter, String title, boolean isNeedSession) {
        Intent intent = new Intent(context, WebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(WebViewActivity.TITLE_SELECT, titleType);
        bundle.putString(WebViewActivity.TITLE_TEXT, title);
        bundle.putBoolean(WebViewActivity.ISNEDDSESSION, isNeedSession);
        bundle.putString(WebViewActivity.CURR_URL, RetrofitProvider.getUrlBase()+"showHtml.action?html="+prameter+"&reqTime="+ new SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(new Date())+"&appVersion="+MainApp.getMainApp().getAppVersion());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void startActivity(Intent intent) {
        getView().obtainContext().startActivity(intent);
        enterAnimation();
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        getView().obtainActivity().startActivityForResult(intent, requestCode);
        enterAnimation();
    }

    public void startCallbackActivity(Intent intent) {
        getView().obtainActivity().startActivityForResult(intent, REQUEST_CODE_CALLBACK);
        enterAnimation();
    }

    public void startResponseActivity(Intent intent) {
        getView().obtainActivity().setResult(RESULT_CODE_RESPONSE, intent);
        getView().obtainActivity().finish();
    }

    public void startActivityFromAssignedActivity(Intent intent, Class<? extends Activity> cls) {
        startActivityFromAssignedActivity(intent, cls, true);
    }

    public void startActivityFromAssignedActivity(Intent intent, Class<? extends Activity> cls, boolean isFromAssigned) {
        getView().obtainActivity().setResult(RESULT_CODE_RESPONSE, intent);
        intent.putExtra(EXTRA_ACTIVITY_NAME, cls.getCanonicalName());
        intent.putExtra(EXTRA_IS_FROM_ASSIGNED, isFromAssigned);
        getView().obtainActivity().finish();
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent intent) {
        boolean isFromAssigned = null != intent && intent.getBooleanExtra(EXTRA_IS_FROM_ASSIGNED, false);
        Logger.d(getClass().getSimpleName(), "onActivityResult isFromAssigned"+isFromAssigned);
        if (REQUEST_CODE_CALLBACK == requestCode && RESULT_CODE_RESPONSE == resultCode && null != intent) {
            Logger.d(getClass().getSimpleName(), "onActivityResult 1 pass");
            try {
                String activityName = intent.getStringExtra(EXTRA_ACTIVITY_NAME);
                if (!TextUtils.isEmpty(activityName)) {
                    Logger.d(getClass().getSimpleName(), "onActivityResult activityName:"+activityName+" "+getView().obtainActivity().getClass().getCanonicalName());
                    Class<? extends Activity> cls = (Class<? extends Activity>) Class.forName(activityName);
                    if (activityName.equals(getView().obtainActivity().getClass().getCanonicalName())) {
                        intent.removeExtra(EXTRA_ACTIVITY_NAME);
                        if (!isFromAssigned) {
                            getView().obtainActivity().finish();
                        }
                        if (null != intent.getComponent())
                            startCallbackActivity(intent);
                        return true;
                    }
                    startActivityFromAssignedActivity(intent, cls, isFromAssigned);
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            startCallbackActivity(intent);
            return true;
        }
        return false;
    }
}
