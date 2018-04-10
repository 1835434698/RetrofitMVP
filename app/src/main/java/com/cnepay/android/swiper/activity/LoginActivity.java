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
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.bean.LoginBean;
import com.cnepay.android.swiper.core.MvpAppCompatActivity;
import com.cnepay.android.swiper.presenter.LoginPresenter;
import com.cnepay.android.swiper.model.Dictionary;
import com.cnepay.android.swiper.utils.Logger;
import com.cnepay.android.swiper.utils.Utils;
import com.cnepay.android.swiper.view.LoginView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by tangzy on 2017/4/27.
 */

public class LoginActivity extends MvpAppCompatActivity implements View.OnClickListener, LoginView {
    private static final String TAG = "LoginActivity";
    private ImageView iv_close;
    private CheckedTextView ctv_eye;
    private TextView tv_forget;
    private TextView tv_register;
    private Button login;
    private EditText et_phone_number;
    private EditText et_password;
    private CheckBox checkBox;

    private LoginPresenter mLoginPresenter;
//    private CheckBox cb_remember;

    private String phone;
    private String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uc.setContentViewPure(R.layout.activity_login);
        uc.initStatusBar(R.color.white);

        if (Dictionary.getLoginBean() != null) {
            Logger.e(getClass().getSimpleName(), "onCreate\n"+Dictionary.getLoginBean().toString());
            Intent i = new Intent(this, HomePageActivity.class);
            ac.startActivity(i);
            finish();
            return;
        }

        boolean isNewlyInstalled = Utils.isNewlyInstalled(this);
        if (isNewlyInstalled) {
            ac.startActivity(new Intent(this, NewFeaturesShowActivity.class));
        }
//        cb_remember = (CheckBox) findViewById(R.id.cb_remember);
//        cb_remember.setOnClickListener(this);
        et_phone_number = (EditText) findViewById(R.id.et_phone_number);
        et_password = (EditText) findViewById(R.id.et_password);
        iv_close = (ImageView) findViewById(R.id.iv_close);
        ctv_eye = (CheckedTextView) findViewById(R.id.ctv_eye);
        tv_forget = (TextView) findViewById(R.id.tv_forget);
        tv_register = (TextView) findViewById(R.id.tv_register);
        login = (Button) findViewById(R.id.resultact_btn);
        checkBox = (CheckBox) findViewById(R.id.cb_remember);
        checkBox.setOnClickListener(this);

        tv_register.setOnClickListener(this);
        tv_forget.setOnClickListener(this);
        ctv_eye.setOnClickListener(this);
        iv_close.setOnClickListener(this);
        login.setOnClickListener(this);
        et_phone_number.addTextChangedListener(textWatcher);
        et_password.addTextChangedListener(textWatcher);

        mLoginPresenter = new LoginPresenter();
        mLoginPresenter.attachView(this);
        phone = Utils.getPhone(this);
        if (phone != null) {
            et_phone_number.setText(phone);
        }
        login.setEnabled(false);
        Utils.focusAndShowInputMethod(et_phone_number, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLoginPresenter!=null)
            mLoginPresenter.detachView(false);
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
            phone = et_phone_number.getText().toString().trim();
            password = et_password.getText().toString().trim();
            if (!TextUtils.isEmpty(password)) {
                iv_close.setVisibility(View.VISIBLE);
            } else {
                iv_close.setVisibility(View.INVISIBLE);
                login.setEnabled(false);
                return;
            }
            if (Utils.verifyPhone(LoginActivity.this, phone) && password.length() > 5) {
                login.setEnabled(true);
            } else {
                login.setEnabled(false);
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ctv_eye:
                Logger.d(TAG, "ctv_eye.onClick");
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
            case R.id.iv_close:
                Logger.d(TAG, "iv_close.onClick");
                et_password.setText("");
                break;
            case R.id.tv_forget:
                Logger.d(TAG, "tv_forget.onClick");
                ac.startCallbackActivity(new Intent(this, ForgotPwdActivity.class));
                break;
            case R.id.tv_register:
                Logger.d(TAG, "tv_register.onClick");
                ac.startCallbackActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.resultact_btn:
                String phone = et_phone_number.getText().toString().trim();
                String psd = et_password.getText().toString().trim();
                if (Utils.verifyPhone(this, phone) && Utils.verifyPasswd4OldUsers(this, psd)) {
                    String position = "116.379062,39.97077";
                    String reqTime = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(new Date());
                    String registId = "1122312233";//推送ID
                    mLoginPresenter.login(phone, psd, position, registId, reqTime,checkBox.isChecked());
                }
                break;
        }
    }

    private void startHomeActivity() {
        ac.startCallbackActivity(new Intent(this, HomePageActivity.class));
        finish();
    }

    @Override
    public void login(LoginBean content) {
            startHomeActivity();
            uc.toast("登陆成功");
        }
}
