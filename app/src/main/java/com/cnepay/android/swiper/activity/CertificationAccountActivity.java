package com.cnepay.android.swiper.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.bean.AccountAuthBean;
import com.cnepay.android.swiper.bean.BankQueryBean;
import com.cnepay.android.swiper.bean.BaseBean;
import com.cnepay.android.swiper.presenter.CertificationAccountPresenter;
import com.cnepay.android.swiper.utils.Countdown;
import com.cnepay.android.swiper.utils.GlideRoundCornerTransformation;
import com.cnepay.android.swiper.view.CertificationAccountView;

/**
 * 资质认证页面-商户认证
 */
public class CertificationAccountActivity extends CertificationBaseActivity implements View.OnClickListener, CertificationAccountView {
    private ImageView imgCertificationAccount;
    private ImageView imgCertificationBankCard;
    private TextView tvCertificationRealName;
    private TextView tvCertificationIDCard;
    private TextView tvVCode;
    private EditText etCertificationSettlementCard;
    private TextView tvCertificationBankAccount;
    private EditText etCertificationPhoneNum;
    private EditText etCertificationVerificationCode;

    private CertificationAccountPresenter mAccountPresenter;
    private Countdown mCountdown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uc.setContentViewFrameWithBottomBtn(R.layout.activity_certification_account);
        uc.setTitle("资质认证");
        imgCertificationAccount = (ImageView) findViewById(R.id.imgCertificationAccount);
        tvVCode = (TextView) findViewById(R.id.tvVCode);
        imgCertificationBankCard = (ImageView) findViewById(R.id.imgCertificationBankCard);
        tvCertificationRealName = (TextView) findViewById(R.id.tvCertificationRealName);
        tvCertificationIDCard = (TextView) findViewById(R.id.tvCertificationIDCard);
        tvCertificationBankAccount = (TextView) findViewById(R.id.tvCertificationBankAccount);
        etCertificationSettlementCard = (EditText) findViewById(R.id.etCertificationSettlementCard);
        etCertificationPhoneNum = (EditText) findViewById(R.id.etCertificationPhoneNum);
        etCertificationVerificationCode = (EditText) findViewById(R.id.etCertificationVerificationCode);
        imgCertificationAccount.setImageResource(R.drawable.certification_img_own);
        imgCertificationBankCard.setOnClickListener(this);
        tvVCode.setOnClickListener(this);
        tvCertificationBankAccount.setOnClickListener(this);
        uc.getRightText().setText("下一步");
        uc.getRightText().setOnClickListener(v -> mAccountPresenter.uploadAccountInfo(etCertificationSettlementCard.getText().toString().trim(), etCertificationPhoneNum.getText().toString().trim(), etCertificationVerificationCode.getText().toString().trim()));

        mCountdown = new Countdown(this, tvVCode, 60 * 1000, 1000);
        mAccountPresenter = new CertificationAccountPresenter();
        mAccountPresenter.attachView(this);
        mAccountPresenter.initData();
        mAccountPresenter.downAccount();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAccountPresenter.detachView(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgCertificationBankCard:
                showPhotoPopupWindows();
                break;
            case R.id.tvCertificationBankAccount:
                ac.startActivityForResult(new Intent(obtainContext(), CertificationChooseBankActivity.class), 100);
                break;
            case R.id.tvVCode:
                mCountdown.start();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case 1000:
                    Uri uri = data.getData();
                    mAccountPresenter.addPicPath(uri);
                    Glide.with(obtainContext()).load(uri).transform(new GlideRoundCornerTransformation(this)).into(imgCertificationBankCard);
                    break;
                case 100:
                    BankQueryBean.Bank bank = (BankQueryBean.Bank) data.getSerializableExtra(CertificationChooseBankActivity.EXTRA_DATA);
                    tvCertificationBankAccount.setText(bank.getBankDeposit());
                    mAccountPresenter.setBankInfo(bank);
                    break;
            }
        }
    }

    @Override
    public void onOcrRevert() {

    }

    @Override
    public void setAuthInfo(String name, String identityCard) {
        tvCertificationRealName.setText(name);
        tvCertificationIDCard.setText(identityCard);
    }

    @Override
    public void onSendVCode(BaseBean baseBean) {

    }

    @Override
    public void onUploadComplete(String respMsg) {
        ac.startCallbackActivity(new Intent(obtainContext(), CertificationSignatureConfirmationActivity.class));
    }

    @Override
    public void onEchoComplete(AccountAuthBean bean) {

    }
}
