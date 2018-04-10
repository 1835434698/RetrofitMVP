package com.cnepay.android.swiper.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;

import com.cnepay.android.swiper.MainApp;
import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.core.MvpAppCompatActivity;
import com.cnepay.android.swiper.presenter.ResetPasswordPresenter;
import com.cnepay.android.swiper.utils.K;
import com.cnepay.android.swiper.utils.Logger;
import com.cnepay.android.swiper.utils.RegularUtils;
import com.cnepay.android.swiper.utils.Utils;
import com.cnepay.android.swiper.view.ResetPasswordView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tangzy on 2017/5/11.
 */

public class ModifyPwdTwoActivity extends MvpAppCompatActivity implements View.OnClickListener,ResetPasswordView {


    private static final String TAG = "ModifyPwdActivity";
    private Button next;
    private ImageView iv_close1;
    private CheckedTextView ctv_eye1;
    private EditText et_password1;
    private ImageView iv_close;
    private CheckedTextView ctv_eye;
    private EditText et_password;
    private String password1;
    private String password2;

    private String oldPassword;

    private ResetPasswordPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uc.setContentViewFrameWithBottomBtn(R.layout.activity_modifypwd_two);
        uc.setTitle(R.string.mine_modify_password);
        next = (Button) findViewById(R.id.next);
        next.setOnClickListener(this);
        ctv_eye1 = (CheckedTextView) findViewById(R.id.ctv_eye1);
        ctv_eye1.setOnClickListener(this);
        et_password1 = (EditText) findViewById(R.id.et_password1);
        et_password1.addTextChangedListener(textWatcher);
        iv_close1 = (ImageView) findViewById(R.id.iv_close1);
        iv_close1.setOnClickListener(this);
        et_password1.setHint(R.string.modify_new_password);

        ctv_eye = (CheckedTextView) findViewById(R.id.ctv_eye);
        ctv_eye.setOnClickListener(this);
        et_password = (EditText) findViewById(R.id.et_password);
        et_password.addTextChangedListener(textWatcher);
        iv_close = (ImageView) findViewById(R.id.iv_close);
        iv_close.setOnClickListener(this);
        et_password.setHint(R.string.modify_new_password_again);
        next.setEnabled(false);

        oldPassword=getIntent().getStringExtra(K.PASSWORD);
        presenter=new ResetPasswordPresenter();
        presenter.attachView(this);
        et_password1.setFilters(RegularUtils.FILTER_LIMIT_PWD_INPUT);
        et_password.setFilters(RegularUtils.FILTER_LIMIT_PWD_INPUT);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.next:
                if (!password2.equals(password1)){
                    uc.toast("输入密码不一致");
                    return;
                }

                Map<String, String> mapParams = new HashMap<>();
                mapParams.put("appVersion", MainApp.APP_VERSION);
                mapParams.put("oldPassword",oldPassword );
                mapParams.put("password", et_password.getText().toString().trim());
                presenter.resetPassword(mapParams);

                break;
            case R.id.ctv_eye1:
                ctv_eye1.toggle();
                if (ctv_eye1.isChecked()) {
//                    //如果选中，显示密码
                    et_password1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    et_password1.setInputType((InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD));
                    et_password1.setTypeface(Typeface.SANS_SERIF);
                } else {
//                    //否则隐藏密码
                    et_password1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    et_password1.setTypeface(Typeface.SANS_SERIF);
                }
                et_password1.requestFocus();
                et_password1.setSelection(et_password.getText().length());//光标置于最后

                break;
            case R.id.ctv_eye:
                ctv_eye.toggle();
                if (ctv_eye.isChecked()) {
//                    //如果选中，显示密码
                    et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    et_password.setInputType((InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD));
                    et_password.setTypeface(Typeface.SANS_SERIF);
                } else {
//                    //否则隐藏密码
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    et_password.setTypeface(Typeface.SANS_SERIF);
                }
                et_password.requestFocus();
                et_password.setSelection(et_password.getText().length());//光标置于最后

                break;
            case R.id.iv_close1:
                Logger.d(TAG, "iv_close.onClick");
                et_password1.setText("");
                break;
            case R.id.iv_close:
                Logger.d(TAG, "iv_close.onClick");
                et_password.setText("");
                break;

        }
    }

    /**
     * 监听EditText内容变化，根据变化显示或者隐藏相应图标。
     */
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String strInEditText = s.toString();
            Logger.i(TAG, "onTextChanged  s：" + strInEditText);
        }

        @Override
        public void afterTextChanged(Editable s) {
            Logger.i(TAG, "onTextChanged  s：");
//            phone = et_phone_number.getText().toString().trim();
            password1 = et_password1.getText().toString().trim();
            password2 = et_password.getText().toString().trim();
            if (!TextUtils.isEmpty(password1)) {
                iv_close1.setVisibility(View.VISIBLE);
            } else {
                iv_close1.setVisibility(View.INVISIBLE);
            }
            if (!TextUtils.isEmpty(password2)) {
                iv_close.setVisibility(View.VISIBLE);
            } else {
                iv_close.setVisibility(View.INVISIBLE);
            }

            if ( password2.length()>5 && password1.length()>5){
                next.setEnabled(true);
            }else {
                next.setEnabled(false);
            }
        }
    };


    @Override
    public void resetPassword(Object content) {
        Logger.i("wjl","修改密码  跳转");
        Intent intent = new Intent(this, ResultDemonstrationActivity.class);
        intent.putExtra(ResultDemonstrationActivity.class.getSimpleName(), ResultDemonstrationActivity.TYPE_CHANGE_PWD);
        ac.startCallbackActivity(intent);
        et_password.setText("");
    }
}
