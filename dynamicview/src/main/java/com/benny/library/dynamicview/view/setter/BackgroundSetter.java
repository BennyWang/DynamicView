package com.benny.library.dynamicview.view.setter;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.view.View;

import com.benny.library.dynamicview.DynamicViewEngine;
import com.benny.library.dynamicview.util.ResourceUtils;
import com.benny.library.dynamicview.util.ViewUtils;
import com.benny.library.dynamicview.view.ThemeManager;
import com.benny.library.dynamicview.view.property.DrawableProperty;

public class BackgroundSetter {
    public static final String PROPERTY = "background";

    public void setBackground(View view, String background) {
        if (background.startsWith("res://")) {
            int resourceId = ResourceUtils.getResourceIdByName(view.getContext(), background.substring(6));
            if (resourceId > 0) {
                view.setBackgroundResource(resourceId);
            }
        }
        else {
            DrawableProperty property = DrawableProperty.of(view.getContext(), background);
            Drawable bgDrawable = property.getDrawable();

            int color = ViewUtils.getThemeColor(view.getContext(), ViewUtils.getThemeId(view));
            if (color > 0) {
                changeDrawableColor(bgDrawable, color);
            }

            view.setBackgroundDrawable(bgDrawable);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (bgDrawable instanceof ShapeDrawable) {
                    view.setClipToOutline(true);
                }
            }
        }
    }

    private void changeDrawableColor(Drawable drawable, int color) {
        if (drawable instanceof ColorDrawable) {
            ((ColorDrawable) drawable).setColor(color);
        } else if (drawable instanceof ShapeDrawable) {
            ((ShapeDrawable) drawable).getPaint().setColor(color);
        }
    }
}
