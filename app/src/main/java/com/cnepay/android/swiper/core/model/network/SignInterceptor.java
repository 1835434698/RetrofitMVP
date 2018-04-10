package com.cnepay.android.swiper.core.model.network;

import android.text.TextUtils;

import com.cnepay.android.swiper.MainApp;
import com.cnepay.android.swiper.bean.LoginBean;
import com.cnepay.android.swiper.core.utils.BodyUtils;
import com.cnepay.android.swiper.core.utils.SignUtils;
import com.cnepay.android.swiper.model.Dictionary;
import com.cnepay.android.swiper.utils.Logger;
import com.cnepay.android.swiper.utils.Utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/4/24.
 * 用于签名的拦截器，所有接口将都进行签名（GET和POST），流忽略
 */

public class SignInterceptor implements Interceptor {

    private BodyUtils bodyUtils;
    private SimpleDateFormat DATETIME = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);

    public SignInterceptor() {
        bodyUtils = new BodyUtils();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request().newBuilder()
                .addHeader(MainApp.SESSION_HEADER, "" + (Dictionary.getLoginBean() == null ? null : Dictionary.getLoginBean().getSessionID()))
                .addHeader("Date", Utils.getHeaderDate()).build();
        Request request = new SignUtils().signParams(oldRequest);
        bodyUtils.logForRequest(request, chain.connection());

        //执行请求，计算请求时间
        long startNs = System.nanoTime();
        Response response = chain.proceed(request);
        if (500 <= response.code()) {
            throw new ResultException(false, "Internet  Server has some Error ,code is " + response.code());
        }
        refreshLoginBean(response);
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        //响应日志拦截
        return bodyUtils.logForResponse(response, tookMs);
    }

    private void refreshLoginBean(Response response) {
        Logger.i(getClass().getSimpleName(), "refreshLoginBean");
        Headers headers = response.headers();
        String newSID = headers.get(MainApp.SESSION_HEADER);
        String removeSID = headers.get("REMOVE-" + MainApp.SESSION_HEADER);
        LoginBean loginBean = Dictionary.getLoginBean();
        String curSID = null;
        if (loginBean != null) curSID = loginBean.getSessionID();

        if (!TextUtils.isEmpty(removeSID)) {//服务端踢下线
            if (loginBean != null)
                Dictionary.deactivateBean(loginBean.getObjectName(), true);
        } else if (!TextUtils.isEmpty(newSID)) {
            if (loginBean != null)
                curSID = loginBean.getSessionID();
            Logger.i(getClass().getSimpleName(), "refreshLoginBean newSID:" + newSID + " removeSID:" + removeSID + " curSID:" + curSID);
            if (!TextUtils.isEmpty(removeSID)) {//服务端踢下线
                if (loginBean != null)
                    Dictionary.deactivateBean(loginBean.getObjectName(), true);
            } else if (!TextUtils.isEmpty(newSID)) {
                if (newSID.equals(curSID))//新旧session一样
                    Dictionary.getActiveBean(loginBean.getObjectName(), true);
                else {//新旧session不一样
                    if (loginBean != null)//清掉老session
                        Dictionary.deactivateBean(loginBean.getObjectName(), true);
                    Dictionary.activateBean(new LoginBean(newSID), MainApp.SESSION_LOGIN_EXPIRED_TIME);
                }
            }
        }
    }
}