package com.benny.library.dynamicview.view.setter;

import android.view.View;
import android.view.ViewGroup;
import com.benny.library.dynamicview.view.property.SizeProperty;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class SizeSetter {
    public static final String PROPERTY = "size";
    private static SizeSetter instance;

    public static SizeSetter getInstance() {
        if (instance == null) {
            instance = new SizeSetter();
        }
        return instance;
    }

    public void setSize(View view, String value) {
        ViewGroup.LayoutParams lparams = view.getLayoutParams();
        if (lparams == null) {
            lparams = new ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
            view.setLayoutParams(lparams);
        }

        SizeProperty property = SizeProperty.of(view.getContext(), value);
        lparams.width = property.width;
        lparams.height = property.height;
    }
}
