package com.benny.library.dynamicview.view.setter;

import android.view.View;
import com.benny.library.dynamicview.widget.property.MarginProperty;

public class PaddingSetter {
    public static final String PROPERTY = "padding";

    public void setPadding(View view, String padding) {
        MarginProperty property = MarginProperty.of(view.getContext(), padding);
        view.setPadding(property.left, property.top, property.right, property.bottom);
    }
}
