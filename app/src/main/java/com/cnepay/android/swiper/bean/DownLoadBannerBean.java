package com.cnepay.android.swiper.bean;

import android.graphics.Bitmap;

import java.util.Arrays;

/**
 * Created by wjl on 2017/5/20.
 */

public class DownLoadBannerBean  {
    String respTime;
    boolean isSuccess;
    String respCode;
    Bitmap file;
    String respMsg;

    public String getRespTime() {
        return respTime;
    }

    public void setRespTime(String respTime) {
        this.respTime = respTime;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public Bitmap getFile() {
        return file;
    }

    public void setFile(Bitmap file) {
        this.file = file;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    @Override
    public String toString() {
        return "DownLoadBannerBean{" +
                "respTime='" + respTime + '\'' +
                ", isSuccess=" + isSuccess +
                ", respCode='" + respCode + '\'' +
                ", file=" + file +
                ", respMsg='" + respMsg + '\'' +
                '}';
    }
}
