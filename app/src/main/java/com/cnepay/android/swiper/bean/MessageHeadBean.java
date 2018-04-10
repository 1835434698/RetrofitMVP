package com.cnepay.android.swiper.bean;

/**
 * Created by wjl on 2017/5/15.
 */

public class MessageHeadBean {
    boolean hasUnRead; //是否有未读消息
    boolean unReadNotice; //是否有未读通知
    boolean unReadBulletin; //是否有未读公告

    int totalCount; //消息总条数
    int readCount; //已阅读数
    int unReadCount; //未阅读数


    public boolean isHasUnRead() {
        return hasUnRead;
    }

    public void setHasUnRead(boolean hasUnRead) {
        this.hasUnRead = hasUnRead;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    @Override
    public String toString() {
        return "MessageHeadBean{" +
                "hasUnRead=" + hasUnRead +
                ", totalCount=" + totalCount +
                ", readCount=" + readCount +
                ", unReadCount=" + unReadCount +
                '}';
    }
}
