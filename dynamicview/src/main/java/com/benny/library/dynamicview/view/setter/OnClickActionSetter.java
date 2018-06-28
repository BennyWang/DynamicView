package com.benny.library.dynamicview.view.setter;

import android.view.View;

import com.benny.library.dynamicview.action.ActionProcessor;

import org.json.JSONObject;

public class OnClickActionSetter {
    public static final String PROPERTY = "onClick";
    private static OnClickActionSetter instance;

    public static OnClickActionSetter getInstance() {
        if (instance == null) {
            instance = new OnClickActionSetter();
        }
        return instance;
    }

    public void setOnClickAction(final View view, final String tag, final JSONObject data, final ActionProcessor processor) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processor.processAction(view, tag, data);
            }
        });
    }
}
