package com.cnepay.android.swiper.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by xugang on 2016/5/27.
 * EditText 关联 Button，全部输入内容button.enabled = true
 */
public class AutoRelationButtonWatcher implements CompoundButton.OnCheckedChangeListener {

    private Map<EditText, View> map = new HashMap<>();

    private Button button;

    private CheckBox checkBox;

    /**
     * 此方法在addGroup()或者setCheckBox()之后调用
     *
     * @param button
     */
    public void setButton(Button button) {
        this.button = button;
        relationButton();
    }

    /**
     * 此方法在addGroup()之后调用
     *
     * @param checkBox
     */
    public AutoRelationButtonWatcher setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
        this.checkBox.setOnCheckedChangeListener(this);
        return this;
    }
    public AutoRelationButtonWatcher addGroup(final EditText editText) {
        return addGroup(editText, null);
    }
    public AutoRelationButtonWatcher addGroup(final EditText editText, View view) {
        editText.addTextChangedListener(new MyWatch(editText));
        if (view!=null){
            view.setVisibility(View.GONE);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editText.setText(null);
                }
            });
            map.put(editText, view);
        }
        return this;
    }

    private void relationButton() {
        boolean enable = true;
        Iterator<Map.Entry<EditText, View>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<EditText, View> entry = iterator.next();
            if (!entry.getValue().isShown()) {
                enable = false;
                break;
            }
        }
        boolean e = checkBox == null ? true : checkBox.isChecked();
        button.setEnabled(enable & e);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        relationButton();
    }


    private class MyWatch implements TextWatcher {

        private EditText editText;

        public MyWatch(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            View view = map.get(editText);
            if (view != null) {
                if (charSequence.length() > 0) {
                    view.setVisibility(View.VISIBLE);
                } else {
                    view.setVisibility(View.GONE);
                }
            }
            relationButton();
        }

        @Override
        public void afterTextChanged(Editable editable) {
//
//            if (editable.length() > 0) {
//                map.get(editText).setVisibility(View.VISIBLE);
//            } else {
//                map.get(editText).setVisibility(View.GONE);
//            }
//
//            relationButton();
        }
    }
}
