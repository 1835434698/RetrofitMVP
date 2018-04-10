package com.cnepay.android.swiper.utils;

import android.text.TextUtils;

/**
 * Created by Administrator on 2017/5/10.
 */

public class SecurityUtils {
    public static String replace4IdCard(String idCard) {
        return replace4String(3, 3, idCard);
    }

    public static String replace4BankCard(String bankCard) {
        return replace4String(6, 4, bankCard);
    }

    /**
     * 将字符串中间某段字符串替换成"*"
     *
     * @param start   前面保留位数
     * @param end     后面保留位数
     * @param content 需要处理的内容串
     * @return 处理后的字符串
     */
    private static String replace4String(int start, int end, String content) {
        if (TextUtils.isEmpty(content)) return null;
        if ((start + end) > content.length()) return null;
        StringBuilder builder = new StringBuilder(content.substring(0, start));
        String str = content.substring(start, content.length() - end);
        for (int i = 0; i < str.length(); i++) {
            builder.append("*");
        }
        builder.append(content.substring(content.length() - end, content.length()));
        return builder.toString();
    }
}
