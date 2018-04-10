package com.cnepay.android.swiper.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.bean.RealNameAuthBean;
import com.cnepay.android.swiper.presenter.CertificationRealNamePresenter;
import com.cnepay.android.swiper.utils.GlideRoundCornerTransformation;
import com.cnepay.android.swiper.view.CertificationRealNameView;
import com.cnepay.android.swiper.utils.HandiOCR;
import com.cnepay.android.swiper.utils.ImageUtils;
import com.cnepay.android.swiper.utils.Logger;
import com.idcard.CardInfo;
import com.idcard.TFieldID;
import com.idcard.TengineID;
import com.turui.bank.ocr.CaptureActivity;

import java.io.File;

/**
 * 资质认证页面-实名认证
 */
public class CertificationRealNameActivity extends CertificationBaseActivity implements View.OnClickListener, CertificationRealNameView {
    private static final String TAG = "CertificationRealNameActivity";
    private ImageView imgCertificationRealName;
    private ImageView imgIdCardPositive;
    private ImageView imgIdCardNegative;
    private ImageView imgHandIdCard;
    private EditText etCertificationRealName;
    private EditText etCertificationIDCard;
    private CertificationRealNamePresenter mRealNamePresenter;
    // TODO: 2017/5/19 修改地址
    private String path = Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera/ID.jpg";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uc.setContentViewFrameWithBottomBtn(R.layout.activity_certification_real_name);
        uc.setTitle("资质认证");
        imgCertificationRealName = (ImageView) findViewById(R.id.imgCertificationRealName);
        imgCertificationRealName.setImageResource(R.drawable.certification_img_own);
        uc.getRightText().setText("下一步");
        imgIdCardPositive = (ImageView) findViewById(R.id.imgIdCardPositive);
        imgIdCardNegative = (ImageView) findViewById(R.id.imgIdCardNegative);
        imgHandIdCard = (ImageView) findViewById(R.id.imgHandIdCard);
        etCertificationRealName = (EditText) findViewById(R.id.etCertificationRealName);
        etCertificationIDCard = (EditText) findViewById(R.id.etCertificationIDCard);
        findViewById(R.id.tvScan).setOnClickListener(this);
        findViewById(R.id.imgIdCardPositive).setOnClickListener(this);
        findViewById(R.id.imgIdCardNegative).setOnClickListener(this);
        findViewById(R.id.imgHandIdCard).setOnClickListener(this);
        uc.getRightText().setOnClickListener(v -> mRealNamePresenter.uploadRealNameInfo(etCertificationRealName.getText().toString().trim(), etCertificationIDCard.getText().toString().trim()));
        mRealNamePresenter = new CertificationRealNamePresenter();
        mRealNamePresenter.attachView(this);

        mRealNamePresenter.downRealNameInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealNamePresenter.detachView(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvScan:
                mRealNamePresenter.scanOcr();
                //翰迪
                Logger.d(TAG, "mine_ll_name_identify");
                HandiOCR.scanOcr(this, ac, TengineID.TIDCARD2);
                break;
            case R.id.imgHandIdCard:
                TAKE_PHOTO = 1001;
                showPhotoPopupWindows();
                break;
            case R.id.imgIdCardNegative:
                TAKE_PHOTO = 1002;
                showPhotoPopupWindows();
                break;
            case R.id.imgIdCardPositive:
                TAKE_PHOTO = 1003;
                showPhotoPopupWindows();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            Uri uri = data.getData();
            mRealNamePresenter.addPicPath(requestCode, uri);
            switch (requestCode) {
                case 1001:
                    Glide.with(obtainContext()).load(uri).transform(new GlideRoundCornerTransformation(this)).into(imgHandIdCard);
                    break;
                case 1002:
                    Glide.with(obtainContext()).load(uri).transform(new GlideRoundCornerTransformation(this)).into(imgIdCardNegative);
                    break;
                case 1003:
                    Glide.with(obtainContext()).load(uri).transform(new GlideRoundCornerTransformation(this)).into(imgIdCardPositive);
                    break;
            }
        } else if (resultCode == CaptureActivity.RESULT_SCAN_IDCAD_OK) {
            if (data == null) {
                return;
            }
            CardInfo cardInfo = (CardInfo) data.getSerializableExtra("cardinfo");
            ImageUtils.compress2File(CaptureActivity.TakeBitmap, path);
            File file = new File(path);
            if (file.exists()) {
                Glide.with(obtainContext()).load(file).transform(new GlideRoundCornerTransformation(this)).into(imgIdCardPositive);
            }
            etCertificationRealName.setText(cardInfo.getFieldString(TFieldID.NAME));
            etCertificationIDCard.setText(cardInfo.getFieldString(TFieldID.NUM));
            Logger.d(TAG, "身份证扫描结果" + cardInfo.getAllinfo());
        } else if (resultCode == CaptureActivity.RESULT_SCAN_CANCLE) {
            Logger.d(TAG, "扫描点点击返回");
        }
    }

    @Override
    public void onUploadComplete(String respMsg) {
        uc.toast(respMsg);
        ac.startCallbackActivity(new Intent(obtainContext(), CertificationMerchantActivity.class));
    }

    @Override
    public void onEchoComplete(RealNameAuthBean bean) {

    }

    @Override
    public void onOcrRevert() {

    }
}
