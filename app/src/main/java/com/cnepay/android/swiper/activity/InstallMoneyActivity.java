package com.cnepay.android.swiper.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.cnepay.android.swiper.R;
import com.cnepay.android.swiper.core.MvpAppCompatActivity;
import com.cnepay.android.swiper.utils.Logger;
import com.cnepay.android.swiper.utils.Utils;
import com.cnepay.android.swiper.widget.XNumberKeyboardView;

/**
 * Created by wjl on 2017/5/5.
 * 输入金额
 */

public class InstallMoneyActivity extends MvpAppCompatActivity implements XNumberKeyboardView.IOnKeyboardListener, View.OnClickListener {
    private static final String TAG = "InstallMoneyActivity";
    private Button bt_ok;
    private XNumberKeyboardView keyboardView;
    private EditText et_money;
    private String money;//输入的金额

    private int mWidth;
    private PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uc.setContentViewWithTitle(R.layout.activity_install_money);
        uc.setTitle(R.string.install_money_title);
        bt_ok = (Button) findViewById(R.id.install_btn_ok);
        bt_ok.setOnClickListener(this);
        keyboardView = (XNumberKeyboardView) findViewById(R.id.install_money_view_keyboard);
        // 设置软键盘按键的布局---改变样式。
        Keyboard keyboard = new Keyboard(this, R.xml.install_money_keyboard_number);
        keyboardView.setKeyboard(keyboard);

        et_money = (EditText) findViewById(R.id.install_money_et_money);
        et_money.addTextChangedListener(textWatcher);
        et_money.setInputType(InputType.TYPE_NULL); // 屏蔽系统软键盘
        keyboardView.setIOnKeyboardListener(this);
        //测试弹窗//测试dialog效果，开发时自行删除
        showDialog();
    }

    /**
     * 弹出自定义的popWindow
     */
    public void showPop(View parent) {
        if (null == mPopupWindow) {
            //初始化PopupWindow的布局
            View popView = getLayoutInflater().inflate(R.layout.layout_popwindow_install_money, null);
            WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics metrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(metrics);
            mWidth = metrics.widthPixels;
            //popView即popupWindow的布局，ture设置focusAble.
            mPopupWindow = new PopupWindow(popView,
                    mWidth,
                    ViewGroup.LayoutParams.WRAP_CONTENT, true);
            //必须设置BackgroundDrawable后setOutsideTouchable(true)才会有效
            mPopupWindow.setBackgroundDrawable(new ColorDrawable());
            //点击外部关闭。
            mPopupWindow.setOutsideTouchable(true);
            //设置一个动画。
            mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
            //设置item的点击监听
            popView.findViewById(R.id.btn_t1).setOnClickListener(this);
            popView.findViewById(R.id.btn_d0).setOnClickListener(this);
            popView.findViewById(R.id.btn_cancel).setOnClickListener(this);

            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    //pop销毁，让屏幕亮起来
                    lighton();
                }
            });

            mPopupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            lightoff();
        } else {
            mPopupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            lightoff();
        }

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
            money=et_money.getText().toString().trim();
            if(TextUtils.isEmpty(money)){
                bt_ok.setEnabled(false);
            }else{
                if(getInputMoney(money).equals("000")){
                    bt_ok.setEnabled(false);
                }else{
                    bt_ok.setEnabled(true);
                }
            }
        }
    };

    private void showDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.prompt);
        builder.setMessage(R.string.install_dialog_title);
        builder.setNegativeButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("好", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.install_btn_ok:
                //测试弹出pop
//                Logger.i(TAG, "弹出pop");
//                showPop(v);
                uc.toast(getInputMoney(et_money.getText().toString()));
                break;
            case R.id.btn_t1:
                //T+1
                break;
            case R.id.btn_d0:
                //D0及时付
                break;
            case R.id.btn_cancel:
                //取消
                mPopupWindow.dismiss();
                break;
        }
    }

    @Override
    public void onInsertKeyEvent(String text) {
        money = et_money.getText().toString();
        if (money == null || money.equals("")) {
            if (text.equals(".")) {
                et_money.append("0" + text);
            } else {
                et_money.append(text);
            }
        } else {
            //输入长度小于17位
            //防止无效0
            if (!money.equals("0")) {
                if (money.contains(".")) {
                    if (money.length() < 13) {
                        String[] strs = money.split("\\.");
                        if (strs[0].length() == money.length() - 1) {
                            if (!text.equals(".")) {
                                et_money.append(text);
                            }
                        } else {
                            //已输入的包含小数点
                            if (!text.equals(".") && strs[1].length() < 2) {
                                //已输入有小数点的，只能在输入2位
                                et_money.append(text);
                            }
                        }
                    }
                } else {
                    if (money.length() < 10) {
                        //已输入的不包含小数点
                        et_money.append(text);
                    } else if (money.length() == 10) {
                        if (text.equals(".")) {
                            et_money.append(text);
                        } else {
                            uc.toast("输入位数最大了");
                        }
                    } else {
                        uc.toast("输入位数最大了");
                    }
                }
            } else {
                if (!text.equals("0")) {
                    if (text.equals(".")) {
                        et_money.append(text);
                    } else {
                        et_money.setText(text);
                    }

                }

            }

        }

    }


    @Override
    public void onDeleteKeyEvent() {
        int start = et_money.length() - 1;
        if (start >= 0) {
            et_money.getText().delete(start, start + 1);
        }
    }

    /**
     * 拿到用户输入的金额--处理过
     *
     * @param money
     * @return
     */
    private String getInputMoney(String money) {
        if (money.contains(".")) {
            String[] strs = money.split("\\.");
            if (money.endsWith(".")) {
                return strs[0] + "00";
            } else if (strs[1].length() == 1) {
                return strs[0] + strs[1] + "0";
            } else if (strs[1].length() == 2) {
                return strs[0] + strs[1];
            } else {
                return null;
            }
        } else {
            return money + "00";
        }
    }

    private void lightoff() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.3f;
        getWindow().setAttributes(lp);
    }

    //PopupWindow消失时，使屏幕恢复正常
    private void lighton() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1.0f;
        getWindow().setAttributes(lp);
    }
}
