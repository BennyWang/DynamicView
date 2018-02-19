package com.benny.library.dynamicview.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import com.benny.library.dynamicview.property.MarginProperty;

import static android.widget.LinearLayout.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.MarginLayoutParams;

public abstract class DynamicViewBuilder {
    public static final String PROP_MARGIN = "margin";
    public static final String PROP_PADDING = "padding";
    public static final String PROP_BACKGROUND = "background";

    protected View view;

    public boolean setProperty(String key, String value) {
        if (PROP_MARGIN.equals(key)) {
            setMargin(value);
            return true;
        }
        else if (PROP_PADDING.equals(key)) {
            setPadding(value);
            return true;
        }
        return false;
    }

    public View getView() {
        return view;
    }

    protected void setMargin(String value) {
        MarginLayoutParams lparams = (MarginLayoutParams) view.getLayoutParams();
        if (lparams == null) {
            lparams = new MarginLayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        }
        MarginProperty property = MarginProperty.of(value);
        lparams.setMargins(property.left, property.top, property.right, property.bottom);
        view.setLayoutParams(lparams);
    }

    private void setPadding(String value) {
        MarginProperty property = MarginProperty.of(value);
        view.setPadding(property.left, property.top, property.right, property.bottom);
    }

    private void setBackground(String color) {
        view.setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));
    }
}
