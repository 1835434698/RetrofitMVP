package com.cnepay.android.swiper.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.core.MvpAppCompatActivity;
import com.cnepay.android.swiper.utils.Utils;

/**
 * Created by wjl on 2017/4/28.
 * 关于
 */
public class AboutActivity extends MvpAppCompatActivity {
    TextView tv_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uc.setContentViewWithTitle(R.layout.activity_about);
        uc.setTitle(R.string.title_activity_about);
        tv_version = (TextView) findViewById(R.id.tv_version);
        tv_version.setText("Android."+ Utils.getVersionName(this));
    }


}
