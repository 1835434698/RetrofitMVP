package com.cnepay.android.swiper.activity;

import android.os.Bundle;

import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.core.MvpAppCompatActivity;

/**
 * Created by XuShiWei on 2017/5/2.
 */

public class TradingProcessActivity extends MvpAppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uc.setContentViewWithTitle(R.layout.activity_trading_process);
        final String label = "交易等待";
        uc.setTitle(label);
    }
}
