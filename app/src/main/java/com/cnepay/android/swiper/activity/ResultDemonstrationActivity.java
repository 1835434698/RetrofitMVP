package com.cnepay.android.swiper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.core.MvpAppCompatActivity;

/**
 * Created by XuShiWei on 2017/5/4.
 */

public class ResultDemonstrationActivity extends MvpAppCompatActivity implements View.OnClickListener {

    private TextView describe;
    private TextView header;
    private Button btn;
    public static final byte TYPE_REGISTER = 1;
    public static final byte TYPE_RETRIEVE_PWD = 2;
    public static final byte TYPE_CHANGE_PWD = 3;
    public static final byte TYPE_ACTIVATE = 4;
    public static final String HEADER = "HEADER";
    private Runnable runnable;
    byte source;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        source = getIntent().getByteExtra(ResultDemonstrationActivity.class.getSimpleName(), (byte) -1);
        switch (source){
            case TYPE_RETRIEVE_PWD:
//                this.requestWindowFeature(Window.FEATURE_NO_TITLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                super.onCreate(savedInstanceState);
                uc.setContentViewPure(R.layout.activity_success);
                uc.initStatusBar(R.color.white);
                uc.setTitle(R.string.mine_modify_password);
                break;
            case TYPE_CHANGE_PWD:
                super.onCreate(savedInstanceState);
                uc.setContentViewWithTitle(R.layout.activity_success);
                uc.initStatusBar(R.color.colorE44036);
                uc.setTitle(R.string.mine_modify_password);
                uc.hideBackIcon();
                break;
            case TYPE_REGISTER:
//                this.requestWindowFeature(Window.FEATURE_NO_TITLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                super.onCreate(savedInstanceState);
                uc.setContentViewPure(R.layout.activity_success);
                uc.initStatusBar(R.color.white);
                break;
        }


        header = (TextView) findViewById(R.id.resultact_tv_header);
        describe = (TextView) findViewById(R.id.resultact_tv_describe);
        btn = (Button) findViewById(R.id.resultact_btn);
        btn.setOnClickListener(this);

        switch (source){
            case TYPE_REGISTER:
                header.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.icon_success, 0,0);
                header.setText(getString(R.string.rst_dmst_act_titletext_register_success));
                describe.setVisibility(View.VISIBLE);
                describe.setText(getString(R.string.rst_dmst_act_desc_register_success));
                runnable = () -> ac.startActivityFromAssignedActivity(new Intent(this, LoginActivity.class),LoginActivity.class,false);
                break;
            case TYPE_RETRIEVE_PWD:
                header.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.icon_success, 0,0);
                header.setText(getString(R.string.rst_dmst_act_titletext_update_pwd_success));
                describe.setVisibility(View.GONE);
                runnable = () -> ac.startActivityFromAssignedActivity(new Intent(this, LoginActivity.class),LoginActivity.class,false);
                break;
            case TYPE_CHANGE_PWD:
                header.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.icon_success, 0,0);
                header.setText(getString(R.string.rst_dmst_act_titletext_set_pwd_success));
                describe.setVisibility(View.GONE);
                runnable = () -> requestSignOff();
                break;
            case TYPE_ACTIVATE:
                header.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.icon_fail, 0,0);
//                header.setText(getString(R.string.rst_dmst_act_titletext_activate_failure));
                header.setText(getIntent().getStringExtra(HEADER));
                describe.setVisibility(View.VISIBLE);
                describe.setText(getString(R.string.rst_dmst_act_desc_fail_reason));
                runnable = () -> ac.startCallbackActivity(new Intent(this,BindDeviceActivity.class));
                break;
            default:
                header.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.icon_success, 0,0);
                header.setText("错误");
                describe.setVisibility(View.VISIBLE);
                describe.setText("错误");
                runnable = null;
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (source == TYPE_CHANGE_PWD){
            uc.hideBackIcon();
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.resultact_btn:
                if (runnable!=null)
                    runnable.run();

                break;
        }
    }
}
