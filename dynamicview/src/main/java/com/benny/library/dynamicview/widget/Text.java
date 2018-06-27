package com.benny.library.dynamicview.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.TextView;

import com.benny.library.dynamicview.annotations.DynamicView;
import com.benny.library.dynamicview.util.ViewUtils;
import com.benny.library.dynamicview.view.ThemeManager;
import com.benny.library.dynamicview.view.property.GravityProperty;
import com.benny.library.dynamicview.view.ViewType;

@DynamicView
public class Text extends TextView implements ViewType.View {
    private static SizeScaler scaler;

    public static void setScaler(SizeScaler scaler) {
        Text.scaler = scaler;
    }

    public Text(Context context) {
        super(context);
    }

    public void setText(String text) {
        if (!text.contentEquals(getText())) {
            super.setText(text);
        }
    }

    public void setFontSize(float size) {
        setTextSize(scaler == null ? size : scaler.scale(size));
    }

    public void setColor(String value) {
        int color = ViewUtils.getThemeColor(getContext(), ViewUtils.getThemeId(this));
        if (color > 0) {
            setTextColor(color);
        }
        else {
            setTextColor(Color.parseColor(value));
        }
    }

    public void setStyle(String value) {
        switch (value) {
            case "bold":
                setTypeface(getTypeface(), Typeface.BOLD);
                break;
            case "italic":
                setTypeface(getTypeface(), Typeface.ITALIC);
                break;
        }
    }

    public void setAlign(String value) {
        GravityProperty property = GravityProperty.of(getContext(), value);
        setGravity(property.gravity);
    }

    public interface SizeScaler {
        float scale(float size);
    }
}
