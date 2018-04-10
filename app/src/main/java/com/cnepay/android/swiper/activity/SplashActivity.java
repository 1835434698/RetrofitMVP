package com.cnepay.android.swiper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.AnimRes;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.core.MvpAppCompatActivity;
import com.cnepay.android.swiper.utils.Logger;

/**
 * Created by tangzy on 2017/4/27.
 */

public class SplashActivity extends MvpAppCompatActivity implements Animation.AnimationListener, Runnable {

    private static final String TAG = "SplashActivity";
    private RelativeLayout rl_back;
    private Animation alpha;
    private Handler handler;
    private ImageView iv_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        uc.setContentViewPure(R.layout.activity_splash);
        handler = new Handler();
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        iv_logo = (ImageView) findViewById(R.id.iv_logo);

        alpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        alpha.setAnimationListener(this);
        rl_back.startAnimation(alpha);

        Animation rotate = AnimationUtils.loadAnimation(this, R.anim.splact_logo_rotate);
        iv_logo.setAnimation(rotate);
        iv_logo.startAnimation(rotate);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        alpha.setAnimationListener(null);
        handler.removeCallbacks(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                alpha.setAnimationListener(null);
                handler.removeCallbacks(this);
                run();
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        handler.postDelayed(this, 2 * 1000);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void run() {
        Logger.d(TAG, "run()");
        ac.startActivity(new Intent(this, LoginActivity.class));
        finish();
        ac.enterAnimation();
    }

}
