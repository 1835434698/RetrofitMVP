package com.cnepay.android.swiper.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.SimpleImplementations.SimpleAnimationListener;
import com.cnepay.android.swiper.SimpleImplementations.SimpleTextWatcher;
import com.cnepay.android.swiper.core.MvpAppCompatActivity;
import com.cnepay.android.swiper.presenter.BindDevicePresenter;
import com.cnepay.android.swiper.utils.Logger;
import com.cnepay.android.swiper.utils.RegularUtils;
import com.cnepay.android.swiper.view.BindDeviceView;

/**
 * Created by XuShiWei on 2017/5/5.
 */

public class BindDeviceActivity extends MvpAppCompatActivity implements View.OnClickListener, BindDeviceView {

    public static final int DURATION = 200;
    private RelativeLayout searchView;
    private View shade;
    private Button btn;
    private boolean isSearchViewShowing;
    private boolean animationPlaying;

    private int ACTIVATE_CODE_ET_MAX_LENGTH = -1;
    private EditText et_dialog_activate_code;
    private Button bt_dialog_confirm;
    private BindDevicePresenter bindDevicePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        uc.setContentViewPure(R.layout.activity_bind_device);

        searchView = (RelativeLayout) findViewById(R.id.binddevact_rl_searchview);
        findViewById(R.id.binddevact_tv_close).setOnClickListener(this);
        findViewById(R.id.binddevact_rl_searchview).setOnClickListener(this);
        findViewById(R.id.binddevact_searchview_bt).setOnClickListener(v -> bindDevicePresenter.cancel());

        btn = (Button) findViewById(R.id.primaryBottomButton);
        btn.setVisibility(View.VISIBLE);
        btn.setText(getString(R.string.replace_device));
        btn.setOnClickListener(this);
        shade = findViewById(R.id.binddevact_view_shade);
        searchView.setVisibility(View.GONE);
        shade.setVisibility(View.GONE);
        isSearchViewShowing = false;
        animationPlaying = false;

        bindDevicePresenter = new BindDevicePresenter();
        bindDevicePresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bindDevicePresenter.detachView(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.primaryBottomButton:
                bindDevicePresenter.searchDevice();
                break;
            case R.id.binddevact_tv_close:
                super.onBackPressed();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        bindDevicePresenter.cancel();
    }

    public void switchSearchView(boolean isOpen) {
        if (animationPlaying)
            return;
        if (isSearchViewShowing && isOpen)//此处以及下面的两个if判断，用来约束外部调用，只有当关时才能打开，开时才能关上
            return;
        if (!isSearchViewShowing && !isOpen){
            super.onBackPressed();
            return;
        }
        searchView.clearAnimation();
        animationPlaying = true;
        Animation searchViewAnim;
        Animation shadeAnim;
        if (isSearchViewShowing){
            searchViewAnim = AnimationUtils.loadAnimation(this, R.anim.search_view_down);
            searchViewAnim.setAnimationListener(new SimpleAnimationListener(){
                @Override
                public void onAnimationEnd(Animation animation) {
                    searchView.setVisibility(View.GONE);
                    shade.setClickable(false);
                    shade.setVisibility(View.GONE);
                    animationPlaying = false;
                }
            });
            shadeAnim = AnimationUtils.loadAnimation(this,R.anim.shade_out);
        }else {
            searchView.setVisibility(View.VISIBLE);
            shade.setClickable(true);
            shade.setVisibility(View.VISIBLE);
            searchViewAnim = AnimationUtils.loadAnimation(this,R.anim.search_view_up);
            searchViewAnim.setAnimationListener(new SimpleAnimationListener(){
                @Override
                public void onAnimationEnd(Animation animation) {
                    animationPlaying = false;
                }
            });
            shadeAnim = AnimationUtils.loadAnimation(this,R.anim.shade_in);
        }
        searchViewAnim.setDuration(DURATION);
        shadeAnim.setDuration(DURATION);
        searchView.startAnimation(searchViewAnim);
        shade.startAnimation(shadeAnim);
        isSearchViewShowing = !isSearchViewShowing;
    }

    private void promptDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.dialog_bind_device_act);
        builder.setTitle("设备激活").setMessage("请输入购买获得的16位激活码").setPositiveButton(getString(android.R.string.ok), null).setNegativeButton(android.R.string.cancel,null).setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        et_dialog_activate_code = (EditText) alertDialog.findViewById(R.id.binddevact_dialog_et_activatecode);
        bt_dialog_confirm = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        bt_dialog_confirm.setEnabled(false);
        et_dialog_activate_code.setFilters(RegularUtils.FILTER_LIMIT_ACTIVATE_CODE);
        et_dialog_activate_code.addTextChangedListener(new SimpleTextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                Logger.e(BindDeviceActivity.class.getSimpleName(), "afterTextChanged");
                bt_dialog_confirm.setEnabled(s.length() == ACTIVATE_CODE_ET_MAX_LENGTH);
                if (s.length()>ACTIVATE_CODE_ET_MAX_LENGTH){
                    et_dialog_activate_code.setText(s.subSequence(0,ACTIVATE_CODE_ET_MAX_LENGTH));
                    et_dialog_activate_code.setSelection(ACTIVATE_CODE_ET_MAX_LENGTH);
                }
            }
        });
        ACTIVATE_CODE_ET_MAX_LENGTH = getResources().getInteger(R.integer.et_activate_code_max_length);
    }

    @Override
    public void toast(int type) {
        switch (type){
            case BindDevicePresenter.DEVICE_BUSY:
                uc.toast(R.string.notice_device_busy);
                break;
        }
    }
}
