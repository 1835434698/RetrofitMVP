package com.cnepay.android.swiper.core.model.network;

import android.net.ParseException;

import com.cnepay.android.swiper.utils.Logger;
import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * created by millerJK on time : 2017/5/15
 * description :
 */

public abstract class CusObserver<T> implements Observer<T> {

    //对应HTTP的状态码
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;


    @Override
    public void onError(Throwable e) {
        Throwable throwable = e;
        //获取最根源的异常
        while (throwable.getCause() != null) {
            e = throwable;
            throwable = throwable.getCause();
        }

        AppException ex;
        if (e instanceof HttpException) {             //HTTP错误
            HttpException httpException = (HttpException) e;
            ex = new AppException(e, httpException.code());
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                    ex.setDisplayMessage("权限错误");
                    break;
                case REQUEST_TIMEOUT:
                case NOT_FOUND:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    ex.setDisplayMessage("网络连接异常");  //均视为网络错误
                    break;
            }
            onError(ex);
        } else if (e instanceof ResultException) {    //服务器返回的错误
            ResultException resultException = (ResultException) e;
            ex = new AppException(resultException, AppException.SERVER_RETURN_ERROR);
            ex.setRespCode(resultException.getRespCode());
            ex.setRespTime(resultException.getRespTime());
            ex.setDisplayMessage(((ResultException) e).getMsg());
            onError(ex);
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            ex = new AppException(e, AppException.PARSE_ERROR);
            ex.setDisplayMessage("数据解析异常");            //均视为解析错误
            onError(ex);
        } else if (e instanceof SocketTimeoutException) {
            ex = new AppException(e, AppException.CONNECT_TIME_OUT);
            ex.setDisplayMessage("网络连接超时");  //均视为网络错误
            onError(ex);
        } else if (e instanceof ConnectException) {
            ex = new AppException(e, AppException.CONNECT_TIME_OUT);
            ex.setDisplayMessage(ex.getMessage());  //均视为网络错误
            onError(ex);
        } else {
            ex = new AppException(e, AppException.UNKNOWN);
            ex.setDisplayMessage("未知错误");          //未知错误
            onError(ex);
        }
        Logger.e("xxx", "onError: " + ex.getMessage());
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    protected abstract void onError(AppException ex);

    @Override
    public void onComplete() {

    }


}
