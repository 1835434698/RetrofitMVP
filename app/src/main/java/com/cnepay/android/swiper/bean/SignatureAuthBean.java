package com.cnepay.android.swiper.bean;

/**
 * Created by xugang on 2017/5/17.
 */

public class SignatureAuthBean extends BaseBean {
    private int authStatus;
    private String name;
    private String signature;
    private String signatureReason;

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

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSignatureReason() {
        return signatureReason;
    }

    public void setSignatureReason(String signatureReason) {
        this.signatureReason = signatureReason;
    }

    @Override
    public String toString() {
        return "SignatureAuthBean{" +
                "authStatus=" + authStatus +
                ", name='" + name + '\'' +
                ", signature='" + signature + '\'' +
                ", signatureReason='" + signatureReason + '\'' +
                '}';
    }
}
