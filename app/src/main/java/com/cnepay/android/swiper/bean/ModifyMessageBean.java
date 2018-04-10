package com.cnepay.android.swiper.bean;

/**
 * Created by wjl on 2017/5/16.
 * 修改消息的状态
 */

public class ModifyMessageBean {

    String respTime;
    boolean isSuccess;
    String respCode;
    String respMsg;
    String des;
    String messageId;


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

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    @Override
    public String toString() {
        return "ModifyMessageBean{" +
                "respTime='" + respTime + '\'' +
                ", isSuccess=" + isSuccess +
                ", respCode='" + respCode + '\'' +
                ", respMsg='" + respMsg + '\'' +
                ", des='" + des + '\'' +
                ", messageId='" + messageId + '\'' +
                '}';
    }
}
