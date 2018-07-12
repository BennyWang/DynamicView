package com.benny.library.dynamicview.view.setter;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.view.View;

import com.benny.library.dynamicview.view.property.DrawableProperty;

public class BackgroundSetter {
    public static final String PROPERTY = "background";
    private static BackgroundSetter instance;

    public static BackgroundSetter getInstance() {
        if (instance == null) {
            instance = new BackgroundSetter();
        }
        return instance;
    }

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
