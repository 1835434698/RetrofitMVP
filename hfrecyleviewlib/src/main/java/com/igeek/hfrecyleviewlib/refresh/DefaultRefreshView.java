package com.igeek.hfrecyleviewlib.refresh;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.igeek.hfrecyleviewlib.R;
import com.igeek.hfrecyleviewlib.utils.DensityUtils;

public class DefaultRefreshView extends LinearLayout implements IPullRefreshView {

    private static final String TAG = "DefaultRefreshView";

    private static final int ANIMATION_DURATION = 150;
    private static final Interpolator ANIMATION_INTERPOLATOR = new DecelerateInterpolator();

    private ImageView icon;
    private TextView text;

    public DefaultRefreshView(Context context) {
        this(context, null);
    }

    public DefaultRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setGravity(Gravity.CENTER);
        setOrientation(HORIZONTAL);
        if (icon == null) {
            LayoutParams lp = new LayoutParams(DensityUtils.dp2px(30), DensityUtils.dp2px(30));
            icon = new ImageView(getContext());
            icon.setImageResource(R.drawable.loading10);
            addView(icon, lp);
        }

        if (text == null) {
            LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp.leftMargin = DensityUtils.dp2px(8);
            text = new TextView(getContext());
            text.setTextSize(14);
            text.setTextColor(Color.parseColor("#a9a8a8"));
            text.setText(R.string.pulling);
            addView(text, lp);
        }

        initAnimation();
    }

    private void initAnimation() {

    }

    @Override
    public void onPullHided() {
//        Log.e(TAG, "隐藏");
        icon.setVisibility(VISIBLE);
        icon.clearAnimation();
        icon.setImageDrawable(getResources().getDrawable(R.drawable.loading10));
        text.setText(R.string.pulling);
    }

    @Override
    public void onPullRefresh() {
//        Log.e(TAG, "刷新中");
        icon.setVisibility(VISIBLE);
        icon.clearAnimation();
        AnimationDrawable drawable = (AnimationDrawable) getResources().getDrawable(R.drawable.refresh);
        icon.setImageDrawable(drawable);
        drawable.start();
        text.setText(R.string.pulling_refreshing);
    }

    @Override
    public void onPullFreeHand() {
//        Log.e(TAG, "提示松手 ");
        icon.setVisibility(VISIBLE);
        icon.clearAnimation();
        text.setText(R.string.pulling_refresh);
    }

    @Override
    public void onPullDowning() {
//        Log.e(TAG, "下拉中....");
        icon.setVisibility(VISIBLE);
        icon.clearAnimation();
        icon.setImageDrawable(getResources().getDrawable(R.drawable.loading10));
        text.setText(R.string.pulling);
    }

    @Override
    public void onPullFinished() {
//        Log.e(TAG, "刷新完成");
        AnimationDrawable drawable = (AnimationDrawable) getResources().getDrawable(R.drawable.refresh);
        drawable.stop();
        icon.setVisibility(INVISIBLE);
        text.setText(R.string.pulling_refreshfinish);
    }

    @Override
    public void onPullProgress(float pullDistance, float pullProgress) {

        if (pullProgress <= 1) {
            icon.setAlpha(pullProgress);
            icon.setScaleX(pullProgress);
            icon.setScaleY(pullProgress);
        }
    }
}
