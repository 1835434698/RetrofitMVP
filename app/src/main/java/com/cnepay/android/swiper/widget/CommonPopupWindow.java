package com.cnepay.android.swiper.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.util.SparseArray;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * Created by xugang on 2016/7/12.
 * 跑跑窗口
 */
public class CommonPopupWindow extends PopupWindow {

    private View mContextView;
    private SparseArray<View> mViews;
    private Context mContext;
    public static final int WRAP = ViewGroup.LayoutParams.WRAP_CONTENT;
    public static final int MATCH = ViewGroup.LayoutParams.MATCH_PARENT;

    public CommonPopupWindow(Context context, @LayoutRes int layoutId) {
        init(context, layoutId, MATCH, WRAP, true);
    }

    public CommonPopupWindow(Context context, @LayoutRes int layoutId, int width, int height) {
        this(context, layoutId, width, height, true);
    }

    public CommonPopupWindow(Context context, @LayoutRes int layoutId, int width, int height, boolean isOutsideTouchable) {
        super(context);
        init(context, layoutId, width, height, isOutsideTouchable);
    }

    private void init(Context context, @LayoutRes int layoutId, int width, int height, boolean isOutsideTouchable) {
        mContext = context;
        mContextView = LayoutInflater.from(context).inflate(layoutId, null);
        mViews = new SparseArray<>();
        setContentView(mContextView);
        //解决Navigation Bar遮挡PopupWindow的解决方法，这个方法不会影响到状态栏的颜色改变
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // 设置SelectPicPopupWindow弹出窗体的宽
        setWidth(width);
        // 设置SelectPicPopupWindow弹出窗体的高
        setHeight(height);
        // 使其聚集
        setFocusable(true);
        // 设置允许在外点击消失
        setOutsideTouchable(isOutsideTouchable);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        setBackgroundDrawable(new BitmapDrawable());
    }

    /**
     * 是否允许PopupWindow外部事件，包含back键
     *
     * @param flag
     * @return
     */
    public CommonPopupWindow setAllowOutsideTouchEvent(boolean flag) {
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        setBackgroundDrawable(flag ? new BitmapDrawable() : null);
        return this;
    }

    public CommonPopupWindow setAnimStyle(@StyleRes int styleId) {
        setAnimationStyle(styleId);
        return this;
    }

    public CommonPopupWindow setDismissListener(OnDismissListener onDismissListener) {
        setOnDismissListener(onDismissListener);
        return this;
    }

    /**
     * 设置透明度
     *
     * @param alpha
     * @return
     */
    public CommonPopupWindow setWindowAlpha(Activity activity, float alpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = alpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
        return this;
    }

    /**
     * 通过ID获取View
     */
    public <T extends View> T findOrGetViewById(@IdRes int id) {
        View view = mViews.get(id);
        if (view == null) {
            view = mContextView.findViewById(id);
            mViews.put(id, view);
        }

        return (T) view;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public CommonPopupWindow setText(int viewId, String text) {
        ((TextView) findOrGetViewById(viewId)).setText(text);
        return this;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param textId
     * @return
     */
    public CommonPopupWindow setText(int viewId, @StringRes int textId) {
        ((TextView) findOrGetViewById(viewId)).setText(textId);
        return this;
    }

    /**
     * 为View 设置监听
     *
     * @param viewId
     * @param listener
     * @return
     */
    public CommonPopupWindow setOnClickListener(int viewId, View.OnClickListener listener) {
        findOrGetViewById(viewId).setOnClickListener(listener);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param drawableId
     * @return
     */
    public CommonPopupWindow setImageResource(int viewId, int drawableId) {
        ImageView view = findOrGetViewById(viewId);
        view.setImageResource(drawableId);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param bm
     * @return
     */
    public CommonPopupWindow setImageBitmap(int viewId, Bitmap bm) {
        ImageView view = findOrGetViewById(viewId);
        view.setImageBitmap(bm);
        return this;
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private Point getPoint(WindowManager wm) {
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
            return getPoint(wm).x;
        return wm.getDefaultDisplay().getWidth();
    }

    public int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
            return getPoint(wm).y;
        return wm.getDefaultDisplay().getHeight();
    }
}
