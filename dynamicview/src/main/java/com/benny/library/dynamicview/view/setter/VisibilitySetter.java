package com.benny.library.dynamicview.view.setter;

import android.view.View;

public class VisibilitySetter {
    public static final String PROPERTY = "visibility";
    private static VisibilitySetter instance;

    public static VisibilitySetter getInstance() {
        if (instance == null) {
            instance = new VisibilitySetter();
        }
        return instance;
    }

    public void setVisibility(View view, String value) {
        switch (value) {
            case "visible":
                view.setVisibility(View.VISIBLE);
                break;
            case "hidden":
                view.setVisibility(View.INVISIBLE);
                break;
            case "gone":
                view.setVisibility(View.GONE);
        }
    }
}
