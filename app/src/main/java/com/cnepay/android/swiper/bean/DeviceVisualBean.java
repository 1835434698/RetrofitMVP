package com.cnepay.android.swiper.bean;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.cnepay.android.swiper.R;
import com.cnepay.device.DeviceType;

import java.util.ArrayList;
import java.util.Arrays;

import static com.cnepay.device.DeviceType.DEV_TYPE_AFV_106;
import static com.cnepay.device.DeviceType.DEV_TYPE_AFV_205;
import static com.cnepay.device.DeviceType.DEV_TYPE_AI15_9;
import static com.cnepay.device.DeviceType.DEV_TYPE_DH_A19;
import static com.cnepay.device.DeviceType.DEV_TYPE_HZ_M20;
import static com.cnepay.device.DeviceType.DEV_TYPE_ITRON_i21;
import static com.cnepay.device.DeviceType.DEV_TYPE_ITRON_i21b;
import static com.cnepay.device.DeviceType.DEV_TYPE_LANDI_M18;
import static com.cnepay.device.DeviceType.DEV_TYPE_M35;
import static com.cnepay.device.DeviceType.DEV_TYPE_TY_63250;
import static com.cnepay.device.DeviceType.DEV_TYPE_TY_71249;

/**
 * Created by Xushiwei on 2017/5/8.
 */

public class DeviceVisualBean {
    private int[] smallDevices = {DEV_TYPE_ITRON_i21, DEV_TYPE_ITRON_i21b, DEV_TYPE_DH_A19, DEV_TYPE_LANDI_M18, DEV_TYPE_TY_63250, DEV_TYPE_AFV_106};
    private int[] bigDevices = {DEV_TYPE_M35, DEV_TYPE_AI15_9, DEV_TYPE_HZ_M20, DEV_TYPE_TY_71249, DEV_TYPE_AFV_205};

    public DeviceVisualBean(Activity activity, int deviceType, ImageView deviceBack, ImageView deviceFront, ImageView cardSwipe, ImageView cardPlug, ArrayList<Animation> animList) {
        initPics(deviceType, deviceBack, deviceFront, cardSwipe, cardPlug);
        initAnim(activity, deviceType, animList);
    }

