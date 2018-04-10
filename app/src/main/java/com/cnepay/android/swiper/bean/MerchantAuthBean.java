package com.cnepay.android.swiper.bean;

/**
 * Created by xugang on 2017/5/17.
 */

public class MerchantAuthBean extends BaseBean {
    private int authStatus;
    private String companyName;
    private String business;
    private String regPlace;
    private String businessLicense;
    private String merchantReason;

    public int getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(int authStatus) {
        this.authStatus = authStatus;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getRegPlace() {
        return regPlace;
    }

    public void setRegPlace(String regPlace) {
        this.regPlace = regPlace;
    }

    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }

    public String getMerchantReason() {
        return merchantReason;
    }

    public void setMerchantReason(String merchantReason) {
        this.merchantReason = merchantReason;
    }

    @Override
    public String toString() {
        return "MerchantAuthBean{" +
                "authStatus=" + authStatus +
                ", companyName='" + companyName + '\'' +
                ", business='" + business + '\'' +
                ", regPlace='" + regPlace + '\'' +
                ", businessLicense='" + businessLicense + '\'' +
                ", merchantReason='" + merchantReason + '\'' +
                '}';
    }
}
