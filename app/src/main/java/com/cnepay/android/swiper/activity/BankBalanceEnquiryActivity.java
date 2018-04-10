package com.cnepay.android.swiper.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.core.MvpAppCompatActivity;
import com.cnepay.android.swiper.utils.Logger;
import com.cnepay.android.swiper.widget.PasswordInputView;
import com.cnepay.android.swiper.widget.XNumberKeyboardView;

import android.support.v7.app.AlertDialog;

/**
 * Created by wjl on 2017/5/8.
 * 银行卡余额---查询余额(输入密码) 或者刷卡交易页面
 */

public class BankBalanceEnquiryActivity extends MvpAppCompatActivity implements XNumberKeyboardView.IOnKeyboardListener, View.OnClickListener {
    private static final String TAG = "BankBalanceEnquiryActiv";
    public static final int TYPE_BALANCE_INQUIRY = 0x000;//查询余额页面
    public static final int TYPE_CREDIT_CARD_TRANSACTIONS = 0x001;//刷卡交易页面


    private XNumberKeyboardView keyboardView;
    private PasswordInputView ed_psd;
    private Button btn_search;
    private int type = TYPE_CREDIT_CARD_TRANSACTIONS;//跳转到该页面携带type  0=查询余额页面 1=刷卡交易页面

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        type = getIntent().getIntExtra("type", 0);

        if (type == TYPE_BALANCE_INQUIRY) {
            uc.setContentViewWithTitle(R.layout.activity_bank_balance_input_psd);
            uc.setTitle(R.string.bank_bankcard_title);
        } else if (type == TYPE_CREDIT_CARD_TRANSACTIONS) {
            uc.setContentViewWithTitle(R.layout.activity_credit_card_transactions);
            uc.setTitle(R.string.transactions_title);
        }

        btn_search = (Button) findViewById(R.id.bank_balance_btn_search);
        if (type == TYPE_BALANCE_INQUIRY) {
            btn_search.setText(R.string.input_bankcard_btn);
        } else if (type == TYPE_CREDIT_CARD_TRANSACTIONS) {
            btn_search.setText(R.string.button_true);
        }

        btn_search.setOnClickListener(this);
        keyboardView = (XNumberKeyboardView) findViewById(R.id.view_keyboard);
        ed_psd = (PasswordInputView) findViewById(R.id.trader_pwd_set_pwd_edittext);
        ed_psd.setInputType(InputType.TYPE_NULL); // 屏蔽系统软键盘
        keyboardView.setIOnKeyboardListener(this);
        ed_psd.setOnClickListener(this);
        ed_psd.addTextChangedListener(textWatcher);
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
        }

        @Override
        public void afterTextChanged(Editable s) {
           if(ed_psd.getText().toString().trim().length()==6){
               btn_search.setEnabled(true);
           }else{
               btn_search.setEnabled(false);
           }
        }
    };
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                keyboardView.hideKeyboard();
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 监听Back键按下事件
     */
    @Override
    public void onBackPressed() {
        if (keyboardView.getKeyboardVisible() == View.VISIBLE) {
            keyboardView.hideKeyboard();
        } else {
            finish();
        }
    }

    @Override
    public void onInsertKeyEvent(String text) {
        ed_psd.append(text);
    }

    @Override
    public void onDeleteKeyEvent() {
        int start = ed_psd.length() - 1;
        if (start >= 0) {
            ed_psd.getText().delete(start, start + 1);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.trader_pwd_set_pwd_edittext:
                //弹出软键盘
                if (keyboardView.getVisibility() != View.VISIBLE) {
                    keyboardView.shuffleKeyboard();// 随机键盘
                    keyboardView.showKeyboard();
                }
                break;
            case R.id.bank_balance_btn_search:
                if (type == TYPE_BALANCE_INQUIRY) {
                    //余额查询界面的按钮---查询余额
                    Logger.i(TAG, "查询余额");
                } else if (type == TYPE_CREDIT_CARD_TRANSACTIONS) {
                    //刷卡交易界面的按钮----确定
                    Logger.i(TAG, "刷卡交易");

                }
                showDialog(500 + "");//测试dialog效果，开发时自行删除
                break;
        }
    }


    private void showDialog(String money) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.prompt);
        builder.setMessage(getResources().getString(R.string.input_bankcard_dialog_message) + money + "元");
        builder.setNegativeButton("返回首页", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("换一张卡", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


}
