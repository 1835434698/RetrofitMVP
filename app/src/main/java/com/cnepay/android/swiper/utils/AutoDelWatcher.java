package com.cnepay.android.swiper.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

/**
 * Created by xugang on 2016/5/27.
 */
public class AutoDelWatcher implements TextWatcher, View.OnClickListener {

    private EditText editText;
    private View vDel;

    public AutoDelWatcher(EditText editText, View vDel) {
        this.editText = editText;
        this.vDel = vDel;
        this.vDel.setOnClickListener(this);
        this.vDel.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        editText.setText(null);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable.length() > 0) {
            vDel.setVisibility(View.VISIBLE);
        } else {
            vDel.setVisibility(View.GONE);
        }
    }
}
