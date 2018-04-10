package com.cnepay.android.swiper.core.presenter;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.core.view.MvpView;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017/4/27.
 */

public final class ActivityInflaterController extends MvpPresenterIml<MvpView> {

    private Toast lastToast;

    private RelativeLayout relTitle;
    private TextView tvLeft;
    private ImageView imgLeft;
    private ImageView imgLeftTip;
    private TextView tvTitle;
    private TextView tvRight;
    private ImageView imgRight;
    private ImageView imgRightTip;
    private Button secondaryButton;
    private Button primaryButton;

    /**
     * 填充布局，正常显示
     *
     * @param layoutId
     */
    public void setContentViewPure(@LayoutRes int layoutId) {
        setContentView(layoutId, -1);
    }

    /**
     * 填充布局，只有一个Title
     *
     * @param layoutId
     */
    public void setContentViewWithTitle(@LayoutRes int layoutId) {
        setContentView(R.layout.mul_layout_uibase_blank, layoutId);
    }

    /**
     * 填充布局，固定大小Layout,带有Title和底部按钮
     *
     * @param layoutId
     */
    public void setContentViewFrameWithBottomBtn(@LayoutRes int layoutId) {
        setContentView(R.layout.mul_layout_uibase_with_aptotic_bottombtn, layoutId);
    }

    /**
     * 填充布局，可上下滚动Layout,带有Title和底部位置按钮，按钮固定屏幕下方
     *
     * @param layoutId
     */
    public void setContentViewScrollWithAptoticBottomBtn(@LayoutRes int layoutId) {
        setContentView(R.layout.mul_layout_uibase_with_aptotic_bottombtn_scoll, layoutId);
    }

    /**
     * 填充布局，可上下滚动Layout,带有Title和底部按钮，按钮跟随UI滚动
     *
     * @param layoutId
     */
    public void setContentViewScrollWithFollowBottomBtn(@LayoutRes int layoutId) {
        setContentView(R.layout.mul_layout_uibase_with_follow_bottombtn_scoll, layoutId);
    }

    private void setContentView(@LayoutRes int parentLayoutId, @LayoutRes int childrenLayoutId) {
        getView().obtainActivity().setContentView(parentLayoutId);
        if (-1 == childrenLayoutId) return;
        ViewGroup vg = (ViewGroup) getView().obtainActivity().findViewById(R.id.uiContainer);
        ViewGroup parentContainer = (ViewGroup) getView().obtainActivity().findViewById(R.id.parentContainer);
        LayoutInflater inflater = LayoutInflater.from(getView().obtainContext());
        inflater.inflate(childrenLayoutId, vg);
        initAllViewForActivity(parentContainer);
    }

    private void initAllViewForActivity(ViewGroup vp) {
        initAllTitle(vp);
        primaryButton = (Button) vp.findViewById(R.id.primaryBottomButton);
        secondaryButton = (Button) vp.findViewById(R.id.secondaryBottomButton);
    }

    private void initAllTitle(ViewGroup vp) {
        relTitle = (RelativeLayout) vp.findViewById(R.id.relTitle);
        if (null != relTitle) {
            tvLeft = (TextView) relTitle.findViewById(R.id.tvLeft);
            imgLeft = (ImageView) relTitle.findViewById(R.id.imgLeft);
            imgLeftTip = (ImageView) relTitle.findViewById(R.id.imgLeftTip);
            imgRight = (ImageView) relTitle.findViewById(R.id.imgRight);
            imgRightTip = (ImageView) relTitle.findViewById(R.id.imgRightTip);
            tvTitle = (TextView) relTitle.findViewById(R.id.tvTitle);
            tvRight = (TextView) relTitle.findViewById(R.id.tvRight);
        }
        initStatusBar(R.color.colorE44036);
        if (R.style.AppRedTheme != getThemeId()) {
            setTextColor(tvLeft, R.color.color333333);
            setTextColor(tvRight, R.color.color333333);
            setTextColor(tvTitle, R.color.color333333);
            imgLeft.setImageResource(R.drawable.all_img_drak_back);
            relTitle.setBackgroundColor(ContextCompat.getColor(getView().obtainContext(), R.style.HomePageStyle == getThemeId() ? R.color.colorF1F1F1 : R.color.colorWhite));
            initStatusBar(R.style.HomePageStyle == getThemeId() ? R.color.colorF1F1F1 : R.color.colorWhite);
        }
    }

