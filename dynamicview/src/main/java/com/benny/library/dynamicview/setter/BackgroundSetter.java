package com.benny.library.dynamicview.setter;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import com.benny.library.dynamicview.property.MarginProperty;

public class BackgroundSetter {
    public static final String PROPERTY = "background";

    public void setBackground(View view, String color) {
        view.setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));
    }
}
