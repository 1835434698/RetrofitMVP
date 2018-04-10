package com.cnepay.android.swiper.core.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Timer;
import java.util.TimerTask;

/**
 * created by millerJK on time : 2017/5/17
 * description :
 */

public class KeyBoardUtils {

    public static void closeInputMethod(Activity context) {
        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = context.getCurrentFocus();
        if (view != null) {
            im.hideSoftInputFromWindow(view.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 当界面中的View 已经全部渲染完毕，还有另外一种情况参考{@link #openInputMethodTwo(EditText)}
     *
     * @param editText
     */
    public static void openInputMethod(EditText editText) {

        InputMethodManager inputManager = (InputMethodManager) editText
                .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);

    }

    /**
     * 界面中的View 尚未渲染完毕，但是又想在进出Activity就弹出软键盘
     * you want to show keyboard immediately when into a activity
     *
     * @param editText
     */
    public static void openInputMethodTwo(EditText editText) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) editText
                        .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(editText, 0);
            }
        }, 200);
    }
}
