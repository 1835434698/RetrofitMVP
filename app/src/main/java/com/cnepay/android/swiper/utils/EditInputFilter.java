package com.cnepay.android.swiper.utils;

import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;

import java.util.Locale;

/**
 * Created by xugang on 2016/6/22.
 * EditView过滤器
 */
public class EditInputFilter implements InputFilter {
    //
    private String regular;

    public EditInputFilter(String regular) {
        this.regular = regular;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        Logger.d("==EditInputFilter==", "source:" + source + "\tstart:" + start + "\tend:" + end + "\tdest" + dest + "\tdstart" + dstart + "\tdend" + dend);
        if (regular == null)
            return source;
        if (dstart == 0 && dend == 0 && TextUtils.isEmpty(source))
            return null;
        if (dstart == dend) {
            //输入
            StringBuilder builder = new StringBuilder(dest);
            if (builder.append(source).toString().matches(regular)) return source;
            else return "";
        }
        return source;
    }
}
