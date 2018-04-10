package com.cnepay.android.swiper.bean;

/**
 * created by millerJK on time : 2017/5/15
 * description :
 */

public class AccountDetailBean extends BaseBean{


    public TranInfoEntity tranInfo;

    public static class TranInfoEntity {

        public String voucherNo;
        public String terminalNo;
        public String cardNoWipe;
        public String batchNo;
        public int amount;
        public String transTime;
        public String transType;
        public int transId;
        public int transStatus;
        public String settleType;
        public String merchantNo;
        public String merchantName;

    }
}
