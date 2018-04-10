package com.cnepay.android.swiper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.core.MvpAppCompatActivity;
import com.cnepay.android.swiper.presenter.CheckMobileMessagePresenter;
import com.cnepay.android.swiper.presenter.SendMobileMessagePresenter;
import com.cnepay.android.swiper.utils.Countdown;
import com.cnepay.android.swiper.utils.K;
import com.cnepay.android.swiper.utils.Logger;
import com.cnepay.android.swiper.utils.Utils;
import com.cnepay.android.swiper.view.CheckMobileMessageView;
import com.cnepay.android.swiper.view.SendMobileMessageView;

/**
 * Created by tangzy on 2017/4/28.
 */

public class ForgotPwdActivity extends MvpAppCompatActivity implements View.OnClickListener,SendMobileMessageView,CheckMobileMessageView{

    private static final String TAG = "RegisterActivity";


    private Button next;
    private Button btn_get_verification_code;
    private Countdown mCountdown;
    private EditText et_phone_number;
    private EditText et_code;

    private String phone;
    private String code;

    private SendMobileMessagePresenter sendPresenter;
    private CheckMobileMessagePresenter checkPresenter;
    private String cookie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uc.setContentViewFrameWithBottomBtn(R.layout.activity_forgot_pwd);
        uc.initStatusBar(R.color.white);
        btn_get_verification_code = (Button) findViewById(R.id.btn_get_verification_code);
        btn_get_verification_code.setOnClickListener(this);
        next = (Button) findViewById(R.id.next);
        next.setOnClickListener(this);
        mCountdown = new Countdown(this, btn_get_verification_code, 60 * 1000, 1000);
        et_phone_number = (EditText) findViewById(R.id.et_phone_number);
        et_phone_number.addTextChangedListener(textWatcher);
        et_code = (EditText) findViewById(R.id.et_code);
        et_code.addTextChangedListener(textWatcher);
        btn_get_verification_code.setEnabled(false);
        next.setEnabled(false);

        sendPresenter=new SendMobileMessagePresenter();
        checkPresenter=new CheckMobileMessagePresenter();

        sendPresenter.attachView(this);
        checkPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sendPresenter.detachView(false);
        checkPresenter.detachView(false);
    }

    @Override
    public void onClick(View view) {
        String mobile=et_phone_number.getText().toString().trim();
        String iscode=et_code.getText().toString().trim();
        switch (view.getId()){
            case R.id.next:
                checkPresenter.checkMobileMessage(mobile,iscode,cookie);
                break;
            case R.id.btn_get_verification_code:
                sendPresenter.sendMobileMessage(mobile,"forget");
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
            phone = et_phone_number.getText().toString().trim();
            if (!TextUtils.isEmpty(phone) && Utils.verifyPhone(ForgotPwdActivity.this, phone)){
                btn_get_verification_code.setEnabled(true);
            }else {
                next.setEnabled(false);
                btn_get_verification_code.setEnabled(false);
                return;
            }
            code = et_code.getText().toString().trim();
            if (!TextUtils.isEmpty(code)){
                next.setEnabled(true);
            }else {
                next.setEnabled(false);
            }
        }
    };


    @Override
    public void onSendMobileMessage(String cookie,String respMsg ) {
        mCountdown.start();//访问成功后  进行倒计时
        this.cookie= cookie;
        uc.toast(respMsg);
    }

    @Override
    public void checkMobileMessage(Object content) {
        //检查验证码回调
        Intent intent=new Intent(this, SetPwdActivity.class);
        intent.putExtra(K.MOBILE,et_phone_number.getText().toString().trim());
        ac.startCallbackActivity(intent);
        et_phone_number.setText("");
        et_code.setText("");
    }


}
