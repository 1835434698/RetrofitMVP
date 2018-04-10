package com.cnepay.android.swiper.core.model.network;

/**
 * created by millerJK on time : 2017/5/15
 * description :
 */

public class ResultException extends RuntimeException {

    private boolean isSuccess = true;
    private String msg;
    private String respTime;
    private String respCode;

    public ResultException(boolean isSuccess, String msg) {
        super(msg);
        this.isSuccess = isSuccess;
        this.msg = msg;
    }

    public ResultException(boolean isSuccess, String msg, String respTime, String respCode) {
        super(msg);
        this.isSuccess = isSuccess;
        this.msg = msg;
        this.respTime = respTime;
        this.respCode = respCode;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getMsg() {
        return msg;
    }

    public String getRespTime() {
        return respTime;
    }

    public void setRespTime(String respTime) {
        this.respTime = respTime;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }
}

