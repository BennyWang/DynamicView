package com.benny.library.dynamicview.setter;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.view.View;
import com.benny.library.dynamicview.property.DrawableProperty;


public class BackgroundSetter {
    public static final String PROPERTY = "background";

    public void setBackground(View view, String background) {
        DrawableProperty property = DrawableProperty.of(view.getContext(), background);
        Drawable bgDrawable = property.getDrawable();
        view.setBackgroundDrawable(bgDrawable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (bgDrawable instanceof ShapeDrawable) {
                view.setClipToOutline(true);
            }
        }
    }
}
