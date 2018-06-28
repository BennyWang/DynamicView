package com.benny.library.dynamicview.view.setter;

import android.view.View;
import com.benny.library.dynamicview.view.property.MarginProperty;

public class PaddingSetter {
    public static final String PROPERTY = "padding";
    private static PaddingSetter instance;

    public static PaddingSetter getInstance() {
        if (instance == null) {
            instance = new PaddingSetter();
        }
        return instance;
    }

    public void setPadding(View view, String padding) {
        MarginProperty property = MarginProperty.of(view.getContext(), padding);
        view.setPadding(property.left, property.top, property.right, property.bottom);
    }
}
