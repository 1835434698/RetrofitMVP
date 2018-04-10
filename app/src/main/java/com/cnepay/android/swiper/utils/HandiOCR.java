package com.cnepay.android.swiper.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.cnepay.android.swiper.core.presenter.ActivityTransformationController;
import com.idcard.TRECAPIImpl;
import com.idcard.TStatus;
import com.idcard.TengineID;
import com.turui.bank.ocr.CaptureActivity;

/**
 * Created by tangzy on 2017/5/19.
 */

public class HandiOCR {

    private static TRECAPIImpl engineDemo = new TRECAPIImpl();
    private Context context;
    private static TStatus tStatus;
    private HandiOCR(Context context){
        String time = engineDemo.TR_GetEngineTimeKey();
        tStatus = engineDemo.TR_StartUP(context,engineDemo.TR_GetEngineTimeKey());
        isOKEngine(context);
    }

    private static void isOKEngine(Context context) {
        if (tStatus == TStatus.TR_TIME_OUT ) {
            Toast.makeText(context, "引擎过期", Toast.LENGTH_SHORT).show();
            return;
        }else  if (tStatus == TStatus.TR_FAIL) {
            Toast.makeText(context, "引擎初始化失败", Toast.LENGTH_SHORT).show();
            return;
        }
    }
    private static HandiOCR instance;

    public static void scanOcr(Context context, ActivityTransformationController ac, TengineID tengineID){
       if (instance == null){
           instance = new HandiOCR(context);
       }else {
           isOKEngine(context);
       }
       //CaptureActivity.isOpenLog = true; //打开Log开关， 待需要时打开
       CaptureActivity.tengineID = tengineID;
       CaptureActivity.ShowCopyRightTxt = "由翰迪提供技术支持";
       Intent intent = new Intent(context, CaptureActivity.class);
       intent.putExtra("engine", engineDemo);
       ac.startActivityForResult(intent,0);
   }



}
