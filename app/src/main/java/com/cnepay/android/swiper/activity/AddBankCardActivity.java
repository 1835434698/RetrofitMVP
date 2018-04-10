package com.cnepay.android.swiper.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.bean.BaseBean;
import com.cnepay.android.swiper.bean.CardMangeBean;
import com.cnepay.android.swiper.core.MvpAppCompatActivity;
import com.cnepay.android.swiper.core.utils.KeyBoardUtils;
import com.cnepay.android.swiper.presenter.BankCardEditPresenter;
import com.cnepay.android.swiper.presenter.SendCustomerMessagePresenter;
import com.cnepay.android.swiper.utils.AutoRelationButtonWatcher;
import com.cnepay.android.swiper.model.Dictionary;
import com.cnepay.android.swiper.utils.RegularUtils;
import com.cnepay.android.swiper.view.CardManageView;
import com.cnepay.android.swiper.view.SendCustomerMessageView;

/**
 * created by millerJK on time : 2017/5/17
 * description : 添加信用卡
 */
public class AddBankCardActivity extends MvpAppCompatActivity
        implements View.OnClickListener
        , SendCustomerMessageView
        , CardManageView {

    public static final String TAG = "AddBankCardActivity";
    public static final int BACK_HOME = 100;
    public static final int BACK_BANK_LIST = 101;
    private TextView mNameTv, mIdTv;
    private EditText mCardNumEdt, mPhoneNumEdt, mVerifyEdt;
    private Button mVerifyCountBt;
    private CountDownTimer countDownTimer;
    private AutoRelationButtonWatcher mVerifyWatcher;
    private SendCustomerMessagePresenter mMessagePresenter;
    private BankCardEditPresenter mCardPresenter;
    private String phoneNum, cardId, verifyCode;
    private boolean isCountingNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uc.setContentViewFrameWithBottomBtn(R.layout.activity_add_bank_card);
        uc.setTitle("添加信用卡");
        uc.getRightText().setText("提交");
        initView();
        initData();
        initListener();
    }

    private void initView() {
        mNameTv = (TextView) findViewById(R.id.add_bank_name);
        mIdTv = (TextView) findViewById(R.id.add_bank_id);
        mCardNumEdt = (EditText) findViewById(R.id.add_bank_num);
        mPhoneNumEdt = (EditText) findViewById(R.id.add_bank_phone);
        mVerifyEdt = (EditText) findViewById(R.id.add_bank_msg);
        mVerifyCountBt = (Button) findViewById(R.id.add_bank_bt);
        mCardPresenter = new BankCardEditPresenter();
        mCardPresenter.attachView(this);
        mMessagePresenter = new SendCustomerMessagePresenter();
        mMessagePresenter.attachView(this);
        mVerifyWatcher = new AutoRelationButtonWatcher();
        mVerifyWatcher.addGroup(mCardNumEdt).addGroup(mPhoneNumEdt)
                .addGroup(mVerifyEdt).setButton(mVerifyCountBt);
    }

    private void initData() {
        mNameTv.setText(Dictionary.getLoginBean().getLoginName());
        //// FIXME: 2017/5/17 Sign 签到需要将id 写到session中
        mIdTv.setText(Dictionary.getLoginBean().getIdentityNumber());

        mIdTv.setText("1111111111111111");
    }

    private void initListener() {
        mVerifyCountBt.setOnClickListener(this);
        uc.getRightText().setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_bank_bt:
                obtainVerify();
                break;
            case R.id.tvRight:
                submitAuth();
                break;
        }
    }


    private void obtainVerify() {
        if (!verifyElements(true)) {
            return;
        }
        initVerify();
        mMessagePresenter.sendMobileMessage(phoneNum, cardId);
    }

    private void submitAuth() {
        if (!verifyElements(false)) {
            return;
        }
        cancelTimerIfNeed();
        mCardPresenter.cardManage("false", cardId, phoneNum, verifyCode, null);
    }

    private boolean verifyElements(boolean isObtainVerify) {

        phoneNum = mPhoneNumEdt.getText().toString().trim();
        cardId = mCardNumEdt.getText().toString().trim();
        verifyCode = mVerifyEdt.getText().toString().trim();

        if (TextUtils.isEmpty(phoneNum)
                || TextUtils.isEmpty(cardId)) {
            uc.toast(R.string.input_msg_not_complete);
            return false;
        }

        if (!isObtainVerify) {
            if (TextUtils.isEmpty(verifyCode)) {
                uc.toast(R.string.input_msg_not_complete);
                return false;
            }

            if (verifyCode.length() != 4) {
                uc.toast(R.string.verify_msg_wrong);
                return false;
            }
        }

        if (!cardId.matches(RegularUtils.BANK_CARD_NO)) {
            uc.toast(R.string.match_fail_bank_card_no);
            return false;
        }
        if (!phoneNum.matches(RegularUtils.PHONE)) {
            uc.toast(R.string.match_fail_phone_num);
            return false;
        }
        return true;
    }

    private void cancelTimerIfNeed() {
        if (isCountingNow) {
            countDownTimer.cancel();
            isCountingNow = false;
            updateVerifyTv(true, R.string.register_btn_hint);
        }
    }

    private void initVerify() {

        mVerifyCountBt.setEnabled(false);
        KeyBoardUtils.openInputMethod(mVerifyEdt);

        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                isCountingNow = true;
                mVerifyCountBt.setText(String.valueOf(millisUntilFinished / 1000 + "秒后重试"));
            }

            @Override
            public void onFinish() {
                isCountingNow = false;
                updateVerifyTv(true, R.string.reobtain_verify_msg_hint);
            }
        };
    }

    public void updateVerifyTv(boolean enabled, int msgId) {
        mVerifyCountBt.setEnabled(enabled);
        mVerifyCountBt.setText(msgId);
    }

    @Override
    public void sendMessageSuccess(BaseBean value) {
        uc.toast(value.getRespMsg());
        updateVerifyTv(false, R.string.after_60_sec_reconnect);
        countDownTimer.start();//按钮倒计时开始
    }

    @Override
    public void sendMessageError() {
        updateVerifyTv(true, R.string.reobtain_verify_msg_hint);
    }

    @Override
    public void success(CardMangeBean bean) {
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle("提示")
                .setMessage("信用卡信息提交成功")
                .setNegativeButton("返回首页", (dialog1, which) -> {
                    setResult(BACK_HOME);
                    onBackPressed();
                })
                .setPositiveButton("管理银行卡", (dialog12, which) -> {
                    setResult(BACK_BANK_LIST);
                    onBackPressed();
                }).show();
    }

    @Override
    protected void onDestroy() {
        if (mCardPresenter != null)
            mCardPresenter.detachView(false);
        if (mMessagePresenter != null)
            mMessagePresenter.detachView(false);
        super.onDestroy();
    }

}
