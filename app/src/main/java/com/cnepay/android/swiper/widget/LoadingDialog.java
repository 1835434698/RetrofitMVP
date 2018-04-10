package com.cnepay.android.swiper.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnepay.android.swiper.R;

public class LoadingDialog extends Dialog {

    private TextView mMsgTv;
    private ImageView mLoadImg;
    private AnimationDrawable animation;


    public LoadingDialog(@NonNull Context context) {
        super(context);
        init();
    }

    public LoadingDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        init();
    }

    protected LoadingDialog(@NonNull Context context, boolean cancelable
            , @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private void init() {
        View view = getLayoutInflater().inflate(R.layout.progress_dialog, null);
        setContentView(view);
        mMsgTv = (TextView) view.findViewById(R.id.load_text);
        mLoadImg = (ImageView) view.findViewById(R.id.load_img);
    }

    public void setMessage(String message) {
        mMsgTv.setText(message);
    }

    public void setMessage(int msgId) {
        setMessage(getContext().getResources().getString(msgId));
    }

    @Override
    public void show() {
        if (!isShowing()) {
            super.show();
            animation = (AnimationDrawable) mLoadImg.getBackground();
            animation.start();
        }
    }

    @Override
    public void dismiss() {
        if (isShowing() && animation != null && animation.isRunning()) {
            animation.stop();
            animation = null;
        }
        super.dismiss();
    }
}
