package com.benny.library.dynamicview.view.setter;

import android.view.View;

import com.benny.library.dynamicview.api.ActionProcessor;

import org.json.JSONObject;

public class OnLongClickActionSetter {
    public static final String PROPERTY = "onLongClick";
    private static OnLongClickActionSetter instance;

    public static OnLongClickActionSetter getInstance() {
        if (instance == null) {
            instance = new OnLongClickActionSetter();
        }
        return instance;
    }

    public void setOnLongClickAction(final View view, final String tag, final JSONObject data, final ActionProcessor processor) {
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                processor.processAction(view, tag, data);
                return true;
            }
        });
    }
}
