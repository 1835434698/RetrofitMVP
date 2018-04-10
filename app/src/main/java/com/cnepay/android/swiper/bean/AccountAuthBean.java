package com.cnepay.android.swiper.bean;

/**
 * Created by xugang on 2017/5/17.
 */

public class AccountAuthBean extends BaseBean {
    private int authStatus;
    private String accountNo;
    private String card;
    private String name;
    private String identityCard;
    private String mobile;
    private String bankName;
    private String unionBankNo;
    private String accountReason;

    public int getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(int authStatus) {
        this.authStatus = authStatus;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getUnionBankNo() {
        return unionBankNo;
    }

    public void setUnionBankNo(String unionBankNo) {
        this.unionBankNo = unionBankNo;
    }

    public String getAccountReason() {
        return accountReason;
    }

    public void setAccountReason(String accountReason) {
        this.accountReason = accountReason;
    }

    @Override
    public String toString() {
        return "AccountAuthBean{" +
                "authStatus=" + authStatus +
                ", accountNo='" + accountNo + '\'' +
                ", card='" + card + '\'' +
                ", name='" + name + '\'' +
                ", identityCard='" + identityCard + '\'' +
                ", mobile='" + mobile + '\'' +
                ", bankName='" + bankName + '\'' +
                ", unionBankNo='" + unionBankNo + '\'' +
                ", accountReason='" + accountReason + '\'' +
                '}';
    }
}
