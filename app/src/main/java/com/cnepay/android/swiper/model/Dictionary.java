package com.cnepay.android.swiper.model;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.bean.BaseBean;
import com.cnepay.android.swiper.bean.LoginBean;
import com.cnepay.android.swiper.core.view.MvpView;
import com.cnepay.android.swiper.utils.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2017/5/15.
 * 需要保存生命周期长于Activity的数据时，可以使用此类
 */

public class Dictionary {

    private static Map<String, BaseBean> activeBeans = new ConcurrentHashMap<>();
    private static long defaultSessionExpiredTime;
    public static final int MSG_EXPIRE = 0;
    private static Dictionary mDictionary;
    private Handler sessionHandler;

    private Dictionary(Looper l) {
        sessionHandler = new SessionHandler(l);
    }

    public static void init(Context context) {
        defaultSessionExpiredTime = context.getResources().getInteger(R.integer.session_expired_time_default);
        if (mDictionary == null) mDictionary = new Dictionary(Looper.myLooper());
    }

    public static void printTest() {
        Logger.e(Dictionary.class.getSimpleName(), activeBeans.toString());
    }

    public static <T extends BaseBean> T getActiveBean(String key) {
        return getActiveBean(key, false);
    }

    public static <T extends BaseBean> T getActiveBean(String key, boolean resetExpire) {
        return getActiveBean(key, resetExpire, -1);
    }

    public static <T extends BaseBean> T getActiveBean(String key, boolean resetExpire, long expireTime) {
        BaseBean bean;
        if (TextUtils.isEmpty(key) || (bean = activeBeans.get(key)) == null) return null;
        if (resetExpire) {
            if (expireTime > 0)
                bean.setExpireTime(expireTime);
            refreshMsgCountDown(mDictionary.sessionHandler, MSG_EXPIRE, bean, bean.getExpireTime());
        }
        return (T) bean;
    }

    public static BaseBean activateBean(BaseBean baseBean) {
        return activateBean(baseBean, defaultSessionExpiredTime);
    }

    public static BaseBean activateBean(BaseBean newBean, long expireTime) {
        Logger.e(Dictionary.class.getSimpleName(), "activateBean newBean:"+newBean);
        if (newBean.dependsOnLoginBean())
            throw new IllegalArgumentException("依赖于LoginBean的Bean不能直接交由Dictionary维护");
        if (expireTime <= 0) expireTime = defaultSessionExpiredTime;
        BaseBean curBean = getActiveBean(newBean.getObjectName());
        if (curBean != null)
            deactivateBean(curBean.getObjectName());
        newBean.setExpireTime(expireTime);
        activeBeans.put(newBean.getObjectName(), newBean);
        refreshMsgCountDown(mDictionary.sessionHandler, MSG_EXPIRE, newBean, expireTime);
        return newBean;
    }

    /**
     * 大部分获取LoginBean的逻辑都伴随着获取不到后的自动下线及资源释放，此种情况应交由{@link com.cnepay.android.swiper.core.presenter.SessionPresenter}统一处理。通过{@link MvpView#requestLoginBean()}调用
     * @return 有效的loginBean
     */
    @Deprecated
    public static LoginBean getLoginBean() {
        return getActiveBean(LoginBean.class.getSimpleName());
    }

    /**
     * 登录信息信息替换，此方法不会替换token信息
     *
     * @param loginBean
     * @return
     */
    public static void replaceLoginBean(LoginBean loginBean) {
        LoginBean u = getLoginBean();
        if (u!=null)
            u.refresh(loginBean);
        else
            Logger.e(Dictionary.class.getSimpleName(),"replaceLoginBean curLoginBean is null!");
    }

    public static void deactivateBean(String key) {
        deactivateBean(key, false);
    }

    public static void deactivateBean(String key, boolean isSignOff) {
        Logger.e(Dictionary.class.getSimpleName(), "deactivateBean");
        if (TextUtils.isEmpty(key) && !isSignOff)
            return;
        if (isSignOff) {
            activeBeans.clear();
            mDictionary.sessionHandler.removeCallbacksAndMessages(null);
        }else {
            BaseBean activeBean = activeBeans.remove(key);
            if (activeBean != null)
                mDictionary.sessionHandler.removeMessages(MSG_EXPIRE, activeBean);
        }
    }

    private static void refreshMsgCountDown(Handler handler, int what, Object obj, long expired) {
        handler.removeMessages(what, obj);//如果找不到目标msg则无效果
        Message msg = handler.obtainMessage(what, obj);
        handler.sendMessageDelayed(msg, expired);
    }

    public static void resetSessionExpired() {
    }

    static class SessionHandler extends Handler {
        public SessionHandler(Looper l) {
            super(l);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_EXPIRE:
                    Logger.e(Dictionary.class.getSimpleName(), "MSG_EXPIRE");
                    BaseBean bean = (BaseBean) msg.obj;
                    if (bean != null) {
                        deactivateBean(bean.getObjectName());
                    }
                    break;
            }
        }
    }
}
