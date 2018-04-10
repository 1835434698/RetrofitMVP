package com.cnepay.android.swiper.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.view.Gravity;

import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.core.MvpAppCompatActivity;
import com.cnepay.android.swiper.view.CertificationTakePhoto;
import com.cnepay.android.swiper.widget.CommonPopupWindow;

/**
 * Created by xugang on 2017/5/16.
 */

public class CertificationBaseActivity extends MvpAppCompatActivity implements CertificationTakePhoto {
    protected int TAKE_PHOTO = 1000;

    @Override
    public void requestNecessaryPermission() {
        if (declarationPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, 1000)) {
            takePhoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000:
            case 1001:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();
                } else {
                    if (1000 == requestCode) {
                        uc.toast("请在设置中授权SD读写权限");
                    } else if (1001 == requestCode) {
                        uc.toast("未获取摄像机权限");
                    }
                }
                break;
        }
    }

    @Override
    public void showPhotoPopupWindows() {
        CommonPopupWindow commonPopupWindow = new CommonPopupWindow(this, R.layout.mul_layout_take_photo);
        commonPopupWindow.setDismissListener(() -> commonPopupWindow.setWindowAlpha(obtainActivity(), 1.0f))
                .setAnimStyle(R.style.style_pop_anim)
                .setWindowAlpha(obtainActivity(), 0.7f)
                .setOnClickListener(R.id.tvCancel, v1 -> commonPopupWindow.dismiss())
                .setOnClickListener(R.id.tvTakePhoto, v -> {
                    requestNecessaryPermission();
                    commonPopupWindow.dismiss();
                })
                .showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void takePhoto() {
        if (declarationPermissions(Manifest.permission.CAMERA, 1001)) {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            ac.startActivityForResult(intent, TAKE_PHOTO);
        }
    }
}
