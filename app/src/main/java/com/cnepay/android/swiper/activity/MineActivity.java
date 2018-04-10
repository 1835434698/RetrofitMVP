package com.cnepay.android.swiper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.bean.BaseBean;
import com.cnepay.android.swiper.core.MvpAppCompatActivity;
import com.cnepay.android.swiper.core.view.MvpView;
import com.cnepay.android.swiper.presenter.MinePresenter;
import com.cnepay.android.swiper.widget.BadgeView;

/**
 * Created by wjl on 2017/4/27.
 * 我的页面
 */
public class MineActivity extends MvpAppCompatActivity implements View.OnClickListener,MvpView {

    private static final int TYPE_IDENTIFY_UNCHECKED = 0x000;//未审核
    private static final int TYPE_IDENTIFY_SUCCESS = 0x001;//审核成功
    private static final int TYPE_IDENTIFY_FAIL = 0x002;//审核失败
    private static final int TYPE_IDENTIFY_REVIEW = 0x003;//审核中


    private ImageView mIvHead;//用户头像
    private TextView mTvPhone;//用户手机号
    private  ImageView mIvBack;//返回按键
    //实名认证
    private  ImageView mIvNameIdentify;
    private  LinearLayout mLLNameIdentify;
    private TextView mTvNameIdentify;
    //商户认证
    private  ImageView mIvMerchantIdentify;
    private  LinearLayout mLLMerchantIdentify;
    private TextView mTvMerchantIdentify;
    //银行认证
    private ImageView mIvBankIdentify;
    private LinearLayout mLLBankIdentify;
    private  TextView mTvBankIdentify;
    //签名认证
    private  ImageView mIvSignatureIdentify;
    private  LinearLayout mLLSignatureIdentify;
    private TextView mTvSignatureIdentify;

    private  TextView mTvMineMessage;//我的消息
    private  TextView mTvMineDevice;//我的设备
    private TextView mTvModifyPwd;//修改密码
    private TextView mTvMyProtocol;//协议说明
    private  TextView mTvAbout;//关于
    private TextView mTvFqz;//常见问题

//    ImageView mine_iv_name_identify;
//    LinearLayout mine_ll_name_identify;
//    private TextView mine_tv_fqz;
//    private TextView mine_tv_modify_psd;
//    private TextView mine_tv_my_protocol;
//    private TextView mine_tv_my_about;

    private Button mBtLogout;

