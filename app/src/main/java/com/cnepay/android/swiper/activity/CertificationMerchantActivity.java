package com.cnepay.android.swiper.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.bean.BaseBean;
import com.cnepay.android.swiper.presenter.CertificationMerchantPresenter;
import com.cnepay.android.swiper.utils.GlideRoundCornerTransformation;
import com.cnepay.android.swiper.view.CertificationMerchantView;

/**
 * 资质认证页面-商户认证
 */
public class CertificationMerchantActivity extends CertificationBaseActivity implements View.OnClickListener, CertificationMerchantView {
    private ImageView imgCertificationMerchant;
    private ImageView imgCertificationBusinessLicense;
    private EditText etCertificationMerchantName;
    private EditText etCertificationBusinessLicense;
    private EditText etCertificationBusinessAddress;
    private CertificationMerchantPresenter merchantPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uc.setContentViewFrameWithBottomBtn(R.layout.activity_certification_merchant);
        uc.setTitle("资质认证");
        imgCertificationMerchant = (ImageView) findViewById(R.id.imgCertificationMerchant);
        imgCertificationBusinessLicense = (ImageView) findViewById(R.id.imgCertificationBusinessLicense);
        etCertificationMerchantName = (EditText) findViewById(R.id.etCertificationMerchantName);
        etCertificationBusinessLicense = (EditText) findViewById(R.id.etCertificationBusinessLicense);
        etCertificationBusinessAddress = (EditText) findViewById(R.id.etCertificationBusinessAddress);
        imgCertificationBusinessLicense.setOnClickListener(this);
        findViewById(R.id.tvCertificationSkip).setOnClickListener(this);
        imgCertificationMerchant.setImageResource(R.drawable.certification_img_own);
        uc.getRightText().setText("下一步");
        uc.getRightText().setOnClickListener(v -> merchantPresenter.uploadMerchantInfo(etCertificationMerchantName.getText().toString().trim(), etCertificationBusinessAddress.getText().toString().trim(), etCertificationBusinessLicense.getText().toString().trim(), "1"));

        merchantPresenter = new CertificationMerchantPresenter();
        merchantPresenter.attachView(this);

        merchantPresenter.downMerchant();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        merchantPresenter.detachView(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgCertificationBusinessLicense:
                showPhotoPopupWindows();
                break;
            case R.id.tvCertificationSkip:
                ac.startCallbackActivity(new Intent(obtainContext(), CertificationAccountActivity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            Uri uri = data.getData();
            merchantPresenter.addPicPath(uri);
            switch (requestCode) {
                case 1000:
                    Glide.with(obtainContext()).load(uri).transform(new GlideRoundCornerTransformation(this)).into(imgCertificationBusinessLicense);
                    break;
            }
        }
    }

    @Override
    public void onUploadComplete(String respMsg) {
        ac.startCallbackActivity(new Intent(obtainContext(), CertificationAccountActivity.class));
        uc.toast(respMsg);
    }

    @Override
    public void onEchoComplete(BaseBean bean) {

    }
}
