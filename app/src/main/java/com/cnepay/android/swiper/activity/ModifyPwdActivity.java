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
import android.widget.TextView;

import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.core.MvpAppCompatActivity;
import com.cnepay.android.swiper.model.Dictionary;
import com.cnepay.android.swiper.presenter.ValidatePasswordPresenter;
import com.cnepay.android.swiper.utils.K;
import com.cnepay.android.swiper.utils.Logger;
import com.cnepay.android.swiper.view.ValidatePasswordView;

/**
 * Created by tangzy on 2017/5/11.
 */

public class ModifyPwdActivity extends MvpAppCompatActivity implements View.OnClickListener, ValidatePasswordView {

    private static final String TAG = "ModifyPwdActivity";
    private Button next;
    private ImageView iv_close;
    private CheckedTextView ctv_eye;
    private EditText et_password;
    private TextView et_phone_number;

    private String password;

    private ValidatePasswordPresenter passwordPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        uc.setContentViewFrameWithBottomBtn(R.layout.activity_modifypwd);
        uc.setTitle(R.string.mine_modify_password);
        ctv_eye = (CheckedTextView) findViewById(R.id.ctv_eye);
        ctv_eye.setOnClickListener(this);
        next = (Button) findViewById(R.id.next);
        next.setOnClickListener(this);
        et_phone_number = (TextView) findViewById(R.id.et_phone_number);
        et_password = (EditText) findViewById(R.id.et_password);
        et_password.addTextChangedListener(textWatcher);
        iv_close = (ImageView) findViewById(R.id.iv_close);
        iv_close.setOnClickListener(this);
        et_password.setHint(R.string.modify_old_password);

        if (Dictionary.getLoginBean() != null) {
            String loginName = Dictionary.getLoginBean().getLoginName();
            et_phone_number.setText(loginName);
        }

        next.setEnabled(false);

        passwordPresenter = new ValidatePasswordPresenter();
        passwordPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        passwordPresenter.detachView(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next:
                password = et_password.getText().toString().trim();
                passwordPresenter.validatePassword(password);
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
            password = et_password.getText().toString().trim();
            if (!TextUtils.isEmpty(password)) {
                iv_close.setVisibility(View.VISIBLE);
            } else {
                iv_close.setVisibility(View.INVISIBLE);
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
    public void validatePasswordView(Object content) {
        Intent intent = new Intent(this, ModifyPwdTwoActivity.class);
        intent.putExtra(K.PASSWORD, et_password.getText().toString().trim());
        ac.startCallbackActivity(intent);
    }
}
