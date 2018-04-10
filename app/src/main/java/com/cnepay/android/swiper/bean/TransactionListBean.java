package com.cnepay.android.swiper.bean;

import java.util.List;

/**
 * created by millerJK on time : 2017/5/20
 * description :
 */

public class TransactionListBean extends BaseBean {

    public int amount;
    public List<TransListEntity> transList;
    public int count;

    public static class TransListEntity {

        public int tranid;
        public int amount;
        public String transTime;
        public String transType;
        public String settleType;

        @Override
        public String toString() {
            return "TransListEntity{" +
                    "tranid=" + tranid +
                    ", amount=" + amount +
                    ", transTime='" + transTime + '\'' +
                    ", transType='" + transType + '\'' +
                    ", settleType='" + settleType + '\'' +
                    '}';
        }
    }
}
