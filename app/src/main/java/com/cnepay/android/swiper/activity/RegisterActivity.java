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
import android.widget.LinearLayout;

import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.bean.BaseBean;
import com.cnepay.android.swiper.core.MvpAppCompatActivity;
import com.cnepay.android.swiper.presenter.RegisterPresenter;
import com.cnepay.android.swiper.presenter.SendMobileMessagePresenter;
import com.cnepay.android.swiper.utils.Countdown;
import com.cnepay.android.swiper.utils.Logger;
import com.cnepay.android.swiper.utils.RegularUtils;
import com.cnepay.android.swiper.utils.Utils;
import com.cnepay.android.swiper.view.RegisterView;
import com.cnepay.android.swiper.view.SendMobileMessageView;

/**
 * Created by tangzy on 2017/4/28.
 */

public class RegisterActivity extends MvpAppCompatActivity implements View.OnClickListener, RegisterView, SendMobileMessageView {

    private static final String TAG = "RegisterActivity";

    private EditText et_phone_number;
    private EditText et_code;
    private EditText et_password;
    private ImageView iv_close;
    private CheckedTextView ctv_eye;

    private Button register;
    private Button btn_get_verification_code;
    private Countdown mCountdown;

    private RegisterPresenter registerPresenter;
    private SendMobileMessagePresenter sendMobileMessagePresenter;
    private String mobil;
    private String password;
    private String code;
    private LinearLayout ll_agreement;

    private String cookie;//请求短信验证码的cookie


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uc.setContentViewFrameWithBottomBtn(R.layout.activity_register);
        et_phone_number = (EditText) findViewById(R.id.et_phone_number);
        et_code = (EditText) findViewById(R.id.et_code);
        et_password = (EditText) findViewById(R.id.et_password);
        iv_close = (ImageView) findViewById(R.id.iv_close);
        ctv_eye = (CheckedTextView) findViewById(R.id.ctv_eye);
        btn_get_verification_code = (Button) findViewById(R.id.btn_get_verification_code);
        btn_get_verification_code.setOnClickListener(this);
        register = (Button) findViewById(R.id.register);
        ll_agreement = (LinearLayout) findViewById(R.id.ll_agreement);
        register.setOnClickListener(this);
        ctv_eye.setOnClickListener(this);
        iv_close.setOnClickListener(this);
        ll_agreement.setOnClickListener(this);
        et_phone_number.addTextChangedListener(textWatcher);
        et_code.addTextChangedListener(textWatcher);
        et_password.addTextChangedListener(textWatcher);
        et_password.setFilters(RegularUtils.FILTER_LIMIT_PWD_INPUT);

        mCountdown = new Countdown(this, btn_get_verification_code, 60 * 1000, 1000);

        registerPresenter = new RegisterPresenter();
        sendMobileMessagePresenter = new SendMobileMessagePresenter();
        registerPresenter.attachView(this);
        sendMobileMessagePresenter.attachView(this);
        btn_get_verification_code.setEnabled(false);
        register.setEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerPresenter.detachView(false);
        sendMobileMessagePresenter.detachView(false);
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
            mobil = et_phone_number.getText().toString().trim();
            password = et_password.getText().toString().trim();
            code = et_code.getText().toString();
            if (Utils.verifyPhone(RegisterActivity.this, mobil)) {
                btn_get_verification_code.setEnabled(true);
            } else {
                btn_get_verification_code.setEnabled(false);
                register.setEnabled(false);
                return;
            }
            if (!TextUtils.isEmpty(password)) {
                iv_close.setVisibility(View.VISIBLE);
            } else {
                iv_close.setVisibility(View.INVISIBLE);
                register.setEnabled(false);
                return;
            }
            if (TextUtils.isEmpty(code)) {
                register.setEnabled(false);
                return;
            }
            if (password.length() > 5) {
                register.setEnabled(true);
            } else {
                register.setEnabled(false);
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
            case R.id.register:
                if (Utils.verifyMsgVCode(RegisterActivity.this, code) &&
                        Utils.verifyPhone(RegisterActivity.this, mobil) &&
                        Utils.verifyPasswd4OldUsers(RegisterActivity.this, password)) {
                    registerPresenter.register(mobil, password, code,cookie);
                }

                break;
            case R.id.btn_get_verification_code:
                mobil = et_phone_number.getText().toString().trim();
                if (Utils.verifyPhone(RegisterActivity.this, mobil)) {
                    sendMobileMessagePresenter.sendMobileMessage(mobil, "registe");
                }

                break;
            case R.id.ll_agreement:
                ac.startWebActivity(this, WebViewActivity.NOTITLE_WHITE_WEB_VIEW, "register-agreement.html", getString(R.string.agreement_title));
                break;
        }
    }


    @Override
    public void onRegister(Object content) {
        Logger.i("wjl","注册成功  跳转");
        BaseBean bean = (BaseBean) content;
        if (bean.isSuccess()) {
            Intent intent = new Intent(this, ResultDemonstrationActivity.class);
            intent.putExtra(ResultDemonstrationActivity.class.getSimpleName(), ResultDemonstrationActivity.TYPE_REGISTER);
            ac.startCallbackActivity(intent);
            et_password.setText("");
        }
    }

    @Override
    public void onSendMobileMessage(String cookie,String respMsg ) {
        mCountdown.start();//访问成功后  进行倒计时
        this.cookie= cookie;
        uc.toast(respMsg);

    }
}