    private MinePresenter minePresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
    }


    private void initView() {
        uc.setContentViewPure(R.layout.activity_mine);
        uc.initStatusBar(R.color.white);
        mIvHead = (ImageView) findViewById(R.id.mine_iv_head);
        mTvPhone = (TextView) findViewById(R.id.mine_tv_phone);
        mIvBack = (ImageView) findViewById(R.id.mine_iv_back);
        mLLNameIdentify = (LinearLayout) findViewById(R.id.mine_ll_name_identify);
        mIvNameIdentify = (ImageView) findViewById(R.id.mine_iv_name_identify);
        mTvNameIdentify = (TextView) findViewById(R.id.mine_tv_name_identify);

        mLLMerchantIdentify = (LinearLayout) findViewById(R.id.mine_ll_merchant_identify);
        mIvMerchantIdentify = (ImageView) findViewById(R.id.mine_iv_merchant_identify);
        mTvMerchantIdentify = (TextView) findViewById(R.id.mine_tv_merchant_identify);

        mLLBankIdentify = (LinearLayout) findViewById(R.id.mine_ll_bank_identify);
        mIvBankIdentify = (ImageView) findViewById(R.id.mine_iv_bank_identify);
        mTvBankIdentify = (TextView) findViewById(R.id.mine_tv_bank_identify);

        mLLSignatureIdentify = (LinearLayout) findViewById(R.id.mine_ll_signature_identify);
        mIvSignatureIdentify = (ImageView) findViewById(R.id.mine_iv_signature_identify);
        mTvSignatureIdentify = (TextView) findViewById(R.id.mine_tv_signature_identify);


        mTvMineMessage = (TextView) findViewById(R.id.mine_tv_message);
        mTvMineDevice = (TextView) findViewById(R.id.mine_tv_my_pos);
        mTvModifyPwd = (TextView) findViewById(R.id.mine_tv_modify_psd);
        mTvMyProtocol = (TextView) findViewById(R.id.mine_tv_my_protocol);
        mTvAbout = (TextView) findViewById(R.id.mine_tv_my_about);
        mTvFqz = (TextView) findViewById(R.id.mine_tv_fqz);
        mBtLogout= (Button) findViewById(R.id.login_out);

        minePresenter =new MinePresenter();
        minePresenter.attachView(this);

        initQualification();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        minePresenter.detachView(false);
    }

    private void initListener() {
        mTvMineMessage.setOnClickListener(this);
        mTvMineDevice.setOnClickListener(this);
        mTvModifyPwd.setOnClickListener(this);
        mTvMyProtocol.setOnClickListener(this);
        mTvAbout.setOnClickListener(this);
        mTvFqz.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
        mBtLogout.setOnClickListener(this);
        mLLNameIdentify.setOnClickListener(this);
        mLLBankIdentify.setOnClickListener(this);
    }

    /**
     * 初始化4项认证
     */
    private void initQualification() {
        initNameIdentify(TYPE_IDENTIFY_SUCCESS);
        initBankIdentify(TYPE_IDENTIFY_FAIL);
        initMerchantIdentify(TYPE_IDENTIFY_REVIEW);
        initSignatureIdentify(TYPE_IDENTIFY_UNCHECKED);
    }

    /**
     * 初始化 实名认证
     */
    private void initNameIdentify(int type) {
        switch (type) {
            case TYPE_IDENTIFY_SUCCESS:
                mTvNameIdentify.setText(R.string.mine_activity_identification_success);
                mIvNameIdentify.setImageResource(R.drawable.mine_img_name_sel);
                new BadgeView(this).setTopRightBg(mLLNameIdentify, R.drawable.mine_img_identification_true);
                break;
            case TYPE_IDENTIFY_FAIL:
                mTvNameIdentify.setText(R.string.mine_activity_identification_fail);
                mIvNameIdentify.setImageResource(R.drawable.mine_img_name_nor);
                new BadgeView(this).setTopRightBg(mLLNameIdentify, R.drawable.mine_img_identification_fail);
                break;
            case TYPE_IDENTIFY_REVIEW:
                mIvNameIdentify.setImageResource(R.drawable.mine_img_name_sel);
                mTvNameIdentify.setText(R.string.mine_activity_identification_during);
                break;
            case TYPE_IDENTIFY_UNCHECKED:
                mIvNameIdentify.setImageResource(R.drawable.mine_img_name_nor);
                mTvNameIdentify.setText(R.string.mine_activity_identification_unaudited);
                break;
        }
    }

    /**
     * 初始化 银行认证
     */
    private void initBankIdentify(int type) {
        switch (type) {
            case TYPE_IDENTIFY_SUCCESS:
                mTvBankIdentify.setText(R.string.mine_activity_identification_success);
                mIvBankIdentify.setImageResource(R.drawable.mine_img_bank_sel);
                new BadgeView(this).setTopRightBg(mLLBankIdentify, R.drawable.mine_img_identification_true);
                break;
            case TYPE_IDENTIFY_FAIL:
                mTvBankIdentify.setText(R.string.mine_activity_identification_fail);
                mIvBankIdentify.setImageResource(R.drawable.mine_img_bank_nor);
                new BadgeView(this).setTopRightBg(mLLBankIdentify, R.drawable.mine_img_identification_fail);
                break;
            case TYPE_IDENTIFY_REVIEW:
                mIvBankIdentify.setImageResource(R.drawable.mine_img_bank_sel);
                mTvBankIdentify.setText(R.string.mine_activity_identification_during);
                break;
            case TYPE_IDENTIFY_UNCHECKED:
                mIvBankIdentify.setImageResource(R.drawable.mine_img_bank_nor);
                mTvBankIdentify.setText(R.string.mine_activity_identification_unaudited);
                break;
        }
    }

    /**
     * 初始化 商户认证
     */
    private void initMerchantIdentify(int type) {
        switch (type) {
            case TYPE_IDENTIFY_SUCCESS:
                mTvMerchantIdentify.setText(R.string.mine_activity_identification_success);
                mIvMerchantIdentify.setImageResource(R.drawable.mine_img_merchant_sel);
                new BadgeView(this).setTopRightBg(mLLMerchantIdentify, R.drawable.mine_img_identification_true);
                break;
            case TYPE_IDENTIFY_FAIL:
                mTvMerchantIdentify.setText(R.string.mine_activity_identification_fail);
                mIvMerchantIdentify.setImageResource(R.drawable.mine_img_merchant_nor);
                new BadgeView(this).setTopRightBg(mLLMerchantIdentify, R.drawable.mine_img_identification_fail);
                break;
            case TYPE_IDENTIFY_REVIEW:
                mIvMerchantIdentify.setImageResource(R.drawable.mine_img_merchant_sel);
                mTvMerchantIdentify.setText(R.string.mine_activity_identification_during);
                break;
            case TYPE_IDENTIFY_UNCHECKED:
                mIvMerchantIdentify.setImageResource(R.drawable.mine_img_merchant_nor);
                mTvMerchantIdentify.setText(R.string.mine_activity_identification_unaudited);
                break;
        }
    }

    /**
     * 初始化 签名认证
     */
    private void initSignatureIdentify(int type) {
        switch (type) {
            case TYPE_IDENTIFY_SUCCESS:
                mTvSignatureIdentify.setText(R.string.mine_activity_identification_success);
                mIvSignatureIdentify.setImageResource(R.drawable.mine_img_signature_sel);
                new BadgeView(this).setTopRightBg(mLLSignatureIdentify, R.drawable.mine_img_identification_true);
                break;
            case TYPE_IDENTIFY_FAIL:
                mTvSignatureIdentify.setText(R.string.mine_activity_identification_fail);
                mIvSignatureIdentify.setImageResource(R.drawable.mine_img_signature_nor);
                new BadgeView(this).setTopRightBg(mLLSignatureIdentify, R.drawable.mine_img_identification_fail);
                break;
            case TYPE_IDENTIFY_REVIEW:
                mIvSignatureIdentify.setImageResource(R.drawable.mine_img_signature_sel);
                mTvSignatureIdentify.setText(R.string.mine_activity_identification_during);
                break;
            case TYPE_IDENTIFY_UNCHECKED:
                mIvSignatureIdentify.setImageResource(R.drawable.mine_img_signature_nor);
                mTvSignatureIdentify.setText(R.string.mine_activity_identification_unaudited);
                break;
        }
//        mine_ll_name_identify = (LinearLayout) findViewById(R.id.mine_ll_name_identify);
//        mine_iv_name_identify = (ImageView) findViewById(R.id.mine_iv_name_identify);
        new BadgeView(this).setTopRightBg(mLLNameIdentify, R.drawable.mine_img_identification_fail);
//        mine_tv_fqz = (TextView) findViewById(R.id.mine_tv_fqz);
//        mine_tv_fqz.setOnClickListener(this);
//        mine_tv_modify_psd = (TextView) findViewById(R.id.mine_tv_modify_psd);
//        mine_tv_modify_psd.setOnClickListener(this);
//        mine_tv_my_protocol = (TextView) findViewById(R.id.mine_tv_my_protocol);
//        mine_tv_my_protocol.setOnClickListener(this);
//        mine_tv_my_about = (TextView) findViewById(R.id.mine_tv_my_about);
//        mine_tv_my_about.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_iv_back:
                //退出
                finish();
                break;
            case R.id.mine_tv_message:
                //我的消息
                ac.startCallbackActivity(new Intent(this, MineMessageActivity.class));
                break;
            case R.id.mine_tv_my_pos:
                //我的设备
                break;
            case R.id.mine_tv_modify_psd:
                //修改密码
                ac.startCallbackActivity(new Intent(this, ModifyPwdActivity.class));
                break;
            case R.id.mine_tv_my_protocol:
                //协议说明
                ac.startWebActivity(this, WebViewActivity.DEFAULT_WEB_VIEW, "agreement.html", getString(R.string.mine_protocol_specification));
                break;
            case R.id.mine_tv_my_about:
                //关于
                ac.startCallbackActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.mine_tv_fqz:
                //常见问题
                ac.startWebActivity(this, WebViewActivity.DEFAULT_WEB_VIEW, "faq-zft.html", getString(R.string.mine_fqz));
                break;
            case R.id.login_out:
                // TODO: 2017/5/19 待优化整合
                minePresenter.logout();
                break;
            case R.id.mine_ll_name_identify:
                ac.startCallbackActivity(new Intent(this, CertificationRealNameActivity.class));
                break;
            case R.id.mine_ll_bank_identify:
                ac.startCallbackActivity(new Intent(this, CertificationReplaceSettlementCardActivity.class));
                break;
        }
    }

    @Override
    public void connectSuccess(Object content) {
        super.connectSuccess(content);
        if(content instanceof BaseBean){
            BaseBean bean= (BaseBean) content;
            if(bean.isSuccess()){
                ac.startCallbackActivity(new Intent(MineActivity.this,LoginActivity.class));
            }else{
                uc.toast(bean.getRespMsg());
            }
        }
    }
}
