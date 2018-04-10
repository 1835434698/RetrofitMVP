package com.cnepay.android.swiper.activity;

import android.os.Bundle;

import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.core.MvpAppCompatActivity;

/**
 * 资质认证-结算卡管理
 */
public class CertificationSettlementCardActivity extends MvpAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uc.setContentViewFrameWithBottomBtn(R.layout.activity_certification_settlement_card);
        uc.setTitle("结算卡管理");
    }
}
