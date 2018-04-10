package com.cnepay.android.swiper.activity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.SimpleImplementations.SimpleAnimationListener;
import com.cnepay.android.swiper.bean.DeviceVisualBean;
import com.cnepay.android.swiper.core.MvpAppCompatActivity;
import com.cnepay.device.DeviceType;

import java.util.ArrayList;

/**
 * Created by XuShiWei on 2017/5/8.
 */

public class ReadCardActivity extends MvpAppCompatActivity{

    public static final byte TYPE_BALANCE = 1;
    public static final byte TYPE_CONSUME = 2;
    private byte activityType;
    private int deviceType;

    private ImageView cardPlug;
    private ImageView cardSwipe;
    private ImageView deviceBack;
    private ImageView deviceFront;
    private DeviceVisualBean deviceVisualBean;
    ArrayList<Animation> animList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activityType = getIntent().getByteExtra(ReadCardActivity.class.getSimpleName(), (byte) -1);
        super.onCreate(savedInstanceState);
        uc.setContentViewWithTitle(R.layout.activity_read_card);
        final String label = activityType ==TYPE_BALANCE? "银行卡余额": "请刷卡";
        uc.setTitle(label);

        deviceBack = (ImageView) findViewById(R.id.read_card_act_iv_trade_device_back);
        deviceFront = (ImageView) findViewById(R.id.read_card_act_iv_trade_device_front);
        cardSwipe = (ImageView) findViewById(R.id.read_card_act_iv_trade_card_swipe);
        cardPlug = (ImageView) findViewById(R.id.read_card_act_iv_trade_card_plug);

        // TODO: 2017/5/11 暂时写死
        deviceType = DeviceType.DEV_TYPE_M35;
        deviceVisualBean = new DeviceVisualBean(this,deviceType, deviceBack, deviceFront, cardSwipe, cardPlug, animList);
        initAnimationListener();
    }

    private void initAnimationListener() {
        SimpleAnimationListener animationFinish = new SimpleAnimationListener() {
            public void onAnimationEnd(Animation animation) {
                uc.toast("读卡结束");
            }
        };
        animList.get(1).setAnimationListener(animationFinish);
        animList.get(2).setAnimationListener(animationFinish);
    }

    private void animOnSwipe(){
        cardSwipe.clearAnimation();
        cardSwipe.startAnimation(animList.get(1));
    }
    private void animOnPlug(){
        cardSwipe.clearAnimation();
        cardSwipe.setVisibility(View.GONE);
        cardPlug.setVisibility(View.VISIBLE);
        cardPlug.startAnimation(animList.get(2));
    }

    @Override
    protected void onDestroy() {
        deviceVisualBean = null;
        super.onDestroy();
    }
}