    private void initPics(int deviceType, ImageView deviceBack, ImageView deviceFront, ImageView cardSwipe, ImageView cardPlug) {
        switch (deviceType) {
            case DEV_TYPE_ITRON_i21:
                deviceBack.setVisibility(View.GONE);

                deviceFront.setVisibility(View.VISIBLE);
                cardSwipe.setVisibility(View.VISIBLE);
                deviceFront.setImageResource(R.drawable.trade_device_small_v1);
                cardSwipe.setImageResource(R.drawable.trade_card_small_swipe_b_down);

                cardPlug.setImageResource(R.drawable.trade_card_small_plug_b_vertical);
                break;
            case DEV_TYPE_ITRON_i21b:
                deviceBack.setVisibility(View.GONE);

                deviceFront.setVisibility(View.VISIBLE);
                cardSwipe.setVisibility(View.VISIBLE);
                deviceFront.setImageResource(R.drawable.trade_device_small_v102);
                cardSwipe.setImageResource(R.drawable.trade_card_small_swipe_a_down);

                cardPlug.setImageResource(R.drawable.trade_card_small_plug_a_vertical);
                break;
            case DEV_TYPE_DH_A19:
                deviceBack.setVisibility(View.GONE);

                deviceFront.setVisibility(View.VISIBLE);
                cardSwipe.setVisibility(View.VISIBLE);
                deviceFront.setImageResource(R.drawable.trade_device_small_v103);
                cardSwipe.setImageResource(R.drawable.trade_card_small_swipe_a_up);

                cardPlug.setImageResource(R.drawable.trade_card_small_plug_a_vertical);
                break;
            case DEV_TYPE_LANDI_M18:
                deviceBack.setVisibility(View.VISIBLE);
                deviceBack.setImageResource(R.drawable.trade_device_small_v104_back);

                deviceFront.setVisibility(View.VISIBLE);
                cardSwipe.setVisibility(View.VISIBLE);
                deviceFront.setImageResource(R.drawable.trade_device_small_v104_front);
                cardSwipe.setImageResource(R.drawable.trade_card_small_swipe_a_down);

                cardPlug.setImageResource(R.drawable.trade_card_small_plug_a_vertical);
                break;
            case DEV_TYPE_TY_63250:
                deviceBack.setVisibility(View.GONE);

                deviceFront.setVisibility(View.VISIBLE);
                cardSwipe.setVisibility(View.VISIBLE);
                deviceFront.setImageResource(R.drawable.trade_device_small_v105);
                cardSwipe.setImageResource(R.drawable.trade_card_small_swipe_b_down);

                cardPlug.setImageResource(R.drawable.trade_card_small_plug_b_vertical);
                break;
            case DEV_TYPE_AFV_106:
                deviceBack.setVisibility(View.VISIBLE);
                deviceBack.setImageResource(R.drawable.trade_device_small_v106_back);

                deviceFront.setVisibility(View.VISIBLE);
                cardSwipe.setVisibility(View.VISIBLE);
                deviceFront.setImageResource(R.drawable.trade_device_small_v106_front);
                cardSwipe.setImageResource(R.drawable.trade_card_small_swipe_a_down_v106);

                cardPlug.setImageResource(R.drawable.trade_card_small_plug_a_vertical);
                break;
            case DEV_TYPE_M35:
                deviceBack.setVisibility(View.VISIBLE);
                deviceBack.setImageResource(R.drawable.trade_device_big_v2_back);

                deviceFront.setVisibility(View.VISIBLE);
                cardSwipe.setVisibility(View.VISIBLE);
                deviceFront.setImageResource(R.drawable.trade_device_big_v2_front);
                cardSwipe.setImageResource(R.drawable.trade_card_big_swipe_a_down_xsw);

                cardPlug.setImageResource(R.drawable.trade_card_big_plug_a_up);
                break;
            case DEV_TYPE_AI15_9:
                deviceBack.setVisibility(View.GONE);

                deviceFront.setVisibility(View.VISIBLE);
                cardSwipe.setVisibility(View.VISIBLE);
                deviceFront.setImageResource(R.drawable.trade_device_big_itron);
                cardSwipe.setImageResource(R.drawable.trade_card_big_swipe_b_down_xsw);

                cardPlug.setImageResource(R.drawable.trade_card_big_plug_a_up);
                break;
            case DEV_TYPE_HZ_M20:
                deviceBack.setVisibility(View.GONE);

                deviceFront.setVisibility(View.VISIBLE);
                cardSwipe.setVisibility(View.VISIBLE);
                deviceFront.setImageResource(R.drawable.trade_device_big_hz_m20);
                cardSwipe.setImageResource(R.drawable.trade_card_big_swipe_b_down_xsw);

                cardPlug.setImageResource(R.drawable.trade_card_big_plug_a_up);
                break;
            case DEV_TYPE_TY_71249:
                deviceBack.setVisibility(View.GONE);

                deviceFront.setVisibility(View.VISIBLE);
                cardSwipe.setVisibility(View.VISIBLE);
                deviceFront.setImageResource(R.drawable.trade_device_big_v204);
                cardSwipe.setImageResource(R.drawable.trade_card_big_swipe_b_down_xsw);

                cardPlug.setImageResource(R.drawable.trade_card_big_plug_a_up);
                break;
            case DEV_TYPE_AFV_205:
                deviceBack.setVisibility(View.VISIBLE);
                deviceBack.setImageResource(R.drawable.trade_device_big_v205_back);

                deviceFront.setVisibility(View.VISIBLE);
                cardSwipe.setVisibility(View.VISIBLE);
                deviceFront.setImageResource(R.drawable.trade_device_big_v205_front);
                cardSwipe.setImageResource(R.drawable.trade_card_big_swipe_a_down_xsw);

                cardPlug.setImageResource(R.drawable.trade_card_big_plug_a_up);
                break;

            default:
                break;
        }
        cardPlug.setVisibility(View.GONE);
    }

    private void initAnim(Activity activity, int deviceType, ArrayList<Animation> animList) {
        Animation animSwipe;
        Animation animOver;
        Animation animPlug = null;
        animSwipe = AnimationUtils.loadAnimation(activity, R.anim.trade_card_swipe);
        animOver = AnimationUtils.loadAnimation(activity, R.anim.trade_card_swipe_over);
        if(Arrays.binarySearch(smallDevices, deviceType)>=0) {//刷卡头设备
            if (deviceType == DeviceType.DEV_TYPE_LANDI_M18){//V104设备插卡槽不在正中
                animPlug = AnimationUtils.loadAnimation(activity, R.anim.trade_card_plug_small_vertical_for_v104);
            }else {
                animPlug = AnimationUtils.loadAnimation(activity, R.anim.trade_card_plug_small_vertical);
            }
        }else if (Arrays.binarySearch(bigDevices, deviceType)>=0){//带键盘设备
            animPlug = AnimationUtils.loadAnimation(activity, R.anim.trade_card_plug_big_horizontal);
        }
        animList.add(animSwipe);animList.add(animOver);animList.add(animPlug);
    }
}
