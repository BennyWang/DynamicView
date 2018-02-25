package com.benny.library.dynamicview.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.TextView;

import com.benny.library.dynamicview.annotations.DynamicView;
import com.benny.library.dynamicview.widget.property.GravityProperty;
import com.benny.library.dynamicview.view.ViewType;

@DynamicView
public class Text extends TextView implements ViewType.View {
    public Text(Context context) {
        super(context);
    }

    public void setText(String text) {
        super.setText(text);
    }

    public void setFontSize(String value) {
        setTextSize(Integer.parseInt(value));
    }

    public void setColor(String value) {
        setTextColor(Color.parseColor(value));
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
}
