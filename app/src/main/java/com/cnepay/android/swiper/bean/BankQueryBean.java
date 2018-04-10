package com.cnepay.android.swiper.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xugang on 2017/5/19.
 */

public class BankQueryBean extends BaseBean {

    private int total;
    private String tip;
    private int reqNo;
    private List<Bank> banks;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public int getReqNo() {
        return reqNo;
    }

    public void setReqNo(int reqNo) {
        this.reqNo = reqNo;
    }

    public List<Bank> getBanks() {
        return banks;
    }

    public void setBanks(List<Bank> banks) {
        this.banks = banks;
    }

    public class Bank implements Serializable{
        private String bankDeposit;
        private String unionBankNo;

        public String getBankDeposit() {
            return bankDeposit;
        }

        public void setBankDeposit(String bankDeposit) {
            this.bankDeposit = bankDeposit;
        }

        public String getUnionBankNo() {
            return unionBankNo;
        }

        public void setUnionBankNo(String unionBankNo) {
            this.unionBankNo = unionBankNo;
        }

        @Override
        public String toString() {
            return "Bank{" +
                    "bankDeposit='" + bankDeposit + '\'' +
                    ", unionBankNo='" + unionBankNo + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "BankQueryBean{" +
                "total=" + total +
                ", tip='" + tip + '\'' +
                ", reqNo=" + reqNo +
                ", banks=" + banks +
                '}';
    }
}
