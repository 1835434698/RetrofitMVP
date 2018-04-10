package com.cnepay.android.swiper.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

/**
 * Save Data To SharePreference Or Get Data from SharePreference
 * 通过SharedPreferences来存储数据，自定义类型
 * Created by xugang on 2016/4/6 0006.
 */
public class SharedPre {
    public static String TAG = "SharedPre";
    private Context ctx;
    private String FileName = "cnepay";

    private SharedPre(Context ctx) {
        this.ctx = ctx;
    }

    private static SharedPre pre;

    public static SharedPre getInstance(Context context) {
        if (pre == null) {
            pre = new SharedPre(context);
        }
        return pre;
    }

    /**
     * Set int value into SharePreference
     *
     * @param key
     * @param value
     */
    //通过SharedPreferences来存储键值对
    public void saveIntValue(String key, int value) {
        SharedPreferences sharePre = ctx.getSharedPreferences(FileName,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharePre.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    @SuppressLint("SimpleDateFormat")
    @SuppressWarnings("unused")
    public void writeDownStartApplicationTime() {
        SharedPreferences sp = ctx.getSharedPreferences(FileName, Context.MODE_PRIVATE);
        long now = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        //Date now = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd:hh-mm-ss");
        SharedPreferences.Editor editor = sp.edit();
        //editor.putString("启动时间", now.toString());
        editor.putLong("nowtimekey", now);
        editor.commit();

    }

    /**
     * Set String value into SharePreference
     *
     * @param key
     * @param value
     */
    public void saveStringValue(String key, String value) {
        SharedPreferences sharePre = ctx.getSharedPreferences(FileName,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharePre.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * Set Boolean value into SharePreference
     *
     * @param key
     * @param value
     */
    public void saveBooleanValue(String key, boolean value) {
        SharedPreferences sharePre = ctx.getSharedPreferences(FileName,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharePre.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * Remove key from SharePreference
     *
     * @param key
     */
    public void removeKey(String key) {
        SharedPreferences sharePre = ctx.getSharedPreferences(FileName,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharePre.edit();
        editor.remove(key);
        editor.commit();
    }

    /**
     * 查询是否包含key的值，存在为true，否则为false
     *
     * @param key
     * @return
     */
    public boolean contains(String key) {
        SharedPreferences sharePre = ctx.getSharedPreferences(FileName,
                Context.MODE_PRIVATE);
        return sharePre.contains(key);
    }

    /**
     * Get all value
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getAllMap() {
        SharedPreferences sharePre = ctx.getSharedPreferences(FileName,
                Context.MODE_PRIVATE);
        return (Map<String, Object>) sharePre.getAll();
    }

    /**
     * Get Integer Value
     *
     * @param key
     * @return
     */
    public Integer getIntValue(String key, int defValue) {
        SharedPreferences sharePre = ctx.getSharedPreferences(FileName,
                Context.MODE_PRIVATE);
        return sharePre.getInt(key, defValue);
    }

    /**
     * Get Integer Value
     *
     * @param key
     * @return
     */
    public Integer getIntValue(String key) {
        SharedPreferences sharePre = ctx.getSharedPreferences(FileName,
                Context.MODE_PRIVATE);
        return sharePre.getInt(key, -1);
    }

    /**
     * Get String Value
     * 通过输入的key来获得value
     *
     * @param key
     * @return
     */
    public String getStringValue(String key) {
        SharedPreferences sharePre = ctx.getSharedPreferences(FileName,
                Context.MODE_PRIVATE);
        return sharePre.getString(key, null);
    }

    /**
     * Get String Value
     * 通过输入的key来获得value, 若value不存在，返回自定义的默认值defaultstr
     *
     * @param key
     * @param defaultstr
     * @return
     */
    public String getStringValue(String key, String defaultstr) {
        SharedPreferences sharePre = ctx.getSharedPreferences(FileName,
                Context.MODE_PRIVATE);
        return sharePre.getString(key, defaultstr);
    }

    public Boolean getBooleanValue(String key) {
        SharedPreferences sharePre = ctx.getSharedPreferences(FileName,
                Context.MODE_PRIVATE);
        return sharePre.getBoolean(key, false);
    }


    public Boolean getBooleanValue(String key, boolean def) {
        SharedPreferences sharePre = ctx.getSharedPreferences(FileName,
                Context.MODE_PRIVATE);
        return sharePre.getBoolean(key, def);
    }

    /**
     * Get Value, Remove key
     *
     * @param key
     * @return
     */
    public Integer pollIntValue(String key) {
        Integer value = getIntValue(key);
        removeKey(key);
        return value;
    }

}