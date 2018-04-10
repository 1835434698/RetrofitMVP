package com.cnepay.android.swiper.bean;

import java.util.List;

/**
 * Created by wjl on 2017/5/17.
 */

public class BannerBean {
    String respTime;
    boolean isSuccess;
    String respCode;
    String respMsg;
    BannerHeardBean head;
    List<BannerBodyBean> body;


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

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    public BannerHeardBean getHead() {
        return head;
    }

    public void setHead(BannerHeardBean head) {
        this.head = head;
    }

    public List<BannerBodyBean> getBody() {
        return body;
    }

    public void setBody(List<BannerBodyBean> body) {
        this.body = body;
    }


    @Override
    public String toString() {
        return "BannerBean{" +
                "respTime='" + respTime + '\'' +
                ", isSuccess=" + isSuccess +
                ", respCode='" + respCode + '\'' +
                ", respMsg='" + respMsg + '\'' +
                ", head=" + head +
                ", body=" + body +
                '}';
    }




}
