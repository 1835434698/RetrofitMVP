package com.cnepay.android.swiper.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * created by millerJK on time : 2017/5/7
 * description :
 */

public class MeasureUtils {


    /**
     * screen width
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * screen height
     *
     * @param context
     * @return
     */
    public static int getScreenHeigth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

}