    private int getThemeId() {
        try {
            Class<?> wrapper = ContextThemeWrapper.class;
            Method method = wrapper.getMethod("getThemeResId");
            method.setAccessible(true);
            return (Integer) method.invoke(getView().obtainActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void hideTitle() {
        if (relTitle != null) {
            relTitle.setVisibility(View.GONE);
        }
    }

    public RelativeLayout getRelTitle() {
        return relTitle;
    }

    public void setTitle(String title) {
        if (null != tvTitle)
            tvTitle.setText(title);
    }

    public void setTitleIcon(@DrawableRes int resId) {
        if (null != tvTitle)
            tvTitle.setBackgroundResource(resId);
    }

    private void setTextColor(TextView tv, @ColorRes int color) {
        if (null != tv)
            tv.setTextColor(ContextCompat.getColor(getView().obtainContext(), color));
    }

    public void setBackIcon(@DrawableRes int resId) {
        if (null != imgLeft) {
            imgLeft.setImageResource(resId);
            imgLeft.setVisibility(View.VISIBLE);
        }
    }

    public ImageView getBackIcon() {
        if (null != imgLeft) {
            imgLeft.setVisibility(View.VISIBLE);
        }
        return imgLeft;
    }

    /**
     * 在oncreate之后调用生效
     */
    public void hideBackIcon() {
        if (null != imgLeft)
            imgLeft.setVisibility(View.GONE);
    }

    public void setTitle(@StringRes int id) {
        setTitle(getView().obtainContext().getString(id));
    }

    public TextView getLeftText() {
        if (tvLeft != null) tvLeft.setVisibility(View.VISIBLE);
        return tvLeft;
    }

    public ImageView getLeftTip() {
        if (imgLeftTip != null) imgLeftTip.setVisibility(View.VISIBLE);
        return imgLeftTip;
    }

    public TextView getRightText() {
        if (tvRight != null) tvRight.setVisibility(View.VISIBLE);
        return tvRight;
    }

    public ImageView getRightTip() {
        if (imgRightTip != null) imgRightTip.setVisibility(View.VISIBLE);
        return imgRightTip;
    }

    public ImageView getRightImage() {
        if (imgRight != null) imgRight.setVisibility(View.VISIBLE);
        return imgRight;
    }

    /**
     * 获取底部的PrimaryButton的引用
     *
     * @return Button
     */
    public Button getPrimaryBtn() {
        if (null == primaryButton) return null;
        primaryButton.setVisibility(View.VISIBLE);
        return primaryButton;
    }

    /**
     * 获取底部的SecondaryButton的引用
     *
     * @return Button
     */
    public Button getSecondaryBtn() {
        if (null == secondaryButton) return null;
        secondaryButton.setVisibility(View.VISIBLE);
        return secondaryButton;
    }

    /**
     * 给最外层布局填充颜色，默认白色
     *
     * @param color
     */
    public void setParentBackgroundColor(@ColorRes int color) {
        ViewGroup parentContainer = (ViewGroup) getView().obtainActivity().findViewById(R.id.parentContainer);
        if (parentContainer != null)
            parentContainer.setBackgroundColor(ContextCompat.getColor(getView().obtainContext(), color));
    }

    /**
     * Toast
     *
     * @param content
     */
    public void toast(String content) {
        if (lastToast == null) {
            lastToast = Toast.makeText(getView().obtainContext(), content, Toast.LENGTH_SHORT);
        } else {
            lastToast.setText(content);
        }
        lastToast.show();
    }

    public void toast(int resId){
        toast(getView().obtainContext().getResources().getString(resId));
    }


    /**
     * 状态栏处理：解决全屏切换非全屏页面被压缩问题
     */
    public void initStatusBar( int barColor) {
        Activity activity = getView().obtainActivity();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT <Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
            // 获取状态栏高度
            int statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
            View rectView = new View(activity);
            // 绘制一个和状态栏一样高的矩形，并添加到视图中
            LinearLayout.LayoutParams params
                    = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
            rectView.setLayoutParams(params);
            //设置状态栏颜色
            rectView.setBackgroundColor(ContextCompat.getColor(activity, barColor));
            // 添加矩形View到布局中
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            decorView.addView(rectView);
            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);
        }
    }

}
