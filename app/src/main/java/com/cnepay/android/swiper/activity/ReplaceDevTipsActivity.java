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

public class ReplaceDevTipsActivity extends MvpAppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uc.setContentViewFrameWithBottomBtn(R.layout.activity_replace_dev_tips);
        String label = getString(R.string.replace_device);
        uc.setTitle(label);
        uc.setParentBackgroundColor(R.color.mine_activity_bg);
        Button primaryBtn = uc.getPrimaryBtn();
        primaryBtn.setText(label);
        primaryBtn.setOnClickListener(view -> {
            // TODO: 2017/5/3 待实现
            ac.startCallbackActivity(new Intent(ReplaceDevTipsActivity.this, PromptToAuthActivity.class));
        });
    }
}
