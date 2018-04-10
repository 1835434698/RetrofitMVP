package com.cnepay.android.swiper.bean;

import java.util.List;

/**
 * created by millerJK on time : 2017/5/14
 * description : 结算查询Item
 */

public class AccountQueryBean {


    private int amount;
    private String respTime;
    private int count;
    private String respMsg;
    private String respCode;
    private List<SettleListEntity> settleList;
    private boolean isSuccess;

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setRespTime(String respTime) {
        this.respTime = respTime;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public void setSettleList(List<SettleListEntity> settleList) {
        this.settleList = settleList;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public int getAmount() {
        return amount;
    }

    public String getRespTime() {
        return respTime;
    }

    public int getCount() {
        return count;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public String getRespCode() {
        return respCode;
    }

    public List<SettleListEntity> getSettleList() {
        return settleList;
    }

    public boolean isIsSuccess() {
        return isSuccess;
    }

    public static class SettleListEntity {
        /**
         * transAmount : 29853
         * accountNum : 6228480010970642611
         * settleId : 10794
         * settleStatus : 2
         * uniqueRecord : 10794-6228480010970642611-298530-d0
         * settleMoney : 29853
         * settleDate : 2016-03-15
         * settleType : D+0
         * merchantName : 旧数据企业
         * merchantNo : 500000000876552
         */
        private int transAmount;
        private String accountNum;
        private int settleId;
        private int settleStatus;
        private String uniqueRecord;
        private int settleMoney;
        private String settleDate;
        private String settleType;
        private String merchantName;
        private String merchantNo;

        public void setTransAmount(int transAmount) {
            this.transAmount = transAmount;
        }

        public void setAccountNum(String accountNum) {
            this.accountNum = accountNum;
        }

        public void setSettleId(int settleId) {
            this.settleId = settleId;
        }

        public void setSettleStatus(int settleStatus) {
            this.settleStatus = settleStatus;
        }

        public void setUniqueRecord(String uniqueRecord) {
            this.uniqueRecord = uniqueRecord;
        }

        public void setSettleMoney(int settleMoney) {
            this.settleMoney = settleMoney;
        }

        public void setSettleDate(String settleDate) {
            this.settleDate = settleDate;
        }

        public void setSettleType(String settleType) {
            this.settleType = settleType;
        }

        public void setMerchantName(String merchantName) {
            this.merchantName = merchantName;
        }

        public void setMerchantNo(String merchantNo) {
            this.merchantNo = merchantNo;
        }

        public int getTransAmount() {
            return transAmount;
        }

        public String getAccountNum() {
            return accountNum;
        }

        public int getSettleId() {
            return settleId;
        }

        public int getSettleStatus() {
            return settleStatus;
        }

        public String getUniqueRecord() {
            return uniqueRecord;
        }

        public int getSettleMoney() {
            return settleMoney;
        }

        public String getSettleDate() {
            return settleDate;
        }

        public String getSettleType() {
            return settleType;
        }

        public String getMerchantName() {
            return merchantName;
        }

        public String getMerchantNo() {
            return merchantNo;
        }
    }
}
