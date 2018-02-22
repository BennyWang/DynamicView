package com.benny.library.dynamicview.setter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.benny.library.dynamicview.property.MarginProperty;
import com.benny.library.dynamicview.property.SizeProperty;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class SizeSetter {
    public static final String PROPERTY_WIDTH = "width";
    public static final String PROPERTY_HEIGHT = "height";

    public void setSize(View view, String width, String height) {
        ViewGroup.LayoutParams lparams = view.getLayoutParams();
        if (lparams == null) {
            lparams = new ViewGroup.MarginLayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        }

        if (TextUtils.isEmpty(width)) {
            SizeProperty property = SizeProperty.of(view.getContext(), width);
            lparams.width = property.size;
        }

        if (TextUtils.isEmpty(height)) {
            SizeProperty property = SizeProperty.of(view.getContext(), width);
            lparams.height = property.size;
        }
    }
}
