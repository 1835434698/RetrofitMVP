package com.cnepay.android.swiper.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;

import com.cnepay.android.swiper.R;

/**
 * Created by tangzy on 2017/5/2.
 */

public class Countdown  extends CountDownTimer {
    private Context mContext;
    private TextView v;

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public Countdown(Context mContext, TextView v, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.mContext = mContext;
        this.v = v;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        v.setEnabled(false);
        v.setText(mContext.getString(R.string.hint_msg_vcode_resend, String.valueOf(millisUntilFinished / 1000)));
    }

    @Override
    public void onFinish() {
        v.setText("短信验证码");
        v.setEnabled(true);
    }
}
