package com.cnepay.android.swiper.core.model.network;

/**
 * created by millerJK on time : 2017/5/15
 * description :
 */

public class AppException extends Exception {

    private final int code;
    private String displayMessage;
    private String respCode;
    private String requestTime;

    public static final int UNKNOWN = 1000;
    public static final int PARSE_ERROR = 1001;
    public static final int SERVER_RETURN_ERROR = 1002;
    public static final int CONNECT_TIME_OUT = 1003;

    public AppException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getDisplayMessage() {
        return displayMessage;
    }

    public void setDisplayMessage(String msg) {
        this.displayMessage = msg + "(code:" + code + ")";
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespTime() {
        return requestTime;
    }

    public void setRespTime(String requestTime) {
        this.requestTime = requestTime;
    }
}

