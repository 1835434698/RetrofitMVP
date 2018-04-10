package com.cnepay.android.swiper.utils;

import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;

import com.cnepay.android.swiper.MainApp;
import com.cnepay.android.swiper.R;

import java.util.Locale;

/**
 * Created by Xushiwei on 2017/5/15.
 */

/**
 * 过滤输入编辑框中不符合规则的字符
 *
 */
public class ActivateCodeInputFilter implements InputFilter {
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        Logger.e(ActivateCodeInputFilter.class.getSimpleName(), "source:"+source+" start:"+start+" end:"+end+" dest:"+dest+" dstart:"+dstart+" dend:"+dend);
        char[] v = new char[end - start];
        TextUtils.getChars(source, start, end, v, 0);
        String s = new String(v).toUpperCase(Locale.US);
        boolean match = false;
        MainApp mainApp = MainApp.getMainApp();
        if (mainApp!=null){
            String regex = mainApp.getResources().getString(R.string.regex_activate_code, String.valueOf(s.length()));
            match = s.toString().matches(regex);
        }
        if (!match) {
            return dest.subSequence(dstart, dend);
        }
        if (source instanceof Spanned) {
            SpannableString sp = new SpannableString(s);
            TextUtils.copySpansFrom((Spanned) source, start,
                    end, null, sp, 0);
            return sp;
        } else {
            return s;
        }
    }
}
