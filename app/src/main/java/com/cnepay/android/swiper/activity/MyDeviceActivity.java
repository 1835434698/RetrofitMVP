package com.cnepay.android.swiper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.core.MvpAppCompatActivity;

/**
 * Created by XuShiWei on 2017/5/2.
 */

public class MyDeviceActivity extends MvpAppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uc.setContentViewFrameWithBottomBtn(R.layout.activity_my_device);
        final String label = "我的设备";
        uc.setTitle(label);
        Button primaryBtn = uc.getPrimaryBtn();
        primaryBtn.setText(getString(R.string.replace_device));
        primaryBtn.setOnClickListener(view -> {
            // TODO: 2017/5/3 待实现
            ac.startCallbackActivity(new Intent(MyDeviceActivity.this, TradingProcessActivity.class));
        });
    }
}
