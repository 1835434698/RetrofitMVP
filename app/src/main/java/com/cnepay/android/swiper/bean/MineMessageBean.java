package com.cnepay.android.swiper.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wjl on 2017/5/11.
 * 我的消息的实体类
 */

public class MineMessageBean implements Serializable {

    boolean isSuccess;
    MessageHeadBean head;
    List<MessageContentBean> body;
    String respTime;
    String respCode;
    String respMsg;


    public MineMessageBean() {
    }


    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public MessageHeadBean getHead() {
        return head;
    }

    public void setHead(MessageHeadBean head) {
        this.head = head;
    }

    public List<MessageContentBean> getBody() {
        return body;
    }

    public void setBody(List<MessageContentBean> body) {
        this.body = body;
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

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }


    @Override
    public String toString() {
        return "MineMessageBean{" +
                "isSuccess=" + isSuccess +
                ", head=" + head +
                ", body=" + body +
                ", respTime='" + respTime + '\'' +
                ", respCode='" + respCode + '\'' +
                ", respMsg='" + respMsg + '\'' +
                '}';
    }
}
