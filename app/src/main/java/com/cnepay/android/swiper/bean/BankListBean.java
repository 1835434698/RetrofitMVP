package com.cnepay.android.swiper.bean;

import java.util.List;

/**
 * created by millerJK on time : 2017/5/17
 * description :
 */

public class BankListBean extends BaseBean{

    public List<ListEntity> list;

    public static class ListEntity {

        public int bankIndex;
        public String accountNo;
        public int cardId;
        public String bankName;
        public int status;
        public String accountName;
    }
}
