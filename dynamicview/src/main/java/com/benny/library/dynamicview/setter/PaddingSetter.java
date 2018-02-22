package com.benny.library.dynamicview.setter;

import android.view.View;
import android.view.ViewGroup;

import com.benny.library.dynamicview.property.MarginProperty;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class PaddingSetter {
    public static final String PROPERTY = "padding";

    public void setPadding(View view, String padding) {
        MarginProperty property = MarginProperty.of(view.getContext(), padding);
        view.setPadding(property.left, property.top, property.right, property.bottom);
    }
}
