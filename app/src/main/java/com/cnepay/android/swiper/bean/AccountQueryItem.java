package com.cnepay.android.swiper.bean;

/**
 * created by millerJK on time : 2017/5/7
 * description :
 */

public class AccountQueryItem {

    public String time;
    public String status;
    public String selector;
    public String money;

    public AccountQueryItem() {
    }

    public AccountQueryItem(String time, String status, String selector, String money) {
        this.time = time;
        this.status = status;
        this.selector = selector;
        this.money = money;
    }
}
