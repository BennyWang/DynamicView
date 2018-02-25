package com.benny.library.dynamicview.view.setter;

import android.view.View;

import com.benny.library.dynamicview.action.ActionProcessor;

public class OnClickActionSetter {
    public static final String PROPERTY = "onClick";

    public void setOnClickAction(final View view, final String tag, final ActionProcessor processor) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processor.processAction(view, tag);
            }
        });
    }
}
