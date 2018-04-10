package com.cnepay.android.swiper.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;

import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.core.MvpAppCompatActivity;

/**
 * Created by wjl on 2017/5/4.
 * 资质认证-确认签名页面
 */

public class CertificationSignatureConfirmationActivity extends MvpAppCompatActivity implements View.OnClickListener{
    private ImageView imgConfirmAgreement;
    private ImageView ivDraw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uc.setContentViewWithTitle(R.layout.activity_certification_signature_confirmation);
        uc.setTitle("资质认证");
        imgConfirmAgreement = (ImageView) findViewById(R.id.imgConfirmAgreement);
        imgConfirmAgreement.setImageResource(R.drawable.certification_img_own);
        ivDraw= (ImageView) findViewById(R.id.signature_iv_draw);
        ivDraw.setOnClickListener(this);
        uc.getRightText().setText("完成");
        uc.getRightText().setOnClickListener(v -> new AlertDialog.Builder(obtainContext()).setTitle("提交成功").setMessage("您的认证信息提交成功啦!").setPositiveButton("查看我的认证", (dialog, which) -> ac.startActivityFromAssignedActivity(new Intent(obtainContext(), MineActivity.class), HomePageActivity.class)).show());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signature_iv_draw:
                Intent intent=new Intent(CertificationSignatureConfirmationActivity.this,CertificationSignatureActivity.class);
                ac.startCallbackActivity(intent);
                break;
        }
    }
}
