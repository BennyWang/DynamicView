package com.benny.library.dynamicview.setter;

import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.view.View;
import com.benny.library.dynamicview.property.BackgroundProperty;


public class BackgroundSetter {
    public static final String PROPERTY = "background";

    public void setBackground(View view, String background) {
        BackgroundProperty property = BackgroundProperty.of(view.getContext(), background);
        view.setBackgroundDrawable(property.background);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (property.background instanceof ShapeDrawable) {
                view.setClipToOutline(true);
            }
        }
    }
}
