package com.cnepay.android.swiper.bean;

public class BannerBodyBean {
    int id;
    String adName;
    String picAndroid;
    String picIos;
    String linkAddress;
    long effectBegin;
    long effectEnd;
    int disableStatus;
    int adSort;
    int sysUserId;
    long createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdName() {
        return adName;
    }

    public void setAdName(String adName) {
        this.adName = adName;
    }

    public String getPicAndroid() {
        return picAndroid;
    }

    public void setPicAndroid(String picAndroid) {
        this.picAndroid = picAndroid;
    }

    public String getPicIos() {
        return picIos;
    }

    public void setPicIos(String picIos) {
        this.picIos = picIos;
    }

    public String getLinkAddress() {
        return linkAddress;
    }

    public void setLinkAddress(String linkAddress) {
        this.linkAddress = linkAddress;
    }

    public long getEffectBegin() {
        return effectBegin;
    }

    public void setEffectBegin(long effectBegin) {
        this.effectBegin = effectBegin;
    }

    public long getEffectEnd() {
        return effectEnd;
    }

    public void setEffectEnd(long effectEnd) {
        this.effectEnd = effectEnd;
    }

    public int getDisableStatus() {
        return disableStatus;
    }

    public void setDisableStatus(int disableStatus) {
        this.disableStatus = disableStatus;
    }

    public int getAdSort() {
        return adSort;
    }

    public void setAdSort(int adSort) {
        this.adSort = adSort;
    }

    public int getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(int sysUserId) {
        this.sysUserId = sysUserId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "BannerBodyBean{" +
                "id=" + id +
                ", adName='" + adName + '\'' +
                ", picAndroid='" + picAndroid + '\'' +
                ", picIos='" + picIos + '\'' +
                ", linkAddress='" + linkAddress + '\'' +
                ", effectBegin=" + effectBegin +
                ", effectEnd=" + effectEnd +
                ", disableStatus=" + disableStatus +
                ", adSort=" + adSort +
                ", sysUserId=" + sysUserId +
                ", createTime=" + createTime +
                '}';
    }
}