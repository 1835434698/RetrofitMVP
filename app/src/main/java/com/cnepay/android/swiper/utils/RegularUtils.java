package com.cnepay.android.swiper.utils;

import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式
 * Created by xugang on 2016/4/1 0001.
 */
public class RegularUtils {

    public static final String PHONE = "^1\\d{10}$";//1开头后面十位数字

    public static final String LIMIT_PHONE_INPUT = "^1\\d{0,10}$";//EditView限制电话号码输入
    public static final String LIMIT_PWD_INPUT = "^[A-Za-z0-9\\`\\~\\!\\@\\#\\$\\%\\^\\&\\*\\(\\)\\-\\_\\=\\+]{0,16}$";//限制密码输入
    public static final String LIMIT_PWD_INPUT1 = "^[A-Za-z0-9\\-\\_\\、]{0,16}$";//限制密码输入

    public static final String OLD_PWD_MATCH = "^[A-Za-z0-9-_]{6,20}$";//6-16位字母数字

    public static final String PWD = "^((?=.*?\\d)(?=.*?[A-Za-z])|(?=.*?\\d)(?=.*?[!@#$%^&*()-_=+'~])|(?=.*?[A-Za-z])(?=.*?[!@#$%^&*()-_=+'~]))[\\dA-Za-z!@#$%^&*()-_=+'~]{6,16}$";//6-16位字母符号,不纯为字母数字符号

    public static final String AllLetters = "^[a-zA-Z]{6,16}$";//全字母
    public static final String AllNumber = "^[0-9]{6,16}$";//全数字
    public static final String AllSymbol = "^[` ~ ! @ # $ % ^ & * ( ) - _ = +]{6,16}$";//全字符

    public static final String BANK_CARD_NO = "^[0-9]{14,23}$";//银行卡号

    public static final InputFilter[] FILTER_LIMIT_PHONE_INPUT = {new EditInputFilter(LIMIT_PHONE_INPUT)};

    public static final InputFilter[] FILTER_LIMIT_PWD_INPUT = {new EditInputFilter(LIMIT_PWD_INPUT1)};

    public static final InputFilter[] FILTER_LIMIT_ACTIVATE_CODE = {new ActivateCodeInputFilter()};

    /**
     * 正则
     *
     * @param rules 正则表达式
     * @return
     */
    public static boolean compare(String content, String rules) {
        if (TextUtils.isEmpty(content))
            return false;
        Pattern p = Pattern.compile(rules);
        Matcher m = p.matcher(content);
        return m.matches();
    }
}
