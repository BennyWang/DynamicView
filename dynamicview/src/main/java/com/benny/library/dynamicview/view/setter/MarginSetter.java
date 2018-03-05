package com.benny.library.dynamicview.view.setter;

import android.view.View;
import android.view.ViewGroup;

import com.benny.library.dynamicview.view.property.MarginProperty;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class MarginSetter {
    public static final String PROPERTY = "margin";

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
