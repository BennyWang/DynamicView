package com.benny.library.dynamicview.view.setter;

import android.view.View;

import com.benny.library.dynamicview.util.ResourceUtils;

public class BackgroundResSetter {
    public static final String PROPERTY = "backgroundRes";
    private static BackgroundResSetter instance;

    public static BackgroundResSetter getInstance() {
        if (instance == null) {
            instance = new BackgroundResSetter();
        }
        return instance;
    }

    public void setBackground(View view, String background) {
        if (background.startsWith("res://")) {
            int resourceId = ResourceUtils.getResourceIdByName(view.getContext(), background.substring(6));
            if (resourceId > 0) {
                view.setBackgroundResource(resourceId);
            }
        }
        else {
            throw new RuntimeException("unknown background res " + background);
        }
    }
}
