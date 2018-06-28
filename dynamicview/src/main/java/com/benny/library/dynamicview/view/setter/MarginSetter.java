package com.benny.library.dynamicview.view.setter;

import android.view.View;
import android.view.ViewGroup;

import com.benny.library.dynamicview.view.property.MarginProperty;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class MarginSetter {
    public static final String PROPERTY = "margin";
    private static MarginSetter instance;

    public static MarginSetter getInstance() {
        if (instance == null) {
            instance = new MarginSetter();
        }
        return instance;
    }

    public void setMargin(View view, String margin) {
        ViewGroup.MarginLayoutParams lparams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        if (lparams == null) {
            lparams = new ViewGroup.MarginLayoutParams(WRAP_CONTENT, WRAP_CONTENT);
            view.setLayoutParams(lparams);
        }
        MarginProperty property = MarginProperty.of(view.getContext(), margin);
        lparams.setMargins(property.left, property.top, property.right, property.bottom);
    }
}
