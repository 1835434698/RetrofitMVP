package com.cnepay.android.swiper.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.activity.HomePageActivity;
import com.cnepay.android.swiper.activity.LoginActivity;
import com.cnepay.android.swiper.core.model.RetrofitProvider;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static android.view.inputmethod.InputMethodManager.SHOW_FORCED;
import static android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT;

/**
 * Created by tangzy on 2017/4/27.
 */

public class Utils {

    public static final String TAG = "Utils";
    public static final String DEFAULT_PREF = "default";
    public static final String PREF_VERSION_NAME = "last_version_name";
    public static final String LOGIN_PHONE = "login_phone";


    public static final String PHONE = "^1\\d{10}$";//1开头后面十位数字
    public static final String OLD_PWD_MATCH = "^[A-Za-z0-9-_]{6,20}$";//6-16位字母数字

    public static final String PWD = "^((?=.*?\\d)(?=.*?[A-Za-z])|(?=.*?\\d)(?=.*?[!@#$%^&*()-_=+'~])|(?=.*?[A-Za-z])(?=.*?[!@#$%^&*()-_=+'~]))[\\dA-Za-z!@#$%^&*()-_=+'~]{6,16}$";//6-16位字母符号,不纯为字母数字符号

    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    /**
     * 返回软件是否是新装软件或者覆盖安装软件，只有每次安装后第一次，并无有新feature
     * 参数调用返回true，其余时候都返回false
     *
     * @param context
     * @return boolean
     */
    //TODO 这个地方使用共享参数的可以看看SharedPre工具类，此方法多余了
    public static boolean isNewlyInstalled(Context context) {
        SharedPre sp = SharedPre.getInstance(context);
        String lastVersion = sp.getStringValue(PREF_VERSION_NAME, null);
        String currVersion = getVersionName(context);
        Logger.d(TAG, "lastVersion:" + lastVersion + "   currVersion:" + currVersion);
        if (!currVersion.equalsIgnoreCase(lastVersion)) {
            return true;
        }
        return false;
    }

    public static boolean getBool(String name, Context context) {
        int bool = context.getResources().getIdentifier(name, "bool", context.getPackageName());
        return context.getResources().getBoolean(bool);
    }

    /**
     * 返回软件版本名称
     *
     * @param context
     * @return String version
     */
    public static String getVersionName(Context context) {
        String version = "unknown";
        try {
            PackageInfo packInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            version = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 校验手机号码
     *
     * @param phoneNum
     * @return
     */
    public static boolean verifyPhone(Context context, String phoneNum) {
        if (TextUtils.isEmpty(phoneNum)) {
//            Toast.makeText(context, R.string.hint_phone_format, Toast.LENGTH_LONG).show();
            return false;
        }
        if (!phoneNum.matches(PHONE)) {
//            Toast.makeText(context, R.string.hint_phone_format, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    /**
     * 短信验证码验证
     */
    public static boolean verifyMsgVCode(Context context, String code) {
        if (TextUtils.isEmpty(code) || code.length() != 4) {
            Toast.makeText(context, R.string.hint_msg_vcode_error, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public static boolean verifyPasswd4OldUsers(Context context, String passwd) {
        if (TextUtils.isEmpty(passwd)) {
            Toast.makeText(context, R.string.hint_pwd_not_null, Toast.LENGTH_LONG).show();
            return false;
        }
        if (!passwd.matches(OLD_PWD_MATCH) && !passwd.matches(PWD)) {
            Toast.makeText(context, R.string.hint_pwd_login, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    /**
     * 指定在UI线程上运行
     *
     * @param r
     * @return 如果当前线程是UI线程，则返回true
     */
    public static boolean runOnUI(Runnable r) {
        return runOnUI(r, 0l);
    }

    public static boolean runOnUI(Runnable r, long delayMillis) {
        Thread t = Thread.currentThread();
        boolean isMainThread = (t != null && t.getId() == Looper.getMainLooper().getThread().getId());
        if (r == null) return isMainThread;
        if (isMainThread) {
            r.run();
        } else {
            if (delayMillis > 0)
                mainHandler.postDelayed(r, delayMillis);
            else
                mainHandler.post(r);
        }
        return isMainThread;
    }

    /**
     * 获取 Header 使用的格林威治时间的Date
     *
     * @return
     */
    public static String getHeaderDate() {
        SimpleDateFormat format = new SimpleDateFormat(
                "EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        return format.format(new Date());
    }

    public static void savePhone(Context context, String phone) {
        SharedPre sp = SharedPre.getInstance(context);
        sp.saveStringValue(LOGIN_PHONE, phone);
    }

    public static String getPhone(Context context) {
        SharedPre sp = SharedPre.getInstance(context);
        return sp.getStringValue(LOGIN_PHONE, null);
    }

    public static void cleanPhone(Context context) {
        SharedPre sp = SharedPre.getInstance(context);
         sp.removeKey(LOGIN_PHONE);
    }

    /**
     * 获取我的消息 的创建时间
     * @param time
     * @return
     */
    public static String getMessageCreateTime(long time){
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日 hh:mm:ss");
        return format.format(date);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将光标切到目标EditText上并弹出输入法。
     */
    public static void focusAndShowInputMethod(EditText et, Activity activity) {
        et.requestFocus();
        et.setSelection(et.getText().length());//光标置于最后
        View v = activity.getCurrentFocus();
        if (v != null) {
            InputMethodManager inputManager = (InputMethodManager) activity
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(v, SHOW_FORCED);
        }
    }

    /**
     * 根据输入字符串生成标准化的url的字符串
     * @param urlString 输入字符串
     * @param suffix 在生成的url后拼接的后缀
     * @return 标准化的url的字符串
     * @throws MalformedURLException
     */
    public static String getStandardUrl(String urlString, String suffix) throws MalformedURLException {
        String temp;
        URL url = new URL(urlString);
        temp = url.getProtocol();
        temp += "://";
        temp += url.getHost();
        temp += (url.getPort() == -1 ? "" : (":" + url.getPort()));
        temp += url.getPath();
        if (!TextUtils.isEmpty(temp) && !TextUtils.isEmpty(suffix))
            temp += suffix;
        Logger.e(RetrofitProvider.class.getSimpleName(), "Temp Url =" + temp);
        return temp;
    }

    /**
     * 得到屏幕宽度
     * @param context
     * @return
     */
    public static int screenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 得到屏幕高度
     * @param context
     * @return
     */
    public static int screenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 判断某个class所对应的Activity是否是前台应所对应的任务栈的底层，
     * 只有当前应用处于前台时，返回的数据才是真实的。
     * 如果当前应用不在前台，则肯定返回false
     *
     * @param cls     目标class文件
     * @param context 上下文对象
     * @return
     */
    public static boolean isBaseActivityOfLauncher(Class cls, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.RunningTaskInfo runningTaskInfo = manager.getRunningTasks(1).get(0);
        if (runningTaskInfo == null || runningTaskInfo.topActivity == null || runningTaskInfo.baseActivity == null) {
            Logger.i(TAG, "isBaseActivityOfLauncher 有元素为空 返回false");
            return false;
        }
        Logger.i(TAG, "topActivity:" + runningTaskInfo.topActivity.getClassName() + "     baseActivity:" + runningTaskInfo.baseActivity.getClassName());
        String name = runningTaskInfo.baseActivity.getClassName();
        if (name.equals(cls.getName())) {
            return true;
        } else {
            return false;
        }
    }
}
