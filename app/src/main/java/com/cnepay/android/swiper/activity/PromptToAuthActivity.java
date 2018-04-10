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

public class PromptToAuthActivity extends MvpAppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uc.setContentViewWithTitle(R.layout.activity_prompt_to_auth);
        final String label = "资质认证";
        uc.setTitle(label);
        Button btn = (Button) findViewById(R.id.primaryBottomButton);
        btn.setVisibility(View.VISIBLE);
        btn.setText("马上认证");
        btn.setOnClickListener(view -> {
            // TODO: 2017/5/3 待实现
            ac.startCallbackActivity(new Intent(PromptToAuthActivity.this, CertificationRealNameActivity.class));
        });
    }
}
