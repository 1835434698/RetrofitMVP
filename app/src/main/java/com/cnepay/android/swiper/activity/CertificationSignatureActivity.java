package com.cnepay.android.swiper.activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.core.MvpAppCompatActivity;
import com.cnepay.android.swiper.widget.CalligraphyView;


/**
 * Created by wjl on 2017/5/4.
 * 资质认证-签名(放大)
 */
public class CertificationSignatureActivity extends MvpAppCompatActivity implements View.OnClickListener {

    private CalligraphyView signature_draw;
    private TextView tv_hint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        uc.setContentViewPure(R.layout.activity_certification_signature);
        tv_hint = (TextView) findViewById(R.id.tv_hint);
        signature_draw = (CalligraphyView) findViewById(R.id.signature_draw);
        signature_draw.setOnSignatureReadyListener(new CalligraphyView.SignatureReadyListener() {
            @Override
            public void onSignatureReady(boolean ready) {
                if (!ready) tv_hint.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStartSigningSignature(boolean startSigning) {
                tv_hint.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_clean:
                signature_draw.clear();
                break;
            case R.id.sign_confirm:
                break;
        }
    }
}
