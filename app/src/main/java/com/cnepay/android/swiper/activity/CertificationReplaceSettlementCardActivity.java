package com.cnepay.android.swiper.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.core.MvpAppCompatActivity;
import com.cnepay.android.swiper.utils.GlideRoundCornerTransformation;
import com.cnepay.android.swiper.utils.HandiOCR;
import com.cnepay.android.swiper.utils.ImageUtils;
import com.cnepay.android.swiper.utils.Logger;
import com.idcard.CardInfo;
import com.idcard.TFieldID;
import com.idcard.TRECAPIImpl;
import com.idcard.TStatus;
import com.idcard.TengineID;
import com.turui.bank.ocr.CaptureActivity;

import java.io.File;

/**
 * 资质认证-更换结算卡
 */
public class CertificationReplaceSettlementCardActivity extends MvpAppCompatActivity implements View.OnClickListener {

    private static final String TAG = "CertificationReplaceSettlementCardActivity";
    private ImageButton imgScan;
    private EditText etCardNO;
    private ImageView imgCertificationBusinessLicense;
    // TODO: 2017/5/19 修改地址
    private String path =  Environment.getExternalStorageDirectory().toString()+"/DCIM/Camera/Card.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uc.setContentViewFrameWithBottomBtn(R.layout.activity_certification_replace_settlement_card);
        uc.setTitle("更换结算卡");
        etCardNO = (EditText) findViewById(R.id.etCardNO);
        imgCertificationBusinessLicense = (ImageView) findViewById(R.id.imgCertificationBusinessLicense);
        imgScan = (ImageButton) findViewById(R.id.imgScan);
        imgScan.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgScan:
                //翰迪
                Logger.d(TAG, "mine_ll_name_identify");
                HandiOCR.scanOcr(this, ac, TengineID.TIDBANK);
                break;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == CaptureActivity.RESULT_SCAN_BANK_OK) {
            if (data == null) {
                return;
            }
            CardInfo cardInfo = (CardInfo) data.getSerializableExtra("cardinfo");
            if (CaptureActivity.tengineID == TengineID.TIDBANK) {
                ImageUtils.compress2File(CaptureActivity.TakeBitmap, path);
                File file = new File(path);
                if (file.exists()){
                    Glide.with(obtainContext()).load(file).transform(new GlideRoundCornerTransformation(this)).into(imgCertificationBusinessLicense);
                }
            }
            etCardNO.setText(cardInfo.getFieldString(TFieldID.TBANK_NUM));
            Logger.d(TAG, "银行卡扫描结果\n" +cardInfo.getAllinfo());
        }else if (resultCode == CaptureActivity.RESULT_SCAN_CANCLE) {
            Logger.d(TAG, "扫描点点击返回");
        }
    }
}
