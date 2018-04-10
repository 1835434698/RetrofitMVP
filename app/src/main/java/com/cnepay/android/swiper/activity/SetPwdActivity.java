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
import com.cnepay.android.swiper.presenter.ResetPasswordWithoutSessionPresenter;
import com.cnepay.android.swiper.utils.K;
import com.cnepay.android.swiper.utils.Logger;
import com.cnepay.android.swiper.utils.RegularUtils;
import com.cnepay.android.swiper.view.ResetPasswordView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tangzy on 2017/4/28.
 * 找回密码  第二步
 */

public class  SetPwdActivity extends MvpAppCompatActivity implements View.OnClickListener, ResetPasswordView {

    private static final String TAG = "RegisterActivity";


    private Button next;
    private EditText et_password;
    private ImageView iv_close;
    private CheckedTextView ctv_eye;
    private String password;

    private ResetPasswordWithoutSessionPresenter presenter;
    private String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getStringExtra(K.MOBILE) != null) {
            mobile = getIntent().getStringExtra(K.MOBILE);
        }
        initView();
    }


    private void initView() {
        uc.setContentViewFrameWithBottomBtn(R.layout.activity_setpwd);
        uc.initStatusBar(R.color.white);
        et_password = (EditText) findViewById(R.id.et_password);
        iv_close = (ImageView) findViewById(R.id.iv_close);
        ctv_eye = (CheckedTextView) findViewById(R.id.ctv_eye);

        next = (Button) findViewById(R.id.next);
        next.setOnClickListener(this);
        ctv_eye.setOnClickListener(this);
        iv_close.setOnClickListener(this);
        et_password.addTextChangedListener(textWatcher);
        next.setEnabled(false);
        presenter = new ResetPasswordWithoutSessionPresenter();
        presenter.attachView(this);
        et_password.setFilters(RegularUtils.FILTER_LIMIT_PWD_INPUT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView(false);
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
            password = et_password.getText().toString().trim();
            if (!TextUtils.isEmpty(password)) {
                iv_close.setVisibility(View.VISIBLE);
            } else {
                iv_close.setVisibility(View.INVISIBLE);
                next.setEnabled(false);
                return;
            }
            if (password.length() > 5) {
                next.setEnabled(true);
            } else {
                next.setEnabled(false);
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
            case R.id.next:

                Map<String, String> mapParams = new HashMap<>();
                mapParams.put("appVersion", MainApp.APP_VERSION);
                mapParams.put("mobile",mobile );
                mapParams.put("password", et_password.getText().toString().trim());
                presenter.resetPasswordWithoutSession(mapParams);
                break;
        }
    }

    @Override
    public void resetPassword(Object content) {
        Intent intent = new Intent(this, ResultDemonstrationActivity.class);
        intent.putExtra(ResultDemonstrationActivity.class.getSimpleName(), ResultDemonstrationActivity.TYPE_RETRIEVE_PWD);
        ac.startCallbackActivity(intent);
        et_password.setText("");
    }
}
