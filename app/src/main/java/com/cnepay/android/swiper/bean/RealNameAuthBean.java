package com.cnepay.android.swiper.bean;

/**
 * Created by xugang on 2017/5/17.
 */

public class RealNameAuthBean extends BaseBean {
    private int authStatus;
    private String name;
    private int idNumber;
    private int personal;
    private int personalBack;
    private int hPersonal;
    private int realReason;
    private int idCard;
    private String realName;

    public int getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(int authStatus) {
        this.authStatus = authStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(int idNumber) {
        this.idNumber = idNumber;
    }

    public int getPersonal() {
        return personal;
    }

    public void setPersonal(int personal) {
        this.personal = personal;
    }

    public int getPersonalBack() {
        return personalBack;
    }

    public void setPersonalBack(int personalBack) {
        this.personalBack = personalBack;
    }

    public int gethPersonal() {
        return hPersonal;
    }

    public void sethPersonal(int hPersonal) {
        this.hPersonal = hPersonal;
    }

    public int getRealReason() {
        return realReason;
    }

    public void setRealReason(int realReason) {
        this.realReason = realReason;
    }

    public int getIdCard() {
        return idCard;
    }

    public void setIdCard(int idCard) {
        this.idCard = idCard;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    @Override
    public String toString() {
        return "RealNameAuthBean{" +
                "authStatus=" + authStatus +
                ", name='" + name + '\'' +
                ", idNumber=" + idNumber +
                ", personal=" + personal +
                ", personalBack=" + personalBack +
                ", hPersonal=" + hPersonal +
                ", realReason=" + realReason +
                ", idCard=" + idCard +
                ", realName='" + realName + '\'' +
                '}';
    }
}
